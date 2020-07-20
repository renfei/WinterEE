package com.winteree.core.service;

import com.winteree.core.entity.EsDocBean;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>Title: ElasticService</p>
 * <p>Description: 搜索引擎服务</p>
 *
 * @author RenFei
 * @date : 2020-07-19 16:05
 */
public interface ElasticService {
    static final String HIGH_LIGHT_START_TAG = "<keyword>";
    static final String HIGH_LIGHT_END_TAG = "</keyword>";

    /**
     * 创建索引
     */
    void createIndex();

    /**
     * 删除索引
     */
    void deleteIndex(String index);

    /**
     * 新增
     */
    void save(EsDocBean docBean);

    /**
     * 批量新增
     */
    void saveAll(List<EsDocBean> list);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 高亮查询
     *
     * @param tenantUuid 租户ID
     * @param siteUuid   站点ID
     * @param query      查询内容
     * @param page       页码
     * @param rows       每页行数
     * @return
     */
    Page<EsDocBean> search(String tenantUuid, String siteUuid, String query, int page, int rows);
}
