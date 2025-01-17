package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MedioPago implements Serializable, Parcelable
{

    @SerializedName("secuencial")
    @Expose
    private Integer secuencial;
    @SerializedName("secuencial_dato")
    @Expose
    private String secuencialDato;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("datos_completos")
    @Expose
    private String datosCompletos;
    public final static Parcelable.Creator<MedioPago> CREATOR = new Creator<MedioPago>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MedioPago createFromParcel(Parcel in) {
            return new MedioPago(in);
        }

        public MedioPago[] newArray(int size) {
            return (new MedioPago[size]);
        }

    }
            ;
    private final static long serialVersionUID = 445704798678925200L;

    protected MedioPago(Parcel in) {
        this.secuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.secuencialDato = ((String) in.readValue((String.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
        this.datosCompletos = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MedioPago() {
    }

    public MedioPago(Integer secuencial) {
        this.secuencial = secuencial;
    }


    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public String getSecuencialDato() {
        return secuencialDato;
    }

    public void setSecuencialDato(String secuencialDato) {
        this.secuencialDato = secuencialDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDatosCompletos() {
        return datosCompletos;
    }

    public void setDatosCompletos(String datosCompletos) {
        this.datosCompletos = datosCompletos;
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

        if ((other instanceof MedioPago) == false) {
            return false;
        }

        MedioPago rhs = ((MedioPago) other);
        return new EqualsBuilder().append(secuencial, rhs.secuencial).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(secuencial);
        dest.writeValue(secuencialDato);
        dest.writeValue(descripcion);
        dest.writeValue(datosCompletos);
    }

    public int describeContents() {
        return 0;
    }
}
