package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clasa care reprezinta un doctor
 */
public class Doctor {
    String nume;
    String email;
    String parola;
    String specializare;
    private Connection connection;
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
    public Doctor() {
        connection = BazaDeDate.getConnection();
    }

    /**
     * Verifica daca doctorul este autentificat folosind email-ul si parola.
     *
     * @param email Adresa de email a doctorului
     * @param parola Parola doctorului
     * @return true daca autentificarea este valida, false altfel
     */
    public boolean autentificareDoctor(String email, String parola) {
        String query = "SELECT * FROM Doctor WHERE doctor_email = ? AND doctor_parola = ?";
        //Folosim un PreparedStatement pentru a cauta un doctor in baza de date
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, parola);

            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  //Daca apare o eroare
        }
    }

    /**
     * Adauga un nou doctor in baza de date.
     *
     * @param nume Numele doctorului
     * @param email Adresa de email a doctorului
     * @param parola Parola doctorului
     * @param specializare Specializarea doctorului
     * @throws SQLException In cazul in care apare o eroare la interactiunea cu baza de date
     */
    public void adaugaDoctor(String nume, String email, String parola, String specializare) throws SQLException {
        String query = "INSERT INTO Doctor (doctor_nume, doctor_email, doctor_parola, doctor_specializare) VALUES (?, ?, ?, ?)";
        //Folosim un PreparedStatement pentru a adauga un nou doctor in baza de date
        try (PreparedStatement stmt = BazaDeDate.getConnection().prepareStatement(query)) {
            stmt.setString(1, nume);
            stmt.setString(2, email);
            stmt.setString(3, parola);
            stmt.setString(4, specializare);
            stmt.executeUpdate();
        }
    }

    //Gettere pentru fiecare atribut
    public String getNumeD(){return nume;}
    public String getEmail(){return email;}
    public String getParola(){return parola;}
    public String getSpecializare(){return specializare;}
}
