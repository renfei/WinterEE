package com.winteree.core.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: ElasticServiceTest</p>
 * <p>Description: 搜索引擎单元测试</p>
 *
 * @author RenFei
 * @date : 2020-07-19 18:02
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test ElasticService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class ElasticServiceTest {
    private final ElasticService elasticService;

    @Autowired
    public ElasticServiceTest(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @Test
    public void test() {
//        elasticService.deleteAll();
//        List<EsDocBean> esDocBeanList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            EsDocBean esDocBean = new EsDocBean();
//            esDocBean.setUuid(UUID.randomUUID().toString().toUpperCase());
//            esDocBean.setTenantUuid("TTTTTT");
//            esDocBean.setSiteUuid("SSSSSS");
//            esDocBean.setContent("这是内容，和标题" + i);
//            esDocBean.setTitle("这是标题" + i);
//            esDocBean.setType(EsDataType.POST.value());
//            esDocBean.setReleaseDate(new Date());
//            esDocBeanList.add(esDocBean);
//        }
//        elasticService.saveAll(esDocBeanList);
//        Page<EsDocBean> esDocBeans = elasticService.search("TTTTTT", "SSSSSS", "spring标题and内容", 1, 10);
//        log.info("总数：" + esDocBeans.getTotalElements());
//        log.info("总页数：" + esDocBeans.getTotalPages());
//        esDocBeans.forEach(x -> log.info(JSON.toJSONString(x)));
    }
}
