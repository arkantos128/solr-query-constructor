package ru.arkantos.solr.builder;

import ru.arkantos.solr.params.Container;

import java.util.ArrayList;
import java.util.List;

class JoinQueryConstructor extends AbstractQueryConstructor {

    private final List<QueryConstructor> queryConstructors;
    private final QueryConstructor defaultQueryConstructor;

    public JoinQueryConstructor(Builder builder) {
        super(builder);
        this.queryConstructors = builder.queryConstructors;
        this.defaultQueryConstructor = builder.defaultQueryConstructor;
    }

    @Override
    public String buildQuery(Container container) throws BuildSolrQueryException {
        StringBuilder sb = new StringBuilder();

        for (QueryConstructor queryConstructor : queryConstructors) {
            String query = queryConstructor.buildQuery(container);
            if (query == null) continue;

            if (queryConstructor.isWrapBrackets()) {
                query = "(" + query + ")";
            }
            if (sb.isEmpty()) {
                sb.append(query);
            } else {
                sb.append(" ").append(joinOperator).append(" ").append(query);
            }
        }

        if (sb.isEmpty()) {
            if (defaultQueryConstructor != null) {
                return defaultQueryConstructor.buildQuery(container);
            }
            throw new BuildSolrQueryException("no query builders");
        }

        return sb.toString();
    }

    public static class Builder extends AbstractQueryConstructor.Builder<Builder> {
        private List<QueryConstructor> queryConstructors = new ArrayList<>();
        private QueryConstructor defaultQueryConstructor;

        public Builder add(QueryConstructor queryConstructor) {
            if (queryConstructor == null) {
                throw new IllegalArgumentException("null query constructor list");
            }
            this.queryConstructors.add(queryConstructor);
            return self();
        }

        public Builder addAll(List<QueryConstructor> queryConstructors) {
            if (queryConstructors == null || queryConstructors.isEmpty()) {
                throw new IllegalArgumentException("null or empty query constructors list");
            }
            this.queryConstructors.addAll(queryConstructors);
            return self();
        }

        public Builder defaultQueryConstructor(QueryConstructor defaultQueryConstructor) {
            this.defaultQueryConstructor = defaultQueryConstructor;
            return self();
        }

        @Override
        public QueryConstructor build() {
            return new JoinQueryConstructor(this);
        }

        @Override
        Builder self() {
            return this;
        }
    }
}
