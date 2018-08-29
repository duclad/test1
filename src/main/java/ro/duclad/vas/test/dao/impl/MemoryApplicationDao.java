package ro.duclad.vas.test.dao.impl;

import org.springframework.stereotype.Repository;
import ro.duclad.vas.test.dao.ApplicationDao;
import ro.duclad.vas.test.dao.model.DayMetrics;
import ro.duclad.vas.test.dao.model.ServiceKips;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The application data is kept in memory
 */
@Repository
public class MemoryApplicationDao implements ApplicationDao {

    private Set<DayMetrics> dayMetricsCollections = new HashSet<>();

    private ServiceKips serviceKips = new ServiceKips();

    @Override
    public void insertDayMetrics(DayMetrics dayMetrics) {
        if (dayMetrics == null) {
            throw new IllegalArgumentException();
        }
        dayMetricsCollections.add(dayMetrics);
    }

    @Override
    public Optional<DayMetrics> getDayMetrics(String date) {
        return dayMetricsCollections.stream().filter(dayMetrics -> dayMetrics.getDate().equalsIgnoreCase(date)).findFirst();
    }

    @Override
    public void updateServiceKips(ServiceKips serviceKips) {
        this.serviceKips = serviceKips;
    }

    @Override
    public ServiceKips getServiceKips() {
        return serviceKips;
    }
}
