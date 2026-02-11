package com.emizor.univida.modelo.dominio.univida.turnos;

public class Evento {
    private int secuencial;
    private String descripcion;

    public Evento() {
    }

    @Override
    public String toString() {
        return descripcion;
    }
    public int getId() {
        return secuencial;
    }
}
