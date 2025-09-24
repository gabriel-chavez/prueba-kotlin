package com.emizor.univida.modelo.dominio.univida.seguridad;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DatosUsuario implements Serializable, Parcelable
{

    @SerializedName("contrato_secuencial")
    @Expose
    private Integer contratoSecuencial;
    @SerializedName("empleado_apellido_casada")
    @Expose
    private String empleadoApellidoCasada;
    @SerializedName("empleado_apellido_materno")
    @Expose
    private String empleadoApellidoMaterno;
    @SerializedName("empleado_apellido_paterno")
    @Expose
    private String empleadoApellidoPaterno;
    @SerializedName("empleado_cargo")
    @Expose
    private String empleadoCargo;
    @SerializedName("empleado_ciudad_residencia")
    @Expose
    private String empleadoCiudadResidencia;
    @SerializedName("empleado_direccion")
    @Expose
    private String empleadoDireccion;
    @SerializedName("empleado_documento_numero")
    @Expose
    private String empleadoDocumentoNumero;
    @SerializedName("empleado_documento_completo")
    @Expose
    private String empleadoDocumentoCompleto;
    @SerializedName("empleado_documento_extension")
    @Expose
    private String empleadoDocumentoExtension;
    @SerializedName("empleado_documento_tipo")
    @Expose
    private String empleadoDocumentoTipo;
    @SerializedName("empleado_nombre_alternativo")
    @Expose
    private String empleadoNombreAlternativo;
    @SerializedName("empleado_nombre_completo")
    @Expose
    private String empleadoNombreCompleto;
    @SerializedName("empleado_primer_nombre")
    @Expose
    private String empleadoPrimerNombre;
    @SerializedName("empleado_segundo_nombre")
    @Expose
    private String empleadoSegundoNombre;
    @SerializedName("empleado_secuencial")
    @Expose
    private Integer empleadoSecuencial;
    @SerializedName("empleado_usuario")
    @Expose
    private String empleadoUsuario;
    @SerializedName("estado_empleado_activo")
    @Expose
    private Boolean estadoEmpleadoActivo;
    @SerializedName("sucursal_ciudad")
    @Expose
    private String sucursalCiudad;
    @SerializedName("sucursal_codigo")
    @Expose
    private Integer sucursalCodigo;
    @SerializedName("sucursal_departamento")
    @Expose
    private String sucursalDepartamento;
    @SerializedName("sucursal_direccion")
    @Expose
    private String sucursalDireccion;
    @SerializedName("sucursal_id_departamento")
    @Expose
    private String sucursalIdDepartamento;
    @SerializedName("sucursal_nombre")
    @Expose
    private String sucursalNombre;
    @SerializedName("superior_cargo")
    @Expose
    private String superiorCargo;
    @SerializedName("superior_secuencial")
    @Expose
    private Integer superiorSecuencial;
    @SerializedName("superior_usuario")
    @Expose
    private String superiorUsuario;
    @SerializedName("rol_soat")
    @Expose
    private boolean  rolSoat;

    @SerializedName("rol_soatc")
    @Expose
    private boolean  rolSoatc;
    public final static Parcelable.Creator<DatosUsuario> CREATOR = new Creator<DatosUsuario>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DatosUsuario createFromParcel(Parcel in) {
            return new DatosUsuario(in);
        }

        public DatosUsuario[] newArray(int size) {
            return (new DatosUsuario[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8201699832928892885L;

    protected DatosUsuario(Parcel in) {
        this.contratoSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.empleadoApellidoCasada = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoApellidoMaterno = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoApellidoPaterno = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoCargo = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoCiudadResidencia = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoDireccion = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoDocumentoNumero = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoDocumentoCompleto = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoDocumentoExtension = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoDocumentoTipo = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoNombreAlternativo = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoNombreCompleto = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoPrimerNombre = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoSegundoNombre = ((String) in.readValue((String.class.getClassLoader())));
        this.empleadoSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.empleadoUsuario = ((String) in.readValue((String.class.getClassLoader())));
        this.estadoEmpleadoActivo = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.sucursalCiudad = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalCodigo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.sucursalDepartamento = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalDireccion = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalIdDepartamento = ((String) in.readValue((String.class.getClassLoader())));
        this.sucursalNombre = ((String) in.readValue((String.class.getClassLoader())));
        this.superiorCargo = ((String) in.readValue((String.class.getClassLoader())));
        this.superiorSecuencial = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.superiorUsuario = ((String) in.readValue((String.class.getClassLoader())));
        this.rolSoat = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.rolSoatc = ((Boolean) in.readValue((Boolean.class.getClassLoader())));

    }

    public DatosUsuario() {
    }

    public Integer getContratoSecuencial() {
        return contratoSecuencial;
    }

    public void setContratoSecuencial(Integer contratoSecuencial) {
        this.contratoSecuencial = contratoSecuencial;
    }

    public String getEmpleadoApellidoCasada() {
        return empleadoApellidoCasada;
    }

    public void setEmpleadoApellidoCasada(String empleadoApellidoCasada) {
        this.empleadoApellidoCasada = empleadoApellidoCasada;
    }

    public String getEmpleadoApellidoMaterno() {
        return empleadoApellidoMaterno;
    }

    public void setEmpleadoApellidoMaterno(String empleadoApellidoMaterno) {
        this.empleadoApellidoMaterno = empleadoApellidoMaterno;
    }

    public String getEmpleadoApellidoPaterno() {
        return empleadoApellidoPaterno;
    }

    public void setEmpleadoApellidoPaterno(String empleadoApellidoPaterno) {
        this.empleadoApellidoPaterno = empleadoApellidoPaterno;
    }

    public String getEmpleadoCargo() {
        return empleadoCargo;
    }

    public void setEmpleadoCargo(String empleadoCargo) {
        this.empleadoCargo = empleadoCargo;
    }

    public String getEmpleadoCiudadResidencia() {
        return empleadoCiudadResidencia;
    }

    public void setEmpleadoCiudadResidencia(String empleadoCiudadResidencia) {
        this.empleadoCiudadResidencia = empleadoCiudadResidencia;
    }

    public String getEmpleadoDireccion() {
        return empleadoDireccion;
    }

    public void setEmpleadoDireccion(String empleadoDireccion) {
        this.empleadoDireccion = empleadoDireccion;
    }

    public String getEmpleadoDocumentoNumero() {
        return empleadoDocumentoNumero;
    }

    public void setEmpleadoDocumentoNumero(String empleadoDocumentoNumero) {
        this.empleadoDocumentoNumero = empleadoDocumentoNumero;
    }

    public String getEmpleadoDocumentoCompleto() {
        return empleadoDocumentoCompleto;
    }

    public void setEmpleadoDocumentoCompleto(String empleadoDocumentoCompleto) {
        this.empleadoDocumentoCompleto = empleadoDocumentoCompleto;
    }

    public String getEmpleadoDocumentoExtension() {
        return empleadoDocumentoExtension;
    }

    public void setEmpleadoDocumentoExtension(String empleadoDocumentoExtension) {
        this.empleadoDocumentoExtension = empleadoDocumentoExtension;
    }

    public String getEmpleadoDocumentoTipo() {
        return empleadoDocumentoTipo;
    }

    public void setEmpleadoDocumentoTipo(String empleadoDocumentoTipo) {
        this.empleadoDocumentoTipo = empleadoDocumentoTipo;
    }

    public String getEmpleadoNombreAlternativo() {
        return empleadoNombreAlternativo;
    }

    public void setEmpleadoNombreAlternativo(String empleadoNombreAlternativo) {
        this.empleadoNombreAlternativo = empleadoNombreAlternativo;
    }

    public String getEmpleadoNombreCompleto() {
        return empleadoNombreCompleto;
    }

    public void setEmpleadoNombreCompleto(String empleadoNombreCompleto) {
        this.empleadoNombreCompleto = empleadoNombreCompleto;
    }

    public String getEmpleadoPrimerNombre() {
        return empleadoPrimerNombre;
    }

    public void setEmpleadoPrimerNombre(String empleadoPrimerNombre) {
        this.empleadoPrimerNombre = empleadoPrimerNombre;
    }

    public String getEmpleadoSegundoNombre() {
        return empleadoSegundoNombre;
    }

    public void setEmpleadoSegundoNombre(String empleadoSegundoNombre) {
        this.empleadoSegundoNombre = empleadoSegundoNombre;
    }

    public Integer getEmpleadoSecuencial() {
        return empleadoSecuencial;
    }

    public void setEmpleadoSecuencial(Integer empleadoSecuencial) {
        this.empleadoSecuencial = empleadoSecuencial;
    }

    public String getEmpleadoUsuario() {
        return empleadoUsuario;
    }

    public void setEmpleadoUsuario(String empleadoUsuario) {
        this.empleadoUsuario = empleadoUsuario;
    }

    public Boolean getEstadoEmpleadoActivo() {
        return estadoEmpleadoActivo;
    }

    public void setEstadoEmpleadoActivo(Boolean estadoEmpleadoActivo) {
        this.estadoEmpleadoActivo = estadoEmpleadoActivo;
    }

    public String getSucursalCiudad() {
        return sucursalCiudad;
    }

    public void setSucursalCiudad(String sucursalCiudad) {
        this.sucursalCiudad = sucursalCiudad;
    }

    public Integer getSucursalCodigo() {
        return sucursalCodigo;
    }

    public void setSucursalCodigo(Integer sucursalCodigo) {
        this.sucursalCodigo = sucursalCodigo;
    }

    public String getSucursalDepartamento() {
        return sucursalDepartamento;
    }

    public void setSucursalDepartamento(String sucursalDepartamento) {
        this.sucursalDepartamento = sucursalDepartamento;
    }

    public String getSucursalDireccion() {
        return sucursalDireccion;
    }

    public void setSucursalDireccion(String sucursalDireccion) {
        this.sucursalDireccion = sucursalDireccion;
    }

    public String getSucursalIdDepartamento() {
        return sucursalIdDepartamento;
    }

    public void setSucursalIdDepartamento(String sucursalIdDepartamento) {
        this.sucursalIdDepartamento = sucursalIdDepartamento;
    }

    public String getSucursalNombre() {
        return sucursalNombre;
    }

    public void setSucursalNombre(String sucursalNombre) {
        this.sucursalNombre = sucursalNombre;
    }

    public String getSuperiorCargo() {
        return superiorCargo;
    }

    public void setSuperiorCargo(String superiorCargo) {
        this.superiorCargo = superiorCargo;
    }

    public Integer getSuperiorSecuencial() {
        return superiorSecuencial;
    }

    public void setSuperiorSecuencial(Integer superiorSecuencial) {
        this.superiorSecuencial = superiorSecuencial;
    }

    public String getSuperiorUsuario() {
        return superiorUsuario;
    }

    public void setSuperiorUsuario(String superiorUsuario) {
        this.superiorUsuario = superiorUsuario;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("contratoSecuencial", contratoSecuencial).append("empleadoApellidoCasada", empleadoApellidoCasada).append("empleadoApellidoMaterno", empleadoApellidoMaterno).append("empleadoApellidoPaterno", empleadoApellidoPaterno).append("empleadoCargo", empleadoCargo).append("empleadoCiudadResidencia", empleadoCiudadResidencia).append("empleadoDireccion", empleadoDireccion).append("empleadoDocumentoNumero", empleadoDocumentoNumero).append("empleadoDocumentoCompleto", empleadoDocumentoCompleto).append("empleadoDocumentoExtension", empleadoDocumentoExtension).append("empleadoDocumentoTipo", empleadoDocumentoTipo).append("empleadoNombreAlternativo", empleadoNombreAlternativo).append("empleadoNombreCompleto", empleadoNombreCompleto).append("empleadoPrimerNombre", empleadoPrimerNombre).append("empleadoSegundoNombre", empleadoSegundoNombre).append("empleadoSecuencial", empleadoSecuencial).append("empleadoUsuario", empleadoUsuario).append("estadoEmpleadoActivo", estadoEmpleadoActivo).append("sucursalCiudad", sucursalCiudad).append("sucursalCodigo", sucursalCodigo).append("sucursalDepartamento", sucursalDepartamento).append("sucursalDireccion", sucursalDireccion).append("sucursalIdDepartamento", sucursalIdDepartamento).append("sucursalNombre", sucursalNombre).append("superiorCargo", superiorCargo).append("superiorSecuencial", superiorSecuencial).append("superiorUsuario", superiorUsuario).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sucursalCodigo).append(empleadoSecuencial).append(superiorCargo).append(sucursalCiudad).append(empleadoDocumentoCompleto).append(empleadoNombreCompleto).append(empleadoUsuario).append(empleadoSegundoNombre).append(empleadoPrimerNombre).append(sucursalIdDepartamento).append(empleadoDocumentoTipo).append(empleadoCiudadResidencia).append(empleadoApellidoPaterno).append(superiorUsuario).append(empleadoDocumentoExtension).append(contratoSecuencial).append(empleadoNombreAlternativo).append(superiorSecuencial).append(sucursalDepartamento).append(empleadoCargo).append(estadoEmpleadoActivo).append(sucursalDireccion).append(empleadoDireccion).append(sucursalNombre).append(empleadoApellidoCasada).append(empleadoDocumentoNumero).append(empleadoApellidoMaterno).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatosUsuario) == false) {
            return false;
        }
        DatosUsuario rhs = ((DatosUsuario) other);
        return new EqualsBuilder().append(sucursalCodigo, rhs.sucursalCodigo).append(empleadoSecuencial, rhs.empleadoSecuencial).append(superiorCargo, rhs.superiorCargo).append(sucursalCiudad, rhs.sucursalCiudad).append(empleadoDocumentoCompleto, rhs.empleadoDocumentoCompleto).append(empleadoNombreCompleto, rhs.empleadoNombreCompleto).append(empleadoUsuario, rhs.empleadoUsuario).append(empleadoSegundoNombre, rhs.empleadoSegundoNombre).append(empleadoPrimerNombre, rhs.empleadoPrimerNombre).append(sucursalIdDepartamento, rhs.sucursalIdDepartamento).append(empleadoDocumentoTipo, rhs.empleadoDocumentoTipo).append(empleadoCiudadResidencia, rhs.empleadoCiudadResidencia).append(empleadoApellidoPaterno, rhs.empleadoApellidoPaterno).append(superiorUsuario, rhs.superiorUsuario).append(empleadoDocumentoExtension, rhs.empleadoDocumentoExtension).append(contratoSecuencial, rhs.contratoSecuencial).append(empleadoNombreAlternativo, rhs.empleadoNombreAlternativo).append(superiorSecuencial, rhs.superiorSecuencial).append(sucursalDepartamento, rhs.sucursalDepartamento).append(empleadoCargo, rhs.empleadoCargo).append(estadoEmpleadoActivo, rhs.estadoEmpleadoActivo).append(sucursalDireccion, rhs.sucursalDireccion).append(empleadoDireccion, rhs.empleadoDireccion).append(sucursalNombre, rhs.sucursalNombre).append(empleadoApellidoCasada, rhs.empleadoApellidoCasada).append(empleadoDocumentoNumero, rhs.empleadoDocumentoNumero).append(empleadoApellidoMaterno, rhs.empleadoApellidoMaterno).isEquals();
    }
    public Boolean getRolSoat() {
        return rolSoat;
    }

    public void setRolSoat(Boolean rolSoat) {
        this.rolSoat = rolSoat;
    }

    public Boolean getRolSoatc() {
        return rolSoatc;
    }

    public void setRolSoatc(Boolean rolSoatc) {
        this.rolSoatc = rolSoatc;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(contratoSecuencial);
        dest.writeValue(empleadoApellidoCasada);
        dest.writeValue(empleadoApellidoMaterno);
        dest.writeValue(empleadoApellidoPaterno);
        dest.writeValue(empleadoCargo);
        dest.writeValue(empleadoCiudadResidencia);
        dest.writeValue(empleadoDireccion);
        dest.writeValue(empleadoDocumentoNumero);
        dest.writeValue(empleadoDocumentoCompleto);
        dest.writeValue(empleadoDocumentoExtension);
        dest.writeValue(empleadoDocumentoTipo);
        dest.writeValue(empleadoNombreAlternativo);
        dest.writeValue(empleadoNombreCompleto);
        dest.writeValue(empleadoPrimerNombre);
        dest.writeValue(empleadoSegundoNombre);
        dest.writeValue(empleadoSecuencial);
        dest.writeValue(empleadoUsuario);
        dest.writeValue(estadoEmpleadoActivo);
        dest.writeValue(sucursalCiudad);
        dest.writeValue(sucursalCodigo);
        dest.writeValue(sucursalDepartamento);
        dest.writeValue(sucursalDireccion);
        dest.writeValue(sucursalIdDepartamento);
        dest.writeValue(sucursalNombre);
        dest.writeValue(superiorCargo);
        dest.writeValue(superiorSecuencial);
        dest.writeValue(superiorUsuario);
        dest.writeValue(rolSoat);
        dest.writeValue(rolSoatc);

    }

    public int describeContents() {
        return 0;
    }
}
