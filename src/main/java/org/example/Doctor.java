package org.example;
/**
 * Clasa care reprezinta un doctor
 */
public class Doctor {
    String nume;
    String email;
    String parola;
    String specializare;
    /**
     * Constructor pentru initializarea unui obiect Doctor.
     *
     * @param nume Numele doctorului
     * @param email Adresa de email a doctorului
     * @param parola Parola pentru contul doctorului
     * @param specializare Specializarea doctorului
     */
    public Doctor(String nume, String email, String parola, String specializare) {
        this.nume = nume;
        this.email = email;
        this.parola = parola;
        this.specializare = specializare;
    }
    //Gettere pentru fiecare atribut
    public String getNumeD(){return nume;}
    public String getEmail(){return email;}
    public String getParola(){return parola;}
    public String getSpecializare(){return specializare;}
}
