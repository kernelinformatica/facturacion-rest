package datos;

import entidades.Menu;
import entidades.Sucursal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Franco Sili
 */
public class SucursalResponse implements Payload {
    private Integer idSucursal;
    private String nombre;
    private String domicilio;
    private String codigoPostal;
    private EmpresaResponse empresa;
    private List<MenuSucursalResponse> menuSucursal;

    public SucursalResponse (Sucursal sucursal) {
        this.idSucursal = sucursal.getIdSucursal();
        this.nombre = sucursal.getNombre();
        this.domicilio = sucursal.getDomicilio();
        this.codigoPostal = sucursal.getCodigoPostal();
        this.empresa = new EmpresaResponse(sucursal.getIdEmpresa());
        this.menuSucursal = new ArrayList<>();    
    }
    
    ////////////////////////////////////////////////////////////
    /////////             GETTERS AND SETTERS          /////////
    ////////////////////////////////////////////////////////////

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public EmpresaResponse getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaResponse empresa) {
        this.empresa = empresa;
    }
    
    
    public List<MenuSucursalResponse> getMenuSucursal() {
        return menuSucursal;
    }

    public void setMenuSucursal(List<MenuSucursalResponse> menuSucursal) {
        this.menuSucursal = menuSucursal;
    }
    
    public void agregarMenuSucursal(Menu menu) {
       MenuSucursalResponse msr = new MenuSucursalResponse(menu);
       this.menuSucursal.add(msr);
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
