package ro.duclad.vas.test.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * contains kips information for the entire application
 */
@Data
public class ServiceKips {
    private Long numberOfRows = 0L;
    private Long numberOfCalls = 0L;
    private Long numberOfMessages = 0L;
    @JsonIgnore
    private Set<Integer> originCountries = new HashSet<>();
    @JsonIgnore
    private Set<Integer> destinationCountries = new HashSet<>();
    private Map<String, Long> filesProcessingTime = new HashMap<>();

    public int getNumberOfOriginCountries() {
        return originCountries.size();
    }

    public int getNumberOfDestinationCountries() {
        return destinationCountries.size();
    }

    public int getNumberOfProcessedFiles() {
        return filesProcessingTime.size();
    }

    public void incrementNumberOfRows() {
        numberOfRows++;
    }

    public void incrementNumberOfMessages() {
        numberOfMessages++;
    }

    public void incrementNumberOfCalls() {
        numberOfCalls++;
    }


}
