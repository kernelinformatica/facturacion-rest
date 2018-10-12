package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.ModeloDetalleResponse;
import datos.Payload;
import datos.PendientesCancelarResponse;
import datos.ProductoResponse;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.ListaPrecio;
import entidades.ListaPrecioDet;
import entidades.ModeloDetalle;
import entidades.Producto;
import entidades.SisModulo;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.ListaPrecioFacade;
import persistencia.ProductoFacade;
import persistencia.SisModuloFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("buscaPendientes")
public class PendientesCancelarRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject Utils utils; 
    @Inject ProductoFacade productoFacade;
    @Inject SisModuloFacade sisModuloFacade;
    @Inject ListaPrecioFacade listaPrecioFacade;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendientesCancelar(  
        @HeaderParam ("token") String token,  
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        ServicioResponse respuesta = new ServicioResponse();
        try {  
            
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            Integer cteTipo = (Integer) Utils.getKeyFromJsonObject("cteTipo", jsonBody, "Integer");
            BigDecimal facNumero = (BigDecimal) Utils.getKeyFromJsonObject("facNumero", jsonBody, "BigDecimal");
            Integer codigoProv = (Integer) Utils.getKeyFromJsonObject("codigoProv", jsonBody, "Integer");
            Integer pendiente = (Integer) Utils.getKeyFromJsonObject("pendiente", jsonBody, "Integer");
            Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", jsonBody, "Integer");
            Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", jsonBody, "Integer");
            String despacho = (String) Utils.getKeyFromJsonObject("despacho", jsonBody, "String");
            
            //valido que token no sea null
            if(token == null || token.trim().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el token
            Acceso userToken = accesoFacade.findByToken(token);

            //valido que Acceso no sea null
            if(userToken == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el usuario
            Usuario user = usuarioFacade.getByToken(userToken);

            //valido que el Usuario no sea null
            if(user == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //valido vencimiento token
            if(!accesoFacade.validarToken(userToken, user)) {
                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
            }
            
            //valido que el Usuario no sea null
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Debe seleccionar un Tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //seteo el nombre del store
            String noombreSP = "call s_comprobantesPendientes(?,?,?,?,?,?,?,?)";
            
            //invoco al store
            CallableStatement callableStatement = this.utils.procedimientoAlmacenado(user, noombreSP);
            
            //valido que el Procedimiento Almacenado no sea null
            if(callableStatement == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            callableStatement.setInt(1,user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
            callableStatement.setInt(2,cteTipo);
            callableStatement.setBigDecimal(3, facNumero);
            callableStatement.setInt(4, codigoProv);
            callableStatement.setInt(5, pendiente);
            callableStatement.setInt(6, idProducto);
            callableStatement.setInt(7,idDeposito);
            callableStatement.setString(8,despacho);
            
            ResultSet rs = callableStatement.executeQuery();
            List<Payload> pendientes = new ArrayList<>();
                while (rs.next()) {
                    Producto prod = productoFacade.find(rs.getInt("idProductos"));
                    if(prod == null) {
                        break;
                    }
                    ProductoResponse producto = new ProductoResponse(prod);
                    producto.getModeloCab().agregarModeloDetalleImputacion(prod.getIdModeloCab().getModeloDetalleCollection(),rs.getString("imputacion") );
                    PendientesCancelarResponse pendientesCancelar = new PendientesCancelarResponse(
                            rs.getString("comprobante"),
                            rs.getString("numero"),
                            rs.getBigDecimal("original"),
                            rs.getBigDecimal("pendiente"),
                            rs.getBigDecimal("precio"),
                            rs.getBigDecimal("dolar"),
                            rs.getString("moneda"),
                            rs.getBigDecimal("porCalc"),
                            rs.getBigDecimal("ivaPorc"),
                            rs.getInt("deposito"),
                            rs.getInt("idFactDetalleImputada"),
                            rs.getInt("idFactCabImputada"),
                            rs.getBigDecimal("descuento"),
                            rs.getString("tipoDescuento"),
                            rs.getBigDecimal("cantBultos"),
                            rs.getString("despacho"),
                            rs.getString("observaciones"),
                            rs.getInt("itemImputada"),
                            rs.getBigDecimal("importe"),
                            producto);
                    pendientes.add(pendientesCancelar);
                }
            //Cierro la conexion    
            callableStatement.getConnection().close();
            
            if(pendientes.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No hay Comprobantes Pendientes");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
                
            respuesta.setArraydatos(pendientes);
            respuesta.setControl(AppCodigo.OK, "Lista de Comprobantes Pendientes");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path ("/{idProducto}")
    public Response getProductos(  
        @HeaderParam ("token") String token,
        @QueryParam ("idSisTipoModelo") Integer idSisTipoModelo,
        @QueryParam ("modulo") Integer idModulo,
        @QueryParam ("listaPrecio") Integer idListaPrecios,
        @PathParam ("idProducto") Integer idProducto,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            //valido que token no sea null
            if(token == null || token.trim().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el token
            Acceso userToken = accesoFacade.findByToken(token);

            //valido que Acceso no sea null
            if(userToken == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el usuario
            Usuario user = usuarioFacade.getByToken(userToken);

            //valido que el Usuario no sea null
            if(user == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //valido vencimiento token
            if(!accesoFacade.validarToken(userToken, user)) {
                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
            }
            
            //Busco los productos de la empresa
            List<Producto> productos = productoFacade.getProductosByEmpresa(user.getIdPerfil().getIdSucursal().getIdEmpresa());
            
            //Valido que haya productos para esa empresa.
            if(productos.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No hay Productos disponibles");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            List<Payload> productosResponse = new ArrayList<>();
            
            if(idModulo != null && idSisTipoModelo != null && idProducto != null &&  idListaPrecios == null) {
                //Busco el modulo(Por lo general en este if viene el de compras)
                SisModulo modulo = sisModuloFacade.find(idModulo);
                if(modulo == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el modulo");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }             
                
                //Busco el producto
                Producto s = productoFacade.find(idProducto);                
                if(s == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el producto");
                    return Response.status(Response.Status.NOT_FOUND).entity(respuesta.toJson()).build();
                }              
                
                //Armo la respuesta
                ProductoResponse sr = new ProductoResponse(s);
                //Agrego las cuentas contables
                sr.getModeloCab().agregarModeloDetalleTipo(s.getIdModeloCab().getModeloDetalleCollection(), idSisTipoModelo, idModulo);
                
                //Si tiene un lote asignado no se puede editar
                if(!s.getLoteCollection().isEmpty()) {
                    sr.setEditar(false);
                }
                
                //Seteo el iva del producto dependiendo la cuenta contable que tiene asignada
                for(ModeloDetalle r : s.getIdModeloCab().getModeloDetalleCollection()) {
                    if(r.getIdSisModulo().getIdSisModulos().equals(modulo.getIdSisModulos()) && 
                        r.getValor().compareTo(BigDecimal.ZERO) != 0 &&
                        r.getIdSisTipoModelo().getIdSisTipoModelo().equals(2)) {
                        sr.getIVA().setPorcIVA(r.getValor());
                        break;
                    }
                }
                
                //Armo la respuesta de pendientes de cancelar para que sea la misma que viene del store procedure
                PendientesCancelarResponse pr = new PendientesCancelarResponse(sr);
                productosResponse.add(pr);               
            } else if(idListaPrecios != null && idModulo != null && idSisTipoModelo != null && idProducto != null) {
                //Busco la lista de precios seleccionada
                ListaPrecio listaPrecio = listaPrecioFacade.find(idListaPrecios);
                if(listaPrecio == null) {
                    respuesta.setControl(AppCodigo.ERROR, "No hay lista de precios disponibles");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Busco el modulo. (Por lo general en este if va a ser el de ventas)
                SisModulo modulo = sisModuloFacade.find(idModulo);
                if(modulo == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el modulo");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Busco el producto
                Producto prod = productoFacade.find(idProducto);
                if(prod == null) {
                    respuesta.setControl(AppCodigo.ERROR, "No existe el producto seleccionado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Busco el producto en la lista de precios
                for(ListaPrecioDet l : listaPrecio.getListaPrecioDetCollection()) {
                    //Si no es el producto que continue
                    if(!l.getIdProductos().equals(prod)) {
                        continue;
                    }                    
                    //Armo el producto response
                    ProductoResponse sr = new ProductoResponse(l.getIdProductos());
                    //Seteo el costo en costo de repocicion el de la lista de precio
                    sr.setCostoReposicion(l.getPrecio());
                    //Agrego los detalles de cuentas contables
                    sr.getModeloCab().agregarModeloDetalleTipo(l.getIdProductos().getIdModeloCab().getModeloDetalleCollection(), idSisTipoModelo, idModulo);
                    //Seteo el iva del producto dependiendo del detalle si es != de 0 seteo el del detalle sino el del producto
                    for(ModeloDetalle r : prod.getIdModeloCab().getModeloDetalleCollection()) {
                        if(r.getIdSisModulo().getIdSisModulos().equals(modulo.getIdSisModulos()) && 
                           r.getValor().compareTo(BigDecimal.ZERO) != 0 &&
                           r.getIdSisTipoModelo().getIdSisTipoModelo().equals(2)) {
                            sr.getIVA().setPorcIVA(r.getValor());
                            break;
                        }
                    }
                    //Armo la respuesta de pendientes de cancelar asi es igual a la del store procedure
                    PendientesCancelarResponse pr = new PendientesCancelarResponse(sr);
                    //seteo el precio de la lista de precios seleccionada
                    pr.setPrecio(l.getPrecio());
                    productosResponse.add(pr);
                }                
            } else {                  
                for(Producto s : productos) {
                    ProductoResponse pr = new ProductoResponse(s);
                    pr.getModeloCab().agregarModeloDetalleTipo(s.getIdModeloCab().getModeloDetalleCollection(),idSisTipoModelo, idModulo);
                    PendientesCancelarResponse sr = new PendientesCancelarResponse(pr);
                    productosResponse.add(sr);
                }
            }
            respuesta.setArraydatos(productosResponse);
            respuesta.setControl(AppCodigo.OK, "Lista de Productos");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
