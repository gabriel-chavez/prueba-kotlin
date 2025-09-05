package com.emizor.univida.modelo.dominio.univida.consultas;

import com.emizor.univida.modelo.dominio.univida.turnos.Turnos;

import java.util.List;

public class RespuestaTurnos {
    public boolean exito;
    private int codigoRetorno;
    public String mensaje;
    private List<Turnos> datos;

    public List<Turnos> getDatos() {
        return datos;
    }

    public void setDatos(List<Turnos> datos) {
        this.datos = datos;
    }


}