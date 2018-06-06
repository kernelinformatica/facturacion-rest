package datos;

import entidades.Deposito;

/**
 *
 * @author FrancoSili
 */
public class DepositoResponse implements Payload {

    private Integer idDeposito;    
    private int codigoDep;   
    private String descripcion;    
    private String domicilio;   
    private String codigoPostal;

    public DepositoResponse(Deposito s) {
        this.idDeposito = s.getIdDepositos();
        this.descripcion = s.getDescripcion();
        this.codigoPostal = s.getCodigoPostal();
        this.domicilio = s.getDomicilio();
        this.codigoDep = s.getCodigoDep();
    }
    public Integer getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(Integer idDeposito) {
        this.idDeposito = idDeposito;
    }

    public int getCodigoDep() {
        return codigoDep;
    }

    public void setCodigoDep(int codigoDep) {
        this.codigoDep = codigoDep;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
