package com.emizor.univida.modelo.dominio.univida.soatc;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmiPolizaObtenerResponse {
    @SerializedName("PolDetSecuencial")
    private int polDetSecuencial;
    @SerializedName("contenidoQR")
    private String contenidoQR;

    @SerializedName("oFacturaMaestro")
    private FacturaMaestro facturaMaestro;

    @SerializedName("oPolizaDetalle")
    private PolizaDetalle polizaDetalle;

    @SerializedName("oPolizaMaestro")
    private PolizaMaestro polizaMaestro;

    @SerializedName("oProducto")
    private Producto producto;

    // Getters y Setters
    public String getContenidoQR() { return contenidoQR; }
    public int getPolDetSecuencial() { return polDetSecuencial; }
    public void setPolDetSecuencial(int polDetSecuencial) { this.polDetSecuencial = polDetSecuencial; }

    public FacturaMaestro getFacturaMaestro() { return facturaMaestro; }
    public void setFacturaMaestro(FacturaMaestro facturaMaestro) { this.facturaMaestro = facturaMaestro; }

    public PolizaDetalle getPolizaDetalle() { return polizaDetalle; }
    public void setPolizaDetalle(PolizaDetalle polizaDetalle) { this.polizaDetalle = polizaDetalle; }

    public PolizaMaestro getPolizaMaestro() { return polizaMaestro; }
    public void setPolizaMaestro(PolizaMaestro polizaMaestro) { this.polizaMaestro = polizaMaestro; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    // Método para verificar si fue exitoso
    public boolean getExito() {
        return facturaMaestro != null && polizaDetalle != null;
    }

    public String getMensaje() {
        return "Póliza emitida exitosamente";
    }

    // Clases internas para la estructura anidada
    public static class FacturaMaestro {
        @SerializedName("NumeroFactura")
        private int numeroFactura;

        @SerializedName("CodigoControl")
        private String codigoControl;

        @SerializedName("CodigoQR")
        private String codigoQR;

        @SerializedName("FechaEmision")
        private String fechaEmision;

        @SerializedName("ImporteNumeral")
        private double importeNumeral;

        @SerializedName("ImporteLiteral")
        private String importeLiteral;

        @SerializedName("NitCiCliente")
        private String nitCiCliente;

        @SerializedName("RazonSocialCliente")
        private String razonSocialCliente;

        @SerializedName("LFacturaDetalle")
        private List<FacturaDetalle> facturaDetalle;

        @SerializedName("NumeroAutorizacion")
        private String numeroAutorizacion;

        @SerializedName("EstadoFactura")
        private String estadoFactura;

        // Nuevos atributos agregados
        @SerializedName("ActividadEconomica")
        private String actividadEconomica;

        @SerializedName("CodigoCliente")
        private String codigoCliente;

        @SerializedName("DireccionEmpresa")
        private String direccionEmpresa;

        @SerializedName("DireccionSucursal")
        private String direccionSucursal;

        @SerializedName("DocumentoSectorCodigo")
        private int documentoSectorCodigo;

        @SerializedName("DocumentoSectorNombre")
        private String documentoSectorNombre;

        @SerializedName("EstadoFacturaFk")
        private int estadoFacturaFk;

        @SerializedName("FacturaQR")
        private String facturaQR;

        @SerializedName("FaxEmpresa")
        private String faxEmpresa;

        @SerializedName("FechaLimiteEmision")
        private String fechaLimiteEmision;

        @SerializedName("ImporteBaseCreditoFiscal")
        private double importeBaseCreditoFiscal;

        @SerializedName("ImporteBaseCreditoFiscalStr")
        private String importeBaseCreditoFiscalStr;

        @SerializedName("ImporteDescuento")
        private double importeDescuento;

        @SerializedName("ImporteDescuentoStr")
        private String importeDescuentoStr;

        @SerializedName("ImporteNumeralStr")
        private String importeNumeralStr;

        @SerializedName("ImporteSubtotal")
        private double importeSubtotal;

        @SerializedName("ImporteSubtotalStr")
        private String importeSubtotalStr;

        @SerializedName("Leyenda")
        private String leyenda;

        @SerializedName("Lugar")
        private String lugar;

        @SerializedName("MaestroFk")
        private int maestroFk;

        @SerializedName("MunicipioDepartamento")
        private String municipioDepartamento;

        @SerializedName("NitEmpresa")
        private String nitEmpresa;

        @SerializedName("NombreSucursal")
        private String nombreSucursal;

        @SerializedName("NumeroSucursal")
        private int numeroSucursal;

        @SerializedName("NumeroTramite")
        private String numeroTramite;

        @SerializedName("PuntoVentaCodigo")
        private int puntoVentaCodigo;

        @SerializedName("RazonSocial")
        private String razonSocial;

        @SerializedName("Subtitulo")
        private String subtitulo;

        @SerializedName("SucursalFk")
        private int sucursalFk;

        @SerializedName("TefefonosEmpresa")
        private String tefefonosEmpresa;

        @SerializedName("TelefonoSucursal")
        private String telefonoSucursal;

        @SerializedName("TipoEmision")
        private String tipoEmision;

        @SerializedName("TipoEmisionLeyenda")
        private String tipoEmisionLeyenda;

        @SerializedName("Titulo")
        private String titulo;

        // Getters y Setters para todos los atributos
        public int getNumeroFactura() { return numeroFactura; }
        public void setNumeroFactura(int numeroFactura) { this.numeroFactura = numeroFactura; }

        public String getCodigoControl() { return codigoControl; }
        public void setCodigoControl(String codigoControl) { this.codigoControl = codigoControl; }

        public String getCodigoQR() { return codigoQR; }
        public void setCodigoQR(String codigoQR) { this.codigoQR = codigoQR; }

        public String getFechaEmision() { return fechaEmision; }
        public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }

        public double getImporteNumeral() { return importeNumeral; }
        public void setImporteNumeral(double importeNumeral) { this.importeNumeral = importeNumeral; }

        public String getImporteLiteral() { return importeLiteral; }
        public void setImporteLiteral(String importeLiteral) { this.importeLiteral = importeLiteral; }

        public String getNitCiCliente() { return nitCiCliente; }
        public void setNitCiCliente(String nitCiCliente) { this.nitCiCliente = nitCiCliente; }

        public String getRazonSocialCliente() { return razonSocialCliente; }
        public void setRazonSocialCliente(String razonSocialCliente) { this.razonSocialCliente = razonSocialCliente; }

        public List<FacturaDetalle> getFacturaDetalle() { return facturaDetalle; }
        public void setFacturaDetalle(List<FacturaDetalle> facturaDetalle) { this.facturaDetalle = facturaDetalle; }

        public String getNumeroAutorizacion() { return numeroAutorizacion; }
        public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }

        public String getEstadoFactura() { return estadoFactura; }
        public void setEstadoFactura(String estadoFactura) { this.estadoFactura = estadoFactura; }

        public String getActividadEconomica() { return actividadEconomica; }
        public void setActividadEconomica(String actividadEconomica) { this.actividadEconomica = actividadEconomica; }

        public String getCodigoCliente() { return codigoCliente; }
        public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }

        public String getDireccionEmpresa() { return direccionEmpresa; }
        public void setDireccionEmpresa(String direccionEmpresa) { this.direccionEmpresa = direccionEmpresa; }

        public String getDireccionSucursal() { return direccionSucursal; }
        public void setDireccionSucursal(String direccionSucursal) { this.direccionSucursal = direccionSucursal; }

        public int getDocumentoSectorCodigo() { return documentoSectorCodigo; }
        public void setDocumentoSectorCodigo(int documentoSectorCodigo) { this.documentoSectorCodigo = documentoSectorCodigo; }

        public String getDocumentoSectorNombre() { return documentoSectorNombre; }
        public void setDocumentoSectorNombre(String documentoSectorNombre) { this.documentoSectorNombre = documentoSectorNombre; }

        public int getEstadoFacturaFk() { return estadoFacturaFk; }
        public void setEstadoFacturaFk(int estadoFacturaFk) { this.estadoFacturaFk = estadoFacturaFk; }

        public String getFacturaQR() { return facturaQR; }
        public void setFacturaQR(String facturaQR) { this.facturaQR = facturaQR; }

        public String getFaxEmpresa() { return faxEmpresa; }
        public void setFaxEmpresa(String faxEmpresa) { this.faxEmpresa = faxEmpresa; }

        public String getFechaLimiteEmision() { return fechaLimiteEmision; }
        public void setFechaLimiteEmision(String fechaLimiteEmision) { this.fechaLimiteEmision = fechaLimiteEmision; }

        public double getImporteBaseCreditoFiscal() { return importeBaseCreditoFiscal; }
        public void setImporteBaseCreditoFiscal(double importeBaseCreditoFiscal) { this.importeBaseCreditoFiscal = importeBaseCreditoFiscal; }

        public String getImporteBaseCreditoFiscalStr() { return importeBaseCreditoFiscalStr; }
        public void setImporteBaseCreditoFiscalStr(String importeBaseCreditoFiscalStr) { this.importeBaseCreditoFiscalStr = importeBaseCreditoFiscalStr; }

        public double getImporteDescuento() { return importeDescuento; }
        public void setImporteDescuento(double importeDescuento) { this.importeDescuento = importeDescuento; }

        public String getImporteDescuentoStr() { return importeDescuentoStr; }
        public void setImporteDescuentoStr(String importeDescuentoStr) { this.importeDescuentoStr = importeDescuentoStr; }

        public String getImporteNumeralStr() { return importeNumeralStr; }
        public void setImporteNumeralStr(String importeNumeralStr) { this.importeNumeralStr = importeNumeralStr; }

        public double getImporteSubtotal() { return importeSubtotal; }
        public void setImporteSubtotal(double importeSubtotal) { this.importeSubtotal = importeSubtotal; }

        public String getImporteSubtotalStr() { return importeSubtotalStr; }
        public void setImporteSubtotalStr(String importeSubtotalStr) { this.importeSubtotalStr = importeSubtotalStr; }

        public String getLeyenda() { return leyenda; }
        public void setLeyenda(String leyenda) { this.leyenda = leyenda; }

        public String getLugar() { return lugar; }
        public void setLugar(String lugar) { this.lugar = lugar; }

        public int getMaestroFk() { return maestroFk; }
        public void setMaestroFk(int maestroFk) { this.maestroFk = maestroFk; }

        public String getMunicipioDepartamento() { return municipioDepartamento; }
        public void setMunicipioDepartamento(String municipioDepartamento) { this.municipioDepartamento = municipioDepartamento; }

        public String getNitEmpresa() { return nitEmpresa; }
        public void setNitEmpresa(String nitEmpresa) { this.nitEmpresa = nitEmpresa; }

        public String getNombreSucursal() { return nombreSucursal; }
        public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }

        public int getNumeroSucursal() { return numeroSucursal; }
        public void setNumeroSucursal(int numeroSucursal) { this.numeroSucursal = numeroSucursal; }

        public String getNumeroTramite() { return numeroTramite; }
        public void setNumeroTramite(String numeroTramite) { this.numeroTramite = numeroTramite; }

        public int getPuntoVentaCodigo() { return puntoVentaCodigo; }
        public void setPuntoVentaCodigo(int puntoVentaCodigo) { this.puntoVentaCodigo = puntoVentaCodigo; }

        public String getRazonSocial() { return razonSocial; }
        public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

        public String getSubtitulo() { return subtitulo; }
        public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

        public int getSucursalFk() { return sucursalFk; }
        public void setSucursalFk(int sucursalFk) { this.sucursalFk = sucursalFk; }

        public String getTefefonosEmpresa() { return tefefonosEmpresa; }
        public void setTefefonosEmpresa(String tefefonosEmpresa) { this.tefefonosEmpresa = tefefonosEmpresa; }

        public String getTelefonoSucursal() { return telefonoSucursal; }
        public void setTelefonoSucursal(String telefonoSucursal) { this.telefonoSucursal = telefonoSucursal; }

        public String getTipoEmision() { return tipoEmision; }
        public void setTipoEmision(String tipoEmision) { this.tipoEmision = tipoEmision; }

        public String getTipoEmisionLeyenda() { return tipoEmisionLeyenda; }
        public void setTipoEmisionLeyenda(String tipoEmisionLeyenda) { this.tipoEmisionLeyenda = tipoEmisionLeyenda; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
    }

    public static class FacturaDetalle {
        @SerializedName("LineaDetalle")
        private String lineaDetalle;

        @SerializedName("ImporteUnitario")
        private double importeUnitario;

        @SerializedName("Cantidad")
        private int cantidad;

        @SerializedName("ImporteTotal")
        private double importeTotal;

        // Nuevos atributos agregados
        @SerializedName("CatalogoCodigo")
        private int catalogoCodigo;

        @SerializedName("Descuento")
        private double descuento;

        @SerializedName("DescuentoStr")
        private String descuentoStr;

        @SerializedName("ImporteTotalStr")
        private String importeTotalStr;

        @SerializedName("ImporteUnitarioStr")
        private String importeUnitarioStr;

        @SerializedName("PrecioUnitario")
        private double precioUnitario;

        @SerializedName("PrecioUnitarioStr")
        private String precioUnitarioStr;

        @SerializedName("UnidadMedida")
        private String unidadMedida;

        // Getters y Setters
        public String getLineaDetalle() { return lineaDetalle; }
        public void setLineaDetalle(String lineaDetalle) { this.lineaDetalle = lineaDetalle; }

        public double getImporteUnitario() { return importeUnitario; }
        public void setImporteUnitario(double importeUnitario) { this.importeUnitario = importeUnitario; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public double getImporteTotal() { return importeTotal; }
        public void setImporteTotal(double importeTotal) { this.importeTotal = importeTotal; }

        public int getCatalogoCodigo() { return catalogoCodigo; }
        public void setCatalogoCodigo(int catalogoCodigo) { this.catalogoCodigo = catalogoCodigo; }

        public double getDescuento() { return descuento; }
        public void setDescuento(double descuento) { this.descuento = descuento; }

        public String getDescuentoStr() { return descuentoStr; }
        public void setDescuentoStr(String descuentoStr) { this.descuentoStr = descuentoStr; }

        public String getImporteTotalStr() { return importeTotalStr; }
        public void setImporteTotalStr(String importeTotalStr) { this.importeTotalStr = importeTotalStr; }

        public String getImporteUnitarioStr() { return importeUnitarioStr; }
        public void setImporteUnitarioStr(String importeUnitarioStr) { this.importeUnitarioStr = importeUnitarioStr; }

        public double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

        public String getPrecioUnitarioStr() { return precioUnitarioStr; }
        public void setPrecioUnitarioStr(String precioUnitarioStr) { this.precioUnitarioStr = precioUnitarioStr; }

        public String getUnidadMedida() { return unidadMedida; }
        public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    }

    public static class PolizaDetalle {
        @SerializedName("PolDetCodigoCCI")
        private String polDetCodigoCCI;

        @SerializedName("PolDetFechaVigenciaIniFormato")
        private String fechaVigenciaInicio;

        @SerializedName("PolDetFechaVigenciaFinFormato")
        private String fechaVigenciaFin;

        @SerializedName("PolDetPrimaCobrada")
        private double primaCobrada;

        @SerializedName("oClientePN")
        private ClientePN clientePN;

        // Getters y Setters
        public String getPolDetCodigoCCI() { return polDetCodigoCCI; }
        public String getFechaVigenciaInicio() { return fechaVigenciaInicio; }
        public String getFechaVigenciaFin() { return fechaVigenciaFin; }
        public double getPrimaCobrada() { return primaCobrada; }
        public ClientePN getClientePN() { return clientePN; }
    }

    public static class PolizaMaestro {
        @SerializedName("PolMaeCodigoPoliza")
        private String codigoPoliza;

        @SerializedName("PolMaeFechaVigenciaIniFormato")
        private String fechaVigenciaInicio;

        @SerializedName("PolMaeFechaVigenciaFinFormato")
        private String fechaVigenciaFin;

        // Getters y Setters
        public String getCodigoPoliza() { return codigoPoliza; }
        public String getFechaVigenciaInicio() { return fechaVigenciaInicio; }
        public String getFechaVigenciaFin() { return fechaVigenciaFin; }
    }

    public static class Producto {
        @SerializedName("ProNombre")
        private String nombre;

        public String getNombre() { return nombre; }
    }

    public static class ClientePN {
        @SerializedName("PerNombrePrimero")
        private String nombre;

        @SerializedName("PerApellidoPaterno")
        private String apellidoPaterno;

        @SerializedName("PerDocumentoIdentidadNumero")
        private int documentoIdentidad;

        @SerializedName("PerDocumentoIdentidadExtension")
        private String documentoExtension;

        @SerializedName("PerDomicilioParticular")
        private String domicilio;

        @SerializedName("PerTelefonoMovil")
        private String telefonoMovil;

        // Getters y Setters
        public String getNombre() { return nombre; }
        public String getApellidoPaterno() { return apellidoPaterno; }
        public int getDocumentoIdentidad() { return documentoIdentidad; }
        public String getDocumentoExtension() { return documentoExtension; }
        public String getDomicilio() { return domicilio; }
        public String getTelefonoMovil() { return telefonoMovil; }

        public String getNombreCompleto() {
            return nombre + " " + (apellidoPaterno != null ? apellidoPaterno : "");
        }
    }
}
