package ru.arkantos.solr.params;

import java.util.Collections;
import java.util.Map;

public record Container(Map<String, Object> params) {
    public Container(Map<String, Object> params) {
        this.params = Collections.unmodifiableMap(params);
    }
}
