package py.com.startic.gestion.controllers;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;/*
import org.primefaces.model.DashboardModel;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;*/
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.ChartObjetivoPrincipal;
import py.com.startic.gestion.models.Periodo;
import py.com.startic.gestion.models.TiposObjetivos;

@Named(value = "chartObjetivoPrincipalController")
@ViewScoped
public class ChartObjetivoPrincipalController extends AbstractController<ChartObjetivoPrincipal> {
/*
    private BarChartModel modelDocumentosMesaPorDiaBar;// Modelo para el gráfico 
    private DashboardModel dashboardModel;
    private LineChartModel modelDocumentosMesaPorDiaLinear;
    private PieChartModel modelDocumentosMesaPorDiaPie;*/
    private Date fechaDesde;
    private Year año;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<TiposObjetivos> listaTiposDoc;
    private List<Acciones> listaAcciones;
    private List<Periodo> listaPeriodo;
    private Periodo periodo;
    private Integer tipo;
    private double resultado;
    private Double valorVariable;
    private Double variable2;
    private Double metasAlcanzada;
    private double porcentajeMisional;
    private double porcentajeAreaApoyo;

    private String tipoObjetivo;
    static final public String COLORES_CHART = "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c";

    private List<ChartObjetivoPrincipal> lista;

    public ChartObjetivoPrincipalController() {
        // Inform the Abstract parent controller of the concrete Contactos Entity
        super(ChartObjetivoPrincipal.class);
    }

    @PostConstruct
    public void init() {
        porcentajeMisional = 0;
        porcentajeAreaApoyo = 0;
        tipoGrafico = "Lineas";
        tipoObjetivo = "Misional";
        fechaDesde = ejbFacade.getSystemDateOnly(-15);
        fechaHasta = ejbFacade.getSystemDateOnly();
        listaPeriodo = ejbFacade.getEntityManager().createNamedQuery("Periodo.findByObjetivo", Periodo.class).getResultList();
        listaTiposDoc = ejbFacade.getEntityManager().createNamedQuery("TiposObjetivos.findAll", TiposObjetivos.class).getResultList();
        //createBarChartAreaApoyo();
        // createBarChartMisional();
        initChartObjetivoPrincipal();
        //calcularPorcentajeMisional();
        //calcularPorcentajeMisional();
    }

    public void initChartObjetivoPrincipal() {
        lista = null;
        List<ChartObjetivoPrincipal> itns;
        List<Integer> periodos = new ArrayList<>();
        for (Periodo per: listaPeriodo) {
            periodos.add(per.getId());
        }
        if (tipoObjetivo.equals("Misional") && periodo!=null) {
            itns = ejbFacade.getEntityManager().createNamedQuery("ChartObjetivoPrincipal.findByObjetivoPeriodo", ChartObjetivoPrincipal.class).setParameter("tipoObjetivo", tipoObjetivo).setParameter("periodos", periodos).getResultList();
            setItems(itns);
            createBarChartMisional();
            /*       
            if (!itns.isEmpty()){
                setSelected(itns.get(0));
                variable2= getSelected().getVariable2();
                valorVariable = getSelected().getValorVariable();
                metasAlcanzada = getSelected().getMetasAlcanzada();
            }
            calcularPorcentajeMisional();
            createBarChartMisional();
            */
        } else if (tipoObjetivo.equals("Area de Apoyo") && periodo!=null) {
            itns = ejbFacade.getEntityManager().createNamedQuery("ChartObjetivoPrincipal.findByObjetivoPeriodo", ChartObjetivoPrincipal.class).setParameter("tipoObjetivo", tipoObjetivo).setParameter("periodos", periodos).getResultList();
            setItems(itns);
            createBarChartAreaApoyo();
            /*
            if (!itns.isEmpty()){
                setSelected(itns.get(0));
                variable2= getSelected().getVariable2();
                valorVariable = getSelected().getValorVariable();
                metasAlcanzada = getSelected().getMetasAlcanzada();
            }
            calcularPorcentajeAreaDeApoyo();
            createBarChartAreaApoyo();
            */
        } else {
            itns = new ArrayList<>();
            setItems(itns);
            createBarChartMisional();
        }
    }

    public Double calcularPorcentajeMisional(ChartObjetivoPrincipal itn) {
        if (itn != null && itn.getVariable2()!=null && itn.getValorVariable() != null) {
            porcentajeMisional = (itn.getVariable2() / itn.getValorVariable()) * 100;
        } else {
            porcentajeMisional = 0D;
        }
        return porcentajeMisional;
        /*
        if (getSelected()!=null && getSelected().getTipoObjetivo() != null) {
            if (variable2 != null && !variable2.equals(0) && valorVariable != null && !valorVariable.equals(0)) {
            porcentajeMisional = (variable2 / valorVariable) * 100;
        } else {
            porcentajeMisional = 0;
        }
        }
        */
        // Llamar al método para actualizar el gráfico después de calcular el porcentaje
       // createBarChartMisional();

    }

    public Double calcularPorcentajeAreaDeApoyo(ChartObjetivoPrincipal itn) {
        if (itn != null && itn.getMetasAlcanzada() != null) {
            porcentajeAreaApoyo = (itn.getMetasAlcanzada() / 8) * 100;
        } else {
            porcentajeAreaApoyo = 0;
        }
        return porcentajeAreaApoyo;
        /*
        if (getSelected()!=null && getSelected().getTipoObjetivo() != null) {
            if (metasAlcanzada != null && !metasAlcanzada.equals(0)) {
                porcentajeAreaApoyo = (metasAlcanzada / 8) * 100;
            } else {
                porcentajeAreaApoyo = 0;
            }
        }
        */
        // Llamar al método para actualizar el gráfico después de calcular el porcentaje
       // createBarChartAreaApoyo();

    }

    // Crear el gráfico de barras con el porcentaje calculado
    private void createBarChartMisional() {
/*
        modelDocumentosMesaPorDiaBar = new BarChartModel();

        // Crear una serie de datos para el gráfico
        ChartSeries porcentajeSerieMisional = new ChartSeries();
        porcentajeSerieMisional.setLabel("Misional");

        // Agregar los datos calculados al gráfico
        for (ChartObjetivoPrincipal itn: getItems()) {
            porcentajeMisional = calcularPorcentajeMisional(itn);
            porcentajeSerieMisional.set(itn.getPeriodo(), porcentajeMisional);
        }
        
        /*
        porcentajeSerieMisional.set("2024", porcentajeMisional); // Mostrar solo el porcentaje calculado
        porcentajeSerieMisional.set("2025", porcentajeMisional); // Mostrar solo el porcentaje calculado
        porcentajeSerieMisional.set("2026", porcentajeMisional); // Mostrar solo el porcentaje calculado
        porcentajeSerieMisional.set("2027", porcentajeMisional); // Mostrar solo el porcentaje calculado
        porcentajeSerieMisional.set("2028", porcentajeMisional); // Mostrar solo el porcentaje calculado
        */
        // Agregar la serie al modelo de gráfico
    //    modelDocumentosMesaPorDiaBar.addSeries(porcentajeSerieMisional);*/
    }

    private void createBarChartAreaApoyo() {
/*
        modelDocumentosMesaPorDiaBar = new BarChartModel();

        // Crear una serie de datos para el gráfico
        ChartSeries porcentajeSerieAreaApoyo = new ChartSeries();
        porcentajeSerieAreaApoyo.setLabel("Area de Apoyo");
        
        for (ChartObjetivoPrincipal itn: getItems()) {
            porcentajeAreaApoyo = calcularPorcentajeAreaDeApoyo(itn);
            porcentajeSerieAreaApoyo.set(itn.getPeriodo(), porcentajeAreaApoyo);
        }

        // Agregar los datos calculados al gráfico 
        /*
        porcentajeSerieAreaApoyo.set("2024", porcentajeAreaApoyo); // Mostrar solo el porcentaje calculado
        porcentajeSerieAreaApoyo.set("2025", porcentajeAreaApoyo); // Mostrar solo el porcentaje calculado
        porcentajeSerieAreaApoyo.set("2026", porcentajeAreaApoyo); // Mostrar solo el porcentaje calculado
        porcentajeSerieAreaApoyo.set("2027", porcentajeAreaApoyo); // Mostrar solo el porcentaje calculado
        porcentajeSerieAreaApoyo.set("2028", porcentajeAreaApoyo); // Mostrar solo el porcentaje calculado  
        */
        // Agregar la serie al modelo de gráfico
        //modelDocumentosMesaPorDiaBar.addSeries(porcentajeSerieAreaApoyo);
    }
/*
    public PieChartModel getModelDocumentosMesaPorDiaPie() {
        return modelDocumentosMesaPorDiaPie;
    }

    public void setModelDocumentosMesaPorDiaPie(PieChartModel modelDocumentosMesaPorDiaPie) {
        this.modelDocumentosMesaPorDiaPie = modelDocumentosMesaPorDiaPie;
    }
*/
    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }
/*
    public LineChartModel getModelDocumentosMesaPorDiaLinear() {
        return modelDocumentosMesaPorDiaLinear;
    }

    public void setModelDocumentosMesaPorDiaLinear(LineChartModel modelDocumentosMesaPorDiaLinear) {
        this.modelDocumentosMesaPorDiaLinear = modelDocumentosMesaPorDiaLinear;
    }*/

    public List<ChartObjetivoPrincipal> getLista() {
        return lista;
    }

    public void setLista(List<ChartObjetivoPrincipal> lista) {
        this.lista = lista;
    }

    public List<TiposObjetivos> getListaTiposDoc() {
        return listaTiposDoc;
    }

    public void setListaTiposDoc(List<TiposObjetivos> listaTiposDoc) {
        this.listaTiposDoc = listaTiposDoc;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }
/*
    public BarChartModel getModelDocumentosMesaPorDiaBar() {
        return modelDocumentosMesaPorDiaBar;
    }

    public void setModelDocumentosMesaPorDiaBar(BarChartModel modelDocumentosMesaPorDiaBar) {
        this.modelDocumentosMesaPorDiaBar = modelDocumentosMesaPorDiaBar;
    }
*/
    public List<Periodo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Periodo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
/*
    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

    public void setDashboardModel(DashboardModel dashboardModel) {
        this.dashboardModel = dashboardModel;
    }
*/
    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public Double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(Double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public Double getVariable2() {
        return variable2;
    }

    public void setVariable2(Double variable2) {
        this.variable2 = variable2;
    }

    public Double getMetasAlcanzada() {
        return metasAlcanzada;
    }

    public void setMetasAlcanzada(Double metasAlcanzada) {
        this.metasAlcanzada = metasAlcanzada;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getTipoObjetivo() {
        return tipoObjetivo;
    }

    public void setTipoObjetivo(String tipoObjetivo) {
        this.tipoObjetivo = tipoObjetivo;
    }

    public Year getAño() {
        return año;
    }

    public void setAño(Year año) {
        this.año = año;
    }

    public double getPorcentajeMisional() {
        return porcentajeMisional;
    }

    public void setPorcentajeMisional(double porcentajeMisional) {
        this.porcentajeMisional = porcentajeMisional;
    }

    public double getPorcentajeAreaApoyo() {
        return porcentajeAreaApoyo;
    }

    public void setPorcentajeAreaApoyo(double porcentajeAreaApoyo) {
        this.porcentajeAreaApoyo = porcentajeAreaApoyo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

}
