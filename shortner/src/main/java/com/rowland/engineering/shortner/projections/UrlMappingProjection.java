package com.rowland.engineering.shortner.projections;

public interface UrlMappingProjection {
    String getShortUrl();
    String getOriginalUrl();
    long getClickCount();
}
