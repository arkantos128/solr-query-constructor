package ru.arkantos.solr.limit;

import java.util.Map;

public interface SolrFacet {

    Integer getFacetLimit();

    String getOtherFieldName();

    Map<String, Integer> getFacetData(String json) throws SolrFacetException;

    Map<String, Integer> limit(Map<String, Integer> dataMap);

    Map<String, Integer> limitAndAddOtherField(Map<String, Integer> dataMap);

    static Map<String, Object> getFacetRequestMap(String query, String field) {
        return Map.of(
                "q", query,
                "facet", true,
                "facet.mincount", 1,
                "facet.field", field,
                "rows", 0
        );
    }
}
