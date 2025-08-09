package com.example.demo.dto.common;

public class StorageOutput {

    private final String key;
    private final String url;
    private final String mimeType;

    public StorageOutput(String key, String url, String mimeType) {
        this.key = key;
        this.mimeType = mimeType;
        this.url = url;
    }

    public String getKey() {
        return this.key;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMimeType() {
        return this.mimeType;
    }

}
