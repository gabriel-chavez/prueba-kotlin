package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RevertirRcvUnivida implements Serializable, Parcelable
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
    public final static Parcelable.Creator<RevertirRcvUnivida> CREATOR = new Creator<RevertirRcvUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RevertirRcvUnivida createFromParcel(Parcel in) {
            return new RevertirRcvUnivida(in);
        }

        public RevertirRcvUnivida[] newArray(int size) {
            return (new RevertirRcvUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -1679088783680670781L;

    protected RevertirRcvUnivida(Parcel in) {
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public RevertirRcvUnivida() {
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
        if ((other instanceof RevertirRcvUnivida) == false) {
            return false;
        }
        RevertirRcvUnivida rhs = ((RevertirRcvUnivida) other);
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
