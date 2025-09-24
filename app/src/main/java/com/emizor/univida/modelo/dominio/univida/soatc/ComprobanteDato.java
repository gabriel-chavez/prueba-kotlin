package com.emizor.univida.modelo.dominio.univida.soatc;

public class ComprobanteDato {
    public double PlaPagComComprobanteImporte;
    public String PlaPagComDepartamentoDescripcion;
    public int PlaPagComDepartamentoFk;
    public String PlaPagComEstadoDescripcion;
    public int PlaPagComEstadoFk;
    public String PlaPagComFecha;              // "2025-10-06T00:00:00"
    public String PlaPagComFechaFormato;       // "06/10/2025"
    public String PlaPagComLineaDetalle;       // "PÓLIZA: ... PARA 1 ASEGURADO"
    public long PlaPagComSecuencial;           // posible nro de factura interno
    public long PlaPagComTComprobanteExternoFk;
    public int PlaPagComTComprobanteExternoNumero;
    public int PlaPagComTDatosGenmericosFk;
    public String PlaPagComTParCobPlanPagosComprobanteTipoDescripcion; // "FACTURA"
    public int PlaPagComTParCobPlanPagosComprobanteTipoFk;
    public String PlaPagComTParCobroCanalDescripcion; // "Oficina"
    public int PlaPagComTParCobroCanalFk;
    public int PlaPagComTReporteCierreCobrosFk;
    public int PlaPagComTSucursalFk;
    public int PlaPagComTUsuarioDatosFkCobrador;
    public String PlaPagComTUsuarioDatosFkCobradorNombreCompleto;
    public String PlaPagComTUsuarioDatosFkCobradorUsuario;
    public String TParBroBrokerDescripcion;
    public int TParBroBrokerFk;
}
