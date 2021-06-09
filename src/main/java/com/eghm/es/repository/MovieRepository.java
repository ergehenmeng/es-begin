package com.eghm.es.repository;

import com.eghm.es.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 殿小二
 * @date 2021/6/8
 */
@Repository
public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    
}
