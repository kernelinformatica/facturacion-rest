package servicios;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteTipo;
import entidades.FormaPago;
import entidades.ListaPrecio;
import entidades.ListaPrecioDet;
import entidades.Producto;
import entidades.SisMonedas;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
import persistencia.CteTipoFacade;
import persistencia.FormaPagoFacade;
import persistencia.SisMonedasFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */
@Stateless
@Path("grabaComprobante")
public class GrabaComprobanteRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject SisMonedasFacade sisMonedasFacade;
    @Inject CteTipoFacade cteTipoFacade;
    @Inject FormaPagoFacade formaPagoFacade;
    
        
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setComprobante(
    @HeaderParam ("token") String token,
    @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            String idCteTipo = (String) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "String");
            String letra = (String) Utils.getKeyFromJsonObject("letra", jsonBody, "String");
            BigDecimal numero = (BigDecimal) Utils.getKeyFromJsonObject("numero", jsonBody, "BigDecimal");
            Date fechaEmision = (Date) Utils.getKeyFromJsonObject("fechaEmision", jsonBody, "Date");
            Date fechaVencimiento = (Date) Utils.getKeyFromJsonObject("fechaVencimiento", jsonBody, "Date");
            Date fechaConta = (Date) Utils.getKeyFromJsonObject("fechaConta", jsonBody, "Date");
            String cai = (String) Utils.getKeyFromJsonObject("cai", jsonBody, "String");
            Date caiVto = (Date) Utils.getKeyFromJsonObject("caiVto", jsonBody, "Date");
            String codBarra =(String) Utils.getKeyFromJsonObject("codBarra", jsonBody, "String");
            Integer codProveedor = (Integer) Utils.getKeyFromJsonObject("codProveedor", jsonBody, "Integer");
            Integer idFormaPago = (Integer) Utils.getKeyFromJsonObject("idFormaPago", jsonBody, "Integer");
            Integer idProductoCanje = (Integer) Utils.getKeyFromJsonObject("idProductoCanje", jsonBody, "Integer");
            BigDecimal precioReferenciaCanje = (BigDecimal) Utils.getKeyFromJsonObject("precioReferenciaCanje", jsonBody, "BigDecimal");
            BigDecimal interesCane = (BigDecimal) Utils.getKeyFromJsonObject("interesCane", jsonBody, "BigDecimal");
            Integer idMoneda = (Integer) Utils.getKeyFromJsonObject("idMoneda", jsonBody, "Integer");
            String nombre = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            String cuit = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            Integer idSisSitIva = (Integer) Utils.getKeyFromJsonObject("idSisSitIva", jsonBody, "Integer");
            String codigoPostal = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            Integer idListaPrecio = (Integer) Utils.getKeyFromJsonObject("idListaPrecio", jsonBody, "Integer");
            Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", jsonBody, "Integer");
            BigDecimal cotDolar = (BigDecimal) Utils.getKeyFromJsonObject("cotDolar", jsonBody, "BigDecimal");
            Date fechaDolar = (Date) Utils.getKeyFromJsonObject("fechaDolar", jsonBody, "Date");
            String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", jsonBody, "String");
            Integer idModeloCab = (Integer) Utils.getKeyFromJsonObject("idModeloCab", jsonBody, "Integer");
            List<JsonElement> factDetalle = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("factDetalle", jsonBody, "ArrayList");           
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

            //Me fijo que  los campos que tienen el atributo NotNull no sean nulos
            if(idCteTipo == null || letra == null || numero == null || fechaEmision == null || 
               fechaVencimiento == null || codProveedor == null || idFormaPago == null || 
               idProductoCanje == null || precioReferenciaCanje == null || interesCane == null || 
               idMoneda == null ||nombre == null || cuit == null || idSisSitIva == null || 
               codigoPostal == null || idDeposito == null || cotDolar== null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo esta en nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Metodos de busquedas de clases por id
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            //Pregunto si existe el CteTipo
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe el tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            FormaPago formaPago = formaPagoFacade.find(idFormaPago);
            //Pregunto si existe la forma de pago
            if(formaPago == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Forma de Pago");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            SisMonedas sisMonedas = sisMonedasFacade.find(idMoneda);
            //Pregunto si existe SisMonedas
            if(sisMonedas == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Moneda");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            boolean transaccion;
//            for(JsonElement j : preciosDet) {
//                boolean transaccion2;
//                ListaPrecioDet listaPrecioDet = new ListaPrecioDet();
//                BigDecimal precio = (BigDecimal) Utils.getKeyFromJsonObject("precio", j.getAsJsonObject(), "BigDecimal");
//                BigDecimal cotaInf = (BigDecimal) Utils.getKeyFromJsonObject("cotaInf", j.getAsJsonObject(), "BigDecimal");
//                BigDecimal cotaSup = (BigDecimal) Utils.getKeyFromJsonObject("cotaSup", j.getAsJsonObject(), "BigDecimal");
//                String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", j.getAsJsonObject(), "String");
//                Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", j.getAsJsonObject(), "Integer"); 
//                if(precio == null || cotaInf == null || cotaSup == null || idProducto == 0) {
//                    respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, algun campo esta vacio");
//                    return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
//                }
//                Producto producto = productoFacade.find(idProducto);
//                if(producto == null) {
//                    listaPrecioFacade.deleteListaPrecio(listaPrecio);
//                    respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, el producto con id " + idProducto + " no existe");
//                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//                }
//                listaPrecioDet.setIdListaPrecios(listaPrecio);
//                listaPrecioDet.setPrecio(precio);
//                listaPrecioDet.setCotaInf(cotaInf);
//                listaPrecioDet.setCotaSup(cotaSup);
//                listaPrecioDet.setObservaciones(observaciones);
//                listaPrecioDet.setIdProductos(producto);
//                transaccion2 = listaPrecioDetFacade.setListaPrecioDetNuevo(listaPrecioDet);
//                if(!transaccion2) {
//                    listaPrecioFacade.deleteListaPrecio(listaPrecio);
//                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Detalle con el producto con codigo: "+ producto.getCodProducto() +", clave primaria repetida");
//                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//                }
//            }
            respuesta.setControl(AppCodigo.OK, "Lista de Precios creado con exito, con detalles");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    } 
}
