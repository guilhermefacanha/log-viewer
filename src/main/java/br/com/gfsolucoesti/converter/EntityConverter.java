package br.com.gfsolucoesti.converter;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.gfsolucoesti.entity.BaseEntity;

/**
 * Entity Converter for use on selectOneMenu
 */
@SuppressWarnings("rawtypes")
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
    	if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        if (value != null && !"".equals(value) && !"0".equals(value)) {
            BaseEntity entity = (BaseEntity) value;
            this.addAttribute(component, entity);
            long id = entity.getId();
            if (id > 0) {
                return String.valueOf(id);
            }
        }
        if (value instanceof BaseEntity) {
            return "-1";
        }
        return (String) value;
    }

    protected void addAttribute(UIComponent component, BaseEntity o) {
        if (o != null) {
            String key = String.valueOf(o.getId());
            this.getAttributesFrom(component).put(key, o);
        }
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
}