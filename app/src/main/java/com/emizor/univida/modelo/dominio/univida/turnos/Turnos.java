package com.emizor.univida.modelo.dominio.univida.turnos;

public class Turnos {
    private int secuencial;
    private String nombre;
    private String cargo;
    private String fechaRegistro;
    private String tipoEvento;
    private String puntoRegistro;
    private String imagen;
    private String latitud;
    private String longitud;

    public Turnos() {
        // Constructor vacío requerido para serialización/deserialización
    }

    public Turnos(int secuencial, String nombre, String cargo, String fechaRegistro,
                  String tipoEvento, String puntoRegistro, String imagen,
                  String latitud, String longitud) {
        this.secuencial = secuencial;
        this.nombre = nombre;
        this.cargo = cargo;
        this.fechaRegistro = fechaRegistro;
        this.tipoEvento = tipoEvento;
        this.puntoRegistro = puntoRegistro;
        this.imagen = imagen;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getPuntoRegistro() {
        return puntoRegistro;
    }

    public void setPuntoRegistro(String puntoRegistro) {
        this.puntoRegistro = puntoRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Turnos{" +
                "secuencial=" + secuencial +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", puntoRegistro='" + puntoRegistro + '\'' +
                ", imagen='" + imagen + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                '}';
    }
}
