package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Gestion implements Serializable, Parcelable
{

    @SerializedName("prioridad_venta")
    @Expose
    private Integer prioridadVenta = 0;
    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("venta_desde")
    @Expose
    private String ventaDesde;
    @SerializedName("venta_hasta")
    @Expose
    private String ventaHasta;
    public final static Parcelable.Creator<Gestion> CREATOR = new Creator<Gestion>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Gestion createFromParcel(Parcel in) {
            return new Gestion(in);
        }

        public Gestion[] newArray(int size) {
            return (new Gestion[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5935993128167345500L;

    protected Gestion(Parcel in) {
        this.prioridadVenta = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaDesde = ((String) in.readValue((String.class.getClassLoader())));
        this.ventaHasta = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Gestion() {
    }

    public Integer getPrioridadVenta() {
        return prioridadVenta;
    }

    public void setPrioridadVenta(Integer prioridadVenta) {
        this.prioridadVenta = prioridadVenta;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public String getVentaDesde() {
        return ventaDesde;
    }

    public void setVentaDesde(String ventaDesde) {
        this.ventaDesde = ventaDesde;
    }

    public String getVentaHasta() {
        return ventaHasta;
    }

    public void setVentaHasta(String ventaHasta) {
        this.ventaHasta = ventaHasta;
    }

    @Override
    public String toString() {
        if (getSecuencial() > 0) {

            return getSecuencial().toString();

        }else{
            return "-- Seleccione una gestión --";
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ventaHasta).append(secuencial).append(prioridadVenta).append(ventaDesde).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Gestion) == false) {
            return false;
        }
        Gestion rhs = ((Gestion) other);
        return new EqualsBuilder().append(ventaHasta, rhs.ventaHasta).append(secuencial, rhs.secuencial).append(prioridadVenta, rhs.prioridadVenta).append(ventaDesde, rhs.ventaDesde).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(prioridadVenta);
        dest.writeValue(secuencial);
        dest.writeValue(ventaDesde);
        dest.writeValue(ventaHasta);
    }

    public int describeContents() {
        return 0;
    }
}
