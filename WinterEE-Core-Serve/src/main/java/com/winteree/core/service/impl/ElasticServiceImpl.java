package com.winteree.core.service.impl;

import com.winteree.core.dao.ElasticRepository;
import com.winteree.core.entity.EsDocBean;
import com.winteree.core.service.ElasticService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Title: ElasticServiceImpl</p>
 * <p>Description: 搜索引擎服务</p>
 *
 * @author RenFei
 * @date : 2020-07-19 16:06
 */
@Service
public class ElasticServiceImpl implements ElasticService {
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ElasticRepository elasticRepository;

    public ElasticServiceImpl(ElasticsearchRestTemplate elasticsearchTemplate,
                              ElasticRepository elasticRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.elasticRepository = elasticRepository;
    }

    /**
     * 创建索引
     */
    @Override
    public void createIndex() {
        elasticsearchTemplate.createIndex(EsDocBean.class);
    }

    /**
     * 删除索引
     */
    @Override
    public void deleteIndex(String index) {
        elasticsearchTemplate.deleteIndex(index);
    }

    /**
     * 新增
     */
    @Override
    public void save(EsDocBean docBean) {
        elasticRepository.save(docBean);
    }

    /**
     * 批量新增
     */
    @Override
    public void saveAll(List<EsDocBean> list) {
        elasticRepository.saveAll(list);
    }

    /**
     * 删除所有
     */
    @Override
    public void deleteAll() {
        elasticRepository.deleteAll();
    }

    @Override
    public Page<EsDocBean> search(String tenantUuid, String siteUuid, String query, int page, int rows) {
        // 1.创建查询对象
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        TermQueryBuilder tenantUuidQuery = QueryBuilders.termQuery("tenantUuid", tenantUuid);
        TermQueryBuilder siteUuidQuery = QueryBuilders.termQuery("siteUuid", siteUuid);
        MatchQueryBuilder titleQuery = QueryBuilders.matchQuery("title", query);
        MatchQueryBuilder contentQuery = QueryBuilders.matchQuery("content", query);
        boolQuery.must(tenantUuidQuery);
        boolQuery.must(siteUuidQuery);
        boolQuery.must(titleQuery);
        boolQuery.must(contentQuery);
        // 2.调用查询接口
        return elasticRepository.search(boolQuery, PageRequest.of(page, rows));
    }
}
