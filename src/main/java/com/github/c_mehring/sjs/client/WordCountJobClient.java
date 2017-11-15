package com.github.c_mehring.sjs.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordCountJobClient {

    private static final Logger LOG = LoggerFactory.getLogger(QueryService.class);

    @Value("${jobserver.jobs.url:http://localhost:8090/jobs}")
    private String jobsBaseUrl;

    @Autowired
    private RestTemplate restTemplate;


    public Map<String, Integer> wordCount(String text) {

        final URI requestUri = UriComponentsBuilder.fromHttpUrl(jobsBaseUrl)
                .queryParam("appName", "test")
                .queryParam("classPath", "spark.jobserver.WordCountExampleNewApi")
                .queryParam("sync", "true")
                .build().encode().toUri();

        Map<String, Object> jobData = new HashMap<>();
        Map<String, String> stringData = new HashMap<>();
        stringData.put("string", text);
        jobData.put("input", stringData);

        Map<String, Map<String, Integer>> result = restTemplate.postForObject(requestUri, jobData, Map.class);

        LOG.info("Retrieved from service {}", result);

        return result.get("result");
    }

}
