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
    static Map<String, Map<String, Long>> correctResult;

    @BeforeAll
    static void beforeAll() {
        pivotSolrFacet = new DefaultSolrPivot(new ObjectMapper());

        correctResult = Map.of(
                "A", Map.of("one", 95L, "oth", 2L, "two", 1L),
                "U", Map.of("one", 24L, "oth", 2L),
                "G", Map.of("one", 127L, "two", 58L, "oth", 18L),
                "M", Map.of("one", 67L, "two", 24L, "oth", 18L)
        );
    }

    @Test
    void testPivot() throws IOException, SolrFacetException {
        String path = this.getClass().getResource("/pivot-test-data.json").getPath();
        String solrResponse = Files.readString(Paths.get(path));
        var parseResult = pivotSolrFacet.getPivotData(solrResponse).toMap();
        for (var e : correctResult.entrySet()) {
            for (var e1: e.getValue().entrySet()) {
                Long i = ((Map<String, Long>) parseResult.get(e.getKey())).get(e1.getKey());
                Assertions.assertEquals(e1.getValue(), i);
            }
        }
    }

}
