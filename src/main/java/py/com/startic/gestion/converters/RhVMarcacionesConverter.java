package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.RhVMarcaciones;
import py.com.startic.gestion.facades.RhVMarcacionesFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "rhVMarcacionesConverter")
public class RhVMarcacionesConverter implements Converter {

    private RhVMarcacionesFacade ejbFacade;
    
    public RhVMarcacionesConverter() {
        this.ejbFacade = CDI.current().select(RhVMarcacionesFacade.class).get();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    int getKey(String value) {
        int key;
        key = Integer.parseInt(value);
        return key;
    }

    String getStringKey(int value) {
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
        if (object instanceof RhVMarcaciones) {
            RhVMarcaciones o = (RhVMarcaciones) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RhVMarcaciones.class.getName()});
            return null;
        }
    }

}
