package com.emizor.univida.modelo.dominio.univida.turnos;

public class Punto {
    private int secuencial;
    private String descripcion;

    public Punto(){}


    public int getId() { return secuencial; }
    public String getNombre() { return descripcion; }

    @Override
    public String toString() {
        return descripcion;
    }
}
