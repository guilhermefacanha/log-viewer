package br.com.gfsolucoesti.init.servlet;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.gfsolucoesti.dao.TaskCategoryDao;
import br.com.gfsolucoesti.dao.TaskDao;
import br.com.gfsolucoesti.entity.Task;
import br.com.gfsolucoesti.entity.TaskCategory;
import br.com.gfsolucoesti.utils.FakerUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbInitUtil implements Serializable {

    private static final long serialVersionUID = 2064603410226281584L;

    @Inject
    private TaskDao dao;
    @Inject
    private TaskCategoryDao typeDao;
    
    private Long lastId;

    public void createDataSample() {
        log.debug("======================================================================");
        log.debug("     Database Context Initialization");
        log.debug("======================================================================");

        try {
            if (checkIfTasksExists() == false) {
                log.debug("Insert sample init data....");
                createSampleTaskCategories();
                createSampleTask();
                log.debug("Init data sample inserted.");
            }
        } catch (Exception e) {
            log.error("Error initializing Datababase", e);
        }
        log.debug("     Database Context Initialized");
        log.debug("======================================================================");
    }

    private void createSampleTaskCategories() {
        typeDao.save(TaskCategory.builder().name("ToDo").build());
        for (int i = 1; i <= 60; i++) {
            lastId = typeDao.save(TaskCategory.builder()
                    .name(FakerUtil.getFaker().commerce().department())
                    .build()).getId();
        }
    }

    private void createSampleTask() {
        for (int i = 0; i < 20; i++) {
            long tid = (long) new Random().nextInt(lastId.intValue());
            if(tid==0) tid = 1;
            Task t = Task.builder()
                    .type(TaskCategory.builder().id(tid).build())
                    .name("Buy " + FakerUtil.getFaker().commerce().productName())
                    .assignedTo(FakerUtil.getFaker().gameOfThrones().character())
                    .dueDate(FakerUtil.getFaker().date().future(30, TimeUnit.DAYS))
                    .description(FakerUtil.getFaker().gameOfThrones().quote())
                    .build();
            dao.save(t);
        }

    }
    
    private boolean checkIfTasksExists() {
        long totalRecords = typeDao.getTotalRecords();
        log.debug("TaskCategory: {} records found", totalRecords);
        return totalRecords > 0;
    }

}
