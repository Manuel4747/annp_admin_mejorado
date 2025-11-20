package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.MotivosMovimientosBienes;
import py.com.startic.gestion.facades.MotivosMovimientosBienesFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "motivosMovimientosBienesConverter")
public class MotivosMovimientosBienesConverter implements Converter {

    private MotivosMovimientosBienesFacade ejbFacade;
    
    public MotivosMovimientosBienesConverter() {
        this.ejbFacade = CDI.current().select(MotivosMovimientosBienesFacade.class).get();
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
        if (object instanceof MotivosMovimientosBienes) {
            MotivosMovimientosBienes o = (MotivosMovimientosBienes) object;
            return getStringKey(o.getCodigo());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MotivosMovimientosBienes.class.getName()});
            return null;
        }
    }

}
