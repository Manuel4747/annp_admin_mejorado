/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.VPermisosUsuarios;

/**
 *
 * @author eduardo
 */
@Named(value = "filtroURL")
@ViewScoped
public class FiltroURL implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent pe) {
        
        
        /*
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect("https://172.16.0.8:6262/administrativo");
        } catch (IOException ex) {
            Logger.getLogger(FiltroURL.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        */
        
        FacesContext facesContext = pe.getFacesContext();

        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isPageLogin = currentPage.lastIndexOf("login.xhtml") > -1;

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacion.xhtml") > -1;
        }

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

        Object usuario = session.getAttribute("Usuarios");

        if (!isPageLogin && usuario == null) {
            NavigationHandler handler = facesContext.getApplication().getNavigationHandler();
            handler.handleNavigation(facesContext, null, "login.xhtml");
        } else if (!isPageLogin) {

            if (!verifPermiso(currentPage, null)) {
                NavigationHandler handler = facesContext.getApplication().getNavigationHandler();
                handler.handleNavigation(facesContext, null, "/denegado.xhtml");
            }

        }
    }

    public boolean verifPermiso(String url) {
        return verifPermiso(url, null);
    }

    public boolean verifPermiso(String url, String permiso) {

        boolean found = false;
        
        if (url.contains("reportes")) {
            found = true;
        } else {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            List<VPermisosUsuarios> p = (List<VPermisosUsuarios>) session.getAttribute("VPermisosUsuarios");
            if (p!=null) {
                Iterator<VPermisosUsuarios> it = p.iterator();
                VPermisosUsuarios permUsua = null;

                while (it.hasNext()) {

                    permUsua = it.next();

                    if (permUsua.getFuncion().equals(url)) {
                        if (permiso != null) {
                            if (permUsua.getPermiso().equals(permiso) || permUsua.getPermiso().equals("TOTAL")) {
                                found = true;
                                break;
                            }
                        } else {
                            found = true;
                            break;
                        }
                    }
                }
            }
            if (!found) {

                System.out.println("No tiene acceso a " + url + ", permiso " + ((permiso == null) ? "null" : permiso));
            }

        }
        return found;
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
