package com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RcvVenta implements Serializable, Parcelable
{

    @SerializedName("factura_aurotizacion_numero")
    @Expose
    private String facturaAurotizacionNumero;
    @SerializedName("factura_fecha")
    @Expose
    private String facturaFecha;
    @SerializedName("factura_numero")
    @Expose
    private Integer facturaNumero;
    @SerializedName("factura_prima")
    @Expose
    private Double facturaPrima;
    @SerializedName("soat_numero_comprobante")
    @Expose
    private Integer soatNumeroComprobante;
    @SerializedName("soat_estado")
    @Expose
    private Integer soatEstado;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    private String estado;
    public final static Parcelable.Creator<RcvVenta> CREATOR = new Creator<RcvVenta>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RcvVenta createFromParcel(Parcel in) {
            return new RcvVenta(in);
        }

        public RcvVenta[] newArray(int size) {
            return (new RcvVenta[size]);
        }

    }
            ;
    private final static long serialVersionUID = 3174802457673609356L;

    protected RcvVenta(Parcel in) {
        this.facturaAurotizacionNumero = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaFecha = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaNumero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.facturaPrima = ((Double) in.readValue((Double.class.getClassLoader())));
        this.soatNumeroComprobante = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soatEstado = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RcvVenta() {
    }

    public String getFacturaAurotizacionNumero() {
        return facturaAurotizacionNumero;
    }

    public void setFacturaAurotizacionNumero(String facturaAurotizacionNumero) {
        this.facturaAurotizacionNumero = facturaAurotizacionNumero;
    }

    public String getFacturaFecha() {
        return facturaFecha;
    }

    public void setFacturaFecha(String facturaFecha) {
        this.facturaFecha = facturaFecha;
    }

    public Integer getFacturaNumero() {
        return facturaNumero;
    }

    public void setFacturaNumero(Integer facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    public Double getFacturaPrima() {
        return facturaPrima;
    }

    public void setFacturaPrima(Double facturaPrima) {
        this.facturaPrima = facturaPrima;
    }

    public Integer getSoatNumeroComprobante() {
        return soatNumeroComprobante;
    }

    public void setSoatNumeroComprobante(Integer soatNumeroComprobante) {
        this.soatNumeroComprobante = soatNumeroComprobante;
    }

    public Integer getSoatEstado() {
        return soatEstado;
    }

    public void setSoatEstado(Integer soatEstado) {
        this.soatEstado = soatEstado;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void resetEstado(){
        switch (getSoatEstado()){
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("facturaAurotizacionNumero", facturaAurotizacionNumero).append("facturaFecha", facturaFecha).append("facturaNumero", facturaNumero).append("facturaPrima", facturaPrima).append("soatNumeroComprobante", soatNumeroComprobante).append("soatEstado", soatEstado).append("vehiculoPlaca", vehiculoPlaca).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(soatEstado).append(soatNumeroComprobante).append(vehiculoPlaca).append(facturaFecha).append(facturaPrima).append(facturaNumero).append(facturaAurotizacionNumero).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RcvVenta) == false) {
            return false;
        }
        RcvVenta rhs = ((RcvVenta) other);
        return new EqualsBuilder().append(soatEstado, rhs.soatEstado).append(soatNumeroComprobante, rhs.soatNumeroComprobante).append(vehiculoPlaca, rhs.vehiculoPlaca).append(facturaFecha, rhs.facturaFecha).append(facturaPrima, rhs.facturaPrima).append(facturaNumero, rhs.facturaNumero).append(facturaAurotizacionNumero, rhs.facturaAurotizacionNumero).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(facturaAurotizacionNumero);
        dest.writeValue(facturaFecha);
        dest.writeValue(facturaNumero);
        dest.writeValue(facturaPrima);
        dest.writeValue(soatNumeroComprobante);
        dest.writeValue(soatEstado);
        dest.writeValue(vehiculoPlaca);
    }

    public int describeContents() {
        return 0;
    }
}
