package com.winteree.core.service;

import com.winteree.api.entity.TableInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: DataBaseService</p>
 * <p>Description: 数据库服务</p>
 *
 * @author RenFei
 * @date : 2020-08-04 20:37
 */
public interface DataBaseService {
    /**
     * 执行任意SQL语句（危险：需要自己判断SQL是否含有危险内容）
     *
     * @param sql SQL
     * @return List<Map < String, String>>
     */
    List<Map<String, String>> execSql(String sql);

    /**
     * 查询表信息
     *
     * @param database  数据库模式名
     * @param tablename 表名
     * @return List<TableInfoDO>
     */
    List<TableInfoDTO> getTableInfo(String database, String tablename);

    /**
     * 创建表
     *
     * @param name          表名
     * @param comment       表描述
     * @param tableInfoDTOS 表字段
     * @return int
     */
    int createTable(String name, String comment, List<TableInfoDTO> tableInfoDTOS) throws RuntimeException;
}
