package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.BazaDeDate.connection;

/**
 * Clasa MainGui se ocupa cu initializarea si gestionarea interfetei
 * Aceasta se conecteza la baza de date si configureaza fereastra principala si layoutul
 */

public class MainGUI {
    Connection c;
     JFrame mainFrame;
     CardLayout cardLayout;
     JPanel mainPanel;
    /**
     * Constructorul MainGUI initializeaza interfata grafica si stabileste conexiunea cu baza de date.
     * @throws SQLException daca apare o eroare la conectarea la baza de date.
     */
    public MainGUI() throws SQLException {
            c = BazaDeDate.getConnection();
            initializareGUI();
    }
    /**
     * Meotoda initializareGUI configureaza și initializeaza componentele principale ale interfetei grafice.
     * Creeaza ferestre, layout-uri si panouri, inclusiv logica pentru gestionarea utilizatorilor medici si pacienti.
     * Configurarea include dimensiunea ferestrei, panourile principale si butoanele pentru navigare.
     * @throws SQLException daca apare o eroare legata de baza de date.
     */
    void initializareGUI() throws SQLException {
        //creez fereastra principala si ii ofer dimensiuni
        mainFrame = new JFrame("Medica");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        //Creez un pacient pentru fereastra principala a pacientului
        List<String> m1=new ArrayList<>();
        m1.add("Tanakan 40 mg 1-1-1");
        m1.add("Piracetam 40 mg 2-1-0");
             Doctor doc1= new Doctor("Amina Alina", "amina.alina@gmail.com", "amina1990", "neurologie");
        Pacient pacient1=new Pacient("Alexandra Popa", LocalDate.of(2004,12,15), "6050201040055","Pacient cunoscut cu HTA, adenom de prostata si insuficienta venoasa" +
                "cronica, se prezinta in serviciul nostru pentru vertij si instabilitate posturala", "Infarcte cerebrale politopice lacunaremicroangiopatie", m1, "0756666386",  LocalDate.of(2025, 10, 12), LocalTime.of( 10, 30), doc1.getNumeD());


        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //Panoul de start (alegere intre Medic si Pacient)
        JPanel startPanel = createStartPanel();

        //Panourile de autentificare
        JPanel medicLoginPanel = medicLoginPanel("Autentificare Medic");
        JPanel pacientLoginPanel = pacientLoginPanel("Autentificare Pacient");
        JPanel signUpPanel = signUpPanel();

        //Panourile principale (dupa autentificare)
        JPanel DmainMenuPanel = DoctorMainMenuPanel();
        JPanel PmainMenuPanel= PacientMainMenuPanel(pacient1);


        //Adaugam panourile la CardLayout
        mainPanel.add(startPanel, "Start");
        mainPanel.add(medicLoginPanel, "LoginMedic");
        mainPanel.add(signUpPanel, "SignUp");
        mainPanel.add(pacientLoginPanel, "LoginPacient");
        mainPanel.add(DoctorMainMenuPanel(), "MeniuDoctor");
        mainPanel.add(PacientMainMenuPanel(pacient1), "MeniuPacient");

        mainFrame.add(mainPanel);
    }
    /**
     * Creeaza panoul de start al interfeței grafice, unde utilizatorii pot alege între autentificarea ca Medic sau Pacient.
     * @return JPanel pentru pagina de start.
     */
    JPanel createStartPanel() {
        //Cream panoul principal cu un BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(147,112,219));

        //Textul din partea de sus
        JLabel welcomeLabel = new JLabel("Cine se autentifică?");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26)); // Font modern și mare
        welcomeLabel.setForeground(Color.BLACK); // Culoare text
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Aliniere la stânga
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(140, 20, 20, 20)); // Margini
        panel.add(welcomeLabel, BorderLayout.NORTH); // Adăugăm textul în zona de sus

        //Panoul pentru butoane
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Aliniere verticală
        buttonPanel.setBackground(new Color(147,112,219));

        //Butonul pentru medic
        JButton medicButton = new JButton("Medic");
        medicButton.setFont(new Font("Arial", Font.BOLD, 23));
        medicButton.setBackground(new Color(75,0,130));
        medicButton.setForeground(Color.WHITE);
        medicButton.setFocusPainted(false);
        medicButton.setBorder(BorderFactory.createEmptyBorder(10, 92, 10, 92)); // Margini
        medicButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aliniere pe centru
        medicButton.addActionListener( e-> {
            cardLayout.show(mainPanel, "LoginMedic");
        });

        //Butonul pentru pacient
        JButton pacientButton = new JButton("Pacient");
        pacientButton.setFont(new Font("Arial", Font.BOLD, 23));
        pacientButton.setBackground(new Color(75,0,130));
        pacientButton.setForeground(Color.WHITE);
        pacientButton.setFocusPainted(false);
        pacientButton.setBorder(BorderFactory.createEmptyBorder(10, 85, 10, 85)); // Margini
        pacientButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aliniere pe centru
        pacientButton.addActionListener( e-> {
            cardLayout.show(mainPanel, "LoginPacient");
        });

        //Adaugam butoanele in panoul pentru butoane
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        buttonPanel.add(medicButton); // Adăugăm butonul "Medic"
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spațiu între butoane
        buttonPanel.add(pacientButton); // Adăugăm butonul "Pacient"
        buttonPanel.add(Box.createVerticalGlue()); // Adăugăm spațiu flexibil de jos

        // Adaugam panoul cu butoanele in centrul panoului principal
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }
    /**
     * Creez panoul pentru log in al medicului, unde medicul se autentifica si poate trece la panoul principal al medicului
     * dupa ce se autentifica. Avem optiunile de back pentru a ne intoarce la panoul de start sau optiunea de Sign up, unde
     * medicii se pot inregistra
     * Panoul include titlul "Autentificare medic", si doua campuri pentru introducerea email-ului si parolei, dar si
     * butonul de log in
     * @param title reprezinta titlul care va fi afisat in partea de sus
     * @return JPanel care contine componentele de logare a medicului
     */
    JPanel medicLoginPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(147, 112, 219));

        //Panel pentru partea de sus (titlu+butonul Back+Sign up)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(147, 112, 219));

        //Butonul Back
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setBackground(new Color(75, 0, 130));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.addActionListener(e -> {
            // Funcționalitatea butonului Back
            cardLayout.show(mainPanel, "Start");
        });
        //Butonul de Sign up
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 15));
        signUpButton.setBackground(new Color(75, 0, 130));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setPreferredSize(new Dimension(100, 30));
        signUpButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "SignUp");
        });

        //Adauga butonul Back in coltul din stanga si butonul Sign Up in coltul din dreapta
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(new Color(147, 112, 219));
        backButtonPanel.add(backButton);
        JPanel SignButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        SignButtonPanel.setBackground(new Color(147, 112, 219));
        SignButtonPanel.add(signUpButton);

        //Titlul
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(90, 50, 10, 50));


        //Adaugam butonul Back, sign up si titlul în topPanel
        topPanel.add(SignButtonPanel, BorderLayout.EAST);
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        //Adaugam topPanel în partea de sus a panel-ului principal
        panel.add(topPanel, BorderLayout.NORTH);
            //Creez un alt JPanel cu alt layout pt organizarea campurile email, parola si buton de login
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBackground(new Color(147, 112, 219));
            //Creez label-ul si text field-uul pentru email, totodata si culoarea, dimensiunea, pozitia
            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setForeground(Color.WHITE);
            emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JTextField mailField = new JTextField(20);
            mailField.setMaximumSize(new Dimension(300, 30));
        //Creez label-ul si text field-ul pentru parola, totodata si culoarea, dimensiunea, pozitia
            JLabel passwordLabel = new JLabel("Parolă:");
            passwordLabel.setForeground(Color.WHITE);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JPasswordField passwordField = new JPasswordField(20);
            passwordField.setMaximumSize(new Dimension(300, 30));
            //Creez butonul de login, adaug culoare, font, pozitie
            JButton loginButton = new JButton("Login");
            loginButton.setFont(new Font("Arial", Font.BOLD, 23));
            loginButton.setBackground(new Color(75, 0, 130));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            //Ofer butonului sa produca o actiune at cand este apasata, sa deschida ferestra main pentru medici
        loginButton.addActionListener(e -> {
            String email = mailField.getText();
            String parola = new String(passwordField.getPassword());
            //verific daca doctorul exista in baza de date apeland functia autentificareDoctor
            Doctor doctor = new Doctor();
            boolean autenticat = doctor.autentificareDoctor(email, parola);

            if (autenticat) {
                cardLayout.show(mainPanel, "MeniuDoctor");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Email sau parolă incorecte!");
            }
        });
            //Adaug totul la formPanel
            formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            formPanel.add(emailLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(mailField);
            formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            formPanel.add(passwordLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(passwordField);
            formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            formPanel.add(loginButton);
            //dupa care adaug la panel-ul cu BorderLayout si returnez
            panel.add(formPanel, BorderLayout.CENTER);

            return panel;
        }
    /**
     * Creez un panou pentru inregistrarea unui medic cu campuri pentru nume, email, parola si specializare.
     * La completarea cu succes a formularului, se adauga medicul in baza de date si se redirectioneaza utilizatorul la pagina de login.
     * @return JPanel care contine toate componentele pentru inregistrarea medicului.
     */
    public JPanel signUpPanel() {
        //Creez un panel principal cu BoxLayout pentru a aranja elementele vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(147, 112, 219));

        //Componentele pentru formularul de inregistrare
        JLabel lblNume = new JLabel("Nume:");
        lblNume.setForeground(Color.WHITE);
        lblNume.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNume = new JTextField(20);
        txtNume.setMaximumSize(new Dimension(300, 30));

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtEmail = new JTextField(20);
        txtEmail.setMaximumSize(new Dimension(300, 30));

        JLabel lblParola = new JLabel("Parolă:");
        lblParola.setForeground(Color.WHITE);
        lblParola.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField txtParola = new JPasswordField(20);
        txtParola.setMaximumSize(new Dimension(300, 30));

        JLabel lblSpecializare = new JLabel("Specializare:");
        lblSpecializare.setForeground(Color.WHITE);
        lblSpecializare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtSpecializare = new JTextField(20);
        txtSpecializare.setMaximumSize(new Dimension(300, 30));

        //Butonul de inregistrare
        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(new Font("Arial", Font.BOLD, 23));
        btnSignUp.setBackground(new Color(75, 0, 130));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFocusPainted(false);
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Adaugam componentele in panel
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblNume);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(txtNume);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(txtEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblParola);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(txtParola);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblSpecializare);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(txtSpecializare);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnSignUp);

        // Functionalitatea butonului de inregistrare
        btnSignUp.addActionListener(e -> {
            String nume = txtNume.getText().trim();
            String email = txtEmail.getText().trim();
            String parola = new String(txtParola.getPassword()).trim();
            String specializare = txtSpecializare.getText().trim();

            //Verificam dacă toate campurile sunt completate
            if (nume.isEmpty() || email.isEmpty() || parola.isEmpty() || specializare.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Toate câmpurile sunt obligatorii!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                //Cream obiectul doctorDAO pentru a adauga medicul in baza de date
                Doctor doctor = new Doctor();
                doctor.adaugaDoctor(nume, email, parola, specializare);

                //Reseteaza campurile
                txtNume.setText("");
                txtEmail.setText("");
                txtParola.setText("");
                txtSpecializare.setText("");

                // Redirectioneaza utilizatorul catre pagina de login
                cardLayout.show(mainPanel, "LoginMedic");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Eroare la crearea contului: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    /**
     * Creez un panel pentru autentificarea pacientului, cu campuri pentru nume și parolă (CNP).
     * La autentificarea reusita, se redirectioneaza pacientul catre meniul principal.
     * @param title titlul care va fi afisat in partea de sus a panel-ului.
     * @return un obiect de tip JPanel care conține toate componentele pentru autentificarea pacientului.
     * @throws SQLException daca exista erori de acces la baza de date.
     */
    JPanel pacientLoginPanel(String title) throws SQLException {
        //Cream panelul principal cu BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(147, 112, 219));

        //Panel pentru partea de sus (titlu + butonul Back)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(147, 112, 219));

        //Butonul Back
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setBackground(new Color(75, 0, 130));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.addActionListener(e -> {
            // Funcționalitatea butonului Back
            cardLayout.show(mainPanel, "Start");
        });

        //Adaugă butonul Back in coltul din stanga
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(new Color(147, 112, 219));
        backButtonPanel.add(backButton);

        //Titlul
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(90, 50, 10, 135));


        //Adaugam butonul back si titlul in topPanel
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Adaugam topPanel in partea de sus a panoului principal
        panel.add(topPanel, BorderLayout.NORTH);

        //Creez panel pentru autentificare
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(147, 112, 219));
        //Adaaugam text si text area pentru nume, centram le dam culoare, dimensiuni
        JLabel numeLabel = new JLabel("Nume:");
        numeLabel.setForeground(Color.WHITE);
        numeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField numeField = new JTextField(20);
        numeField.setMaximumSize(new Dimension(300, 30));
        //Adaugam text si text area pentru parola, centram le dam culoare, dimensiuni
        JLabel passwordLabel = new JLabel("Parolă (CNP):");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 30));
        //Adaug butonul de login, ii ofer culoare, marime ..
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 23));
        loginButton.setBackground(new Color(75, 0, 130));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Pacient pacient=new Pacient();
        //Adaug actiune butonului de a trece la fereastra principala pentru pacienti
        //verific conexiunea la baza de date
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
        }
            loginButton.addActionListener(e -> {
                String nume = numeField.getText();
                String cnp = new String(passwordField.getPassword());

                //Verifica autentificarea, daca exista pacientul in baza de date
                pacient.PacientCNP(nume, cnp);

                if (pacient != null) {
                    System.out.println("Autentificare reușită! Pacientul: " + pacient.getNume());

                    // Afișează meniul principal cu datele pacientului
                    JPanel pacientMainMenuPanel = PacientMainMenuPanel(pacient);
                    cardLayout.show(mainPanel, "MeniuPacient");
                    mainPanel.removeAll();  // Indeparteaza orice alt panou
                    mainPanel.add(pacientMainMenuPanel);  // Adaugă panoul pacientului
                    mainPanel.revalidate();  // Actualizeaza vizualizarea
                    mainPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "CNP sau nume incorect!");
                }
            });
        //Adaug totul in formPanel si folosesc niste "cutii" pentru a organiza mai bine label-urile si textul
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(numeLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(numeField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(loginButton);
            //Adaug formPanel-ul la panel si il centrez apoi il returnez
            panel.add(formPanel, BorderLayout.CENTER);
            return panel;
        }

    /**
     * Creez un panou care reprezinta meniul principal pentru doctor care contine optiuni pentru adaugarea pacientului,
     * programarea acestuia, cautarea pacientului, ștergerea acestuia si un buton de log out din aplicație care ne
     * intoarce la panoul de start.
     * @return JPanel care contine toate componentele pentru meniul principal al doctorului.
     */
    JPanel DoctorMainMenuPanel() {
        //Creez un panel cu gridlayout
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBackground(new Color(75, 0, 130));
        //creez label pentru titlu
        JLabel menuLabel = new JLabel("Meniu Principal", JLabel.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 23));
        menuLabel.setForeground(Color.WHITE);
        //creez un panel cu gridlayout unde in coltul din stanga sus va fi butonul de log out
        JPanel jp= new JPanel(new GridLayout(2,2));
        JPanel jp1=new JPanel();
        jp1.setBackground(new Color(75, 0, 130));
            JPanel jp3=new JPanel();
            jp3.setBackground(new Color(75, 0, 130));
            JPanel jp4=new JPanel();
            jp4.setBackground(new Color(75, 0, 130));
            jp.add(jp1);
            JButton logoutButton = new JButton("Logout");
            logoutButton.setFont(new Font("Arial", Font.BOLD, 23));
            logoutButton.setBackground(new Color(105, 100, 230));
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFocusPainted(false);
            // Functionalitatea butonului log out
            logoutButton.addActionListener(e -> {
                cardLayout.show(mainPanel, "Start");
            });
            jp.add(logoutButton);
            jp.add(jp3);
            jp.add(jp4);
        jp.setBackground(new Color(75, 0, 130));
        //creez butoanele, le ofer font, culoare si dimensiune
        JButton adaugaButton = new JButton("Adaugă Pacient");
        adaugaButton.setFont(new Font("Arial", Font.BOLD, 19));
        adaugaButton.setBackground(new Color(142, 112, 219));
        adaugaButton.setForeground(Color.BLACK);

        JButton progButton = new JButton("Programează Pacienți");
        progButton.setFont(new Font("Arial", Font.BOLD, 19));
        progButton.setBackground(new Color(142, 112, 219));
        progButton.setForeground(Color.BLACK);

        JButton cautaButton = new JButton("Caută Pacient");
        cautaButton.setFont(new Font("Arial", Font.BOLD, 19));
        cautaButton.setBackground(new Color(142, 112, 219));
        cautaButton.setForeground(Color.BLACK);

        JButton stergeButton = new JButton("Șterge Pacient");
        stergeButton.setFont(new Font("Arial", Font.BOLD, 19));
        stergeButton.setBackground(new Color(142, 112, 219));
        stergeButton.setForeground(Color.BLACK);

        //Functionalitati pentru fiecare buton
        adaugaButton.addActionListener(e -> AdaugaPacient());
        progButton.addActionListener(e -> {
            try {
                ProgrameazaPacienti();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        cautaButton.addActionListener(e -> {
            try {
                CautaPacient();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        stergeButton.addActionListener(e -> StergePacient());
        //Adaug label-ul si butoanele in panel
        panel.add(menuLabel);
        panel.add(jp);
        panel.add(adaugaButton);
        panel.add(progButton);
        panel.add(cautaButton);
        panel.add(stergeButton);
        return panel;
    }

    /**
     * Creez un panou principal care afiseaza detaliile pacientului, panoul contine si un buton de logout
     * @param pacient obiectul Pacient care contine datele ce vor fi afisate
     * @return un JPanel care contine detaliile pacientului si butonul de logout
     */
    JPanel PacientMainMenuPanel(Pacient pacient) {
        //JPanel pentru afisarea detaliilor
        JPanel detaliiPanel = new JPanel();
        detaliiPanel.setLayout(new BoxLayout(detaliiPanel, BoxLayout.Y_AXIS));
        detaliiPanel.setBackground(new Color(147, 112, 219));

        //Panel pentru partea de sus pt butonul de log out
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(147, 112, 219));

        //Butonul Log out
        JButton logoutButton = new JButton("Log out");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(75, 0, 130));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(80, 30));
        logoutButton.addActionListener(e -> {
            //Functionalitatea butonului logout
            cardLayout.show(mainPanel, "Start");  // Schimbă la panoul "Start"
            mainPanel.removeAll();  // Îndepărtează orice alt panou
            mainPanel.add(createStartPanel());  // Adaugă panoul pacientului
            mainPanel.revalidate();  // Actualizează vizualizarea
            mainPanel.repaint();
        });

        //Cream un panou pentru butonul de logout
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutButtonPanel.setBackground(new Color(147, 112, 219));
        logoutButtonPanel.add(logoutButton);

        //Adaugam logoutButtonPanel in topPanel
        topPanel.add(logoutButtonPanel, BorderLayout.WEST);

        //Adaugam topPanel in partea de sus a panel-ului principal
        detaliiPanel.add(topPanel);  // Adăugăm topPanel în detailsPanel

        //Fonturi si culori pentru text
        Color textColor = Color.WHITE;
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font valueFont = new Font("Arial", Font.PLAIN, 16);

        //Adaugam campurile de detalii
        formatPanel(detaliiPanel, "Nume: ", pacient.getNume(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Data Nașterii: ", pacient.getDataNas().toString(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "CNP: ", pacient.getCNP(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Istoric Medical: ", pacient.getIstoric(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Diagnostic: ", pacient.getDiagnostic(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Medicamente: ", String.join(", ", pacient.getMedicamente()), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Data programare: ", pacient.getData().toString(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Ora programare: ", pacient.getOra().toString(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Telefon: ", pacient.getTel(), labelFont, valueFont, textColor);
        formatPanel(detaliiPanel, "Doctor: ", pacient.getDoctor(), labelFont, valueFont, textColor);

        //Adaugam un JScrollPane pentru a permite derularea daca este nevoie
        JScrollPane scrollPane = new JScrollPane(detaliiPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(147, 112, 219));

        //Cream un nou JPanel pentru cardul detaliilor pacientului
        JPanel pacientDetaliiPanel = new JPanel(new BorderLayout());
        pacientDetaliiPanel.setBackground(new Color(147, 112, 219));
        pacientDetaliiPanel.add(scrollPane, BorderLayout.CENTER);

        //Returnam JPanel-ul creat
        return pacientDetaliiPanel;
    }

    /**
     * Adauga o pereche eticheta-valoare intr-un panel.
     * Aceasta functie creeaza un sub-panel cu doua componente: o eticheta si o valoare, apoi adauga
     * acest sub-panel in panelul principal
     * @param panel JPanel-ul in care se vor adauga detaliile
     * @param labelText Textul etichetei
     * @param valueText Textul valorii corespunzatoare etichetei
     * @param labelFont Fontul pentru eticheta
     * @param valueFont Fontul pentru valoare
     * @param textColor Culoarea textului
     */
    void formatPanel(JPanel panel, String labelText, String valueText, Font labelFont, Font valueFont, Color textColor) {
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(new Color(147, 112, 219));

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(textColor);

        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        value.setForeground(textColor);

        detailPanel.add(label, BorderLayout.WEST);
        detailPanel.add(value, BorderLayout.CENTER);

        panel.add(detailPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

    }

    /**
     * Permite utilizatorului sa adauge un pacient in baza de date printr-un formular
     * Datele pacientului sunt colectate din campurile de text si textarea, iar acestea sunt adaugate in baza de date
     * folosind un PreparedStatement.
     * @throws SQLException daca apare o eroare in timpul operatiunii de interactiune cu baza de date
     */
    void AdaugaPacient() {
             //Campuri text si textarea
             JTextField nameField = new JTextField();
             JTextField birthdateField = new JTextField();
             JTextField cnpField = new JTextField();
             JTextArea historyArea = new JTextArea(5, 20);
             JTextArea diagnosisArea = new JTextArea(5, 20);
             JTextField medicationField = new JTextField();
             JTextField telField = new JTextField();
             JTextField progDataField = new JTextField();
             JTextField progTimpField = new JTextField();
             JTextField doctorNumeField = new JTextField();

             //Scroll pentru textareas
             JScrollPane historyScroll = new JScrollPane(historyArea);
             JScrollPane diagnosisScroll = new JScrollPane(diagnosisArea);

             //Setare culori si fonturi pentru campuri si etichete
             Font labelFont = new Font("Arial", Font.BOLD, 14);
             Font fieldFont = new Font("Arial", Font.PLAIN, 14);

             //Label-uri pentru fiecare camp
             JLabel nameLabel = new JLabel("Nume:");
             JLabel birthdateLabel = new JLabel("Data Nașterii (YYYY-MM-DD):");
             JLabel cnpLabel = new JLabel("CNP:");
             JLabel historyLabel = new JLabel("Istoric Medical:");
             JLabel diagnosisLabel = new JLabel("Diagnostic:");
             JLabel medicationLabel = new JLabel("Medicamente (separate prin virgulă):");
             JLabel telLabel = new JLabel("Telefon:");
             JLabel progDataLabel = new JLabel("Data programării (YYYY-MM-DD):");
             JLabel progTimpLabel = new JLabel("Ora programării (HH:mm):");
             JLabel doctorNumeLabel = new JLabel("Numele doctorului: ");
             //Setez font-ul pt fiecare label
             nameLabel.setFont(labelFont);
             birthdateLabel.setFont(labelFont);
             cnpLabel.setFont(labelFont);
             historyLabel.setFont(labelFont);
             diagnosisLabel.setFont(labelFont);
             medicationLabel.setFont(labelFont);
             telLabel.setFont(labelFont);
             progTimpLabel.setFont(labelFont);
             progDataLabel.setFont(labelFont);
             doctorNumeLabel.setFont(labelFont);

             //Panou pentru layout vertical
             JPanel formPanel = new JPanel();
             formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
             formPanel.setBackground(new Color(147, 112, 219));


             //Adaug in panou
             formPanel.add(nameLabel);
             formPanel.add(nameField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(birthdateLabel);
             formPanel.add(birthdateField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(cnpLabel);
             formPanel.add(cnpField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(historyLabel);
             formPanel.add(historyScroll);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(diagnosisLabel);
             formPanel.add(diagnosisScroll);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(medicationLabel);
             formPanel.add(medicationField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(telLabel);
             formPanel.add(telField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(progDataLabel);
             formPanel.add(progDataField);
             formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
             formPanel.add(progTimpLabel);
             formPanel.add(progTimpField);
             formPanel.add(doctorNumeLabel);
             formPanel.add(doctorNumeField);

             int option = JOptionPane.showConfirmDialog(mainFrame, formPanel, "Adaugă Pacient", JOptionPane.OK_CANCEL_OPTION);
             if (option == JOptionPane.OK_OPTION) {
                 try {
                     Pacient pacient =new Pacient();
                     // Preluare date
                     String nume = nameField.getText();
                     LocalDate dataNas = LocalDate.parse(birthdateField.getText());
                     String cnp = cnpField.getText();
                     String istoric = historyArea.getText();
                     String diagnostic = diagnosisArea.getText();
                     List<String> medicamente = List.of(medicationField.getText().split("\\s*,\\s*"));
                     String tel = telField.getText();
                     LocalDate programareData = LocalDate.parse(progDataField.getText());
                     LocalTime programareOra = LocalTime.parse(progTimpField.getText());
                     String doctorNume = doctorNumeField.getText();


                     //Adaug pacientul
                     int rows= pacient.adaugaPacient(nume, dataNas, cnp, istoric, diagnostic, medicamente, tel, programareData, programareOra, doctorNume);
                    if(rows<0){
                        JOptionPane.showMessageDialog(mainFrame, "A apărut o eroare la adăugarea pacientului.");
                    }
                 } catch (Exception e) {
                     JOptionPane.showMessageDialog(mainFrame, "Datele introduse nu sunt valide. Încercați din nou.", "Eroare", JOptionPane.ERROR_MESSAGE);
                 }
             }
         }

    /**
     * Permite utilizatorului sa caute pacienti in baza de date dupa nume, afiseaza o fereastra pentru
     * cautare cu campuri pentru selectarea criteriului si valoarea cautata
     * Dupa ce utilizatorul apasa butonul de cautare, rezultatele sunt afisate intr-un JTextArea
     * @throws SQLException daca apare o eroare la interactiunea cu baza de date
     */
    void CautaPacient() throws SQLException {
            //Cream un JFrame pentru cautare
            JFrame frame = new JFrame("Caută Pacient");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());
            frame.setBackground(new Color(142, 112, 219));

            //Creez label-urile si field-urile
            JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            searchPanel.setBackground(new Color(142, 112, 219));
            JLabel labelCriteriu = new JLabel("Caută pacient după:");
            labelCriteriu.setFont(new Font("Arial", Font.BOLD, 17));
            labelCriteriu.setForeground(Color.BLACK);
            JComboBox<String> criteriuCombo = new JComboBox<>(new String[]{"Nume"});
            criteriuCombo.setFont(new Font("Arial", Font.BOLD, 17));
            criteriuCombo.setForeground(Color.BLACK);
            JLabel labelValoare = new JLabel("Valoare:");
            labelValoare.setFont(new Font("Arial", Font.BOLD, 17));
            labelValoare.setForeground(Color.BLACK);
            JTextField campCautare = new JTextField();
            campCautare.setFont(new Font("Arial", Font.BOLD, 17));
            campCautare.setForeground(Color.BLACK);
            JButton butonCauta = new JButton("Caută");
            butonCauta.setFont(new Font("Arial", Font.BOLD, 17));
            butonCauta.setForeground(Color.BLACK);
            butonCauta.setBackground(new Color(75, 0, 130));

            //Adaugam componentele in panel
            searchPanel.add(labelCriteriu);
            searchPanel.add(criteriuCombo);
            searchPanel.add(labelValoare);
            searchPanel.add(campCautare);
            searchPanel.add(new JLabel());
            searchPanel.add(butonCauta);

            //JTextArea pentru afisarea rezultatelor
            JTextArea rezultatArea = new JTextArea();
            rezultatArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(rezultatArea);

            //Adaugam panourile in JFrame
            frame.add(searchPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            //Functionalitate pentru butonul de cautare
             butonCauta.addActionListener(e -> {
                 String criteriu = (String) criteriuCombo.getSelectedItem();
                 String valoare = campCautare.getText().trim();
                    //daca nu este scris in camp atunci apare un mesaj corespunzator
                 if (valoare.isEmpty()) {
                     JOptionPane.showMessageDialog(frame, "Câmpul de căutare nu poate fi gol!", "Eroare", JOptionPane.ERROR_MESSAGE);
                     return;
                 }
                 Pacient pacient=new Pacient();
                 List<Pacient> rezultate = null;
                 try {
                     rezultate = pacient.cautaPacienti(criteriu, valoare);
                 } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(frame, "Eroare. Încercați din nou.", "Eroare SQL", JOptionPane.ERROR_MESSAGE);
                 }
                    //verific daca am gasit un pacient
                 if (rezultate.isEmpty()) {
                     rezultatArea.setText("Nu au fost găsiți pacienți conform criteriului dat.");
                 } else {
                     StringBuilder rezultatText = new StringBuilder("Rezultatele căutării:\n\n");
                     for (Pacient p : rezultate) {
                         //Afisam doar cateva informatii
                         rezultatText.append("Nume: ").append(p.getNume())
                                 .append(", Diagnostic: ").append(p.getDiagnostic())
                                 .append(", Programare: ").append(p.getData())
                                 .append(", Ora: ").append(p.getOra())
                                 .append(", CNP: ").append(p.getCNP()).append("\n");
                     }
                     rezultatArea.setText(rezultatText.toString());
                 }
             });


             //Afisam fereastra
            frame.setVisible(true);
        }

    /**
     * Deschide o fereastra pentru programarea unui pacient, utilizatorul poate introduce CNP-ul pacientului,
     * data si ora dorita pentru programare
     * Daca pacientul exista in baza de date, programarea va fi actualizata; in caz contrar, va fi afișat
     * un mesaj de eroare
     * @throws SQLException daca apare o eroare de conexiune la baza de date sau in timpul actualizarii
     * informatiilor pacientului
     */
    void ProgrameazaPacienti() throws SQLException {
        //Cream un JFrame pentru programarea pacientului
        JFrame frame = new JFrame("Programează Pacient");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(142, 112, 219));

        //Creez un panel nou pentru programare, label-urile si field-urile
        JPanel programarePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        programarePanel.setBackground(new Color(142, 112, 219));
        JLabel labelCNP = new JLabel("CNP Pacient:");
        labelCNP.setFont(new Font("Arial", Font.BOLD, 17));
        labelCNP.setForeground(Color.BLACK);
        JTextField campCNP = new JTextField();
        campCNP.setFont(new Font("Arial", Font.BOLD, 17));
        campCNP.setForeground(Color.BLACK);

        JLabel labelData = new JLabel("Data programării (YYYY-MM-DD):");
        labelData.setFont(new Font("Arial", Font.BOLD, 17));
        labelData.setForeground(Color.BLACK);
        JTextField campData = new JTextField();
        campData.setFont(new Font("Arial", Font.BOLD, 17));
        campData.setForeground(Color.BLACK);

        JLabel labelOra = new JLabel("Ora programării (HH:MM:SS):");
        labelOra.setFont(new Font("Arial", Font.BOLD, 17));
        labelOra.setForeground(Color.BLACK);
        JTextField campOra = new JTextField();
        campOra.setFont(new Font("Arial", Font.BOLD, 17));
        campOra.setForeground(Color.BLACK);

        JButton butonProgrameaza = new JButton("Programează");
        butonProgrameaza.setFont(new Font("Arial", Font.BOLD, 17));
        butonProgrameaza.setForeground(Color.BLACK);
        butonProgrameaza.setBackground(new Color(75, 0, 130));

        //Adaugam componentele in panel
        programarePanel.add(labelCNP);
        programarePanel.add(campCNP);
        programarePanel.add(labelData);
        programarePanel.add(campData);
        programarePanel.add(labelOra);
        programarePanel.add(campOra);
        programarePanel.add(new JLabel());
        programarePanel.add(butonProgrameaza);

        //JTextArea pentru mesaje
        JTextArea mesajArea = new JTextArea();
        mesajArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mesajArea);

        //Adaugam panourile în JFrame
        frame.add(programarePanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        //Functionalitate pentru butonul de programare
        butonProgrameaza.addActionListener(e -> {
            String cnp = campCNP.getText().trim();
            String data = campData.getText().trim();
            String ora = campOra.getText().trim();

            if (cnp.isEmpty() || data.isEmpty() || ora.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Toate câmpurile sunt obligatorii!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //Daca nu exista o conexiune activa, o deschidem
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
                }

                //Validare pentru data și ora
                try {
                    LocalDate.parse(data);
                    LocalTime.parse(ora);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Formatul datei sau orei este invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Actualizare in baza de date

                    Pacient pacient =new Pacient();
                    int rows=pacient.editeazaPacient(data, ora, cnp);
                    if (rows > 0) {
                        mesajArea.setText("Pacientul cu CNP-ul " + cnp + " a fost programat cu succes la data " + data + " și ora " + ora + ".");
                    } else {
                        mesajArea.setText("Pacientul cu CNP-ul " + cnp + " nu a fost găsit în baza de date.");
                    }


            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "A apărut o eroare la căutarea pacienților: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }

        });

        // Afișăm fereastra
        frame.setVisible(true);
    }
    /**
     * Permite utilizatorului sa sterga un pacient din baza de date pe baza CNP-ului introdus
     * Daca pacientul exista in baza de date, acesta va fi sters, iar un mesaj de succes va fi afisat, altfel
     * va fi afisat un mesaj corespunzator
     * @throws SQLException daca apare o eroare in timpul interogarii bazei de date pentru stergerea pacientului
     */
    void StergePacient() {
        Pacient pacient=new Pacient();
    String cnp = JOptionPane.showInputDialog(mainFrame, "Introduceți CNP-ul pacientului de șters:");
    //Daca nu este introdus, apare un mesaj de avertizare
    if (cnp == null || cnp.isEmpty()) {
        JOptionPane.showMessageDialog(mainFrame, "CNP-ul nu poate fi gol.", "Eroare", JOptionPane.ERROR_MESSAGE);
        return;
    }
    //daca a fost gasit si sters apeland functia stergePacient atunci apare un mesaj de succes, altfel apare un mesaj de eroare
    try {
        boolean rezultat = pacient.stergePacient(cnp);
        if (rezultat) {
            JOptionPane.showMessageDialog(mainFrame, "Pacientul a fost șters cu succes.");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Pacientul cu CNP-ul specificat nu a fost găsit.", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(mainFrame, "Eroare la ștergerea pacientului: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
    }
}
    /**
     * Afișeaza fereastra principala a aplicatiei, setand-o ca vizibila
     */
    public void show() {
        mainFrame.setVisible(true);
    }

}
