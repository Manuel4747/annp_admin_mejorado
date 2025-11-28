package py.com.annp.persistencia.converters;

import jakarta.enterprise.inject.spi.CDI;
import py.com.startic.gestion.converters.*;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import py.com.annp.persistencia.facades.FnPresupuestoFacade;
import py.com.annp.persistencia.models.FnPresupuesto;

@FacesConverter(value = "fnPresupuestoConverter")
public class FnPresupuestoConverter implements Converter {

    //@Inject
    private FnPresupuestoFacade ejbFacade;
    
    public FnPresupuestoConverter() {
        this.ejbFacade = CDI.current().select(FnPresupuestoFacade.class).get();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
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
        if (object instanceof FnPresupuesto) {
            FnPresupuesto o = (FnPresupuesto) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FnPresupuesto.class.getName()});
            return null;
        }
    }

}
