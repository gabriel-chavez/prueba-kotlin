package com.emizor.univida.modelo.dominio.univida.parametricas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Departamento implements Serializable, Parcelable
{

    @SerializedName("codigo")
    @Expose
    private String codigo;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    public final static Parcelable.Creator<Departamento> CREATOR = new Creator<Departamento>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Departamento createFromParcel(Parcel in) {
            return new Departamento(in);
        }

        public Departamento[] newArray(int size) {
            return (new Departamento[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6559103632524370110L;

    protected Departamento(Parcel in) {
        this.codigo = ((String) in.readValue((String.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Departamento() {
    }

    public Departamento(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        return new HashCodeBuilder().append(codigo).append(descripcion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Departamento) == false) {
            return false;
        }
        Departamento rhs = ((Departamento) other);
        return new EqualsBuilder().append(codigo, rhs.codigo).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(codigo);
        dest.writeValue(descripcion);
    }

    public int describeContents() {
        return 0;
    }
}
