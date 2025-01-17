package com.emizor.univida.modelo.dominio.univida.seguridad;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginRespUnivida implements Serializable, Parcelable
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
    private DatosUser datosUser;
    public final static Parcelable.Creator<LoginRespUnivida> CREATOR = new Creator<LoginRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LoginRespUnivida createFromParcel(Parcel in) {
            return new LoginRespUnivida(in);
        }

        public LoginRespUnivida[] newArray(int size) {
            return (new LoginRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2574356308334776611L;

    protected LoginRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Long) in.readValue((Long.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.datosUser = ((DatosUser) in.readValue((DatosUser.class.getClassLoader())));
    }

    public LoginRespUnivida() {
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

    public DatosUser getDatosUser() {
        return datosUser;
    }

    public void setDatosUser(DatosUser datosUser) {
        this.datosUser = datosUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("exito", exito).append("codigoRetorno", codigoRetorno).append("mensaje", mensaje).append("datosUser", datosUser).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(codigoRetorno).append(exito).append(datosUser).append(mensaje).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LoginRespUnivida) == false) {
            return false;
        }
        LoginRespUnivida rhs = ((LoginRespUnivida) other);
        return new EqualsBuilder().append(codigoRetorno, rhs.codigoRetorno).append(exito, rhs.exito).append(datosUser, rhs.datosUser).append(mensaje, rhs.mensaje).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(exito);
        dest.writeValue(codigoRetorno);
        dest.writeValue(mensaje);
        dest.writeValue(datosUser);
    }

    public int describeContents() {
        return 0;
    }
}
