package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class EfectivizarRcvUnivida implements Serializable, Parcelable
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
    @SerializedName("rcv_importe")
    @Expose
    private Double rcvImporte;
    @SerializedName("rcv_cantidad")
    @Expose
    private Integer rcvCantidad;
    @SerializedName("rcv_json_medio_pago")
    @Expose
    private String rcvJsonMedioPago;
    @SerializedName("sucursal_fk")
    @Expose
    private Integer sucursalFk;
    public final static Parcelable.Creator<EfectivizarRcvUnivida> CREATOR = new Creator<EfectivizarRcvUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EfectivizarRcvUnivida createFromParcel(Parcel in) {
            return new EfectivizarRcvUnivida(in);
        }

        public EfectivizarRcvUnivida[] newArray(int size) {
            return (new EfectivizarRcvUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8439119647303662762L;

    protected EfectivizarRcvUnivida(Parcel in) {
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.rcvImporte = ((Double) in.readValue((Double.class.getClassLoader())));
        this.rcvCantidad = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvJsonMedioPago = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public EfectivizarRcvUnivida() {
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

    public Double getRcvImporte() {
        return rcvImporte;
    }

    public void setRcvImporte(Double rcvImporte) {
        this.rcvImporte = rcvImporte;
    }

    public Integer getRcvCantidad() {
        return rcvCantidad;
    }

    public void setRcvCantidad(Integer rcvCantidad) {
        this.rcvCantidad = rcvCantidad;
    }

    public String getRcvJsonMedioPago() {
        return rcvJsonMedioPago;
    }

    public void setRcvJsonMedioPago(String rcvJsonMedioPago) {
        this.rcvJsonMedioPago = rcvJsonMedioPago;
    }

    public Integer getSucursalFk() {
        return sucursalFk;
    }

    public void setSucursalFk(Integer sucursalFk) {
        this.sucursalFk = sucursalFk;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rcvJsonMedioPago).append(sucursalFk).append(ventaVendedor).append(rcvCantidad).append(rcvImporte).append(gestionFk).append(ventaFecha).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EfectivizarRcvUnivida) == false) {
            return false;
        }
        EfectivizarRcvUnivida rhs = ((EfectivizarRcvUnivida) other);
        return new EqualsBuilder().append(rcvJsonMedioPago, rhs.rcvJsonMedioPago).append(sucursalFk, rhs.sucursalFk).append(ventaVendedor, rhs.ventaVendedor).append(rcvCantidad, rhs.rcvCantidad).append(rcvImporte, rhs.rcvImporte).append(gestionFk, rhs.gestionFk).append(ventaFecha, rhs.ventaFecha).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ventaVendedor);
        dest.writeValue(gestionFk);
        dest.writeValue(ventaFecha);
        dest.writeValue(rcvImporte);
        dest.writeValue(rcvCantidad);
        dest.writeValue(rcvJsonMedioPago);
        dest.writeValue(sucursalFk);
    }

    public int describeContents() {
        return 0;
    }
}
