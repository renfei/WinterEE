package com.winteree.core.service.impl;

import com.winteree.api.entity.TableInfoDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.DataBaseMapper;
import com.winteree.core.dao.entity.TableInfoDO;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.DataBaseService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: DataBaseServiceImpl</p>
 * <p>Description: 数据库服务</p>
 *
 * @author RenFei
 * @date : 2020-08-04 20:37
 */
@Slf4j
@Service
public class DataBaseServiceImpl extends BaseService implements DataBaseService {
    private final DataBaseMapper dataBaseMapper;

    protected DataBaseServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                  DataBaseMapper dataBaseMapper) {
        super(wintereeCoreConfig);
        this.dataBaseMapper = dataBaseMapper;
    }

    /**
     * 执行任意SQL语句（危险：需要自己判断SQL是否含有危险内容）
     *
     * @param sql SQL
     * @return List<Map < String, String>>
     */
    @Override
    public List<Map<String, String>> execSql(String sql) {
        return dataBaseMapper.execSql(sql);
    }

    /**
     * 查询表信息
     *
     * @param database  数据库模式名
     * @param tablename 表名
     * @return List<TableInfoDO>
     */
    @Override
    public List<TableInfoDTO> getTableInfo(String database, String tablename) {
        List<TableInfoDO> tableInfoDOS = dataBaseMapper.selectTableInfo(database, tablename);
        if (BeanUtils.isEmpty(tableInfoDOS)) {
            return null;
        }
        List<TableInfoDTO> tableInfoDTOS = new ArrayList<>();
        tableInfoDOS.forEach(tableInfoDO -> tableInfoDTOS.add(convert(tableInfoDO)));
        return tableInfoDTOS;
    }

    /**
     * 创建表
     *
     * @param name          表名
     * @param comment       表描述
     * @param tableInfoDTOS 表字段
     * @return int
     */
    @Override
    public int createTable(String name, String comment, List<TableInfoDTO> tableInfoDTOS) throws RuntimeException {
        if (BeanUtils.isEmpty(name)) {
            throw new RuntimeException("Name Not Null");
        }
        if (BeanUtils.isEmpty(tableInfoDTOS)) {
            throw new RuntimeException("tableInfoDTOS Not Null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `");
        sb.append(name);
        sb.append("`(");
        sb.append("`id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',");
        tableInfoDTOS.forEach(tableInfoDTO -> {
            if (!"id".equals(tableInfoDTO.getColumnName().toLowerCase())) {
                sb.append("`");
                sb.append(tableInfoDTO.getColumnName());
                sb.append("` ");
                sb.append(tableInfoDTO.getDataType());
                if(!BeanUtils.isEmpty(tableInfoDTO.getLength())){
                    sb.append("(");
                    sb.append(tableInfoDTO.getLength());
                    sb.append(")");
                    sb.append(" ");
                }
                sb.append("YES".equals(tableInfoDTO.getIsNullable()) ? " NULL" : " NOT NULL");
                sb.append(" ");
                sb.append("COMMENT '");
                sb.append(tableInfoDTO.getColumnComment());
                sb.append("',");
            }
        });
        sb.append("PRIMARY KEY (`id`) )");
        if (!BeanUtils.isEmpty(comment)) {
            sb.append(" COMMENT = '");
            sb.append(comment);
            sb.append("'");
        }
        log.debug(sb.toString());
        dataBaseMapper.execSql(sb.toString());
        return 1;
    }

    private TableInfoDTO convert(TableInfoDO tableInfoDO) {
        return Builder.of(TableInfoDTO::new)
                .with(TableInfoDTO::setColumnComment, tableInfoDO.getColumnComment())
                .with(TableInfoDTO::setColumnName, tableInfoDO.getColumnName())
                .with(TableInfoDTO::setDataType, tableInfoDO.getDataType())
                .with(TableInfoDTO::setIsNullable, tableInfoDO.getIsNullable())
                .with(TableInfoDTO::setLength, tableInfoDO.getLength())
                .build();
    }
}
