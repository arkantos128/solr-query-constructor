package ru.arkantos.solr.limit;

import ru.arkantos.solr.limit.model.FacetResult;

import java.util.Map;

public interface SolrFacet {

    FacetResult getFacetData(String json) throws SolrFacetException;

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
