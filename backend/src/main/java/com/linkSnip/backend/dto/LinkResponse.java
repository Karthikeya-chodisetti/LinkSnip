package com.linkSnip.backend.dto;

public class LinkResponse {

    private String shortUrl;

    public LinkResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}