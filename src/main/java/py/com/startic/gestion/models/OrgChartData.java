/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.annotation.ManagedBean;
import jakarta.faces.view.ViewScoped;

/**
 *
 * @author DELL
 */
@ManagedBean // Or @Named
@ViewScoped // Or other appropriate scope
public class OrgChartData implements Serializable {

    private Name name;
    private String manager;
    private String tootip;

    public OrgChartData() {
    }

    public OrgChartData(Name name, String manager, String tootip) {
        this.name = name;
        this.manager = manager;
        this.tootip = tootip;
    }

    public OrgChartData(String v, String f, String manager, String tootip) {
        this.name = new Name(v, f);
        this.manager = manager;
        this.tootip = tootip;
    }

    public OrgChartData(String v, String f, String manager, String tootip, String fechaHora) {
        this.name = new Name(v, f, fechaHora);
        this.manager = manager;
        this.tootip = tootip;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getTootip() {
        return tootip;
    }

    public void setTootip(String tootip) {
        this.tootip = tootip;
    }

    class Name {

        private String v;
        private String f;

        public Name() {
        }

        public Name(String v, String f) {
            this.v = v;
            this.f = v + "<div style=\"color:red; font-style:italic\">" + f + "</div>";
        }

        public Name(String v, String f, String fechaHora) {
            this.v = v;
            this.f = v + "<div style=\"color:red; font-style:italic\">" + f + "</div>" 
                    + "<div style=\"color:green; font-style:bolt\">" + fechaHora + "</div>";
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }

    }
}
