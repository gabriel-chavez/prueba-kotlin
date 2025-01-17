package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class VehiculoDatos implements Serializable, Parcelable
{

    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("vehiculo_tipo_fk")
    @Expose
    private Integer vehiculoTipoFk;
    @SerializedName("vehiculo_uso_fk")
    @Expose
    private Integer vehiculoUsoFk;
    @SerializedName("departamento_fk")
    @Expose
    private String departamentoFk;
    @SerializedName("roseta_numero")
    @Expose
    private Long rosetaNumero;
    public final static Parcelable.Creator<VehiculoDatos> CREATOR = new Creator<VehiculoDatos>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VehiculoDatos createFromParcel(Parcel in) {
            return new VehiculoDatos(in);
        }

        public VehiculoDatos[] newArray(int size) {
            return (new VehiculoDatos[size]);
        }

    }
            ;
    private final static long serialVersionUID = -5615668821581132874L;

    protected VehiculoDatos(Parcel in) {
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoTipoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoUsoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.departamentoFk = ((String) in.readValue((String.class.getClassLoader())));
        this.rosetaNumero = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public VehiculoDatos() {
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public Integer getVehiculoTipoFk() {
        return vehiculoTipoFk;
    }

    public void setVehiculoTipoFk(Integer vehiculoTipoFk) {
        this.vehiculoTipoFk = vehiculoTipoFk;
    }

    public Integer getVehiculoUsoFk() {
        return vehiculoUsoFk;
    }

    public void setVehiculoUsoFk(Integer vehiculoUsoFk) {
        this.vehiculoUsoFk = vehiculoUsoFk;
    }

    public String getDepartamentoFk() {
        return departamentoFk;
    }

    public void setDepartamentoFk(String departamentoFk) {
        this.departamentoFk = departamentoFk;
    }

    public Long getRosetaNumero() {
        return rosetaNumero;
    }

    public void setRosetaNumero(Long rosetaNumero) {
        this.rosetaNumero = rosetaNumero;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vehiculoPlaca", vehiculoPlaca).append("gestionFk", gestionFk).append("vehiculoTipoFk", vehiculoTipoFk).append("vehiculoUsoFk", vehiculoUsoFk).append("departamentoFk", departamentoFk).append("rosetaNumero", rosetaNumero).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(departamentoFk).append(vehiculoPlaca).append(vehiculoTipoFk).append(gestionFk).append(vehiculoUsoFk).append(rosetaNumero).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiculoDatos) == false) {
            return false;
        }
        VehiculoDatos rhs = ((VehiculoDatos) other);
        return new EqualsBuilder().append(departamentoFk, rhs.departamentoFk).append(vehiculoPlaca, rhs.vehiculoPlaca).append(vehiculoTipoFk, rhs.vehiculoTipoFk).append(gestionFk, rhs.gestionFk).append(vehiculoUsoFk, rhs.vehiculoUsoFk).append(rosetaNumero, rhs.rosetaNumero).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(gestionFk);
        dest.writeValue(vehiculoTipoFk);
        dest.writeValue(vehiculoUsoFk);
        dest.writeValue(departamentoFk);
        dest.writeValue(rosetaNumero);
    }

    public int describeContents() {
        return 0;
    }
}
