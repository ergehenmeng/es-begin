package com.eghm.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 殿小二
 * @date 2021/6/8
 */
@Document(indexName = "user")
@Data
public class User implements Serializable {
    
    @Id
    private Long id;
    
    private String nickName;
    
    private String mobile;
    
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date addTime;
}
