package com.eghm.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author 二哥很猛
 */
@SpringBootApplication
@EnableElasticsearchRepositories
public class EsBeginApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EsBeginApplication.class, args);
    }
    
}
