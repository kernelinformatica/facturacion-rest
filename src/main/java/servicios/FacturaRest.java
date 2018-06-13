package servicios;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.FacturaResponse;
import datos.ModeloDetalleResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.FormaPago;
import entidades.ListaPrecio;
import entidades.ListaPrecioDet;
import entidades.ModeloDetalle;
import entidades.Producto;
import entidades.SisFormaPago;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.ModeloDetalleFacade;
import persistencia.ProductoFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("factura")
public class FacturaRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject ProductoFacade productoFacade;
    @Inject ModeloDetalleFacade modeloFacade;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFactura(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
                       
            // Obtengo los atributos del body
            List<JsonElement> productos = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("productos", jsonBody, "ArrayList");
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

            if(productos.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Lista de Precios sin detalles");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            List<ModeloDetalleResponse> modelos = new ArrayList<>();
            for(JsonElement j : productos) {
                Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", j.getAsJsonObject(), "Integer");
                BigDecimal precio = (BigDecimal) Utils.getKeyFromJsonObject("precio", j.getAsJsonObject(), "BigDecimal");
                Integer cantidad = (Integer) Utils.getKeyFromJsonObject("cantidad", j.getAsJsonObject(), "Integer");
                
                if(idProducto == null || cantidad == null || precio == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, algun campo esta vacio");
                    return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
                }
                
                //Busco el producto
                Producto producto = productoFacade.find(idProducto);
                if(producto == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, el producto con id:  " + idProducto + " no existe");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Pregunto si tiene un modelo asociado
                if(producto.getIdModeloCab().getModeloDetalleCollection().isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, el producto:  " + producto.getDescripcion() + "no tiene un modelo asociado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Agrego a la lista de modelos response
                for(ModeloDetalle p : producto.getIdModeloCab().getModeloDetalleCollection()) {
                    ModeloDetalleResponse modeloResponse = new ModeloDetalleResponse(p, precio.multiply(new BigDecimal(cantidad)));
                    modelos.add(modeloResponse);
                } 
            }
            
            List<FacturaResponse> listaFacturas = new ArrayList<>();
            List<Payload> lista = new ArrayList<>();
            Map<String, List<FacturaResponse>> map = new HashMap<>();
            //Armo la lista de facturas response a partir de la lista de modelos obtenidas para todos los productos
            for(ModeloDetalleResponse mdr : modelos) {
                FacturaResponse fr = new FacturaResponse(mdr.getCtaContable(),mdr.getDescripcion(),mdr.getTotalModelo());
                listaFacturas.add(fr);
            }
            //Separo por cuenta contable la lista de facturas y los seteo en el Map
            for(FacturaResponse fr : listaFacturas) {
                String key  = fr.getCuentaContable();
                if(map.containsKey(key)){
                    List<FacturaResponse> list = map.get(key);
                    list.add(fr);
                }else{
                    List<FacturaResponse> list = new ArrayList<>();
                    list.add(fr);
                    map.put(key, list);
                }
            }
            //Recorro el map
            for (Map.Entry<String,  List<FacturaResponse>> entry : map.entrySet()) {
                BigDecimal total = new BigDecimal(BigInteger.ZERO);
                String descripcion = "";
                String cuentaContable = "";
                //Recorro la lista dentro del Map
                for(FacturaResponse fr : entry.getValue()) {
                    //De acuerdo al operador tengo que realizar distintas operaciones.
                    total = total.add(fr.getImporteTotal());
                    descripcion = fr.getDescripcion();
                    cuentaContable = fr.getCuentaContable();
                }
                //Armo la respuesta final
                FacturaResponse fr = new FacturaResponse(cuentaContable,descripcion,total);
                lista.add(fr);
            }
            //System.out.println(map);
            respuesta.setArraydatos(lista);
            respuesta.setControl(AppCodigo.OK, "Lista de Modelos");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    } 
}
