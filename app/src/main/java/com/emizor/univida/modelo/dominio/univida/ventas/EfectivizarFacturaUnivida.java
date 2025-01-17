package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EfectivizarFacturaUnivida implements Serializable, Parcelable
{

    @SerializedName("correo_cliente")
    @Expose
    private String correoCliente;
    @SerializedName("nit_ci")
    @Expose
    private String nitCi;
    @SerializedName("factura_ci_complemento")
    @Expose
    private String ciComplemento;
    @SerializedName("factura_tipo_doc_identidad")
    @Expose
    private Integer tipoDocIdentidadFk;
    @SerializedName("prima")
    @Expose
    private Integer prima;
    @SerializedName("razon_social")
    @Expose
    private String razonSocial;
    @SerializedName("sucursal_fk")
    @Expose
    private Integer sucursalFk;
    @SerializedName("telefono_cliente")
    @Expose
    private String telefonoCliente;
    @SerializedName("prop_celular")
    @Expose
    private String propCelular;
    @SerializedName("prop_ci")
    @Expose
    private String propCi;
    @SerializedName("prop_nit")
    @Expose
    private String propNit;
    @SerializedName("prop_direccion")
    @Expose
    private String propDireccion;
    @SerializedName("prop_telefono")
    @Expose
    private String propTelefono;
    @SerializedName("prop_tomador")
    @Expose
    private String propTomador;
    @SerializedName("roseta_numero")
    @Expose
    private Long rosetaNumero;
    @SerializedName("departamento_plaza_circulacion_fk")
    @Expose
    private String departamentoPlazaCirculacionFk;
    @SerializedName("departamento_venta_fk")
    @Expose
    private String departamentoVentaFk;
    @SerializedName("gestion_fk")
    @Expose
    private Integer gestionFk;
    @SerializedName("vehiculo_tipo_fk")
    @Expose
    private Integer vehiculoTipoFk;
    @SerializedName("vehiculo_uso_fk")
    @Expose
    private Integer vehiculoUsoFk;
    @SerializedName("venta_datos_adicionales")
    @Expose
    private String ventaDatosAdicionales;
    @SerializedName("vehiculo_anio")
    @Expose
    private Integer vehiculoAnio;
    @SerializedName("vehiculo_chasis")
    @Expose
    private String vehiculoChasis;
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
    @SerializedName("vehiculo_placa_tipo")
    @Expose
    private Integer vehiculoPlacaTipo;
    @SerializedName("vehiculo_cilindrada")
    @Expose
    private Integer vehiculoCilindrada;
    @SerializedName("vehiculo_capacidad_carga")
    @Expose
    private Double vehiculoCapacidadCarga;
    public final static Parcelable.Creator<EfectivizarFacturaUnivida> CREATOR = new Creator<EfectivizarFacturaUnivida>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EfectivizarFacturaUnivida createFromParcel(Parcel in) {
            return new EfectivizarFacturaUnivida(in);
        }

        public EfectivizarFacturaUnivida[] newArray(int size) {
            return (new EfectivizarFacturaUnivida[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6609727108259924988L;

    protected EfectivizarFacturaUnivida(Parcel in) {
        this.correoCliente = ((String) in.readValue((String.class.getClassLoader())));
        this.nitCi = ((String) in.readValue((String.class.getClassLoader())));
        this.ciComplemento = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoDocIdentidadFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.prima = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.razonSocial = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.telefonoCliente = ((String) in.readValue((String.class.getClassLoader())));
        this.propCelular = ((String) in.readValue((String.class.getClassLoader())));
        this.propCi = ((String) in.readValue((String.class.getClassLoader())));
        this.propNit = ((String) in.readValue((String.class.getClassLoader())));
        this.propDireccion = ((String) in.readValue((String.class.getClassLoader())));
        this.propTelefono = ((String) in.readValue((String.class.getClassLoader())));
        this.propTomador = ((String) in.readValue((String.class.getClassLoader())));
        this.rosetaNumero = ((Long) in.readValue((Long.class.getClassLoader())));
        this.departamentoPlazaCirculacionFk = ((String) in.readValue((String.class.getClassLoader())));
        this.departamentoVentaFk = ((String) in.readValue((String.class.getClassLoader())));
        this.gestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoTipoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoUsoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ventaDatosAdicionales = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoAnio = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoChasis = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoColor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMarca = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoModelo = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMotor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoPlacaTipo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoCilindrada = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoCapacidadCarga = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public EfectivizarFacturaUnivida() {
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getNitCi() {
        return nitCi;
    }

    public void setNitCi(String nitCi) {
        this.nitCi = nitCi;
    }

    public String getCiComplemento() {
        return ciComplemento;
    }

    public void setCiComplemento(String ciComplemento) {
        this.ciComplemento = ciComplemento;
    }

    public Integer getTipoDocIdentidadFk() {
        return tipoDocIdentidadFk;
    }

    public void setTipoDocIdentidadFk(Integer tipoDocIdentidadFk) {
        this.tipoDocIdentidadFk = tipoDocIdentidadFk;
    }

    public Integer getPrima() {
        return prima;
    }

    public void setPrima(Integer prima) {
        this.prima = prima;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getSucursalFk() {
        return sucursalFk;
    }

    public void setSucursalFk(Integer sucursalFk) {
        this.sucursalFk = sucursalFk;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
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

    public String getPropNit() {
        return propNit;
    }

    public void setPropNit(String propNit) {
        this.propNit = propNit;
    }

    public String getPropDireccion() {
        return propDireccion;
    }

    public void setPropDireccion(String propDireccion) {
        this.propDireccion = propDireccion;
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

    public Long getRosetaNumero() {
        return rosetaNumero;
    }

    public void setRosetaNumero(Long rosetaNumero) {
        this.rosetaNumero = rosetaNumero;
    }

    public String getDepartamentoPlazaCirculacionFk() {
        return departamentoPlazaCirculacionFk;
    }

    public void setDepartamentoPlazaCirculacionFk(String departamentoPlazaCirculacionFk) {
        this.departamentoPlazaCirculacionFk = departamentoPlazaCirculacionFk;
    }

    public String getDepartamentoVentaFk() {
        return departamentoVentaFk;
    }

    public void setDepartamentoVentaFk(String departamentoVentaFk) {
        this.departamentoVentaFk = departamentoVentaFk;
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

    public String getVentaDatosAdicionales() {
        return ventaDatosAdicionales;
    }

    public void setVentaDatosAdicionales(String ventaDatosAdicionales) {
        this.ventaDatosAdicionales = ventaDatosAdicionales;
    }

    public Integer getVehiculoAnio() {
        return vehiculoAnio;
    }

    public void setVehiculoAnio(Integer vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
    }

    public String getVehiculoChasis() {
        return vehiculoChasis;
    }

    public void setVehiculoChasis(String vehiculoChasis) {
        this.vehiculoChasis = vehiculoChasis;
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

    public Integer getVehiculoPlacaTipo() {
        return vehiculoPlacaTipo;
    }

    public void setVehiculoPlacaTipo(Integer vehiculoPlacaTipo) {
        this.vehiculoPlacaTipo = vehiculoPlacaTipo;
    }

    public Integer getVehiculoCilindrada() {
        return vehiculoCilindrada;
    }

    public void setVehiculoCilindrada(Integer vehiculoCilindrada) {
        this.vehiculoCilindrada = vehiculoCilindrada;
    }

    public Double getVehiculoCapacidadCarga() {
        return vehiculoCapacidadCarga;
    }

    public void setVehiculoCapacidadCarga(Double vehiculoCapacidadCarga) {
        this.vehiculoCapacidadCarga = vehiculoCapacidadCarga;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vehiculoMotor).append(vehiculoCapacidadCarga).append(vehiculoCilindrada).append(vehiculoPlacaTipo).append(propTomador).append(vehiculoMarca).append(departamentoVentaFk).append(propDireccion).append(vehiculoTipoFk).append(gestionFk).append(vehiculoAnio).append(ventaDatosAdicionales).append(telefonoCliente).append(propCi).append(nitCi).append(ciComplemento).append(tipoDocIdentidadFk).append(vehiculoChasis).append(propCelular).append(correoCliente).append(departamentoPlazaCirculacionFk).append(rosetaNumero).append(razonSocial).append(vehiculoPlaca).append(vehiculoModelo).append(sucursalFk).append(propNit).append(vehiculoColor).append(prima).append(vehiculoUsoFk).append(propTelefono).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EfectivizarFacturaUnivida) == false) {
            return false;
        }
        EfectivizarFacturaUnivida rhs = ((EfectivizarFacturaUnivida) other);
        return new EqualsBuilder().append(vehiculoCilindrada, rhs.vehiculoCilindrada).append(vehiculoCapacidadCarga, rhs.vehiculoCapacidadCarga).append(vehiculoMotor, rhs.vehiculoMotor).append(vehiculoPlacaTipo, rhs.vehiculoPlacaTipo).append(propTomador, rhs.propTomador).append(vehiculoMarca, rhs.vehiculoMarca).append(departamentoVentaFk, rhs.departamentoVentaFk).append(propDireccion, rhs.propDireccion).append(vehiculoTipoFk, rhs.vehiculoTipoFk).append(gestionFk, rhs.gestionFk).append(vehiculoAnio, rhs.vehiculoAnio).append(ventaDatosAdicionales, rhs.ventaDatosAdicionales).append(telefonoCliente, rhs.telefonoCliente).append(propCi, rhs.propCi).append(nitCi, rhs.nitCi).append(ciComplemento, rhs.ciComplemento).append(tipoDocIdentidadFk, rhs.tipoDocIdentidadFk).append(vehiculoChasis, rhs.vehiculoChasis).append(propCelular, rhs.propCelular).append(correoCliente, rhs.correoCliente).append(departamentoPlazaCirculacionFk, rhs.departamentoPlazaCirculacionFk).append(rosetaNumero, rhs.rosetaNumero).append(razonSocial, rhs.razonSocial).append(vehiculoPlaca, rhs.vehiculoPlaca).append(vehiculoModelo, rhs.vehiculoModelo).append(sucursalFk, rhs.sucursalFk).append(propNit, rhs.propNit).append(vehiculoColor, rhs.vehiculoColor).append(prima, rhs.prima).append(vehiculoUsoFk, rhs.vehiculoUsoFk).append(propTelefono, rhs.propTelefono).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(correoCliente);
        dest.writeValue(nitCi);
        dest.writeValue(ciComplemento);
        dest.writeValue(tipoDocIdentidadFk);
        dest.writeValue(prima);
        dest.writeValue(razonSocial);
        dest.writeValue(sucursalFk);
        dest.writeValue(telefonoCliente);
        dest.writeValue(propCelular);
        dest.writeValue(propCi);
        dest.writeValue(propNit);
        dest.writeValue(propDireccion);
        dest.writeValue(propTelefono);
        dest.writeValue(propTomador);
        dest.writeValue(rosetaNumero);
        dest.writeValue(departamentoPlazaCirculacionFk);
        dest.writeValue(departamentoVentaFk);
        dest.writeValue(gestionFk);
        dest.writeValue(vehiculoTipoFk);
        dest.writeValue(vehiculoUsoFk);
        dest.writeValue(ventaDatosAdicionales);
        dest.writeValue(vehiculoAnio);
        dest.writeValue(vehiculoChasis);
        dest.writeValue(vehiculoColor);
        dest.writeValue(vehiculoMarca);
        dest.writeValue(vehiculoModelo);
        dest.writeValue(vehiculoMotor);
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(vehiculoPlacaTipo);
        dest.writeValue(vehiculoCilindrada);
        dest.writeValue(vehiculoCapacidadCarga);
    }

    public int describeContents() {
        return 0;
    }
}
