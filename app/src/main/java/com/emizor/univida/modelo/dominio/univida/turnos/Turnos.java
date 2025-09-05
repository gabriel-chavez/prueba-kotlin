package com.emizor.univida.modelo.dominio.univida.turnos;

import com.google.gson.annotations.SerializedName;

public class Turnos {

    @SerializedName("Secuencial")
    private int secuencial;

    @SerializedName("EmpleadoNombreCompleto")
    private String nombre;

    @SerializedName("EmpleadoCargo")
    private String empleadoCargo;

    @SerializedName("FechaHora")
    private String fechaRegistro;

    @SerializedName("TParControlTurnosTipoEventoDescripcion")
    private String tipoEvento;

    @SerializedName("TParControlTurnosTipoPuntoDescripcion")
    private String puntoRegistro;

    @SerializedName("ImageBase64")
    private String imagen;

    @SerializedName("Latitud")
    private double latitud;

    @SerializedName("Longitud")
    private double longitud;

    @SerializedName("DispositivoId")
    private String dispositivoId;

    @SerializedName("Usuario")
    private String usuario;

    // Constructor vacío requerido para la serialización/deserialización
    public Turnos() {
        // Constructor vacío
    }

    public Turnos(int secuencial, String nombre, String fechaRegistro, String tipoEvento,
                  String puntoRegistro, String imagen, double latitud, double longitud,
                  String dispositivoId, String usuario,String empleadoCargo) {
        this.secuencial = secuencial;
        this.nombre = nombre;
        this.fechaRegistro = fechaRegistro;
        this.tipoEvento = tipoEvento;
        this.puntoRegistro = puntoRegistro;
        this.imagen = imagen;
        this.latitud = latitud;
        this.longitud = longitud;
        this.dispositivoId = dispositivoId;
        this.usuario = usuario;
        this.empleadoCargo = empleadoCargo;


    }

    // Getters y setters
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
    public String getEmpleadoCargo() {
        return empleadoCargo;
    }

    public void setEmpleadoCargo(String empleadoCargo) {
        this.empleadoCargo = empleadoCargo;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDispositivoId() {
        return dispositivoId;
    }

    public void setDispositivoId(String dispositivoId) {
        this.dispositivoId = dispositivoId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Turnos{" +
                "secuencial=" + secuencial +
                ", nombre='" + nombre + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", puntoRegistro='" + puntoRegistro + '\'' +
                ", imagen='" + imagen + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", dispositivoId='" + dispositivoId + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
