package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UsoVehiculo implements Serializable, Parcelable
{

    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    public final static Parcelable.Creator<UsoVehiculo> CREATOR = new Creator<UsoVehiculo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UsoVehiculo createFromParcel(Parcel in) {
            return new UsoVehiculo(in);
        }

        public UsoVehiculo[] newArray(int size) {
            return (new UsoVehiculo[size]);
        }

    }
            ;
    private final static long serialVersionUID = -5865291332251649136L;

    protected UsoVehiculo(Parcel in) {
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UsoVehiculo() {
    }

    public UsoVehiculo(Integer secuencial) {
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
        return new HashCodeBuilder().append(secuencial).append(descripcion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UsoVehiculo) == false) {
            return false;
        }
        UsoVehiculo rhs = ((UsoVehiculo) other);
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
