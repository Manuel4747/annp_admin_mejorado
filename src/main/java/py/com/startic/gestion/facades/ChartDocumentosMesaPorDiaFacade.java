/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import py.com.startic.gestion.datasource.DateItem;
import py.com.startic.gestion.models.ChartDocumentosMesaPorDia;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;

/**
 *
 * @author eduardo
 */
@Stateless
public class ChartDocumentosMesaPorDiaFacade extends AbstractFacade<ChartDocumentosMesaPorDia> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ChartDocumentosMesaPorDiaFacade() {
        super(ChartDocumentosMesaPorDia.class);
    }


    
    
    public List<ChartDocumentosMesaPorDia> getDocumentosMensual(Date fechaDesde, Date fechaHasta, List<TiposDocumentosJudiciales> listaTiposDoc) {

        return null;
    }

    public List<ChartDocumentosMesaPorDia> getComprasVariosPie(Date fechaDesde, Date fechaHasta, List<TiposDocumentosJudiciales> listaTiposDoc) {
    
        return null;
    }
    /*
    public List<ChartComprasComitePorDia> getComprasVariosPie(Date fechaDesde, Date fechaHasta, List<VComites> listaComites) {

        String comites = " ";

        if (listaComites != null) {
            Iterator<VComites> it = listaComites.iterator();
            
            comites = " and nombre_comite in (";

            VComites comite = null;
            boolean primero = true;
            while (it.hasNext()) {
                comite = it.next();
                if (!primero) {
                    comites += ",";
                }
                primero = false;

                comites += "'" + comite.getNombre() + "'";
            }
            
            comites += ") ";
        }

        Query query = getEntityManager().createNativeQuery(
                "select i.nombre_comite as codigo, i.empresa, to_date('2000/01/01','YYYY/MM/DD') as fecha, i.nombre_comite, count(coalesce(i.cantidad,0)) as cantidad, sum(coalesce(i.monto,0)) as monto \n"
                + " from chart_compras_comite_por_dia as i "
                + " where fecha >= ?1 AND fecha <= ?2 " + comites
                + " group by i.nombre_comite, i.empresa order by i.nombre_comite",
                 ChartComprasComitePorDia.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta);
        return query.getResultList();
    }

    public List<ChartComprasComitePorDia> getComprasVarios(Date fechaDesde, Date fechaHasta, List<VComites> listaComites) {
        String comites = " ";
        String comites2 = " ";

        if (listaComites != null) {
            Iterator<VComites> it = listaComites.iterator();
            
            comites = " where nombre_comite in (";
            comites2 = " where d.nombre in (";

            VComites comite = null;
            boolean primero = true;
            while (it.hasNext()) {
                comite = it.next();
                if (!primero) {
                    comites += ",";
                    comites2 += ",";
                }
                primero = false;

                comites += "'" + comite.getNombre() + "'";
                comites2 += "'" + comite.getNombre() + "'";
            }
            
            comites += ") ";
            comites2 += ") ";
        }else{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            comites2 = " where d.nombre in (select distinct nombre_comite " +
                      " from chart_compras_comite_por_dia fecha where fecha >=  to_date('" + format.format(fechaDesde) + "', 'dd/mm/yyyy') " +
                      " and fecha <= to_date('" + format.format(fechaHasta) + "', 'dd/mm/yyyy')) ";
        }

        Query query = getEntityManager().createNativeQuery(
                "select coalesce(c.codigo,to_char(i.fecha,'yyyymmdd')) || i.nombre_comite as codigo, c.empresa, i.fecha, i.nombre_comite, coalesce(c.cantidad,0) as cantidad, coalesce(c.monto,0) as monto \n"
                + " from (select a.fecha, a.nombre_comite from (select g::date as fecha, d.nombre as nombre_comite from generate_series(?1, \n"
                + "  ?2, '1 day'::interval) as g, v_comites as d " + comites2 + ") as a) as i \n"
                + "  LEFT JOIN (select * from chart_compras_comite_por_dia " + comites + ") as c on (i.fecha = c.fecha AND i.nombre_comite = c.nombre_comite) "
                + "  order by i.nombre_comite, i.fecha",
                 ChartComprasComitePorDia.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta);
        return query.getResultList();
    }

    public List<ChartComprasComitePorDia> getComprasMensual(Date fechaDesde, Date fechaHasta, List<VComites> listaComites) {

        Iterator<VComites> it = listaComites.iterator();
        String comites = "";
        VComites comite = null;
        boolean primero = true;
        while (it.hasNext()) {
            comite = it.next();
            if (!primero) {
                comites += ",";
            }
            primero = false;

            comites += "'" + comite.getNombre() + "'";
        }

        Query query = getEntityManager().createNativeQuery(
                "select coalesce(c.codigo,to_char(i.fecha,'yyyymmdd')) || i.nombre_comite as codigo, c.empresa, i.fecha, i.nombre_comite, coalesce(c.cantidad,0) as cantidad, coalesce(c.monto,0) as monto \n"
                + " from (select a.fecha, a.nombre_comite from (select g::date as fecha, d.nombre as nombre_comite from generate_series(date_trunc('month'::text, ?1::date), \n"
                + "  date_trunc('month'::text, ?2::date) + interval '1 month', '1 month'::interval) as g, v_comites as d where d.nombre in (" + comites + ")) as a) as i \n"
                + "  LEFT JOIN (select to_char(date_trunc('month', fecha),'yyyymm') || nombre_comite || to_char(empresa,'FM0999999') as codigo, \n"
                + "  empresa, date_trunc('month', fecha) as fecha, nombre_comite, sum(cantidad) as cantidad, sum(monto) as monto \n"
                + "   from chart_compras_comite_por_dia \n"
                + "   where nombre_comite in (" + comites + ")  \n"
                + "    group by empresa, date_trunc('month', fecha), nombre_comite) as c on (i.fecha = c.fecha AND i.nombre_comite = c.nombre_comite) \n"
                + "  order by i.nombre_comite, i.fecha",
                 ChartComprasComitePorDia.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta);
        return query.getResultList();
    }
     */
}
