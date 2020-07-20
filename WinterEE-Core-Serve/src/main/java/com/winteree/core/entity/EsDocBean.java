package com.winteree.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * <p>Title: CmsDocBean</p>
 * <p>Description: ElasticSearch 存储的对象 </p>
 *
 * @author RenFei
 * @date : 2020-07-19 16:08
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(indexName = "es", type = "_doc", shards = 1, replicas = 0)
public class EsDocBean {
    /**
     * index：是否设置分词
     * analyzer：存储时使用的分词器
     * ik_max_word
     * ik_word
     * searchAnalyze：搜索时使用的分词器
     * store：是否存储
     * type: 数据类型
     */

    @Id
    @Field(store = true, type = FieldType.Keyword)
    private String uuid;

    @Field(store = true, type = FieldType.Keyword)
    private String tenantUuid;

    @Field(store = true, type = FieldType.Keyword)
    private String siteUuid;

    @Field(store = true, index = false, type = FieldType.Keyword)
    private String featureImg;

    @Field(store = true, index = false, type = FieldType.Date)
    private Date releaseDate;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;

    @Field(store = true, type = FieldType.Integer)
    private Integer type;

}
