package br.com.gfsolucoesti.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Classe Converter de Double formatado para Currency
 * @author gfsolucoesti
 */
@FacesConverter(value="currency")
public class CurrencyConverter implements Converter {
	public Object getAsObject(FacesContext faces, UIComponent componente, String value)
	{
		if(value!=null)
		{
			value = value.replace(".", "");
			value = value.replace(",", ".");
		}
		return value;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
	{
		if (arg2.toString().equals("0.0") || arg2.toString().equals("0") || arg2.toString().equals("0,00") || arg2.toString().equals(""))
			return "";
		else
		{
			Double double1 = Double.valueOf(arg2.toString());
			NumberFormat moneyFormat = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
			moneyFormat.setMinimumFractionDigits(2);
			moneyFormat.setMaximumFractionDigits(2);

			return moneyFormat.format(double1).replace("R$", "").trim();
		}
	}
}
