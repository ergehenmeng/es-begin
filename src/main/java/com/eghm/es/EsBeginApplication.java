package com.eghm.es;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 二哥很猛
 */
@SpringBootApplication
@EnableElasticsearchRepositories
@RestController
public class EsBeginApplication {
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    
    public static void main(String[] args) {
        SpringApplication.run(EsBeginApplication.class, args);
    }
 
    @PostMapping("/saveUser")
    public void saveUser(@RequestBody User user) {
        elasticsearchRestTemplate.save(user);
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
    
}
