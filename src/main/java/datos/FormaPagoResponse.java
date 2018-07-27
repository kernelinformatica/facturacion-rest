
package datos;

import entidades.FormaPago;
import entidades.FormaPagoDet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Franco Sili
 */
public class FormaPagoResponse implements Payload {
    private int idFormaPago;
    private String descripcion;
    private boolean editar;
    private ListaPreciosResponse listaPrecio;
    private SisFormaPagoResponse tipo;
    private List<FormaPagoDetResponse> formaPagoDet;

    public FormaPagoResponse(FormaPago f) {
        this.idFormaPago = f.getIdFormaPago();
        this.descripcion = f.getDescripcion();
        this.tipo = new SisFormaPagoResponse(f.getTipo());
        this.listaPrecio = new ListaPreciosResponse(f.getIdListaPrecios());
        this.editar = true;
        this.formaPagoDet = new ArrayList<>();
    }
    
    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }
    
    public SisFormaPagoResponse getTipo() {
        return tipo;
    }

    public void setTipo(SisFormaPagoResponse tipo) {
        this.tipo = tipo;
    }
    
    
    public ListaPreciosResponse getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(ListaPreciosResponse listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public List<FormaPagoDetResponse> getFormaPagoDet() {
        return formaPagoDet;
    }

    public void setFormaPagoDet(List<FormaPagoDetResponse> formaPagoDet) {
        this.formaPagoDet = formaPagoDet;
    }
    
    public void agregarDetalles(Collection<FormaPagoDet> formaPagos) {
        for(FormaPagoDet f : formaPagos) {
            FormaPagoDetResponse re = new FormaPagoDetResponse(f);
            this.formaPagoDet.add(re);
        }
    }

    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
