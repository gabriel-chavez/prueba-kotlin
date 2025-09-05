package com.emizor.univida.modelo.dominio.univida.turnos;

public class Punto {
    private int id;
    private String nombre;

    public Punto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
