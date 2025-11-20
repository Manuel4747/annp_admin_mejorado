/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.startic.gestion.models.Actividades;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;

/**
 *
 * @author grecia
 */
@Stateless
public class DetallesPlanEstrategicasFacade extends AbstractFacade<DetallesPlanEstrategicas> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DetallesPlanEstrategicasFacade() {
        super( DetallesPlanEstrategicas.class);
    }
    
}
