package datos;

import entidades.SisComprobante;

/**
 *
 * @author FrancoSili
 */
public class SisComprobanteResponse implements Payload{
    private Integer idSisComprobantes;
    private String descripcion;
    private String imputacion;
    private SisModuloResponse modulo;

    public SisComprobanteResponse(SisComprobante s) {
       this.idSisComprobantes = s.getIdSisComprobantes();
       this.descripcion = s.getDescripcion();
       this.imputacion = s.getImputacion();
       this.modulo = new SisModuloResponse(s.getIdSisModulos());
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

    public String getImputacion() {
        return imputacion;
    }

    public void setImputacion(String imputacion) {
        this.imputacion = imputacion;
    }

    public SisModuloResponse getModulo() {
        return modulo;
    }

    public void setModulo(SisModuloResponse modulo) {
        this.modulo = modulo;
    }
    
    

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
