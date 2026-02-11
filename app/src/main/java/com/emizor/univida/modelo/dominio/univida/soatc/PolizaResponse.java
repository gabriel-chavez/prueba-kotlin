package com.emizor.univida.modelo.dominio.univida.soatc;


import java.util.List;

public class PolizaResponse {
    public int PolDetSecuencial;
    public String contenidoQR;
    public OFacturaMaestro oFacturaMaestro;
    public OPoizaDetalle oPolizaDetalle;
    public OPoizaMaestro oPolizaMaestro;
    public Producto oProducto;

    public static class OFacturaMaestro {
        public String ActividadEconomica;
        public String CodigoCliente;
        public String CodigoControl;
        public String CodigoQR;
        public String DireccionEmpresa;
        public String DireccionSucursal;
        public int DocumentoSectorCodigo;
        public String DocumentoSectorNombre;
        public String EstadoFactura;
        public int EstadoFacturaFk;
        public String FacturaQR;
        public String FaxEmpresa;
        public String FechaEmision;
        public String FechaLimiteEmision;
        public double ImporteBaseCreditoFiscal;
        public String ImporteBaseCreditoFiscalStr;
        public double ImporteDescuento;
        public String ImporteDescuentoStr;
        public String ImporteLiteral;
        public String ImporteNumeral;
        public String ImporteNumeralStr;
        public double ImporteSubtotal;
        public String ImporteSubtotalStr;
        public List<LFacturaDetalle> LFacturaDetalle;
        public String Leyenda;
        public String Lugar;
        public int MaestroFk;
        public String MunicipioDepartamento;
        public String NitCiCliente;
        public String NitEmpresa;
        public String NombreSucursal;
        public String NumeroAutorizacion;
        public int NumeroFactura;
        public int NumeroSucursal;
        public String NumeroTramite;
        public int PuntoVentaCodigo;
        public String RazonSocial;
        public String RazonSocialCliente;
        public String Subtitulo;
        public int SucursalFk;
        public String TefefonosEmpresa;
        public String TelefonoSucursal;
        public String TipoEmision;
        public String TipoEmisionLeyenda;
        public String Titulo;

        public static class LFacturaDetalle {
            public int Cantidad;
            public int CatalogoCodigo;
            public double Descuento;
            public String DescuentoStr;
            public double ImporteTotal;
            public String ImporteTotalStr;
            public double ImporteUnitario;
            public String ImporteUnitarioStr;
            public String LineaDetalle;
            public double PrecioUnitario;
            public String PrecioUnitarioStr;
            public String UnidadMedida;
        }
    }

    public static class OPoizaDetalle {
        public int PolDetCantidadCuotas;
        public String PolDetCodigoCCI;
        public double PolDetDescuento;
        public String PolDetFechaVigenciaFin;
        public String PolDetFechaVigenciaFinFormato;
        public String PolDetFechaVigenciaIni;
        public String PolDetFechaVigenciaIniFormato;
        public double PolDetPorcentajeCuotaInicial;
        public double PolDetPrimaCobrada;
        public double PolDetPrimaProducto;
        public String PolDetTParEmiMedioPagoDescripcion;
        public String PolDetTParGenMonedaDescripcion;
        public List<PolizaDetalleVA> lPolizaDetalleVA;
        public ClientePN oClientePN;
        public DatosGenericos oDatosGenericos;
        public TransaccionIdentificador oETransaccionIdentificador;
        public TransaccionOrigen oETransaccionOrigen;
        public PlanPagosMaestro oPlanPagosMaestro;

        public static class PolizaDetalleVA {
            public String PolDetVATParGenMonedaDescripcion;
            public double PolDetVAValorAsegurado;
        }

        public static class ClientePN {
            public String PerApellidoCasada;
            public String PerApellidoMaterno;
            public String PerApellidoPaterno;
            public String PerCorreoElectronico;
            public String PerDocumentoIdentidadExtension;
            public String PerDocumentoIdentidadNumero;
            public String PerDomicilioComercial;
            public String PerDomicilioParticular;
            public String PerNacimientoFecha;
            public String PerNacimientoFechaFormato;
            public String PerNombrePrimero;
            public String PerNombreSegundo;
            public String PerNumeroIdentificacionTributaria;
            public String PerTParCliDocumentoIdentidadTipoDescripcion;
            public String PerTParCliEstadoCivilDescripcion;
            public String PerTParCliGeneroDescripcion;
            public String PerTParCliProfesionDescripcion;
            public String PerTParCliSituacionLaboralDescripcion;
            public String PerTParGenActividadEconomicaDescripcion;
            public String PerTParGenActividadEconomicaSecDescripcion;
            public String PerTParGenDepartamentoDescripcionDocumentoIdentidad;
            public String PerTParGenDepartamentoDescripcionNacimiento;
            public String PerTParGenNivelIngresosDescripcion;
            public String PerTParGenPaisDescripcionNacionalidad;
            public String PerTParGenPaisDescripcionResidencia;
            public String PerTelefonoFijo;
            public String PerTelefonoMovil;
            public int PerTrabajoAnioIngreso;
            public String PerTrabajoCargo;
            public String PerTrabajoLugar;
            public Object oConyuge;
        }

        public static class DatosGenericos {
            public String DatGenFecha;
            public String DatGenFechaFormato;
            public int DatGenSecuencial;
            public String DatGenTParGenDepartamentoAbreviacion;
            public String DatGenTParGenDepartamentoDescripcion;
            public int DatGenTParGenDepartamentoFk;
            public int DatGenTSucursalFk;
        }

        public static class TransaccionIdentificador {
            public String TraIdeLlaveA;
            public String TraIdeLlaveB;
            public String TraIdeLlaveC;
        }

        public static class TransaccionOrigen {
            public String TraOriAgencia;
            public String TraOriCajero;
            public int TraOriCanal;
            public String TraOriEntidad;
            public int TraOriIntermediario;
            public String TraOriSucursal;
        }

        public static class PlanPagosMaestro {
            public int PlaPagMaeCantidadCuotas;
            public double PlaPagMaeImporte;
            public String PlaPagMaeTParCobPlanPagosComprobanteTipoDescripcion;
            public List<PlanPagosDetalle> lPlanPagosDetalle;

            public static class PlanPagosDetalle {
                public int PlaPagDetCuotaNro;
                public String PlaPagDetFechaPago;
                public double PlaPagDetImporte;
                public double PlaPagDetPorcentaje;
                public int PlaPagDetSecuencial;
            }
        }
    }

    public static class OPoizaMaestro {
        public String PolMaeCodigoPoliza;
        public String PolMaeFechaVigenciaFin;
        public String PolMaeFechaVigenciaFinFormato;
        public String PolMaeFechaVigenciaIni;
        public String PolMaeFechaVigenciaIniFormato;
        public int PolMaeSecuencial;
        public EmiPolizaObtenerResponse.ClientePN oClientePN;
        public DatosGenericos oDatosGenericos;
      //  public OPoizaDetalle.TransaccionOrigen oETransaccionOrigen;

        // Similar a la clase oPolizaDetalle, puedes agregar otras clases internas según sea necesario
    }

    public static class Producto {
        public String ProNombre;
    }
}