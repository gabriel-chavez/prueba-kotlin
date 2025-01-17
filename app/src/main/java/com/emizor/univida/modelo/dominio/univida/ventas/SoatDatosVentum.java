package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SoatDatosVentum implements Serializable, Parcelable
{

    @SerializedName("soat_numero_comprobante")
    @Expose
    private Long soatNumeroComprobante;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("factura_autorizacion_numero")
    @Expose
    private String facturaAutorizacionNumero;
    @SerializedName("factura_numero")
    @Expose
    private Integer facturaNumero;
    @SerializedName("factura_fecha")
    @Expose
    private String facturaFecha;
    @SerializedName("factura_prima")
    @Expose
    private Double facturaPrima;
    @SerializedName("soat_generica_estado_fk")
    @Expose
    private Integer soatGenericaEstadoFk;
    private String estado;
    public final static Parcelable.Creator<SoatDatosVentum> CREATOR = new Creator<SoatDatosVentum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SoatDatosVentum createFromParcel(Parcel in) {
            return new SoatDatosVentum(in);
        }

        public SoatDatosVentum[] newArray(int size) {
            return (new SoatDatosVentum[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3758526623012788984L;

    protected SoatDatosVentum(Parcel in) {
        this.soatNumeroComprobante = ((Long) in.readValue((Long.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaAutorizacionNumero = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaNumero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.facturaFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaPrima = ((Double) in.readValue((Double.class.getClassLoader())));
        this.soatGenericaEstadoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public SoatDatosVentum() {
    }

    public Long getSoatNumeroComprobante() {
        return soatNumeroComprobante;
    }

    public void setSoatNumeroComprobante(Long soatNumeroComprobante) {
        this.soatNumeroComprobante = soatNumeroComprobante;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getFacturaAutorizacionNumero() {
        return facturaAutorizacionNumero;
    }

    public void setFacturaAutorizacionNumero(String facturaAutorizacionNumero) {
        this.facturaAutorizacionNumero = facturaAutorizacionNumero;
    }

    public Integer getFacturaNumero() {
        return facturaNumero;
    }

    public void setFacturaNumero(Integer facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    public String getFacturaFecha() {
        return facturaFecha;
    }

    public void setFacturaFecha(String facturaFecha) {
        this.facturaFecha = facturaFecha;
    }

    public Double getFacturaPrima() {
        return facturaPrima;
    }

    public void setFacturaPrima(Double facturaPrima) {
        this.facturaPrima = facturaPrima;
    }

    public Integer getSoatGenericaEstadoFk() {
        return soatGenericaEstadoFk;
    }

    public void setSoatGenericaEstadoFk(Integer soatGenericaEstadoFk) {
        this.soatGenericaEstadoFk = soatGenericaEstadoFk;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void resetEstado(){
        switch (soatGenericaEstadoFk){
            case 1:
                setEstado("VALIDO");
                break;
            case 2:
                setEstado("REVERTIDO");
                break;
            case 3:
                setEstado("ANULADO");
                break;
            default:
                setEstado("NO TIENE");
                break;
        }
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("soatNumeroComprobante", soatNumeroComprobante).append("vehiculoPlaca", vehiculoPlaca).append("facturaAutorizacionNumero", facturaAutorizacionNumero).append("facturaNumero", facturaNumero).append("facturaFecha", facturaFecha).append("facturaPrima", facturaPrima).append("soatGenericaEstadoFk", soatGenericaEstadoFk).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(soatNumeroComprobante).append(vehiculoPlaca).append(facturaFecha).append(soatGenericaEstadoFk).append(facturaAutorizacionNumero).append(facturaPrima).append(facturaNumero).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SoatDatosVentum) == false) {
            return false;
        }
        SoatDatosVentum rhs = ((SoatDatosVentum) other);
        return new EqualsBuilder().append(soatNumeroComprobante, rhs.soatNumeroComprobante).append(vehiculoPlaca, rhs.vehiculoPlaca).append(facturaFecha, rhs.facturaFecha).append(soatGenericaEstadoFk, rhs.soatGenericaEstadoFk).append(facturaAutorizacionNumero, rhs.facturaAutorizacionNumero).append(facturaPrima, rhs.facturaPrima).append(facturaNumero, rhs.facturaNumero).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(soatNumeroComprobante);
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(facturaAutorizacionNumero);
        dest.writeValue(facturaNumero);
        dest.writeValue(facturaFecha);
        dest.writeValue(facturaPrima);
        dest.writeValue(soatGenericaEstadoFk);
    }

    public int describeContents() {
        return 0;
    }
}
