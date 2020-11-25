package br.com.gfsolucoesti.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gfsolucoesti.business.BaseBusiness;
import br.com.gfsolucoesti.business.TaskCategoryBusiness;
import br.com.gfsolucoesti.entity.TaskCategory;

@Named
@ViewScoped
public class TaskCategoryController extends BaseController<TaskCategory> {

    private static final long serialVersionUID = 4884372877835103541L;

    @Inject
    TaskCategoryBusiness taskCategoryBusiness;

    @Override
    public void newEntity() {
        this.ent = new TaskCategory();
    }

    @Override
    protected BaseBusiness<TaskCategory> getBusiness() {
        return taskCategoryBusiness;
    }

}
