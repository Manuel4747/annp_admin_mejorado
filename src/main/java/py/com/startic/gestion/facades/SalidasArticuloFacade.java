/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.startic.gestion.models.SalidasArticulo;

/**
 *
 * @author grecia
 */
@Stateless
public class SalidasArticuloFacade extends AbstractFacade<SalidasArticulo> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SalidasArticuloFacade() {
        super(SalidasArticulo.class);
    }
    
}
