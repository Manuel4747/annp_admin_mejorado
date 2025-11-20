/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import py.com.startic.gestion.models.RhFeriados;

/**
 *
 * @author DELL
 */
@Stateless
public class RhFeriadosFacade extends AbstractFacade<RhFeriados> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public RhFeriadosFacade() {
        super(RhFeriados.class);
    }
}
