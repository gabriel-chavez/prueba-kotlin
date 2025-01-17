package com.emizor.univida.modelo.dominio.univida.ventas;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FacturaMaestro implements Serializable, Parcelable
{

    @SerializedName("actividad_economica")
    @Expose
    private String actividadEconomica;
    @SerializedName("codigo_control")
    @Expose
    private String codigoControl;
    @SerializedName("codigo_qr")
    @Expose
    private String codigoQr;
    @SerializedName("direccion_empresa")
    @Expose
    private String direccionEmpresa;
    @SerializedName("direccion_sucursal")
    @Expose
    private String direccionSucursal;
    @SerializedName("estado_factura")
    @Expose
    private String estadoFactura;
    @SerializedName("factura_qr")
    @Expose
    private Object facturaQr;
    @SerializedName("fax_empresa")
    @Expose
    private String faxEmpresa;
    @SerializedName("fecha_emision")
    @Expose
    private String fechaEmision;
    @SerializedName("fecha_limite_emision")
    @Expose
    private String fechaLimiteEmision;
    @SerializedName("importe_literal")
    @Expose
    private String importeLiteral;
    @SerializedName("importe_numeral")
    @Expose
    private String importeNumeral;
    @SerializedName("leyenda")
    @Expose
    private String leyenda;
    @SerializedName("lugar")
    @Expose
    private String lugar;
    @SerializedName("municipio_departamento")
    @Expose
    private String municipioDepartamento;
    @SerializedName("nit_ci_cliente")
    @Expose
    private String nitCiCliente;
    @SerializedName("nit_empresa")
    @Expose
    private String nitEmpresa;
    @SerializedName("nombre_sucursal")
    @Expose
    private String nombreSucursal;
    @SerializedName("numero_autorizacion")
    @Expose
    private String numeroAutorizacion;
    @SerializedName("numero_factura")
    @Expose
    private Integer numeroFactura;
    @SerializedName("numero_sucursal")
    @Expose
    private Integer numeroSucursal;
    @SerializedName("numero_tramite")
    @Expose
    private String numeroTramite;
    @SerializedName("razon_social")
    @Expose
    private Object razonSocial;
    @SerializedName("razon_social_client")
    @Expose
    private String razonSocialClient;
    @SerializedName("sucursal_fk")
    @Expose
    private Integer sucursalFk;
    @SerializedName("telefonos_empresa")
    @Expose
    private String telefonosEmpresa;
    @SerializedName("telefono_sucursal")
    @Expose
    private String telefonoSucursal;
    @SerializedName("tipo_emision")
    @Expose
    private String tipoEmision;
    @SerializedName("codigo_cliente")
    @Expose
    private String codigoCliente;
    @SerializedName("docuemento_sector_codigo")
    @Expose
    private Integer docuementoSectorCodigo;
    @SerializedName("documento_sector_nombre")
    @Expose
    private String documentoSectorNombre;
    @SerializedName("importe_base_credito_fiscal")
    @Expose
    private Double importeBaseCreditoFiscal;
    @SerializedName("importe_base_credito_fiscal_str")
    @Expose
    private String importeBaseCreditoFiscalStr;
    @SerializedName("importe_descuento")
    @Expose
    private Double importeDescuento;
    @SerializedName("importe_descuento_str")
    @Expose
    private String importeDescuentoStr;
    @SerializedName("importe_numeral_str")
    @Expose
    private String importeNumeralStr;
    @SerializedName("importe_subtotal")
    @Expose
    private Double importeSubtotal;
    @SerializedName("importe_subtotal_str")
    @Expose
    private String importeSubtotalStr;
    @SerializedName("punto_venta_codigo")
    @Expose
    private Integer puntoVentaCodigo;
    @SerializedName("subtitulo")
    @Expose
    private String subtitulo;
    @SerializedName("tipo_emision_leyenda")
    @Expose
    private String tipoEmisionLeyenda;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("detalle")
    @Expose
    private List<Detalle> detalle = null;
    public final static Parcelable.Creator<FacturaMaestro> CREATOR = new Creator<FacturaMaestro>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FacturaMaestro createFromParcel(Parcel in) {
            return new FacturaMaestro(in);
        }

        public FacturaMaestro[] newArray(int size) {
            return (new FacturaMaestro[size]);
        }

    }
            ;
    private final static long serialVersionUID = -2476063378576694980L;

    protected FacturaMaestro(Parcel in) {
        this.actividadEconomica = ((String) in.readValue((String.class.getClassLoader())));
        this.codigoControl = ((String) in.readValue((String.class.getClassLoader())));
        this.codigoQr = ((String) in.readValue((String.class.getClassLoader())));
        this.direccionEmpresa = ((String) in.readValue((String.class.getClassLoader())));
        this.direccionSucursal = ((String) in.readValue((String.class.getClassLoader())));
        this.estadoFactura = ((String) in.readValue((String.class.getClassLoader())));
        this.facturaQr = ((Object) in.readValue((Object.class.getClassLoader())));
        this.faxEmpresa = ((String) in.readValue((String.class.getClassLoader())));
        this.fechaEmision = ((String) in.readValue((String.class.getClassLoader())));
        this.fechaLimiteEmision = ((String) in.readValue((String.class.getClassLoader())));
        this.importeLiteral = ((String) in.readValue((String.class.getClassLoader())));
        this.importeNumeral = ((String) in.readValue((String.class.getClassLoader())));
        this.leyenda = ((String) in.readValue((String.class.getClassLoader())));
        this.lugar = ((String) in.readValue((String.class.getClassLoader())));
        this.municipioDepartamento = ((String) in.readValue((String.class.getClassLoader())));
        this.nitCiCliente = ((String) in.readValue((String.class.getClassLoader())));
        this.nitEmpresa = ((String) in.readValue((String.class.getClassLoader())));
        this.nombreSucursal = ((String) in.readValue((String.class.getClassLoader())));
        this.numeroAutorizacion = ((String) in.readValue((String.class.getClassLoader())));
        this.numeroFactura = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.numeroSucursal = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.numeroTramite = ((String) in.readValue((String.class.getClassLoader())));
        this.razonSocial = ((Object) in.readValue((Object.class.getClassLoader())));
        this.razonSocialClient = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalFk = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.telefonosEmpresa = ((String) in.readValue((String.class.getClassLoader())));
        this.telefonoSucursal = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoEmision = ((String) in.readValue((String.class.getClassLoader())));
        this.codigoCliente = ((String) in.readValue((String.class.getClassLoader())));
        this.docuementoSectorCodigo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.documentoSectorNombre = ((String) in.readValue((String.class.getClassLoader())));
        this.importeBaseCreditoFiscal = ((Double) in.readValue((Double.class.getClassLoader())));
        this.importeBaseCreditoFiscalStr = ((String) in.readValue((String.class.getClassLoader())));
        this.importeDescuento = ((Double) in.readValue((Double.class.getClassLoader())));
        this.importeDescuentoStr = ((String) in.readValue((String.class.getClassLoader())));
        this.importeNumeralStr = ((String) in.readValue((String.class.getClassLoader())));
        this.importeSubtotal = ((Double) in.readValue((Double.class.getClassLoader())));
        this.importeSubtotalStr = ((String) in.readValue((String.class.getClassLoader())));
        this.puntoVentaCodigo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.subtitulo = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoEmisionLeyenda = ((String) in.readValue((String.class.getClassLoader())));
        this.titulo = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.detalle, (Detalle.class.getClassLoader()));
    }

    public FacturaMaestro() {
    }

    public String getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public FacturaMaestro withActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
        return this;
    }

    public String getCodigoControl() {
        return codigoControl;
    }

    public void setCodigoControl(String codigoControl) {
        this.codigoControl = codigoControl;
    }

    public FacturaMaestro withCodigoControl(String codigoControl) {
        this.codigoControl = codigoControl;
        return this;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public FacturaMaestro withCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
        return this;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public FacturaMaestro withDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
        return this;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public FacturaMaestro withDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
        return this;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public FacturaMaestro withEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
        return this;
    }

    public Object getFacturaQr() {
        return facturaQr;
    }

    public void setFacturaQr(Object facturaQr) {
        this.facturaQr = facturaQr;
    }

    public FacturaMaestro withFacturaQr(Object facturaQr) {
        this.facturaQr = facturaQr;
        return this;
    }

    public String getFaxEmpresa() {
        return faxEmpresa;
    }

    public void setFaxEmpresa(String faxEmpresa) {
        this.faxEmpresa = faxEmpresa;
    }

    public FacturaMaestro withFaxEmpresa(String faxEmpresa) {
        this.faxEmpresa = faxEmpresa;
        return this;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public FacturaMaestro withFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
        return this;
    }

    public String getFechaLimiteEmision() {
        return fechaLimiteEmision;
    }

    public void setFechaLimiteEmision(String fechaLimiteEmision) {
        this.fechaLimiteEmision = fechaLimiteEmision;
    }

    public FacturaMaestro withFechaLimiteEmision(String fechaLimiteEmision) {
        this.fechaLimiteEmision = fechaLimiteEmision;
        return this;
    }

    public String getImporteLiteral() {
        return importeLiteral;
    }

    public void setImporteLiteral(String importeLiteral) {
        this.importeLiteral = importeLiteral;
    }

    public FacturaMaestro withImporteLiteral(String importeLiteral) {
        this.importeLiteral = importeLiteral;
        return this;
    }

    public String getImporteNumeral() {
        return importeNumeral;
    }

    public void setImporteNumeral(String importeNumeral) {
        this.importeNumeral = importeNumeral;
    }

    public FacturaMaestro withImporteNumeral(String importeNumeral) {
        this.importeNumeral = importeNumeral;
        return this;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public FacturaMaestro withLeyenda(String leyenda) {
        this.leyenda = leyenda;
        return this;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public FacturaMaestro withLugar(String lugar) {
        this.lugar = lugar;
        return this;
    }

    public String getMunicipioDepartamento() {
        return municipioDepartamento;
    }

    public void setMunicipioDepartamento(String municipioDepartamento) {
        this.municipioDepartamento = municipioDepartamento;
    }

    public FacturaMaestro withMunicipioDepartamento(String municipioDepartamento) {
        this.municipioDepartamento = municipioDepartamento;
        return this;
    }

    public String getNitCiCliente() {
        return nitCiCliente;
    }

    public void setNitCiCliente(String nitCiCliente) {
        this.nitCiCliente = nitCiCliente;
    }

    public FacturaMaestro withNitCiCliente(String nitCiCliente) {
        this.nitCiCliente = nitCiCliente;
        return this;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    public FacturaMaestro withNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
        return this;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public FacturaMaestro withNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
        return this;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public FacturaMaestro withNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
        return this;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public FacturaMaestro withNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
        return this;
    }

    public Integer getNumeroSucursal() {
        return numeroSucursal;
    }

    public void setNumeroSucursal(Integer numeroSucursal) {
        this.numeroSucursal = numeroSucursal;
    }

    public FacturaMaestro withNumeroSucursal(Integer numeroSucursal) {
        this.numeroSucursal = numeroSucursal;
        return this;
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public FacturaMaestro withNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
        return this;
    }

    public Object getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(Object razonSocial) {
        this.razonSocial = razonSocial;
    }

    public FacturaMaestro withRazonSocial(Object razonSocial) {
        this.razonSocial = razonSocial;
        return this;
    }

    public String getRazonSocialClient() {
        return razonSocialClient;
    }

    public void setRazonSocialClient(String razonSocialClient) {
        this.razonSocialClient = razonSocialClient;
    }

    public FacturaMaestro withRazonSocialClient(String razonSocialClient) {
        this.razonSocialClient = razonSocialClient;
        return this;
    }

    public Integer getSucursalFk() {
        return sucursalFk;
    }

    public void setSucursalFk(Integer sucursalFk) {
        this.sucursalFk = sucursalFk;
    }

    public FacturaMaestro withSucursalFk(Integer sucursalFk) {
        this.sucursalFk = sucursalFk;
        return this;
    }

    public String getTelefonosEmpresa() {
        return telefonosEmpresa;
    }

    public void setTelefonosEmpresa(String telefonosEmpresa) {
        this.telefonosEmpresa = telefonosEmpresa;
    }

    public FacturaMaestro withTelefonosEmpresa(String telefonosEmpresa) {
        this.telefonosEmpresa = telefonosEmpresa;
        return this;
    }

    public String getTelefonoSucursal() {
        return telefonoSucursal;
    }

    public void setTelefonoSucursal(String telefonoSucursal) {
        this.telefonoSucursal = telefonoSucursal;
    }

    public FacturaMaestro withTelefonoSucursal(String telefonoSucursal) {
        this.telefonoSucursal = telefonoSucursal;
        return this;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public FacturaMaestro withTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
        return this;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public FacturaMaestro withCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
        return this;
    }

    public Integer getDocuementoSectorCodigo() {
        return docuementoSectorCodigo;
    }

    public void setDocuementoSectorCodigo(Integer docuementoSectorCodigo) {
        this.docuementoSectorCodigo = docuementoSectorCodigo;
    }

    public FacturaMaestro withDocuementoSectorCodigo(Integer docuementoSectorCodigo) {
        this.docuementoSectorCodigo = docuementoSectorCodigo;
        return this;
    }

    public String getDocumentoSectorNombre() {
        return documentoSectorNombre;
    }

    public void setDocumentoSectorNombre(String documentoSectorNombre) {
        this.documentoSectorNombre = documentoSectorNombre;
    }

    public FacturaMaestro withDocumentoSectorNombre(String documentoSectorNombre) {
        this.documentoSectorNombre = documentoSectorNombre;
        return this;
    }

    public Double getImporteBaseCreditoFiscal() {
        return importeBaseCreditoFiscal;
    }

    public void setImporteBaseCreditoFiscal(Double importeBaseCreditoFiscal) {
        this.importeBaseCreditoFiscal = importeBaseCreditoFiscal;
    }

    public FacturaMaestro withImporteBaseCreditoFiscal(Double importeBaseCreditoFiscal) {
        this.importeBaseCreditoFiscal = importeBaseCreditoFiscal;
        return this;
    }

    public String getImporteBaseCreditoFiscalStr() {
        return importeBaseCreditoFiscalStr;
    }

    public void setImporteBaseCreditoFiscalStr(String importeBaseCreditoFiscalStr) {
        this.importeBaseCreditoFiscalStr = importeBaseCreditoFiscalStr;
    }

    public FacturaMaestro withImporteBaseCreditoFiscalStr(String importeBaseCreditoFiscalStr) {
        this.importeBaseCreditoFiscalStr = importeBaseCreditoFiscalStr;
        return this;
    }

    public Double getImporteDescuento() {
        return importeDescuento;
    }

    public void setImporteDescuento(Double importeDescuento) {
        this.importeDescuento = importeDescuento;
    }

    public FacturaMaestro withImporteDescuento(Double importeDescuento) {
        this.importeDescuento = importeDescuento;
        return this;
    }

    public String getImporteDescuentoStr() {
        return importeDescuentoStr;
    }

    public void setImporteDescuentoStr(String importeDescuentoStr) {
        this.importeDescuentoStr = importeDescuentoStr;
    }

    public FacturaMaestro withImporteDescuentoStr(String importeDescuentoStr) {
        this.importeDescuentoStr = importeDescuentoStr;
        return this;
    }

    public String getImporteNumeralStr() {
        return importeNumeralStr;
    }

    public void setImporteNumeralStr(String importeNumeralStr) {
        this.importeNumeralStr = importeNumeralStr;
    }

    public FacturaMaestro withImporteNumeralStr(String importeNumeralStr) {
        this.importeNumeralStr = importeNumeralStr;
        return this;
    }

    public Double getImporteSubtotal() {
        return importeSubtotal;
    }

    public void setImporteSubtotal(Double importeSubtotal) {
        this.importeSubtotal = importeSubtotal;
    }

    public FacturaMaestro withImporteSubtotal(Double importeSubtotal) {
        this.importeSubtotal = importeSubtotal;
        return this;
    }

    public String getImporteSubtotalStr() {
        return importeSubtotalStr;
    }

    public void setImporteSubtotalStr(String importeSubtotalStr) {
        this.importeSubtotalStr = importeSubtotalStr;
    }

    public FacturaMaestro withImporteSubtotalStr(String importeSubtotalStr) {
        this.importeSubtotalStr = importeSubtotalStr;
        return this;
    }

    public Integer getPuntoVentaCodigo() {
        return puntoVentaCodigo;
    }

    public void setPuntoVentaCodigo(Integer puntoVentaCodigo) {
        this.puntoVentaCodigo = puntoVentaCodigo;
    }

    public FacturaMaestro withPuntoVentaCodigo(Integer puntoVentaCodigo) {
        this.puntoVentaCodigo = puntoVentaCodigo;
        return this;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public FacturaMaestro withSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
        return this;
    }

    public String getTipoEmisionLeyenda() {
        return tipoEmisionLeyenda;
    }

    public void setTipoEmisionLeyenda(String tipoEmisionLeyenda) {
        this.tipoEmisionLeyenda = tipoEmisionLeyenda;
    }

    public FacturaMaestro withTipoEmisionLeyenda(String tipoEmisionLeyenda) {
        this.tipoEmisionLeyenda = tipoEmisionLeyenda;
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public FacturaMaestro withTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

    public FacturaMaestro withDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FacturaMaestro.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("actividadEconomica");
        sb.append('=');
        sb.append(((this.actividadEconomica == null)?"<null>":this.actividadEconomica));
        sb.append(',');
        sb.append("codigoControl");
        sb.append('=');
        sb.append(((this.codigoControl == null)?"<null>":this.codigoControl));
        sb.append(',');
        sb.append("codigoQr");
        sb.append('=');
        sb.append(((this.codigoQr == null)?"<null>":this.codigoQr));
        sb.append(',');
        sb.append("direccionEmpresa");
        sb.append('=');
        sb.append(((this.direccionEmpresa == null)?"<null>":this.direccionEmpresa));
        sb.append(',');
        sb.append("direccionSucursal");
        sb.append('=');
        sb.append(((this.direccionSucursal == null)?"<null>":this.direccionSucursal));
        sb.append(',');
        sb.append("estadoFactura");
        sb.append('=');
        sb.append(((this.estadoFactura == null)?"<null>":this.estadoFactura));
        sb.append(',');
        sb.append("facturaQr");
        sb.append('=');
        sb.append(((this.facturaQr == null)?"<null>":this.facturaQr));
        sb.append(',');
        sb.append("faxEmpresa");
        sb.append('=');
        sb.append(((this.faxEmpresa == null)?"<null>":this.faxEmpresa));
        sb.append(',');
        sb.append("fechaEmision");
        sb.append('=');
        sb.append(((this.fechaEmision == null)?"<null>":this.fechaEmision));
        sb.append(',');
        sb.append("fechaLimiteEmision");
        sb.append('=');
        sb.append(((this.fechaLimiteEmision == null)?"<null>":this.fechaLimiteEmision));
        sb.append(',');
        sb.append("importeLiteral");
        sb.append('=');
        sb.append(((this.importeLiteral == null)?"<null>":this.importeLiteral));
        sb.append(',');
        sb.append("importeNumeral");
        sb.append('=');
        sb.append(((this.importeNumeral == null)?"<null>":this.importeNumeral));
        sb.append(',');
        sb.append("leyenda");
        sb.append('=');
        sb.append(((this.leyenda == null)?"<null>":this.leyenda));
        sb.append(',');
        sb.append("lugar");
        sb.append('=');
        sb.append(((this.lugar == null)?"<null>":this.lugar));
        sb.append(',');
        sb.append("municipioDepartamento");
        sb.append('=');
        sb.append(((this.municipioDepartamento == null)?"<null>":this.municipioDepartamento));
        sb.append(',');
        sb.append("nitCiCliente");
        sb.append('=');
        sb.append(((this.nitCiCliente == null)?"<null>":this.nitCiCliente));
        sb.append(',');
        sb.append("nitEmpresa");
        sb.append('=');
        sb.append(((this.nitEmpresa == null)?"<null>":this.nitEmpresa));
        sb.append(',');
        sb.append("nombreSucursal");
        sb.append('=');
        sb.append(((this.nombreSucursal == null)?"<null>":this.nombreSucursal));
        sb.append(',');
        sb.append("numeroAutorizacion");
        sb.append('=');
        sb.append(((this.numeroAutorizacion == null)?"<null>":this.numeroAutorizacion));
        sb.append(',');
        sb.append("numeroFactura");
        sb.append('=');
        sb.append(((this.numeroFactura == null)?"<null>":this.numeroFactura));
        sb.append(',');
        sb.append("numeroSucursal");
        sb.append('=');
        sb.append(((this.numeroSucursal == null)?"<null>":this.numeroSucursal));
        sb.append(',');
        sb.append("numeroTramite");
        sb.append('=');
        sb.append(((this.numeroTramite == null)?"<null>":this.numeroTramite));
        sb.append(',');
        sb.append("razonSocial");
        sb.append('=');
        sb.append(((this.razonSocial == null)?"<null>":this.razonSocial));
        sb.append(',');
        sb.append("razonSocialClient");
        sb.append('=');
        sb.append(((this.razonSocialClient == null)?"<null>":this.razonSocialClient));
        sb.append(',');
        sb.append("sucursalFk");
        sb.append('=');
        sb.append(((this.sucursalFk == null)?"<null>":this.sucursalFk));
        sb.append(',');
        sb.append("telefonosEmpresa");
        sb.append('=');
        sb.append(((this.telefonosEmpresa == null)?"<null>":this.telefonosEmpresa));
        sb.append(',');
        sb.append("telefonoSucursal");
        sb.append('=');
        sb.append(((this.telefonoSucursal == null)?"<null>":this.telefonoSucursal));
        sb.append(',');
        sb.append("tipoEmision");
        sb.append('=');
        sb.append(((this.tipoEmision == null)?"<null>":this.tipoEmision));
        sb.append(',');
        sb.append("codigoCliente");
        sb.append('=');
        sb.append(((this.codigoCliente == null)?"<null>":this.codigoCliente));
        sb.append(',');
        sb.append("docuementoSectorCodigo");
        sb.append('=');
        sb.append(((this.docuementoSectorCodigo == null)?"<null>":this.docuementoSectorCodigo));
        sb.append(',');
        sb.append("documentoSectorNombre");
        sb.append('=');
        sb.append(((this.documentoSectorNombre == null)?"<null>":this.documentoSectorNombre));
        sb.append(',');
        sb.append("importeBaseCreditoFiscal");
        sb.append('=');
        sb.append(((this.importeBaseCreditoFiscal == null)?"<null>":this.importeBaseCreditoFiscal));
        sb.append(',');
        sb.append("importeBaseCreditoFiscalStr");
        sb.append('=');
        sb.append(((this.importeBaseCreditoFiscalStr == null)?"<null>":this.importeBaseCreditoFiscalStr));
        sb.append(',');
        sb.append("importeDescuento");
        sb.append('=');
        sb.append(((this.importeDescuento == null)?"<null>":this.importeDescuento));
        sb.append(',');
        sb.append("importeDescuentoStr");
        sb.append('=');
        sb.append(((this.importeDescuentoStr == null)?"<null>":this.importeDescuentoStr));
        sb.append(',');
        sb.append("importeNumeralStr");
        sb.append('=');
        sb.append(((this.importeNumeralStr == null)?"<null>":this.importeNumeralStr));
        sb.append(',');
        sb.append("importeSubtotal");
        sb.append('=');
        sb.append(((this.importeSubtotal == null)?"<null>":this.importeSubtotal));
        sb.append(',');
        sb.append("importeSubtotalStr");
        sb.append('=');
        sb.append(((this.importeSubtotalStr == null)?"<null>":this.importeSubtotalStr));
        sb.append(',');
        sb.append("puntoVentaCodigo");
        sb.append('=');
        sb.append(((this.puntoVentaCodigo == null)?"<null>":this.puntoVentaCodigo));
        sb.append(',');
        sb.append("subtitulo");
        sb.append('=');
        sb.append(((this.subtitulo == null)?"<null>":this.subtitulo));
        sb.append(',');
        sb.append("tipoEmisionLeyenda");
        sb.append('=');
        sb.append(((this.tipoEmisionLeyenda == null)?"<null>":this.tipoEmisionLeyenda));
        sb.append(',');
        sb.append("titulo");
        sb.append('=');
        sb.append(((this.titulo == null)?"<null>":this.titulo));
        sb.append(',');
        sb.append("detalle");
        sb.append('=');
        sb.append(((this.detalle == null)?"<null>":this.detalle));
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
        result = ((result* 31)+((this.faxEmpresa == null)? 0 :this.faxEmpresa.hashCode()));
        result = ((result* 31)+((this.numeroAutorizacion == null)? 0 :this.numeroAutorizacion.hashCode()));
        result = ((result* 31)+((this.numeroSucursal == null)? 0 :this.numeroSucursal.hashCode()));
        result = ((result* 31)+((this.importeNumeralStr == null)? 0 :this.importeNumeralStr.hashCode()));
        result = ((result* 31)+((this.subtitulo == null)? 0 :this.subtitulo.hashCode()));
        result = ((result* 31)+((this.puntoVentaCodigo == null)? 0 :this.puntoVentaCodigo.hashCode()));
        result = ((result* 31)+((this.docuementoSectorCodigo == null)? 0 :this.docuementoSectorCodigo.hashCode()));
        result = ((result* 31)+((this.codigoControl == null)? 0 :this.codigoControl.hashCode()));
        result = ((result* 31)+((this.estadoFactura == null)? 0 :this.estadoFactura.hashCode()));
        result = ((result* 31)+((this.lugar == null)? 0 :this.lugar.hashCode()));
        result = ((result* 31)+((this.titulo == null)? 0 :this.titulo.hashCode()));
        result = ((result* 31)+((this.facturaQr == null)? 0 :this.facturaQr.hashCode()));
        result = ((result* 31)+((this.razonSocial == null)? 0 :this.razonSocial.hashCode()));
        result = ((result* 31)+((this.leyenda == null)? 0 :this.leyenda.hashCode()));
        result = ((result* 31)+((this.razonSocialClient == null)? 0 :this.razonSocialClient.hashCode()));
        result = ((result* 31)+((this.importeBaseCreditoFiscal == null)? 0 :this.importeBaseCreditoFiscal.hashCode()));
        result = ((result* 31)+((this.importeSubtotalStr == null)? 0 :this.importeSubtotalStr.hashCode()));
        result = ((result* 31)+((this.importeBaseCreditoFiscalStr == null)? 0 :this.importeBaseCreditoFiscalStr.hashCode()));
        result = ((result* 31)+((this.codigoQr == null)? 0 :this.codigoQr.hashCode()));
        result = ((result* 31)+((this.documentoSectorNombre == null)? 0 :this.documentoSectorNombre.hashCode()));
        result = ((result* 31)+((this.numeroTramite == null)? 0 :this.numeroTramite.hashCode()));
        result = ((result* 31)+((this.importeLiteral == null)? 0 :this.importeLiteral.hashCode()));
        result = ((result* 31)+((this.nitEmpresa == null)? 0 :this.nitEmpresa.hashCode()));
        result = ((result* 31)+((this.municipioDepartamento == null)? 0 :this.municipioDepartamento.hashCode()));
        result = ((result* 31)+((this.telefonosEmpresa == null)? 0 :this.telefonosEmpresa.hashCode()));
        result = ((result* 31)+((this.importeDescuento == null)? 0 :this.importeDescuento.hashCode()));
        result = ((result* 31)+((this.nitCiCliente == null)? 0 :this.nitCiCliente.hashCode()));
        result = ((result* 31)+((this.fechaEmision == null)? 0 :this.fechaEmision.hashCode()));
        result = ((result* 31)+((this.numeroFactura == null)? 0 :this.numeroFactura.hashCode()));
        result = ((result* 31)+((this.actividadEconomica == null)? 0 :this.actividadEconomica.hashCode()));
        result = ((result* 31)+((this.fechaLimiteEmision == null)? 0 :this.fechaLimiteEmision.hashCode()));
        result = ((result* 31)+((this.detalle == null)? 0 :this.detalle.hashCode()));
        result = ((result* 31)+((this.direccionSucursal == null)? 0 :this.direccionSucursal.hashCode()));
        result = ((result* 31)+((this.importeNumeral == null)? 0 :this.importeNumeral.hashCode()));
        result = ((result* 31)+((this.direccionEmpresa == null)? 0 :this.direccionEmpresa.hashCode()));
        result = ((result* 31)+((this.importeDescuentoStr == null)? 0 :this.importeDescuentoStr.hashCode()));
        result = ((result* 31)+((this.importeSubtotal == null)? 0 :this.importeSubtotal.hashCode()));
        result = ((result* 31)+((this.codigoCliente == null)? 0 :this.codigoCliente.hashCode()));
        result = ((result* 31)+((this.nombreSucursal == null)? 0 :this.nombreSucursal.hashCode()));
        result = ((result* 31)+((this.sucursalFk == null)? 0 :this.sucursalFk.hashCode()));
        result = ((result* 31)+((this.tipoEmisionLeyenda == null)? 0 :this.tipoEmisionLeyenda.hashCode()));
        result = ((result* 31)+((this.telefonoSucursal == null)? 0 :this.telefonoSucursal.hashCode()));
        result = ((result* 31)+((this.tipoEmision == null)? 0 :this.tipoEmision.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FacturaMaestro) == false) {
            return false;
        }
        FacturaMaestro rhs = ((FacturaMaestro) other);
        return ((((((((((((((((((((((((((((((((((((((((((((this.faxEmpresa == rhs.faxEmpresa)||((this.faxEmpresa!= null)&&this.faxEmpresa.equals(rhs.faxEmpresa)))&&((this.numeroAutorizacion == rhs.numeroAutorizacion)||((this.numeroAutorizacion!= null)&&this.numeroAutorizacion.equals(rhs.numeroAutorizacion))))&&((this.numeroSucursal == rhs.numeroSucursal)||((this.numeroSucursal!= null)&&this.numeroSucursal.equals(rhs.numeroSucursal))))&&((this.importeNumeralStr == rhs.importeNumeralStr)||((this.importeNumeralStr!= null)&&this.importeNumeralStr.equals(rhs.importeNumeralStr))))&&((this.subtitulo == rhs.subtitulo)||((this.subtitulo!= null)&&this.subtitulo.equals(rhs.subtitulo))))&&((this.puntoVentaCodigo == rhs.puntoVentaCodigo)||((this.puntoVentaCodigo!= null)&&this.puntoVentaCodigo.equals(rhs.puntoVentaCodigo))))&&((this.docuementoSectorCodigo == rhs.docuementoSectorCodigo)||((this.docuementoSectorCodigo!= null)&&this.docuementoSectorCodigo.equals(rhs.docuementoSectorCodigo))))&&((this.codigoControl == rhs.codigoControl)||((this.codigoControl!= null)&&this.codigoControl.equals(rhs.codigoControl))))&&((this.estadoFactura == rhs.estadoFactura)||((this.estadoFactura!= null)&&this.estadoFactura.equals(rhs.estadoFactura))))&&((this.lugar == rhs.lugar)||((this.lugar!= null)&&this.lugar.equals(rhs.lugar))))&&((this.titulo == rhs.titulo)||((this.titulo!= null)&&this.titulo.equals(rhs.titulo))))&&((this.facturaQr == rhs.facturaQr)||((this.facturaQr!= null)&&this.facturaQr.equals(rhs.facturaQr))))&&((this.razonSocial == rhs.razonSocial)||((this.razonSocial!= null)&&this.razonSocial.equals(rhs.razonSocial))))&&((this.leyenda == rhs.leyenda)||((this.leyenda!= null)&&this.leyenda.equals(rhs.leyenda))))&&((this.razonSocialClient == rhs.razonSocialClient)||((this.razonSocialClient!= null)&&this.razonSocialClient.equals(rhs.razonSocialClient))))&&((this.importeBaseCreditoFiscal == rhs.importeBaseCreditoFiscal)||((this.importeBaseCreditoFiscal!= null)&&this.importeBaseCreditoFiscal.equals(rhs.importeBaseCreditoFiscal))))&&((this.importeSubtotalStr == rhs.importeSubtotalStr)||((this.importeSubtotalStr!= null)&&this.importeSubtotalStr.equals(rhs.importeSubtotalStr))))&&((this.importeBaseCreditoFiscalStr == rhs.importeBaseCreditoFiscalStr)||((this.importeBaseCreditoFiscalStr!= null)&&this.importeBaseCreditoFiscalStr.equals(rhs.importeBaseCreditoFiscalStr))))&&((this.codigoQr == rhs.codigoQr)||((this.codigoQr!= null)&&this.codigoQr.equals(rhs.codigoQr))))&&((this.documentoSectorNombre == rhs.documentoSectorNombre)||((this.documentoSectorNombre!= null)&&this.documentoSectorNombre.equals(rhs.documentoSectorNombre))))&&((this.numeroTramite == rhs.numeroTramite)||((this.numeroTramite!= null)&&this.numeroTramite.equals(rhs.numeroTramite))))&&((this.importeLiteral == rhs.importeLiteral)||((this.importeLiteral!= null)&&this.importeLiteral.equals(rhs.importeLiteral))))&&((this.nitEmpresa == rhs.nitEmpresa)||((this.nitEmpresa!= null)&&this.nitEmpresa.equals(rhs.nitEmpresa))))&&((this.municipioDepartamento == rhs.municipioDepartamento)||((this.municipioDepartamento!= null)&&this.municipioDepartamento.equals(rhs.municipioDepartamento))))&&((this.telefonosEmpresa == rhs.telefonosEmpresa)||((this.telefonosEmpresa!= null)&&this.telefonosEmpresa.equals(rhs.telefonosEmpresa))))&&((this.importeDescuento == rhs.importeDescuento)||((this.importeDescuento!= null)&&this.importeDescuento.equals(rhs.importeDescuento))))&&((this.nitCiCliente == rhs.nitCiCliente)||((this.nitCiCliente!= null)&&this.nitCiCliente.equals(rhs.nitCiCliente))))&&((this.fechaEmision == rhs.fechaEmision)||((this.fechaEmision!= null)&&this.fechaEmision.equals(rhs.fechaEmision))))&&((this.numeroFactura == rhs.numeroFactura)||((this.numeroFactura!= null)&&this.numeroFactura.equals(rhs.numeroFactura))))&&((this.actividadEconomica == rhs.actividadEconomica)||((this.actividadEconomica!= null)&&this.actividadEconomica.equals(rhs.actividadEconomica))))&&((this.fechaLimiteEmision == rhs.fechaLimiteEmision)||((this.fechaLimiteEmision!= null)&&this.fechaLimiteEmision.equals(rhs.fechaLimiteEmision))))&&((this.detalle == rhs.detalle)||((this.detalle!= null)&&this.detalle.equals(rhs.detalle))))&&((this.direccionSucursal == rhs.direccionSucursal)||((this.direccionSucursal!= null)&&this.direccionSucursal.equals(rhs.direccionSucursal))))&&((this.importeNumeral == rhs.importeNumeral)||((this.importeNumeral!= null)&&this.importeNumeral.equals(rhs.importeNumeral))))&&((this.direccionEmpresa == rhs.direccionEmpresa)||((this.direccionEmpresa!= null)&&this.direccionEmpresa.equals(rhs.direccionEmpresa))))&&((this.importeDescuentoStr == rhs.importeDescuentoStr)||((this.importeDescuentoStr!= null)&&this.importeDescuentoStr.equals(rhs.importeDescuentoStr))))&&((this.importeSubtotal == rhs.importeSubtotal)||((this.importeSubtotal!= null)&&this.importeSubtotal.equals(rhs.importeSubtotal))))&&((this.codigoCliente == rhs.codigoCliente)||((this.codigoCliente!= null)&&this.codigoCliente.equals(rhs.codigoCliente))))&&((this.nombreSucursal == rhs.nombreSucursal)||((this.nombreSucursal!= null)&&this.nombreSucursal.equals(rhs.nombreSucursal))))&&((this.sucursalFk == rhs.sucursalFk)||((this.sucursalFk!= null)&&this.sucursalFk.equals(rhs.sucursalFk))))&&((this.tipoEmisionLeyenda == rhs.tipoEmisionLeyenda)||((this.tipoEmisionLeyenda!= null)&&this.tipoEmisionLeyenda.equals(rhs.tipoEmisionLeyenda))))&&((this.telefonoSucursal == rhs.telefonoSucursal)||((this.telefonoSucursal!= null)&&this.telefonoSucursal.equals(rhs.telefonoSucursal))))&&((this.tipoEmision == rhs.tipoEmision)||((this.tipoEmision!= null)&&this.tipoEmision.equals(rhs.tipoEmision))));
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(actividadEconomica);
        dest.writeValue(codigoControl);
        dest.writeValue(codigoQr);
        dest.writeValue(direccionEmpresa);
        dest.writeValue(direccionSucursal);
        dest.writeValue(estadoFactura);
        dest.writeValue(facturaQr);
        dest.writeValue(faxEmpresa);
        dest.writeValue(fechaEmision);
        dest.writeValue(fechaLimiteEmision);
        dest.writeValue(importeLiteral);
        dest.writeValue(importeNumeral);
        dest.writeValue(leyenda);
        dest.writeValue(lugar);
        dest.writeValue(municipioDepartamento);
        dest.writeValue(nitCiCliente);
        dest.writeValue(nitEmpresa);
        dest.writeValue(nombreSucursal);
        dest.writeValue(numeroAutorizacion);
        dest.writeValue(numeroFactura);
        dest.writeValue(numeroSucursal);
        dest.writeValue(numeroTramite);
        dest.writeValue(razonSocial);
        dest.writeValue(razonSocialClient);
        dest.writeValue(sucursalFk);
        dest.writeValue(telefonosEmpresa);
        dest.writeValue(telefonoSucursal);
        dest.writeValue(tipoEmision);
        dest.writeValue(codigoCliente);
        dest.writeValue(docuementoSectorCodigo);
        dest.writeValue(documentoSectorNombre);
        dest.writeValue(importeBaseCreditoFiscal);
        dest.writeValue(importeBaseCreditoFiscalStr);
        dest.writeValue(importeDescuento);
        dest.writeValue(importeDescuentoStr);
        dest.writeValue(importeNumeralStr);
        dest.writeValue(importeSubtotal);
        dest.writeValue(importeSubtotalStr);
        dest.writeValue(puntoVentaCodigo);
        dest.writeValue(subtitulo);
        dest.writeValue(tipoEmisionLeyenda);
        dest.writeValue(titulo);
        dest.writeList(detalle);
    }

    public int describeContents() {
        return 0;
    }

}
