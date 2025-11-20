/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.startic.gestion.facades.AbstractFacade;


/**
 *
 * @author grecia
 */
@Stateless
public class PlanEstrategicasFacade extends AbstractFacade<PlanEstrategicas> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public PlanEstrategicasFacade() {
        super(PlanEstrategicas.class);
    }
    
}
