package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RcvListarVentaRespUnivida implements Serializable, Parcelable
{

    @SerializedName("exito")
    @Expose
    private Boolean exito;
    @SerializedName("codigo_retorno")
    @Expose
    private Integer codigoRetorno;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("datos")
    @Expose
    private RcvVentaDatos datos;
    @SerializedName("fecharcv")
    @Expose
    private String fechaRcv;

    public final static Parcelable.Creator<RcvListarVentaRespUnivida> CREATOR = new Creator<RcvListarVentaRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvListarVentaRespUnivida createFromParcel(Parcel in) {
            return new RcvListarVentaRespUnivida(in);
        }

        public RcvListarVentaRespUnivida[] newArray(int size) {
            return (new RcvListarVentaRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 49875264506924436L;

    protected RcvListarVentaRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.datos = ((RcvVentaDatos) in.readValue((RcvVentaDatos.class.getClassLoader())));
    }

    public RcvListarVentaRespUnivida() {
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public Integer getCodigoRetorno() {
        return codigoRetorno;
    }

    public void setCodigoRetorno(Integer codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public RcvVentaDatos getDatos() {
        return datos;
    }

    public void setDatos(RcvVentaDatos datos) {
        this.datos = datos;
    }

    public String getFechaRcv() {
        return fechaRcv;
    }

    public void setFechaRcv(String fechaRcv) {
        this.fechaRcv = fechaRcv;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("exito", exito).append("codigoRetorno", codigoRetorno).append("mensaje", mensaje).append("datos", datos).append("fecharcv", fechaRcv).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(codigoRetorno).append(exito).append(datos).append(mensaje).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvListarVentaRespUnivida) == false) {
            return false;
        }
        RcvListarVentaRespUnivida rhs = ((RcvListarVentaRespUnivida) other);
        return new EqualsBuilder().append(codigoRetorno, rhs.codigoRetorno).append(exito, rhs.exito).append(datos, rhs.datos).append(mensaje, rhs.mensaje).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(exito);
        dest.writeValue(codigoRetorno);
        dest.writeValue(mensaje);
        dest.writeValue(datos);
    }

    public int describeContents() {
        return 0;
    }
}
