package ro.duclad.vas.test.service;

import org.springframework.stereotype.Service;
import ro.duclad.vas.test.ApplicationProperties;
import ro.duclad.vas.test.dao.ApplicationDao;
import ro.duclad.vas.test.dao.model.DayMetrics;
import ro.duclad.vas.test.dao.model.ServiceKips;
import ro.duclad.vas.test.loader.DataLoader;
import ro.duclad.vas.test.loader.model.RecordConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApplicationService {


    private final ApplicationDao applicationDao;
    private final DataLoader dataLoader;
    private ApplicationProperties applicationProperties;

    public ApplicationService(DataLoader dataLoader, ApplicationDao applicationDao, ApplicationProperties applicationProperties) {
        this.applicationDao = applicationDao;
        this.dataLoader = dataLoader;
        this.applicationProperties = applicationProperties;
    }

    public Optional<DayMetrics> getDayMetrics(String date) {
        if (!applicationDao.getDayMetrics(date).isPresent()) {
            ServiceKips serviceKips = applicationDao.getServiceKips();
            DayMetrics dayMetrics = new DayMetrics(date);
            List<String> words = Arrays.stream(applicationProperties.getWords().split(",")).collect(Collectors.toList());
            dayMetrics.getWordsRanking().putAll(words.stream().collect(Collectors.toMap(Function.identity(), w -> 0L)));
            long startTime = System.currentTimeMillis();
            dataLoader.loadData(date).forEach(record -> {
                serviceKips.incrementNumberOfRows();
                if (record.hasMissingFields()) {
                    dayMetrics.incrementRowsWithMissingInfo();
                } else if (record.hasFieldsErrors()) {
                    dayMetrics.incrementRowsWithFieldsErrors();
                } else if (record.hasEmptyMessageContent()) {
                    dayMetrics.incrementRowsWithEmptyContent();
                } else {
                    dayMetrics.getCallsNumberByOriginCountry().merge(record.getOriginCountryCode(), 1L, Long::sum);
                    dayMetrics.getCallsNumberByDestinationCountry().merge(record.getDestinationCountryCode(), 1L, Long::sum);
                    serviceKips.getOriginCountries().add(record.getOriginCountryCode());
                    serviceKips.getDestinationCountries().add(record.getDestinationCountryCode());
                    if (RecordConstants.CALL_TYPE.equalsIgnoreCase(record.getMessageType())) {
                        dayMetrics.getCallDurationByOriginCountry().merge(record.getOriginCountryCode(), record.getDuration(), Long::sum);
                        dayMetrics.getCallDurationByDestinationCountry().merge(record.getDestinationCountryCode(), record.getDuration(), Long::sum);
                        serviceKips.incrementNumberOfCalls();
                    } else {
                        words.forEach(s -> {
                            if (record.getMessageContent().toLowerCase().contains(s.toLowerCase())) {
                                dayMetrics.getWordsRanking().merge(s, 1L, Long::sum);
                            }
                        });
                        serviceKips.incrementNumberOfMessages();
                    }
                }
            });
            serviceKips.getFilesProcessingTime().put(date, new Long(System.currentTimeMillis() - startTime));
            applicationDao.updateServiceKips(serviceKips);
            applicationDao.insertDayMetrics(dayMetrics);
        }
        return applicationDao.getDayMetrics(date);
    }

    public ServiceKips getServiceKips() {
        return applicationDao.getServiceKips();
    }

}
