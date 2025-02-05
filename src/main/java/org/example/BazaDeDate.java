package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Clasa care gestioneaza conexiunea la baza de date si crearea tabelelor necesare.
 * Aceasta clasa ofera metode pentru initializarea conexiunii la baza de date si pentru crearea tabelelor 'Doctor' si 'Pacient'.
 */
public class BazaDeDate {
    // Conexiunea la baza de date
       public static Connection connection;
    /**
     * Initializeaza conexiunea la baza de date si creaza tabelele necesare.
     * In cazul in care tabelele nu exista, acestea sunt create.
     */
    public static void initialize() {
        try {
            //URL-ul bazei de date (cu locatia fisierului)
            String DB_URL = "jdbc:derby:C:/Users/40756/IdeaProjects/App/untitled/mediciDB;create=true";
            //Stabilim conexiunea la baza de date
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            //Afiseaza eroarea daca conexiunea nu poate fi stabilita
            e.printStackTrace();
        }
    }
    /**
     * Creaza tabelele necesare in baza de date: Doctor si Pacient.
     * Tabelele vor fi create doar daca nu exista deja.
     */
    public static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            //Comanda SQL pentru crearea tabelului Doctor
            String createDoctorTable = """
            CREATE TABLE Doctor (
                id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                doctor_nume VARCHAR(100) NOT NULL,
                doctor_email VARCHAR(100) UNIQUE NOT NULL,
                doctor_parola VARCHAR(100) NOT NULL,
                doctor_specializare VARCHAR(100)
            )
        """;
            try {
                //Executa comanda pentru crearea tabelului Doctor
                stmt.executeUpdate(createDoctorTable);
                System.out.println("Tabelul Doctor a fost creat.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) { // Codul de eroare Derby pentru "tabela exista deja"
                    System.out.println("Tabelul Doctor există deja.");
                } else {
                    throw e; // Arunca alte erori neasteptate
                }
            }
            //Comanda SQL pentru crearea tabelului Pacient
            String createPacientTable = """
            CREATE TABLE Pacient (
                id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                nume VARCHAR(100) NOT NULL,
                data_nasterii DATE NOT NULL,
                cnp VARCHAR(13) UNIQUE NOT NULL,
                istoric VARCHAR(500),
                diagnostic VARCHAR(255),
                telefon VARCHAR(15),
                medicamente VARCHAR(500),
                data_programarii DATE,
                ora_programarii TIME,
                doctor_nume VARCHAR(50)
            )
        """;
            try {
                //Executa comanda pentru crearea tabelului Pacient
                stmt.executeUpdate(createPacientTable);
                System.out.println("Tabelul Pacient a fost creat.");
            } catch (SQLException e) {
                //Verifica daca tabela exista deja
                if (e.getSQLState().equals("X0Y32")) { // Tabela exista deja
                    System.out.println("Tabelul Pacient există deja.");
                } else {
                    e.printStackTrace(); //Afiseaza alte erori
                }
            }
            //Mesaj de succes pentru crearea tabelelor
            System.out.println("Tabelele au fost create cu succes!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Obtine conexiunea la baza de date.
     * @return conexiunea la baza de date activa
     */

    public static Connection getConnection() {
        return connection;
    }
}
