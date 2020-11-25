package br.com.gfsolucoesti.business;

import javax.inject.Inject;

import br.com.gfsolucoesti.dao.BaseDao;
import br.com.gfsolucoesti.dao.TaskCategoryDao;
import br.com.gfsolucoesti.entity.TaskCategory;

public class TaskCategoryBusiness extends BaseBusiness<TaskCategory> {

    private static final long serialVersionUID = -8860890814138385442L;

    @Inject
    private TaskCategoryDao dao;

    @Override
    public BaseDao<TaskCategory> getDao() {
        return dao;
    }

}
