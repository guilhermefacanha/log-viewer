package br.com.gfsolucoesti.converter;

import java.sql.Timestamp;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.gfsolucoesti.utils.GFUtils;

/**
 * Classe Converter de Date para padrao dd/MM/yy HH:mm
 * 
 * @author gfsolucoesti
 */
@FacesConverter(value = "date")
public class DateConverter implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
        try {
            if (!GFUtils.isNullEmpty(value))
                return new Timestamp(GFUtils.parseStringDate(value, "dd/MM/yyyy").getTime());

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
        if (obj == null)
            return null;

        return GFUtils.getStringDate((java.util.Date) obj, "dd/MM/yyyy");
    }

}
