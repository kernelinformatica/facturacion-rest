package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.CteFechaResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteFecha;
import entidades.CteTipo;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
import persistencia.CteTipoFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("buscaCteFecha")
public class BuscaCteFechaRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject CteTipoFacade cteTipoFacade;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCteFecha(  
        @HeaderParam ("token") String token,    
        @Context HttpServletRequest request
    ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            Integer idCteTipo = (Integer) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "Integer");
            Integer puntoVenta = (Integer) Utils.getKeyFromJsonObject("puntoVenta", jsonBody, "Integer");
            
            if(token == null ||token.trim().isEmpty()) {
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
            
            if(idCteTipo == null || puntoVenta == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo es nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el tipo de comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            if(cteTipo.getCteFechaCollection().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no hay fechas para el tipo de comprobante seleccionado");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            List<Payload> cteRespuesta = new ArrayList<>();
            for(CteFecha c : cteTipo.getCteFechaCollection()) {
                if(c.getPuntoVenta().equals(puntoVenta)) {
                    CteFechaResponse cteFechaResponse = new CteFechaResponse(c);
                    cteRespuesta.add(cteFechaResponse);
                }
            }
            
            if(cteRespuesta.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existen fechas con esos parametros");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            respuesta.setArraydatos(cteRespuesta);
            respuesta.setControl(AppCodigo.OK, "Cte Fechas");
            return Response.status(Response.Status.OK).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
