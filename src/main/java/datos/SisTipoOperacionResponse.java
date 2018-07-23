package datos;

import entidades.SisTipoOperacion;

/**
 *
 * @author FrancoSili
 */
public class SisTipoOperacionResponse implements Payload {
    private Integer idSisTipoOperacion;
    private String descripcion;
    private boolean canje;
    private SisModuloResponse modulo;    
    
    public SisTipoOperacionResponse(SisTipoOperacion s) {
        this.idSisTipoOperacion = s.getIdSisTipoOperacion();
        this.descripcion = s.getDescripcion();
        this.canje = s.getCanje();
        this.modulo = new SisModuloResponse(s.getIdSisModulos());
    }
    
    public Integer getIdSisTipoOperacion() {
        return idSisTipoOperacion;
    }

    public void setIdSisTipoOperacion(Integer idSisTipoOperacion) {
        this.idSisTipoOperacion = idSisTipoOperacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SisModuloResponse getModulo() {
        return modulo;
    }

    public void setModulo(SisModuloResponse modulo) {
        this.modulo = modulo;
    }

    public boolean isCanje() {
        return canje;
    }

    public void setCanje(boolean canje) {
        this.canje = canje;
    }
    
    

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
