package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.DocumentosJudiciales;

@Named(value = "entradaMesaEntradaDocumentosJudicialesValidatorController")
@ViewScoped
public class EntradaMesaEntradaDocumentosJudicialesValidatorController extends AbstractController<DocumentosJudiciales>  implements Validator{

    public EntradaMesaEntradaDocumentosJudicialesValidatorController() {
        // Inform the Abstract parent controller of the concrete Bienes Entity
        super(DocumentosJudiciales.class);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        String nroMesaEntrada = (String) submittedAndConvertedValue;

        if (nroMesaEntrada == null || nroMesaEntrada.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }
        
        Collection<DocumentosJudiciales> bienes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByNroMesaEntradaJudicial", DocumentosJudiciales.class).setParameter("nroMesaEntrada",nroMesaEntrada).getResultList();

        if (bienes != null) {
            if(bienes.size() > 0 ){
                JsfUtil.addErrorMessage("Nro de Mesa de Entrada ya existe");
                throw new ValidatorException(new FacesMessage("Nro de Mesa de Entrada ya existe"));
            }
        }
    }

}
