package ru.arkantos.solr.limit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.arkantos.solr.limit.model.FacetResult;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultSolrFacet implements SolrFacet {

    private final static String FACET_FIELD_PATH = "/responseHeader/params/facet.field";
    private final static String FACET_DATA_FORMAT_PATH = "/facet_counts/facet_fields/%s";

    private final ObjectMapper objectMapper;

    @Override
    public FacetResult getFacetData(String json) throws SolrFacetException {
        try {
            JsonNode facetDataNode = getFacetDataNode(json);
            Map<String, Long> facetData = new LinkedHashMap<>();
            for (int i = 0; i < facetDataNode.size(); i += 2) {
                facetData.put(facetDataNode.get(i).asText(), facetDataNode.get(i + 1).asLong());
            }
            return new FacetResult(facetData);
        } catch (Exception ex) {
            throw new SolrFacetException("parse facet error", ex);
        }
    }

    private JsonNode getFacetDataNode(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String facetField = jsonNode.at(FACET_FIELD_PATH).asText();
        String path = String.format(FACET_DATA_FORMAT_PATH, facetField);
        return jsonNode.at(path);
    }
}
