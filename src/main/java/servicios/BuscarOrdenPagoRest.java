/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;
import com.google.gson.JsonObject;
import datos.ServicioResponse;
import javax.ejb.Stateless;
import javax.inject.Inject;
import datos.AppCodigo;
import datos.FactCabResponse;
import datos.OrdenPagoCabResponse;
import datos.OrdenPagoDetalleResponse;
import datos.OrdenPagoFormaPagoResponse;
import datos.OrdenPagoPieResponse;
import datos.OrdenesPagoResponse;
import datos.Payload;
import entidades.Acceso;
import entidades.FormaPago;
import entidades.FactCab;
import entidades.OrdenesPagosDetalle;
import entidades.OrdenesPagosFormaPago;
import entidades.OrdenesPagosPCab;
import entidades.OrdenesPagosPie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import persistencia.AccesoFacade;
import utils.Utils;
import entidades.Usuario;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.ws.rs.POST;
import persistencia.FactCabFacade;
import persistencia.OrdenesPagosDetalleFacade;
import persistencia.OrdenesPagosFormaPagoFacade;
import persistencia.OrdenesPagosPCabFacade;
import persistencia.OrdenesPagosPieFacade;
import persistencia.UsuarioFacade;
/**
 *
 * @author mertw
 */
@Stateless
@Path("buscarOrdenPago")
public class BuscarOrdenPagoRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject FactCabFacade factCabFacade;
    @Inject OrdenesPagosPCabFacade ordenesPagosPCabFacade;
    @Inject OrdenesPagosDetalleFacade ordenesPagosDetalleFacade;
    @Inject OrdenesPagosFormaPagoFacade ordenesPagosFormaPagoFacade;
    @Inject OrdenesPagosPieFacade ordenesPagosPieFacade;
    @Inject Utils utils;   
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
     public Response getOrdenPagos(  
        @HeaderParam ("token") String token,
        
        // Si idFactCab viene con valor tgraigo las orgenes de pago relacionadas a un comprobante de compra
        // Si idOpCab viene con valor traigo una orden de pago especifica, independientemente que idFacCab traiga valor
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        ServicioResponse respuesta = new ServicioResponse();
     try {  
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            // Obtengo los atributos del body
            Integer idEmpresa = (Integer) Utils.getKeyFromJsonObject("idEmpresa", jsonBody, "Integer");
            Integer numeroComprobante = (Integer) Utils.getKeyFromJsonObject("nroComprobante", jsonBody, "Integer");
           
            
     
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
            
            //Valido datos busqueda
            if (numeroComprobante == null || numeroComprobante == 0){
                respuesta.setControl(AppCodigo.ERROR, "Error, debe indicar una comprobante válido");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            
            OrdenesPagosPCab ordenesPagos = ordenesPagosPCabFacade.getByNumero(numeroComprobante.longValue());
            
             
             

            //respuesta.setArraydatos(_orden);
            respuesta.setControl(AppCodigo.OK, "Comprobantes");
            return Response.status(Response.Status.OK).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
     }

}
