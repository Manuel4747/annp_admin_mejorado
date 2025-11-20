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
import jakarta.inject.Inject;
/*import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
*/
import py.com.startic.gestion.datasource.CantidadItem;
import py.com.startic.gestion.datasource.ChartAntecedentesPorDia;
import py.com.startic.gestion.models.Antecedentes;

@Named(value = "chartAntecedentesPorDiaController")
@ViewScoped
public class ChartAntecedentesPorDiaController extends AbstractController<ChartAntecedentesPorDia> {

    @Inject
    private EmpresasController empresaController;

/*    private BarChartModel modelAntecedentesPorDiaBar;
    private BarChartModel modelGestionesAtrasadasPorGrupoBar;
    private LineChartModel modelGestionesPorDiaLinear;
    private DonutChartModel modelGestionesPorDiaPie;*/
    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<String> coloresFondo;    
    private List<String> coloresBorde;    
    private Integer usuariosCreados;   
    private Integer magistradosHabilitados;
    private Integer magistradosTotal;
    private Integer totalAntecedentes;
    private List<Antecedentes> listaAntecedentes;
    private String titulo;
    
    // "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c"


/*    public DonutChartModel getModelGestionesPorDiaPie() {
        return modelGestionesPorDiaPie;
    }

    public void setModelGestionesPorDiaPie(DonutChartModel modelGestionesorDiaPie) {
        this.modelGestionesPorDiaPie = modelGestionesorDiaPie;
    }

    public BarChartModel getModelGestionesAtrasadasPorGrupoBar() {
        return modelGestionesAtrasadasPorGrupoBar;
    }

    public void setModelGestionesAtrasadasPorGrupoBar(BarChartModel modelGestionesAtrasadasPorGrupoBar) {
        this.modelGestionesAtrasadasPorGrupoBar = modelGestionesAtrasadasPorGrupoBar;
    }
*/
    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }
/*
    public LineChartModel getModelGestionesPorDiaLinear() {
        return modelGestionesPorDiaLinear;
    }

    public void setModelGestionesPorDiaLinear(LineChartModel modelGestionesPorDiaLinear) {
        this.modelGestionesPorDiaLinear = modelGestionesPorDiaLinear;
    }

    public BarChartModel getModelAntecedentesPorDiaBar() {
        return modelAntecedentesPorDiaBar;
    }

    public void setModelAntecedentesPorDiaBar(BarChartModel modelAntecedentesPorDiaBar) {
        this.modelAntecedentesPorDiaBar = modelAntecedentesPorDiaBar;
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

    public Integer getUsuariosCreados() {
        return usuariosCreados;
    }

    public void setUsuariosCreados(Integer usuariosCreados) {
        this.usuariosCreados = usuariosCreados;
    }

    public Integer getMagistradosHabilitados() {
        return magistradosHabilitados;
    }

    public void setMagistradosHabilitados(Integer magistradosHabilitados) {
        this.magistradosHabilitados = magistradosHabilitados;
    }

    public Integer getMagistradosTotal() {
        return magistradosTotal;
    }

    public void setMagistradosTotal(Integer magistradosTotal) {
        this.magistradosTotal = magistradosTotal;
    }

    public ChartAntecedentesPorDiaController() {
        // Inform the Abstract parent controller of the concrete VComprasPorRubroPorMes Entity
        super(ChartAntecedentesPorDia.class);
    }

    public Integer getTotalAntecedentes() {
        return totalAntecedentes;
    }

    public void setTotalAntecedentes(Integer totalAntecedentes) {
        this.totalAntecedentes = totalAntecedentes;
    }

    public List<Antecedentes> getListaAntecedentes() {
        return listaAntecedentes;
    }

    public void setListaAntecedentes(List<Antecedentes> listaAntecedentes) {
        this.listaAntecedentes = listaAntecedentes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @PostConstruct
    public void init() {
        
        coloresFondo = new ArrayList<>();
/*
        COLORES_CHART.add("#31a00d");
        COLORES_CHART.add("#fffb2f");
        COLORES_CHART.add("#4a148c");
        COLORES_CHART.add("#827717");
        COLORES_CHART.add("#0d47a1");
        COLORES_CHART.add("#f57f17");
        COLORES_CHART.add("#FFFF00");
        COLORES_CHART.add("#bf360c");
        COLORES_CHART.add("#3e2723");
        COLORES_CHART.add("#880e4f");
        COLORES_CHART.add("#b71c1c");
        */
                
        coloresFondo.add("rgba(255, 159, 64, 0.2)");
        coloresFondo.add("rgba(75, 192, 192, 0.2)");
        coloresFondo.add("rgba(255, 99, 132, 0.2)");
        coloresFondo.add("rgba(54, 162, 235, 0.2)");
        coloresFondo.add("rgba(255, 205, 86, 0.2)");
        coloresFondo.add("rgba(153, 102, 255, 0.2)");
        coloresFondo.add("rgba(201, 203, 207, 0.2)");
        
        coloresBorde = new ArrayList<>();
        
        coloresBorde.add("rgb(255, 159, 64)");
        coloresBorde.add("rgb(75, 192, 192)");
        coloresBorde.add("rgb(255, 99, 132)");
        coloresBorde.add("rgb(54, 162, 235)");
        coloresBorde.add("rgb(255, 205, 86)");
        coloresBorde.add("rgb(153, 102, 255)");
        coloresBorde.add("rgb(201, 203, 207)");
        
        tipoGrafico = "Barras";
        fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        fechaHasta = ejbFacade.getSystemDateOnly();
        antecedentesPorDia();

    }

    public void antecedentesPorDia() {
        antecedentesPorDiaBarras();
    }

    public void antecedentesPorDiaBarras() {
        List<ChartAntecedentesPorDia> lista = null;

        Date fechaActual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }
        
        
        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "SELECT count(*) AS cantidad FROM personas where usuario is not null and id in (select id from antecedentes_permisos_por_roles where permiso = 1) and estado = 'AC'"
                , CantidadItem.class);
        CantidadItem cantidadItem = (CantidadItem) query.getSingleResult();
        usuariosCreados = cantidadItem.getCantidad();
/*
        
        query = ejbFacade.getEntityManager().createNativeQuery(
                "SELECT count(*) AS cantidad FROM personas where habilitar_antecedentes = true and estado = 'AC'"
                , CantidadItem.class);
        CantidadItem cantidadItem2 = (CantidadItem) query.getSingleResult();
        magistradosHabilitados = cantidadItem2.getCantidad();
        
        query = ejbFacade.getEntityManager().createNativeQuery(
                "SELECT count(*) AS cantidad FROM personas where estado = 'AC'"
                , CantidadItem.class);
        CantidadItem cantidadItem3 = (CantidadItem) query.getSingleResult();
        magistradosTotal = cantidadItem3.getCantidad();
        */

        query = ejbFacade.getEntityManager().createNativeQuery(
                "SELECT count(*) AS cantidad FROM antecedentes"
                , CantidadItem.class);
        CantidadItem cantidadItem4 = (CantidadItem) query.getSingleResult();
        totalAntecedentes = cantidadItem4.getCantidad();
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaHasta);
        cal2.add(Calendar.DATE, 1);
        Date fechaHastaNueva = cal2.getTime();
        
        listaAntecedentes = ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findByRangoFechaHoraAlta", Antecedentes.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHastaNueva).getResultList();

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }
        
        titulo = "Antecedentes Generados entre " + sdf2.format(fechaDesde) + " y " + sdf2.format(fechaHasta);

        ChartAntecedentesPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");


        Calendar cal = Calendar.getInstance();
        
        ChartAntecedentesPorDia nodo = null;

        ChartAntecedentesPorDia nodoCero = new ChartAntecedentesPorDia();

        nodoCero.setCantidad(0);

//        modelAntecedentesPorDiaBar = new BarChartModel();
 //       ChartData data = new ChartData();
         
        /*
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
         
        List<String> borderColor = new ArrayList<>();
        
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        */
         
         
        List<String> labels = new ArrayList<>();

        List<ChartAntecedentesPorDia> listaTemp;
        
//        BarChartDataSet barDataSet;

        lista = new ArrayList<>();
        List<Number> values = new ArrayList<>();
        
        boolean completarLabels = true;

        int posColor = 0;
        
        //while (it3.hasNext()) {

            listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartAntecedentesPorDia.findByRangoFechaAlta", ChartAntecedentesPorDia.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
            lista.addAll(listaTemp);

            Iterator<ChartAntecedentesPorDia> it2 = listaTemp.iterator();

            envio = new ChartAntecedentesPorDia();
            envio.setFechaAlta(fechaCero);

            boolean iterar = true;

            fechaActual.setTime(fechaDesde.getTime());
            /*
            barDataSet = new BarChartDataSet();
            barDataSet.setBackgroundColor(coloresFondo.get(posColor));
            
            barDataSet.setBorderColor(coloresBorde.get(posColor));
            barDataSet.setBorderWidth(1);
            */
            posColor++;
            if(posColor >= coloresFondo.size()){
                posColor = 0;
            }

            values = new ArrayList<>();
            while (fechaActual.compareTo(fechaHasta) <= 0) {

                if (iterar) {
                    if (it2.hasNext()) {
                        envio = it2.next();
                    }
                    iterar = false;
                }

                if (envio.getFechaAlta().compareTo(fechaActual) != 0) {
                    nodoCero.setFechaAlta(fechaActual);
                    nodo = nodoCero;
                } else {
                    nodo = envio;
                    iterar = true;
                }

                // series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
            
//                barDataSet.setLabel("Antecedentes");
                
                values.add(nodo.getCantidad());
                
                if(completarLabels){
                    labels.add(format.format(nodo.getFechaAlta()));
                }
                
                if (cantMax < nodo.getCantidad()) {
                    cantMax = nodo.getCantidad();
                }

                cal.setTime(fechaActual);
                cal.add(Calendar.DATE, 1);
                fechaActual = cal.getTime();
            }
            
            
            completarLabels = false;
            
            
/*            barDataSet.setData(values);
            data.addChartDataSet(barDataSet);

        //}
        
        
        data.setLabels(labels);
        modelAntecedentesPorDiaBar.setData(data);*/
/*
       cantMax = ((Double) (cantMax * 1.5)).intValue();

       //cantMax = cantMax * 2;
       
        // modelGestionesMesaPorDia.setSeriesColors("A30303,58BA27,FFCC33,F74A4A,F52F2F");
        modelGestionesMesaPorDia.setSeriesColors(COLORES_CHART);
        // modelSmsHora.setTitle("Cantidad de SMS's promerio por hora en los ultimos 30 dias");

        modelGestionesMesaPorDia.setLegendPosition(
                "e");
        modelGestionesMesaPorDia.setLegendPlacement(LegendPlacement.OUTSIDE);

        modelGestionesMesaPorDia.setLegendCols(
                2);
        modelGestionesMesaPorDia.setZoom(
                true);
        modelGestionesMesaPorDia.setShowPointLabels(
                true);
        modelGestionesMesaPorDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dia"));
        Axis yAxis = modelGestionesMesaPorDia.getAxis(AxisType.Y);

        yAxis.setTickInterval("2");
        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        yAxis.setTickFormat(
                "%'.0f");
        // yAxis.setMax(2100);
        yAxis.setMax(cantMax);

        Axis xAxis = modelGestionesMesaPorDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                30);
*/
    }
/*
    public void antecedentesPorDiaBarras() {
        List<ChartAntecedentesPorDia> lista = null;

        Date fechaActual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }

        ChartAntecedentesPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");


        Calendar cal = Calendar.getInstance();
        
        ChartAntecedentesPorDia nodo = null;

        ChartAntecedentesPorDia nodoCero = new ChartAntecedentesPorDia();

        nodoCero.setCantidad(0);

        modelAntecedentesPorDiaBar = new BarChartModel();
        ChartData data = new ChartData();
         
         
        List<String> labels = new ArrayList<>();

        List<ChartAntecedentesPorDia> listaTemp;
        
        BarChartDataSet barDataSet;

        lista = new ArrayList<>();
        List<Number> values = new ArrayList<>();
        
        boolean completarLabels = true;

        int posColor = 0;
        
        //while (it3.hasNext()) {

            listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartAntecedentesPorDia.findByRangoFechaAlta", ChartAntecedentesPorDia.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
            lista.addAll(listaTemp);

            Iterator<ChartAntecedentesPorDia> it2 = listaTemp.iterator();

            envio = new ChartAntecedentesPorDia();
            envio.setFechaAlta(fechaCero);

            boolean iterar = true;

            fechaActual.setTime(fechaDesde.getTime());
            
            barDataSet = new BarChartDataSet();
            barDataSet.setBackgroundColor(coloresFondo.get(posColor));
            
            barDataSet.setBorderColor(coloresBorde.get(posColor));
            barDataSet.setBorderWidth(1);
            
            posColor++;
            if(posColor >= coloresFondo.size()){
                posColor = 0;
            }

            values = new ArrayList<>();
            while (fechaActual.compareTo(fechaHasta) <= 0) {

                if (iterar) {
                    if (it2.hasNext()) {
                        envio = it2.next();
                    }
                    iterar = false;
                }

                if (envio.getFechaAlta().compareTo(fechaActual) != 0) {
                    nodoCero.setFechaAlta(fechaActual);
                    nodo = nodoCero;
                } else {
                    nodo = envio;
                    iterar = true;
                }

                // series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
            
                barDataSet.setLabel("Antecedentes");
                
                values.add(nodo.getCantidad());
                
                if(completarLabels){
                    labels.add(format.format(nodo.getFechaAlta()));
                }
                
                if (cantMax < nodo.getCantidad()) {
                    cantMax = nodo.getCantidad();
                }

                cal.setTime(fechaActual);
                cal.add(Calendar.DATE, 1);
                fechaActual = cal.getTime();
            }
            
            
            completarLabels = false;
            
            
            barDataSet.setData(values);
            data.addChartDataSet(barDataSet);

        //}
        
        
        data.setLabels(labels);
        modelAntecedentesPorDiaBar.setData(data);
        
    }
*/
}
