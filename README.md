## Solr Query Constructor

sample data (class must be __public__ and have __getters__)
```java
Human steve = new Human(
        "Steve",                                    // name
        20,                                         // age
        Map.of("someField", List.of("42", "solr")), // idk some map
        List.of(7, 49, 99999)                       // favorite numbers
        );
```

### converter (field extractor)
simple converter, generics `<FIELD_TYPE, RESULT_TYPE>` (only if you personally need it, for query constructor `<Object, Object>` will be enough) 
```java
Converter<String, String> nameConverter = Converter
        .defaultConverter("name", "name"); // ("path-to-the-field", "param-name")

Converter<List<Integer>, List<Integer>> numbersConverter = Converter
        .defaultConverter("favoriteNumbers", "numbers");
```
converter with function
```java
Converter<Integer, Boolean> ageConverter = Converter
        .defaultConverter("age", "isAdult", age -> Boolean.valueOf(age > 18).toString());
```

path separated by dots (even indexes), supported only string keys in map
```java
Converter<String, String> mapConverter = Converter
        .defaultConverter("idkMap.someField.1", "someWord");
```

combine all in pipeline
```java
ConverterPipeline converterPipeline = ConverterPipeline.defaultPipeline(
                nameConverter,
                ageConverter,
                numbersConverter,
                mapConverter
        );
```

### arguments container
get container from any object
```java
Container paramsContainer = converterPipeline.convert(steve);
```
or from prepared map
```java
Container paramsContainer = new Container(Map<String, Object>);
```

### query constructors
prepare query constructors \
from string template
```java
QueryConstructor nameQuery = QueryConstructor.templateBuilder()
                .queryTemplate("name:{#name}")
              //.prefix("<")  add string before
              //.suffix(">")  and after query
                
              // these options are available in all builders
              //.joinOperator(JoinOperator)    default AND
              //.wrapBrackets(boolead)         default true
                .build();
```
from param value, only string keys too
```java
QueryConstructor isAdultQuery = QueryConstructor.caseBuilder()
                .caseParamKey("isAdult")
                .addCase("true", QueryConstructor.templateBuilder()
                        .queryTemplate("type:man").build())
                .addCase("false", QueryConstructor.templateBuilder()
                        .queryTemplate("type:kid").build())
              //.defaultQueryBuilder() optionaly, if the key is not found
                .build();
```

join list values with OR operator (e.g. `num:1 OR num:2 OR...`)
```java
QueryConstructor numbersQuery = QueryConstructor.templateBuilder()
                .queryTemplate("num:{#numbers}")
                .joinOperator(JoinOperator.OR)
                .build();
```

collect constructors in one join-constructor
```java
QueryConstructor fullQuery = QueryConstructor.joinBuilder()
                .add(nameQuery)
                .add(isAdultQuery)
                .add(numbersQuery)
                .add(wordQuery)
              //.addAll(List<QueryConstructor>)
              //.defaultQueryConstructor(QueryConstructor) optionaly, if all others returns null
                .build();
```

finally, build query
```java
String solrQuery = fullQuery.buildQuery(paramsContainer); // throws BuildSolrQueryException
```
result: `(name:Steve) AND (type:man) AND ((num:7) OR (num:49) OR (num:99999)) AND (word:solr)`
<br>
<br>
P.S. \
for `SolrFacet` and `SolrPivot` parsers see related tests
