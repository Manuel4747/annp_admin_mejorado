package py.com.startic.gestion.controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;/*
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;*/
import py.com.startic.gestion.controllers.util.JsfUtil;

import py.com.startic.gestion.facades.ChartDocumentosSecretariaPorDiaFacade;
import py.com.startic.gestion.models.ChartDocumentosSecretariaPorDia;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "chartDocumentosSecretariaPorDiaController")
@ViewScoped
public class ChartDocumentosSecretariaPorDiaController extends AbstractController<ChartDocumentosSecretariaPorDia> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private DepartamentosController departamentoController;

/*    private BarChartModel modelDocumentosSecretariaPorDiaBar;
    private DashboardModel dashboardModel;
    private LineChartModel modelDocumentosSecretariaPorDiaLinear;
    private PieChartModel modelDocumentosSecretariaPorDiaPie;*/
    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<Usuarios> listaTiposDoc;
    static final public String COLORES_CHART = "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c";

    private List<ChartDocumentosSecretariaPorDia> lista;
/*
    public PieChartModel getModelDocumentosSecretariaPorDiaPie() {
        return modelDocumentosSecretariaPorDiaPie;
    }

    public void setModelDocumentosSecretariaPorDiaPie(PieChartModel modelDocumentosSecretariaPorDiaPie) {
        this.modelDocumentosSecretariaPorDiaPie = modelDocumentosSecretariaPorDiaPie;
    }
*/
    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }
/*
    public LineChartModel getModelDocumentosSecretariaPorDiaLinear() {
        return modelDocumentosSecretariaPorDiaLinear;
    }

    public void setModelDocumentosSecretariaPorDiaLinear(LineChartModel modelDocumentosSecretariaPorDiaLinear) {
        this.modelDocumentosSecretariaPorDiaLinear = modelDocumentosSecretariaPorDiaLinear;
    }
*/
    public List<ChartDocumentosSecretariaPorDia> getLista() {
        return lista;
    }

    public void setLista(List<ChartDocumentosSecretariaPorDia> lista) {
        this.lista = lista;
    }

    public List<Usuarios> getListaTiposDoc() {
        return listaTiposDoc;
    }

    public void setListaTiposDoc(List<Usuarios> listaTiposDoc) {
        this.listaTiposDoc = listaTiposDoc;
    }
/*
    public BarChartModel getModelDocumentosSecretariaPorDiaBar() {
        return modelDocumentosSecretariaPorDiaBar;
    }

    public void setModelDocumentosSecretariaPorDiaBar(BarChartModel modelDocumentosSecretariaPorDiaBar) {
        this.modelDocumentosSecretariaPorDiaBar = modelDocumentosSecretariaPorDiaBar;
    }
*/
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
    public ChartDocumentosSecretariaPorDiaController() {
        // Inform the Abstract parent controller of the concrete VComprasPorRubroPorMes Entity
        super(ChartDocumentosSecretariaPorDia.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    @PostConstruct
    public void init() {

        tipoGrafico = "Lineas";
        fechaDesde = ejbFacade.getSystemDateOnly(-30);
        fechaHasta = ejbFacade.getSystemDateOnly();
        Departamentos dpto = departamentoController.prepareCreate(null);
        dpto.setId(5); // Secretaria
        listaTiposDoc = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", dpto).getResultList();
        initChartDocumentosSecretariaPorDia();

    }

    public void initChartDocumentosSecretariaPorDia() {
        lista = null;
        if (tipoGrafico.equals("Barras") || tipoGrafico.equals("Lineas")) {
            initChartDocumentosSecretariaPorDiaCartesianos();
        } else if (tipoGrafico.equals("Torta")) {
          ///  initChartDocumentosSecretariaPorDiaPie();
        }
    }

    public void initChartDocumentosSecretariaPorDiaCartesianos() {
/*
        dashboardModel = new DefaultDashboardModel();

        Date fechaActual = new Date();

        DashboardColumn column1 = new DefaultDashboardColumn();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        CartesianChartModel modelDocumentosSecretariaPorDia = null;

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(-30);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }

        if (listaTiposDoc != null) {
            if (listaTiposDoc.isEmpty()) {
                JsfUtil.addErrorMessage("Debe elegir uno o mas tipos de documento.");
                return;
            }
        } else {
            JsfUtil.addErrorMessage("Debe elegir uno o mas tipos de documento");
            return;
        }

        ChartDocumentosSecretariaPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");

        ChartSeries series1;

        Calendar cal = Calendar.getInstance();

        Iterator<Usuarios> it3 = listaTiposDoc.iterator();

        Usuarios tipoDoc = null;

        ChartDocumentosSecretariaPorDia nodo = null;

        ChartDocumentosSecretariaPorDia nodoCero = new ChartDocumentosSecretariaPorDia();

        nodoCero.setCantidad(0);


        if (tipoGrafico.equals("Barras")) {
            modelDocumentosSecretariaPorDiaBar = new BarChartModel();
            modelDocumentosSecretariaPorDia = modelDocumentosSecretariaPorDiaBar;
            series1 = new BarChartSeries();
        } else {
            modelDocumentosSecretariaPorDiaLinear = new LineChartModel();
            modelDocumentosSecretariaPorDia = modelDocumentosSecretariaPorDiaLinear;
            series1 = new LineChartSeries();
        }

        List<ChartDocumentosSecretariaPorDia> listaTemp;

        lista = new ArrayList<>();

        while (it3.hasNext()) {

            tipoDoc = it3.next();
            listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartDocumentosSecretariaPorDia.findByRangoFechaPresentacion", ChartDocumentosSecretariaPorDia.class).setParameter("tipoDocumentoJudicial", String.valueOf(tipoDoc.getId())).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
            lista.addAll(listaTemp);

            Iterator<ChartDocumentosSecretariaPorDia> it2 = listaTemp.iterator();

            series1.setLabel(tipoDoc.getNombresApellidos());

            envio = new ChartDocumentosSecretariaPorDia();
            envio.setFechaPresentacion(fechaCero);

            boolean iterar = true;

            fechaActual.setTime(fechaDesde.getTime());

            while (fechaActual.compareTo(fechaHasta) <= 0) {

                if (iterar) {
                    if (it2.hasNext()) {
                        envio = it2.next();
                    }
                    iterar = false;
                }

                if (envio.getFechaPresentacion().compareTo(fechaActual) != 0) {
                    nodoCero.setFechaPresentacion(fechaActual);
                    nodo = nodoCero;
                } else {
                    nodo = envio;
                    iterar = true;
                }

                series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
                if (cantMax < nodo.getCantidad()) {
                    cantMax = nodo.getCantidad();
                }

                cal.setTime(fechaActual);
                cal.add(Calendar.DATE, 1);
                fechaActual = cal.getTime();
            }

            modelDocumentosSecretariaPorDia.addSeries(series1);

            if (tipoGrafico.equals("Barras")) {
                series1 = new BarChartSeries();
            } else if (tipoGrafico.equals("Lineas")) {
                series1 = new LineChartSeries();
            }
        }

        cantMax = ((Double) (cantMax * 1.3)).intValue();

        // modelDocumentosSecretariaPorDia.setSeriesColors("A30303,58BA27,FFCC33,F74A4A,F52F2F");
        modelDocumentosSecretariaPorDia.setSeriesColors(COLORES_CHART);
        // modelSmsHora.setTitle("Cantidad de SMS's promerio por hora en los ultimos 30 dias");

        modelDocumentosSecretariaPorDia.setLegendPosition(
                "e");
        modelDocumentosSecretariaPorDia.setLegendPlacement(LegendPlacement.OUTSIDE);

        modelDocumentosSecretariaPorDia.setLegendCols(
                2);
        modelDocumentosSecretariaPorDia.setZoom(
                true);
        modelDocumentosSecretariaPorDia.setShowPointLabels(
                true);
        modelDocumentosSecretariaPorDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dia"));
        Axis yAxis = modelDocumentosSecretariaPorDia.getAxis(AxisType.Y);

        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        yAxis.setTickFormat(
                "%'.0f");
        // yAxis.setMax(2100);
        yAxis.setMax(cantMax);

        Axis xAxis = modelDocumentosSecretariaPorDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                30);

        if (tipoGrafico.equals(
                "Barras")) {
            column1.addWidget("chartDocumentosSecretariaPorDiaBar");
        } else if (tipoGrafico.equals(
                "Lineas")) {
            column1.addWidget("chartDocumentosSecretariaPorDiaLineal");
        }

        dashboardModel.addColumn(column1);
    }

    public void initChartDocumentosSecretariaPorDiaPie() {

        dashboardModel = new DefaultDashboardModel();

        DashboardColumn column1 = new DefaultDashboardColumn();

        modelDocumentosSecretariaPorDiaPie = new PieChartModel();

        if (fechaDesde == null) {
            JsfUtil.addErrorMessage("Debe elegir fecha desde.");
            return;
        }

        if (fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe elegir fecha hasta.");
            return;
        }

        if (listaTiposDoc == null) {
            JsfUtil.addErrorMessage("Debe elegir uno o mas tipos de documento.");
            return;
        } else if (listaTiposDoc.isEmpty()) {
            JsfUtil.addErrorMessage("Debe elegir uno o mas tipos de documento.");
            return;
        }
        List<ChartDocumentosSecretariaPorDia> lista2 = ejbFacade.getEntityManager().createNativeQuery(
                "select descripcion_tipo_documento_judicial as codigo, \n"
                + "convert('1900-01-01', date) as fecha_presentacion,\n"
                + "empresa, descripcion_tipo_documento_judicial, tipo_documento_judicial, sum(cantidad) as cantidad from chart_documentos_secretaria_por_dia\n"
                + "group by empresa, descripcion_tipo_documento_judicial, tipo_documento_judicial",
                ChartDocumentosSecretariaPorDia.class).getResultList();
        ChartDocumentosSecretariaPorDia envio = null;

        Iterator<ChartDocumentosSecretariaPorDia> it2 = lista2.iterator();

        while (it2.hasNext()) {

            envio = it2.next();
            modelDocumentosSecretariaPorDiaPie.set(envio.getDescripcionTipoDocumentoJudicial(), envio.getCantidad());
        }

        modelDocumentosSecretariaPorDiaPie.setLegendPosition("e");
        modelDocumentosSecretariaPorDiaPie.setLegendPlacement(LegendPlacement.OUTSIDE);
        modelDocumentosSecretariaPorDiaPie.setLegendCols(2);
        modelDocumentosSecretariaPorDiaPie.setFill(true);
        modelDocumentosSecretariaPorDiaPie.setShowDataLabels(true);
        modelDocumentosSecretariaPorDiaPie.setDiameter(300);
        modelDocumentosSecretariaPorDiaPie.setShadow(true);
        modelDocumentosSecretariaPorDiaPie.setSliceMargin(0);
        modelDocumentosSecretariaPorDiaPie.setShowDatatip(true);

        column1.addWidget("chartDocumentosSecretariaPorDiaPie");

        dashboardModel.addColumn(column1);*/
    }
}
