package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ListarVentaUnivida implements Serializable, Parcelable
{

    @SerializedName("venta_fecha")
    @Expose
    private String ventaFecha;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    public final static Parcelable.Creator<ListarVentaUnivida> CREATOR = new Creator<ListarVentaUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ListarVentaUnivida createFromParcel(Parcel in) {
            return new ListarVentaUnivida(in);
        }

        public ListarVentaUnivida[] newArray(int size) {
            return (new ListarVentaUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8686466988892734798L;

    protected ListarVentaUnivida(Parcel in) {
        this.ventaFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ListarVentaUnivida() {
    }

    public String getVentaFecha() {
        return ventaFecha;
    }

    public void setVentaFecha(String ventaFecha) {
        this.ventaFecha = ventaFecha;
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
        return new HashCodeBuilder().append(vehiculoPlaca).append(ventaVendedor).append(gestionFk).append(ventaFecha).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ListarVentaUnivida) == false) {
            return false;
        }
        ListarVentaUnivida rhs = ((ListarVentaUnivida) other);
        return new EqualsBuilder().append(vehiculoPlaca, rhs.vehiculoPlaca).append(ventaVendedor, rhs.ventaVendedor).append(gestionFk, rhs.gestionFk).append(ventaFecha, rhs.ventaFecha).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ventaFecha);
        dest.writeValue(gestionFk);
        dest.writeValue(ventaVendedor);
        dest.writeValue(vehiculoPlaca);
    }

    public int describeContents() {
        return 0;
    }

}
