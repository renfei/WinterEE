package com.winteree.core.dao;

import com.winteree.core.dao.entity.TableInfoDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: DataBaseMapper</p>
 * <p>Description: 数据库接口</p>
 *
 * @author RenFei
 * @date : 2020-08-04 20:38
 */
@Repository
public interface DataBaseMapper {
    /**
     * 执行任意SQL语句
     *
     * @param mysql SQL
     * @return List<Map < String, String>>
     */
    @Select("${mysql}")
    List<Map<String, String>> execSql(@Param("mysql") String mysql);

    /**
     * 查询表信息
     *
     * @param database  数据库模式名
     * @param tablename 表名
     * @return List<TableInfoDO>
     */
    @Select("SELECT COLUMN_NAME AS columnName, IS_NULLABLE AS isNullable, DATA_TYPE AS dataType, " +
            "CHARACTER_MAXIMUM_LENGTH AS LENGTH, COLUMN_COMMENT AS columnComment " +
            "FROM information_schema.COLUMNS " +
            "WHERE table_schema = #{database, jdbcType=VARCHAR}  AND table_name = #{tablename, jdbcType=VARCHAR}")
    List<TableInfoDO> selectTableInfo(@Param("database") String database, @Param("tablename") String tablename);
}
