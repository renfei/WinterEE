package com.winteree.core.service;

import com.alibaba.fastjson.JSON;
import com.winteree.api.entity.TableInfoDTO;
import net.renfei.sdk.utils.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: DataBaseServiceTest</p>
 * <p>Description: 数据库服务测试</p>
 *
 * @author RenFei
 * @date : 2020-08-04 21:16
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test DataBaseService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class DataBaseServiceTest {
    private final DataBaseService dataBaseService;

    @Autowired
    public DataBaseServiceTest(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @Test
    public void execSql() {
        List<Map<String, String>> maps = dataBaseService.execSql("SELECT * FROM winteree_core_cms_site");
        System.out.println(JSON.toJSONString(maps));
    }

    @Test
    public void getTableInfo() {
        List<TableInfoDTO> tableInfoDTOS = dataBaseService.getTableInfo("winteree", "winteree_core_cms_site");
        System.out.println(JSON.toJSONString(tableInfoDTOS));
    }

    @Test
    @Rollback
    @Transactional
    public void createTable() {
        List<TableInfoDTO> tableInfoDTOS = new ArrayList<>();
        tableInfoDTOS.add(
                Builder.of(TableInfoDTO::new)
                        .with(TableInfoDTO::setLength, "255")
                        .with(TableInfoDTO::setDataType, "varchar")
                        .with(TableInfoDTO::setColumnName, "test1")
                        .with(TableInfoDTO::setColumnComment, "测试1")
                        .with(TableInfoDTO::setIsNullable, "YES")
                        .build()
        );
        tableInfoDTOS.add(
                Builder.of(TableInfoDTO::new)
                        .with(TableInfoDTO::setLength, "12")
                        .with(TableInfoDTO::setDataType, "int")
                        .with(TableInfoDTO::setColumnName, "test2")
                        .with(TableInfoDTO::setColumnComment, "测试2")
                        .with(TableInfoDTO::setIsNullable, "YES")
                        .build()
        );
        dataBaseService.createTable("test1", "表描述", tableInfoDTOS);
    }
}
