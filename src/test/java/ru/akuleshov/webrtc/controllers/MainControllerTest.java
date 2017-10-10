package ru.akuleshov.webrtc.controllers;

import com.google.common.base.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class MainControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void portTest() throws Exception {
        assertTrue(port == 8080); // ! if you want to change port, u must change it with chat.js too.
    }


    @Test
    public void indexPageLoad() throws Exception {
        ResponseEntity<String> channelsEntity = this.restTemplate.getForEntity( "/", String.class);
        String content = channelsEntity.getBody();
        int statusCodeValue = channelsEntity.getStatusCodeValue();

        assertTrue(!Strings.isNullOrEmpty(content) && statusCodeValue==200 && content.length() > 0);
    }
}