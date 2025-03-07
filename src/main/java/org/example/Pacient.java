package org.example;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.BazaDeDate.connection;

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
    private Connection connection;
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
      /** Initializeaza conexiunea la baza de date folosind o conexiune existenta
       */
    public Pacient() {
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
     * Adaugam un pacient in baza de date specificand toate detaliile acestuia
     * @param nume
     * @param dataNas - data nasterii pacientului
     * @param cnp
     * @param istoric
     * @param diagnostic
     * @param medicamente
     * @param tel
     * @param programareData
     * @param programareOra
     * @param doctorNume - numele doctorului
     * @return
     */

    public int adaugaPacient(String nume, LocalDate dataNas, String cnp, String istoric, String diagnostic, List<String> medicamente, String tel, LocalDate programareData, LocalTime programareOra, String doctorNume) {
        String sql = "INSERT INTO Pacient (nume, data_nasterii, cnp, istoric, diagnostic, medicamente, telefon, data_programarii, ora_programarii, doctor_nume) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = BazaDeDate.getConnection().prepareStatement(sql)) {
            stmt.setString(1, nume);
            stmt.setDate(2, Date.valueOf(dataNas));
            stmt.setString(3, cnp);
            stmt.setString(4, istoric);
            stmt.setString(5, diagnostic);
            stmt.setString(6, String.join(", ", medicamente));
            stmt.setString(7, tel);
            stmt.setDate(8, Date.valueOf(programareData));
            stmt.setTime(9, Time.valueOf(programareOra));
            stmt.setString(10, doctorNume);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Cauta pacienti in baza de date in functie de criteriul specificat
     * Metoda va cauta pacienti in baza de date si va returna o lista cu pacientii care se potrivesc criteriului dat
     * @param criteriu Criteriul de cautare, de exemplu "Nume"
     * @param valoare valoarea criteriului ales
     * @return O lista cu pacientii care se potrivesc criteriului de cautare
     * @throws SQLException Dacă apare o eroare de conectare la baza de date sau in executarea interogarii
     */
    public List<Pacient> cautaPacienti(String criteriu, String valoare) throws SQLException {
        List<Pacient> pacientiGasiti = new ArrayList<>();
        String sql = "";

        if (criteriu.equals("Nume")) {
            sql = "SELECT * FROM Pacient WHERE nume LIKE ?";
        }
        try{
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            }
            try (PreparedStatement stmt = connection.prepareStatement(sql)){

                if (criteriu.equals("Nume")) {
                    stmt.setString(1, "%" + valoare + "%");}


                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        //Extragem datele pacientului și construim obiectul Pacient
                        String nume = rs.getString("nume");
                        LocalDate dataNas = rs.getDate("data_nasterii").toLocalDate();
                        String cnp = rs.getString("cnp");
                        String istoric = rs.getString("istoric");
                        String diagnostic = rs.getString("diagnostic");
                        String tel = rs.getString("telefon");
                        List<String> medicamente = Arrays.asList(rs.getString("medicamente").split(","));
                        LocalDate dataProgramare = rs.getDate("data_programarii").toLocalDate();
                        LocalTime oraProgramare = rs.getTime("ora_programarii").toLocalTime();


                        String doctor = rs.getString("doctor_nume");

                        Pacient pacient = new Pacient(nume, dataNas, cnp, istoric, diagnostic, medicamente, tel, dataProgramare, oraProgramare, doctor);
                        pacientiGasiti.add(pacient);
                    }
                }
                //daca nu au fost gasiti afisez un mesaj
                if (pacientiGasiti.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nu au fost găsiți pacienți pentru criteriul specificat: " + criteriu + " cu valoarea: " + valoare, "Căutare", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "A apărut o eroare la căutarea pacienților: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Conexiunea la baza de date nu a putut fi stabilită: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
        if (pacientiGasiti.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nu au fost găsiți pacienți pentru criteriul: " + criteriu + " cu valoarea: " + valoare, "Căutare", JOptionPane.INFORMATION_MESSAGE);
        }
        return pacientiGasiti;
    }

    /**
     * Modifica data si ora unei programari
     * @param data cu care va fi modificat
     * @param ora cu care va fi modificat
     * @param cnp pentru a cauta pacientul dupa cnp
     * @return
     * @throws SQLException
     */
    public int editeazaPacient(String data, String ora, String cnp) throws SQLException {
        String sql = "UPDATE Pacient SET data_programarii = ?, ora_programarii = ? WHERE cnp = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //Convertim datele in formate compatibile cu baza de date
            stmt.setDate(1, Date.valueOf(data));  // Setam data
            stmt.setTime(2, Time.valueOf(ora));   // Setam ora
            stmt.setString(3, cnp);               // Setam CNP-ul pacientului

            return stmt.executeUpdate();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return 0;
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
