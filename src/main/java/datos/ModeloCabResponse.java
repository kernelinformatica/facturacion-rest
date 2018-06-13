package datos;

import entidades.ModeloCab;

/**
 *
 * @author FrancoSili
 */
public class ModeloCabResponse {
    private Integer idModeloCab;
    private Integer idEmpresa; 
    private String descripcion;

    public ModeloCabResponse(ModeloCab s) {
        this.idModeloCab = s.getIdModeloCab();
        this.idEmpresa = s.getIdEmpresa();
        this.descripcion = s.getDescripcion();
    }

    public Integer getIdModeloCab() {
        return idModeloCab;
    }

    public void setIdModeloCab(Integer idModeloCab) {
        this.idModeloCab = idModeloCab;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
