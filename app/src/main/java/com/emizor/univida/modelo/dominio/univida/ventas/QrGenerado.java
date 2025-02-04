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
    private int tParSimpleEstadoSolicitudFk;
    double importe;
    private int tramiteSecuencial;
    public QrGenerado(String secuencial, String identificadorVehiculo, String gestion,
                      String fechaHoraSolicitud, String estadoSolicitud,
                      String fechaHoraEstado, String efectivizado, String mensajeEfectivizacion, int tVehiSoatPropFk, int tParSimpleEstadoSolicitudFk, int tramiteSecuencial, double importe) {
        this.secuencial = secuencial;
        this.identificadorVehiculo = identificadorVehiculo;
        this.gestion = gestion;
        this.fechaHoraSolicitud = fechaHoraSolicitud;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaHoraEstado = fechaHoraEstado;
        this.efectivizado = efectivizado;
        this.mensajeEfectivizacion = mensajeEfectivizacion;
        this.tVehiSoatPropFk=tVehiSoatPropFk;
        this.tParSimpleEstadoSolicitudFk=tParSimpleEstadoSolicitudFk;
        this.tramiteSecuencial=tramiteSecuencial;
        this.importe=importe;
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
    public int getTParSimpleEstadoSolicitudFk() { return tParSimpleEstadoSolicitudFk; }
    public int getTramiteSecuencial() { return tramiteSecuencial; }
    public double getImporte() { return importe; }
}

