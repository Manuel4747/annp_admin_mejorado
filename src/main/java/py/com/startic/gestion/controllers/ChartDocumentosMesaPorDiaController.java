package py.com.startic.gestion.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
/*
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
import org.primefaces.model.chart.PieChartModel;
*/
import py.com.startic.gestion.controllers.util.JsfUtil;

import py.com.startic.gestion.models.ChartDocumentosMesaPorDia;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;

@Named(value = "chartDocumentosMesaPorDiaController")
@ViewScoped
public class ChartDocumentosMesaPorDiaController extends AbstractController<ChartDocumentosMesaPorDia> {

    @Inject
    private EmpresasController empresaController;

/*    private BarChartModel modelDocumentosMesaPorDiaBar;
    private DashboardModel dashboardModel;
    private LineChartModel modelDocumentosMesaPorDiaLinear;
    private PieChartModel modelDocumentosMesaPorDiaPie;*/
    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<TiposDocumentosJudiciales> listaTiposDoc;
    static final public String COLORES_CHART = "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c";

    private List<ChartDocumentosMesaPorDia> lista;
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
    }
*/
    public List<ChartDocumentosMesaPorDia> getLista() {
        return lista;
    }

    public void setLista(List<ChartDocumentosMesaPorDia> lista) {
        this.lista = lista;
    }

    public List<TiposDocumentosJudiciales> getListaTiposDoc() {
        return listaTiposDoc;
    }

    public void setListaTiposDoc(List<TiposDocumentosJudiciales> listaTiposDoc) {
        this.listaTiposDoc = listaTiposDoc;
    }
/*
    public BarChartModel getModelDocumentosMesaPorDiaBar() {
        return modelDocumentosMesaPorDiaBar;
    }

    public void setModelDocumentosMesaPorDiaBar(BarChartModel modelDocumentosMesaPorDiaBar) {
        this.modelDocumentosMesaPorDiaBar = modelDocumentosMesaPorDiaBar;
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
    public ChartDocumentosMesaPorDiaController() {
        // Inform the Abstract parent controller of the concrete VComprasPorRubroPorMes Entity
        super(ChartDocumentosMesaPorDia.class);
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
        fechaDesde = ejbFacade.getSystemDateOnly(-15);
        fechaHasta = ejbFacade.getSystemDateOnly();
        listaTiposDoc = ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosJudiciales.findAll", TiposDocumentosJudiciales.class).getResultList();
        initChartDocumentosMesaPorDia();

    }

    public void initChartDocumentosMesaPorDia() {
        lista = null;
        if (tipoGrafico.equals("Barras") || tipoGrafico.equals("Lineas")) {
            initChartDocumentosMesaPorDiaCartesianos();
        } else if (tipoGrafico.equals("Torta")) {
            initChartDocumentosMesaPorDiaPie();
        }
    }

    public void initChartDocumentosMesaPorDiaCartesianos() {

       // dashboardModel = new DefaultDashboardModel();

        Date fechaActual = new Date();

        //DashboardColumn column1 = new DefaultDashboardColumn();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        //CartesianChartModel modelDocumentosMesaPorDia = null;

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(-15);
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

        ChartDocumentosMesaPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");

//        ChartSeries series1;

        Calendar cal = Calendar.getInstance();

        Iterator<TiposDocumentosJudiciales> it3 = listaTiposDoc.iterator();

        TiposDocumentosJudiciales tipoDoc = null;

        ChartDocumentosMesaPorDia nodo = null;

        ChartDocumentosMesaPorDia nodoCero = new ChartDocumentosMesaPorDia();

        nodoCero.setCantidad(0);
/*
        if (tipoGrafico.equals("Barras")) {
            modelDocumentosMesaPorDiaBar = new BarChartModel();
            modelDocumentosMesaPorDia = modelDocumentosMesaPorDiaBar;
            series1 = new BarChartSeries();
        } else {
            modelDocumentosMesaPorDiaLinear = new LineChartModel();
            modelDocumentosMesaPorDia = modelDocumentosMesaPorDiaLinear;
            series1 = new LineChartSeries();
        }
*/
        List<ChartDocumentosMesaPorDia> listaTemp;

        lista = new ArrayList<>();

        while (it3.hasNext()) {

            tipoDoc = it3.next();
            listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartDocumentosMesaPorDia.findByRangoFechaPresentacion", ChartDocumentosMesaPorDia.class).setParameter("tipoDocumentoJudicial", tipoDoc.getCodigo()).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
            lista.addAll(listaTemp);

            Iterator<ChartDocumentosMesaPorDia> it2 = listaTemp.iterator();

//            series1.setLabel(tipoDoc.getDescripcion());

            envio = new ChartDocumentosMesaPorDia();
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

//                series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
                if (cantMax < nodo.getCantidad()) {
                    cantMax = nodo.getCantidad();
                }

                cal.setTime(fechaActual);
                cal.add(Calendar.DATE, 1);
                fechaActual = cal.getTime();
            }
/*
            modelDocumentosMesaPorDia.addSeries(series1);

            if (tipoGrafico.equals("Barras")) {
                series1 = new BarChartSeries();
            } else if (tipoGrafico.equals("Lineas")) {
                series1 = new LineChartSeries();
            }*/
        }

       cantMax = ((Double) (cantMax * 1.5)).intValue();

       //cantMax = cantMax * 2;
       
        // modelDocumentosMesaPorDia.setSeriesColors("A30303,58BA27,FFCC33,F74A4A,F52F2F");
    /*    modelDocumentosMesaPorDia.setSeriesColors(COLORES_CHART);
        // modelSmsHora.setTitle("Cantidad de SMS's promerio por hora en los ultimos 30 dias");

        modelDocumentosMesaPorDia.setLegendPosition(
                "e");
        modelDocumentosMesaPorDia.setLegendPlacement(LegendPlacement.OUTSIDE);

        modelDocumentosMesaPorDia.setLegendCols(
                2);
        modelDocumentosMesaPorDia.setZoom(
                true);
        modelDocumentosMesaPorDia.setShowPointLabels(
                true);
        modelDocumentosMesaPorDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dia"));
        Axis yAxis = modelDocumentosMesaPorDia.getAxis(AxisType.Y);

        yAxis.setTickInterval("2");
        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        yAxis.setTickFormat(
                "%'.0f");
        // yAxis.setMax(2100);
        yAxis.setMax(cantMax);

        Axis xAxis = modelDocumentosMesaPorDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                30);

        if (tipoGrafico.equals(
                "Barras")) {
            column1.addWidget("chartDocumentosMesaPorDiaBar");
        } else if (tipoGrafico.equals(
                "Lineas")) {
            column1.addWidget("chartDocumentosMesaPorDiaLineal");
        }

        dashboardModel.addColumn(column1);*/
    }

    public void initChartDocumentosMesaPorDiaPie() {
/*
        dashboardModel = new DefaultDashboardModel();

        DashboardColumn column1 = new DefaultDashboardColumn();

        modelDocumentosMesaPorDiaPie = new PieChartModel();

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
        List<ChartDocumentosMesaPorDia> lista2 = ejbFacade.getEntityManager().createNativeQuery(
                "select descripcion_tipo_documento_judicial as codigo, \n"
                + "convert('1900-01-01', date) as fecha_presentacion,\n"
                + "empresa, descripcion_tipo_documento_judicial, tipo_documento_judicial, sum(cantidad) as cantidad from chart_documentos_mesa_por_dia\n"
                + "group by empresa, descripcion_tipo_documento_judicial, tipo_documento_judicial",
                ChartDocumentosMesaPorDia.class).getResultList();
        ChartDocumentosMesaPorDia envio = null;

        Iterator<ChartDocumentosMesaPorDia> it2 = lista2.iterator();

        while (it2.hasNext()) {

            envio = it2.next();
            modelDocumentosMesaPorDiaPie.set(envio.getDescripcionTipoDocumentoJudicial(), envio.getCantidad());
        }

        modelDocumentosMesaPorDiaPie.setLegendPosition("e");
        modelDocumentosMesaPorDiaPie.setLegendPlacement(LegendPlacement.OUTSIDE);
        modelDocumentosMesaPorDiaPie.setLegendCols(2);
        modelDocumentosMesaPorDiaPie.setFill(true);
        modelDocumentosMesaPorDiaPie.setShowDataLabels(true);
        modelDocumentosMesaPorDiaPie.setDiameter(300);
        modelDocumentosMesaPorDiaPie.setShadow(true);
        modelDocumentosMesaPorDiaPie.setSliceMargin(0);
        modelDocumentosMesaPorDiaPie.setShowDatatip(true);

        column1.addWidget("chartDocumentosMesaPorDiaPie");

        dashboardModel.addColumn(column1);*/
    }
}
