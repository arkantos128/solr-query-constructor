package ru.arkantos.solr.limit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultSolrFacet implements SolrFacet {

    private final static String FACET_FIELD_PATH = "/responseHeader/params/facet.field";
    private final static String FACET_DATA_FORMAT_PATH = "/facet_counts/facet_fields/%s";

    private final ObjectMapper objectMapper;

    @Getter
    @Setter
    private Integer facetLimit = 3;
    @Getter
    @Setter
    private String otherFieldName = "other";

    @Override
    public Map<String, Integer> getFacetData(String json) throws SolrFacetException {
        try {
            JsonNode facetDataNode = getFacetDataNode(json);
            Map<String, Integer> facetData = new LinkedHashMap<>();
            for (int i = 0; i < facetDataNode.size(); i += 2) {
                facetData.put(facetDataNode.get(i).asText(), facetDataNode.get(i + 1).asInt());
            }
            return facetData;
        } catch (Exception ex) {
            throw new SolrFacetException("parse facet error", ex);
        }
    }

    @Override
    public Map<String, Integer> limit(Map<String, Integer> dataMap) {
        int index = 0;
        Map<String, Integer> resMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
            if (index < facetLimit) {
                resMap.put(entry.getKey(), entry.getValue());
            } else {
                break;
            }
            index++;
        }
        return resMap;
    }

    @Override
    public Map<String, Integer> limitAndAddOtherField(Map<String, Integer> dataMap) {
        int index = 0, otherCount = 0;
        Map<String, Integer> resMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
            if (index < facetLimit) {
                resMap.put(entry.getKey(), entry.getValue());
            } else {
                otherCount += entry.getValue();
            }
            index++;
        }
        if (otherCount != 0) {
            resMap.put(otherFieldName, otherCount);
        }
        return resMap;
    }

    private JsonNode getFacetDataNode(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String facetField = jsonNode.at(FACET_FIELD_PATH).asText();
        String path = String.format(FACET_DATA_FORMAT_PATH, facetField);
        return jsonNode.at(path);
    }
}
