package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Detalle implements Serializable, Parcelable
{

    @SerializedName("cantidad")
    @Expose
    private Double cantidad;
    @SerializedName("importe_total")
    @Expose
    private Double importeTotal;
    @SerializedName("importe_unitario")
    @Expose
    private Double importeUnitario;
    @SerializedName("linea_detalle")
    @Expose
    private String lineaDetalle;
    @SerializedName("precio_unitario")
    @Expose
    private Double precioUnitario;
    @SerializedName("catalogo_codigo")
    @Expose
    private Integer catalogoCodigo;
    @SerializedName("descuento")
    @Expose
    private Double descuento;
    @SerializedName("descuento_str")
    @Expose
    private String descuentoStr;
    @SerializedName("importe_total_str")
    @Expose
    private String importeTotalStr;
    @SerializedName("importe_unitario_str")
    @Expose
    private String importeUnitarioStr;
    @SerializedName("precio_unitario_str")
    @Expose
    private String precioUnitarioStr;
    @SerializedName("unidad_medida")
    @Expose
    private String unidadMedida;
    public final static Parcelable.Creator<Detalle> CREATOR = new Creator<Detalle>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Detalle createFromParcel(Parcel in) {
            return new Detalle(in);
        }

        public Detalle[] newArray(int size) {
            return (new Detalle[size]);
        }

    }
            ;
    private final static long serialVersionUID = 3777455154970242710L;

    protected Detalle(Parcel in) {
        this.cantidad = ((Double) in.readValue((Double.class.getClassLoader())));
        this.importeTotal = ((Double) in.readValue((Double.class.getClassLoader())));
        this.importeUnitario = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lineaDetalle = ((String) in.readValue((String.class.getClassLoader())));
        this.precioUnitario = ((Double) in.readValue((Double.class.getClassLoader())));
        this.catalogoCodigo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.descuento = ((Double) in.readValue((Double.class.getClassLoader())));
        this.descuentoStr = ((String) in.readValue((String.class.getClassLoader())));
        this.importeTotalStr = ((String) in.readValue((String.class.getClassLoader())));
        this.importeUnitarioStr = ((String) in.readValue((String.class.getClassLoader())));
        this.precioUnitarioStr = ((String) in.readValue((String.class.getClassLoader())));
        this.unidadMedida = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Detalle() {
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Detalle withCantidad(Double cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Detalle withImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
        return this;
    }

    public Double getImporteUnitario() {
        return importeUnitario;
    }

    public void setImporteUnitario(Double importeUnitario) {
        this.importeUnitario = importeUnitario;
    }

    public Detalle withImporteUnitario(Double importeUnitario) {
        this.importeUnitario = importeUnitario;
        return this;
    }

    public String getLineaDetalle() {
        return lineaDetalle;
    }

    public void setLineaDetalle(String lineaDetalle) {
        this.lineaDetalle = lineaDetalle;
    }

    public Detalle withLineaDetalle(String lineaDetalle) {
        this.lineaDetalle = lineaDetalle;
        return this;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Detalle withPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        return this;
    }

    public Integer getCatalogoCodigo() {
        return catalogoCodigo;
    }

    public void setCatalogoCodigo(Integer catalogoCodigo) {
        this.catalogoCodigo = catalogoCodigo;
    }

    public Detalle withCatalogoCodigo(Integer catalogoCodigo) {
        this.catalogoCodigo = catalogoCodigo;
        return this;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Detalle withDescuento(Double descuento) {
        this.descuento = descuento;
        return this;
    }

    public String getDescuentoStr() {
        return descuentoStr;
    }

    public void setDescuentoStr(String descuentoStr) {
        this.descuentoStr = descuentoStr;
    }

    public Detalle withDescuentoStr(String descuentoStr) {
        this.descuentoStr = descuentoStr;
        return this;
    }

    public String getImporteTotalStr() {
        return importeTotalStr;
    }

    public void setImporteTotalStr(String importeTotalStr) {
        this.importeTotalStr = importeTotalStr;
    }

    public Detalle withImporteTotalStr(String importeTotalStr) {
        this.importeTotalStr = importeTotalStr;
        return this;
    }

    public String getImporteUnitarioStr() {
        return importeUnitarioStr;
    }

    public void setImporteUnitarioStr(String importeUnitarioStr) {
        this.importeUnitarioStr = importeUnitarioStr;
    }

    public Detalle withImporteUnitarioStr(String importeUnitarioStr) {
        this.importeUnitarioStr = importeUnitarioStr;
        return this;
    }

    public String getPrecioUnitarioStr() {
        return precioUnitarioStr;
    }

    public void setPrecioUnitarioStr(String precioUnitarioStr) {
        this.precioUnitarioStr = precioUnitarioStr;
    }

    public Detalle withPrecioUnitarioStr(String precioUnitarioStr) {
        this.precioUnitarioStr = precioUnitarioStr;
        return this;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Detalle withUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Detalle.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("cantidad");
        sb.append('=');
        sb.append(((this.cantidad == null)?"<null>":this.cantidad));
        sb.append(',');
        sb.append("importeTotal");
        sb.append('=');
        sb.append(((this.importeTotal == null)?"<null>":this.importeTotal));
        sb.append(',');
        sb.append("importeUnitario");
        sb.append('=');
        sb.append(((this.importeUnitario == null)?"<null>":this.importeUnitario));
        sb.append(',');
        sb.append("lineaDetalle");
        sb.append('=');
        sb.append(((this.lineaDetalle == null)?"<null>":this.lineaDetalle));
        sb.append(',');
        sb.append("precioUnitario");
        sb.append('=');
        sb.append(((this.precioUnitario == null)?"<null>":this.precioUnitario));
        sb.append(',');
        sb.append("catalogoCodigo");
        sb.append('=');
        sb.append(((this.catalogoCodigo == null)?"<null>":this.catalogoCodigo));
        sb.append(',');
        sb.append("descuento");
        sb.append('=');
        sb.append(((this.descuento == null)?"<null>":this.descuento));
        sb.append(',');
        sb.append("descuentoStr");
        sb.append('=');
        sb.append(((this.descuentoStr == null)?"<null>":this.descuentoStr));
        sb.append(',');
        sb.append("importeTotalStr");
        sb.append('=');
        sb.append(((this.importeTotalStr == null)?"<null>":this.importeTotalStr));
        sb.append(',');
        sb.append("importeUnitarioStr");
        sb.append('=');
        sb.append(((this.importeUnitarioStr == null)?"<null>":this.importeUnitarioStr));
        sb.append(',');
        sb.append("precioUnitarioStr");
        sb.append('=');
        sb.append(((this.precioUnitarioStr == null)?"<null>":this.precioUnitarioStr));
        sb.append(',');
        sb.append("unidadMedida");
        sb.append('=');
        sb.append(((this.unidadMedida == null)?"<null>":this.unidadMedida));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.precioUnitario == null)? 0 :this.precioUnitario.hashCode()));
        result = ((result* 31)+((this.unidadMedida == null)? 0 :this.unidadMedida.hashCode()));
        result = ((result* 31)+((this.descuento == null)? 0 :this.descuento.hashCode()));
        result = ((result* 31)+((this.precioUnitarioStr == null)? 0 :this.precioUnitarioStr.hashCode()));
        result = ((result* 31)+((this.catalogoCodigo == null)? 0 :this.catalogoCodigo.hashCode()));
        result = ((result* 31)+((this.importeUnitario == null)? 0 :this.importeUnitario.hashCode()));
        result = ((result* 31)+((this.importeTotal == null)? 0 :this.importeTotal.hashCode()));
        result = ((result* 31)+((this.descuentoStr == null)? 0 :this.descuentoStr.hashCode()));
        result = ((result* 31)+((this.importeUnitarioStr == null)? 0 :this.importeUnitarioStr.hashCode()));
        result = ((result* 31)+((this.importeTotalStr == null)? 0 :this.importeTotalStr.hashCode()));
        result = ((result* 31)+((this.cantidad == null)? 0 :this.cantidad.hashCode()));
        result = ((result* 31)+((this.lineaDetalle == null)? 0 :this.lineaDetalle.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Detalle) == false) {
            return false;
        }
        Detalle rhs = ((Detalle) other);
        return (((((((((((((this.precioUnitario == rhs.precioUnitario)||((this.precioUnitario!= null)&&this.precioUnitario.equals(rhs.precioUnitario)))&&((this.unidadMedida == rhs.unidadMedida)||((this.unidadMedida!= null)&&this.unidadMedida.equals(rhs.unidadMedida))))&&((this.descuento == rhs.descuento)||((this.descuento!= null)&&this.descuento.equals(rhs.descuento))))&&((this.precioUnitarioStr == rhs.precioUnitarioStr)||((this.precioUnitarioStr!= null)&&this.precioUnitarioStr.equals(rhs.precioUnitarioStr))))&&((this.catalogoCodigo == rhs.catalogoCodigo)||((this.catalogoCodigo!= null)&&this.catalogoCodigo.equals(rhs.catalogoCodigo))))&&((this.importeUnitario == rhs.importeUnitario)||((this.importeUnitario!= null)&&this.importeUnitario.equals(rhs.importeUnitario))))&&((this.importeTotal == rhs.importeTotal)||((this.importeTotal!= null)&&this.importeTotal.equals(rhs.importeTotal))))&&((this.descuentoStr == rhs.descuentoStr)||((this.descuentoStr!= null)&&this.descuentoStr.equals(rhs.descuentoStr))))&&((this.importeUnitarioStr == rhs.importeUnitarioStr)||((this.importeUnitarioStr!= null)&&this.importeUnitarioStr.equals(rhs.importeUnitarioStr))))&&((this.importeTotalStr == rhs.importeTotalStr)||((this.importeTotalStr!= null)&&this.importeTotalStr.equals(rhs.importeTotalStr))))&&((this.cantidad == rhs.cantidad)||((this.cantidad!= null)&&this.cantidad.equals(rhs.cantidad))))&&((this.lineaDetalle == rhs.lineaDetalle)||((this.lineaDetalle!= null)&&this.lineaDetalle.equals(rhs.lineaDetalle))));
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(cantidad);
        dest.writeValue(importeTotal);
        dest.writeValue(importeUnitario);
        dest.writeValue(lineaDetalle);
        dest.writeValue(precioUnitario);
        dest.writeValue(catalogoCodigo);
        dest.writeValue(descuento);
        dest.writeValue(descuentoStr);
        dest.writeValue(importeTotalStr);
        dest.writeValue(importeUnitarioStr);
        dest.writeValue(precioUnitarioStr);
        dest.writeValue(unidadMedida);
    }

    public int describeContents() {
        return 0;
    }
}
