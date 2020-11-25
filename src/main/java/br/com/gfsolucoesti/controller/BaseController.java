package br.com.gfsolucoesti.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.PrimeFaces;

import br.com.gfsolucoesti.business.BaseBusiness;
import br.com.gfsolucoesti.entity.BaseEntity;
import br.com.gfsolucoesti.utils.GFUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseController<T extends BaseEntity> extends DefaultController implements Serializable {

    private static final long serialVersionUID = 4721461454168381459L;

    public static final String REQUIRED_FIELD_MSG = "base.label.required.field";

    protected abstract BaseBusiness<T> getBusiness();

    public abstract void newEntity();

    @Setter
    protected T ent;

    @Getter
    @Setter
    protected List<T> list;

    public String getSucessMsg() {
        return messages.get("base.label.saved");
    }

    public String getSucessMsgRemove() {
        return messages.get("base.label.removed");
    }

    public void initList() {
        loadList();
    }
    public void initController() {
        newEntity();
        initList();
    }

    protected void loadList() {
        if (GFUtils.isNullEmpty(getOrderBy()))
            list = getBusiness().getAll();
        else
            list = getBusiness().getAll(getOrderBy());
    }

    public void saveRedirect() {
        save();
        keepMsgs();
        goTo(getPage());
    }

    // method to add a new task to our list
    public void save() {
        this.getBusiness().save(ent);
        loadList();
        newEntity();
        showInfo(getSucessMsg());
    }

    public void remove(T entRemove) {
        this.getBusiness().remove(entRemove);
        loadList();
        showInfo(getSucessMsgRemove());
    }

    public void edit(T entEdit) {
        this.ent = entEdit;
    }

    public void loadEdit() {
        if (this.ent == null)
            newEntity();

        try {
            if (GFUtils.isValidObject(this.ent))
                this.ent = getBusiness().get(this.ent.getId());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public T getEnt() {
        if (ent == null)
            newEntity();

        return ent;
    }

    public boolean isEditEnt() {
        return GFUtils.isValidObject(getEnt());
    }

    public String getOrderBy() {
        return null;
    }

    protected void validate() {
    };

    public String getPage() {
        return null;
    };
    
    public void clearList() {
        if(CollectionUtils.isNotEmpty(this.list))
            this.list.clear();
    }

    public String getButtonOp() {
        if (this.isEditEnt())
            return messages.get("label.edit");
        else
            return messages.get("label.save");
    }

    public void addErrorMesssage(String msg) {
        PrimeFaces.current().executeScript("addErrorMessage('" + msg + "')");
    }

}
