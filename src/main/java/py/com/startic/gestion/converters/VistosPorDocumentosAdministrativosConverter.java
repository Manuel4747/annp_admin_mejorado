package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.VistosPorDocumentosAdministrativos;
import py.com.startic.gestion.facades.VistosPorDocumentosAdministrativosFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import py.com.startic.gestion.controllers.util.JsfUtil;

@FacesConverter(value = "vistosPorDocumentosAdministrativosConverter")
public class VistosPorDocumentosAdministrativosConverter implements Converter {

    
    private VistosPorDocumentosAdministrativosFacade ejbFacade;
    
    public VistosPorDocumentosAdministrativosConverter() {
        this.ejbFacade = CDI.current().select(VistosPorDocumentosAdministrativosFacade.class).get();
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
        if (object instanceof VistosPorDocumentosAdministrativos) {
            VistosPorDocumentosAdministrativos o = (VistosPorDocumentosAdministrativos) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VistosPorDocumentosAdministrativos.class.getName()});
            return null;
        }
    }

}
