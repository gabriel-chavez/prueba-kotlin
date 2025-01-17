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

public class GestionRespUnivida implements Serializable, Parcelable
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
    private List<Gestion> datos = null;
    public final static Parcelable.Creator<GestionRespUnivida> CREATOR = new Creator<GestionRespUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GestionRespUnivida createFromParcel(Parcel in) {
            return new GestionRespUnivida(in);
        }

        public GestionRespUnivida[] newArray(int size) {
            return (new GestionRespUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6664203170319966062L;

    protected GestionRespUnivida(Parcel in) {
        this.exito = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.codigoRetorno = ((Long) in.readValue((Long.class.getClassLoader())));
        this.mensaje = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.datos, (Gestion.class.getClassLoader()));
    }

    public GestionRespUnivida() {
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

    public List<Gestion> getDatos() {
        return datos;
    }

    public void setDatos(List<Gestion> datos) {
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
        if ((other instanceof GestionRespUnivida) == false) {
            return false;
        }
        GestionRespUnivida rhs = ((GestionRespUnivida) other);
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
