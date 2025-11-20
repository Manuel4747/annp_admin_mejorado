package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.FirmasArchivoAdministrativo;
import py.com.startic.gestion.facades.FirmasArchivoAdministrativoFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.faces.convert.FacesConverter;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "firmasArchivoAdministrativoConverter")
public class FirmasArchivoAdministrativoConverter implements Converter {

    private FirmasArchivoAdministrativoFacade ejbFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.getEjbFacade().find(getKey(value));
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
        if (object instanceof FirmasArchivoAdministrativo) {
            FirmasArchivoAdministrativo o = (FirmasArchivoAdministrativo) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FirmasArchivoAdministrativo.class.getName()});
            return null;
        }
    }

    private FirmasArchivoAdministrativoFacade getEjbFacade() {
        this.ejbFacade = CDI.current().select(FirmasArchivoAdministrativoFacade.class).get();
        return this.ejbFacade;
    }
}
