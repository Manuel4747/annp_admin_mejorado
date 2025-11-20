package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.ArchivosAdministrativo;
import py.com.startic.gestion.models.DocumentosAdministrativos;
import py.com.startic.gestion.models.ParametrosSistema;

@Named(value = "validacionController")
@ViewScoped
public class ValidacionController extends AbstractController<DocumentosAdministrativos> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private DocumentosAdministrativos ant;
    private String endpoint;
    private ParametrosSistema par;
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ValidacionController() {
        // Inform the Abstract parent controller of the concrete DocumentosAdministrativos Entity
        super(DocumentosAdministrativos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        // Obtenemos el nro de telefono enviado por parametro
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String hash = params.get("hash");
        // hash = hash.replace(" ", "+");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        if (hash != null) {
            
            byte[] decoded = Base64.getDecoder().decode(hash);
            
            String hashFinal;
            try {
               // hashFinal = Utils.decryptMsg(decoded, Utils.generateKey());
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            
//            Integer hashInt = Integer.valueOf(hashFinal);
            try {
//                ant = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", hashInt).getSingleResult();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fecha = sdf.format(ant.getFechaPresentacion());
                /*
                Date fecha = ejbFacade.getSystemDate();
                
                long diffInMillies = Math.abs(fecha.getTime() - ant.getFechaHoraAlta().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if(diff > 30){
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/Caducado.xhtml");
                    return;
                }
                nombresApellidos = ant.getPersona().getNombresApellidos();
                 */
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        } else {
            /*
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            */
            return;
        }

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    @Override
    public Collection<DocumentosAdministrativos> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findAll", DocumentosAdministrativos.class).getResultList());
        return super.getItems2();
    }
    public void validar(DocumentosAdministrativos doc) {
        if(doc != null){
            ant = doc;
            validar();
        }
    }

    public void validar() {
        List<ArchivosAdministrativo> lista2 = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", ant).getResultList();
        if (!lista2.isEmpty()) {
            verDoc(lista2.get(0).getRuta());
        }
    }

    public void verDoc(String archivo) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            try {
                fileByte = Files.readAllBytes(new File(par.getRutaArchivosAdministrativo() + "/" + archivo).toPath());
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        }
    }
}
