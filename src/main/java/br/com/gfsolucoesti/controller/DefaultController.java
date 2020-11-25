package br.com.gfsolucoesti.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.gfsolucoesti.init.servlet.AppConfig;
import br.com.gfsolucoesti.internationalization.MessageProvider;
import br.com.gfsolucoesti.utils.GFUtils;

public abstract class DefaultController implements Serializable {

    private static final long serialVersionUID = -2559317603008719489L;
    
    @Inject
    protected MessageProvider messages;


    protected void goTo(String page) {
        goTo(page, null);
    }

    protected void goTo(String page, String param) {
        try {
            String app = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            page = app + page + "." + AppConfig.getUrlExtension();
            if (!GFUtils.isNullEmpty(param))
                page += "?" + param;
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            GFUtils.addMsg(e.getMessage(), true);
        }
    }

    protected void showError(String msg) {
        clearMsgList();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO: " + msg, ""));
    }

    protected void showInfo(String msg) {
        clearMsgList();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO: " + msg, ""));
    }

    protected void showWarn(String msg) {
        clearMsgList();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN: " + msg, ""));
    }

    protected void keepMsgs() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    private void clearMsgList() {
        try {
            if (GFUtils.isValidList(FacesContext.getCurrentInstance().getMessageList()))
                FacesContext.getCurrentInstance().getMessageList().clear();

        } catch (Exception e) {
        }
    }

}
