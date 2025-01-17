package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RcvListarUnivida implements Serializable, Parcelable
{

    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    public final static Parcelable.Creator<RcvListarUnivida> CREATOR = new Creator<RcvListarUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvListarUnivida createFromParcel(Parcel in) {
            return new RcvListarUnivida(in);
        }

        public RcvListarUnivida[] newArray(int size) {
            return (new RcvListarUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7492702798128247784L;

    protected RcvListarUnivida(Parcel in) {
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RcvListarUnivida() {
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public String getVentaVendedor() {
        return ventaVendedor;
    }

    public void setVentaVendedor(String ventaVendedor) {
        this.ventaVendedor = ventaVendedor;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ventaVendedor).append(gestionFk).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvListarUnivida) == false) {
            return false;
        }
        RcvListarUnivida rhs = ((RcvListarUnivida) other);
        return new EqualsBuilder().append(ventaVendedor, rhs.ventaVendedor).append(gestionFk, rhs.gestionFk).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(gestionFk);
        dest.writeValue(ventaVendedor);
    }

    public int describeContents() {
        return 0;
    }
}
