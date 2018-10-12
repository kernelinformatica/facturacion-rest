package servicios;

import datos.AppCodigo;
import datos.PadronResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.Padron;
import entidades.Parametro;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import persistencia.AccesoFacade;
import persistencia.PadronFacade;
import persistencia.ParametroFacade;
import persistencia.UsuarioFacade;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("padron")
public class PadronRest {

    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject PadronFacade padronFacade;
    @Inject ParametroFacade parametroFacade;
 
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadron(  
        @HeaderParam ("token") String token,
        @QueryParam ("grupo") Integer grupo,    
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
            List<Payload> clientes = new ArrayList<>();
            
            if(grupo == null) {
                List<Padron> listaPadron = padronFacade.findAll();

                //valido que tenga campos disponibles
                if(listaPadron.isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "No hay registros en el Padron");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }

                for(Padron p : listaPadron){
                    PadronResponse fp = new PadronResponse(p);
                    clientes.add(fp);
                }
            } else {
                List<Parametro> listaParametros = parametroFacade.getByGrupoEmpresa(grupo, user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
                
                if(listaParametros.isEmpty() || listaParametros == null) {
                    respuesta.setControl(AppCodigo.ERROR, "No existe el grupo");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                List<Padron> listaPadron = new ArrayList();
                
                for(Parametro p : listaParametros) {
                    listaPadron.addAll(padronFacade.getPadronByCategoria(Integer.parseInt(p.getValor())));
                }
                
                if(listaPadron.isEmpty() ||listaPadron == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Padron no disponible");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                for(Padron p : listaPadron) {
                    PadronResponse fp = new PadronResponse(p);
                    clientes.add(fp);
                }

            }
            respuesta.setArraydatos(clientes);
            respuesta.setControl(AppCodigo.OK, "Padron");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
