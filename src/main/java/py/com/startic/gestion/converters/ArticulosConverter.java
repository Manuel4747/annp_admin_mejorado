package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.Articulos;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.facades.ArticulosFacade;

@FacesConverter(value = "articulosConverter")
public class ArticulosConverter implements Converter {

    private ArticulosFacade ejbFacade;
    
    public ArticulosConverter() {
        this.ejbFacade = CDI.current().select(ArticulosFacade.class).get();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    java.lang.String getKey(String value) {
        java.lang.String key;
        key = value;
        return key;
    }

    String getStringKey(java.lang.String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof Articulos) {
            Articulos o = (Articulos) object;
            return getStringKey(o.getCodigo());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Articulos.class.getName()});
            return null;
        }
    }

}
