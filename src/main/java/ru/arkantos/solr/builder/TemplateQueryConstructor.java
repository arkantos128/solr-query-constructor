package ru.arkantos.solr.builder;


import ru.arkantos.solr.params.Container;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TemplateQueryConstructor extends AbstractQueryConstructor {

    private static final String VALUE_SEPARATOR = "\\|";
    private static final Pattern paramPattern = Pattern.compile("\\{#[^{}]+}");

    private final String queryTemplate;
    private final String prefix;
    private final String suffix;
    private final Collection<Position> contextParamsPositions;

    public TemplateQueryConstructor(Builder builder) {
        super(builder);
        this.prefix = builder.prefix;
        this.suffix = builder.suffix;
        this.queryTemplate = builder.queryTemplate;

        List<Position> positions = new ArrayList<>();
        Matcher m = paramPattern.matcher(builder.queryTemplate);
        while (m.find()) {
            positions.add(new Position(m.start(), m.end()));
        }
        this.contextParamsPositions = positions;
    }

    @Override
    public String buildQuery(Container container) {
        Map<String, Object> params = container.params();
        List<String> queries = getQueries(queryTemplate, params);

        if (queries.isEmpty()) return null;
        if (queries.size() == 1) return queries.get(0);

        String operatorString = ") " + joinOperator + " (";
        String fullQuery = "(" + String.join(operatorString, queries) + ")";
        if (prefix != null) fullQuery = prefix + fullQuery;
        if (suffix != null) fullQuery += suffix;
        return fullQuery;
    }

    private List<String> getQueries(String template, Map<String, Object> data) {
        if (template == null) return new ArrayList<>();
        if (!isContainsContextParam(template)) {
            return List.of(template);
        }
        List<String> queries = new ArrayList<>();

        for (var pos : contextParamsPositions) {
            String paramName = pos.end() < template.length()
                    ? template.substring(pos.start(), pos.end())
                    : template.substring(pos.start());

            List<ContextParam> params = getContextParamList(paramName, data);
            if (params.isEmpty()) return List.of();

            for (var param : params) {
                StringBuilder sb = new StringBuilder();
                sb.append(template, 0, pos.start()).append(param.value());

                int end = pos.end();
                if (end < template.length()) sb.append(template.substring(end));

                queries.add(sb.toString());
            }
        }

        return queries;
    }

    private String getContextParam(String text) {
        return text.substring(2, text.length() - 1); //remove {# and }
    }

    private List<ContextParam> getContextParamList(String contextParamText, Map<String, Object> data) {
        List<Object> paramValues = new ArrayList<>();
        String[] contextParams = getContextParam(contextParamText).split(VALUE_SEPARATOR);
        String contextParam = contextParams[0];
        Object paramValue = data.get(contextParam);
        if (paramValue == null) {
            if (contextParams.length > 1) {
                paramValues.add(contextParams[1]); //set default value if exists
            }
        } else {
            if (paramValue instanceof Object[] objs) {
                paramValues = Arrays.asList(objs);
            } else if (paramValue instanceof Collection<?> col) {
                paramValues.addAll(col);
            } else {
                paramValues = List.of(paramValue);
            }
        }
        return paramValues.stream()
                .map(this::convertToString)
                .map(v -> new ContextParam(contextParam, v))
                .toList();
    }

    private boolean isContainsContextParam(String text) {
        if (text == null) return false;
        return paramPattern.matcher(text).find();
    }

    private String convertToString(Object value) {
        return value != null ? value.toString() : "null";
    }

    private record Position(int start, int end) {}

    private record ContextParam(String name, String value) {}


    public static class Builder extends AbstractQueryConstructor.Builder<Builder> {
        private String queryTemplate;
        private String prefix;
        private String suffix;

        public Builder queryTemplate(String queryTemplate) {
            this.queryTemplate = queryTemplate;
            return self();
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return self();
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return self();
        }

        @Override
        public QueryConstructor build() {
            if (this.queryTemplate == null || this.queryTemplate.isBlank()) {
                throw new IllegalArgumentException("query template is null or empty");
            }
            return new TemplateQueryConstructor(this);
        }

        @Override
        Builder self() {
            return this;
        }
    }
}
