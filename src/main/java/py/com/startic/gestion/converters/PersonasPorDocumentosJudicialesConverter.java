package py.com.startic.gestion.converters;

import py.com.startic.gestion.models.PersonasPorDocumentosJudiciales;
import py.com.startic.gestion.facades.PersonasPorDocumentosJudicialesFacade;
import py.com.startic.gestion.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

@FacesConverter(value = "personasPorDocumentosJudicialesConverter")
public class PersonasPorDocumentosJudicialesConverter implements Converter {

    private PersonasPorDocumentosJudicialesFacade ejbFacade;
    
    public PersonasPorDocumentosJudicialesConverter() {
        this.ejbFacade = CDI.current().select(PersonasPorDocumentosJudicialesFacade.class).get();
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

    py.com.startic.gestion.models.PersonasPorDocumentosJudicialesPK getKey(String value) {
        py.com.startic.gestion.models.PersonasPorDocumentosJudicialesPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new py.com.startic.gestion.models.PersonasPorDocumentosJudicialesPK();
        key.setPersona(Integer.parseInt(values[0]));
        key.setDocumentoJudicial(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(py.com.startic.gestion.models.PersonasPorDocumentosJudicialesPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getPersona());
        sb.append(SEPARATOR);
        sb.append(value.getDocumentoJudicial());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof PersonasPorDocumentosJudiciales) {
            PersonasPorDocumentosJudiciales o = (PersonasPorDocumentosJudiciales) object;
            return getStringKey(o.getPersonasPorDocumentosJudicialesPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PersonasPorDocumentosJudiciales.class.getName()});
            return null;
        }
    }

}
