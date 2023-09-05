package ru.arkantos.solr.limit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.arkantos.solr.limit.model.PivotResult;
import ru.arkantos.solr.limit.model.PivotResult.PivotNode;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DefaultSolrPivot implements SolrPivot {

    private final static String PIVOT_FIELD_PATH = "/responseHeader/params/facet.pivot";
    private final static String PIVOT_DATA_FORMAT_PATH = "/facet_counts/facet_pivot/%s";

    private final ObjectMapper objectMapper;

    @Override
    public PivotResult getPivotData(String json) throws SolrFacetException {
        try {
            JsonNode pivotDataNode = getPivotDataNode(json);
            List<PivotNode> pivotList = getPivotList(pivotDataNode);
            return new PivotResult(pivotList);
        } catch (Exception ex) {
            throw new SolrFacetException("parse facet error", ex);
        }

    }

    private List<PivotNode> getPivotList(JsonNode pivotDataNode) {
        List<PivotNode> pivots = new ArrayList<>();
        for (var node : pivotDataNode) {
            String field = node.get("field").asText();
            String value = node.get("value").asText();
            long count = node.get("count").asLong(0);
            List<PivotNode> innerNodes = null;
            if (node.hasNonNull("pivot")) {
                innerNodes = getPivotList(node.get("pivot"));
            }
            PivotNode pivotNode = new PivotNode(field, value, count, innerNodes);
            pivots.add(pivotNode);
        }
        return pivots;
    }

    private JsonNode getPivotDataNode(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String pivotField = jsonNode.at(PIVOT_FIELD_PATH).asText();
        String path = String.format(PIVOT_DATA_FORMAT_PATH, pivotField);
        return jsonNode.at(path);
    }
}
