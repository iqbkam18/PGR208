[![Build Status](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-guberArmin.svg?token=m6BpjWymm3UWnZ6QxDwC&branch=master)](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-guberArmin)


# PGR203 - Eksamen avansert java - Gruppe 12
 
Prosjektet inneholder en database og en server. Serveren brukes for å kommunisere med databasen og vise fram innholdet. 
* Databasen inneholder: Project, Task, Worker, Cookie - og to koblingstabeller. (Worker_task, Project_workers) 
* Det finnes flere operasjoner som kan utføres mot databasen som feks det å legge til og endre i databasen. 


## Hvordan kjøre dette programmet

1. For å bygge prosjektet bruker man `mvn clean package`.
2. For å starte serveren taster man inn `java -jar target/project-managment-1.0-SNAPSHOT.jar`
3. I browseren skal man taste inn `http://localhost:8080` for å besøke hjemmesiden. 

### Funksjonalitet

1. Når man kommer til hovedsiden kan man legge til data i databasen ved å trykke på en av linkene: 
    * `Add new project member` 
    * `Create new project`
    * `Create new task`
    * `Add worker to project`
    * `Add task to worker`      
2. På hovedsiden kan man også liste opp data fra databasen ved å trykke på en av disse linkene: 
    * `List all tasks and workers on it`
    * `List all workers`
    * `List projects`
    * `List default worker (based on your cookie)`
    * `Change your default worker`   
3. Ved å trykke på linken `Add worker to project` kan vi velge en project og en worker som vi kan legge til i project. 
Ved å trykke på `Add member to project` får man oversikt over koblingstabellen mellom `Worker` og `Project`.
4. Funksjonaliteten på `Add task to worker` ligner veldig mye på `Add worker to project`.
5. Når man går inn på `List all workers` har man mulighet til å velge en worker som man ønsker å se taskene til. 
6. Ved å gå til `List tasks` har man mulighet til å endre task og lagre endringene. (Det samme gjelder `List projects`).
7. Basert på din cookie som lagres i databasen kan du velge hvilken worker som skal være din default worker, 
slik at når du trykker på `List default worker` så listes det opp dataen til valgt worker og taskene han har. 

## Designbeskrivelse

 ### Packages: 
For å ha det mest mulig ryddig kode har vi fordelt det slik at de klassene som hører sammen ligger i samme package.
Prosjektet vårt er fordelt i to hoveddeler, server og database. 
Databasen inneholder controllers, dao og tabels. 
  * `Tabels package`: Støtteklassene som brukes av dao'er brukes for å hente eller endre data fra databasen.
  * `Dao package`: Klasser som har ansvar for å hente eller endre data i databasen. 
    Hver av tabellene i databasen har tilsvarende dao'en. Alle dao'er baserer seg på `AbstractDao` som inneholder mye av felles funksjonalitet til alle dao'er.
  * `Controllers package`: Det er klasser som har ansvar for å koble frontend (browser) med backend (databasen). 
    De er fordelt i tre packages hvor hver av de har abstract controller, for felles funksjonalitet. 
    De tre er som følger: 
    * `http` for å legge til og vise fram data fra tabeller. 
    * `update` brukes for å endre data som allerede ligger i tabellen. 
    * `views` brukes for å koble flere tabeller sammen ved å bruke koblingstabeller og presentere den dataen som samlet.
    
### Navngivning: 
Vi har passet på at alle klasser, variabler og packages har navn som er forklarende for funksjonaliteten deres. 
Vi har fulgt navngivning konvensjoner for java, i forhold til hvor vi skal ha store- og små bokstaver, understrek og lignende. 

### Generelt:
Vi har utviklet en prosjektstyrings-applikasjon som gjennom serveren kommuniserer med databasen og viser fram eller endrer oppgaver, deltakere på oppgaver
og prosjekter gjennom nettleseren. Serveren har ansvar for å håndtere request ved å lese de og svare på dem. 
Selve serveren baserer seg på versjonen som ble utviklet ila innlevering 3, med endringer slik at vi kan håndtere cookies og har bedre måte
å håndtere http request ved at vi har http controller som en interface for felles funksjonalitet.  
Databasen skal ved hjelp av serveren kommunisere data videre til brukeren ved å bruke dao'er (data access objects).
Dao kobler seg opp mot controller som viser eller endrer data gjennom browseren.


## Egenevaluering



### Hva vi lærte underveis
Vi har lært å håndtere sockets i java slik at vi kan kommunisere med andre applikasjoner. 
Vi synes det er veldig viktig at vi lærte oss å bruke postgresql i java sammenheng fordi det 
er en veldig mye brukt database system. Denne kan vi få godt nytte av videre i arbeidslivet sammen med alt det andre.
Andre ting vi har lært er å skrive gode tester slik at vi kan beskytte koden vår mot bugs på en best mulig måte. 
Vi har opplevd at parprogrammering i kombinasjon med TDD har vært et veldig nyttig verktøy for å skrive
god og feilfri kode. 
Vi har underveis i prosjektet møtt på en del uforutsette problemer som vi har måtte finne ut av. 
Det å løse problemene som oppstod var veldig lærerikt. Vi hadde stort fokus på å bruke shortcuts og på denne måten
økte vi produktiviteten vår. 

### Hva vi fikk til bra i koden vår
Vi har hatt fokus på å fullføre alle kravene i oppgaven. Både de som måtte implementeres og tillegskrav. 

* Vi har fått til å implementere avansert datamodell (mer enn 3 tabeller). 
    * Cookies tabell
    * Projects tabell
    * Workers tabell
    * Tasks tabell 
    * Project_Workers og Worker_Task som er koblingstabeller

* Vi har fått til at tasks og projects kan redigeres. 

* Cookies har blitt implementert. 
  Som beskrevet i punkt 7 i funksjonalitet.

* Vi har klasse UML diagrammer og database UML diagram som kan finnes ved å klikke på: [***UML diagrams***](doc/UML_DIAGRAMS.md)

* Vi har prøvd å gjenspeile RFC7230 standard i HTTP håndteringen slik at vi har passet på
at vi har bygget headers og at vi leser request på en riktig måte. 
Vi har gjort det slik at serveren vår bruker HTTP controllere for å sende svar til klienten. 

* Vi har gjort det slik at vi kan bruke norske tegn når vi sender forespørsel til serveren, for å lagre i databasen
og når vi får svar fra serveren. 
Vi har gjort det ved å bruke URLDecoder som er en java klasse for html dekoding. 
Fordel med å bruke URLDecoder, i tillegg til å håndtere norske tegn at spesielle 
tegn som %20 kan automatisk oversettes til riktig verdi.(%20 blir da gjort om til mellomrom)

* Vi har gjort det slik at for å kommunisere databasen bruker vi dao'er som bygger seg på
en abstract dao som inneholder veldig mye av funksjonaliteten som er felles for de andre dao'ene. 
Ved behov har vi implementert funksjonaliteter til andre dao'er som er unik for den ene. 

* Vi hadde fokus på å teste prosjektet vårt grundig nok og beskytte den mot feil. 
Dette gjorde vi ved å skrive mange og gode nok tester. Etter å ha lest litt på det fant vi ut at 
good code coverage bør ligge på rundt 70-80% (https://www.bullseye.com/minimum.html), noe vi har oppfylt.
Vi har også brukt flat extracting for å sammenligne flere felter av en klasse med data som ble hentet fra tabellen.

* For å beskytte at brukeren skal prøve å sende samme forespørsel mot databasen flere ganger 
har vi gjort det slik at når brukeren trykker på en av knappene som operer mot databasen(create eller lignende) blir knappen
disabled. 

### Hva vi skulle ønske vi hadde gjort annerledes
* Vi har opplevd at når vi bygger prosjektet med `mvn clean package` og kjører den fra CLI så oppfører serveren seg ikke 
likt som den gjør når man kjører den fra Intellij. 
Etter at vi har tatt kontakt med foreleser og andre medstudenter gjennom slack og andre kanaler, så har 
vi ikke funnet ut av dette problemet. Derfor anbefaler vi at selve programmet startes fra Intellij dersom man opplever 
slike problemer. Vi har testet programmet underveis og aldri opplevd et slikt problem. Men når vi kjørte den siste dagen før
innlevering oppdaget vi at det var noe som ikke stemte. 

* Vi hadde lyst til å implementere en abstrakt klasse på serverens nivå som skulle inneholde 
felles funksjonalitet for de ulike controllerne. Fordi når man bygger et svar er det veldig mye som går igjen 
som vi kunne ha implementert i en abstrakt klasse og gjenbrukt det. 

* Når det gjelder tester har vi prøvd så godt vi kan å ha gode nok tester men 
vi har ikke implementert tester for views. Grunnen til det er at det krevdes mye tid og operasjon 
mot forskjellige dao'er og controllers. Disse har allerede tester knyttet til seg så vi følte at det
var rimelig beskyttet mot feil, selv om det ikke var optimalt. 

* Vi kunne ha commited og pushet oftere til github. Grunnen til at vi ikke har pushet så veldig mye er at 
vi ofte har sittet sammen og parprogrammert, så behovet har ikke vært der. 
Vi kunne vekslet mellom pc'er litt mer men vanlgte ikke å gjøre det altfor ofte, fordi vi valgte
et tastatur oppsett slik at det skulle bli enklere å arbeide. 


## Evaluering fra annen gruppe
* https://github.com/Westerdals/pgr203-2019-eksamen-guberArmin/issues/4
* https://github.com/Westerdals/pgr203-2019-eksamen-guberArmin/issues/3
* https://github.com/Westerdals/pgr203-2019-eksamen-guberArmin/issues/2
* https://github.com/Westerdals/pgr203-2019-eksamen-guberArmin/issues/1
## Evaluering gitt til annen gruppe
* https://github.com/Westerdals/pgr203-2019-eksamen-Leifhaa/issues/4
* https://github.com/Westerdals/pgr203-2019-eksamen-Leifhaa/issues/3
* https://github.com/Westerdals/pgr203-2019-eksamen-Leifhaa/issues/2
* https://github.com/Westerdals/pgr203-2019-eksamen-Leifhaa/issues/1