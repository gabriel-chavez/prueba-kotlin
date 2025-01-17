package com.emizor.univida.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Objects;

public class RootActivity extends AppCompatActivity {
    public static String TAG = RootActivity.class.getName();

    protected boolean iniciarCapturaAutomatica = false;
    protected boolean activeGps = false, activeNet = false, activeBest = false;

//    protected int contador = 0;

    @Override
    protected void onDestroy() {
        toggleGPSUpdates("parar");
        toggleNetworkUpdates("parar");
        super.onDestroy();
    }

    protected LocationManager locationManager;
    private double longitudeGPS, latitudeGPS;
    private double longitudeNetwork, latitudeNetwork;
    private double longitudeBest, latitudeBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

//        contador = 0;

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            LogUtils.i(TAG, "verificarPermisoLocacion :: init 00");
            // You can use the API that requires the permission.
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtils.i(TAG, "onRequestPermissionsResult :: on request permisos");
        switch (requestCode) {
            case 3000:
                LogUtils.i(TAG, "onRequestPermissionsResult :: 3000");
                LogUtils.i(TAG, "onRequestPermissionsResult :: grants " + ((grantResults != null) ?new Gson().toJson(grantResults) : grantResults) + " persmiiones " + ((permissions != null) ? new Gson().toJson(permissions) : permissions));
                if (grantResults != null && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // exit the app if one permission is not granted
                    LogUtils.i(TAG, "onRequestPermissionsResult :: finalizando");
//                    finish();
//                    return;
//                    contador++;
//                    if(contador == 2){
                        showAlertPermisionSettings();
//                        contador = 0;
//                    }
                } else {
                    if(iniciarCapturaAutomatica) {
                        toggleGPSUpdates("init");
                        toggleNetworkUpdates("init");
                    }
                }
                LogUtils.i(TAG, "response = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
//                    verificarPermisoLocacion();
                break;
        }
    }

    protected void verificarPermisoLocacion(){
        LogUtils.i(TAG, "verificarPermisoLocacion :: init");
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            LogUtils.i(TAG, "verificarPermisoLocacion :: tiene permisos");
            // You can use the API that requires the permission.
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean response = checkLocation();
            LogUtils.i(TAG, "verificarPermisoLocacion :: response = "+response);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogUtils.i(TAG, "verificarPermisoLocacion :: response = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                LogUtils.i(TAG, "verificarPermisoLocacion :: shouldShowRequestPermissionRationale");
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },3000);
            } else {
                LogUtils.i(TAG, "verificarPermisoLocacion :: else shouldShowRequestPermissionRationale");
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },3000);
            }
        } else {
            LogUtils.i(TAG, "verificarPermisoLocacion :: ActivityCompat.requestPermissions");
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },3000);
        }
        LogUtils.i(TAG, "verificarPermisoLocacion :: end");
    }

    protected  boolean existePermisoLocacion(){
        return (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    protected boolean checkLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            if (!isLocationEnabled())
                showAlert();
            return isLocationEnabled();
        } else {
            return false;
        }
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Habilitar GPS")
                .setMessage("Su ubicación esta desactivada.\nPor favor active la ubicación del dispositivo.")
                .setCancelable(false)
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
//                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    }
//                });
        dialog.show();
    }

    private void showAlertPermisionSettings() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Activar Permisos")
                .setMessage("Por favor active el permiso de ubicación de la aplicación.")
                .setCancelable(false)
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
//                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    }
//                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        LogUtils.i(TAG, "LocationListener :: isLocationEnabled :: INIT ");
        if(locationManager == null){
            return false;
        }
        LogUtils.i(TAG, "LocationListener :: isLocationEnabled :: INIT pased");
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } else {
            return false;
        }
    }

    public void toggleGPSUpdates(String action) {
        LogUtils.i(TAG, "LocationListener :: toggleGPSUpdates :: INIT ");
        if (!checkLocation())
            return;
        LogUtils.i(TAG, "LocationListener :: toggleGPSUpdates :: INIT enabled");
//        Button button = (Button) view;
        if (action.equals("stop")) {
            activeGps = false;
            locationManager.removeUpdates(locationListenerGPS);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            activeGps = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ConfigEmizor.TIEMPO_MINIMO_ACTUALIZACION_LOCACION, ConfigEmizor.DISTACIA_ACTUALIZACION_LOCACION, locationListenerGPS);
//            Toast.makeText(this, "Proveedor de GPS comenzo ", Toast.LENGTH_LONG).show();
        }
    }

    public void toggleNetworkUpdates(String action) {
        LogUtils.i(TAG, "LocationListener :: toggleNetworkUpdates :: INIT ");
        if (!checkLocation())
            return;
        LogUtils.i(TAG, "LocationListener :: toggleNetworkUpdates :: INIT enabled");
        if (action.equals("stop")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            activeNet = false;
            locationManager.removeUpdates(locationListenerNetwork);
        }
        else {
            activeNet = true;
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, ConfigEmizor.TIEMPO_MINIMO_ACTUALIZACION_LOCACION, ConfigEmizor.DISTACIA_ACTUALIZACION_LOCACION, locationListenerNetwork);
//            Toast.makeText(this, "Proveedor de red comenzo ", Toast.LENGTH_LONG).show();
        }
    }

    public void toggleBestUpdates(String action) {
        LogUtils.i(TAG, "LocationListener :: toggleBestUpdates :: INIT ");
        if (!checkLocation())
            return;
        LogUtils.i(TAG, "LocationListener :: toggleBestUpdates :: INIT enabled");

        if (action.equals("stop")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            activeBest = false;
            locationManager.removeUpdates(locationListenerBest);
        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
            String provider = locationManager.getBestProvider(criteria, true);
            if (provider != null) {
                activeBest = true;
                locationManager.requestLocationUpdates(provider, ConfigEmizor.TIEMPO_MINIMO_ACTUALIZACION_LOCACION, ConfigEmizor.DISTACIA_ACTUALIZACION_LOCACION, locationListenerBest);
//                Toast.makeText(this, "Iniciando Mejor Proveedor es " + provider, Toast.LENGTH_LONG).show();
            }
        }
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            LogUtils.i(TAG, "LocationListener :: onLocationChanged :: longitudeNet = "+ longitudeNetwork + ", latitudeNet = "+ latitudeNetwork);
            registrarLocacion("red");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(RootActivity.this, "Actualizacion del proveedor de red ("+longitudeNetwork + "," +latitudeNetwork +")" , Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            LogUtils.i(TAG, "LocationListener :: onStatusChanged :: NET s = "+ s + ", i = "+ i);
        }

        @Override
        public void onProviderEnabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderEnabled :: NET s = "+ s);
        }
        @Override
        public void onProviderDisabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: NET s = "+ s);
        }
    };

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            LogUtils.i(TAG, "LocationListener :: onLocationChanged :: longitudeGPS = "+ longitudeGPS + ", latitudeGPS = "+ latitudeGPS);
            registrarLocacion("gps");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(RootActivity.this, "Actualizacion del proveedor de Gps ("+longitudeGPS + "," +latitudeGPS +")", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            LogUtils.i(TAG, "LocationListener :: onStatusChanged :: GPS s = "+ s + ", i = "+ i);
        }

        @Override
        public void onProviderEnabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderEnabled :: GPS s = "+ s);
        }
        @Override
        public void onProviderDisabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: GPS s = "+ s);
        }
    };

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: BEST CRITERIA longitudeBest = "+ longitudeBest +", latitudeBest = " + latitudeBest);
            registrarLocacion("best");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(RootActivity.this, "Mejor Proveedor de Actualizacion (longitudeBest = "+ longitudeBest +", latitudeBest = " + latitudeBest + ")", Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: BEST CRITERIA s = "+ s +", i = " + i);
        }

        @Override
        public void onProviderEnabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: BEST CRITERIA s = "+ s);
        }

        @Override
        public void onProviderDisabled(String s) {
            LogUtils.i(TAG, "LocationListener :: onProviderDisabled :: BEST CRITERIA s = "+ s);
        }
    };

    protected Double getLocationLatitud(){
        return Double.valueOf(Objects.requireNonNull(getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE).getString("latitud", "0")));
    }

    protected Double getLocationLongitud(){
        return Double.valueOf(Objects.requireNonNull(getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE).getString("longitud", "0")));
    }

    protected boolean existeLocacionSemilla(){
        try {
            if (Double.parseDouble(Objects.requireNonNull(getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE).getString("longitud", "0"))) == 0d) {
                return false;
            }
            if (Double.parseDouble(Objects.requireNonNull(getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE).getString("latitud", "0"))) == 0d) {
                return false;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    protected void verificarTiempoLocacion() {
        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: init ");
        iniciarCapturaAutomatica = true;
        if(existePermisoLocacion()) {
            if (checkLocation()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE);

                        long timeUpdate = sharedPreferences.getLong("fecha_hora_update", 0);

                        long timeFechaActual = Calendar.getInstance().getTimeInMillis();

                        long tiempomls = timeFechaActual - timeUpdate;

                        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: time update " + timeUpdate + " time fechaactual " + timeFechaActual + " tiem rest " + tiempomls);

                        if (tiempomls > ConfigEmizor.TIEMPO_ESPERA_UPDATE_LOCATION) {
                            toggleGPSUpdates("init");
                            toggleNetworkUpdates("init");
                        }
                    }
                });
            } else {
                verificarPermisoLocacion();
            }
        } else {
            verificarPermisoLocacion();
        }



    }

    private void registrarLocacion(String proveedor){
        toggleNetworkUpdates("stop");
        toggleGPSUpdates("stop");
        toggleBestUpdates("stop");
        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: init ");
        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: proveedor = " + proveedor);
        String latitudFinal = "", longitudFinal = "";
        SharedPreferences sharedPreferences = getSharedPreferences("pref_datos_location", Context.MODE_PRIVATE);
        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: all prefs = " + new Gson().toJson(sharedPreferences.getAll()));

        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (proveedor){
            case "red":
                latitudFinal = "" + latitudeNetwork;
                longitudFinal = "" + longitudeNetwork;
                editor.putString("latitud_red", latitudFinal);
                editor.putString("longitud_red", longitudFinal);
                editor.putLong("fecha_hora_update_red", Calendar.getInstance().getTimeInMillis());
                break;
            case "gps":
                latitudFinal = "" + latitudeGPS;
                longitudFinal = "" + longitudeGPS;
                editor.putString("latitud_gps", latitudFinal);
                editor.putString("longitud_gps", longitudFinal);
                editor.putLong("fecha_hora_update_gps", Calendar.getInstance().getTimeInMillis());
                break;
            case "best":
                latitudFinal = "" + latitudeBest;
                longitudFinal = "" + longitudeBest;
                editor.putString("latitud_best", latitudFinal);
                editor.putString("longitud_best", longitudFinal);
                editor.putLong("fecha_hora_update_best", Calendar.getInstance().getTimeInMillis());
                break;
        }
        editor.putLong("fecha_hora_update", Calendar.getInstance().getTimeInMillis());
        editor.putString("latitud", latitudFinal);
        editor.putString("longitud", longitudFinal);
        editor.apply();
        editor.commit();

        LogUtils.i(TAG, "LocationListener :: verificarTiempoLocacion :: END all prefs = " + new Gson().toJson(sharedPreferences.getAll()));

    }

}