package datos;

import entidades.UsuarioListaPrecio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Franco Sili
 */
public class UsuarioResponse implements Payload{
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private PerfilResponse perfil;
    private String clave;
    private List<ListaPreciosResponse> listaPrecios;

    public UsuarioResponse(entidades.Usuario u) {
        this.id = u.getIdUsuarios();
        this.email = u.getMail();
        this.nombre = u.getNombre();
        this.perfil = new PerfilResponse(u.getIdPerfil());
        this.telefono = u.getTelefono();
        this.clave = u.getClave();
        this.listaPrecios = new ArrayList<>();
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public PerfilResponse getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilResponse perfil) {
        this.perfil = perfil;
    }
    
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<ListaPreciosResponse> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<ListaPreciosResponse> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }
    
    public void agregarListaPrecios(Collection<UsuarioListaPrecio> lista) {
        for(UsuarioListaPrecio l : lista) {
            ListaPreciosResponse lr  = new ListaPreciosResponse(l.getIdListaPrecios());
            this.listaPrecios.add(lr);
        }
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
