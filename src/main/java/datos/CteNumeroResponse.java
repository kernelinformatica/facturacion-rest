package datos;

import entidades.CteNumero;

/**
 *
 * @author FrancoSili
 */
public class CteNumeroResponse implements Payload {
    private Integer idCteNumero;
    private Integer ptoVenta;
    private Integer numero;

    public CteNumeroResponse(CteNumero c) {
        this.idCteNumero = c.getIdCteNumero();
        this.ptoVenta = c.getPtoVenta();
        this.numero = c.getNumero();
    }
    
    

    public Integer getIdCteNumero() {
        return idCteNumero;
    }

    public void setIdCteNumero(Integer idCteNumero) {
        this.idCteNumero = idCteNumero;
    }

    public Integer getPtoVenta() {
        return ptoVenta;
    }

    public void setPtoVenta(Integer ptoVenta) {
        this.ptoVenta = ptoVenta;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }


    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
