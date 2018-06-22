package datos;

import entidades.SisSitIVA;

/**
 *
 * @author FrancoSili
 */
public class SisSitIvaResponse implements Payload {

    private Integer idSisSitIVA;
    private String descripcion;
    private String desCorta;
    private String letra;
    
    public SisSitIvaResponse(SisSitIVA s) {
        this.desCorta = s.getDesCorta();
        this.descripcion = s.getDescripcion();
        this.idSisSitIVA = s.getIdSisSitIVA();
        this.letra = s.getLetra();
    }

    public Integer getIdSisSitIVA() {
        return idSisSitIVA;
    }

    public void setIdSisSitIVA(Integer idSisSitIVA) {
        this.idSisSitIVA = idSisSitIVA;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesCorta() {
        return desCorta;
    }

    public void setDesCorta(String desCorta) {
        this.desCorta = desCorta;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }
    

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
