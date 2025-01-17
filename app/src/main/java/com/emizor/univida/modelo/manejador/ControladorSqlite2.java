package com.emizor.univida.modelo.manejador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.emizor.univida.modelo.dominio.univida.parametricas.Banco;
import com.emizor.univida.modelo.dominio.univida.parametricas.MedioPago;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoDocumentoIdentidad;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoPlaca;
import com.google.gson.Gson;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.seguridad.DatosUsuario;
import com.emizor.univida.modelo.dominio.univida.parametricas.Departamento;
import com.emizor.univida.modelo.dominio.univida.parametricas.Gestion;
import com.emizor.univida.modelo.dominio.univida.parametricas.TipoVehiculo;
import com.emizor.univida.modelo.dominio.univida.parametricas.UsoVehiculo;
import com.emizor.univida.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class ControladorSqlite2 extends SQLiteOpenHelper implements ControladorTablas {

    private static SQLiteDatabase DATABASE;
    private static final int VERSIONDB = 1126;
    private final String TAG = "BDEMIZOR";


    public ControladorSqlite2(Context applicationcontext) {
        super(applicationcontext, "emizor.db", null, VERSIONDB);

        LogUtils.i(TAG, "Constructor version" + VERSIONDB);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        LogUtils.i(TAG, "onCreate");

        ////////////////////////////////////////////////////

        sqLiteDatabase.execSQL(CREAR_TABLA_USUARIO);

        sqLiteDatabase.execSQL(CREAR_TABLA_DEPARTAMENTOS);
        sqLiteDatabase.execSQL(CREAR_TABLA_GESTION);
        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_USO);
        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_VEHICULO);
        sqLiteDatabase.execSQL(CREAR_TABLA_MEDIO_PAGO);
        sqLiteDatabase.execSQL(CREAR_TABLA_BANCO);
        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_PLACA);
        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        LogUtils.i(TAG, "onUpgrade version 1 " + i + " version2 " + i1);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_USUARIO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_DEPARTAMENTOS);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_GESTION);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_USO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_VEHICULO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_MEDIO_PAGO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_BANCO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_PLACA);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD);

        onCreate(sqLiteDatabase);

    }

    public void eliminarTodoDatoTabla(String nombreTabla) {
        LogUtils.i(TAG, "eliminar todo");
        String query = null;

        switch (nombreTabla) {
            case TABLA_USUARIO:
                query = "DELETE from " + nombreTabla + " where id_usuario > -1;";
                break;
            case TABLA_DEPARTAMENTOS:
                query = "DELETE from " + nombreTabla + " where codigo_departamento <> '1';";
                break;
            case TABLA_GESTION:
                query = "DELETE from " + nombreTabla + " where codigo_gestion <> '1';";
                break;
            case TABLA_TIPO_USO:
                query = "DELETE from " + nombreTabla + " where codigo_tipo_uso > -1;";
                break;
            case TABLA_TIPO_VEHICULO:
                query = "DELETE from " + nombreTabla + " where codigo_tipo_vehiculo > -1;";
                break;
            case TABLA_MEDIO_PAGO:
                query = "DELETE from " + nombreTabla + " where codigo_medio_pago > -1;";
                break;
            case TABLA_BANCO:
                query = "DELETE from " + nombreTabla + " where codigo_banco > -1;";
                break;
            case TABLA_TIPO_PLACA:
                query = "DELETE from " + nombreTabla + " where codigo_tipo_placa > -1;";
                break;
            case TABLA_TIPO_DOCUMENTO_IDENTIDAD:
                query = "DELETE from " + nombreTabla + " where codigo_tipo_documento_identidad > -1;";
                break;
        }

        if (query != null) {
            abrirConexion();
            DATABASE.execSQL(query);
            DATABASE.close();

        }
    }

    public void crearTablasPrincipal() {

        Log.i(TAG, "crear tablas principal");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL(CREAR_TABLA_USUARIO);

        sqLiteDatabase.execSQL(CREAR_TABLA_DEPARTAMENTOS);

        sqLiteDatabase.execSQL(CREAR_TABLA_GESTION);

        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_USO);

        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_VEHICULO);

        sqLiteDatabase.execSQL(CREAR_TABLA_MEDIO_PAGO);

        sqLiteDatabase.execSQL(CREAR_TABLA_BANCO);

        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_PLACA);

        sqLiteDatabase.execSQL(CREAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD);
    }

    public void eliminarTodoPrincipal() {
        LogUtils.i(TAG, "eliminar todo principal");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_USUARIO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_DEPARTAMENTOS);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_GESTION);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_USO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_VEHICULO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_MEDIO_PAGO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_BANCO);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_PLACA);

        sqLiteDatabase.execSQL(ELIMINAR_TABLA_TIPO_DOCUMENTO_IDENTIDAD);

    }


    private void abrirConexion() {
        try {

            if (DATABASE == null || !(DATABASE.isOpen())) {
                DATABASE = this.getWritableDatabase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {

            if (DATABASE != null) {
                if (DATABASE.isOpen()) {
                    DATABASE.close();
                    try {
                        this.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized List<ContentValues> obtenerRegistro(String consulta) {

        List<ContentValues> listaContentValues = new ArrayList<>();
        Cursor cursorAux = null;
        try {

            abrirConexion();

            cursorAux = DATABASE.rawQuery(consulta, null);

            if (cursorAux != null){
                if (cursorAux.moveToFirst()){
                    do{
                        ContentValues contentValues = new ContentValues();

                        DatabaseUtils.cursorRowToContentValues(cursorAux, contentValues);

                        listaContentValues.add(contentValues);
                    }while(cursorAux.moveToNext());
                }
            }


        } catch (Exception sqlex) {
            sqlex.printStackTrace();
            cerrarConexion();
        }finally {
            if (cursorAux != null){
                cursorAux.close();
            }
        }

        return listaContentValues;
    }

    public void insertarUsuario(User usuario) {
        try {

            if (obtenerUsuario() != null){
                eliminarTodoDatoTabla(TABLA_USUARIO);
            }

            ContentValues values = new ContentValues();
            values.put("id_usuario", usuario.getId());
            values.put("nombre_usuario", usuario.getFirstName());
            values.put("ap_usuario", usuario.getLastName());
            values.put("ficha_usuario", usuario.getTokenAuth());
            values.put("usuario_usuario", usuario.getUsername());

            String jsonDatosUsuario = new Gson().toJson(usuario.getDatosUsuario());

            values.put("datos_usuario", jsonDatosUsuario);
            values.put("estado_usuario", usuario.getEstado());


            LogUtils.i("DATABASE", "insertar usuario " + values.toString());
            abrirConexion();
            DATABASE.insert(TABLA_USUARIO, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void cambiarEstado(int estado) {
        try {

            ContentValues values = new ContentValues();
            values.put("estado_usuario", estado);

            abrirConexion();
            DATABASE.update("usuario", values, "id_usuario>?", new String[]{"0"});

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public User obtenerUsuario() {


        String selectQuery = "SELECT * FROM " + TABLA_USUARIO + " ORDER BY upper(nombre_usuario) DESC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        User usuario = null;

        if (lista != null) {
            try {

                if (lista.size() > 0) {

                    ContentValues contentValues = lista.get(0);

                    usuario = new User();
                    usuario.setAccountId(contentValues.getAsString("id_usuario"));
                    usuario.setFirstName(contentValues.getAsString("nombre_usuario"));
                    usuario.setLastName(contentValues.getAsString("ap_usuario"));
                    usuario.setTokenAuth(contentValues.getAsString("ficha_usuario"));
                    usuario.setEstado(contentValues.getAsInteger("estado_usuario"));
                    try{

                        DatosUsuario datosUsuario = new Gson().fromJson(contentValues.getAsString("datos_usuario"), DatosUsuario.class);

                        usuario.setDatosUsuario(datosUsuario);

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    usuario.setUsername(contentValues.getAsString("usuario_usuario"));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                usuario = null;
            }
        }

        return usuario;
    }

    public void guardarFechaUso(String fechaUso){

        try{

            ContentValues contentValues = new ContentValues();

            contentValues.put("fecha_ultimo_uso", fechaUso);

            abrirConexion();
            DATABASE.update(TABLA_USUARIO, contentValues, "id_usuario>?", new String[]{"-1"});

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    // TABLA DEPARTAMENTOS

    public void insertarDepartamento(Departamento departamento) {
        abrirConexion();

        try {

            if (obtenerDepartamento(departamento.getCodigo()) != null) {
                actualizarDepartamento(departamento);
            } else {
                ContentValues values = new ContentValues();

                values.put("codigo_departamento", departamento.getCodigo());
                values.put("descripcion_departamento", departamento.getDescripcion());

                //LogUtils.i("DATABASE", "insertar categoria " + values.toString());

                DATABASE.insert(TABLA_DEPARTAMENTOS, null, values);
            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarDepartamento(Departamento departamento) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("descripcion_departamento", departamento.getDescripcion());

            DATABASE.update(TABLA_DEPARTAMENTOS, values, "codigo_departamento=?", new String[]{String.valueOf(departamento.getDescripcion())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public Departamento obtenerDepartamento(String codigoDepartamento) {

        String selectQuery = "SELECT * FROM " + TABLA_DEPARTAMENTOS + " where codigo_departamento='" + codigoDepartamento + "'";
        //LogUtils.i("Query", selectQuery);

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        Departamento departamento = null;

        try {

            if (! lista.isEmpty()) {

                departamento = new Departamento();
                departamento.setCodigo(lista.get(0).getAsString("codigo_departamento"));
                departamento.setDescripcion(lista.get(0).getAsString("descripcion_departamento"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return departamento;

    }

    public List<Departamento> obtenerDepartamentos() {

        String selectQuery = "SELECT * FROM " + TABLA_DEPARTAMENTOS + " ORDER BY upper(descripcion_departamento) ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<Departamento> listDepartamento = new ArrayList<>();
        Departamento departamento1 = new Departamento();
        departamento1.setCodigo("-1");
        departamento1.setDescripcion("-- Seleccione un departamento --");
        listDepartamento.add(departamento1);

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        Departamento departamento = new Departamento();
                        departamento.setCodigo(contentValues.getAsString("codigo_departamento"));
                        departamento.setDescripcion(contentValues.getAsString("descripcion_departamento"));

                        listDepartamento.add(departamento);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listDepartamento;
    }

    //FIN TABLA DEPARTAMENTOS

    // TABLA GESTION

    public void insertarGestion(Gestion gestion) {
        abrirConexion();

        try {

            if (obtenerGestion(gestion.getSecuencial()) != null) {
                actualizarGestion(gestion);
            } else {
                ContentValues values = new ContentValues();

                values.put("codigo_gestion", gestion.getSecuencial());
                values.put("prioridad_venta", gestion.getPrioridadVenta());
                values.put("venta_desde", gestion.getVentaDesde());
                values.put("venta_hasta", gestion.getVentaHasta());

                //LogUtils.i("DATABASE", "insertar categoria " + values.toString());

                DATABASE.insert(TABLA_GESTION, null, values);
            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarGestion(Gestion gestion) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("prioridad_venta", gestion.getPrioridadVenta());
            values.put("venta_desde", gestion.getVentaDesde());
            values.put("venta_hasta", gestion.getVentaHasta());

            DATABASE.update(TABLA_GESTION, values, "codigo_gestion=?", new String[]{String.valueOf(gestion.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    private Gestion obtenerGestion(Integer codigoGestion) {

        String selectQuery = "SELECT * FROM " + TABLA_GESTION + " where codigo_gestion=" + codigoGestion;
        //LogUtils.i("Query", selectQuery);

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        Gestion gestion = null;

        try {

            if (! lista.isEmpty()) {

                gestion = new Gestion();
                ContentValues contentValues = lista.get(0);
                gestion.setSecuencial(contentValues.getAsInteger("codigo_gestion"));
                gestion.setPrioridadVenta(contentValues.getAsInteger("prioridad_venta"));
                gestion.setVentaDesde(contentValues.getAsString("venta_desde"));
                gestion.setVentaHasta(contentValues.getAsString("venta_hasta"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return gestion;

    }

    public List<Gestion> obtenerGestiones() {

        String selectQuery = "SELECT * FROM " + TABLA_GESTION + " ORDER BY prioridad_venta,codigo_gestion ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<Gestion> listGestion = new ArrayList<>();
        Gestion gestion1 = new Gestion();
        gestion1.setSecuencial(Integer.valueOf("-1"));

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        Gestion gestion = new Gestion();
                        gestion.setSecuencial(contentValues.getAsInteger("codigo_gestion"));
                        gestion.setPrioridadVenta(contentValues.getAsInteger("prioridad_venta"));
                        gestion.setVentaDesde(contentValues.getAsString("venta_desde"));
                        gestion.setVentaHasta(contentValues.getAsString("venta_hasta"));

                        listGestion.add(gestion);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (listGestion.size() > 1){
            listGestion.add(0,gestion1);
        }

        return listGestion;
    }

    //FIN TABLA GESTION

    // TABLA TIPO USO

    public void insertarTipoUso(UsoVehiculo usoVehiculo) {
        abrirConexion();

        try {

            if (obtenerTipoUso(usoVehiculo.getSecuencial()) != null) {
                actualizarTipoUso(usoVehiculo);
            } else {
                ContentValues values = new ContentValues();

                values.put("codigo_tipo_uso", usoVehiculo.getSecuencial());
                values.put("descripcion_tipo_uso", usoVehiculo.getDescripcion());

                //LogUtils.i("DATABASE", "insertar categoria " + values.toString());

                DATABASE.insert(TABLA_TIPO_USO, null, values);
            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarTipoUso(UsoVehiculo usoVehiculo) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("descripcion_tipo_uso", usoVehiculo.getDescripcion());

            DATABASE.update(TABLA_TIPO_USO, values, "codigo_tipo_uso=?", new String[]{String.valueOf(usoVehiculo.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public UsoVehiculo obtenerTipoUso(Integer codigoTipoUso) {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_USO + " where codigo_tipo_uso=" + codigoTipoUso;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        UsoVehiculo usoVehiculo = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                usoVehiculo = new UsoVehiculo();
                usoVehiculo.setSecuencial(contentValues.getAsInteger("codigo_tipo_uso"));
                usoVehiculo.setDescripcion(contentValues.getAsString("descripcion_tipo_uso"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return usoVehiculo;

    }

    public List<UsoVehiculo> obtenerTipoUsos() {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_USO + " ORDER BY upper(codigo_tipo_uso) ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<UsoVehiculo> listTipoUso = new ArrayList<>();

        UsoVehiculo usoVehiculo1 = new UsoVehiculo();
        usoVehiculo1.setSecuencial(-1);
        usoVehiculo1.setDescripcion("-- Seleccione tipo de uso --");

        listTipoUso.add(usoVehiculo1);

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        UsoVehiculo usoVehiculo = new UsoVehiculo();
                        usoVehiculo.setSecuencial(contentValues.getAsInteger("codigo_tipo_uso"));
                        usoVehiculo.setDescripcion(contentValues.getAsString("descripcion_tipo_uso"));

                        listTipoUso.add(usoVehiculo);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listTipoUso;
    }

    //FIN TABLA TIPO USO

    // TABLA TIPO VEHICULO

    public void insertarTipoVehiculo(TipoVehiculo tipoVehiculo) {
        abrirConexion();

        try {

            if (obtenerTipoVehiculo(tipoVehiculo.getSecuencial()) != null) {
                actualizarTipoVehiculo(tipoVehiculo);
            } else {
                ContentValues values = new ContentValues();

                values.put("codigo_tipo_vehiculo", tipoVehiculo.getSecuencial());
                values.put("descripcion_tipo_vehiculo", tipoVehiculo.getDescripcion());

                DATABASE.insert(TABLA_TIPO_VEHICULO, null, values);
            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarTipoVehiculo(TipoVehiculo tipoVehiculo) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("descripcion_tipo_vehiculo", tipoVehiculo.getDescripcion());

            DATABASE.update(TABLA_TIPO_VEHICULO, values, "codigo_tipo_vehiculo=?", new String[]{String.valueOf(tipoVehiculo.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public TipoVehiculo obtenerTipoVehiculo(Integer codigoTipoVehiculo) {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_VEHICULO + " where codigo_tipo_vehiculo=" + codigoTipoVehiculo;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        TipoVehiculo tipoVehiculo = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                tipoVehiculo = new TipoVehiculo();
                tipoVehiculo.setSecuencial(contentValues.getAsInteger("codigo_tipo_vehiculo"));
                tipoVehiculo.setDescripcion(contentValues.getAsString("descripcion_tipo_vehiculo"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tipoVehiculo;

    }

    public List<TipoVehiculo> obtenerTipoVehiculos() {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_VEHICULO + " ORDER BY upper(codigo_tipo_vehiculo) ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<TipoVehiculo> listTipoVehiculo = new ArrayList<>();

        TipoVehiculo tipoVehiculo1 = new TipoVehiculo();
        tipoVehiculo1.setSecuencial(-1);
        tipoVehiculo1.setDescripcion("-- Seleccione tipo de vehiculo --");

        listTipoVehiculo.add(tipoVehiculo1);

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        TipoVehiculo tipoVehiculo = new TipoVehiculo();
                        tipoVehiculo.setSecuencial(contentValues.getAsInteger("codigo_tipo_vehiculo"));
                        tipoVehiculo.setDescripcion(contentValues.getAsString("descripcion_tipo_vehiculo"));

                        listTipoVehiculo.add(tipoVehiculo);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listTipoVehiculo;
    }

    //FIN TABLA TIPO USO

    // TABLA MEDIO PAGO

    public void insertarMedioPago(MedioPago medioPago) {
        abrirConexion();

        try {

            if (obtenerMedioPago(medioPago.getSecuencial()) != null) {

                actualizarMedioPago(medioPago);

            } else {

                ContentValues values = new ContentValues();

                values.put("codigo_medio_pago", medioPago.getSecuencial());
                values.put("descripcion_medio_pago", medioPago.getDescripcion());
                values.put("secuencial_dato", medioPago.getSecuencialDato());
                values.put("datos_completos", medioPago.getDatosCompletos());

                DATABASE.insert(TABLA_MEDIO_PAGO, null, values);

            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarMedioPago(MedioPago medioPago) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("descripcion_medio_pago", medioPago.getDescripcion());
            values.put("secuencial_dato", medioPago.getSecuencialDato());
            values.put("datos_completos", medioPago.getDatosCompletos());

            DATABASE.update(TABLA_MEDIO_PAGO, values, "codigo_medio_pago=?", new String[]{String.valueOf(medioPago.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public MedioPago obtenerMedioPago(Integer codigoMedioPago) {

        String selectQuery = "SELECT * FROM " + TABLA_MEDIO_PAGO + " where codigo_medio_pago=" + codigoMedioPago;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        MedioPago medioPago = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                medioPago = new MedioPago();
                medioPago.setSecuencial(contentValues.getAsInteger("codigo_medio_pago"));
                medioPago.setDescripcion(contentValues.getAsString("descripcion_medio_pago"));
                medioPago.setSecuencialDato(contentValues.getAsString("secuencial_dato"));
                medioPago.setDatosCompletos(contentValues.getAsString("datos_completos"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return medioPago;

    }

    public List<MedioPago> obtenerMedioPagos() {

        String selectQuery = "SELECT * FROM " + TABLA_MEDIO_PAGO + " ORDER BY upper(codigo_medio_pago) ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<MedioPago> listMedioPago = new ArrayList<>();

        MedioPago medioPago1 = new MedioPago();
        medioPago1.setSecuencial(-1);
        medioPago1.setDescripcion("-- Seleccione un medio de pago --");

        listMedioPago.add(medioPago1);

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        MedioPago medioPago = new MedioPago();
                        medioPago.setSecuencial(contentValues.getAsInteger("codigo_medio_pago"));
                        medioPago.setDescripcion(contentValues.getAsString("descripcion_medio_pago"));
                        medioPago.setSecuencialDato(contentValues.getAsString("secuencial_dato"));
                        medioPago.setDatosCompletos(contentValues.getAsString("datos_completos"));

                        listMedioPago.add(medioPago);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listMedioPago;
    }

    //FIN TABLA MEDIO PAGO

    // TABLA BANCO

    public void insertarBanco(Banco banco) {
        abrirConexion();

        try {

            if (obtenerBanco(banco.getSecuencial()) != null) {

                actualizarBanco(banco);

            } else {

                ContentValues values = new ContentValues();

                values.put("codigo_banco", banco.getSecuencial());
                if (banco.getDescripcion().trim().length() > 0) {
                    values.put("descripcion_banco", banco.getDescripcion());
                }else{
                    values.put("descripcion_banco", "VACIO "  + banco.getSecuencial());
                }

                DATABASE.insert(TABLA_BANCO, null, values);

            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarBanco(Banco banco) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            values.put("descripcion_banco", banco.getDescripcion());

            DATABASE.update(TABLA_BANCO, values, "codigo_banco=?", new String[]{String.valueOf(banco.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    private Banco obtenerBanco(Integer codigoBanco) {

        String selectQuery = "SELECT * FROM " + TABLA_BANCO + " where codigo_banco=" + codigoBanco;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        Banco banco = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                banco = new Banco();
                banco.setSecuencial(contentValues.getAsInteger("codigo_banco"));
                banco.setDescripcion(contentValues.getAsString("descripcion_banco"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return banco;

    }

    public List<Banco> obtenerBancos() {

        String selectQuery = "SELECT * FROM " + TABLA_BANCO + " ORDER BY upper(codigo_banco) ASC";

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<Banco> listBanco = new ArrayList<>();

        Banco banco1 = new Banco();
        banco1.setSecuencial(-1);
        banco1.setDescripcion("-- Seleccione un banco --");

        listBanco.add(banco1);

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        Banco banco = new Banco();
                        banco.setSecuencial(contentValues.getAsInteger("codigo_banco"));
                        banco.setDescripcion(contentValues.getAsString("descripcion_banco"));

                        listBanco.add(banco);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listBanco;
    }

    //FIN TABLA BANCO

    // TABLA TIPO PLACAS

    public void insertarTipoPlaca(TipoPlaca tipoPlaca) {
        abrirConexion();

        try {

            if (obtenerTipoPlaca(tipoPlaca.getSecuencial()) != null) {

                actualizarTipoPlaca(tipoPlaca);

            } else {

                ContentValues values = new ContentValues();

                values.put("codigo_tipo_placa", tipoPlaca.getSecuencial());

                if (tipoPlaca.getDescripcion().trim().length() > 0) {

                    values.put("descripcion_tipo_placa", tipoPlaca.getDescripcion());

                }else{

                    values.put("descripcion_tipo_placa", "VACIO "  + tipoPlaca.getSecuencial());

                }

                if (tipoPlaca.getHabilitado() != null) {

                    values.put("habilitado_tipo_placa", ((tipoPlaca.getHabilitado()) ? 1 : 0));

                }else{

                    values.put("habilitado_tipo_placa", 0);

                }

                DATABASE.insert(TABLA_TIPO_PLACA, null, values);

            }

        } catch (Exception sqlex) {
            sqlex.printStackTrace();

        }

    }

    private void actualizarTipoPlaca(TipoPlaca tipoPlaca) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            if (tipoPlaca.getDescripcion().trim().length() > 0) {

                values.put("descripcion_tipo_placa", tipoPlaca.getDescripcion());

            }

            if (tipoPlaca.getHabilitado() != null) {

                values.put("habilitado_tipo_placa", ((tipoPlaca.getHabilitado()) ? 1 : 0));

            }else{

                values.put("habilitado_tipo_placa", 0);

            }

            DATABASE.update(TABLA_TIPO_PLACA, values, "codigo_tipo_placa=?", new String[]{String.valueOf(tipoPlaca.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public TipoPlaca obtenerTipoPlaca(Integer codigoTipoPlaca) {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_PLACA + " where codigo_tipo_placa=" + codigoTipoPlaca;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        TipoPlaca tipoPlaca = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                tipoPlaca = new TipoPlaca();
                tipoPlaca.setSecuencial(contentValues.getAsInteger("codigo_tipo_placa"));
                tipoPlaca.setDescripcion(contentValues.getAsString("descripcion_tipo_placa"));
                tipoPlaca.setHabilitado((contentValues.getAsInteger("habilitado_tipo_placa") == 1));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tipoPlaca;

    }

    public List<TipoPlaca> obtenerTipoPlacas() {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_PLACA;

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<TipoPlaca> listTipoPlacas = new ArrayList<>();

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {

                        TipoPlaca tipoPlaca = new TipoPlaca();
                        tipoPlaca.setSecuencial(contentValues.getAsInteger("codigo_tipo_placa"));
                        tipoPlaca.setDescripcion(contentValues.getAsString("descripcion_tipo_placa"));
                        tipoPlaca.setHabilitado((contentValues.getAsInteger("habilitado_tipo_placa") == 1));

                        listTipoPlacas.add(tipoPlaca);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listTipoPlacas;
    }

    //FIN TABLA TIPO PLACAS

    // TABLA TIPO DOCUMENTOS DE INDENTIDAD

    public void insertarTipoDocumentosIdentidad(TipoDocumentoIdentidad tipoDocumentoIdentidad) {
        abrirConexion();

        try {

            if (obtenerTipoDocumentosIdentidad(tipoDocumentoIdentidad.getSecuencial()) != null) {

                actualizarTipoDocumentosIdentidad(tipoDocumentoIdentidad);

            } else {

                ContentValues values = new ContentValues();

                values.put("codigo_tipo_documento_identidad", tipoDocumentoIdentidad.getSecuencial());

                if (tipoDocumentoIdentidad.getDescripcion().trim().length() > 0) {

                    values.put("descripcion_tipo_documento_identidad", tipoDocumentoIdentidad.getDescripcion());

                }else{

                    values.put("descripcion_tipo_documento_identidad", "VACIO "  + tipoDocumentoIdentidad.getSecuencial());

                }

                if (tipoDocumentoIdentidad.getRequiereComplemento() != null) {

                    values.put("requiere_complemento", ((tipoDocumentoIdentidad.getRequiereComplemento()) ? 1 : 0));

                }else{

                    values.put("requiere_complemento", 0);

                }

                DATABASE.insert(TABLA_TIPO_DOCUMENTO_IDENTIDAD, null, values);

            }

        } catch (Exception sqlex) {
            LogUtils.i(TAG, "ERORO ERROR");
            sqlex.printStackTrace();

        }

    }

    private void actualizarTipoDocumentosIdentidad(TipoDocumentoIdentidad tipoDocumentoIdentidad) {
        abrirConexion();
        try {

            ContentValues values = new ContentValues();

            if (tipoDocumentoIdentidad.getDescripcion().trim().length() > 0) {

                values.put("descripcion_tipo_documento_identidad", tipoDocumentoIdentidad.getDescripcion());

            }

            if (tipoDocumentoIdentidad.getRequiereComplemento() != null) {

                values.put("requiere_complemento", ((tipoDocumentoIdentidad.getRequiereComplemento()) ? 1 : 0));

            }else{

                values.put("requiere_complemento", 0);

            }

            DATABASE.update(TABLA_TIPO_DOCUMENTO_IDENTIDAD, values, "codigo_tipo_documento_identidad=?", new String[]{String.valueOf(tipoDocumentoIdentidad.getSecuencial())});

        } catch (Exception sqlex) {
            sqlex.printStackTrace();
        }

    }

    public TipoDocumentoIdentidad obtenerTipoDocumentosIdentidad(Integer codigoDocumentoIdentidad) {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_DOCUMENTO_IDENTIDAD + " where codigo_tipo_documento_identidad=" + codigoDocumentoIdentidad;

        List<ContentValues> lista = obtenerRegistro(selectQuery);

        TipoDocumentoIdentidad tipoDocumentoIdentidad = null;

        try {

            if (! lista.isEmpty()) {

                ContentValues contentValues = lista.get(0);

                tipoDocumentoIdentidad = new TipoDocumentoIdentidad();
                tipoDocumentoIdentidad.setSecuencial(contentValues.getAsInteger("codigo_tipo_documento_identidad"));
                tipoDocumentoIdentidad.setDescripcion(contentValues.getAsString("descripcion_tipo_documento_identidad"));
                tipoDocumentoIdentidad.setRequiereComplemento((contentValues.getAsInteger("requiere_complemento") == 1));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tipoDocumentoIdentidad;

    }

    public List<TipoDocumentoIdentidad> obtenerTipoDocumentosIdentidad() {

        String selectQuery = "SELECT * FROM " + TABLA_TIPO_DOCUMENTO_IDENTIDAD;

        List<ContentValues> lista = obtenerRegistro(selectQuery);
        List<TipoDocumentoIdentidad> listTipoDocumentoIdentidades = new ArrayList<>();

        try {
            if (lista != null) {
                if (! lista.isEmpty()) {
                    for(ContentValues contentValues : lista) {
                        LogUtils.i(TAG, "Tipodocs identidad :: " + new Gson().toJson(contentValues) + "\n daatatat " + contentValues.toString());
                        TipoDocumentoIdentidad tipoDocumentoIdentidad = new TipoDocumentoIdentidad();
                        tipoDocumentoIdentidad.setSecuencial(contentValues.getAsInteger("codigo_tipo_documento_identidad"));
                        tipoDocumentoIdentidad.setDescripcion(contentValues.getAsString("descripcion_tipo_documento_identidad"));
                        tipoDocumentoIdentidad.setRequiereComplemento((contentValues.getAsInteger("requiere_complemento") == 1));

                        listTipoDocumentoIdentidades.add(tipoDocumentoIdentidad);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listTipoDocumentoIdentidades;
    }

    //FIN TABLA TIPO DOCUMENTOS DE INDENTIDAD
}
