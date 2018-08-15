package datos;

import entidades.CteNumerador;
import entidades.CteNumero;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author FrancoSili
 */
public class CteNumeradorResponse implements Payload {
    private Integer idCteNumerador;
    private String descripcion;
    private List<CteNumeroResponse> numero;

    public CteNumeradorResponse(CteNumerador c) {
        this.idCteNumerador = c.getIdCteNumerador();
        this.descripcion = c.getDescripcion();
        this.numero = new ArrayList<>();
    }
    
    public Integer getIdCteNumerador() {
        return idCteNumerador;
    }

    public void setIdCteNumerador(Integer idCteNumerador) {
        this.idCteNumerador = idCteNumerador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<CteNumeroResponse> getNumero() {
        return numero;
    }

    public void setNumero(List<CteNumeroResponse> numero) {
        this.numero = numero;
    }
    
    public void agregarNumero(Collection<CteNumero> numero) {
        //agrego ctenumero a la lista de numeros y le sumo uno mas a punto de venta
        for(CteNumero c : numero) {
            CteNumeroResponse cteNumero = new CteNumeroResponse(c);
            cteNumero.setNumero(cteNumero.getNumero()+ 1);
            this.numero.add(cteNumero);
        }
    }
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
