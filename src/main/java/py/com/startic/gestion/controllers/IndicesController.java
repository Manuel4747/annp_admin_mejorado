package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.Indices;
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
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
*/
@Named(value = "indicesController")
@ViewScoped
public class IndicesController extends AbstractController<Indices> {

    @Inject
    private EmpresasController empresaController;
/*    private LineChartModel modelSmsDia;
    private BarChartModel modelInsumo;
    private PieChartModel modelSmsHoy;
    private PieChartModel modelCategoria;
    private DashboardModel dashboardModel;
*/
    public IndicesController() {
        // Inform the Abstract parent controller of the concrete Permisos Entity
        super(Indices.class);
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

    //    dashboardModel = new DefaultDashboardModel();

   //     DashboardColumn column1 = new DefaultDashboardColumn();
   //     DashboardColumn column2 = new DefaultDashboardColumn();

        initChartSmsPorDia();
        initChartSmsHoy();
        initChartCategoria();
        initChartInsumos();
        
     //   column1.addWidget("smsdia");
        //column1.addWidget("smshora");

        //column2.addWidget("metaAno");
   //     column2.addWidget("smshoy");
    //    column1.addWidget("activo1");
      //  column2.addWidget("activo2");

      // dashboardModel.addColumn(column1);
     //   dashboardModel.addColumn(column2);

        // dashboard.getChildren().add(chart);
    }
/*
    public BarChartModel getModelInsumo() {
        return modelInsumo;
    }

    public void setModelInsumo(BarChartModel modelInsumo) {
        this.modelInsumo = modelInsumo;
    }

    public PieChartModel getModelCategoria() {
        return modelCategoria;
    }

    public void setModelCategoria(PieChartModel modelCategoria) {
        this.modelCategoria = modelCategoria;
    }

    public LineChartModel getModelSmsDia() {
        return modelSmsDia;
    }

    public PieChartModel getModelSmsHoy() {
        return modelSmsHoy;
    }

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }    
    */
    private void initChartSmsPorDia() {/*
        modelSmsDia = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Cantidad Documentos");
        
        Collection<Indices> lista = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "DOCUMENTOS_X_DIA").getResultList();

        Iterator<Indices> it = lista.iterator();

        Indices envio = null;
        
        long cantMax = 0;
        
        while (it.hasNext()) {

            envio = it.next();
            
            series1.set(envio.getValorX(), Integer.valueOf(envio.getValorY()));

            
            if(cantMax < Integer.valueOf(envio.getValorY())){
                cantMax = Integer.valueOf(envio.getValorY());
            }
        }

        Double maxEscala = new Double(cantMax / 60);
        
        if(!maxEscala.equals(maxEscala.longValue())){
            maxEscala = new Double(maxEscala.longValue() + 1);
        }
        
        maxEscala = maxEscala * 60;

        modelSmsDia.addSeries(series1);

        modelSmsDia.setMouseoverHighlight(
                false);

        // modelSmsDia.setTitle("Cantidad de SMS's enviados por dia en los ultimos 30 dias");
        modelSmsDia.setLegendPosition(
                "e");
        modelSmsDia.setShowPointLabels(
                true);
        modelSmsDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dias"));
        Axis yAxis = modelSmsDia.getAxis(AxisType.Y);

        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        // yAxis.setMax(15000);
        yAxis.setMax(
                maxEscala);

        Axis xAxis = modelSmsDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                45);
*/
    }

    private void initChartInsumos() {/*
        modelInsumo = new BarChartModel();

        BarChartSeries series1 = new BarChartSeries();
        series1.setLabel("Lapices");
        
        Collection<Indices> lista = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "INSUMOS_X_DIA").getResultList();

        Iterator<Indices> it = lista.iterator();

        Indices envio = null;
        
        long cantMax = 0;
        
        while (it.hasNext()) {

            envio = it.next();
            
            series1.set(envio.getValorX(), Integer.valueOf(envio.getValorY()));

            
            if(cantMax < Integer.valueOf(envio.getValorY())){
                cantMax = Integer.valueOf(envio.getValorY());
            }
        }
        
        /***********/
        /*
        BarChartSeries series2 = new BarChartSeries();
        series2.setLabel("Resmas de Papel");
        
        Collection<Indices> lista2 = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "INSUMOS2_X_DIA").getResultList();

        Iterator<Indices> it2 = lista2.iterator();

        Indices envio2 = null;
        
        while (it2.hasNext()) {

            envio2 = it2.next();
            
            series2.set(envio2.getValorX(), Integer.valueOf(envio2.getValorY()));

            
            if(cantMax < Integer.valueOf(envio2.getValorY())){
                cantMax = Integer.valueOf(envio2.getValorY());
            }
        }

        /************/
        
    /*    BarChartSeries series3 = new BarChartSeries();
        series3.setLabel("Tinta Impresora");
        
        Collection<Indices> lista3 = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "INSUMOS3_X_DIA").getResultList();

        Iterator<Indices> it3 = lista3.iterator();

        Indices envio3 = null;
        
        while (it3.hasNext()) {

            envio3 = it3.next();
            
            series3.set(envio3.getValorX(), Integer.valueOf(envio3.getValorY()));

            
            if(cantMax < Integer.valueOf(envio3.getValorY())){
                cantMax = Integer.valueOf(envio3.getValorY());
            }
        }

        /************/   
    /*    
        BarChartSeries series4 = new BarChartSeries();
        series4.setLabel("Insumos de limpieza");
        
        Collection<Indices> lista4 = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "INSUMOS4_X_DIA").getResultList();

        Iterator<Indices> it4 = lista4.iterator();

        Indices envio4 = null;
        
        while (it4.hasNext()) {

            envio4 = it4.next();
            
            series4.set(envio4.getValorX(), Integer.valueOf(envio4.getValorY()));

            
            if(cantMax < Integer.valueOf(envio4.getValorY())){
                cantMax = Integer.valueOf(envio4.getValorY());
            }
        }

        /************/

/*
        Double maxEscala = new Double(cantMax / 60);
        
        if(!maxEscala.equals(maxEscala.longValue())){
            maxEscala = new Double(maxEscala.longValue() + 1);
        }
        
        maxEscala = maxEscala * 60;

        modelInsumo.addSeries(series1);
        modelInsumo.addSeries(series2);
        modelInsumo.addSeries(series3);
        modelInsumo.addSeries(series4);

        modelInsumo.setMouseoverHighlight(
                false);

        // modelSmsDia.setTitle("Cantidad de SMS's enviados por dia en los ultimos 30 dias");
        modelInsumo.setLegendPosition(
                "e");
        modelInsumo.setShowPointLabels(
                true);
        modelInsumo.getAxes()
                .put(AxisType.X, new CategoryAxis("Dias"));
        Axis yAxis = modelInsumo.getAxis(AxisType.Y);

        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        // yAxis.setMax(15000);
        yAxis.setMax(
                maxEscala);

        Axis xAxis = modelInsumo.getAxis(AxisType.X);

        xAxis.setTickAngle(
                45);
*/
    }

    private void initChartSmsHoy() {/*
        modelSmsHoy = new PieChartModel();

        Collection<Indices> lista = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "DOCUMENTOS_X_DPTO").getResultList();

        Iterator<Indices> it = lista.iterator();

        Indices envio = null;

        int cont = 0;

        while (it.hasNext()) {

            cont++;
            envio = it.next();

            modelSmsHoy.set(envio.getValorX(), Integer.valueOf(envio.getValorY()));
        }
//
//        if (cont == 0) {
//            modelSmsHoy.set("No hay mensajes hoy", 0);
//        }

        //modelSmsHoy.setTitle("Monitoreo de SMS's de hoy");
        modelSmsHoy.setLegendPosition("e");
        modelSmsHoy.setFill(true);
        modelSmsHoy.setShowDataLabels(true);
        modelSmsHoy.setDiameter(150);
        modelSmsHoy.setShadow(true);
        modelSmsHoy.setSliceMargin(10);
        modelSmsHoy.setShowDatatip(true);*/
    }
    
    private void initChartCategoria() {/*
        modelCategoria = new PieChartModel();

        Collection<Indices> lista = ejbFacade.getEntityManager().createNamedQuery("Indices.findByNombreIndice", Indices.class).setParameter("nombreIndice", "BIENES_X_CATEGORIA").getResultList();

        Iterator<Indices> it = lista.iterator();

        Indices envio = null;

        int cont = 0;

        while (it.hasNext()) {

            cont++;
            envio = it.next();

            modelCategoria.set(envio.getValorX(), Integer.valueOf(envio.getValorY()));
        }
//
//        if (cont == 0) {
//            modelSmsHoy.set("No hay mensajes hoy", 0);
//        }

        //modelSmsHoy.setTitle("Monitoreo de SMS's de hoy");
     /*   modelCategoria.setLegendPosition("e");
        modelCategoria.setFill(true);
        modelCategoria.setShowDataLabels(true);
        modelCategoria.setDiameter(150);
        modelCategoria.setShadow(true);
        modelCategoria.setSliceMargin(10);
        modelCategoria.setShowDatatip(true);*/
    }

}
