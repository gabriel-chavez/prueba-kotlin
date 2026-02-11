package com.emizor.univida.modelo.dominio.univida.soatc;

import java.util.List;

public class ListarRccResponse {
    public int RepCieCobCantidadComprobante;
    public int RepCieCobCantidadComprobanteAnulados;
    public int RepCieCobCantidadComprobanteRevertidos;
    public int RepCieCobCantidadComprobanteValidos;
    public String RepCieCobComprobanteFecha;
    public String RepCieCobComprobanteFechaFormato;
    public boolean RepCieCobEfectivizado;
    public String RepCieCobEmpleadoNombreCompleto;
    public String RepCieCobEstadoDescripcion;
    public int RepCieCobEstadoFk;
    public double RepCieCobFormularioImporte;
    public int RepCieCobFormularioNumero;
    public int RepCieCobIntermediario;
    public String RepCieCobIntermediarioDescripcion;
    public List<ComprobanteDatos> RepCieCobJsonDatosComprobante;
    public String RepCieCobNombreSupervisor;
    public int RepCieCobSecuencial;
    public String RepCieCobTDatosGenericosFecha;
    public String RepCieCobTDatosGenericosFechaFormato;
    public int RepCieCobTDatosGenericosFk;
    public int RepCieCobTDatosGenericosTParGenDepartamentoFk;
    public int RepCieCobTDatosGenericosTSucursalFk;
    public String RepCieCobTParCobroCanalDescripcion;
    public int RepCieCobTParCobroCanalFk;
    public String RepCieCobTSucursalNombre;
    public int RepCieCobTUsuarioDatosFkCobrador;
    public int RepCieCobTUsuarioDatosFkSupervisor;
    public String RepCieCobUsuario;
    public String RepCieCobUsuarioSupervisor;
}
