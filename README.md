# Event_Manager

##Opis
Aplikacja webowa zbudowana w architekturze klient-serwer pozwalająca na adminowi na dodawanie, edytowanie, usuwanie eventów, a użytkownikowi na robieniu rezerwacji:
- Backend: Spring Boot + PostgreSQL + REST API
- Frontend: prosta strona HTML

##Technologie

Projekt wykorzystuje:
- Java 17
- Spring Boot
- Spring Security (RBAC: role `user` i `admin`)
- Hibernate (ORM)
- PostgreSQL
- Flyway / Liquibase (migracje bazy)
- Swagger UI (Springdoc OpenAPI)
- Docker
- Maven
- JUnit + JaCoCo
- JavaScript
- HTML

##Architektura i zasady

- Projekt zgodny z zasadami SOLID i obiektowości.
- Zastosowano wzorzec projektowy: Strategy. 
- Polimorfizm został zaimplementowany np. w hierarchii klas obsługujących operacje użytkowników.
- Struktura zgodna z Maven.

##Role i uprawnienia

Aplikacja obsługuje dwa typy użytkowników:
- `user` – dostęp do podstawowych funkcji.
- `admin` – dostęp do panelu administracyjnego i zarządzania danymi.

Zarządzanie uprawnieniami oparte na Spring Security (RBAC).

##Spring Security

- Uwierzytelnianie za pomocą JWT.
- Role przypisywane na podstawie danych z bazy danych.

Dostępne serwisy:

##Frontend
Prosty interfejs oparty na index.html
Można otworzyć bezpośrednio lub przez Docker:
http://localhost:8082
Własny Dockerfile w katalogu frontend/

Swagger UI: http://localhost:8081/swagger-ui.html

#Migracje bazy danych

Migracje zarządzane za pomocą Flyway
Pliki migracyjne znajdują się w:

src/main/resources/db/migration/

##Struktura repozytorium: 

Event_Manager
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/eventmanager/
│   │   │   └── resources/
│   │   └── test/
│   │       └── java/com/example/eventmanager/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── index.html 
│   └── Dockerfile
├── docker-compose.yml
└── README.md

##Testy

- Testy jednostkowe i integracyjne z wykorzystaniem JUnit 5.
- Pokrycie kodu > 80% (potwierdzone przez JaCoCo).
- Aby uruchomić testy:  
  
  mvn clean verify

##Docker

Projekt uruchamiany jest w kontenerze Docker:
docker-compose build
docker-compose up

Instrukcja uruchomienia: 
git clone: https://github.com/KamilW-git/Event_Manager
cd Event_Manager
docker-compose build
docker-compose up


  ##Autor

  Kamil Wrona
