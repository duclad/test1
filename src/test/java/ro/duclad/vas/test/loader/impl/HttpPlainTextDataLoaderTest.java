package ro.duclad.vas.test.loader.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ro.duclad.vas.test.loader.model.Record;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpPlainTextDataLoaderTest {

    @MockBean
    private RestTemplate restTemplate;

    private HttpPlainTextDataLoader dataLoader;

    private String testJson = "{\"message_type\": \"CALL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": 34969000101,\"duration\": 120,\"status_code\": \"OK\",\"status_description\": \"OK\"}\n" +
            "{\"message_type\": \"NULL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": 34969000101,\"status_code\": \"OK\",\"status_description\": \"OK\"}";

    private String testDate = "20180101";

    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(testJson).when(restTemplate).getForObject(eq("/MCP_{date}.json"), eq(String.class), eq(testDate));
        dataLoader = new HttpPlainTextDataLoader(restTemplate);
    }

    @Test
    public void loadData() {
        assertEquals(2, dataLoader.loadData(testDate).size());
    }
}