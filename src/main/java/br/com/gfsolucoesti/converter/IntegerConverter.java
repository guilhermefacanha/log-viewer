package br.com.gfsolucoesti.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Classe Converter de Inteiro onde Zero sera limpo para branco
 * @author gfsolucoesti
 */
@FacesConverter(value="int")
public class IntegerConverter implements Converter {
	public Object getAsObject(FacesContext faces, UIComponent componente, String value)
	{
		return value;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
	{
		if (arg2.toString().equals("0.0") || arg2.toString().equals("0") || arg2.toString().equals("0,00") || arg2.toString().equals(""))
			return "";
		
		return arg2.toString();
	}
}
