package datos;

import entidades.SisComprobante;

/**
 *
 * @author FrancoSili
 */
public class SisComprobanteResponse implements Payload{
    private Integer idSisComprobantes;
    private String descripcion;
    private String modulo;
    private String imputacion;

    public SisComprobanteResponse(SisComprobante s) {
       this.idSisComprobantes = s.getIdSisComprobantes();
       this.descripcion = s.getDescripcion();
       this.modulo = s.getModulo();
       this.imputacion = s.getImputacion();
    }
    
    public Integer getIdSisComprobantes() {
        return idSisComprobantes;
    }

    public void setIdSisComprobantes(Integer idSisComprobantes) {
        this.idSisComprobantes = idSisComprobantes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getImputacion() {
        return imputacion;
    }

    public void setImputacion(String imputacion) {
        this.imputacion = imputacion;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
