package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Clasa `PacientDAO` este responsabila pentru operatiunile de interactiune cu baza de date
 * legate de entitatea Pacient cum ar fi adaugarea, cautarea si stergerea pacientilor
 */
public class PacientDAO {
    // Obiectul Connection pentru conexiunea la baza de date
    private Connection connection;
    /**
     * Constructorul clasei PacientDAO.
     * Initializeaza conexiunea la baza de date folosind o conexiune existenta
     */
    public PacientDAO() {
        connection = BazaDeDate.getConnection();  // Folosește conexiunea existentă
    }
    /**
     * Cautam un pacient in baza de date folosind numele si CNP-ul
     * @param nume numele pacientului
     * @param cnp CNP-ul pacientului
     * @return obiectul Pacient cu informatiile preluate din baza de date, sau null daca nu este gasit
     */
    public Pacient PacientCNP(String nume, String cnp) {
        //Interogarea SQL pentru a cauta pacientul dupa nume si CNP
        String query = "SELECT * FROM Pacient WHERE nume = ? AND cnp = ?";
        Pacient pacient = null;
        //Executam interogarea SQL
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            //Setam parametrii pentru interogare
            stmt.setString(1, nume);
            stmt.setString(2, cnp);
            //Executam interogarea si obtinem rezultatele
            ResultSet resultSet = stmt.executeQuery();
            // Dacă exista un rezultat, construim obiectul Pacient
            if (resultSet.next()) {
                //Extragem datele din baza de date
                String nume1 = resultSet.getString("nume");
                LocalDate dataNas = resultSet.getDate("data_nasterii").toLocalDate();
                String CNP = resultSet.getString("cnp");
                String istoric = resultSet.getString("istoric");
                String diagnostic = resultSet.getString("diagnostic");
                String tel = resultSet.getString("telefon");
                LocalDate data=resultSet.getDate("data_programarii").toLocalDate();
                LocalTime ora=resultSet.getTime("ora_programarii").toLocalTime();

                // Obține lista de medicamente
                List<String> medicamente = Arrays.asList(resultSet.getString("medicamente").split(","));
                String doctor_nume=resultSet.getString("doctor_nume");

                //Cream obiectul cu datele luate din baza de date
                pacient = new Pacient(nume1, dataNas, CNP, istoric, diagnostic, medicamente, tel, data, ora,doctor_nume);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacient;

    }

    /**
     * Metoda pentru a cauta un pacient dupa cnp si a il sterge din baza de date
     * @param cnp cnp-ul pacientului care trebuie sters
     * @return true daca s-a gasit pacientul si s-a sters, altfel false
     * @throws SQLException daca apare o eroare in timpul interactiunii cu baza de date
     */
    public boolean stergePacient(String cnp) throws SQLException {
        //Declaram interogarea SQL pentru a sterge un pacient pe baza CNP-ului
        String sql = "DELETE FROM Pacient WHERE cnp LIKE ?";
        //verificam daca conexiunea este deschisa sau nu
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
        }
        //Executam blocul de interogare SQL pentru a sterge pacientul din baza de date
        try (Connection connection = BazaDeDate.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            //Setam cnp-ul in interogarea SQL
            stmt.setString(1, cnp);
            //Executam interogarea si obtinem numarul de randuri afectate
            int rowsAffected = stmt.executeUpdate();
            //Daca numarul de randuri afectate este mai mare de 0, pacientul a fost sters cu succes
            return rowsAffected > 0; // Returneaza true daca stergerea a fost efectuata
        }
    }


}
