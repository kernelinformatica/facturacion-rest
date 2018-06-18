package datos;

import entidades.ModeloCab;
import entidades.ModeloDetalle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author FrancoSili
 */
public class ModeloCabResponse {
    private Integer idModeloCab;
    private Integer idEmpresa; 
    private String descripcion;
    private List<ModeloDetalleResponse> modeloDetalle;

    public ModeloCabResponse(ModeloCab s) {
        this.idModeloCab = s.getIdModeloCab();
        this.idEmpresa = s.getIdEmpresa();
        this.descripcion = s.getDescripcion();
        this.modeloDetalle = new ArrayList<>();
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

    public List<ModeloDetalleResponse> getModeloDetalle() {
        return modeloDetalle;
    }

    public void setModeloDetalle(List<ModeloDetalleResponse> modeloDetalle) {
        this.modeloDetalle = modeloDetalle;
    }

    
    public void agregarModeloDetalle(Collection<ModeloDetalle> lista) {
        for(ModeloDetalle l : lista) {
            ModeloDetalleResponse lr = new ModeloDetalleResponse(l);
            this.modeloDetalle.add(lr);
        }
    }
    
    
}
