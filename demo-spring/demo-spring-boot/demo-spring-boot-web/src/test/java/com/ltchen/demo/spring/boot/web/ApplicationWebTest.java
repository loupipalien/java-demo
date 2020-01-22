package com.ltchen.demo.spring.boot.web;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author: 01139983
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ApplicationWebTest {

    @LocalServerPort
    private int port;

    @Value("${local.management.port}")
    private int managementPort;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void shouldReturn200WhenSendingRequestToController() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = this.template.getForEntity("http://localhost:" + this.port + "/rest/greeting", Map.class);
        BDDAssertions.then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = this.template.getForEntity("http://localhost:" + this.managementPort + "/actuator/info", Map.class);
        BDDAssertions.then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        Assertions.assertThat(this.template.getForObject("http://localhost:" + this.port + "/rest/greeting", String.class)).contains("Hello World");
    }

}
