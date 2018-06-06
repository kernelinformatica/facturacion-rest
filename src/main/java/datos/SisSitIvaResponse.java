package datos;

import entidades.SisSitIVA;

/**
 *
 * @author FrancoSili
 */
class SisSitIvaResponse {

    private Integer idSisSitIVA;
    private String descripcion;
    private String desCorta;
    
    public SisSitIvaResponse(SisSitIVA s) {
        this.desCorta = s.getDesCorta();
        this.descripcion = s.getDescripcion();
        this.idSisSitIVA = s.getIdSisSitIVA();
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
    
    
}
