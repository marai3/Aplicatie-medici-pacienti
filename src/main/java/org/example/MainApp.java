package org.example;

import java.sql.SQLException;
/**
 * Clasa `MainApp` este punctul de intrare in aplicatie, initializeaza baza de date si porneste interfata grafica a utilizatorului
 */
public class MainApp {
    public static void main(String[] args) {
        //initializeaza baza de date, creeaza tabelele si conexiunile necesare
       BazaDeDate.initialize();
        //creaza si afiseaza interfata grafica a utilizatorului
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainGUI appGUI = null;
            try {
                appGUI = new MainGUI();
            } catch (SQLException e) {
                //Trateaza posibilele erori legate de SQL
                throw new RuntimeException(e);
            }
            //Afisam fereastra GUI
            appGUI.show();
        });
    }
}

