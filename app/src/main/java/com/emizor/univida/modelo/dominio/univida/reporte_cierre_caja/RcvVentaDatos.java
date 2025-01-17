package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RcvVentaDatos implements Serializable, Parcelable
{

    @SerializedName("cantidad")
    @Expose
    private Integer cantidad;
    @SerializedName("cantidad_anulados")
    @Expose
    private Integer cantidadAnulados;
    @SerializedName("cantidad_revertidos")
    @Expose
    private Integer cantidadRevertidos;
    @SerializedName("cantidad_validos")
    @Expose
    private Integer cantidadValidos;
    @SerializedName("formulario_importe")
    @Expose
    private Integer formularioImporte;
    @SerializedName("datos_venta")
    @Expose
    private List<RcvVenta> datosVenta = null;
    public final static Parcelable.Creator<RcvVentaDatos> CREATOR = new Creator<RcvVentaDatos>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvVentaDatos createFromParcel(Parcel in) {
            return new RcvVentaDatos(in);
        }

        public RcvVentaDatos[] newArray(int size) {
            return (new RcvVentaDatos[size]);
        }

    }
            ;
    private final static long serialVersionUID = 797181476747204177L;

    protected RcvVentaDatos(Parcel in) {
        this.cantidad = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cantidadAnulados = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cantidadRevertidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cantidadValidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.formularioImporte = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.datosVenta, (RcvVenta.class.getClassLoader()));
    }

    public RcvVentaDatos() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadAnulados() {
        return cantidadAnulados;
    }

    public void setCantidadAnulados(Integer cantidadAnulados) {
        this.cantidadAnulados = cantidadAnulados;
    }

    public Integer getCantidadRevertidos() {
        return cantidadRevertidos;
    }

    public void setCantidadRevertidos(Integer cantidadRevertidos) {
        this.cantidadRevertidos = cantidadRevertidos;
    }

    public Integer getCantidadValidos() {
        return cantidadValidos;
    }

    public void setCantidadValidos(Integer cantidadValidos) {
        this.cantidadValidos = cantidadValidos;
    }

    public Integer getFormularioImporte() {
        return formularioImporte;
    }

    public void setFormularioImporte(Integer formularioImporte) {
        this.formularioImporte = formularioImporte;
    }

    public List<RcvVenta> getDatosVenta() {
        return datosVenta;
    }

    public void setDatosVenta(List<RcvVenta> datosVenta) {
        this.datosVenta = datosVenta;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cantidad", cantidad).append("cantidadAnulados", cantidadAnulados).append("cantidadRevertidos", cantidadRevertidos).append("cantidadValidos", cantidadValidos).append("formularioImporte", formularioImporte).append("datosVenta", datosVenta).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cantidadAnulados).append(cantidadValidos).append(formularioImporte).append(cantidad).append(cantidadRevertidos).append(datosVenta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvVentaDatos) == false) {
            return false;
        }
        RcvVentaDatos rhs = ((RcvVentaDatos) other);
        return new EqualsBuilder().append(cantidadAnulados, rhs.cantidadAnulados).append(cantidadValidos, rhs.cantidadValidos).append(formularioImporte, rhs.formularioImporte).append(cantidad, rhs.cantidad).append(cantidadRevertidos, rhs.cantidadRevertidos).append(datosVenta, rhs.datosVenta).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cantidad);
        dest.writeValue(cantidadAnulados);
        dest.writeValue(cantidadRevertidos);
        dest.writeValue(cantidadValidos);
        dest.writeValue(formularioImporte);
        dest.writeList(datosVenta);
    }

    public int describeContents() {
        return 0;
    }
}
