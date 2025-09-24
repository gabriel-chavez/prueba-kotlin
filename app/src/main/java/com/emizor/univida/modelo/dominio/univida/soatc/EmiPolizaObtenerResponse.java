package com.emizor.univida.modelo.dominio.univida.soatc;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmiPolizaObtenerResponse {
    @SerializedName("PolDetSecuencial")
    private int polDetSecuencial;

    @SerializedName("oFacturaMaestro")
    private FacturaMaestro facturaMaestro;

    @SerializedName("oPolizaDetalle")
    private PolizaDetalle polizaDetalle;

    @SerializedName("oPolizaMaestro")
    private PolizaMaestro polizaMaestro;

    @SerializedName("oProducto")
    private Producto producto;

    // Getters y Setters
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

        // Getters y Setters
        public int getNumeroFactura() { return numeroFactura; }
        public String getCodigoControl() { return codigoControl; }
        public String getCodigoQR() { return codigoQR; }
        public String getFechaEmision() { return fechaEmision; }
        public double getImporteNumeral() { return importeNumeral; }
        public String getImporteLiteral() { return importeLiteral; }
        public String getNitCiCliente() { return nitCiCliente; }
        public String getRazonSocialCliente() { return razonSocialCliente; }
        public List<FacturaDetalle> getFacturaDetalle() { return facturaDetalle; }
        public String getNumeroAutorizacion() { return numeroAutorizacion; }
        public String getEstadoFactura() { return estadoFactura; }
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

        // Getters y Setters
        public String getLineaDetalle() { return lineaDetalle; }
        public double getImporteUnitario() { return importeUnitario; }
        public int getCantidad() { return cantidad; }
        public double getImporteTotal() { return importeTotal; }
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
