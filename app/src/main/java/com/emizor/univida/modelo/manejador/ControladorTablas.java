package com.emizor.univida.modelo.manejador;

/**
 * Created by denis on 2/2/18.
 */

public interface ControladorTablas {

    //NOMBRES DE LAS TABLAS
    String TABLA_USUARIO = "usuario";

    String TABLA_DEPARTAMENTOS = "departamento";
    String TABLA_GESTION = "gestion";
    String TABLA_TIPO_USO ="tipo_uso";
    String TABLA_TIPO_VEHICULO = "tipo_vehiculo";
    String TABLA_MEDIO_PAGO = "medio_pago";
    String TABLA_BANCO = "banco";
    String TABLA_TIPO_PLACA = "tipo_placa";
    String TABLA_TIPO_DOCUMENTO_IDENTIDAD = "tipo_documento_identidad";


    // DEFINIMOS LA ESTRUCTURA DE LAS TABLAS
    String CREAR_TABLA_USUARIO = "CREATE TABLE " + TABLA_USUARIO + " (id_usuario INTEGER PRIMARY KEY, nombre_usuario TEXT, " +
            "ap_usuario TEXT, ficha_usuario TEXT, protocolo_usuario TEXT DEFAULT 'http://', " +
            "fecha_servicio_usuario TEXT, fecha_factura_usuario TEXT, datos_usuario TEXT, datos_encriptado_usuario TEXT, " +
            "usuario_usuario TEXT, estado_usuario INTEGER DEFAULT 0, fecha_ultimo_uso TEXT);";
    String CREAR_TABLA_DEPARTAMENTOS = "CREATE TABLE " + TABLA_DEPARTAMENTOS + " (codigo_departamento TEXT PRIMARY KEY NOT NULL, " +
            "descripcion_departamento TEXT NOT NULL);";
    String CREAR_TABLA_GESTION = "CREATE TABLE " + TABLA_GESTION + " (codigo_gestion INTEGER PRIMARY KEY NOT NULL, " +
            "prioridad_venta INTEGER NOT NULL DEFAULT 0, venta_desde TEXT, venta_hasta TEXT);";
    String CREAR_TABLA_TIPO_USO ="CREATE TABLE " + TABLA_TIPO_USO + " (codigo_tipo_uso INTEGER PRIMARY KEY NOT NULL, " +
            "descripcion_tipo_uso TEXT NOT NULL)";
    String CREAR_TABLA_TIPO_VEHICULO = "CREATE TABLE " + TABLA_TIPO_VEHICULO + " (codigo_tipo_vehiculo INTEGER PRIMARY KEY NOT NULL, " +
            "descripcion_tipo_vehiculo TEXT NOT NULL);";
    String CREAR_TABLA_MEDIO_PAGO = "CREATE TABLE " + TABLA_MEDIO_PAGO + " (codigo_medio_pago INTEGER PRIMARY KEY NOT NULL, " +
            "descripcion_medio_pago TEXT NOT NULL, secuencial_dato TEXT, datos_completos TEXT);";
    String CREAR_TABLA_BANCO = "CREATE TABLE " + TABLA_BANCO + " (codigo_banco TEXT PRIMARY KEY NOT NULL, " +
            "descripcion_banco TEXT NOT NULL);";
    String CREAR_TABLA_TIPO_PLACA = "CREATE TABLE " + TABLA_TIPO_PLACA + " (codigo_tipo_placa TEXT PRIMARY KEY NOT NULL, " +
            "descripcion_tipo_placa TEXT NOT NULL, habilitado_tipo_placa INTEGER DEFAULT 0);";
    String CREAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD = "CREATE TABLE " + TABLA_TIPO_DOCUMENTO_IDENTIDAD + " (codigo_tipo_documento_identidad TEXT PRIMARY KEY NOT NULL, " +
            "descripcion_tipo_documento_identidad TEXT NOT NULL, requiere_complemento INTEGER DEFAULT 0);";

    // DEFINIMOS LAS CONSULTAS PARA ELIMINAR DE TABLAS
    String ELIMINAR_TABLA_USUARIO = "DROP TABLE IF EXISTS " + TABLA_USUARIO;
    String ELIMINAR_TABLA_DEPARTAMENTOS = "DROP TABLE IF EXISTS " + TABLA_DEPARTAMENTOS;
    String ELIMINAR_TABLA_GESTION = "DROP TABLE IF EXISTS " + TABLA_GESTION;
    String ELIMINAR_TABLA_TIPO_USO ="DROP TABLE IF EXISTS " + TABLA_TIPO_USO;
    String ELIMINAR_TABLA_TIPO_VEHICULO = "DROP TABLE IF EXISTS " + TABLA_TIPO_VEHICULO;
    String ELIMINAR_TABLA_MEDIO_PAGO = "DROP TABLE IF EXISTS " + TABLA_MEDIO_PAGO;
    String ELIMINAR_TABLA_BANCO = "DROP TABLE IF EXISTS " + TABLA_BANCO;
    String ELIMINAR_TABLA_TIPO_PLACA = "DROP TABLE IF EXISTS " + TABLA_TIPO_PLACA;
    String ELIMINAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD = "DROP TABLE IF EXISTS " + TABLA_TIPO_DOCUMENTO_IDENTIDAD;

}
