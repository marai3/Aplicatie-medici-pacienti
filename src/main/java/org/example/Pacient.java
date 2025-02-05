package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
/**
 * Clasa Pacient reprezinta un pacient cu detaliile sale
 */
public class Pacient {
    //Atributele clasei care stocheaza informatiile unui pacient
    String nume;
    LocalDate dataNas;
    String CNP;
    String istoric;
    String diagnostic;
    String tel;
    List<String> medicamente;
    LocalDate data;
    LocalTime ora;
    String doctor;
    /**
     * Constructorul clasei `Pacient` pentru initializarea unui obiect cu toate informatiile unui pacient
     * @param nume Numele pacientului
     * @param dataNas Data nasterii pacientului
     * @param cnp CNP-ul pacientului
     * @param istoric Istoricul medical al pacientului
     * @param diagnostic Diagnosticarea pacientului
     * @param medicamente Lista de medicamente prescrise pacientului
     * @param tel Numarul de telefon al pacientului
     * @param data Data programarii pacientului
     * @param ora Ora programarii pacientului
     * @param Doctor Numele doctorului care se ocupa de pacient
     */
    public Pacient(String nume, LocalDate dataNas, String cnp, String istoric, String diagnostic, List<String> medicamente, String tel, LocalDate data, LocalTime ora, String Doctor) {

        this.nume = nume;
        this.dataNas = dataNas;
        this.CNP=cnp;
        this.istoric = istoric;
        this.diagnostic = diagnostic;
        this.medicamente = medicamente;
        this.tel=tel;
        this.data=data;
        this.ora=ora;
        this.doctor=Doctor;
    }
    //Gettere pentru accesarea valorilor fiecarui atribut al clasei
    String getNume(){return nume;}
    LocalDate getDataNas(){ return dataNas; }
    String getCNP(){ return CNP; }
    String getIstoric(){ return istoric; }
    String getDiagnostic(){ return diagnostic; }
    String getTel(){ return tel; }
    LocalDate getData(){ return data; }
    LocalTime getOra(){ return ora; }
    List<String> getMedicamente(){ return medicamente; }
    String getDoctor(){ return doctor;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ nume: ");
        sb.append(nume);
        sb.append(" ,data nasterii: ");
        sb.append(dataNas.toString());
        sb.append(" ,cnp: ");
        sb.append(CNP.toString());
        sb.append(" ,istoric: ");
        sb.append(istoric);
        sb.append(" ,diagnostic: ");
        sb.append(diagnostic);
        sb.append(" ,medicamente: ");
        for( String m : medicamente) {
            sb.append(m);
            sb.append(", ");

        }
        sb.append(" tel: ");
        sb.append(tel);
        sb.append(" ,programare ora: ");
        sb.append(ora.toString());
        sb.append(" ,programare data: ");
        sb.append(data.toString());
        sb.append(" ,doctor: ");
        sb.append(doctor);
        sb.append(" }");
        return sb.toString();


    }
}
