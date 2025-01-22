
package com.emizor.univida.rest;
/**
 * Created by posemizor on 28-11-17.
 */

public class DatosConexion {

    //SERVIDOR PRUEBAS yerson
//    public static final String SERVIDORUNIVIDA = "https://c80d-177-222-112-80.ngrok.io";
    //SERVIDOR PRUEBAS univida test
//    public static final String SERVIDORUNIVIDA = "https://ws2-ws-pos.univida.bo:7046";
// SERVIDOR PRUEBAS
//    public static final String SERVIDORUNIVIDA = "http://18.208.110.169:9000";
//// SERVIDOR PRUEBAS 2
//    public static final String SERVIDORUNIVIDA = "https://ws8-pos-univida.univida.bo:8001";
// SERVIDOR PRUEBAS 3
///////////=>>>    public static final String SERVIDORUNIVIDA = "https://ws8-pos-univida.univida.bo:7001";
    public static final String SERVIDORUNIVIDA = "https://app-desarrollo.univida.bo:8001";
// SERVIDOR PRUEBAS 3 QA
//    public static final String SERVIDORUNIVIDA = "https://ws7-pos-univida.univida.bo:7001";
    //SERVIDOR PRODUCCION
//    public static final String SERVIDORUNIVIDA = "https://unividapos.univida.bo:6443";

    //SEGURIDAD
    public static final String URL_UNIVIDA_SEGURIDAD_AUNTENTICACION = "/seguridad/autenticacion";
    public static final String URL_UNIVIDA_SEGURIDAD_CAMBIAR_CLAVE = "/seguridad/cambiar-contrasena";

    //PARAMETRICAS
    public static final String URL_UNIVIDA_PARAMETRICAS_TIPO_VEHICULO = "/parametricas/tipo-vehiculo";
    public static final String URL_UNIVIDA_PARAMETRICAS_USO_VEHICULO = "/parametricas/vehiculo-usos";
    public static final String URL_UNIVIDA_PARAMETRICAS_DEPARTAMENTOS = "/parametricas/departamentos";
    public static final String URL_UNIVIDA_PARAMETRICAS_GESTIONES = "/parametricas/gestiones-habilitadas";
    public static final String URL_UNIVIDA_PARAMETRICAS_MEDIO_PAGO = "/parametricas/medio-pago";
    public static final String URL_UNIVIDA_PARAMETRICAS_BANCOS = "/parametricas/bancos";
    public static final String URL_UNIVIDA_PARAMETRICAS_TIPO_PLACAS = "/parametricas/placas-tipo";
    public static final String URL_UNIVIDA_PARAMETRICAS_TIPO_DOC_IDENTIDAD = "/parametricas/tipo-doc-identidad";


    //VENTAS
    public static final String URL_UNIVIDA_VENTAS_OBTENER_PRIMA = "/ventas/obtener-prima";
    public static final String URL_UNIVIDA_VENTAS_LISTAR = "/ventas/listar";
    public static final String URL_UNIVIDA_VENTAS_OBTENER = "/ventas/obtener";
    public static final String URL_UNIVIDA_VENTAS_SOLICITAR_REVERTIR = "/ventas/solicitud-revertir";
    public static final String URL_UNIVIDA_VENTAS_VALIDARVENDIBLE_OBTENER_DATOS_INTER = "/ventas/validar-vendible-y-obtener-datos-inter";
    public static final String URL_UNIVIDA_VENTAS_EFECTIVIZAR_FACTURA_CICLOS_INTER = "/ventas/efectivizar-factura-ciclos-inter";

    //RCV
    public static final String URL_UNIVIDA_RCV_EFECTIVIZAR = "/reporte-cierre-ventas/efectivizar";
    public static final String URL_UNIVIDA_RCV_LISTAR_VENTAS = "/reporte-cierre-ventas/listar-ventas";
    public static final String URL_UNIVIDA_RCV_OBTENER = "/reporte-cierre-ventas/obtener";
    public static final String URL_UNIVIDA_RCV_REMITIR = "/reporte-cierre-ventas/remitir";
    public static final String URL_UNIVIDA_RCV_LISTAR = "/reporte-cierre-ventas/listar";
    public static final String URL_UNIVIDA_RCV_REVERTIR = "/reporte-cierre-ventas/revertir";
}
