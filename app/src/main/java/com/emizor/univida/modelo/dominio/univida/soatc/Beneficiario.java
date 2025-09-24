package com.emizor.univida.modelo.dominio.univida.soatc;

public class Beneficiario {
    private String pol_ben_nombre_completo;
    private int pol_ben_t_par_emi_beneficiario_parentesco_fk;
    private int pol_ben_beneficio_porcentaje;

    public Beneficiario(String nombre, int parentesco, int porcentaje) {
        this.pol_ben_nombre_completo = nombre;
        this.pol_ben_t_par_emi_beneficiario_parentesco_fk = parentesco;
        this.pol_ben_beneficio_porcentaje = porcentaje;
    }

    public String getNombre() { return pol_ben_nombre_completo; }
    public int getParentesco() { return pol_ben_t_par_emi_beneficiario_parentesco_fk; }
    public int getPorcentaje() { return pol_ben_beneficio_porcentaje; }
}

