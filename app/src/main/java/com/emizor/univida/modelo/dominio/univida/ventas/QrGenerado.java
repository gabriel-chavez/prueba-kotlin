package com.emizor.univida.modelo.dominio.univida.ventas;

public class QrGenerado {
    private String secuencial;
    private String identificadorVehiculo;
    private String gestion;
    private String fechaHoraSolicitud;
    private String estadoSolicitud;
    private String fechaHoraEstado;
    private String efectivizado;
    private String mensajeEfectivizacion;
    private int tVehiSoatPropFk;

    public QrGenerado(String secuencial, String identificadorVehiculo, String gestion,
                      String fechaHoraSolicitud, String estadoSolicitud,
                      String fechaHoraEstado, String efectivizado, String mensajeEfectivizacion, int tVehiSoatPropFk) {
        this.secuencial = secuencial;
        this.identificadorVehiculo = identificadorVehiculo;
        this.gestion = gestion;
        this.fechaHoraSolicitud = fechaHoraSolicitud;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaHoraEstado = fechaHoraEstado;
        this.efectivizado = efectivizado;
        this.mensajeEfectivizacion = mensajeEfectivizacion;
        this.tVehiSoatPropFk=tVehiSoatPropFk;
    }

    public String getSecuencial() { return secuencial; }
    public String getIdentificadorVehiculo() { return identificadorVehiculo; }
    public String getGestion() { return gestion; }
    public String getFechaHoraSolicitud() { return fechaHoraSolicitud; }
    public String getEstadoSolicitud() { return estadoSolicitud; }
    public String getFechaHoraEstado() { return fechaHoraEstado; }
    public String getEfectivizado() { return efectivizado; }
    public String getMensajeEfectivizacion() { return mensajeEfectivizacion; }
    public int getTVehiSoatPropFk() { return tVehiSoatPropFk; }
}

