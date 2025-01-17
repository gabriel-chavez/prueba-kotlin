package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class VehiculoDatosInter implements Serializable, Parcelable {

    @SerializedName("prop_celular")
    @Expose
    private String propCelular;
    @SerializedName("prop_ci")
    @Expose
    private String propCi;
    @SerializedName("prop_direccion")
    @Expose
    private String propDireccion;
    @SerializedName("prop_nit")
    @Expose
    private String propNit;
    @SerializedName("prop_telefono")
    @Expose
    private String propTelefono;
    @SerializedName("prop_tomador")
    @Expose
    private String propTomador;
    @SerializedName("soat_roseta_numero")
    @Expose
    private String soatRosetaNumero;
    @SerializedName("soat_departamento_pc_fk")
    @Expose
    private String soatDepartamentoPcFk;
    @SerializedName("soat_departamento_vt_fk")
    @Expose
    private String soatDepartamentoVtFk;
    @SerializedName("soat_gestion_fk")
    @Expose
    private Integer soatGestionFk;
    @SerializedName("soat_vehiculo_tipo_fk")
    @Expose
    private Integer soatVehiculoTipoFk;
    @SerializedName("soat_vehiculo_uso_fk")
    @Expose
    private Integer soatVehiculoUsoFk;
    @SerializedName("vehiculo_acople")
    @Expose
    private String vehiculoAcople;
    @SerializedName("vehiculo_anio")
    @Expose
    private String vehiculoAnio;
    @SerializedName("vehiculo_capacidad_carga")
    @Expose
    private String vehiculoCapacidadCarga;
    @SerializedName("vahiculo_chasis")
    @Expose
    private String vahiculoChasis;
    @SerializedName("vehiculo_color")
    @Expose
    private String vehiculoColor;
    @SerializedName("vehiculo_marca")
    @Expose
    private String vehiculoMarca;
    @SerializedName("vehiculo_modelo")
    @Expose
    private String vehiculoModelo;
    @SerializedName("vehiculo_motor")
    @Expose
    private String vehiculoMotor;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("vehiculo_cilindrada")
    @Expose
    private String vehiculoCilindrada;
    public final static Parcelable.Creator<VehiculoDatosInter> CREATOR = new Creator<VehiculoDatosInter>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VehiculoDatosInter createFromParcel(Parcel in) {
            return new VehiculoDatosInter(in);
        }

        public VehiculoDatosInter[] newArray(int size) {
            return (new VehiculoDatosInter[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2652482652982559914L;

    protected VehiculoDatosInter(Parcel in) {
        this.propCelular = ((String) in.readValue((String.class.getClassLoader())));
        this.propCi = ((String) in.readValue((String.class.getClassLoader())));
        this.propDireccion = ((String) in.readValue((String.class.getClassLoader())));
        this.propNit = ((String) in.readValue((String.class.getClassLoader())));
        this.propTelefono = ((String) in.readValue((String.class.getClassLoader())));
        this.propTomador = ((String) in.readValue((String.class.getClassLoader())));
        this.soatRosetaNumero = ((String) in.readValue((String.class.getClassLoader())));
        this.soatDepartamentoPcFk = ((String) in.readValue((String.class.getClassLoader())));
        this.soatDepartamentoVtFk = ((String) in.readValue((String.class.getClassLoader())));
        this.soatGestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soatVehiculoTipoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soatVehiculoUsoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoAcople = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoAnio = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoCapacidadCarga = ((String) in.readValue((String.class.getClassLoader())));
        this.vahiculoChasis = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoColor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMarca = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoModelo = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMotor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoCilindrada = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VehiculoDatosInter() {
    }

    public String getPropCelular() {
        return propCelular;
    }

    public void setPropCelular(String propCelular) {
        this.propCelular = propCelular;
    }

    public String getPropCi() {
        return propCi;
    }

    public void setPropCi(String propCi) {
        this.propCi = propCi;
    }

    public String getPropDireccion() {
        return propDireccion;
    }

    public void setPropDireccion(String propDireccion) {
        this.propDireccion = propDireccion;
    }

    public String getPropNit() {
        return propNit;
    }

    public void setPropNit(String propNit) {
        this.propNit = propNit;
    }

    public String getPropTelefono() {
        return propTelefono;
    }

    public void setPropTelefono(String propTelefono) {
        this.propTelefono = propTelefono;
    }

    public String getPropTomador() {
        return propTomador;
    }

    public void setPropTomador(String propTomador) {
        this.propTomador = propTomador;
    }

    public String getSoatRosetaNumero() {
        return soatRosetaNumero;
    }

    public void setSoatRosetaNumero(String soatRosetaNumero) {
        this.soatRosetaNumero = soatRosetaNumero;
    }

    public String getSoatDepartamentoPcFk() {
        return soatDepartamentoPcFk;
    }

    public void setSoatDepartamentoPcFk(String soatDepartamentoPcFk) {
        this.soatDepartamentoPcFk = soatDepartamentoPcFk;
    }

    public String getSoatDepartamentoVtFk() {
        return soatDepartamentoVtFk;
    }

    public void setSoatDepartamentoVtFk(String soatDepartamentoVtFk) {
        this.soatDepartamentoVtFk = soatDepartamentoVtFk;
    }

    public Integer getSoatGestionFk() {
        return soatGestionFk;
    }

    public void setSoatGestionFk(Integer soatGestionFk) {
        this.soatGestionFk = soatGestionFk;
    }

    public Integer getSoatVehiculoTipoFk() {
        return soatVehiculoTipoFk;
    }

    public void setSoatVehiculoTipoFk(Integer soatVehiculoTipoFk) {
        this.soatVehiculoTipoFk = soatVehiculoTipoFk;
    }

    public Integer getSoatVehiculoUsoFk() {
        return soatVehiculoUsoFk;
    }

    public void setSoatVehiculoUsoFk(Integer soatVehiculoUsoFk) {
        this.soatVehiculoUsoFk = soatVehiculoUsoFk;
    }

    public String getVehiculoAcople() {
        return vehiculoAcople;
    }

    public void setVehiculoAcople(String vehiculoAcople) {
        this.vehiculoAcople = vehiculoAcople;
    }

    public String getVehiculoAnio() {
        return vehiculoAnio;
    }

    public void setVehiculoAnio(String vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
    }

    public String getVehiculoCapacidadCarga() {
        return vehiculoCapacidadCarga;
    }

    public void setVehiculoCapacidadCarga(String vehiculoCapacidadCarga) {
        this.vehiculoCapacidadCarga = vehiculoCapacidadCarga;
    }

    public String getVahiculoChasis() {
        return vahiculoChasis;
    }

    public void setVahiculoChasis(String vahiculoChasis) {
        this.vahiculoChasis = vahiculoChasis;
    }

    public String getVehiculoColor() {
        return vehiculoColor;
    }

    public void setVehiculoColor(String vehiculoColor) {
        this.vehiculoColor = vehiculoColor;
    }

    public String getVehiculoMarca() {
        return vehiculoMarca;
    }

    public void setVehiculoMarca(String vehiculoMarca) {
        this.vehiculoMarca = vehiculoMarca;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    public String getVehiculoMotor() {
        return vehiculoMotor;
    }

    public void setVehiculoMotor(String vehiculoMotor) {
        this.vehiculoMotor = vehiculoMotor;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getVehiculoCilindrada() {
        return vehiculoCilindrada;
    }

    public void setVehiculoCilindrada(String vehiculoCilindrada) {
        this.vehiculoCilindrada = vehiculoCilindrada;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vehiculoCilindrada", vehiculoCilindrada).append("propCelular", propCelular).append("propCi", propCi).append("propDireccion", propDireccion).append("propNit", propNit).append("propTelefono", propTelefono).append("propTomador", propTomador).append("soatRosetaNumero", soatRosetaNumero).append("soatDepartamentoPcFk", soatDepartamentoPcFk).append("soatDepartamentoVtFk", soatDepartamentoVtFk).append("soatGestionFk", soatGestionFk).append("soatVehiculoTipoFk", soatVehiculoTipoFk).append("soatVehiculoUsoFk", soatVehiculoUsoFk).append("vehiculoAcople", vehiculoAcople).append("vehiculoAnio", vehiculoAnio).append("vehiculoCapacidadCarga", vehiculoCapacidadCarga).append("vahiculoChasis", vahiculoChasis).append("vehiculoColor", vehiculoColor).append("vehiculoMarca", vehiculoMarca).append("vehiculoModelo", vehiculoModelo).append("vehiculoMotor", vehiculoMotor).append("vehiculoPlaca", vehiculoPlaca).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vehiculoCilindrada).append(vehiculoMotor).append(soatVehiculoUsoFk).append(propCelular).append(vehiculoAcople).append(soatDepartamentoPcFk).append(propTomador).append(vehiculoPlaca).append(vehiculoModelo).append(vehiculoMarca).append(soatGestionFk).append(propDireccion).append(vehiculoCapacidadCarga).append(vahiculoChasis).append(propNit).append(vehiculoColor).append(soatVehiculoTipoFk).append(vehiculoAnio).append(soatRosetaNumero).append(propCi).append(soatDepartamentoVtFk).append(propTelefono).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiculoDatosInter) == false) {
            return false;
        }
        VehiculoDatosInter rhs = ((VehiculoDatosInter) other);
        return new EqualsBuilder().append(vehiculoCilindrada, rhs.vehiculoCilindrada).append(vehiculoMotor, rhs.vehiculoMotor).append(soatVehiculoUsoFk, rhs.soatVehiculoUsoFk).append(propCelular, rhs.propCelular).append(vehiculoAcople, rhs.vehiculoAcople).append(soatDepartamentoPcFk, rhs.soatDepartamentoPcFk).append(propTomador, rhs.propTomador).append(vehiculoPlaca, rhs.vehiculoPlaca).append(vehiculoModelo, rhs.vehiculoModelo).append(vehiculoMarca, rhs.vehiculoMarca).append(soatGestionFk, rhs.soatGestionFk).append(propDireccion, rhs.propDireccion).append(vehiculoCapacidadCarga, rhs.vehiculoCapacidadCarga).append(vahiculoChasis, rhs.vahiculoChasis).append(propNit, rhs.propNit).append(vehiculoColor, rhs.vehiculoColor).append(soatVehiculoTipoFk, rhs.soatVehiculoTipoFk).append(vehiculoAnio, rhs.vehiculoAnio).append(soatRosetaNumero, rhs.soatRosetaNumero).append(propCi, rhs.propCi).append(soatDepartamentoVtFk, rhs.soatDepartamentoVtFk).append(propTelefono, rhs.propTelefono).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(propCelular);
        dest.writeValue(propCi);
        dest.writeValue(propDireccion);
        dest.writeValue(propNit);
        dest.writeValue(propTelefono);
        dest.writeValue(propTomador);
        dest.writeValue(soatRosetaNumero);
        dest.writeValue(soatDepartamentoPcFk);
        dest.writeValue(soatDepartamentoVtFk);
        dest.writeValue(soatGestionFk);
        dest.writeValue(soatVehiculoTipoFk);
        dest.writeValue(soatVehiculoUsoFk);
        dest.writeValue(vehiculoAcople);
        dest.writeValue(vehiculoAnio);
        dest.writeValue(vehiculoCapacidadCarga);
        dest.writeValue(vahiculoChasis);
        dest.writeValue(vehiculoColor);
        dest.writeValue(vehiculoMarca);
        dest.writeValue(vehiculoModelo);
        dest.writeValue(vehiculoMotor);
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(vehiculoCilindrada);
    }

    public int describeContents() {
        return 0;
    }
}
