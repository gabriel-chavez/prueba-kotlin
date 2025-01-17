package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EfectivizarRespUnivida implements Serializable, Parcelable
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
    private Datos datos;
    public final static Parcelable.Creator<EfectivizarRespUnivida> CREATOR = new Creator<EfectivizarRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EfectivizarRespUnivida createFromParcel(Parcel in) {
            return new EfectivizarRespUnivida(in);
        }

        public EfectivizarRespUnivida[] newArray(int size) {
            return (new EfectivizarRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7792354662505903003L;

    protected EfectivizarRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.datos = ((Datos) in.readValue((Datos.class.getClassLoader())));
    }

    public EfectivizarRespUnivida() {
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

    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("exito", exito).append("codigoRetorno", codigoRetorno).append("mensaje", mensaje).append("datos", datos).toString();
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
        if ((other instanceof EfectivizarRespUnivida) == false) {
            return false;
        }
        EfectivizarRespUnivida rhs = ((EfectivizarRespUnivida) other);
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
