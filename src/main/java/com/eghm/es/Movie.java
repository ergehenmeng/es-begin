package com.eghm.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    
    /**
     * text:分词 keyword不分词
     */
    @Field(type = FieldType.Text)
    private String title;
    
    private Long year;
}
