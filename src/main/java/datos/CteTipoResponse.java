package datos;

import entidades.CteTipo;

/**
 *
 * @author FrancoSili
 */
public class CteTipoResponse implements Payload {
    Integer idCteTipo; 
    int codigoComp; 
    String descCorta;
    String descripcion; 
    boolean cursoLegal; 
    int codigoAfip; 
    String surenu;
    String observaciones;
    SisComprobanteResponse comprobante;
    
    public CteTipoResponse(CteTipo c) {
        this.idCteTipo = c.getIdCteTipo();
        this.codigoComp = c.getCodigoComp();
        this.descCorta = c.getDescCorta();
        this.descripcion = c.getDescripcion();
        this.cursoLegal = c.getCursoLegal();
        this.codigoAfip = c.getCodigoAfip();
        this.surenu = c.getSurenu();
        this.observaciones = c.getObservaciones();
        this.comprobante = new SisComprobanteResponse(c.getIdSisComprobante());
    }

    public Integer getIdCteTipo() {
        return idCteTipo;
    }

    public void setIdCteTipo(Integer idCteTipo) {
        this.idCteTipo = idCteTipo;
    }

    public int getCodigoComp() {
        return codigoComp;
    }

    public void setCodigoComp(int codigoComp) {
        this.codigoComp = codigoComp;
    }

    public String getDescCorta() {
        return descCorta;
    }

    public void setDescCorta(String descCorta) {
        this.descCorta = descCorta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCursoLegal() {
        return cursoLegal;
    }

    public void setCursoLegal(boolean cursoLegal) {
        this.cursoLegal = cursoLegal;
    }

    public int getCodigoAfip() {
        return codigoAfip;
    }

    public void setCodigoAfip(int codigoAfip) {
        this.codigoAfip = codigoAfip;
    }

    public String getSurenu() {
        return surenu;
    }

    public void setSurenu(String surenu) {
        this.surenu = surenu;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
    public SisComprobanteResponse getComprobante() {
        return comprobante;
    }

    public void setComprobante(SisComprobanteResponse comprobante) {
        this.comprobante = comprobante;
    }
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
