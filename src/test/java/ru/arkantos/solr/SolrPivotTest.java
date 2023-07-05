package ru.arkantos.solr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.limit.DefaultSolrPivot;
import ru.arkantos.solr.limit.SolrFacetException;
import ru.arkantos.solr.limit.SolrPivot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class SolrPivotTest {

    static SolrPivot pivotSolrFacet;
    static Map<String, Map<String, Integer>> correctResult;

    @BeforeAll
    static void beforeAll() {
        Map<String, String> replacerMap = Map.of(
                "replace_one", "rep_o",
                "replace_two", "rep_t"
        );
        pivotSolrFacet = new DefaultSolrPivot(new ObjectMapper(), replacerMap, "oth");

        correctResult = Map.of(
                "A", Map.of("rep_o", 95, "oth", 2, "rep_t", 1),
                "U", Map.of("rep_o", 24, "oth", 2),
                "G", Map.of("rep_o", 127, "rep_t", 58, "oth", 8),
                "M", Map.of("rep_o", 67, "rep_t", 24, "oth", 18)
        );
    }

    @Test
    void testPivot() throws IOException, SolrFacetException {
        String path = this.getClass().getResource("/pivot-test-data.json").getPath();
        String solrResponse = Files.readString(Paths.get(path));
        var parseResult = pivotSolrFacet.getPivotData(solrResponse);
        Assertions.assertEquals(correctResult, parseResult);
    }

}
