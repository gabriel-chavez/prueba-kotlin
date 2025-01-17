package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidarVendibleRespUnivida implements Serializable, Parcelable
{

    @SerializedName("exito")
    @Expose
    private Boolean exito;
    @SerializedName("codigo_retorno")
    @Expose
    private Long codigoRetorno;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("datos")
    @Expose
    private VehiculoDatos datos;
    public final static Parcelable.Creator<ValidarVendibleRespUnivida> CREATOR = new Creator<ValidarVendibleRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ValidarVendibleRespUnivida createFromParcel(Parcel in) {
            return new ValidarVendibleRespUnivida(in);
        }

        public ValidarVendibleRespUnivida[] newArray(int size) {
            return (new ValidarVendibleRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7293080493350465832L;

    protected ValidarVendibleRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Long) in.readValue((Long.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.datos = ((VehiculoDatos) in.readValue((VehiculoDatos.class.getClassLoader())));
    }

    public ValidarVendibleRespUnivida() {
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public Long getCodigoRetorno() {
        return codigoRetorno;
    }

    public void setCodigoRetorno(Long codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public VehiculoDatos getDatos() {
        return datos;
    }

    public void setDatos(VehiculoDatos datos) {
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
        if ((other instanceof ValidarVendibleRespUnivida) == false) {
            return false;
        }
        ValidarVendibleRespUnivida rhs = ((ValidarVendibleRespUnivida) other);
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
