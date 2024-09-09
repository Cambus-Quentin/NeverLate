
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

## Utilisateurs de test

L'application est livrée avec plusieurs utilisateurs préconfigurés et des fuseaux horaires associés. Ces utilisateurs sont automatiquement ajoutés à la base de données lors du lancement de l'application grâce au **seeder**. Vous pouvez utiliser ces utilisateurs pour tester les différentes fonctionnalités de l'application.

### Liste des utilisateurs de test :

| Nom d'utilisateur | Email                    | Rôle(s)            | Mot de passe | Fuseaux horaires associés                            |
|-------------------|--------------------------|--------------------|--------------|-----------------------------------------------------|
| **admin**         | admin@example.com         | ADMIN              | admin        | - UTC (London, +00:00)                              |
| **user**          | user@example.com          | USER               | user         | - Eastern Time (New York, -05:00) <br> - Central European Time (Paris, +01:00) |
| **john_doe**      | john.doe@example.com      | USER               | password123  | - Pacific Time (Los Angeles, -08:00) <br> - Mountain Time (Denver, -07:00) <br> - Eastern Time (New York, -05:00) |
| **jane_doe**      | jane.doe@example.com      | ADMIN, USER        | password456  | - Greenwich Mean Time (London, +00:00) <br> - Central Standard Time (Chicago, -06:00) <br> - China Standard Time (Beijing, +08:00) |
| **user5**         | user5@example.com         | USER               | password5    | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |
| **user6**         | user6@example.com         | USER               | password6    | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |
| **user7**         | user7@example.com         | USER               | password7    | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |
| **user8**         | user8@example.com         | USER               | password8    | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |
| **user9**         | user9@example.com         | USER               | password9    | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |
| **user10**        | user10@example.com        | USER               | password10   | - UTC (London, +00:00) <br> - Central European Time (Berlin, +01:00) <br> - Eastern Time (New York, -05:00) |

### Utilisation des utilisateurs de test

Vous pouvez utiliser les utilisateurs préconfigurés pour tester l'application :

- **Rôles** : Chaque utilisateur a des rôles spécifiques (ADMIN, USER) qui déterminent l'accès à certaines fonctionnalités de l'application.
- **Mot de passe** : Chaque utilisateur a un mot de passe correspondant au modèle `password{X}`, où `X` est le numéro de l'utilisateur. Par exemple, `user5` a le mot de passe `password5`.
- **Fuseaux horaires** : Chaque utilisateur est associé à différents fuseaux horaires, ce qui permet de tester les fonctionnalités de gestion des fuseaux horaires dans l'application.

