/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entidades.SisTipoModelo;

/**
 *
 * @author Usuario
 */
public class SisTipoModeloResponse {
    private int idTipoModelo;
    private String descripcion;

    public SisTipoModeloResponse(SisTipoModelo s) {
        this.idTipoModelo = s.getIdSisTipoModelo();
        this.descripcion = s.getTipo();
    }
    
    public int getIdTipoModelo() {
        return idTipoModelo;
    }

    public void setIdTipoModelo(int idTipoModelo) {
        this.idTipoModelo = idTipoModelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
