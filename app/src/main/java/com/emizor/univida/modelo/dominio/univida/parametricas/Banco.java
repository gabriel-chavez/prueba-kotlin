package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Banco implements Serializable, Parcelable
{

    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    public final static Parcelable.Creator<Banco> CREATOR = new Creator<Banco>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Banco createFromParcel(Parcel in) {
            return new Banco(in);
        }

        public Banco[] newArray(int size) {
            return (new Banco[size]);
        }

    }
            ;
    private final static long serialVersionUID = 8448503055856947584L;

    protected Banco(Parcel in) {
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Banco() {
    }

    public Banco(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
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
        return new HashCodeBuilder().append(secuencial).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Banco) == false) {
            return false;
        }
        Banco rhs = ((Banco) other);
        return new EqualsBuilder().append(secuencial, rhs.secuencial).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(secuencial);
        dest.writeValue(descripcion);
    }

    public int describeContents() {
        return 0;
    }
}
