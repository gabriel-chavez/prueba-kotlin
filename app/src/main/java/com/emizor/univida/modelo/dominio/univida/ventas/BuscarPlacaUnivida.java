package com.emizor.univida.modelo.dominio.univida.ventas;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuscarPlacaUnivida {

    @SerializedName("venta_cajero")
    @Expose
    private String ventaCajero;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("venta_canal_fk")
    @Expose
    private Integer ventaCanalFk;
    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("vehiculo_placa_tipo")
    @Expose
    private Integer vehiculoPlacaTipo;

    public String getVentaCajero() {
        return ventaCajero;
    }

    public void setVentaCajero(String ventaCajero) {
        this.ventaCajero = ventaCajero;
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public Integer getVentaCanalFk() {
        return ventaCanalFk;
    }

    public void setVentaCanalFk(Integer ventaCanalFk) {
        this.ventaCanalFk = ventaCanalFk;
    }

    public String getVentaVendedor() {
        return ventaVendedor;
    }

    public void setVentaVendedor(String ventaVendedor) {
        this.ventaVendedor = ventaVendedor;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public Integer getVehiculoPlacaTipo() {
        return vehiculoPlacaTipo;
    }

    public void setVehiculoPlacaTipo(Integer vehiculoPlacaTipo) {
        this.vehiculoPlacaTipo = vehiculoPlacaTipo;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
