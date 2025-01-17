package com.emizor.univida.modelo.dominio.univida.ventas;

import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPago;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EfectivizarAdicionalUnivida {

    @SerializedName("Imei")
    @Expose
    private String imei;
    @SerializedName("Longitud")
    @Expose
    private Double longitud;
    @SerializedName("Latitud")
    @Expose
    private Double latitud;
    @SerializedName("Ip")
    @Expose
    private String ip;
    @SerializedName("VersionAppp")
    @Expose
    private String versionApp;

    @SerializedName("MedioPago")
    @Expose
    private MedioPago medioPago;
    @SerializedName("SerialNumberPax")
    @Expose
    private String serialNumberPax;
    @SerializedName("ModelPax")
    @Expose
    private String modelPax;
    @SerializedName("CodigoSistema")
    @Expose
    private String codigoSistema = "022-000-000-003";

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public String getSerialNumberPax() {
        return serialNumberPax;
    }

    public void setSerialNumberPax(String serialNumberPax) {
        this.serialNumberPax = serialNumberPax;
    }

    public String getModelPax() {
        return modelPax;
    }

    public void setModelPax(String modelPax) {
        this.modelPax = modelPax;
    }

    public String getCodigoSistema() {
        return codigoSistema;
    }

    public void setCodigoSistema(String codigoSistema) {
        this.codigoSistema = codigoSistema;
    }

}
