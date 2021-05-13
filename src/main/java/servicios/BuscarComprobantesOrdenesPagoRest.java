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
import datos.FactDetalleResponse;
import datos.FactFormaPagoResponse;
import datos.FactPieResponse;
import datos.OrdenPagoCabResponse;
import datos.OrdenPagoDetalleResponse;
import datos.OrdenPagoFormaPagoResponse;
import datos.OrdenPagoPieResponse;
import datos.OrdenesPagoResponse;
import datos.Payload;
import entidades.Acceso;
import entidades.FormaPago;
import entidades.FactCab;
import entidades.FactFormaPago;
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
import persistencia.FactDetalleFacade;
import persistencia.MasterFacade;
import persistencia.OrdenesPagosDetalleFacade;
import persistencia.OrdenesPagosFormaPagoFacade;
import persistencia.OrdenesPagosPCabFacade;
import persistencia.OrdenesPagosPieFacade;
import persistencia.FactFormaPagoFacade;
import persistencia.FactImputaFacade;
import persistencia.FactPieFacade;
import persistencia.UsuarioFacade;

/**
 *
 * @author DARIO
 */
@Stateless
@Path("buscarComprobantesOrdenesPago")
public class BuscarComprobantesOrdenesPagoRest {

    @Inject
    UsuarioFacade usuarioFacade;
    @Inject
    AccesoFacade accesoFacade;
    @Inject
    Utils utils;
    @Inject
    FactCabFacade factCabFacade;
    @Inject
    FactDetalleFacade factDetalleFacade;
    @Inject
    MasterFacade masterFacade;
    @Inject
    FactFormaPagoFacade factFormaPagoFacade;
    @Inject
    FactImputaFacade factImputaFacade;
    @Inject
    FactPieFacade factPieFacade;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductos(
            @HeaderParam("token") String token,
            @QueryParam("imputados") String imputados,
            @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            // Obtengo los atributos del body
            Integer comprobanteModulo = (Integer) Utils.getKeyFromJsonObject("comprobanteModulo", jsonBody, "Integer");
            Integer comprobanteTipo = (Integer) Utils.getKeyFromJsonObject("comprobanteTipo", jsonBody, "Integer");
            BigDecimal comprobanteNumero = (BigDecimal) Utils.getKeyFromJsonObject("comprobanteNumero", jsonBody, "BigDecimal");
            Date fechaDesde = (Date) Utils.getKeyFromJsonObject("fechaDesde", jsonBody, "Date");
            Date fechaHasta = (Date) Utils.getKeyFromJsonObject("fechaHasta", jsonBody, "Date");
            Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", jsonBody, "Integer");
            Integer padCodigo = (Integer) Utils.getKeyFromJsonObject("padCodigo", jsonBody, "Integer");
            Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", jsonBody, "Integer");
            Integer idEstado = (Integer) Utils.getKeyFromJsonObject("idEstado", jsonBody, "Integer");
            Integer idVendedor = (Integer) Utils.getKeyFromJsonObject("idVendedor", jsonBody, "Integer");
            Integer idSisTipoOperacion = (Integer) Utils.getKeyFromJsonObject("idSisTipoOperacion", jsonBody, "Integer");
            String autorizada = (String) Utils.getKeyFromJsonObject("autorizada", jsonBody, "String");
            Integer contratoRelacionado = (Integer) Utils.getKeyFromJsonObject("contratoRelacionado", jsonBody, "Integer");
            String codProductoDesde = (String) Utils.getKeyFromJsonObject("codProductoDesde", jsonBody, "String");
            String codProductoHasta = (String) Utils.getKeyFromJsonObject("codProductoHasta", jsonBody, "String");

            //valido que token no sea null
            if (token == null || token.trim().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el token
            Acceso userToken = accesoFacade.findByToken(token);

            //valido que Acceso no sea null
            if (userToken == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el usuario
            Usuario user = usuarioFacade.getByToken(userToken);

            //valido que el Usuario no sea null
            if (user == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //valido vencimiento token
            if (!accesoFacade.validarToken(userToken, user)) {
                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
            }

            //Valido fechas
            if (fechaDesde.after(fechaHasta)) {
                respuesta.setControl(AppCodigo.ERROR, "Error, la fecha desde debe ser menor que la fecha hasta");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //seteo el nombre del store cabecera s_buscaComprobantesOrdenesPagoCabecera
            String nombreSP = "call s_buscaComprobantesOrdenPagoCabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //seteo el nombre del store detalle
            String nombreSPDetalle = "call s_buscaComprobantesDetalles(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //seteo el nombre del store detalle
            String nombreSPFormaPago = "call s_buscaComprobantesFormaPago(?)";
            //seteo el nombre del store pie
            String nombreSPPie = "call s_buscaComprobantesPie(?)";
            //seteo el nombre del store imputa
            String nombreSPImputa = "call s_buscaComprobantesImputa(?)";
            // seteo el nombre del store ordenesde pago
            String nombreSPordenesPago = "call s_buscaOrdenesPago(?,?,?,?)";

            //invoco al store
            CallableStatement callableStatement = this.utils.procedimientoAlmacenado(user, nombreSP);
            CallableStatement callableStatementDetalle = this.utils.procedimientoAlmacenado(user, nombreSPDetalle);
            CallableStatement buscaOrdenesPagoRs = this.utils.procedimientoAlmacenado(user, nombreSP);
            CallableStatement callableStatementFormaPago = this.utils.procedimientoAlmacenado(user, nombreSPFormaPago);
            CallableStatement callableStatementPie = this.utils.procedimientoAlmacenado(user, nombreSPPie);
            CallableStatement callableStatementImputa = this.utils.procedimientoAlmacenado(user, nombreSPImputa);
            
            //valido que el Procedimiento Almacenado no sea null
            if (callableStatement == null || callableStatementDetalle == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if (callableStatementFormaPago == null || callableStatementFormaPago == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento s_buscaComprobantesFormaPago");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if (callableStatementPie == null || callableStatementPie == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento s_buscaComprobantesPie");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
             if (callableStatementImputa == null || callableStatementImputa == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento s_buscaComprobantesImputa");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if (contratoRelacionado == null) {
                contratoRelacionado = 0;
            }

            //Seteo los parametros para la cabecera 
            callableStatement.setInt(1, user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
            callableStatement.setInt(2, comprobanteModulo);
            callableStatement.setLong(3, comprobanteNumero.longValue());
            //Parseo las fechas a sql.date
            java.sql.Date sqlFechaDesde = new java.sql.Date(fechaDesde.getTime());
            java.sql.Date sqlFechaHasta = new java.sql.Date(fechaHasta.getTime());

            callableStatement.setDate(4, sqlFechaDesde);
            callableStatement.setDate(5, sqlFechaHasta);
            callableStatement.setInt(6, idProducto);
            callableStatement.setInt(7, padCodigo);
            callableStatement.setInt(8, idDeposito);
            callableStatement.setInt(9, idEstado);
            callableStatement.setInt(10, idVendedor);
            callableStatement.setInt(11, idSisTipoOperacion);
            callableStatement.setString(12, autorizada);
            callableStatement.setInt(13, contratoRelacionado);
            callableStatement.setString(14, codProductoDesde);
            callableStatement.setString(15, codProductoHasta);

            //Seteo los parametros para los detalle
            callableStatementDetalle.setInt(1, user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
            callableStatementDetalle.setInt(2, comprobanteModulo);
            callableStatementDetalle.setInt(3, comprobanteTipo);
            callableStatementDetalle.setLong(4, comprobanteNumero.longValue());

            //Parseo las fechas a sql.date
            java.sql.Date sqlFechaDesdeDetalle = new java.sql.Date(fechaDesde.getTime());
            java.sql.Date sqlFechaHastaDetalle = new java.sql.Date(fechaHasta.getTime());

            callableStatementDetalle.setDate(5, sqlFechaDesdeDetalle);
            callableStatementDetalle.setDate(6, sqlFechaHastaDetalle);
            callableStatementDetalle.setInt(7, idProducto);
            callableStatementDetalle.setInt(8, padCodigo);
            callableStatementDetalle.setInt(9, idDeposito);
            callableStatementDetalle.setInt(10, idEstado);
            callableStatementDetalle.setInt(11, idVendedor);
            callableStatementDetalle.setInt(12, idSisTipoOperacion);
            callableStatementDetalle.setString(13, codProductoDesde);
            callableStatementDetalle.setString(14, codProductoHasta);

            // forma pago params
            //Reccorro los resultados para la cabecera
            ResultSet rs = callableStatement.executeQuery();
            List<FactCabResponse> factCabResponses = new ArrayList<>();
            List<FactDetalleResponse> factDetResponses = new ArrayList<>();
            List<FactFormaPagoResponse> factFormaPagoResponses = new ArrayList<>();
            List<FactPieResponse> factPResponse = new ArrayList<>();
            while (rs.next()) {
                FactCabResponse factCab = new FactCabResponse(
                        rs.getInt("idFactCab"),
                        rs.getString("comprobante"),
                        rs.getLong("numero"),
                        rs.getDate("fechaEmision"),
                        rs.getInt("codigoPad"),
                        rs.getString("nombre"),
                        rs.getString("cuit"),
                        rs.getBigDecimal("dolar"),
                        rs.getString("moneda"),
                        rs.getString("imputada"),
                        rs.getString("modulo"),
                        rs.getString("vendedor"),
                        rs.getInt("idCteTipo"),
                        rs.getBigDecimal("importeNeto"),
                        rs.getBigDecimal("importeTotal"),
                        rs.getString("tipoOperacion"),
                        rs.getString("autorizada"),
                        rs.getString("permiteBorrado"),
                        rs.getInt("kilosCanje"),
                        rs.getBoolean("pesificado"),
                        rs.getBoolean("dolarizadoAlVto"),
                        rs.getBigDecimal("interesMensualCompra"),
                        rs.getBoolean("canjeInsumos"),
                        rs.getString("tipoCambio"));
                factCabResponses.add(factCab);
            }

            //Recorro los resultados para los detalles
            ResultSet rsd = callableStatementDetalle.executeQuery();
            while (rsd.next()) {
                FactDetalleResponse factDet = new FactDetalleResponse(
                        rsd.getString("comprobante"),
                        rsd.getLong("numero"),
                        rsd.getDate("fechaEmision"),
                        rsd.getString("codProducto"),
                        rsd.getString("articulo"),
                        rsd.getBigDecimal("original"),
                        rsd.getBigDecimal("pendiente"),
                        rsd.getBigDecimal("precio"),
                        rsd.getBigDecimal("dolar"),
                        rsd.getString("moneda"),
                        rsd.getBigDecimal("porCalc"),
                        rsd.getBigDecimal("ivaPorc"),
                        rsd.getInt("deposito"),
                        rsd.getBigDecimal("importe"),
                        rsd.getInt("idFactCab"),
                        rsd.getString("vendedor"),
                        rsd.getString("descuento"),
                        rsd.getBigDecimal("precioDesc"),
                        rsd.getString("unidadDescuento"));
                factDetResponses.add(factDet);
            }
            List<Payload> comprobantes = new ArrayList<>();
            for (FactCabResponse c : factCabResponses) {
                for (FactDetalleResponse d : factDetResponses) {
                    if (c.getIdFactCab() == d.getFactCab()) {
                        c.getDetalle().add(d);

                    }
                }
                comprobantes.add(c);
            }
            // armo la forma de pago
            callableStatementFormaPago.setInt(1, 0);
            ResultSet rsfp = callableStatementFormaPago.executeQuery();
            while (rsfp.next()) {
                FactFormaPagoResponse factFPagoResponses = new FactFormaPagoResponse(
                        rsfp.getInt("idFactFormaPago"),
                        rsfp.getString("detalle"),
                        rsfp.getBigDecimal("importe"),
                        rsfp.getBigDecimal("porcentaje"),
                        rsfp.getDate("fechaPago"),
                        rsfp.getInt("diasPago"),
                        rsfp.getInt("idFactCab"),
                        rsfp.getInt("idFormaPago"),
                        rsfp.getString("ctaContable"));
                factFormaPagoResponses.add(factFPagoResponses);
            }
            for (FactCabResponse co : factCabResponses) {
                for (FactFormaPagoResponse f : factFormaPagoResponses) {
                    if (co.getIdFactCab() == f.getIdFactCab()) {
                        co.getFormaPago().add(f);
                    }
                }
                comprobantes.add(co);
            }
            // armo el pie
            
            callableStatementPie.setInt(1, 0);
            ResultSet rsp = callableStatementPie.executeQuery();
            while (rsp.next()) {
                FactPieResponse factPiesResponse = new FactPieResponse(
                        rsp.getInt("idFactPie"),
                        rsp.getString("detalle"),
                        rsp.getBigDecimal("porcentaje"),
                        rsp.getString("ctaContable"),
                        rsp.getInt("idConceptos"),
                        rsp.getInt("idFactCab"),
                        rsp.getInt("idSisTipoModelo"),
                        rsp.getBigDecimal("baseImponible"),
                        rsp.getString("operador"));
                factPResponse.add(factPiesResponse);
            }
            for (FactCabResponse com : factCabResponses) {
                for (FactPieResponse p : factPResponse) {
                    if (com.getIdFactCab() == p.getIdFactCab()) {
                        com.getPie().add(p);
                    }
                }
                comprobantes.add(com);
            }
            // armo el imputa
            

            callableStatement.getConnection().close();
            callableStatementDetalle.getConnection().close();
            callableStatementFormaPago.getConnection().close();
            callableStatementPie.getConnection().close();
            respuesta.setArraydatos(comprobantes);
            respuesta.setControl(AppCodigo.OK, "Comprobantes");
            return Response.status(Response.Status.OK).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }

}
