package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ObtenerVentaUnivida implements Serializable, Parcelable
{

    @SerializedName("autorizacion_numero")
    @Expose
    private String autorizacionNumero;
    @SerializedName("numero")
    @Expose
    private Integer numero;
    @SerializedName("numero_comprobante")
    @Expose
    private Long numeroComprobante;
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
    public final static Parcelable.Creator<ObtenerVentaUnivida> CREATOR = new Creator<ObtenerVentaUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ObtenerVentaUnivida createFromParcel(Parcel in) {
            return new ObtenerVentaUnivida(in);
        }

        public ObtenerVentaUnivida[] newArray(int size) {
            return (new ObtenerVentaUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5079457230006232659L;

    protected ObtenerVentaUnivida(Parcel in) {
        this.autorizacionNumero = ((String) in.readValue((String.class.getClassLoader())));
        this.numero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.numeroComprobante = ((Long) in.readValue((Long.class.getClassLoader())));
        this.ventaCajero = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaCanalFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ObtenerVentaUnivida() {
    }

    public String getAutorizacionNumero() {
        return autorizacionNumero;
    }

    public void setAutorizacionNumero(String autorizacionNumero) {
        this.autorizacionNumero = autorizacionNumero;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Long getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(Long numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(numeroComprobante).append(ventaCanalFk).append(autorizacionNumero).append(vehiculoPlaca).append(ventaVendedor).append(ventaCajero).append(gestionFk).append(numero).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ObtenerVentaUnivida) == false) {
            return false;
        }
        ObtenerVentaUnivida rhs = ((ObtenerVentaUnivida) other);
        return new EqualsBuilder().append(numeroComprobante, rhs.numeroComprobante).append(ventaCanalFk, rhs.ventaCanalFk).append(autorizacionNumero, rhs.autorizacionNumero).append(vehiculoPlaca, rhs.vehiculoPlaca).append(ventaVendedor, rhs.ventaVendedor).append(ventaCajero, rhs.ventaCajero).append(gestionFk, rhs.gestionFk).append(numero, rhs.numero).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(autorizacionNumero);
        dest.writeValue(numero);
        dest.writeValue(numeroComprobante);
        dest.writeValue(ventaCajero);
        dest.writeValue(gestionFk);
        dest.writeValue(ventaCanalFk);
        dest.writeValue(ventaVendedor);
        dest.writeValue(vehiculoPlaca);
    }

    public int describeContents() {
        return 0;
    }
}
