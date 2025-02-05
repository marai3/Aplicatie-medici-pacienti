
# Medica
### Iancu Mara-Ștefania

## Descriere
Sistem de gestionare a unui cabinet medical. Acesta aplicație
are ca scop organizarea și gestionarea datelor pacienților de către medic. Medicul poate
adăuga, edita și șterge un pacient, poate căuta pacienții înregistrați după nume și poate
vizualiza informații despre pacient, precum numele, diagnostic, data și ora programării,
istoric, etc.

## Obiective
  Scopul principal al proiectului este de a dezvolta o aplicație pentru gestionarea pacienților și a doctorilor dintr-un sistem medical, care să ajute la o organizarea mai bună.
  Aplicația:
  - Permite gestionarea pacienților (autentificare, adăugare, modificare, ștergere)
  - Permite gestionarea doctorilor (adăugare, autentificare, vizualizare informații)
  - Oferă o interfață grafică intuitivă pentru utilizatori
  - Permite căutarea pacienților după nume

## Arhitectura
  Pentru baza de date am folosit Derby, iar pentru interfata Swing
  ![diagrama](diagrama.png)
## Functionalitati/Exemple utilizare
Cazuri de utilizare:
- Adăugare pacient – medicul poate adăuga un pacient
- Programare pacient – medicul poate programa un pacient
- Ștergere pacient – medical poate șterge un pacient dupa CNP împreuna cu toate
informațiile despre acesta
- Căutare pacient – medicul poate căuta un pacient dupa nume și poate vizualiza
detaliile acestuia

Ecranele aplicației:
- Start: unde utilizatorul poate alege să se autentifice ca medic sau pacient
- Autentificare pacient: pacientul poate să se autentifice cu numele și cnp-ul ca parola
- Autentificare medic: medicul se poate autentifică cu email-ul și parola sau poate alege să se înregistreze
- Inregistrare medic: medicul trebuie să scrie numele, email-ul, parola și specializarea
- Principal Medic: medicul poate alege să caute un pacient, adauge unul, să caute unul după nume și să programeze
- Principal Pacient: pacientul poate să iși vizualizează detaliile 
  
