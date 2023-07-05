package ru.arkantos.solr.limit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultSolrPivot implements SolrPivot {

    private final static String PIVOT_FIELD_PATH = "/responseHeader/params/facet.pivot";
    private final static String PIVOT_DATA_FORMAT_PATH = "/facet_counts/facet_pivot/%s";

    private final ObjectMapper objectMapper;
    private Map<String, String> keyReplacerMap;
    private String defaultReplacerValue;

    public DefaultSolrPivot(ObjectMapper objectMapper, Map<String, String> keyReplacerMap, String defaultReplacerValue) {
        this.objectMapper = objectMapper;
        this.keyReplacerMap = keyReplacerMap;
        this.defaultReplacerValue = defaultReplacerValue;
    }

    @Override
    public Map<String, Map<String, Integer>> getPivotData(String json) throws SolrFacetException {
        try {
            JsonNode pivotDataNode = getPivotDataNode(json);
            Map<String, Map<String, Integer>> facetDataMap = new HashMap<>();
            for (var recordNode : pivotDataNode) {
                Map<String, Integer> pivotMap = new HashMap<>();

                JsonNode pivots = recordNode.get("pivot");
                for (var pivot : pivots) {
                    String value = getPivotValue(pivot);
                    int count = pivot.get("count").asInt();
                    pivotMap.put(value, count);
                }
                facetDataMap.put(recordNode.get("value").asText(), pivotMap);
            }

            return facetDataMap;
        } catch (Exception ex) {
            throw new SolrFacetException("parse facet error", ex);
        }

    }

    private JsonNode getPivotDataNode(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String pivotField = jsonNode.at(PIVOT_FIELD_PATH).asText();
        String path = String.format(PIVOT_DATA_FORMAT_PATH, pivotField);
        return jsonNode.at(path);
    }

    private String getPivotValue(JsonNode pivotNode) {
        String value = pivotNode.get("value").asText().toLowerCase();
        if (keyReplacerMap == null || keyReplacerMap.isEmpty()) {
            return value;
        }
        return keyReplacerMap.getOrDefault(value, defaultReplacerValue);
    }
}
