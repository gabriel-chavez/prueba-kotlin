package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DatoRcvLista implements Serializable, Parcelable
{

    @SerializedName("rcv_cantidad")
    @Expose
    private Integer rcvCantidad;
    @SerializedName("rcv_cantidad_anulados")
    @Expose
    private Integer rcvCantidadAnulados;
    @SerializedName("rcv_cantidad_validos")
    @Expose
    private Integer rcvCantidadValidos;
    @SerializedName("rcv_cantidad_revertidos")
    @Expose
    private Integer rcvCantidadRevertidos;
    @SerializedName("rcv_estado_remitido")
    @Expose
    private Boolean rcvEstadoRemitido;
    @SerializedName("rcv_formulario_fecha")
    @Expose
    private String rcvFormularioFecha;
    @SerializedName("rcv_formulario_importe")
    @Expose
    private Double rcvFormularioImporte;
    @SerializedName("rcv_secuencial")
    @Expose
    private Integer rcvSecuencial;
    @SerializedName("rcv_estado_fk")
    @Expose
    private Integer rcvEstadoFk;
    private String estado;
    public final static Parcelable.Creator<DatoRcvLista> CREATOR = new Creator<DatoRcvLista>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DatoRcvLista createFromParcel(Parcel in) {
            return new DatoRcvLista(in);
        }

        public DatoRcvLista[] newArray(int size) {
            return (new DatoRcvLista[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3934206261727602788L;

    protected DatoRcvLista(Parcel in) {
        this.rcvCantidad = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadAnulados = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadValidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvCantidadRevertidos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvEstadoRemitido = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.rcvFormularioFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.rcvFormularioImporte = ((Double) in.readValue((Double.class.getClassLoader())));
        this.rcvSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rcvEstadoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DatoRcvLista() {
    }

    public DatoRcvLista(Integer rcvSecuencial) {
        this.rcvSecuencial = rcvSecuencial;
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

    public Integer getRcvCantidadRevertidos() {
        return rcvCantidadRevertidos;
    }

    public void setRcvCantidadRevertidos(Integer rcvCantidadRevertidos) {
        this.rcvCantidadRevertidos = rcvCantidadRevertidos;
    }

    public String getRcvFormularioFecha() {
        return rcvFormularioFecha;
    }

    public void setRcvFormularioFecha(String rcvFormularioFecha) {
        this.rcvFormularioFecha = rcvFormularioFecha;
    }

    public Double getRcvFormularioImporte() {
        return rcvFormularioImporte;
    }

    public void setRcvFormularioImporte(Double rcvFormularioImporte) {
        this.rcvFormularioImporte = rcvFormularioImporte;
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

    public String getEstado() {
        return estado;
    }

    public void resetEstado(){
        if (getRcvEstadoFk() != null)
            switch (getRcvEstadoFk()){
                case 1:
                    setEstado("VALIDO");
                    break;
                case 2:
                    setEstado("REVERTIDO");
                    break;
                case 3:
                    setEstado("ANULADO");
                    break;
                default:
                    setEstado("NO TIENE");
                    break;
            }
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("rcvCantidad", rcvCantidad).append("rcvCantidadAnulados", rcvCantidadAnulados).append("rcvCantidadValidos", rcvCantidadValidos).append("rcvEstadoRemitido", rcvEstadoRemitido).append("rcvFormularioFecha", rcvFormularioFecha).append("rcvFormularioImporte", rcvFormularioImporte).append("rcvSecuencial", rcvSecuencial).append("rcvEstadoFk", rcvEstadoFk).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rcvSecuencial).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatoRcvLista) == false) {
            return false;
        }
        DatoRcvLista rhs = ((DatoRcvLista) other);
        return new EqualsBuilder().append(rcvSecuencial, rhs.rcvSecuencial).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rcvCantidad);
        dest.writeValue(rcvCantidadAnulados);
        dest.writeValue(rcvCantidadValidos);
        dest.writeValue(rcvCantidadRevertidos);
        dest.writeValue(rcvEstadoRemitido);
        dest.writeValue(rcvFormularioFecha);
        dest.writeValue(rcvFormularioImporte);
        dest.writeValue(rcvSecuencial);
        dest.writeValue(rcvEstadoFk);
    }

    public int describeContents() {
        return 0;
    }
}
