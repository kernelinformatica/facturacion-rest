package datos;

import entidades.PtoVenta;

/**
 *
 * @author FrancoSili
 */
public class PtoVentaResponse implements Payload {
    private Integer idPtoVenta;
    private Integer ptoVenta;

    public PtoVentaResponse(PtoVenta c) {
        this.idPtoVenta = c.getIdPtoVenta();
        this.ptoVenta = c.getPtoVenta();
    }

    public Integer getIdPtoVenta() {
        return idPtoVenta;
    }

    public void setIdPtoVenta(Integer idPtoVenta) {
        this.idPtoVenta = idPtoVenta;
    }
       
    public Integer getPtoVenta() {
        return ptoVenta;
    }

    public void setPtoVenta(Integer ptoVenta) {
        this.ptoVenta = ptoVenta;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
