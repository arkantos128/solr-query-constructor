package ru.arkantos.solr.limit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class FacetResult {
    private Map<String, Long> facetMap;

    public FacetResult limit(int limit) {
        int index = 0;
        Map<String, Long> resMap = new LinkedHashMap<>();
        for (var entry : this.facetMap.entrySet()) {
            if (index < limit) {
                resMap.put(entry.getKey(), entry.getValue());
            } else {
                break;
            }
            index++;
        }
        return new FacetResult(resMap);
    }

    public FacetResult limitAndAddOtherField(int limit, String otherFieldName) {
        int index = 0;
        long otherCount = 0;
        Map<String, Long> resMap = new LinkedHashMap<>();
        for (var entry : this.facetMap.entrySet()) {
            if (index < limit) {
                resMap.put(entry.getKey(), entry.getValue());
            } else {
                otherCount += entry.getValue();
            }
            index++;
        }
        if (otherCount != 0) {
            resMap.put(otherFieldName, otherCount);
        }
        return new FacetResult(resMap);
    }
}
