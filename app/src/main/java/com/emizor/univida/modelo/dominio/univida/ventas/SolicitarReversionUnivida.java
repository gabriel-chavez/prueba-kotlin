package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SolicitarReversionUnivida implements Serializable, Parcelable
{

    @SerializedName("numero_comprobante")
    @Expose
    private Long numeroComprobante;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("motivo")
    @Expose
    private String motivo;
    public final static Parcelable.Creator<SolicitarReversionUnivida> CREATOR = new Creator<SolicitarReversionUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SolicitarReversionUnivida createFromParcel(Parcel in) {
            return new SolicitarReversionUnivida(in);
        }

        public SolicitarReversionUnivida[] newArray(int size) {
            return (new SolicitarReversionUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -2405358587642722757L;

    protected SolicitarReversionUnivida(Parcel in) {
        this.numeroComprobante = ((Long) in.readValue((Long.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.motivo = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SolicitarReversionUnivida() {
    }

    public Long getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(Long numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(numeroComprobante).append(motivo).append(vehiculoPlaca).append(gestionFk).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SolicitarReversionUnivida) == false) {
            return false;
        }
        SolicitarReversionUnivida rhs = ((SolicitarReversionUnivida) other);
        return new EqualsBuilder().append(numeroComprobante, rhs.numeroComprobante).append(motivo, rhs.motivo).append(vehiculoPlaca, rhs.vehiculoPlaca).append(gestionFk, rhs.gestionFk).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(numeroComprobante);
        dest.writeValue(gestionFk);
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(motivo);
    }

    public int describeContents() {
        return 0;
    }
}
