#### es以window服务方式启动
* `elasticsearch-service.bat install` 安装服务
* `elasticsearch-service.bat start` 启动服务

#### 集群说明
> 建立索引和类型时通过master,之后同步给slave
> 数据写入时,根据一定的routing规则,将route到集群任意节点,因此写入压力分散到整个集群中

#### Analyzer组成部分
> 0或多个character filter + 1个tokenizer + 0或多个token filter组成
* character filter 字符过滤
* tokenizer 文本切为分词
* token filter 分词之后进行过滤

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