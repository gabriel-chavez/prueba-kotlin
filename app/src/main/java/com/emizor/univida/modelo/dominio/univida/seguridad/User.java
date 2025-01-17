
package com.emizor.univida.modelo.dominio.univida.seguridad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.*;
import java.lang.Object;

public class User implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("account_id")
    @Expose
    private String accountId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("authentication_token")
    @Expose
    private String tokenAuth;

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("datos_usuario")
    @Expose
    private DatosUsuario datosUsuario;

    // el estado del usuario 1 validadado(con sesion), 2 con parametricas obtenidas, 0 no tiene sesion
    private int estado = 0;

    public final static Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2065194714370983464L;

    protected User(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.accountId = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.tokenAuth = ((String) in.readValue((String.class.getClassLoader())));
        this.datosUsuario = ((DatosUsuario) in.readValue((DatosUsuario.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTokenAuth() {
        return tokenAuth;
    }

    public void setTokenAuth(String tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id",id).append("accountId", accountId).append("firstName", firstName)
                .append("lastName", lastName).append("tokenAuth", tokenAuth).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(lastName).append(accountId).append(tokenAuth)
                .append(firstName).append(datosUsuario).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof User) == false) {
            return false;
        }
        User rhs = ((User) other);
        return new EqualsBuilder().append(id, rhs.id).append(lastName, rhs.lastName).append(accountId, rhs.accountId)
                .append(tokenAuth, rhs.tokenAuth).append(firstName, rhs.firstName).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(accountId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(tokenAuth);
        dest.writeValue(datosUsuario);
    }

    public int describeContents() {
        return  0;
    }

}
