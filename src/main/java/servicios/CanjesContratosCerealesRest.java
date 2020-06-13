/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import datos.AppCodigo;
import datos.CanjesContratosCerealesResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CanjesContratosCereales;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.CanjesContratosCerealesFacade;
import persistencia.UsuarioFacade;

/**
 *
 * @author administrador
 */
@Stateless
@Path("contratos-cereales")
public class CanjesContratosCerealesRest {
    
    @Inject AccesoFacade accesoFacade;
    @Inject UsuarioFacade usuarioFacade;
    @Inject CanjesContratosCerealesFacade canjesContratosCerealesFacade;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorias(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) {
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
            
            List<Payload> cereales = new ArrayList<>();            
            Collection<CanjesContratosCereales> ccc = canjesContratosCerealesFacade.getAllCanjesContratosCereales();
            for(CanjesContratosCereales canjesContratosCereales : ccc) {
                CanjesContratosCerealesResponse cccResponse = new CanjesContratosCerealesResponse(canjesContratosCereales);
                cereales.add(cccResponse);
            }
            respuesta.setArraydatos(cereales);
            respuesta.setControl(AppCodigo.OK, "Lista de Categorias");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
