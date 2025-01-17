package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UsosVehiculoRespUnivida implements Serializable, Parcelable
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
    private List<UsoVehiculo> datos = null;
    public final static Parcelable.Creator<UsosVehiculoRespUnivida> CREATOR = new Creator<UsosVehiculoRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UsosVehiculoRespUnivida createFromParcel(Parcel in) {
            return new UsosVehiculoRespUnivida(in);
        }

        public UsosVehiculoRespUnivida[] newArray(int size) {
            return (new UsosVehiculoRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7401453312159875403L;

    protected UsosVehiculoRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.datos, (UsoVehiculo.class.getClassLoader()));
    }

    public UsosVehiculoRespUnivida() {
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

    public List<UsoVehiculo> getDatos() {
        return datos;
    }

    public void setDatos(List<UsoVehiculo> datos) {
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
        if ((other instanceof UsosVehiculoRespUnivida) == false) {
            return false;
        }
        UsosVehiculoRespUnivida rhs = ((UsosVehiculoRespUnivida) other);
        return new EqualsBuilder().append(codigoRetorno, rhs.codigoRetorno).append(exito, rhs.exito).append(datos, rhs.datos).append(mensaje, rhs.mensaje).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(exito);
        dest.writeValue(codigoRetorno);
        dest.writeValue(mensaje);
        dest.writeList(datos);
    }

    public int describeContents() {
        return 0;
    }
}
