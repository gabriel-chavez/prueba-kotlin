package com.emizor.univida.modelo.dominio.univida.seguridad;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CambiarClaveUnivida implements Serializable, Parcelable
{

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("new_password")
    @Expose
    private String newPassword;
    @SerializedName("username")
    @Expose
    private String username;
    public final static Parcelable.Creator<CambiarClaveUnivida> CREATOR = new Creator<CambiarClaveUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CambiarClaveUnivida createFromParcel(Parcel in) {
            return new CambiarClaveUnivida(in);
        }

        public CambiarClaveUnivida[] newArray(int size) {
            return (new CambiarClaveUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1630554514582703423L;

    protected CambiarClaveUnivida(Parcel in) {
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.newPassword = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CambiarClaveUnivida() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(username).append(newPassword).append(password).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CambiarClaveUnivida) == false) {
            return false;
        }
        CambiarClaveUnivida rhs = ((CambiarClaveUnivida) other);
        return new EqualsBuilder().append(username, rhs.username).append(newPassword, rhs.newPassword).append(password, rhs.password).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(password);
        dest.writeValue(newPassword);
        dest.writeValue(username);
    }

    public int describeContents() {
        return 0;
    }

}
