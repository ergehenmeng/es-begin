package com.eghm.es;

import com.eghm.es.repository.MovieRepository;
import com.eghm.es.repository.UserRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 二哥很猛
 */
@SpringBootApplication
@EnableElasticsearchRepositories
@RestController
public class EsBeginApplication {
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public static void main(String[] args) {
        SpringApplication.run(EsBeginApplication.class, args);
    }
 
   
    @GetMapping("/getByNickName")
    public User getByNickName() {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("nickName", "二哥很猛"))
                .build();
        SearchHitsIterator<User> search = elasticsearchRestTemplate.searchForStream(query, User.class);
        if (search.hasNext()) {
            return search.next().getContent();
        }
        return null;
    }
    
    @GetMapping("/getByNickNames")
    public List<User> getByNickNames(String nickName) {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("nickName", nickName))
                .build();
        SearchHitsIterator<User> search = elasticsearchRestTemplate.searchForStream(query, User.class);
        List<User> userList = new ArrayList<>();
        search.forEachRemaining(userSearchHit -> userList.add(userSearchHit.getContent()));
        return userList;
    }
    
    @GetMapping("/getMovie")
    public Page<Movie> getMovie(Integer page) {
        return movieRepository.findAll(PageRequest.of(page, 20));
    }
    
    @GetMapping("/getByPage")
    public List<Movie> getByPage(Integer page) {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("year", 1995))
                .withPageable(PageRequest.of(page, 20)).build();
        SearchHits<Movie> searchHits = elasticsearchRestTemplate.search(query, Movie.class);
        List<Movie> movieList = new ArrayList<>();
        searchHits.forEach(movieSearchHit -> movieList.add(movieSearchHit.getContent()));
        return movieList;
    }
    
    @GetMapping("/getMax")
    public String getMax(String title) {
        SearchRequest request = new SearchRequest().source(SearchSourceBuilder.searchSource()
                .query(QueryBuilders.termQuery("title", title)).aggregation(AggregationBuilders.min("maxYear").field("year")));
        SearchResponse execute = elasticsearchRestTemplate
                .execute(client -> client.search(request, RequestOptions.DEFAULT));
        ParsedMin maxYear = execute.getAggregations().get("maxYear");
        return maxYear.getValueAsString();
    }
    
    
    
    @GetMapping("/getUserById")
    public User getUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElse(null);
    }
    
    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User user) {
        userRepository.save(user);
        return "SUCCESS";
    }
    
    @GetMapping("/getAllUser")
    public List<User> getAllUser() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        iterable.forEach(userList::add);
        return userList;
    }
    
    @GetMapping("/deleteIndex")
    public String deleteIndex() {
        elasticsearchRestTemplate.indexOps(User.class).delete();
        return "SUCCESS";
    }
    
    /**
     * 创建索引
     */
    @GetMapping("/createIndex")
    public String createIndex() {
        elasticsearchRestTemplate.indexOps(User.class).create();
        return "SUCCESS";
    }
    
    /**
     * 添加数据并创建索引
     */
    @GetMapping("/index")
    public String index() {
        User user = new User();
        user.setNickName("二哥很猛");
        user.setMobile("13123223422");
        user.setId(12L);
        user.setAddTime(new Date());
        IndexQuery query = new IndexQueryBuilder().withId("12").withObject(user).build();
        return elasticsearchRestTemplate.index(query, IndexCoordinates.of("user"));
    }
}
