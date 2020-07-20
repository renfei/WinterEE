package com.winteree.core.dao;

import com.winteree.core.entity.EsDocBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>Title: ElasticRepository</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-19 16:09
 */
public interface ElasticRepository extends ElasticsearchRepository<EsDocBean, String> {
}
