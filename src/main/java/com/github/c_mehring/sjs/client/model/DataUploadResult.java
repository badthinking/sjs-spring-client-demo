
package com.github.c_mehring.sjs.client.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * thanks to http://www.jsonschema2pojo.org for generating the classes
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataUploadResult {

    @JsonProperty("result")
    private DataUploadFileResult result;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("result")
    public DataUploadFileResult getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(DataUploadFileResult result) {
        this.result = result;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
