package br.com.gfsolucoesti.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.gfsolucoesti.utils.GFUtils;

/**
 * Classe Converter de Boolean para SIM/NAO
 * 
 * @author gfsolucoesti
 */
@FacesConverter(value = "boolean")
public class BooleanConverter implements Converter {

    public Object getAsObject(FacesContext faces, UIComponent componente, String value) {
        return value;
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (GFUtils.isNullEmpty(arg2.toString()) || arg2.toString().equals("false"))
            return "NÃO";
        else
            return "SIM";

    }

}
