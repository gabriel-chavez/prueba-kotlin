package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ArchivoAdjunto implements Serializable, Parcelable
{

    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("extension")
    @Expose
    private String extension;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("imagen_base_64")
    @Expose
    private String imagenBase64;
    public final static Parcelable.Creator<ArchivoAdjunto> CREATOR = new Creator<ArchivoAdjunto>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ArchivoAdjunto createFromParcel(Parcel in) {
            return new ArchivoAdjunto(in);
        }

        public ArchivoAdjunto[] newArray(int size) {
            return (new ArchivoAdjunto[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7198810758692397380L;

    protected ArchivoAdjunto(Parcel in) {
        this.nombre = ((String) in.readValue((String.class.getClassLoader())));
        this.extension = ((String) in.readValue((String.class.getClassLoader())));
        this.descripcion = ((String) in.readValue((String.class.getClassLoader())));
        this.imagenBase64 = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ArchivoAdjunto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("nombre", nombre).append("extension", extension).append("descripcion", descripcion).append("imagenBase64", imagenBase64).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(extension).append(nombre).append(imagenBase64).append(descripcion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ArchivoAdjunto) == false) {
            return false;
        }
        ArchivoAdjunto rhs = ((ArchivoAdjunto) other);
        return new EqualsBuilder().append(extension, rhs.extension).append(nombre, rhs.nombre).append(imagenBase64, rhs.imagenBase64).append(descripcion, rhs.descripcion).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(nombre);
        dest.writeValue(extension);
        dest.writeValue(descripcion);
        dest.writeValue(imagenBase64);
    }

    public int describeContents() {
        return 0;
    }
}
