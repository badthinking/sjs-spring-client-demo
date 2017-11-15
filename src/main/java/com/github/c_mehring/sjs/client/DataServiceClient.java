package com.github.c_mehring.sjs.client;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Service class to work with the /data API of spark jobserver
 * 
 * @author cmehring
 *
 */
@Service
@SuppressWarnings( "nls" )
public class DataServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger( DataServiceClient.class );

    @Value( "${jobserver.data.url:http://localhost:8090/data/}" )
    private String dataUrl;

    @Autowired
    private RestTemplate        restTemplate;

    /** reads the list of all files listed by /data API */
    public List<String> readData() {
        LOG.info( "Start reading data from {}", this.dataUrl );
        return this.restTemplate.getForObject( this.dataUrl, List.class );
    }

    /**
     * Deletes files from jobservers /data.
     *
     * @param rawFilenames where one windows compatible name would be like this one:
     *            <code>C:&#92;temp&#92;spark-jobserver&#92;upload&#92;environm.txt-2017-11-15T17_07_55.437+01_00.dat</code>.
     *            Escaped bashslashes will be normalized for convenience reasons, so one can copy the windows name
     *            directly from the list of files.
     */
    public void deleteFile( final List<String> rawFilenames ) {
        rawFilenames.stream()
                .map( DataServiceClient::normalizeWindowsFilename )
                .map( this::buildDeleteUri )
                // TODO error handling in case one of the files is not deletable (HTTP 400)
                .forEach( this::executeDeleteRequest );
    }

    private void executeDeleteRequest( final String deleteUri ) {
        LOG.debug( "sending delete request to: {}", deleteUri );
        try {
            this.restTemplate.delete( deleteUri );
        } catch ( final HttpClientErrorException ex ) {
            LOG.error( "{}", ex.getResponseHeaders() );
            LOG.error( "{}", ex.getResponseBodyAsString(), ex );
        }
    }

    private String buildDeleteUri( final String jobserverFilename ) {
        final String baseUriComponent = UriComponentsBuilder.fromHttpUrl( this.dataUrl).build().encode()
                .toUriString();
        return baseUriComponent + jobserverFilename;
    }

    /**
     * this is the big magic trick when you're copying the filename directly as listed at "/data" endpoint
     */
    private static String normalizeWindowsFilename( final String filename ) {
        return filename.replace( "\\\\", "\\" );
    }
}
