# Project Petunia Überblick  

Das Projekt Petunia ist ein Börsensimulationsspiel.

Das Spiel wird im Internet gespielt. Die Spieler können auf ihr Konto und Portfolio zugreifen, wie man es von "normalen" Banking-Apps gewöhnt ist. Mittels einer Browser-App können sie dort auch ihre Kauf- und Verkauforders anlegen.

## Spielidee
Das Spiel besteht aus folgenden Elementen:

### Player
Die Spieler sind die Marktteilnehmer an der Börse. Sie können jederzeit in das Spiel eintreten und erhalten einen bestimmten Betrag an Spielgeld.

### Shares
An der fiktiven Börse können fiktive Anteilscheine an den Mannschaften der 1. deutschen Fußballbundesliga gehandelt werden. Zum Spielstart gibt es pro Mannschaft eine festgelegte Anzahl an Anteilen, die die Spieler handeln können.

### Dividenden
Nach den realen, tatsächlichen Spieltagen in der Fußballbundesliga werden Dividenden ausgezahlt. Hierbei soll für das Spiel berücksichtigt werden, ob die in der Tabelle aktuell weiter unten stehende Mannschaft gegen eine weiter oben stehende Mannschaft gewonnen hat und ob der Sieger sogar einen Auswärtssieg errungen hat.

### Orderbuch
Das Orderbuch ist der Service, der die eingehenden Orders entgegennimmt und abwickelt. Mit den historischen Orderdaten sollen auch die Marktveränderungen in klassischen Börsencharts angezeigt werden können.

### Clearing
Der Clearingservice verwaltet die Wertpapiere und Konten der Spieler. Er fungiert als Bank und Clearingstelle.

### Die Zentralbank und Börsenaufsicht
Die fiktive Zentralbank hat die Aufgabe durch Markt-Eingriffe im Spiel notwenige Regulierungen durchzuführen - beispielsweise Inflationsbekämpfung.

### Die Börse
Die Börse ist die zentrale Game-Engine. Sie stellt die Verbindung zwischen Orderbuch, Clearing und Zentralbank her. Sie ist in dieser Simulation auch für die Auszahlung der Dividenden zuständig, nachdem sie mit den wahren Spielergebnissen gefüttert wurde.

### Technische Umsetzung

Die Frontends sind mit React geschrieben. Alle Microservices sind Spring Boot / Kotlin Webapps, die mittels REST-Schnittstellen oder über Nakadi Event-Streams kommunizieren. Das Orderbuch und die Clearingstelle speichern ihre Daten in einer PostgreSQL Datenbank.

Orderbuch, Clearing, Zentralbank und Börse sind in der ersten Version in einem Monolithen implementiert, der auf der Hexagonal Architektur basiert. 

Die Benutzerverwaltung und der Login werden ein Keycloak Server sein.

Deployment soll zum lokalen Testen auf minikube und im live-Betrieb auf einem k3s stattfinden. Dazu soll es helm charts geben.

Für die Komponenten gibt es zusätzlich zu diesem Dokument spezielle Spezifikationen im Ordner `specs`.
