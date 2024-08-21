# Accordo

Mobile Computing project - MSc Computer Science program

# Descrizione del Progetto

Il progetto è un prototipo di client per una piattaforma di discussione online, ispirata a sistemi come Discord ma con caratteristiche uniche. La piattaforma, chiamata **Accordo**, offre un unico forum di discussione globale, accessibile a tutti gli utenti. Questo forum è organizzato in più canali tematici, che possono essere creati liberamente dagli utenti. Ogni canale permette la pubblicazione e visualizzazione di messaggi di testo, immagini e la condivisione della posizione geografica degli utenti.

## Tecnologie e Competenze Utilizzate

- **Java**: Il linguaggio principale per lo sviluppo dell'applicazione, utilizzato per costruire la logica dell'app, gestire i dati e implementare le funzionalità di rete e interfaccia utente.

- **Android SDK**: Strumenti e API per lo sviluppo di applicazioni Android, utilizzati per creare un'applicazione nativa con interfaccia utente intuitiva e accesso alle funzionalità hardware del dispositivo.

- **Sviluppo per Android**: Le migliori pratiche per lo sviluppo di applicazioni mobili, con particolare attenzione alla gestione delle risorse di sistema, ottimizzazione delle prestazioni e compatibilità con diverse versioni di Android.

- **JSON**: Formato per lo scambio di dati tra client e server. Utilizzato per serializzare e deserializzare i dati provenienti dalla piattaforma, inclusi messaggi, immagini e informazioni sulla posizione.

- **Programmazione Orientata agli Oggetti (OOP)**: Paradigma di programmazione utilizzato per organizzare il codice in classi e oggetti, favorendo la modularità, la riusabilità e la manutenzione del codice.

- **XML**: Linguaggio utilizzato per definire i layout dell'interfaccia utente e configurare componenti dell'applicazione, come le risorse di stringhe, stili e temi.

- **Model-View-Controller (MVC)**: Pattern architetturale adottato per separare la logica di business, l'interfaccia utente e la gestione dei dati, facilitando la manutenzione e l'espansione del codice.

- **Multithreading**: Tecnica per eseguire operazioni in parallelo, utilizzata per mantenere l'applicazione reattiva durante operazioni come il caricamento di immagini o l'invio di messaggi.

- **Libreria Volley**: Strumento per la gestione delle richieste HTTP, utilizzato per comunicare con il server, inviare e ricevere messaggi, immagini e dati in tempo reale.

- **Acquisizione Posizione**: Funzionalità che consente agli utenti di condividere la loro posizione geografica all'interno dei canali, migliorando l'interazione sociale sulla piattaforma.

- **GooglePlayService**: API utilizzate per l'accesso ai servizi di localizzazione, autenticazione utente e altre funzionalità avanzate offerte da Google.

- **Room**: Libreria per la gestione del database SQLite locale, utilizzata per memorizzare i dati dell'applicazione in modo persistente e performante.

- **MapBox**: Strumento per la visualizzazione delle mappe, utilizzato per mostrare la posizione degli utenti all'interno dell'app e migliorare l'esperienza di condivisione della posizione.

## Funzionalità Principali

- **Forum Unificato**: Un unico forum globale diviso in canali tematici, accessibile a tutti gli utenti registrati.

- **Canali di Discussione**: Gli utenti possono creare e gestire canali di discussione, all'interno dei quali possono inviare messaggi di testo, immagini e condividere la propria posizione.

- **Condivisione della Posizione**: Ogni utente può condividere la propria posizione geografica all'interno di un canale, utilizzando le API di GooglePlayService e la visualizzazione tramite MapBox.

- **Gestione in Tempo Reale**: Grazie alla libreria Volley e al multithreading, l'applicazione gestisce in tempo reale l'invio e la ricezione di messaggi, immagini e dati di posizione, mantenendo un'interfaccia reattiva.

- **Architettura MVC**: Separazione della logica, dei dati e dell'interfaccia utente per un'applicazione modulare e facilmente manutenibile.

