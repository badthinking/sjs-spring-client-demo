package com.github.c_mehring.sjs.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Call variants for commandline
 * <pre>
 *     Main class
 *     com.github.c_mehring.sjs.client.{@link DemoApplication}
 *
 *     A) list files available @ /data
 *        --queryData
 *
 *     B) deleteFile(s) available @ /data
 *        "--deleteData=[FILENAME]"
 *
 *     C) run WordCountApplication from jobserver examples
 *        "--countWords=a b c see d a c"
 *
 *
 * </pre>
 */
@SpringBootApplication
public class DemoApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    private WordCountJobClient wordCountJobClient;

    @Autowired
    private DataServiceClient dataServiceClient;

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ApplicationRunner runDataQuery() {
        return args -> {
            if (args.containsOption("queryData")) {
                List<String> data = dataServiceClient.readData();
                LOG.info("{} result(s) from Service: {}", data.size(), data);
            } else if (args.containsOption("deleteData")) {
                dataServiceClient.deleteFile(args.getOptionValues("deleteData"));
            } else if (args.containsOption("countWords")) {

                String text = args
                        .getOptionValues("countWords")
                        .stream()
                        .collect(Collectors.joining(" "));

                Map<String, Integer> result = wordCountJobClient.wordCount(text);

                LOG.info("Text '{}' contains {} words", text, result);

            }
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
