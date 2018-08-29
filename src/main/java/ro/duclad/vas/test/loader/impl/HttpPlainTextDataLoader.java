package ro.duclad.vas.test.loader.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import ro.duclad.vas.test.loader.DataLoader;
import ro.duclad.vas.test.loader.DataLoadingException;
import ro.duclad.vas.test.loader.model.Record;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads the data from an http plain text response
 */
@Service
@Slf4j
public class HttpPlainTextDataLoader implements DataLoader {
    private final RestTemplate restTemplate;

    public HttpPlainTextDataLoader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Record> loadData(String date) {
        String rawData;
        try {
            rawData = restTemplate.getForObject("/MCP_{date}.json", String.class, date);
        } catch (Exception e) {
            throw new DataLoadingException("unable to access the raw data", e);
        }
        if (!StringUtils.isEmpty(rawData)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.stream(rawData.split("\\r?\\n")).map(s -> {
                try {
                    log.debug("parsing json string:" + s);
                    return objectMapper.readValue(new StringReader(s), Record.class);
                } catch (IOException e) {
                    throw new DataLoadingException("unable to parse the raw data!", e);
                }
            }).collect(Collectors.toList());
        }
        throw new DataLoadingException("no data found!");
    }
}
