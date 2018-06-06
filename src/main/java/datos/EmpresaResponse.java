package datos;

import entidades.Empresa;

/**
 *
 * @author FrancoSili
 */
public class EmpresaResponse {
    private Integer idEmpresa;
    private String nombre;
    private String domicilio;
    private String cuit;
    private String iibb;
    private String logo;


    public EmpresaResponse (Empresa empresa) {
        this.idEmpresa = empresa.getIdEmpresa();
        this.nombre = empresa.getNombre();
        this.domicilio = empresa.getDomicilio();
        this.cuit = empresa.getCuit();
        this.iibb = empresa.getIibb();
        //this.logo = empresa.getLogo(); TODO: Agregar logo a BD   
    }
    
    ////////////////////////////////////////////////////////////
    /////////             GETTERS AND SETTERS          /////////
    ////////////////////////////////////////////////////////////

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getIibb() {
        return iibb;
    }

    public void setIibb(String iibb) {
        this.iibb = iibb;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    
}
