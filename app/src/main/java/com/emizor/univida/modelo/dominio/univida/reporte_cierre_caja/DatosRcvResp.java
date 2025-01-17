package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DatosRcvResp implements Serializable, Parcelable
{

    @SerializedName("rcv_cantidad")
    @Expose
    private Integer rcvCantidad;
    @SerializedName("rcv_cantidad_anulados")
    @Expose
    private Integer rcvCantidadAnulados;
    @SerializedName("rcv_cantidad_revertidos")
    @Expose
    private Integer rcvCantidadRevertidos;
    @SerializedName("rcv_cantidad_validos")
    @Expose
    private Integer rcvCantidadValidos;
    @SerializedName("rcv_estado_remitido")
    @Expose
    private Boolean rcvEstadoRemitido;
    @SerializedName("rcv_fecha_elaborado")
    @Expose
    private String rcvFechaElaborado;
    @SerializedName("rcv_formulario_fecha")
    @Expose
    private String rcvFormularioFecha;
    @SerializedName("rcv_formulario_importe")
    @Expose
    private Integer rcvFormularioImporte;
    @SerializedName("rcv_formulario_numero")
    @Expose
    private Integer rcvFormularioNumero;
    @SerializedName("rcv_secuencial")
    @Expose
    private Integer rcvSecuencial;
    @SerializedName("rcv_estado_fk")
    @Expose
    private Integer rcvEstadoFk;
    @SerializedName("rcv_gestion_fk")
    @Expose
    private Integer rcvGestionFk;
    @SerializedName("rcv_venta_vendedor")
    @Expose
    private String rcvVentaVendedor;
    public final static Parcelable.Creator<DatosRcvResp> CREATOR = new Creator<DatosRcvResp>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DatosRcvResp createFromParcel(Parcel in) {
            return new DatosRcvResp(in);
        }

        public DatosRcvResp[] newArray(int size) {
            return (new DatosRcvResp[size]);
        }

    }
            ;
    private final static long serialVersionUID = 636936424990142619L;

    protected DatosRcvResp(Parcel in) {
        this.rcvCantidad = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadAnulados = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadRevertidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadValidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvEstadoRemitido = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.rcvFechaElaborado = ((String) in.readValue((String.class.getClassLoader())));
        this.rcvFormularioFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.rcvFormularioImporte = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvFormularioNumero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvEstadoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvGestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvVentaVendedor = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DatosRcvResp() {
    }

    public Integer getRcvCantidad() {
        return rcvCantidad;
    }

    public void setRcvCantidad(Integer rcvCantidad) {
        this.rcvCantidad = rcvCantidad;
    }

    public Integer getRcvCantidadAnulados() {
        return rcvCantidadAnulados;
    }

    public void setRcvCantidadAnulados(Integer rcvCantidadAnulados) {
        this.rcvCantidadAnulados = rcvCantidadAnulados;
    }

    public Integer getRcvCantidadRevertidos() {
        return rcvCantidadRevertidos;
    }

    public void setRcvCantidadRevertidos(Integer rcvCantidadRevertidos) {
        this.rcvCantidadRevertidos = rcvCantidadRevertidos;
    }

    public Integer getRcvCantidadValidos() {
        return rcvCantidadValidos;
    }

    public void setRcvCantidadValidos(Integer rcvCantidadValidos) {
        this.rcvCantidadValidos = rcvCantidadValidos;
    }

    public Boolean getRcvEstadoRemitido() {
        return rcvEstadoRemitido;
    }

    public void setRcvEstadoRemitido(Boolean rcvEstadoRemitido) {
        this.rcvEstadoRemitido = rcvEstadoRemitido;
    }

    public String getRcvFechaElaborado() {
        return rcvFechaElaborado;
    }

    public void setRcvFechaElaborado(String rcvFechaElaborado) {
        this.rcvFechaElaborado = rcvFechaElaborado;
    }

    public String getRcvFormularioFecha() {
        return rcvFormularioFecha;
    }

    public void setRcvFormularioFecha(String rcvFormularioFecha) {
        this.rcvFormularioFecha = rcvFormularioFecha;
    }

    public Integer getRcvFormularioImporte() {
        return rcvFormularioImporte;
    }

    public void setRcvFormularioImporte(Integer rcvFormularioImporte) {
        this.rcvFormularioImporte = rcvFormularioImporte;
    }

    public Integer getRcvFormularioNumero() {
        return rcvFormularioNumero;
    }

    public void setRcvFormularioNumero(Integer rcvFormularioNumero) {
        this.rcvFormularioNumero = rcvFormularioNumero;
    }

    public Integer getRcvSecuencial() {
        return rcvSecuencial;
    }

    public void setRcvSecuencial(Integer rcvSecuencial) {
        this.rcvSecuencial = rcvSecuencial;
    }

    public Integer getRcvEstadoFk() {
        return rcvEstadoFk;
    }

    public void setRcvEstadoFk(Integer rcvEstadoFk) {
        this.rcvEstadoFk = rcvEstadoFk;
    }

    public Integer getRcvGestionFk() {
        return rcvGestionFk;
    }

    public void setRcvGestionFk(Integer rcvGestionFk) {
        this.rcvGestionFk = rcvGestionFk;
    }

    public String getRcvVentaVendedor() {
        return rcvVentaVendedor;
    }

    public void setRcvVentaVendedor(String rcvVentaVendedor) {
        this.rcvVentaVendedor = rcvVentaVendedor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("rcvCantidad", rcvCantidad).append("rcvCantidadAnulados", rcvCantidadAnulados).append("rcvCantidadRevertidos", rcvCantidadRevertidos).append("rcvCantidadValidos", rcvCantidadValidos).append("rcvEstadoRemitido", rcvEstadoRemitido).append("rcvFechaElaborado", rcvFechaElaborado).append("rcvFormularioFecha", rcvFormularioFecha).append("rcvFormularioImporte", rcvFormularioImporte).append("rcvFormularioNumero", rcvFormularioNumero).append("rcvSecuencial", rcvSecuencial).append("rcvEstadoFk", rcvEstadoFk).append("rcvGestionFk", rcvGestionFk).append("rcvVentaVendedor", rcvVentaVendedor).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rcvFormularioFecha).append(rcvEstadoRemitido).append(rcvCantidadAnulados).append(rcvVentaVendedor).append(rcvSecuencial).append(rcvCantidadValidos).append(rcvFormularioImporte).append(rcvEstadoFk).append(rcvCantidad).append(rcvFechaElaborado).append(rcvGestionFk).append(rcvFormularioNumero).append(rcvCantidadRevertidos).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatosRcvResp) == false) {
            return false;
        }
        DatosRcvResp rhs = ((DatosRcvResp) other);
        return new EqualsBuilder().append(rcvFormularioFecha, rhs.rcvFormularioFecha).append(rcvEstadoRemitido, rhs.rcvEstadoRemitido).append(rcvCantidadAnulados, rhs.rcvCantidadAnulados).append(rcvVentaVendedor, rhs.rcvVentaVendedor).append(rcvSecuencial, rhs.rcvSecuencial).append(rcvCantidadValidos, rhs.rcvCantidadValidos).append(rcvFormularioImporte, rhs.rcvFormularioImporte).append(rcvEstadoFk, rhs.rcvEstadoFk).append(rcvCantidad, rhs.rcvCantidad).append(rcvFechaElaborado, rhs.rcvFechaElaborado).append(rcvGestionFk, rhs.rcvGestionFk).append(rcvFormularioNumero, rhs.rcvFormularioNumero).append(rcvCantidadRevertidos, rhs.rcvCantidadRevertidos).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rcvCantidad);
        dest.writeValue(rcvCantidadAnulados);
        dest.writeValue(rcvCantidadRevertidos);
        dest.writeValue(rcvCantidadValidos);
        dest.writeValue(rcvEstadoRemitido);
        dest.writeValue(rcvFechaElaborado);
        dest.writeValue(rcvFormularioFecha);
        dest.writeValue(rcvFormularioImporte);
        dest.writeValue(rcvFormularioNumero);
        dest.writeValue(rcvSecuencial);
        dest.writeValue(rcvEstadoFk);
        dest.writeValue(rcvGestionFk);
        dest.writeValue(rcvVentaVendedor);
    }

    public int describeContents() {
        return 0;
    }



}
