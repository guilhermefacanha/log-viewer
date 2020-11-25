package br.com.gfsolucoesti.exception;

import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.primefaces.PrimeFaces;
import org.primefaces.util.EscapeUtils;

import br.com.gfsolucoesti.utils.GFUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;

    }

    @SuppressWarnings("unused")
    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();
            ResourceBundle messageBuldle = getMessageProvider(fc);
            ExternalContext extContext = fc.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) extContext.getResponse();

            try {
                Throwable rootCause = getRootCause(t);
                String message = EscapeUtils.forJavaScript(rootCause.getMessage());
                if (t instanceof ViewExpiredException) {
                    log.info("ViewExpiredException, expiredViewId = " + ((ViewExpiredException) t).getViewId());
                } else {

                    if (rootCause instanceof javax.validation.ConstraintViolationException) {
                        ConstraintViolationException ex = (ConstraintViolationException) rootCause;
                        Optional<ConstraintViolation<?>> findFirst = ex.getConstraintViolations().stream().findFirst();
                        if (findFirst.isPresent()) {
                            String mb = getMsg(messageBuldle, findFirst.get().getMessage());
                            message = GFUtils.isNullEmpty(mb) ? findFirst.get().getMessage() : mb;
                        }
                    }

                    if (rootCause instanceof SystemBusinessException) {
                        PrimeFaces.current().executeScript("addErrorMessage('" + message + "')");
                    } else {
                        log.error("Error: ", t);
                        PrimeFaces.current().executeScript("addErrorMessage('" + message + "')");
                    }
                }

                synchronized (fc) {
                    if (fc.getResponseComplete() == false) {
                        if (isFacesAjax()) {
                            fc.responseComplete();
                            i.remove();
                        }
                    }
                }

            } catch (Exception e) {
                log.error("Exception while trying to handle Faces Context Exception", e);
            }
        }

        getWrapped().handle();
    }

    private String getMsg(ResourceBundle messageBuldle, String key) {
        try {
            return messageBuldle.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isFacesAjax() {
        // TODO Auto-generated method stub
        return false;
    }

    private ResourceBundle getMessageProvider(FacesContext context) {
        return context.getApplication().getResourceBundle(context, "msg");
    }

    /* GETTERS AND SETTERS */

    @Override
    public Throwable getRootCause(Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

}
