
# NeverLate

## Description

NeverLate est une application web qui permet aux utilisateurs de gérer des horaires sur différents fuseaux horaires.
Elle aide les utilisateurs à visualiser, ajouter, modifier, et supprimer des fuseaux horaires et à comparer des horaires
entre différentes zones géographiques.

Le projet est divisé en deux parties : un backend développé en Java avec Spring Boot, et un frontend développé en ReactJS.

## Fonctionnalités

- Ajouter un fuseau horaire avec un libellé personnalisé.
- Modifier un fuseau horaire.
- Supprimer un fuseau horaire.
- Visualiser une liste de fuseaux horaires configurés.
- Comparer les heures d'un fuseau horaire donné avec d'autres fuseaux.
- Saisie de date et heure pour une correspondance précise entre les fuseaux horaires.

## Prérequis

- Node.js (pour le frontend)
- Java JDK 11+ (pour le backend)
- Docker (facultatif, si vous souhaitez utiliser Docker)
- Maven (pour le backend)

## Installation et Exécution

### Backend (Spring Boot)

1. Naviguez dans le répertoire `backend` :
   ```bash
   cd backend
   ```

2. Compilez et exécutez l'application avec Maven :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Le backend sera disponible sur `http://localhost:8080`.

### Frontend (ReactJS)

1. Naviguez dans le répertoire `frontend` :
   ```bash
   cd frontend
   ```

2. Installez les dépendances Node.js :
   ```bash
   npm install
   ```

3. Exécutez l'application React :
   ```bash
   npm start
   ```

   Le frontend sera disponible sur `http://localhost:5173`.

## Utilisation de Docker

1. Assurez-vous que Docker est installé et en cours d'exécution sur votre machine.

2. Dans la racine du projet (où se trouve le fichier `docker-compose.yml`), exécutez la commande suivante pour lancer les services backend et frontend :
   ```bash
   docker-compose up --build
   ```

   Cette commande démarrera à la fois le backend et le frontend dans des conteneurs Docker.

3. Accédez à l'application :
   - Backend : `http://localhost:8080`
   - Frontend : `http://localhost:5173`

## Structure du Projet

- `backend/` : Contient le code source du backend (Spring Boot).
- `frontend/` : Contient le code source du frontend (ReactJS).
- `docker-compose.yml` : Fichier Docker Compose pour lancer l'application dans des conteneurs.

## Base de données

L'application utilise une base de données H2 en mémoire pour stocker les données des fuseaux horaires. Vous pouvez accéder à la console H2 via `http://localhost:8080/h2-console` lorsque l'application est en cours d'exécution.

## Tests

Des tests unitaires et d'intégration peuvent être exécutés dans le backend avec Maven :
```bash
mvn test
```


