package com.winteree.core.service;

import com.winteree.api.entity.TaskJobDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: TaskServiceTest</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-17 21:37
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test TaskService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class TaskServiceTest {
    private final TaskService taskService;

    @Autowired
    public TaskServiceTest(TaskService taskService) {
        this.taskService = taskService;
    }

    @Test
    public void saveJob(){
        TaskJobDTO taskJobDTO = new TaskJobDTO();
        taskJobDTO.setJobName("UpdatePostPageRankJob");
        taskJobDTO.setJobGroup("CMS");
        taskJobDTO.setCronExpression("0 0 3 * * ? ");
        taskJobDTO.setDescription("定时更新文章评级");
        taskJobDTO.setJobClassName("com.winteree.core.task.UpdatePostPageRankJob");
//        taskService.saveJob(taskJobDTO);
    }
}
