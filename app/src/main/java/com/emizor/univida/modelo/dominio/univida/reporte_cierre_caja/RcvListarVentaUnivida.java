package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RcvListarVentaUnivida implements Serializable, Parcelable
{

    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("venta_fecha")
    @Expose
    private String ventaFecha;
    public final static Parcelable.Creator<RcvListarVentaUnivida> CREATOR = new Creator<RcvListarVentaUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvListarVentaUnivida createFromParcel(Parcel in) {
            return new RcvListarVentaUnivida(in);
        }

        public RcvListarVentaUnivida[] newArray(int size) {
            return (new RcvListarVentaUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3232415314763188806L;

    protected RcvListarVentaUnivida(Parcel in) {
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaFecha = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RcvListarVentaUnivida() {
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

    public String getVentaFecha() {
        return ventaFecha;
    }

    public void setVentaFecha(String ventaFecha) {
        this.ventaFecha = ventaFecha;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ventaVendedor).append(gestionFk).append(ventaFecha).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvListarVentaUnivida) == false) {
            return false;
        }
        RcvListarVentaUnivida rhs = ((RcvListarVentaUnivida) other);
        return new EqualsBuilder().append(ventaVendedor, rhs.ventaVendedor).append(gestionFk, rhs.gestionFk).append(ventaFecha, rhs.ventaFecha).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ventaVendedor);
        dest.writeValue(gestionFk);
        dest.writeValue(ventaFecha);
    }

    public int describeContents() {
        return 0;
    }
}
