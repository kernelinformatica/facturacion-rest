package datos;

import entidades.SisTipoModelo;

/**
 *
 * @author FrancoSili
 */
public class SisTipoModeloResponse implements Payload{
    private int idTipoModelo;
    private String descripcion;

    public SisTipoModeloResponse(SisTipoModelo s) {
        this.idTipoModelo = s.getIdSisTipoModelo();
        this.descripcion = s.getTipo();
    }
    
    public int getIdTipoModelo() {
        return idTipoModelo;
    }

    public void setIdTipoModelo(int idTipoModelo) {
        this.idTipoModelo = idTipoModelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
