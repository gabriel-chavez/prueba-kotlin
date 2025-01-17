package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

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

public class RcvJsonMedioPagoUnivida implements Serializable, Parcelable
{

    @SerializedName("Numero")
    @Expose
    private Integer numero;
    @SerializedName("MedioDePagoFk")
    @Expose
    private Integer medioDePagoFk;
    @SerializedName("MedioDePagoDescripcion")
    @Expose
    private String medioDePagoDescripcion;
    @SerializedName("MedioDePagoPrima")
    @Expose
    private Double medioDePagoPrima;
    @SerializedName("MedioDePagoDato")
    @Expose
    private String medioDePagoDato;
    @SerializedName("MedioDePagoFecha")
    @Expose
    private String medioDePagoFecha;
    @SerializedName("MedioBanco")
    @Expose
    private String medioBanco;
    @SerializedName("MedioBancoFk")
    @Expose
    private Integer medioBancoFk;
    @SerializedName("MedioSigno")
    @Expose
    private Integer medioSigno;
    public final static Parcelable.Creator<RcvJsonMedioPagoUnivida> CREATOR = new Creator<RcvJsonMedioPagoUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvJsonMedioPagoUnivida createFromParcel(Parcel in) {
            return new RcvJsonMedioPagoUnivida(in);
        }

        public RcvJsonMedioPagoUnivida[] newArray(int size) {
            return (new RcvJsonMedioPagoUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7066732793616180669L;

    protected RcvJsonMedioPagoUnivida(Parcel in) {
        this.numero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.medioDePagoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.medioDePagoDescripcion = ((String) in.readValue((String.class.getClassLoader())));
        this.medioDePagoPrima = ((Double) in.readValue((Double.class.getClassLoader())));
        this.medioDePagoDato = ((String) in.readValue((String.class.getClassLoader())));
        this.medioDePagoFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.medioBanco = ((String) in.readValue((String.class.getClassLoader())));
        this.medioBancoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.medioSigno = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public RcvJsonMedioPagoUnivida() {
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getMedioDePagoFk() {
        return medioDePagoFk;
    }

    public void setMedioDePagoFk(Integer medioDePagoFk) {
        this.medioDePagoFk = medioDePagoFk;
    }

    public String getMedioDePagoDescripcion() {
        return medioDePagoDescripcion;
    }

    public void setMedioDePagoDescripcion(String medioDePagoDescripcion) {
        this.medioDePagoDescripcion = medioDePagoDescripcion;
    }

    public Double getMedioDePagoPrima() {
        return medioDePagoPrima;
    }

    public void setMedioDePagoPrima(Double medioDePagoPrima) {
        this.medioDePagoPrima = medioDePagoPrima;
    }

    public String getMedioDePagoDato() {
        return medioDePagoDato;
    }

    public void setMedioDePagoDato(String medioDePagoDato) {
        this.medioDePagoDato = medioDePagoDato;
    }

    public String getMedioDePagoFecha() {
        return medioDePagoFecha;
    }

    public void setMedioDePagoFecha(String medioDePagoFecha) {
        this.medioDePagoFecha = medioDePagoFecha;
    }

    public String getMedioBanco() {
        return medioBanco;
    }

    public void setMedioBanco(String medioBanco) {
        this.medioBanco = medioBanco;
    }

    public Integer getMedioBancoFk() {
        return medioBancoFk;
    }

    public void setMedioBancoFk(Integer medioBancoFk) {
        this.medioBancoFk = medioBancoFk;
    }

    public Integer getMedioSigno() {
        return medioSigno;
    }

    public void setMedioSigno(Integer medioSigno) {
        this.medioSigno = medioSigno;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(medioBancoFk).append(medioDePagoFk).append(medioSigno).append(medioBanco).append(medioDePagoPrima).append(medioDePagoDato).append(medioDePagoDescripcion).append(medioDePagoFecha).append(numero).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvJsonMedioPagoUnivida) == false) {
            return false;
        }
        RcvJsonMedioPagoUnivida rhs = ((RcvJsonMedioPagoUnivida) other);
        return new EqualsBuilder().append(medioBancoFk, rhs.medioBancoFk).append(medioDePagoFk, rhs.medioDePagoFk).append(medioSigno, rhs.medioSigno).append(medioBanco, rhs.medioBanco).append(medioDePagoPrima, rhs.medioDePagoPrima).append(medioDePagoDato, rhs.medioDePagoDato).append(medioDePagoDescripcion, rhs.medioDePagoDescripcion).append(medioDePagoFecha, rhs.medioDePagoFecha).append(numero, rhs.numero).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(numero);
        dest.writeValue(medioDePagoFk);
        dest.writeValue(medioDePagoDescripcion);
        dest.writeValue(medioDePagoPrima);
        dest.writeValue(medioDePagoDato);
        dest.writeValue(medioDePagoFecha);
        dest.writeValue(medioBanco);
        dest.writeValue(medioBancoFk);
        dest.writeValue(medioSigno);
    }

    public int describeContents() {
        return 0;
    }
}
