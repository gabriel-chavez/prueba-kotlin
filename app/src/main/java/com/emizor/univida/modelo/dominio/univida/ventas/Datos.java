package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datos implements Serializable, Parcelable
{

    @SerializedName("factura_numero")
    @Expose
    private Integer facturaNumero;
    @SerializedName("prop_ci")
    @Expose
    private String propCi;
    @SerializedName("prop_celular")
    @Expose
    private String propCelular;
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
    @SerializedName("soat_fecha_cobertura_fin")
    @Expose
    private String soatFechaCoberturaFin;
    @SerializedName("soat_fecha_cobertura_inicio")
    @Expose
    private String soatFechaCoberturaInicio;
    @SerializedName("soat_fecha_venta")
    @Expose
    private String soatFechaVenta;
    @SerializedName("soat_mensaje")
    @Expose
    private String soatMensaje;
    @SerializedName("soat_numero_comprobante")
    @Expose
    private Integer soatNumeroComprobante;
    @SerializedName("soat_qr_contenido")
    @Expose
    private List<String> soatQrContenido = null;
    @SerializedName("soat_qr_imagen")
    @Expose
    private List<Object> soatQrImagen = null;
    @SerializedName("soat_roseta_numero")
    @Expose
    private Integer soatRosetaNumero;
    @SerializedName("vehiculo_acople")
    @Expose
    private String vehiculoAcople;
    @SerializedName("vehiculo_anio")
    @Expose
    private String vehiculoAnio;
    @SerializedName("vehiculo_capacidad_carga")
    @Expose
    private String vehiculoCapacidadCarga;
    @SerializedName("vehiculo_chasis")
    @Expose
    private String vehiculoChasis;
    @SerializedName("vehiculo_cilindrada")
    @Expose
    private String vehiculoCilindrada;
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
    @SerializedName("soat_departamento_pc_description")
    @Expose
    private String soatDepartamentoPcDescription;
    @SerializedName("soat_departamento_pc_fk")
    @Expose
    private String soatDepartamentoPcFk;
    @SerializedName("soat_departamento_vt_fk")
    @Expose
    private String soatDepartamentoVtFk;
    @SerializedName("soat_gestion_fk")
    @Expose
    private Integer soatGestionFk;
    @SerializedName("soat_vehiculo_tipo_descripcion")
    @Expose
    private String soatVehiculoTipoDescripcion;
    @SerializedName("soat_vehiculo_tipo_fk")
    @Expose
    private Integer soatVehiculoTipoFk;
    @SerializedName("soat_vehiculo_uso_description")
    @Expose
    private String soatVehiculoUsoDescription;
    @SerializedName("soat_vehiculo_uso_fk")
    @Expose
    private Integer soatVehiculoUsoFk;
    @SerializedName("vehiculo_placa")
    @Expose
    private String vehiculoPlaca;
    @SerializedName("factura_maestro")
    @Expose
    private FacturaMaestro facturaMaestro;
    public final static Parcelable.Creator<Datos> CREATOR = new Creator<Datos>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Datos createFromParcel(Parcel in) {
            return new Datos(in);
        }

        public Datos[] newArray(int size) {
            return (new Datos[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1900626021346287980L;

    protected Datos(Parcel in) {
        this.facturaNumero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.propCi = ((String) in.readValue((String.class.getClassLoader())));
        this.propCelular = ((String) in.readValue((String.class.getClassLoader())));
        this.propDireccion = ((String) in.readValue((String.class.getClassLoader())));
        this.propNit = ((String) in.readValue((String.class.getClassLoader())));
        this.propTelefono = ((String) in.readValue((String.class.getClassLoader())));
        this.propTomador = ((String) in.readValue((String.class.getClassLoader())));
        this.soatFechaCoberturaFin = ((String) in.readValue((String.class.getClassLoader())));
        this.soatFechaCoberturaInicio = ((String) in.readValue((String.class.getClassLoader())));
        this.soatFechaVenta = ((String) in.readValue((String.class.getClassLoader())));
        this.soatMensaje = ((String) in.readValue((String.class.getClassLoader())));
        this.soatNumeroComprobante = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.soatQrContenido, (java.lang.String.class.getClassLoader()));
        in.readList(this.soatQrImagen, (java.lang.Object.class.getClassLoader()));
        this.soatRosetaNumero = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoAcople = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoAnio = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoCapacidadCarga = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoChasis = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoCilindrada = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoColor = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMarca = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoModelo = ((String) in.readValue((String.class.getClassLoader())));
        this.vehiculoMotor = ((String) in.readValue((String.class.getClassLoader())));
        this.soatDepartamentoPcDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.soatDepartamentoPcFk = ((String) in.readValue((String.class.getClassLoader())));
        this.soatDepartamentoVtFk = ((String) in.readValue((String.class.getClassLoader())));
        this.soatGestionFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soatVehiculoTipoDescripcion = ((String) in.readValue((String.class.getClassLoader())));
        this.soatVehiculoTipoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soatVehiculoUsoDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.soatVehiculoUsoFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vehiculoPlaca = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaMaestro = ((FacturaMaestro) in.readValue((FacturaMaestro.class.getClassLoader())));
    }

    public Datos() {
    }

    public Integer getFacturaNumero() {
        return facturaNumero;
    }

    public void setFacturaNumero(Integer facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    public Datos withFacturaNumero(Integer facturaNumero) {
        this.facturaNumero = facturaNumero;
        return this;
    }

    public String getPropCi() {
        return propCi;
    }

    public void setPropCi(String propCi) {
        this.propCi = propCi;
    }

    public Datos withPropCi(String propCi) {
        this.propCi = propCi;
        return this;
    }

    public String getPropCelular() {
        return propCelular;
    }

    public void setPropCelular(String propCelular) {
        this.propCelular = propCelular;
    }

    public Datos withPropCelular(String propCelular) {
        this.propCelular = propCelular;
        return this;
    }

    public String getPropDireccion() {
        return propDireccion;
    }

    public void setPropDireccion(String propDireccion) {
        this.propDireccion = propDireccion;
    }

    public Datos withPropDireccion(String propDireccion) {
        this.propDireccion = propDireccion;
        return this;
    }

    public String getPropNit() {
        return propNit;
    }

    public void setPropNit(String propNit) {
        this.propNit = propNit;
    }

    public Datos withPropNit(String propNit) {
        this.propNit = propNit;
        return this;
    }

    public String getPropTelefono() {
        return propTelefono;
    }

    public void setPropTelefono(String propTelefono) {
        this.propTelefono = propTelefono;
    }

    public Datos withPropTelefono(String propTelefono) {
        this.propTelefono = propTelefono;
        return this;
    }

    public String getPropTomador() {
        return propTomador;
    }

    public void setPropTomador(String propTomador) {
        this.propTomador = propTomador;
    }

    public Datos withPropTomador(String propTomador) {
        this.propTomador = propTomador;
        return this;
    }

    public String getSoatFechaCoberturaFin() {
        return soatFechaCoberturaFin;
    }

    public void setSoatFechaCoberturaFin(String soatFechaCoberturaFin) {
        this.soatFechaCoberturaFin = soatFechaCoberturaFin;
    }

    public Datos withSoatFechaCoberturaFin(String soatFechaCoberturaFin) {
        this.soatFechaCoberturaFin = soatFechaCoberturaFin;
        return this;
    }

    public String getSoatFechaCoberturaInicio() {
        return soatFechaCoberturaInicio;
    }

    public void setSoatFechaCoberturaInicio(String soatFechaCoberturaInicio) {
        this.soatFechaCoberturaInicio = soatFechaCoberturaInicio;
    }

    public Datos withSoatFechaCoberturaInicio(String soatFechaCoberturaInicio) {
        this.soatFechaCoberturaInicio = soatFechaCoberturaInicio;
        return this;
    }

    public String getSoatFechaVenta() {
        return soatFechaVenta;
    }

    public void setSoatFechaVenta(String soatFechaVenta) {
        this.soatFechaVenta = soatFechaVenta;
    }

    public Datos withSoatFechaVenta(String soatFechaVenta) {
        this.soatFechaVenta = soatFechaVenta;
        return this;
    }

    public String getSoatMensaje() {
        return soatMensaje;
    }

    public void setSoatMensaje(String soatMensaje) {
        this.soatMensaje = soatMensaje;
    }

    public Datos withSoatMensaje(String soatMensaje) {
        this.soatMensaje = soatMensaje;
        return this;
    }

    public Integer getSoatNumeroComprobante() {
        return soatNumeroComprobante;
    }

    public void setSoatNumeroComprobante(Integer soatNumeroComprobante) {
        this.soatNumeroComprobante = soatNumeroComprobante;
    }

    public Datos withSoatNumeroComprobante(Integer soatNumeroComprobante) {
        this.soatNumeroComprobante = soatNumeroComprobante;
        return this;
    }

    public List<String> getSoatQrContenido() {
        return soatQrContenido;
    }

    public void setSoatQrContenido(List<String> soatQrContenido) {
        this.soatQrContenido = soatQrContenido;
    }

    public Datos withSoatQrContenido(List<String> soatQrContenido) {
        this.soatQrContenido = soatQrContenido;
        return this;
    }

    public List<Object> getSoatQrImagen() {
        return soatQrImagen;
    }

    public void setSoatQrImagen(List<Object> soatQrImagen) {
        this.soatQrImagen = soatQrImagen;
    }

    public Datos withSoatQrImagen(List<Object> soatQrImagen) {
        this.soatQrImagen = soatQrImagen;
        return this;
    }

    public Integer getSoatRosetaNumero() {
        return soatRosetaNumero;
    }

    public void setSoatRosetaNumero(Integer soatRosetaNumero) {
        this.soatRosetaNumero = soatRosetaNumero;
    }

    public Datos withSoatRosetaNumero(Integer soatRosetaNumero) {
        this.soatRosetaNumero = soatRosetaNumero;
        return this;
    }

    public String getVehiculoAcople() {
        return vehiculoAcople;
    }

    public void setVehiculoAcople(String vehiculoAcople) {
        this.vehiculoAcople = vehiculoAcople;
    }

    public Datos withVehiculoAcople(String vehiculoAcople) {
        this.vehiculoAcople = vehiculoAcople;
        return this;
    }

    public String getVehiculoAnio() {
        return vehiculoAnio;
    }

    public void setVehiculoAnio(String vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
    }

    public Datos withVehiculoAnio(String vehiculoAnio) {
        this.vehiculoAnio = vehiculoAnio;
        return this;
    }

    public String getVehiculoCapacidadCarga() {
        return vehiculoCapacidadCarga;
    }

    public void setVehiculoCapacidadCarga(String vehiculoCapacidadCarga) {
        this.vehiculoCapacidadCarga = vehiculoCapacidadCarga;
    }

    public Datos withVehiculoCapacidadCarga(String vehiculoCapacidadCarga) {
        this.vehiculoCapacidadCarga = vehiculoCapacidadCarga;
        return this;
    }

    public String getVehiculoChasis() {
        return vehiculoChasis;
    }

    public void setVehiculoChasis(String vehiculoChasis) {
        this.vehiculoChasis = vehiculoChasis;
    }

    public Datos withVehiculoChasis(String vehiculoChasis) {
        this.vehiculoChasis = vehiculoChasis;
        return this;
    }

    public String getVehiculoCilindrada() {
        return vehiculoCilindrada;
    }

    public void setVehiculoCilindrada(String vehiculoCilindrada) {
        this.vehiculoCilindrada = vehiculoCilindrada;
    }

    public Datos withVehiculoCilindrada(String vehiculoCilindrada) {
        this.vehiculoCilindrada = vehiculoCilindrada;
        return this;
    }

    public String getVehiculoColor() {
        return vehiculoColor;
    }

    public void setVehiculoColor(String vehiculoColor) {
        this.vehiculoColor = vehiculoColor;
    }

    public Datos withVehiculoColor(String vehiculoColor) {
        this.vehiculoColor = vehiculoColor;
        return this;
    }

    public String getVehiculoMarca() {
        return vehiculoMarca;
    }

    public void setVehiculoMarca(String vehiculoMarca) {
        this.vehiculoMarca = vehiculoMarca;
    }

    public Datos withVehiculoMarca(String vehiculoMarca) {
        this.vehiculoMarca = vehiculoMarca;
        return this;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    public Datos withVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
        return this;
    }

    public String getVehiculoMotor() {
        return vehiculoMotor;
    }

    public void setVehiculoMotor(String vehiculoMotor) {
        this.vehiculoMotor = vehiculoMotor;
    }

    public Datos withVehiculoMotor(String vehiculoMotor) {
        this.vehiculoMotor = vehiculoMotor;
        return this;
    }

    public String getSoatDepartamentoPcDescription() {
        return soatDepartamentoPcDescription;
    }

    public void setSoatDepartamentoPcDescription(String soatDepartamentoPcDescription) {
        this.soatDepartamentoPcDescription = soatDepartamentoPcDescription;
    }

    public Datos withSoatDepartamentoPcDescription(String soatDepartamentoPcDescription) {
        this.soatDepartamentoPcDescription = soatDepartamentoPcDescription;
        return this;
    }

    public String getSoatDepartamentoPcFk() {
        return soatDepartamentoPcFk;
    }

    public void setSoatDepartamentoPcFk(String soatDepartamentoPcFk) {
        this.soatDepartamentoPcFk = soatDepartamentoPcFk;
    }

    public Datos withSoatDepartamentoPcFk(String soatDepartamentoPcFk) {
        this.soatDepartamentoPcFk = soatDepartamentoPcFk;
        return this;
    }

    public String getSoatDepartamentoVtFk() {
        return soatDepartamentoVtFk;
    }

    public void setSoatDepartamentoVtFk(String soatDepartamentoVtFk) {
        this.soatDepartamentoVtFk = soatDepartamentoVtFk;
    }

    public Datos withSoatDepartamentoVtFk(String soatDepartamentoVtFk) {
        this.soatDepartamentoVtFk = soatDepartamentoVtFk;
        return this;
    }

    public Integer getSoatGestionFk() {
        return soatGestionFk;
    }

    public void setSoatGestionFk(Integer soatGestionFk) {
        this.soatGestionFk = soatGestionFk;
    }

    public Datos withSoatGestionFk(Integer soatGestionFk) {
        this.soatGestionFk = soatGestionFk;
        return this;
    }

    public String getSoatVehiculoTipoDescripcion() {
        return soatVehiculoTipoDescripcion;
    }

    public void setSoatVehiculoTipoDescripcion(String soatVehiculoTipoDescripcion) {
        this.soatVehiculoTipoDescripcion = soatVehiculoTipoDescripcion;
    }

    public Datos withSoatVehiculoTipoDescripcion(String soatVehiculoTipoDescripcion) {
        this.soatVehiculoTipoDescripcion = soatVehiculoTipoDescripcion;
        return this;
    }

    public Integer getSoatVehiculoTipoFk() {
        return soatVehiculoTipoFk;
    }

    public void setSoatVehiculoTipoFk(Integer soatVehiculoTipoFk) {
        this.soatVehiculoTipoFk = soatVehiculoTipoFk;
    }

    public Datos withSoatVehiculoTipoFk(Integer soatVehiculoTipoFk) {
        this.soatVehiculoTipoFk = soatVehiculoTipoFk;
        return this;
    }

    public String getSoatVehiculoUsoDescription() {
        return soatVehiculoUsoDescription;
    }

    public void setSoatVehiculoUsoDescription(String soatVehiculoUsoDescription) {
        this.soatVehiculoUsoDescription = soatVehiculoUsoDescription;
    }

    public Datos withSoatVehiculoUsoDescription(String soatVehiculoUsoDescription) {
        this.soatVehiculoUsoDescription = soatVehiculoUsoDescription;
        return this;
    }

    public Integer getSoatVehiculoUsoFk() {
        return soatVehiculoUsoFk;
    }

    public void setSoatVehiculoUsoFk(Integer soatVehiculoUsoFk) {
        this.soatVehiculoUsoFk = soatVehiculoUsoFk;
    }

    public Datos withSoatVehiculoUsoFk(Integer soatVehiculoUsoFk) {
        this.soatVehiculoUsoFk = soatVehiculoUsoFk;
        return this;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public Datos withVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
        return this;
    }

    public FacturaMaestro getFacturaMaestro() {
        return facturaMaestro;
    }

    public void setFacturaMaestro(FacturaMaestro facturaMaestro) {
        this.facturaMaestro = facturaMaestro;
    }

    public Datos withFacturaMaestro(FacturaMaestro facturaMaestro) {
        this.facturaMaestro = facturaMaestro;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Datos.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("facturaNumero");
        sb.append('=');
        sb.append(((this.facturaNumero == null)?"<null>":this.facturaNumero));
        sb.append(',');
        sb.append("propCi");
        sb.append('=');
        sb.append(((this.propCi == null)?"<null>":this.propCi));
        sb.append(',');
        sb.append("propCelular");
        sb.append('=');
        sb.append(((this.propCelular == null)?"<null>":this.propCelular));
        sb.append(',');
        sb.append("propDireccion");
        sb.append('=');
        sb.append(((this.propDireccion == null)?"<null>":this.propDireccion));
        sb.append(',');
        sb.append("propNit");
        sb.append('=');
        sb.append(((this.propNit == null)?"<null>":this.propNit));
        sb.append(',');
        sb.append("propTelefono");
        sb.append('=');
        sb.append(((this.propTelefono == null)?"<null>":this.propTelefono));
        sb.append(',');
        sb.append("propTomador");
        sb.append('=');
        sb.append(((this.propTomador == null)?"<null>":this.propTomador));
        sb.append(',');
        sb.append("soatFechaCoberturaFin");
        sb.append('=');
        sb.append(((this.soatFechaCoberturaFin == null)?"<null>":this.soatFechaCoberturaFin));
        sb.append(',');
        sb.append("soatFechaCoberturaInicio");
        sb.append('=');
        sb.append(((this.soatFechaCoberturaInicio == null)?"<null>":this.soatFechaCoberturaInicio));
        sb.append(',');
        sb.append("soatFechaVenta");
        sb.append('=');
        sb.append(((this.soatFechaVenta == null)?"<null>":this.soatFechaVenta));
        sb.append(',');
        sb.append("soatMensaje");
        sb.append('=');
        sb.append(((this.soatMensaje == null)?"<null>":this.soatMensaje));
        sb.append(',');
        sb.append("soatNumeroComprobante");
        sb.append('=');
        sb.append(((this.soatNumeroComprobante == null)?"<null>":this.soatNumeroComprobante));
        sb.append(',');
        sb.append("soatQrContenido");
        sb.append('=');
        sb.append(((this.soatQrContenido == null)?"<null>":this.soatQrContenido));
        sb.append(',');
        sb.append("soatQrImagen");
        sb.append('=');
        sb.append(((this.soatQrImagen == null)?"<null>":this.soatQrImagen));
        sb.append(',');
        sb.append("soatRosetaNumero");
        sb.append('=');
        sb.append(((this.soatRosetaNumero == null)?"<null>":this.soatRosetaNumero));
        sb.append(',');
        sb.append("vehiculoAcople");
        sb.append('=');
        sb.append(((this.vehiculoAcople == null)?"<null>":this.vehiculoAcople));
        sb.append(',');
        sb.append("vehiculoAnio");
        sb.append('=');
        sb.append(((this.vehiculoAnio == null)?"<null>":this.vehiculoAnio));
        sb.append(',');
        sb.append("vehiculoCapacidadCarga");
        sb.append('=');
        sb.append(((this.vehiculoCapacidadCarga == null)?"<null>":this.vehiculoCapacidadCarga));
        sb.append(',');
        sb.append("vehiculoChasis");
        sb.append('=');
        sb.append(((this.vehiculoChasis == null)?"<null>":this.vehiculoChasis));
        sb.append(',');
        sb.append("vehiculoCilindrada");
        sb.append('=');
        sb.append(((this.vehiculoCilindrada == null)?"<null>":this.vehiculoCilindrada));
        sb.append(',');
        sb.append("vehiculoColor");
        sb.append('=');
        sb.append(((this.vehiculoColor == null)?"<null>":this.vehiculoColor));
        sb.append(',');
        sb.append("vehiculoMarca");
        sb.append('=');
        sb.append(((this.vehiculoMarca == null)?"<null>":this.vehiculoMarca));
        sb.append(',');
        sb.append("vehiculoModelo");
        sb.append('=');
        sb.append(((this.vehiculoModelo == null)?"<null>":this.vehiculoModelo));
        sb.append(',');
        sb.append("vehiculoMotor");
        sb.append('=');
        sb.append(((this.vehiculoMotor == null)?"<null>":this.vehiculoMotor));
        sb.append(',');
        sb.append("soatDepartamentoPcDescription");
        sb.append('=');
        sb.append(((this.soatDepartamentoPcDescription == null)?"<null>":this.soatDepartamentoPcDescription));
        sb.append(',');
        sb.append("soatDepartamentoPcFk");
        sb.append('=');
        sb.append(((this.soatDepartamentoPcFk == null)?"<null>":this.soatDepartamentoPcFk));
        sb.append(',');
        sb.append("soatDepartamentoVtFk");
        sb.append('=');
        sb.append(((this.soatDepartamentoVtFk == null)?"<null>":this.soatDepartamentoVtFk));
        sb.append(',');
        sb.append("soatGestionFk");
        sb.append('=');
        sb.append(((this.soatGestionFk == null)?"<null>":this.soatGestionFk));
        sb.append(',');
        sb.append("soatVehiculoTipoDescripcion");
        sb.append('=');
        sb.append(((this.soatVehiculoTipoDescripcion == null)?"<null>":this.soatVehiculoTipoDescripcion));
        sb.append(',');
        sb.append("soatVehiculoTipoFk");
        sb.append('=');
        sb.append(((this.soatVehiculoTipoFk == null)?"<null>":this.soatVehiculoTipoFk));
        sb.append(',');
        sb.append("soatVehiculoUsoDescription");
        sb.append('=');
        sb.append(((this.soatVehiculoUsoDescription == null)?"<null>":this.soatVehiculoUsoDescription));
        sb.append(',');
        sb.append("soatVehiculoUsoFk");
        sb.append('=');
        sb.append(((this.soatVehiculoUsoFk == null)?"<null>":this.soatVehiculoUsoFk));
        sb.append(',');
        sb.append("vehiculoPlaca");
        sb.append('=');
        sb.append(((this.vehiculoPlaca == null)?"<null>":this.vehiculoPlaca));
        sb.append(',');
        sb.append("facturaMaestro");
        sb.append('=');
        sb.append(((this.facturaMaestro == null)?"<null>":this.facturaMaestro));
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
        result = ((result* 31)+((this.vehiculoAnio == null)? 0 :this.vehiculoAnio.hashCode()));
        result = ((result* 31)+((this.soatGestionFk == null)? 0 :this.soatGestionFk.hashCode()));
        result = ((result* 31)+((this.soatRosetaNumero == null)? 0 :this.soatRosetaNumero.hashCode()));
        result = ((result* 31)+((this.soatNumeroComprobante == null)? 0 :this.soatNumeroComprobante.hashCode()));
        result = ((result* 31)+((this.soatDepartamentoVtFk == null)? 0 :this.soatDepartamentoVtFk.hashCode()));
        result = ((result* 31)+((this.propDireccion == null)? 0 :this.propDireccion.hashCode()));
        result = ((result* 31)+((this.vehiculoMarca == null)? 0 :this.vehiculoMarca.hashCode()));
        result = ((result* 31)+((this.vehiculoMotor == null)? 0 :this.vehiculoMotor.hashCode()));
        result = ((result* 31)+((this.vehiculoPlaca == null)? 0 :this.vehiculoPlaca.hashCode()));
        result = ((result* 31)+((this.soatQrContenido == null)? 0 :this.soatQrContenido.hashCode()));
        result = ((result* 31)+((this.soatDepartamentoPcFk == null)? 0 :this.soatDepartamentoPcFk.hashCode()));
        result = ((result* 31)+((this.propCelular == null)? 0 :this.propCelular.hashCode()));
        result = ((result* 31)+((this.soatFechaCoberturaFin == null)? 0 :this.soatFechaCoberturaFin.hashCode()));
        result = ((result* 31)+((this.soatVehiculoTipoDescripcion == null)? 0 :this.soatVehiculoTipoDescripcion.hashCode()));
        result = ((result* 31)+((this.propTomador == null)? 0 :this.propTomador.hashCode()));
        result = ((result* 31)+((this.soatVehiculoUsoFk == null)? 0 :this.soatVehiculoUsoFk.hashCode()));
        result = ((result* 31)+((this.vehiculoCilindrada == null)? 0 :this.vehiculoCilindrada.hashCode()));
        result = ((result* 31)+((this.soatFechaCoberturaInicio == null)? 0 :this.soatFechaCoberturaInicio.hashCode()));
        result = ((result* 31)+((this.propNit == null)? 0 :this.propNit.hashCode()));
        result = ((result* 31)+((this.soatDepartamentoPcDescription == null)? 0 :this.soatDepartamentoPcDescription.hashCode()));
        result = ((result* 31)+((this.vehiculoChasis == null)? 0 :this.vehiculoChasis.hashCode()));
        result = ((result* 31)+((this.facturaMaestro == null)? 0 :this.facturaMaestro.hashCode()));
        result = ((result* 31)+((this.propTelefono == null)? 0 :this.propTelefono.hashCode()));
        result = ((result* 31)+((this.vehiculoColor == null)? 0 :this.vehiculoColor.hashCode()));
        result = ((result* 31)+((this.soatVehiculoTipoFk == null)? 0 :this.soatVehiculoTipoFk.hashCode()));
        result = ((result* 31)+((this.propCi == null)? 0 :this.propCi.hashCode()));
        result = ((result* 31)+((this.soatQrImagen == null)? 0 :this.soatQrImagen.hashCode()));
        result = ((result* 31)+((this.vehiculoCapacidadCarga == null)? 0 :this.vehiculoCapacidadCarga.hashCode()));
        result = ((result* 31)+((this.vehiculoAcople == null)? 0 :this.vehiculoAcople.hashCode()));
        result = ((result* 31)+((this.facturaNumero == null)? 0 :this.facturaNumero.hashCode()));
        result = ((result* 31)+((this.soatMensaje == null)? 0 :this.soatMensaje.hashCode()));
        result = ((result* 31)+((this.soatVehiculoUsoDescription == null)? 0 :this.soatVehiculoUsoDescription.hashCode()));
        result = ((result* 31)+((this.soatFechaVenta == null)? 0 :this.soatFechaVenta.hashCode()));
        result = ((result* 31)+((this.vehiculoModelo == null)? 0 :this.vehiculoModelo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Datos) == false) {
            return false;
        }
        Datos rhs = ((Datos) other);
        return (((((((((((((((((((((((((((((((((((this.vehiculoAnio == rhs.vehiculoAnio)||((this.vehiculoAnio!= null)&&this.vehiculoAnio.equals(rhs.vehiculoAnio)))&&((this.soatGestionFk == rhs.soatGestionFk)||((this.soatGestionFk!= null)&&this.soatGestionFk.equals(rhs.soatGestionFk))))&&((this.soatRosetaNumero == rhs.soatRosetaNumero)||((this.soatRosetaNumero!= null)&&this.soatRosetaNumero.equals(rhs.soatRosetaNumero))))&&((this.soatNumeroComprobante == rhs.soatNumeroComprobante)||((this.soatNumeroComprobante!= null)&&this.soatNumeroComprobante.equals(rhs.soatNumeroComprobante))))&&((this.soatDepartamentoVtFk == rhs.soatDepartamentoVtFk)||((this.soatDepartamentoVtFk!= null)&&this.soatDepartamentoVtFk.equals(rhs.soatDepartamentoVtFk))))&&((this.propDireccion == rhs.propDireccion)||((this.propDireccion!= null)&&this.propDireccion.equals(rhs.propDireccion))))&&((this.vehiculoMarca == rhs.vehiculoMarca)||((this.vehiculoMarca!= null)&&this.vehiculoMarca.equals(rhs.vehiculoMarca))))&&((this.vehiculoMotor == rhs.vehiculoMotor)||((this.vehiculoMotor!= null)&&this.vehiculoMotor.equals(rhs.vehiculoMotor))))&&((this.vehiculoPlaca == rhs.vehiculoPlaca)||((this.vehiculoPlaca!= null)&&this.vehiculoPlaca.equals(rhs.vehiculoPlaca))))&&((this.soatQrContenido == rhs.soatQrContenido)||((this.soatQrContenido!= null)&&this.soatQrContenido.equals(rhs.soatQrContenido))))&&((this.soatDepartamentoPcFk == rhs.soatDepartamentoPcFk)||((this.soatDepartamentoPcFk!= null)&&this.soatDepartamentoPcFk.equals(rhs.soatDepartamentoPcFk))))&&((this.propCelular == rhs.propCelular)||((this.propCelular!= null)&&this.propCelular.equals(rhs.propCelular))))&&((this.soatFechaCoberturaFin == rhs.soatFechaCoberturaFin)||((this.soatFechaCoberturaFin!= null)&&this.soatFechaCoberturaFin.equals(rhs.soatFechaCoberturaFin))))&&((this.soatVehiculoTipoDescripcion == rhs.soatVehiculoTipoDescripcion)||((this.soatVehiculoTipoDescripcion!= null)&&this.soatVehiculoTipoDescripcion.equals(rhs.soatVehiculoTipoDescripcion))))&&((this.propTomador == rhs.propTomador)||((this.propTomador!= null)&&this.propTomador.equals(rhs.propTomador))))&&((this.soatVehiculoUsoFk == rhs.soatVehiculoUsoFk)||((this.soatVehiculoUsoFk!= null)&&this.soatVehiculoUsoFk.equals(rhs.soatVehiculoUsoFk))))&&((this.vehiculoCilindrada == rhs.vehiculoCilindrada)||((this.vehiculoCilindrada!= null)&&this.vehiculoCilindrada.equals(rhs.vehiculoCilindrada))))&&((this.soatFechaCoberturaInicio == rhs.soatFechaCoberturaInicio)||((this.soatFechaCoberturaInicio!= null)&&this.soatFechaCoberturaInicio.equals(rhs.soatFechaCoberturaInicio))))&&((this.propNit == rhs.propNit)||((this.propNit!= null)&&this.propNit.equals(rhs.propNit))))&&((this.soatDepartamentoPcDescription == rhs.soatDepartamentoPcDescription)||((this.soatDepartamentoPcDescription!= null)&&this.soatDepartamentoPcDescription.equals(rhs.soatDepartamentoPcDescription))))&&((this.vehiculoChasis == rhs.vehiculoChasis)||((this.vehiculoChasis!= null)&&this.vehiculoChasis.equals(rhs.vehiculoChasis))))&&((this.facturaMaestro == rhs.facturaMaestro)||((this.facturaMaestro!= null)&&this.facturaMaestro.equals(rhs.facturaMaestro))))&&((this.propTelefono == rhs.propTelefono)||((this.propTelefono!= null)&&this.propTelefono.equals(rhs.propTelefono))))&&((this.vehiculoColor == rhs.vehiculoColor)||((this.vehiculoColor!= null)&&this.vehiculoColor.equals(rhs.vehiculoColor))))&&((this.soatVehiculoTipoFk == rhs.soatVehiculoTipoFk)||((this.soatVehiculoTipoFk!= null)&&this.soatVehiculoTipoFk.equals(rhs.soatVehiculoTipoFk))))&&((this.propCi == rhs.propCi)||((this.propCi!= null)&&this.propCi.equals(rhs.propCi))))&&((this.soatQrImagen == rhs.soatQrImagen)||((this.soatQrImagen!= null)&&this.soatQrImagen.equals(rhs.soatQrImagen))))&&((this.vehiculoCapacidadCarga == rhs.vehiculoCapacidadCarga)||((this.vehiculoCapacidadCarga!= null)&&this.vehiculoCapacidadCarga.equals(rhs.vehiculoCapacidadCarga))))&&((this.vehiculoAcople == rhs.vehiculoAcople)||((this.vehiculoAcople!= null)&&this.vehiculoAcople.equals(rhs.vehiculoAcople))))&&((this.facturaNumero == rhs.facturaNumero)||((this.facturaNumero!= null)&&this.facturaNumero.equals(rhs.facturaNumero))))&&((this.soatMensaje == rhs.soatMensaje)||((this.soatMensaje!= null)&&this.soatMensaje.equals(rhs.soatMensaje))))&&((this.soatVehiculoUsoDescription == rhs.soatVehiculoUsoDescription)||((this.soatVehiculoUsoDescription!= null)&&this.soatVehiculoUsoDescription.equals(rhs.soatVehiculoUsoDescription))))&&((this.soatFechaVenta == rhs.soatFechaVenta)||((this.soatFechaVenta!= null)&&this.soatFechaVenta.equals(rhs.soatFechaVenta))))&&((this.vehiculoModelo == rhs.vehiculoModelo)||((this.vehiculoModelo!= null)&&this.vehiculoModelo.equals(rhs.vehiculoModelo))));
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(facturaNumero);
        dest.writeValue(propCi);
        dest.writeValue(propCelular);
        dest.writeValue(propDireccion);
        dest.writeValue(propNit);
        dest.writeValue(propTelefono);
        dest.writeValue(propTomador);
        dest.writeValue(soatFechaCoberturaFin);
        dest.writeValue(soatFechaCoberturaInicio);
        dest.writeValue(soatFechaVenta);
        dest.writeValue(soatMensaje);
        dest.writeValue(soatNumeroComprobante);
        dest.writeList(soatQrContenido);
        dest.writeList(soatQrImagen);
        dest.writeValue(soatRosetaNumero);
        dest.writeValue(vehiculoAcople);
        dest.writeValue(vehiculoAnio);
        dest.writeValue(vehiculoCapacidadCarga);
        dest.writeValue(vehiculoChasis);
        dest.writeValue(vehiculoCilindrada);
        dest.writeValue(vehiculoColor);
        dest.writeValue(vehiculoMarca);
        dest.writeValue(vehiculoModelo);
        dest.writeValue(vehiculoMotor);
        dest.writeValue(soatDepartamentoPcDescription);
        dest.writeValue(soatDepartamentoPcFk);
        dest.writeValue(soatDepartamentoVtFk);
        dest.writeValue(soatGestionFk);
        dest.writeValue(soatVehiculoTipoDescripcion);
        dest.writeValue(soatVehiculoTipoFk);
        dest.writeValue(soatVehiculoUsoDescription);
        dest.writeValue(soatVehiculoUsoFk);
        dest.writeValue(vehiculoPlaca);
        dest.writeValue(facturaMaestro);
    }

    public int describeContents() {
        return 0;
    }
}
