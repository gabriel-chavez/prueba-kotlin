package com.emizor.univida.modelo.dominio.univida.soatc;

import java.util.List;

public class ListarCobrosResponse {
    public int RccCantidadComprobante;
    public int RccCantidadComprobanteAnulados;
    public int RccCantidadComprobanteRevertidos;
    public int RccCantidadComprobanteValidos;
    public double RccFormularioImporte;
    public List<ComprobanteDatos> lComprobanteDatos;
}
