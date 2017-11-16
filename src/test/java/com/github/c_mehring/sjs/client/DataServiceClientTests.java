package com.github.c_mehring.sjs.client;

import com.github.c_mehring.sjs.client.model.DataUploadResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceClientTests {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceClientTests.class);

    @Autowired
    private DataServiceClient client;

    @Test
    public void uploadFile() {

        ClassPathResource file = new ClassPathResource("com/github/c_mehring/sjs/client/file.dat");

        Assert.assertTrue("Does not exist: " + file, file.exists());

        DataUploadResult filenameOnServer = client.upload(file, "testfile.dat");

        Assert.assertNotNull(filenameOnServer);
        Assert.assertTrue("Name does not include testfile.dat: " + filenameOnServer,
                filenameOnServer.getResult().getFilename().contains("testfile.dat"));

    }

    @Test
    public void uploadReadDelete() {
        ClassPathResource file = new ClassPathResource("com/github/c_mehring/sjs/client/file.dat");

        String uploadFilePrefix = "uploadReadDelete.dat";
        DataUploadResult uploadName = client.upload(file, uploadFilePrefix);

        Assert.assertNotEquals("", uploadName);

        client.deleteFile(Collections.singletonList(uploadName.getResult().getFilename()));

        List<String> filesAfterDeletion = client.readData();

        List<String> filesWithUploadName = filesAfterDeletion.stream()
                .filter(f -> f.contains(uploadFilePrefix))
                .collect(Collectors.toList());

        Assert.assertEquals("All files with " + uploadFilePrefix + " should be deleted",
                0, filesWithUploadName.size());

    }

}
