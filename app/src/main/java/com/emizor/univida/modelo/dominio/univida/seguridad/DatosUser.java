package com.emizor.univida.modelo.dominio.univida.seguridad;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DatosUser implements Serializable, Parcelable
{

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("datos_usuario")
    @Expose
    private DatosUsuario datosUsuario;
    public final static Parcelable.Creator<DatosUser> CREATOR = new Creator<DatosUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DatosUser createFromParcel(Parcel in) {
            return new DatosUser(in);
        }

        public DatosUser[] newArray(int size) {
            return (new DatosUser[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7063240599123634195L;

    protected DatosUser(Parcel in) {
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        this.datosUsuario = ((DatosUsuario) in.readValue((DatosUsuario.class.getClassLoader())));
    }

    public DatosUser() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("token", token).append("datosUsuario", datosUsuario).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(datosUsuario).append(token).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatosUser) == false) {
            return false;
        }
        DatosUser rhs = ((DatosUser) other);
        return new EqualsBuilder().append(datosUsuario, rhs.datosUsuario).append(token, rhs.token).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(token);
        dest.writeValue(datosUsuario);
    }

    public int describeContents() {
        return 0;
    }

}
