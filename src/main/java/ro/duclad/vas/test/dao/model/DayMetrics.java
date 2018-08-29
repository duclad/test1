package ro.duclad.vas.test.dao.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * Contains metrics information for the data captured for a day.
 */
@Getter
@RequiredArgsConstructor
public class DayMetrics {
    @NonNull
    private String date;
    private Long rowsWithMissingInfo = 0L;
    private Long rowsWithEmptyContent = 0L;
    private Long rowsWithFieldsErrors = 0L;
    private Map<Integer, Long> callsNumberByOriginCountry = new HashMap<>();
    private Map<Integer, Long> callsNumberByDestinationCountry = new HashMap<>();
    private Map<Integer, Long> callDurationByOriginCountry = new HashMap<>();
    private Map<Integer, Long> callDurationByDestinationCountry = new HashMap<>();
    private Map<String, Long> wordsRanking = new HashMap<>();

    public synchronized void incrementRowsWithMissingInfo() {
        rowsWithMissingInfo++;
    }

    public synchronized void incrementRowsWithEmptyContent() {
        rowsWithEmptyContent++;
    }

    public synchronized void incrementRowsWithFieldsErrors() {
        rowsWithFieldsErrors++;
    }

}
