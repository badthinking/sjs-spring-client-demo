package com.github.c_mehring.sjs.client;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("input")
public class WordCountJobData {

    private String string;

    public WordCountJobData(String string) {
        this.string = string;
    }

    public WordCountJobData() {
        super();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}
