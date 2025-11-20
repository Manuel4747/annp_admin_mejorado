package py.com.startic.gestion.controllers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import py.com.startic.gestion.models.EstadosUsuario;
import py.com.startic.gestion.models.Usuarios;
import py.com.startic.gestion.models.UsuariosAsociados;
import py.com.startic.gestion.models.VPermisosUsuarios;

public class Query {

    private Usuarios l = null;
    private List<VPermisosUsuarios> p = null;
    private EntityManagerFactory emf;
    private EntityManager em;

    public Query() {
        emf = Persistence.createEntityManagerFactory("gestionstarticPU");
        em = emf.createEntityManager();
        //em.getTransaction().begin();
    }

    public Usuarios loginControl(String username, String password, String estado) {
        try {
            
            EstadosUsuario est = new EstadosUsuario();
            
            est.setCodigo(estado);
            
            l = em.createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", username).setParameter("contrasena", password).setParameter("estado", est).getSingleResult();
            if (l != null) {
                
                // 88
                
                List<UsuariosAsociados> usuAs = em.createNamedQuery("UsuariosAsociados.findByUsuarioAsociado", UsuariosAsociados.class).setParameter("usuarioAsociado", l).getResultList();
                if(usuAs.size() > 1){
                    return null;
                }
                
                if(usuAs.isEmpty()){
                    p = em.createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", l.getId()).getResultList();
                }else{
                    p = em.createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", usuAs.get(0).getUsuario()).getResultList();
                }

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VPermisosUsuarios", p);
 
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", l);
               
                return l;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
