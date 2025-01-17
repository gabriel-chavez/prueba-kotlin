package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ObtenerPrimaRespUnivida implements Serializable, Parcelable
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
    private Integer montoPrima;
    public final static Parcelable.Creator<ObtenerPrimaRespUnivida> CREATOR = new Creator<ObtenerPrimaRespUnivida>() {

        @SuppressWarnings({
                "unchecked"
        })
        public ObtenerPrimaRespUnivida createFromParcel(Parcel in) {
            return new ObtenerPrimaRespUnivida(in);
        }

        public ObtenerPrimaRespUnivida[] newArray(int size) {
            return (new ObtenerPrimaRespUnivida[size]);
        }

    };

    private final static long serialVersionUID = 4750522589260054533L;

    protected ObtenerPrimaRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.montoPrima = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ObtenerPrimaRespUnivida() {
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

    public Integer getMontoPrima() {
        return montoPrima;
    }

    public void setMontoPrima(Integer montoPrima) {
        this.montoPrima = montoPrima;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("exito", exito).append("codigoRetorno", codigoRetorno).append("mensaje", mensaje).append("montoPrima", montoPrima).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(codigoRetorno).append(exito).append(montoPrima).append(mensaje).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ObtenerPrimaRespUnivida) == false) {
            return false;
        }
        ObtenerPrimaRespUnivida rhs = ((ObtenerPrimaRespUnivida) other);
        return new EqualsBuilder().append(codigoRetorno, rhs.codigoRetorno).append(exito, rhs.exito).append(montoPrima, rhs.montoPrima).append(mensaje, rhs.mensaje).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(exito);
        dest.writeValue(codigoRetorno);
        dest.writeValue(mensaje);
        dest.writeValue(montoPrima);
    }

    public int describeContents() {
        return 0;
    }
}
