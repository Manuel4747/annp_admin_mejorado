package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.AvisosPorUsuarios;
import py.com.startic.gestion.facades.AvisosPorUsuariosFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "avisosPorUsuariosConverter")
public class AvisosPorUsuariosConverter implements Converter {

    private AvisosPorUsuariosFacade ejbFacade;
    
    public AvisosPorUsuariosConverter() {
        this.ejbFacade = CDI.current().select(AvisosPorUsuariosFacade.class).get();
    }

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    py.com.startic.gestion.models.AvisosPorUsuariosPK getKey(String value) {
        py.com.startic.gestion.models.AvisosPorUsuariosPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new py.com.startic.gestion.models.AvisosPorUsuariosPK();
        key.setUsuario(Integer.parseInt(values[0]));
        key.setAviso(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(py.com.startic.gestion.models.AvisosPorUsuariosPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getUsuario());
        sb.append(SEPARATOR);
        sb.append(value.getAviso());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof AvisosPorUsuarios) {
            AvisosPorUsuarios o = (AvisosPorUsuarios) object;
            return getStringKey(o.getAvisosPorUsuariosPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvisosPorUsuarios.class.getName()});
            return null;
        }
    }

}
