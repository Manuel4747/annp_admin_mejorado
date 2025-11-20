package py.com.startic.gestion.controllers;

import java.util.Date;
import jakarta.faces.context.FacesContext;
import py.com.startic.gestion.models.DetallesSalidaArticuloCambios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesSalidaArticuloCambiosController")
@ViewScoped
public class DetallesSalidaArticuloCambiosController extends AbstractController<DetallesSalidaArticuloCambios> {

    public DetallesSalidaArticuloCambiosController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DetallesSalidaArticuloCambios.class);
    }
    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {


                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                    Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                    Date fecha = ejbFacade.getSystemDate();

                    getSelected().setFechaHoraCambio(fecha);
                    getSelected().setUsuarioCambio(usu);

                    super.saveNew(event);

        }

    }
}





