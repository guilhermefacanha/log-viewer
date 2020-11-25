package br.com.gfsolucoesti.controller;

import java.io.Serializable;
import java.util.Collections;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gfsolucoesti.business.BaseBusiness;
import br.com.gfsolucoesti.business.TaskBusiness;
import br.com.gfsolucoesti.business.TaskCategoryBusiness;
import br.com.gfsolucoesti.entity.Task;
import br.com.gfsolucoesti.utils.GFUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
@ViewScoped
public class TaskController extends BaseController<Task> implements Serializable {

    private static final long serialVersionUID = 2702358477103653868L;

    @Getter
    @Setter
    private String typeId;

    //object to communicate with database
    @Inject
    private TaskBusiness taskBusiness;

    @Inject
    private TaskCategoryBusiness taskCategoryBusiness;

    @Override
    public void initController() {
        super.initController();
        log.debug("load type {} ... ", typeId);
        if (!GFUtils.isNullEmpty(typeId)) {
            long id = GFUtils.tryParse(GFUtils.decodeBase64(typeId));
            ent.setType(taskCategoryBusiness.get(id));
        }
    }

    @Override
    protected void loadList() {
        this.list = taskBusiness.getAll(Collections.singletonMap("type", "type"));
    }

    //first method to test controller communication with view
    public String getHello() {
        return "Hello from my First Controller Class";
    }

    @Override
    public void newEntity() {
        ent = new Task();
    }

    @Override
    protected BaseBusiness<Task> getBusiness() {
        return taskBusiness;
    }

}
