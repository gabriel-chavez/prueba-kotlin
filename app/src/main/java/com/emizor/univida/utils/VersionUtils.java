package com.emizor.univida.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class VersionUtils {

    private static final String TAG = "VersionUtils";

    /**
     * Obtiene la versión completa de la app (ej: "1.5.0")
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error obteniendo versión de la app", e);
            return "0.0.0";
        }
    }

    /**
     * Obtiene solo major.minor (ej: "1.5")
     */
    public static String getMajorMinor(Context context) {
        String versionName = getVersionName(context);
        String[] parts = versionName.split("\\.");
        if (parts.length >= 2) {
            return parts[0] + "." + parts[1];
        } else {
            return versionName; // fallback
        }
    }
    public static boolean isVersionLessThan(Context context, String referenceVersion) {
        // Reutiliza el método de comparación mayor o igual
        return !isVersionGreaterOrEqual(context, referenceVersion);
    }
    /**
     * Compara si la versión actual es mayor o igual a la de referencia (major.minor)
     */
    public static boolean isVersionGreaterOrEqual(Context context, String referenceVersion) {
        String actualVersion = getMajorMinor(context);

        try {
            String[] actualParts = actualVersion.split("\\.");
            String[] refParts = referenceVersion.split("\\.");

            int actualMajor = Integer.parseInt(actualParts[0]);
            int actualMinor = Integer.parseInt(actualParts[1]);
            int refMajor = Integer.parseInt(refParts[0]);
            int refMinor = Integer.parseInt(refParts[1]);

            if (actualMajor > refMajor) return true;
            if (actualMajor < refMajor) return false;
            return actualMinor >= refMinor;

        } catch (Exception e) {
            Log.e(TAG, "Error comparando versiones", e);
            return false;
        }
    }
}