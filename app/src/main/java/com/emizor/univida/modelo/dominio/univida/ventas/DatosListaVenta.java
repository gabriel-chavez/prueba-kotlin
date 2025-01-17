package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DatosListaVenta implements Serializable, Parcelable
{

    @SerializedName("rcv_cantidad_soat")
    @Expose
    private Integer rcvCantidadSoat;
    @SerializedName("rcv_cantidad_soat_validos")
    @Expose
    private Integer rcvCantidadSoatValidos;
    @SerializedName("rcv_cantidad_soat_revertidos")
    @Expose
    private Integer rcvCantidadSoatRevertidos;
    @SerializedName("rcv_cantidad_soat_anulados")
    @Expose
    private Integer rcvCantidadSoatAnulados;
    @SerializedName("rcv_formulario_importe")
    @Expose
    private Integer rcvFormularioImporte;
    @SerializedName("soat_datos_venta")
    @Expose
    private List<SoatDatosVentum> soatDatosVenta = null;
    public final static Parcelable.Creator<DatosListaVenta> CREATOR = new Creator<DatosListaVenta>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DatosListaVenta createFromParcel(Parcel in) {
            return new DatosListaVenta(in);
        }

        public DatosListaVenta[] newArray(int size) {
            return (new DatosListaVenta[size]);
        }

    }
            ;
    private final static long serialVersionUID = -5397534144545091171L;

    protected DatosListaVenta(Parcel in) {
        this.rcvCantidadSoat = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadSoatValidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadSoatRevertidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadSoatAnulados = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvFormularioImporte = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.soatDatosVenta, (SoatDatosVentum.class.getClassLoader()));
    }

    public DatosListaVenta() {
    }

    public Integer getRcvCantidadSoat() {
        return rcvCantidadSoat;
    }

    public void setRcvCantidadSoat(Integer rcvCantidadSoat) {
        this.rcvCantidadSoat = rcvCantidadSoat;
    }

    public Integer getRcvCantidadSoatValidos() {
        return rcvCantidadSoatValidos;
    }

    public void setRcvCantidadSoatValidos(Integer rcvCantidadSoatValidos) {
        this.rcvCantidadSoatValidos = rcvCantidadSoatValidos;
    }

    public Integer getRcvCantidadSoatRevertidos() {
        return rcvCantidadSoatRevertidos;
    }

    public void setRcvCantidadSoatRevertidos(Integer rcvCantidadSoatRevertidos) {
        this.rcvCantidadSoatRevertidos = rcvCantidadSoatRevertidos;
    }

    public Integer getRcvCantidadSoatAnulados() {
        return rcvCantidadSoatAnulados;
    }

    public void setRcvCantidadSoatAnulados(Integer rcvCantidadSoatAnulados) {
        this.rcvCantidadSoatAnulados = rcvCantidadSoatAnulados;
    }

    public Integer getRcvFormularioImporte() {
        return rcvFormularioImporte;
    }

    public void setRcvFormularioImporte(Integer rcvFormularioImporte) {
        this.rcvFormularioImporte = rcvFormularioImporte;
    }

    public List<SoatDatosVentum> getSoatDatosVenta() {
        return soatDatosVenta;
    }

    public void setSoatDatosVenta(List<SoatDatosVentum> soatDatosVenta) {
        this.soatDatosVenta = soatDatosVenta;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("rcvCantidadSoat", rcvCantidadSoat).append("rcvCantidadSoatValidos", rcvCantidadSoatValidos).append("rcvCantidadSoatRevertidos", rcvCantidadSoatRevertidos).append("rcvCantidadSoatAnulados", rcvCantidadSoatAnulados).append("rcvFormularioImporte", rcvFormularioImporte).append("soatDatosVenta", soatDatosVenta).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rcvCantidadSoatAnulados).append(soatDatosVenta).append(rcvCantidadSoatRevertidos).append(rcvFormularioImporte).append(rcvCantidadSoatValidos).append(rcvCantidadSoat).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatosListaVenta) == false) {
            return false;
        }
        DatosListaVenta rhs = ((DatosListaVenta) other);
        return new EqualsBuilder().append(rcvCantidadSoatAnulados, rhs.rcvCantidadSoatAnulados).append(soatDatosVenta, rhs.soatDatosVenta).append(rcvCantidadSoatRevertidos, rhs.rcvCantidadSoatRevertidos).append(rcvFormularioImporte, rhs.rcvFormularioImporte).append(rcvCantidadSoatValidos, rhs.rcvCantidadSoatValidos).append(rcvCantidadSoat, rhs.rcvCantidadSoat).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rcvCantidadSoat);
        dest.writeValue(rcvCantidadSoatValidos);
        dest.writeValue(rcvCantidadSoatRevertidos);
        dest.writeValue(rcvCantidadSoatAnulados);
        dest.writeValue(rcvFormularioImporte);
        dest.writeList(soatDatosVenta);
    }

    public int describeContents() {
        return 0;
    }
}
