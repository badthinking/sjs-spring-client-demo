package com.github.c_mehring.sjs.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QueryService {

    private static final Logger LOG = LoggerFactory.getLogger(QueryService.class);

    @Value("${jobserver.data.url:http://localhost:8090/data/}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public List<String> readData() {
        LOG.info("Start reading data from {}", url);
        return restTemplate.getForObject(this.url, List.class);
    }
}
