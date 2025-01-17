package com.emizor.univida.modelo.dominio.univida.parametricas;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class TipoDocumentoIdentidad implements Serializable, Parcelable
{

    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("requiere_complemento")
    @Expose
    private Boolean requiereComplemento;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    public final static Creator<TipoDocumentoIdentidad> CREATOR = new Creator<TipoDocumentoIdentidad>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TipoDocumentoIdentidad createFromParcel(Parcel in) {
            return new TipoDocumentoIdentidad(in);
        }

        public TipoDocumentoIdentidad[] newArray(int size) {
            return (new TipoDocumentoIdentidad[size]);
        }

    }
            ;
    private final static long serialVersionUID = 9189600887122125249L;

    protected TipoDocumentoIdentidad(Parcel in) {
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.requiereComplemento = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TipoDocumentoIdentidad() {
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public Boolean getRequiereComplemento() {
        return requiereComplemento;
    }

    public void setRequiereComplemento(Boolean requiereComplemento) {
        this.requiereComplemento = requiereComplemento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(secuencial).append(requiereComplemento).append(descripcion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TipoDocumentoIdentidad) == false) {
            return false;
        }
        TipoDocumentoIdentidad rhs = ((TipoDocumentoIdentidad) other);
        return new EqualsBuilder().append(secuencial, rhs.secuencial).append(requiereComplemento, rhs.requiereComplemento).append(descripcion, rhs.descripcion).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(secuencial);
        dest.writeValue(requiereComplemento);
        dest.writeValue(descripcion);
    }

    public int describeContents() {
        return 0;
    }
}
