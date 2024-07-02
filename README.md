# **1.Introduzione**

## ***1.1 Obiettivi del documento***

Il Ministero della Salute (MdS) metterà a disposizione degli Enti, da cui riceve dati, applicazioni SDK specifiche per flusso logico e tecnologie applicative (Java, PHP e C#) per verifica preventiva (in casa Ente) della qualità del dato prodotto.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.002.png)

Nel presente documento sono fornite la struttura e la sintassi dei tracciati previsti dalla soluzione SDK per avviare il proprio processo elaborativo e i controlli di merito sulla qualità, completezza e coerenza dei dati.

Gli obiettivi del documento sono:

- fornire una descrizione funzionale chiara e consistente dei tracciati di input a SDK;
- fornire le regole funzionali per la verifica di qualità, completezza e coerenza dei dati;

In generale, la soluzione SDK è costituita da 2 diversi moduli applicativi (Access Layer e Validation Engine) per abilitare

- l’interoperabilità con il contesto tecnologico dell’Ente in cui la soluzione sarà installata;
- la validazione del dato ed il suo successivo invio verso il MdS.

La figura che segue descrive la soluzione funzionale ed i relativi benefici attesi.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.003.png)

## ***1.2 Acronimi***

Nella tabella riportata di seguito sono elencati tutti gli acronimi e le definizioni adottati nel presente documento.


|**#**|**Acronimo / Riferimento**|**Definizione**|
| - | - | - |
|1|NSIS|Nuovo Sistema Informativo Sanitario|
|2|SDK|Software Development Kit|
|3|AVN|Anagrafe Nazionale Vaccini|
|4|SSN|Sistema Sanitario Nazionale|
|5|CI|Codice Identificativo|
|6|AIC|Autorizzazione alla Immissione in Commercio del vaccino in Italia rilasciata dall’Agenzia Italiana del Farmaco|


# **2. Architettura SDK**

## ***2.1 Architettura funzionale***

Di seguito una rappresentazione architetturale del processo di gestione e trasferimento dei flussi dall’ente verso l’area MdS attraverso l’utilizzo dell’applicativo SDK e il corrispondente diagramma di sequenza.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.004.jpeg)


1. L’utente dell’ente caricherà in una apposita directory (es. /sdk/input/) il flusso sorgente.  L’utente avvierà l’SDK passando in input una serie di parametri descritti in dettaglio al paragrafo 3.1
1. La compenente Access Layer estrae dalla chiamata dell’ente i parametri utilizzati per lanciare l’SDK,  genera un identificativo ID\_RUN, e un file chiamato “{ID\_RUN}.json” in cui memorizza le informazioni dell’esecuzione.
1. I record del flusso verranno sottoposti alle logiche di validazione e controllo definite nel Validation Engine. Nel processare il dato, il Validation Engine acquisirà da MdS eventuali anagrafiche di validazione del dato stesso.
1. Generazione del file degli scarti contenente tutti i record in scarto con evidenza degli errori riscontrati. I file di scarto saranno memorizzati in cartelle ad hoc (es. /sdk/esiti).
1. Tutti i record che passeranno i controlli verranno inseriti in un file xml copiato in apposita cartella (es /sdk/xml\_output), il quale verrà eventualmente trasferito a MdS utilizzando la procedura “invioFlussi” esposta da GAF WS (tramite PDI). A fronte di un’acquisizione, il MdS fornirà a SDK un identificativo (ID\_UPLOAD) che sarà usato da SDK sia per fini di logging che di recupero del File Unico degli Scarti.
1. A conclusione del processo di verifica dei flussi, il Validation Engine eseguirà le seguenti azioni:

 a. Aggiornamento file contenente il riepilogo dell’esito dell’elaborazione del Validation Engine e del ritorno dell’esito da parte di MdS. I file contenenti l’esito dell’elaborazione saranno memorizzati in cartelle ad hoc (es. /sdk/run).

 b. Consolidamento del file di log applicativo dell’elaborazione dell’SDK in cui sono disponibili una serie di informazioni tecniche (Es. StackTrace di eventuali errori).

 c. Copia del file generato al punto 5, se correttamente inviato al MdS, in apposita cartella (es. /sdk/sent).

Ad ogni step del precedente elenco e a partire dal punto 2, l’SDK aggiornerà di volta in volta il file contenente l’esito dell’elaborazione.

**Nota: l’SDK elaborerà un solo file di input per esecuzione.**

In generale, le classi di controllo previste su Validation Engine sono:

- Controlli FORMALI (es. correttezza dei formati e struttura record)
- Controlli SINTATTICI (es. check correttezza del Codice Fiscale)
- Controlli di OBBLIGATORIETÀ DEL DATO (es. Codice Prestazione su flusso…)
- Controlli STRUTTURE FILE (es. header/footer ove applicabile)
- Controlli di COERENZA CROSS RECORD
- Controlli di corrispondenza dei dati trasmessi con le anagrafiche di riferimento
- Controlli di esistenza di chiavi duplicate nel file trasmesso rispetto alle chiavi logiche individuate per ogni tracciato.

Si sottolinea che la soluzione SDK non implementa controlli che prevedono la congruità del dato input con la base date storica (es: non viene verificato se per un nuovo inserimento (Tipo = I) la chiave del record non sia già presente sulla struttura dati MdS).

## ***2.2 Architettura di integrazione***

La figura sottostante mostra l’architettura di integrazione della soluzione SDK con il MdS. Si evidenzia in particolare che:

- Tutti i dati scambiati fra SDK e MdS saranno veicolati tramite Porta di Interoperabilità (PDI);
- Il MdS esporrà servizi (API) per il download di dati anagrafici;
- SDK provvederà ad inviare vs MdS l’output (record validati) delle proprie elaborazioni. A fronte di tale invio, il MdS provvederà a generare un identificativo di avvenuta acquisizione del dato (ID\_UPLOAD) che SDK memorizzerà a fini di logging.


![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.006.png)

# **3 Funzionamento della soluzione SDK**

In questa sezione è descritta le specifica di funzionamento del flusso **AVX**  per l’alimentazione dello stesso.


## ***3.1 Input SDK***

In fase di caricamento del file verrano impostati i seguenti parametri che andranno in input al SDK in fase di processamento del file:


|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|ID CLIENT|Identificativo univoco della trasazione che fa richiesta all'SDK|100|Non definito|
|NOME FILE INPUT|Nome del file per il quale si richiede il processamento lato SDK|256|Non definito|
|ANNO RIFERIMENTO|Stringa numerica rappresentante l’anno di riferimento per cui si intende inviare la fornitura|4|Anno (Es. 2022)|
|TIPO TRASMISSIONE |Indica se la trasmissione dei dati verso MDS avverrà in modalità full (F) o record per record (R). Per questo flusso la valorizzazione del parametro sarà impostata di default a F|1|F/R|
|FINALITA' ELABORAZIONE|Indica se i flussi in output prodotti dal SDK verranno inviati verso MDS (Produzione) oppure se rimarranno all’interno del SDK e il processamento vale solo come test del flusso (Test)|1|Produzione/Test|
|CODICE REGIONE|<p>Individua la Regione a cui afferisce la struttura. Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.</p><p></p>|3|Es. 010|

Inoltre è previsto anche il parametro Periodo Riferimento, i cui valori differiscono in base al flusso specifico.
Di seguito la tabella specifica per il flusso AVX:



|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|PERIODO RIFERIMENTO|Stringa alfanumerica rappresentante il periodo per il quale si intende inviare la fornitura|2|Q1 ,Q2, Q3, Q4|

## ***3.2 Tracciato input a SDK***

Il flusso di input avrà formato **csv** posizionale e una naming convention libera a discrezione dell’utente che carica il flusso senza alcun vincolo di nomenclatura specifica (es: nome\_file.csv). Il separatore per il file csv sarà la combinazione di caratteri tra doppi apici: “~“

All’interno della specifica del tracciato sono indicati i dettagli dei campi di business del tracciato di input atteso da SDK, il quale differisce per i sei  flussi dell’area AVN. All’interno di tale file è presente la colonna **Posizione nel file** la quale rappresenta l’ordinamento delle colonne del tracciato di input da caricare all’SDK.


Di seguito la tabella in cui è riportata la specifica del tracciato di input per il flusso in oggetto:


|**Nome campo**|**Posizione nel File Input**|**Key**|**Descrizione**|**Tipo** |**Obbligatorietà**|**Informazioni di Dominio**|**Lunghezza campo**|**XPATH Tracciato Output**|
| :- | :- | :- | :-: | :-: | :-: | :-: | :-: | :-: |
|Codice Identificatico dell’Assistito|0|KEY|Codice identificativo dell’assistito|AN|OBB|Il campo deve avere lunghezza massima di 20 caratteri in input alla procedura di cifratura che produrrà un output di massimo 172 caratteri.<br>Le modalità di alimentazione del presente campo sono descritte nel paragarfo 3.6 Codice Identificativo dell’ Assistito – procedura di cifratura|172|**/informazioniAnagrafiche/Assistito/IdAssistito** |
|Modalita|1| |Campo tecnico utilizzato per distinguere le modalità di invio di: schede vaccinali di soggetti residenti in regione |A|OBB|Devono essere utilizzati i codici 'RE  per la trasmissione delle informazioni anagarfiche relative alla scheda vaccinale dei **soggetti residenti** nella regione che sta trasmettendo.|2|**/informazioniAnagrafiche/@Modalita**|
|Codice Regione|2|KEY|Individua la Regione che trasmette il dato. |AN|OBB|Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.I valori ammessi sono quelli riportati all’**Allegato 1 – Regioni**|3|<br>**/informazioniAnagrafiche/@CodiceRegione**|
|Validità codice Identificativo|3| |Informazione restituita dal servizio di verifica dei dati dell’assistito del sistema TS,  relativa alla presenza del codice identificativo dell’assistito nella banca dati TS |N|OBB|Valori ammessi:<br>0 = Codice identificativo valido (presente in banca dati);<br>1 = Codice identificativo errato (non presente in banca dati);|1|**/informazioniAnagrafiche/Assistito/ValiditaCI**|
|Tipologia codice identificativo|4| |Informazione restituita dal servizio di verifica dei dati dell’assistito del sistema TS relativa alla tipologia del codice identificativo dell’assistito nella banca dati TS |N|OBB|Valori ammessi:<br>0 = Codice fiscale<br>1 = Codice STP<br>2 = Codice ENI<br>3 = Codice TEAM<br>4 =Codice richiedente asilo<br>99 =Codice non presente in banca dati|2|**/informazioniAnagrafiche/Assistito/TipologiaCI**|
|Sesso|5| |Indica il sesso del soggetto assistito|N|OBB|Valori ammessi:<br>1 = maschio<br>2 = femmina<br>9 = non definito|1|**/informazioniAnagrafiche/Assistito/Sesso**|
|Data di nascita|6| |Data di nascita del soggetto assistito|D|OBB|Formato: AAAA-MM-GG<br>La data di Nascita non deve essere successiva alla Data di Somministrazione del Vaccino. <br>La differenza tra anno, mese e giorno di nascita e anno, mese e  giorno di somministrazione del vaccino non può essere superiore a 125 anni.|10|**/informazioniAnagrafiche/Assistito/DataNascita**|
|Comune di  Residenza|7| |Identifica il Comune nella cui anagrafe è iscritto l’assistito.|AN|OBB|Formato: NNNNNN<br>Il codice da utilizzare è il codice secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune.|6|**/informazioniAnagrafiche/Assistito/ComuneResidenza**|
|ASL di Residenza|8| |Indentifica la Azienda Sanitaria Locale che comprende il Comune in cui l’assistito ha la residenza. |AN|OBB|Il campo deve essere valorizzato con i codici a tre caratteri della ASL (di cui al D.M. 05/12/2006 e successive modifiche - Anagrafica MRA fase 1) utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.|3|**/informazioniAnagrafiche/Assistito/AslResidenza**|
|Regione di Residenza|9| |Identifica la regione cui appartiene il Comune in cui risiede l’assistito. |AN|OBB|Formato: NNN<br>I valori ammessi sono quelli a tre caratteri definiti con decreto del Ministero della sanità del 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n. 240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali. |3|**/informazioniAnagrafiche/Assistito/RegioneResidenza**|
|Stato di residenza|10| |Indica lo Stato estero presso cui ha la residenza l’assistito |AN|OBB|La codifica da utilizzare è quella Alpha2 (a due lettere) prevista dalla normativa ISO 3166-2.|2|**/informazioniAnagrafiche/Assistito/StatoEsteroResidenza**|
|Data Trasferimento Residenza|11| |Indica la data del cambio di residenza dell’assistito.|D|FAC|Formato: AAAA-MM-GG|10|**/informazioniAnagrafiche/Assistito/DataTrasferimentoResidenza**|
|Comune di Domicilio|12| |Comune in cui l'assistito ha il domicilio sanitario.|AN|NBB|Formato: NNNNNN<br>Il codice da utilizzare è il codice secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune. <br>Da valorizzare obbligatoriamente se valorizzato l’ASL di domicilio o la regione di domicilio e se il domicilio sanitario è diverso dalla residenza|6|**/informazioniAnagrafiche/Assistito/ComuneDomicilio**|
|ASL di domicilio sanitario|13| |Indentifica la Azienda Sanitaria che comprende il comune in cui l’assistito ha il domicilio sanitario|AN|NBB|Il codice da utilizzare è quello a tre caratteri della ASL di cui al decreto ministeriale 5 dicembre 2006, pubblicato nella Gazzetta Ufficiale n. 22 del 27 gennaio 2007, e successive modifiche (Anagrafica MRA fase 1), utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.<br>Da valorizzare obbligatoriamente se valorizzato il comune di domicilio o la regione di domicilio e se il domicilio sanitario è diverso dalla residenza |3|**/informazioniAnagrafiche/Assistito/AslDomicilio**|
|Regione di domicilio sanitario|14| |Indica la regione cui appartiene il comune in cui l’assistito ha il domicilio sanitario.|AN|NBB|I valori ammessi sono quelli a tre caratteri definito con decreto ministeriale 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n. 240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.<br>Da valorizzare obbligatoriamente se valorizzato il comune di domicilio o la ASL di domicilio e se il domicilio sanitario è diverso dalla residenza|3|**/informazioniAnagrafiche/Assistito/RegioneDomicilio**|
|Cittadinanza|15| |Identifica la cittadinanza dell’assistito|AN|OBB|La codifica da utilizzare è quella Alpha2 (a due lettere) prevista dalla normativa ISO 3166-2.<br>Ulteriori valori ammessi:<br>• XK= Kosovo;<br>• XX = Cittadinanza sconosciuta;<br>• ZZ = Apolidi.|2|**/informazioniAnagrafiche/Assistito/Cittadinanza**|
|Data decesso|16| |Indica la data di decesso dell’assistito|AN|FAC|Formato: AAAA-MM-GG<br>L’informazione può essere recuperata tramite il servizio TS del MEF|10|**/informazioniAnagrafiche/Assistito/DataDecesso**|
|Tipo Trasmissione |17| |Campo tecnico utilizzato per distinguere trasmissioni di informazioni nuove, modificate o eventualmente annullate|A|OBB|Valori ammessi: <br>I: Inserimento (per la trasmissione di informazioni nuove o per la ritrasmissione di informazioni precedentemente scartate dal sistema di acquisizione);<br>V: Variazione (per la trasmissione di informazioni per le quali si intende far effettuare una sovrascrittura dal sistema di acquisizione);<br>C: Cancellazione (per la trasmissione di informazioni per le quali si intende far effettuare una cancellazione dal sistema di acquisizione).|1|**/informazioniAnagrafiche/Assistito/TipoTrasmissione**|


## ***3.3 Controlli di validazione del dato (business rules)***

Di seguito sono indicati i controlli da configurare sulla componente di Validation Engine e rispettivi error code associati riscontrabili sui dati di input per il flusso AVX.

Gli errori sono solo di tipo scarti (mancato invio del record).

Al verificarsi anche di un solo errore di scarto, tra quelli descritti, il record oggetto di controllo sarà inserito tra i record scartati.

Business Rule non implementabili lato SDK:

- Storiche (Business Rule che effettuano controlli su dati già acquisiti/consolidati che non facciano parte del dato anagrafico)
- Transazionali (Business Rule che effettuano controlli su record, i quali rappresentano transazioni, su cui andrebbe garantito l’ACID (Atomicità-Consistenza-Isolamento-Durabilità))
- Controllo d’integrità (cross flusso) (Business Rule che effettuano controlli sui record utilizzando informazioni estratte da record di altri flussi)

Di seguito le BR per il flusso in oggetto:


|**CAMPO**|**FLUSSO**|**CODICE ERRORE**|**FLAG ATTIVAZIONE**|**DESCRIZIONE ERRORE**|**DESCRIZIONE ALGORITMO**|**TABELLA ANAGRAFICA**|**CAMPI DI COERENZA**|**SCARTI/ANOMALIE**|**TIPOLOGIA BR**| |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|Modalità|AVX|1801|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Modalità|AVX|1802|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : RE| | |Scarti|Basic| |
|Modalità|AVX|1803|ATTIVA|Modalità non coerente con il flusso inviato|in caso di flusso Residenti il naming del flusso conterrà Re e la modalità è diversa da RE| | |Scarti|Basic| |
|Tipo Anagrafica|AVX|1901|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Tipo Anagrafica|AVX|1902|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : I,i,V,v,C,c| | |Scarti|Basic| |
|Codice Regione |AVX|1921|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Codice Regione |AVX|1922|ATTIVA|Non appartenenza al dominio di riferimento|Il valore inserito e controllato non è presente in anagrafica regioni|Anagrafiche: REGIONE| |Scarti|Anagrafica| |
|Codice Regione |AVX|1905|ATTIVA|Il codice regione non coincide con la regione inviante.|Il campo Codice Regione non coincide con la regione che sta trasmettendo il file. | | |Scarti|Basic| |
|Codice Identificatico dell’Assistito|AVX|1701|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Codice Identificatico dell’Assistito|AVX|1703|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 172 caratteri| | |Scarti|Basic| |
|Validità codice Identificativo|AVX|1701|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Validità codice Identificativo|AVX|1702|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi:0,1| | |Scarti|Basic| |
|Tipologia codice identificativo|AVX|1601|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Tipologia codice identificativo|AVX|1602|ATTIVA|Non appartenenza al dominio di riferimento per un campo obbligatorio|Valori diversi da quelli ammessi:0,1,2,3,4,99| | |Scarti|Basic| |
|Sesso|AVX|1501|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Sesso|AVX|1502|ATTIVA|Non appartenenza al dominio di riferimento per un campo obbligatorio|Valori diversi da quelli ammessi :1,2,9| | |Scarti|Basic| |
|Data di nascita|AVX|1400|ATTIVA|Datatype errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |Scarti|Basic| |
|Data di nascita|AVX|1401|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Data di nascita|AVX|1935|ATTIVA|Data nascita successiva alla Data di trasmissione|Data nascita > Data trasmissione| | |Scarti|Basic| |
|Data di nascita|AVX|1940|ATTIVA|Data nascita successiva alla Data di decesso|Data nascita > Data Decesso se valorizzata| | |Scarti|Basic| |
|Comune di Residenza|AVX|1301|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Comune di Residenza|AVX|1303|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 6 caratteri| | |Scarti|Basic| |
|Comune di Residenza|AVX|1945|ATTIVA|Codice non presente nel dominio di riferimento|Il codice è valorizzaro e uguale a 999999 (residenti non in Italia)  o non è secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune.|Comuni Istat| |Scarti|Anagrafica| |
|Comune di Residenza|AVX|1950|ATTIVA|Comune di Residenza incoerente con Stato Estero di Residenza |Il codice è valorizzaro e uguale a 999999 (residenti all’estero)  e lo stato estero di Residenza è non nullo e uguale a ‘IT’| |Stato Estero di Residenza|Scarti|Basic| |
|Comune di Residenza|AVX|1955|ATTIVA|Comune di Residenza incoerente con Regione o ASL  di Residenza (solo per invii con modalità RE)|Il codice è valorizzaro e diverso da 999999 e nel dominio e il codice  ASL di residenza e/o  la Regione di residenza sono non nulli e valorizzati con 999 oppure sono non nulli e valorizzati con valori che non afferiscono al comune di residenza.|tabella di raccordo regioni-province-comuni-asl|Codice  ASL di residenza e/o  la Regione di residenza|Scarti|Anagrafica| |
|ASL di Residenza|AVX|1201|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|ASL di Residenza|AVX|1203|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri| | |Scarti|Basic| |
|ASL di Residenza|AVX|1960|ATTIVA|Codice non presente nel dominio di riferimento|Il codice è valorizzaro e uguale a 999  o non presente nell’anagrafe di riferimento (D.M. 05/12/2006 e successive modifiche – Anagrafica MRA fase 1)<br>**Approfondimento**: <br>la coppia da utlizzare per effettuare il controllo sulla tabella anagrafica "Anagrafiche: ASL" è:<br>` `(codice regione di residenza,codice asl di residenza|Anagrafiche: ASL| |Scarti|Anagrafica| |
|ASL di Residenza|AVX|1965|ATTIVA|Asl di Residenza incoerente con Stato Estero di Residenza|Il codice è valorizzaro correttamente ma lo stato estero di Residenza è non nullo e diverso da ‘IT’| |stato estero di Residenza|Scarti|Basic| |
|ASL di Residenza|AVX|1970|ATTIVA|Asl di Residenza incoerente con Regione o Comune  di Residenza (solo per invii con modalità RE)|Il codice è valorizzato e diverso da 999 e nel dominio e il codice  Comune di residenza è non nullo e valorizzato con 999999 e/o  la Regione di residenza è non nulla e valorizzata con 999 oppure sono non nulli e valorizzati con valori che non afferiscono alla ASL di residenza.  |tabella di raccordo regioni-province-comuni-asl|codice  Comune di residenza, Regione di residenza|Scarti|Basic| |
|Regione di Residenza|AVX|1101|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato| | |Scarti|Basic| |
|Regione di Residenza|AVX|1103|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri| | |Scarti|Basic| |
|Regione di Residenza|AVX|1975|ATTIVA|Codice non presente nel dominio di riferimento|Il codice è valorizzaro e diverso da 999 e non presente nell’anagrafe di riferimento delle regioni.|Anagrafice: REGIONE| |Scarti|Anagrafica| |
|Regione di Residenza|AVX|1980|ATTIVA|Regione di Residenza incoerente con Stato Estero di Residenza|Il codice è valorizzaro correttamente ma lo stato estero di Residenza è non nullo e non uguale a ‘IT’| |Stato Estero di Residenza|Scarti|Basic| |
|Regione di Residenza|AVX|1985|ATTIVA|Regione di Residenza incoerente con Comune o ASL  di Residenza|Il record **non viene scartato** se:<br><br>o Codice Regione e Codice Comune sono rispettivamente “999” e “999999”;<br><br>o Codice Regione diverso da “999”, Codice Comune diverso da “999999”, ed esiste una riga della tabella di raccordo che associa il Comune alla Regione.|tabella di raccordo regioni-comuni|Comune|Scarti|Anagrafica| |
|Regione di Residenza|AVX|1990|ATTIVA|Regione di Residenza incoerente con la modalità di trasmissione|Il codice è valorizzato e diverso da 999 e la Modalità è uguale a RE e la regione di residenza è diversa dal codice regione che sta trasmettendo il dato | |modalità di trasmissione|Scarti|Basic| |
|Stato di Residenza|AVX|3001|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato solo per Modalità diversa da CO.| | |Scarti|Basic| |
|Stato di Residenza|AVX|3003|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |Scarti|Basic| |
|Stato di Residenza|AVX|1995|ATTIVA|Codice non presente nel dominio di riferimento|Il codice è valorizzaro ed è diverso da "IT"|Codifica Alpha2| |Scarti|Anagrafica| |
|Stato di Residenza|AVX|2000|ATTIVA|Stato estero di residenza diverso da “IT” incoerente con Regione, comune e ASL di residenza|Il codice è valorizzato e diverso da IT (Italia), e Regione di residenza è non nullo e diverso da 999 e/o ASL di residenza è non nullo e diverso da 999 e/o Comune di residenza è non nullo e diverso da 999999| | |scarti|Basic| |
|Stato di Residenza|AVX|2005|ATTIVA|Stato estero di residenza uguale a “IT” incoerente con Regione, comune e ASL di residenza|Il codice è valorizzaro e uguale a IT (Italia), e Regione di residenza è non nullo e uguale a 999 e/o ASL di residenza è non nullo e uguale a 999 e/o Comune di residenza è non nullo e uguale a 999999| |Regione, comune e ASL di residenza|Scarti|Basic| |
|Data Trasferimento Residenza|AVX|6170|ATTIVA|Datatype errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |scarti|Basic| |
|Data Trasferimento Residenza|AVX|2010|ATTIVA|Data Trasferimento Residenza obbligatoria se modalita di tramissione è uguale a TR – Trasferimento in altra regione per cambio di residenza|La data non è valorizzato e la modalità di trasmissione è uguale a TR.| | |scarti|Basic| |
|Data Trasferimento Residenza|AVX|2020|ATTIVA|Data di trasferimento residenza antecedente alla data di nascita|Data trasferimento residenza se valorizzata < data di decesso se valorizzata| | |scarti|Basic| |
|Data Trasferimento Residenza|AVX|2025|ATTIVA|Data di trasferimento residenza successiva alla data di decesso|Data trasferimento residenza se valorizzata < data di decesso se valorizzata| | |scarti|Basic| |
|Data Trasferimento Residenza|AVX|2030|ATTIVA|Data di trasferimento residenza incoerente con la modalità di trasmissione|La data trasferimento residenza valorizzata e modalità di trasmisione = RE oppure MV| | |scarti|Basic| |
|Comune di Domicilio|AVX|1903|ATTIVA|Lunghezza diversa da quella attesa|se valorizzato e la lunghezza è diversa da 6 caratteri| | |Scarti|Basic| |
|Comune di Domicilio|AVX|2035|ATTIVA|Codice non presente nel dominio di riferimento|Il codice non è secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune.|Comuni Istat| |Scarti|Anagrafica| |
|Comune di Domicilio|AVX|2040|ATTIVA|Comune di Domicilio incoerente con Comune o ASL  di Docmicilio (solo per invii con modalità RE)|Il codice è valorizzato e la ASL o la regione di domicilio non sono valorizzati oppure sono valorizzati con valori che non afferiscono al comune di domicilio.|tabella di raccordo regioni-province-comuni-asl|comune domicilio e asl di domicilio|Scarti|Anagrafica| |
|ASL di domicilio sanitario|AVX|2012|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri| | |Scarti|Basic| |
|ASL di domicilio sanitario|AVX|2045|DISATTIVATA|Codice non presente nel dominio di riferimento|Il codice non è presente nell’anagrafe di riferimento (D.M. 05/12/2006 e successive modifiche – Anagrafica MRA fase 1)<br>**Approfondimento**:<br>` `la coppia da utlizzare per effettuare il controllo sulla tabella anagrafica "Anagrafiche: ASL" è:<br>(codice regione domicilio, codice asl domicilio).|Anagrafiche: ASL| |Scarti|Anagrafica| |
|ASL di domicilio sanitario|AVX|2050|ATTIVA|ASL di Domicilio incoerente con Comune o regione  di Docmicilio (solo per invii con modalità RE)|Il codice è valorizzato e il comune o la regione di domicilio non sono valorizzati oppure sono valorizzati con valori che non afferiscono alla ASL di domicilio.|tabella di raccordo regioni-province-comuni-asl|comune e regione di domicilio|Scarti|Anagrafica| |
|Regione di domicilio sanitario|AVX|2032|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri| | |Scarti|Basic| |
|Regione di domicilio sanitario|AVX|2055|ATTIVA|Codice non presente nel dominio di riferimento|Il codice non è presente nell’anagrafe di riferimento delle regioni.|Anagrafiche: REGIONE| |Scarti|Anagrafica| |
|Regione di domicilio sanitario|AVX|2060|ATTIVA|Regione di Domicilio incoerente con Comune o ASL  di Docmicilio|Il codice è valorizzato e il comune o la ASL di domicilio non sono valorizzati oppure sono valorizzati con valori che non afferiscono alla regione di domicilio.|tabella di raccordo regioni-province-comuni-asl|Comune e ASL  di Docmicilio|Scarti|Anagrafica| |
|Regione di domicilio sanitario|AVX|2065|ATTIVA|Dati di Domicilio incoerenti con i dati di Residenza (solo per invii con modalità RE)|Il codice è valorizzato insieme al comune e alla ASL di domicilio che sono uguali a regione, ASL e comune di residenza.| |residenza|Scarti|Basic| |
|Cittadinanza|AVX|2041|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |Scarti|Basic| |
|Cittadinanza|AVX|2043|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |Scarti|Basic| |
|Cittadinanza|AVX|2070|ATTIVA|Codice non presente nel dominio di riferimento|Il codice non è presente nella codifica Alpha2 (a due lettere) prevista dalla normativa ISO 3166-2.|Codifica Alpha2| |Scarti|Anagrafica| |
|Cittadinanza|AVX|2075|ATTIVA|Cittadinanza incongruente con la Tipologia del codice identificativo assistito |Se la cittadinanza è uguale a ‘IT’ e la “tipologia di CI” è uguale a 1 o 2 o 3| |Tipologia del codice identificativo assistito |Scarti|Basic| |
|Data Decesso|AVX|3000|ATTIVA|Datatype errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |Scarti|Basic| |
|Data Decesso|AVX|2080|ATTIVA|La data decesso è successiva alla trasmissione|In caso di flusso Residenti (RE) La data di decesso è successiva alla data di trasmissione.| |data trasmissione (data elaborazione del flusso) cioè la data in cui si elabora il flusso|Scarti|Basic| |
|Data Decesso|AVX|2085|ATTIVA|La data decesso è antecedente alla data di nascita |In caso di flusso Residenti (RE) La data di decesso è antecedente alla data di nascita.| |data di nascita|Scarti|Basic| |
|Data Decesso|AVX|2090|ATTIVA|La differenza tra data di nascita e data decesso non congura|In caso di flusso Residenti (RE) La differenza tra data di nascita e data di decesso è superiore a 130 anni| |data di nascita|Scarti|Basic| |
|Data Decesso|AVX|2095|ATTIVA|La data di decesso deve essere compresa nel periodo di invio|In caso di flusso Residenti (RE) La data del decesso deve essere all’interno del trimestre di invio| | |Scarti|Basic| |
|Comune di Residenza|AVX|1945|ATTIVA|Codice non presente nel dominio di riferimento|Il codice è valorizzaro e diverso da 999999 (residenti in Italia)  e non è secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune.|Comuni Istat| |scarti|Anagrafica| |



## ***3.4 Accesso alle anagrafiche***

I controlli applicativi saranno implementati a partire dall’acquisizione dei seguenti dati anagrafici disponibili in ambito MdS e recuperati con servizi ad hoc (Service Layer mediante PDI):

- REGIONE
- ASL
- tabella di raccordo regioni-province-comuni-asl
- Codifica Alpha2
- HPS11
- STS11
- MRA
- RIA11
- Condizioni sanitarie a rischio
- Categorie a rischio
- AIFA
- ELENCO MdS
- Tipologie Formulazione
- Antigeni
- Principi Vaccinali
- Comuni Istat

Il dato anagrafico sarà presente sottoforma di tabella composta da tre colonne:

- Valore (in cui è riportato il dato, nel caso di più valori, sarà usato il carattere # come separatore)


- Data inizio validità (rappresenta la data di inizio validità del campo Valore)
 - Formato: AAAA-MM-DD
 - Notazione inizio validità permanente: **1900-01-01**


- Data Fine Validità (rappresenta la data di fine validità del campo Valore)
  - Formato: AAAA-MM-DD
  - Notazione fine validità permanente: **9999-12-31**

Affinchè le Business Rule che usano il dato anagrafico per effettuare controlli siano correttamente funzionanti, occorre controllare che la data di competenza del record su cui si effettua il controllo (la quale varia in base al flusso), sia compresa tra le data di validità.  Tutte le tabelle anagrafiche hanno dei dati con validità permanente ad eccezione delle seguenti, per le quali sono previste date di validità specifiche:

- Tabella di raccordo regioni-province-comuni-asl
- Comuni Istat
- Codifica Alpha2
- Anagrafiche: ASL
- Anagrafiche: REGIONE

Di seguito viene mostrato un caso limite di anagrafica in cui sono presenti delle sovrapposizioni temporali e contraddizioni di validità permanente/specifico range


|ID|VALUE|VALID\_FROM|VALID\_TO|
| - | - | - | - |
|1|VALORE 1|1900-01-01|9999-12-31|
|2|VALORE 1|2015-01-01|2015-12-31|
|3|VALORE 1|2018-01-01|2023-12-31|
|4|VALORE 1|2022-01-01|2024-12-31|


Diremo che  il dato presente sul tracciato di input è valido se e solo se:

∃ VALUE\_R = VALUE\_A “tale che” VALID\_FROM<= **DATA\_COMPETENZA** <= VALID\_TO

(Esiste almeno un valore compreso tra le date di validità)

Dove:

- VALUE\_R rappresenta i campi del tracciato di input coinvolti nei controlli della specifica BR

- VALUE\_A rappresenta i campi dell’anagrafica coinvolti nei controlli della specifica BR

- VALID\_FROM/VALID\_TO rappresentano le colonne dell’anagrafica

- DATA\_COMPETENZA data da utilizzare per il filtraggio del dato anagrafico specifica per flusso ( vedi paragrafo successivo)


La DATA\_COMPETENZA da utilizzare per filtrare il dato anagrafico per il flusso AVX sulle tabelle con date di validità specifiche, riportate in precedenza, è l’Anno  Riferimento passato in input all’SDK.

## ***3.5 Alimentazione MdS***

### **3.5.1 Invio Flussi**

A valle delle verifiche effettuate dal Validation Engine, qualora il caricamento sia stato effettuato con il parametro Tipo Elaborazione impostato a P, verranno inviati verso MdS tutti i record corretti secondo le regole di validazione impostate.

Verrà richiamata la procedura invioFlussi di GAF WS (tramite PDI) alla quale verranno passati in input i parametri così come riportati nella seguente tabella:



|**NOME PARAMETRO**|**VALORIZZAZIONE**|
| :- | :- |
|ANNO RIFERIMENTO|Parametro ANNO RIFERIMENTO in input a SDK|
|PERIODO RIFERIMENTO|Parametro PERIODO RIFERIMENTO in input a SDK |
|CATEGORIA FLUSSI|AVN|
|NOME FLUSSO|AVX (Anagrafica)|
|NOME FILE|Parametro popolato dall’SDK in fase di invio flusso con il nome file generato dal Validation Engine in fase di produzione file.|


### **3.5.2 Flussi di Output per alimentazione MdS**


I flussi generati dall’SDK saranno presenti sotto la cartella /sdk/xml\_output e dovranno essere salvati e inviati verso MdS rispettando la seguente nomenclatura:


**SDK\_AVN\_AVX\_\_{Periodo di riferimento}\_{ID\_RUN}.xml**


Dove:

- Periodo di Riferimento: composto dal quarter di riferimento (Es. Q1).
- ID\_RUN rappresenta l’identificativo univoco


### **3.5.3 Gestione Risposta Mds**

A valle della presa in carico del dato da parte di MdS, SDK riceverà una response contenente le seguenti informazioni:

1. **codiceFornitura**: stringa numerica indicante l’identificativo univoco della fornitura inviata al GAF
1. **errorCode**: stringa alfanumerica di 256 caratteri rappresentante il codice identificativo dell’errore eventualmente riscontrato
1. **errorText**: stringa alfanumerica di 256 caratteri rappresentante il descrittivo dell’errore eventualmente riscontrato
1. Insieme delle seguenti triple, una per ogni file inviato:

 a. **idUpload**: stringa numerica indicante l’identificativo univoco del singolo file ricevuto ed accettato dal MdS, e corrispondente al file inviato con la denominazione riportata nell’elemento “nomeFile” che segue

 b. **esito**: stringa alfanumerica di 4 caratteri rappresentante l’esito dell’operazione (Vedi tabella sotto)

 c. **nomeFile**: stringa alfanumerica di 256 caratteri rappresentante il nome dei file inviati.

Di seguito la tabella di riepilogo dei codici degli esiti possibili dell’invio del file:


|**ESITO**|**DESCRIZIONE**|
| :- | :- |
|AE00|Errore di autenticazione al servizio|
|IF00|Operazione completata con successo|
|IF01|Incongruenza tra CF utente firmatario e cf utente inviante|
|IF02|Firma digitale non valida|
|IF03|Firma digitale scaduta|
|IF04|Estensione non ammessa|
|IF05|Utente non abilitato all’invio per la Categoria Flusso indicata|
|IF06|Utente non abilitato all’invio per il Flusso indicata|
|IF07|Periodo non congurente con la Categoria Flusso indicata|
|IF08|Il file inviato è vuoto|
|IF09|Errore interno al servizio nella ricezione del file|
|IF10|Il numero di allegati segnalati nel body non corrisponde al numero di allegati riscontrati nella request|
|IF11|Il nome dell’allegato riportato nel body non è presente tra gli allegati della request (content-id)|
|IF12|Presenza di nomi file duplicati|
|IF13|Errore interno al servizio nella ricezione del file|
|IF14|Errore interno al servizio nella ricezione del file|
|IF15|Errore interno al servizio nella ricezione del file|
|IF99|Errore generico dell’operation|


Copia dei file inviati verso MdS il cui esito è positivo (ovvero risposta della procedura Invio Flussi con IF00) saranno trasferiti e memorizzati in una cartella ad hoc di SDK (es. /sdk/sent) rispettando la seguente naming:


**SDK\_AVN\_AVX\_\_{Periodo di riferimento}\_{ID\_RUN}.xml**


Dove :

- Periodo di Riferimento: composto dal quarter di riferimento (Es. Q1).
- ID\_RUN rappresenta l’identificativo univoco

## ***3.6 Scarti di processamento***

In una cartella dedicata (es. /sdk/esiti) verrà creato un file json contenente il dettaglio degli scarti riscontrati ad ogni esecuzione del processo SDK.

Il naming del file sarà:  ESITO\_{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|NUMERO RECORD|Numero del record del flusso input|
|RECORD PROCESSATO|Campi esterni rispetto al tracciato, che sono necessari per la validazione dello stesso.</p><p>Record su cui si è verificato uno scarto, riportato in maniera strutturata (nome\_campo-valore).|
|LISTA ESITI|<p>Lista di oggetti contenente l’esito di validazione per ciascun campo:</p><p>- Campo: nome campo su cui si è verificato uno scarto</p><p>- Valore Scarto: valore del campo su cui si è verificato uno scarto</p><p>- Valore Esito: esito di validazione del particolare campo</p><p>- Errori Validazione: contiene i campi Codice (della Business Rule) e Descrizione (della Business Rule)</p>|

## ***3.7 Informazioni dell’esecuzione***

In una cartella dedicata (es. /sdk/run) verrà creato un file contenente il dettaglio degli esiti riscontrati ad ogni esecuzione del processo SDK. Verrà prodotto un file di log per ogni giorno di elaborazione.

Il naming del file sarà:  

{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|ID RUN (chiave)|Identificativo univoco di ogni esecuzione del SDK|
|ID\_CLIENT|Identificativo Univoco della trasazione sorgente che richiede processamento lato SDK|
|ID UPLOAD (chiave)|Identificativo di caricamento fornito da MdS|
|TIPO ELABORAZIONE|F (full)/R (per singolo record) - Impostato di default a F|
|MODALITA’ OPERATIVA|P (=produzione) /T (=test)|
|DATA INIZIO ESECUZIONE|Timestamp dell’ inizio del processamento|
|DATA FINE ESECUZIONE|Timestamp di completamento del processamento|
|STATO ESECUZIONE |<p>Esito dell’esecuzione dell’ SDK. </p><p>Possibili valori: </p><p>- IN ELABORAZIONE: Sdk in esecuzione;</p><p>- ELABORATA: Esecuzione completata con successo;</p><p>- KO: Esecuzione fallita: </p><p>- KO SPECIFICO: Esecuzione fallita per una fase/componente più rilevante della soluzione (es. ko\_gestione\_file, ko\_gestione\_validazione, ko\_invio\_ministero, etc.); </p><p>- KO GENERICO: un errore generico non controllato.</p>|
|FILE ASSOCIATI RUN|nome del file di input elaborato dall’SDK|
|NOME FLUSSO|valore fisso che identifica lo specifico SDK in termini di categoria e nome flusso|
|NUMERO RECORD |Numero di record del flusso input|
|NUMERO RECORD ACCETTATI|Numero validi|
|NUMERO RECORD SCARTATI|Numero scarti|
|VERSION|Versione del SDK (Access Layer e Validation Engine)|
|TIMESTAMP CREAZIONE|Timestamp creazione della info run|
|API (\*DPM)|Rappresenta L’API utilizzata per il flusso DPM (non valorizzata per gli altri flussi)|
|IDENTIFICATIVO SOGGETTO ALIMENTANTE (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|NUMERO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ESITO MDS (\*DPM)|Esito della response dell’API 2 (non valorizzata per gli altri flussi) |
|DATA RICEVUTA MDS (\*DPM)|Data della response dell’API 3 (non valorizzata per gli altri flussi)|
|CODICE REGIONE|Codice Regione del Mittente|
|ANNO RIFERIMENTO|Anno cui si riferiscono i dati del flusso|
|PERIODO DI RIFERIMENTO|Rappresenta il periodo di riferimento passato in input all’SDK|
|DESCRIZIONE STATO ESECUZIONE |Specifica il messaggio breve dell’errore, maggiori informazioni saranno presenti all’interno del log applicativo|
|NOME FILE OUTPUT MDS|Nome dei file di output inviati verso MdS|
|ESITO ACQUISIZIONE FLUSSO|Codice dell’esito del processo di acquisizione del flusso su MdS. Tale campo riflette la proprietà invioFlussiReturn/listaEsitiUpload/item/esito della response della procedura **invioFlussi**. (Es IF00)|
|CODICE ERRORE INVIO FLUSSI|Codice d’errore della procedura di invio. Tale campo riflette la proprietà InvioFlussiReturn/errorCode della response della procedura **invioFlussi**|
|TESTO ERRORE INVIO FLUSSI|Descrizione codice d’errore della procedura.Tale campo riflette la proprietà InvioFlussiReturn/ errorText della response della procedura **invioFlussi**|


Inoltre, a supporto dell’entità che rappresenta lo stato dell’esecuzione, sotto la cartella /sdk/log, saranno presenti anche i file di log applicativi (aggregati giornalmente) non strutturati, nei quali saranno presenti informazioni aggiuntive, ad esempio lo StackTrace (in caso di errori).

Il naming del file, se non modificata la politica di rolling (impostazioni), è il seguente:

**SDK \_AVN-AVX.log**

## mantainer:
 Accenture SpA until January 2026