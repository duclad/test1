package ro.duclad.vas.test.dao;

import org.springframework.stereotype.Repository;
import ro.duclad.vas.test.dao.model.DayMetrics;
import ro.duclad.vas.test.dao.model.ServiceKips;

import java.util.Optional;

/**
 * Handle persistence of the application data
 */
public interface ApplicationDao {

    /**
     * Persist the information for the a day metrics
     * @param dayMetrics
     */
    void insertDayMetrics(DayMetrics dayMetrics);

    Optional<DayMetrics> getDayMetrics(String date);

    void updateServiceKips(ServiceKips serviceKips);

    ServiceKips getServiceKips();
}
