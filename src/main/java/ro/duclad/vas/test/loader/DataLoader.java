package ro.duclad.vas.test.loader;

import ro.duclad.vas.test.loader.model.Record;

import java.util.List;

/**
 * Loads the raw data from external system
 */
public interface DataLoader {

    /**
     * Load the data for a specific date
     *
     * @param date
     * @return A list of records from parsing the raw data
     * @throws DataLoadingException - if any exception prevents the loading of the data, or if there is no data to load
     */
    List<Record> loadData(String date);
}
