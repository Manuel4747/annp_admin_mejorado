/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.facades;

import py.com.startic.gestion.facades.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.annp.persistencia.models.FnTipoDeVariacionRp;

/**
 *
 * @author lichi
 */
@Stateless
public class FnTipoDeVariacionRpFacade extends AbstractFacade<FnTipoDeVariacionRp> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FnTipoDeVariacionRpFacade() {
        super(FnTipoDeVariacionRp.class);
    }
    
}
