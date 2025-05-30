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
![erd](https://github.com/user-attachments/assets/5ae3abe4-3bc9-4b9f-a5d8-aec2b141d37d)
![databaseview](https://github.com/user-attachments/assets/46ad1466-b28e-4ba1-ba8e-71f688ac3bd0)

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
  ![jacoco](https://github.com/user-attachments/assets/8a2d6fd3-ffa3-4c40-baf4-a8e3388645ce)
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

![logview](https://github.com/user-attachments/assets/4ebafc5f-dcba-4576-8a1d-1c9b75634a3d)

![adminview](https://github.com/user-attachments/assets/19bf4ea2-2415-41fd-b13c-5701cf1a2125)

![userview](https://github.com/user-attachments/assets/cf154c65-d593-40db-90b0-37a6246205b7)

  ##Autor

  Kamil Wrona
