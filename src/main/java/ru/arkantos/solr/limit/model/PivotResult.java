package ru.arkantos.solr.limit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PivotResult {
    private List<PivotNode> pivotList;

    public Map<String, Object> toMap() {
        if (this.pivotList == null || this.pivotList.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        for (var p : this.pivotList) {
            map.putAll(p.toMap());
        }
        return map;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PivotNode {
        private String field;
        private String value;
        private Long count;
        private List<PivotNode> pivot;

        public Map<String, Object> toMap() {
            if (this.pivot == null || this.pivot.isEmpty()) {
                return Map.of(this.value, this.count);
            }
            Map<String, Object> map = new HashMap<>();
            for (var p : this.pivot) {
                map.putAll(p.toMap());
            }
            return Map.of(this.value, map);
        }
    }
}
