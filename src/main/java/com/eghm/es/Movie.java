package com.eghm.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author 殿小二
 * @date 2021/6/8
 */
@Data
@Document(indexName = "movies")
public class Movie {
    
    private String id;
    
    private List<String> genre;
    
    private String title;
    
    private Long year;
}
