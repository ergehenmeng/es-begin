```java
// 自定义数据转换
@Configuration
public class Config extends AbstractElasticsearchConfiguration {

  @Bean
  @Override
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    return new ElasticsearchCustomConversions(
      Arrays.asList(new AddressToMap(), new MapToAddress()));       
  }

  @WritingConverter                                                 
  static class AddressToMap implements Converter<Address, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(Address source) {

      LinkedHashMap<String, Object> target = new LinkedHashMap<>();
      target.put("x", source.getCity());

      return target;
    }
  }

  @ReadingConverter                                                 
  static class MapToAddress implements Converter<Map<String, Object>, Address> {

    @Override
    public Address convert(Map<String, Object> source) {

      return address;
    }
  }
}

```

* `IndexOperations` defines actions on index level like creating or deleting an index. (类似DDL)
* `DocumentOperations` defines actions to store, update and retrieve entities based on their id. (DML)
* `SearchOperations` define the actions to search for multiple entities using queries (类似DQL)
* `ElasticsearchOperations` combines the DocumentOperations and SearchOperations interfaces. 混合模式