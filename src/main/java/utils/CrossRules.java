package utils;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author German
 */
@Provider
public class CrossRules implements ContainerResponseFilter {

//    @Override
//    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
//        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
//        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
//        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type");
//    }
    
    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext cres) throws IOException {
       cres.getHeaders().add("Access-Control-Allow-Origin", "*");
       cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, clave, token, idUsuario");
       cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
       cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
       cres.getHeaders().add("Access-Control-Max-Age", "1209600");
       
    }
    
}
