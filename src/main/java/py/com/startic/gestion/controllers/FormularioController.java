package py.com.startic.gestion.controllers;


import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import py.com.startic.gestion.models.AutorizacionFabricante;
import py.com.startic.gestion.models.CaracterSolicitud;
import py.com.startic.gestion.models.ContratoAbierto;
import py.com.startic.gestion.models.ContratoExcepcion;
import py.com.startic.gestion.models.Formulario;
import py.com.startic.gestion.models.Idioma;
import py.com.startic.gestion.models.LlamadoSbe;
import py.com.startic.gestion.models.Muestra;
import py.com.startic.gestion.models.ParticipacionVt;
import py.com.startic.gestion.models.TipoContratoAbierto;

@Named(value = "formularioController")
@ViewScoped
public class FormularioController extends AbstractController<Formulario> {

    @Inject
    private HistoricoContratoController historicoContratoController;
    @Inject
    private HistoricoEjecucionController historicoEjecucionController;
    @Inject
    private SistemaAdjudicacionController sistemaAdjudicacionController;
    @Inject
    private TipoModalidadController tipoModalidadController;
   
    
    @Enumerated(EnumType.STRING)
    private CaracterSolicitud caracterSolicitud;
    
     @Enumerated(EnumType.STRING)
    private ContratoExcepcion contratoExcepcion;
    
    @Enumerated(EnumType.STRING)
    private LlamadoSbe LlamadoSbe;
    
    @Enumerated(EnumType.STRING)
    private ContratoAbierto contratoAbierto;
        
    @Enumerated(EnumType.STRING)
    private TipoContratoAbierto tipoContratoAbierto;
    
     @Enumerated(EnumType.STRING)
    private Idioma idioma;
    
      @Enumerated(EnumType.STRING)
    private ParticipacionVt participacionVt;
    
        @Enumerated(EnumType.STRING)
    private AutorizacionFabricante autorizacionFabricante;
        
         @Enumerated(EnumType.STRING)
    private Muestra muestra;
    
    
       
    public CaracterSolicitud getCaracterSolicitud() {
        return caracterSolicitud;
    }

    public void setCaracterSolicitud(CaracterSolicitud caracterSolicitud) {
        this.caracterSolicitud = caracterSolicitud;
    }
    
    
    public LlamadoSbe getLlamadoSbe() {
        return LlamadoSbe;
    }

    public void setLlamadoSbe(LlamadoSbe LlamadoSbe) {
        this.LlamadoSbe = LlamadoSbe;
    }
    
    
    public ContratoExcepcion getContratoExcepcion() {
        return contratoExcepcion;
    }

    public void setContratoExcepcion(ContratoExcepcion contratoExcepcion) {
        this.contratoExcepcion = contratoExcepcion;
    }

    public ContratoAbierto getContratoAbierto() {
        return contratoAbierto;
    }

    public void setContratoAbierto(ContratoAbierto contratoAbierto) {
        this.contratoAbierto = contratoAbierto;
    }

    public TipoContratoAbierto getTipoContratoAbierto() {
        return tipoContratoAbierto;
    }

    public void setTipoContratoAbierto(TipoContratoAbierto tipoContratoAbierto) {
        this.tipoContratoAbierto = tipoContratoAbierto;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public ParticipacionVt getParticipacionVt() {
        return participacionVt;
    }

    public void setParticipacionVt(ParticipacionVt participacionVt) {
        this.participacionVt = participacionVt;
    }

    public Muestra getMuesstra() {
        return muestra;
    }

    public void setMuesstra(Muestra muesstra) {
        this.muestra = muestra;
    }
    
       
       


    public FormularioController() {
        // Inform the Abstract parent controller of the concrete Formulario Entity
        super(Formulario.class);
    }
    
     
    public CaracterSolicitud[] getCaracterSolicitudDisponibles() {
        return CaracterSolicitud.values(); // devuelve los valores del enum
    }
    
    
    public ContratoExcepcion[] getContratoExcepcionDisponibles() {
        return ContratoExcepcion.values(); // devuelve los valores del enum
    }
       
    public LlamadoSbe[] getLlamadoSbeDisponible() {
        return LlamadoSbe.values(); // devuelve los valores del enum
    }
    
         
    public ContratoAbierto[] getContratoAbiertoDisponibles() {
        return ContratoAbierto.values(); // devuelve los valores del enum
    }
    
     public TipoContratoAbierto[] getTipoContratoAbiertoDisponibles() {
        return TipoContratoAbierto.values(); // devuelve los valores del enum
    }
        
     public Idioma[] getIdiomaDisponibles() {
        return Idioma.values(); // devuelve los valores del enum
    }
     
       public ParticipacionVt[] getParticipacionVtDisponibles() {
        return ParticipacionVt.values(); // devuelve los valores del enum
    }
        public AutorizacionFabricante[] getAutorizacionFabricanteDisponibles() {
        return AutorizacionFabricante.values(); // devuelve los valores del enum
    }
            public Muestra[] getMuestraDisponibles() {
        return Muestra.values(); // devuelve los valores del enum
    }
    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        historicoContratoController.setSelected(null);
        historicoEjecucionController.setSelected(null);
        sistemaAdjudicacionController.setSelected(null);
        tipoModalidadController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the HistoricoContrato controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareHistoricoContrato(ActionEvent event) {
        if (this.getSelected() != null && historicoContratoController.getSelected() == null) {
            historicoContratoController.setSelected(this.getSelected().getHistoricoContrato());
        }
    }

    /**
     * Sets the "selected" attribute of the HistoricoEjecucion controller in
 order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareHistoricoEjecucion(ActionEvent event) {
        if (this.getSelected() != null && historicoEjecucionController.getSelected() == null) {
            historicoEjecucionController.setSelected(this.getSelected().getHistoricoEjecucion());
        }
    }

    /**
     * Sets the "selected" attribute of the SistemaAdjudicacion controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSistemaAdjudicacion(ActionEvent event) {
        if (this.getSelected() != null && sistemaAdjudicacionController.getSelected() == null) {
            sistemaAdjudicacionController.setSelected(this.getSelected().getSistemaAdjudicacion());
        }
    }

    /**
     * Sets the "selected" attribute of the TipoModalidad controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoModalidad(ActionEvent event) {
        if (this.getSelected() != null && tipoModalidadController.getSelected() == null) {
            tipoModalidadController.setSelected(this.getSelected().getTipoModalidad());
        }
    }
}
