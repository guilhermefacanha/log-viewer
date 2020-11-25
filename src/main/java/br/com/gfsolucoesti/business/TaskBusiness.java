package br.com.gfsolucoesti.business;

import javax.inject.Inject;

import br.com.gfsolucoesti.dao.BaseDao;
import br.com.gfsolucoesti.dao.TaskDao;
import br.com.gfsolucoesti.entity.Task;

public class TaskBusiness extends BaseBusiness<Task> {

    private static final long serialVersionUID = -17859364919675491L;
    
    @Inject
    private TaskDao dao;

    @Override
    public BaseDao<Task> getDao() {
        return dao;
    }

}
