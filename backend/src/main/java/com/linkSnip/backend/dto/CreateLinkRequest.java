package com.linkSnip.backend.dto;

public class CreateLinkRequest {

    private String url;
    private String customAlias;
    private Integer ttlValue;
    private String ttlUnit;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public Integer getTtlValue() {
        return ttlValue;
    }

    public void setTtlValue(Integer ttlValue) {
        this.ttlValue = ttlValue;
    }

    public String getTtlUnit() {
        return ttlUnit;
    }

    public void setTtlUnit(String ttlUnit) {
        this.ttlUnit = ttlUnit;
    }
}