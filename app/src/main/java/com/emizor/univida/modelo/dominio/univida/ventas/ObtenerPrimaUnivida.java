package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ObtenerPrimaUnivida implements Serializable, Parcelable
{

    @SerializedName("departamento_plaza_circulacion_fk")
    @Expose
    private String departamentoPlazaCirculacionFk;
    @SerializedName("vehiculo_tipo_fk")
    @Expose
    private Integer vehiculoTipoFk;
    @SerializedName("vehiculo_uso_fk")
    @Expose
    private Integer vehiculoUsoFk;
    @SerializedName("venta_cajero")
    @Expose
    private String ventaCajero;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("venta_canal_fk")
    @Expose
    private Integer ventaCanalFk;
    @SerializedName("venta_vendedor")
    @Expose
    private String ventaVendedor;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    public final static Parcelable.Creator<ObtenerPrimaUnivida> CREATOR = new Creator<ObtenerPrimaUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ObtenerPrimaUnivida createFromParcel(Parcel in) {
            return new ObtenerPrimaUnivida(in);
        }

        public ObtenerPrimaUnivida[] newArray(int size) {
            return (new ObtenerPrimaUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7511006149968307529L;

    protected ObtenerPrimaUnivida(Parcel in) {
        this.departamentoPlazaCirculacionFk = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoTipoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoUsoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaCajero = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaCanalFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaVendedor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ObtenerPrimaUnivida() {
    }

    public String getDepartamentoPlazaCirculacionFk() {
        return departamentoPlazaCirculacionFk;
    }

    public void setDepartamentoPlazaCirculacionFk(String departamentoPlazaCirculacionFk) {
        this.departamentoPlazaCirculacionFk = departamentoPlazaCirculacionFk;
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

    public String getVentaCajero() {
        return ventaCajero;
    }

    public void setVentaCajero(String ventaCajero) {
        this.ventaCajero = ventaCajero;
    }

    public Integer getGestionFk() {
        return gestionFk;
    }

    public void setGestionFk(Integer gestionFk) {
        this.gestionFk = gestionFk;
    }

    public Integer getVentaCanalFk() {
        return ventaCanalFk;
    }

    public void setVentaCanalFk(Integer ventaCanalFk) {
        this.ventaCanalFk = ventaCanalFk;
    }

    public String getVentaVendedor() {
        return ventaVendedor;
    }

    public void setVentaVendedor(String ventaVendedor) {
        this.ventaVendedor = ventaVendedor;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ventaCanalFk).append(departamentoPlazaCirculacionFk).append(vehiculoPlaca).append(ventaVendedor).append(ventaCajero).append(gestionFk).append(vehiculoTipoFk).append(vehiculoUsoFk).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ObtenerPrimaUnivida) == false) {
            return false;
        }
        ObtenerPrimaUnivida rhs = ((ObtenerPrimaUnivida) other);
        return new EqualsBuilder().append(ventaCanalFk, rhs.ventaCanalFk).append(departamentoPlazaCirculacionFk, rhs.departamentoPlazaCirculacionFk).append(vehiculoPlaca, rhs.vehiculoPlaca).append(ventaVendedor, rhs.ventaVendedor).append(ventaCajero, rhs.ventaCajero).append(gestionFk, rhs.gestionFk).append(vehiculoTipoFk, rhs.vehiculoTipoFk).append(vehiculoUsoFk, rhs.vehiculoUsoFk).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(departamentoPlazaCirculacionFk);
        dest.writeValue(vehiculoTipoFk);
        dest.writeValue(vehiculoUsoFk);
        dest.writeValue(ventaCajero);
        dest.writeValue(gestionFk);
        dest.writeValue(ventaCanalFk);
        dest.writeValue(ventaVendedor);
        dest.writeValue(vehiculoPlaca);
    }

    public int describeContents() {
        return 0;
    }
}
