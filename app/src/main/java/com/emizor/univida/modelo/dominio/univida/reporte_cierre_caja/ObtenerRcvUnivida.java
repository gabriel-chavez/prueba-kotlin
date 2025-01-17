package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class ObtenerRcvUnivida implements Serializable, Parcelable {

    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("rcv_secuencial")
    @Expose
    private Integer rcvSecuencial;
    @SerializedName("venta_fecha")
    @Expose
    private String ventaFecha;

    public final static Parcelable.Creator<ObtenerRcvUnivida> CREATOR = new Parcelable.Creator<ObtenerRcvUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ObtenerRcvUnivida createFromParcel(Parcel in) {
            return new ObtenerRcvUnivida(in);
        }

        public ObtenerRcvUnivida[] newArray(int size) {
            return (new ObtenerRcvUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8439119647303662762L;

    protected ObtenerRcvUnivida(Parcel in) {
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaFecha = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ObtenerRcvUnivida(){

    }

    public String getVentaVendedor() {
        return ventaVendedor;
    }

    public void setVentaVendedor(String ventaVendedor) {
        this.ventaVendedor = ventaVendedor;
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public Integer getRcvSecuencial() {
        return rcvSecuencial;
    }

    public void setRcvSecuencial(Integer rcvSecuencial) {
        this.rcvSecuencial = rcvSecuencial;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ventaVendedor).append(gestionFk).append(rcvSecuencial).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ObtenerRcvUnivida) == false) {
            return false;
        }
        ObtenerRcvUnivida rhs = ((ObtenerRcvUnivida) other);
        return new EqualsBuilder().append(ventaVendedor, rhs.ventaVendedor).append(gestionFk, rhs.gestionFk).append(rcvSecuencial, rhs.rcvSecuencial).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ventaVendedor);
        dest.writeValue(gestionFk);
        dest.writeValue(rcvSecuencial);
    }

    public int describeContents() {
        return 0;
    }
}
