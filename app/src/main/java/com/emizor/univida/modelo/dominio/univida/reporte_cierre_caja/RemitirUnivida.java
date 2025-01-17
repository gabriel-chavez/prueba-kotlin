package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RemitirUnivida implements Serializable, Parcelable
{

    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("rcv_secuencial")
    @Expose
    private Integer rcvSecuencial;
    @SerializedName("archivo_adjunto")
    @Expose
    private ArchivoAdjunto archivoAdjunto;
    public final static Parcelable.Creator<RemitirUnivida> CREATOR = new Creator<RemitirUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RemitirUnivida createFromParcel(Parcel in) {
            return new RemitirUnivida(in);
        }

        public RemitirUnivida[] newArray(int size) {
            return (new RemitirUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 4254534000831837584L;

    protected RemitirUnivida(Parcel in) {
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.archivoAdjunto = ((ArchivoAdjunto) in.readValue((ArchivoAdjunto.class.getClassLoader())));
    }

    public RemitirUnivida() {
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

    public ArchivoAdjunto getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(archivoAdjunto).append(ventaVendedor).append(gestionFk).append(rcvSecuencial).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RemitirUnivida) == false) {
            return false;
        }
        RemitirUnivida rhs = ((RemitirUnivida) other);
        return new EqualsBuilder().append(archivoAdjunto, rhs.archivoAdjunto).append(ventaVendedor, rhs.ventaVendedor).append(gestionFk, rhs.gestionFk).append(rcvSecuencial, rhs.rcvSecuencial).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ventaVendedor);
        dest.writeValue(gestionFk);
        dest.writeValue(rcvSecuencial);
        dest.writeValue(archivoAdjunto);
    }

    public int describeContents() {
        return 0;
    }
}
