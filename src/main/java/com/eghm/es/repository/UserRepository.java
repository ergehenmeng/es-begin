package com.eghm.es.repository;

import com.eghm.es.Movie;
import com.eghm.es.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 殿小二
 * @date 2021/6/9
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Long>  {
    
}
