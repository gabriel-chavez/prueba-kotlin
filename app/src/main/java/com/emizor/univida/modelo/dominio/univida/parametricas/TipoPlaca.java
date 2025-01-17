package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TipoPlaca  implements Serializable, Parcelable
{

    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("habilitado")
    @Expose
    private Boolean habilitado;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    public final static Parcelable.Creator<TipoPlaca> CREATOR = new Creator<TipoPlaca>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TipoPlaca createFromParcel(Parcel in) {
            return new TipoPlaca(in);
        }

        public TipoPlaca[] newArray(int size) {
            return (new TipoPlaca[size]);
        }

    }
            ;
    private final static long serialVersionUID = 9189600887122125249L;

    protected TipoPlaca(Parcel in) {
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.habilitado = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TipoPlaca() {
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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
        return new HashCodeBuilder().append(secuencial).append(habilitado).append(descripcion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TipoPlaca) == false) {
            return false;
        }
        TipoPlaca rhs = ((TipoPlaca) other);
        return new EqualsBuilder().append(secuencial, rhs.secuencial).append(habilitado, rhs.habilitado).append(descripcion, rhs.descripcion).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(secuencial);
        dest.writeValue(habilitado);
        dest.writeValue(descripcion);
    }

    public int describeContents() {
        return 0;
    }
}
