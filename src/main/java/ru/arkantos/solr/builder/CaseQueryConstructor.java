package ru.arkantos.solr.builder;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.arkantos.solr.params.Container;

import java.util.HashMap;
import java.util.Map;

@Setter
class CaseQueryConstructor extends AbstractQueryConstructor {

    private String caseParamKey;
    private Map<Object, QueryConstructor> caseQueryBuilderMap;
    private QueryConstructor defaultQueryConstructor;

    CaseQueryConstructor(Builder builder) {
        super(builder);
        this.caseParamKey = builder.caseParamKey;
        this.caseQueryBuilderMap = builder.caseQueryBuilderMap;
        this.defaultQueryConstructor = builder.defaultQueryBuilder;
    }

    @Override
    public String buildQuery(Container container) throws BuildSolrQueryException {
        try {
            var params = container.params();
            var caseParam = params.get(caseParamKey);
            if (caseParam != null) {
                var queryBuilder = caseQueryBuilderMap.get(caseParam);
                if (queryBuilder != null) {
                    return queryBuilder.buildQuery(container);
                }
            }
            if (defaultQueryConstructor != null) {
                return defaultQueryConstructor.buildQuery(container);
            }
            return null;
        } catch (Exception ex) {
            throw new BuildSolrQueryException("build case query exception", ex);
        }
    }

    @NoArgsConstructor
    public static class Builder extends AbstractQueryConstructor.Builder<Builder> {
        private String caseParamKey;
        private Map<Object, QueryConstructor> caseQueryBuilderMap = new HashMap<>();
        private QueryConstructor defaultQueryBuilder;

        public Builder caseParamKey(String caseParamKey) {
            this.caseParamKey = caseParamKey;
            return self();
        }

        public Builder addCase(Object key, QueryConstructor queryConstructor) {
            this.caseQueryBuilderMap.put(key, queryConstructor);
            return self();
        }

        public Builder defaultQueryBuilder(QueryConstructor defaultQueryConstructor) {
            this.defaultQueryBuilder = defaultQueryConstructor;
            return self();
        }

        @Override
        public QueryConstructor build() {
            if (this.caseParamKey == null) {
                throw new IllegalArgumentException("case param key is null");
            }
            if (this.caseQueryBuilderMap.isEmpty()) {
                throw new IllegalArgumentException("case query list is empty");
            }
            return new CaseQueryConstructor(this);
        }

        @Override
        Builder self() {
            return this;
        }
    }
}
