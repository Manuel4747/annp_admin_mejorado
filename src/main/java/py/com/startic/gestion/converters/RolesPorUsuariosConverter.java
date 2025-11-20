package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.facades.RolesPorUsuariosFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "rolesPorUsuariosConverter")
public class RolesPorUsuariosConverter implements Converter {

    private RolesPorUsuariosFacade ejbFacade;
    
    public RolesPorUsuariosConverter() {
        this.ejbFacade = CDI.current().select(RolesPorUsuariosFacade.class).get();
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

    py.com.startic.gestion.models.RolesPorUsuariosPK getKey(String value) {
        py.com.startic.gestion.models.RolesPorUsuariosPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new py.com.startic.gestion.models.RolesPorUsuariosPK();
        key.setUsuario(Integer.parseInt(values[0]));
        key.setRol(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(py.com.startic.gestion.models.RolesPorUsuariosPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getUsuario());
        sb.append(SEPARATOR);
        sb.append(value.getRol());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof RolesPorUsuarios) {
            RolesPorUsuarios o = (RolesPorUsuarios) object;
            return getStringKey(o.getRolesPorUsuariosPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RolesPorUsuarios.class.getName()});
            return null;
        }
    }

}
