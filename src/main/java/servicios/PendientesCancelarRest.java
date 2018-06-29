package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.Payload;
import datos.PendientesCancelarResponse;
import datos.ProductoResponse;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.Producto;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.ProductoFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("buscarPendientes")
public class PendientesCancelarRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject Utils utils; 
    @Inject ProductoFacade productoFacade;
    
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
                    producto.getModeloCab().agregarModeloDetalle(prod.getIdModeloCab().getModeloDetalleCollection());
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
                            rs.getBigDecimal("cantBultos"),
                            rs.getString("despacho"),
                            rs.getString("observaciones"),
                            rs.getInt("itemImputada"),
                            producto);
                    pendientes.add(pendientesCancelar);
                }
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
    public Response getProductos(  
        @HeaderParam ("token") String token,  
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
            
            //busco los SubRubros de la empresa del usuario
            List<Payload> productosResponse = new ArrayList<>();
            for(Producto s : productos) {
                ProductoResponse pr = new ProductoResponse(s);
                pr.getModeloCab().agregarModeloDetalle(s.getIdModeloCab().getModeloDetalleCollection());
                PendientesCancelarResponse sr = new PendientesCancelarResponse(pr);
                productosResponse.add(sr);
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
