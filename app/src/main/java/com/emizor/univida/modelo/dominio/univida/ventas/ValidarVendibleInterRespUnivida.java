package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidarVendibleInterRespUnivida implements Serializable, Parcelable {

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
    private VehiculoDatosInter datos;
    public final static Parcelable.Creator<ValidarVendibleInterRespUnivida> CREATOR = new Creator<ValidarVendibleInterRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ValidarVendibleInterRespUnivida createFromParcel(Parcel in) {
            return new ValidarVendibleInterRespUnivida(in);
        }

        public ValidarVendibleInterRespUnivida[] newArray(int size) {
            return (new ValidarVendibleInterRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6778019769098920961L;

    protected ValidarVendibleInterRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.datos = ((VehiculoDatosInter) in.readValue((VehiculoDatosInter.class.getClassLoader())));
    }

    public ValidarVendibleInterRespUnivida() {
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

    public VehiculoDatosInter getDatos() {
        return datos;
    }

    public void setDatos(VehiculoDatosInter datos) {
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
        if ((other instanceof ValidarVendibleInterRespUnivida) == false) {
            return false;
        }
        ValidarVendibleInterRespUnivida rhs = ((ValidarVendibleInterRespUnivida) other);
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
