package ru.arkantos.solr.limit;

import ru.arkantos.solr.limit.model.PivotResult;

import java.util.Map;

public interface SolrPivot {

    PivotResult getPivotData(String json) throws SolrFacetException;

    static Map<String, Object> getPivotRequestMap(String query, String fields) {
        return Map.of(
                "q", query,
                "facet", true,
                "facet.pivot.mincount", 1,
                "facet.pivot", fields,
                "rows", 0
        );
    }
}
