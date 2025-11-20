/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.startic.gestion.models.DetallesEntradaArticulo;

/**
 *
 * @author grecia
 */
@Stateless
public class DetallesEntradaArticuloFacade extends AbstractFacade<DetallesEntradaArticulo> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    /**
     *
     * @return
     */
    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DetallesEntradaArticuloFacade() {
        super(DetallesEntradaArticulo.class);
    }
    
}
