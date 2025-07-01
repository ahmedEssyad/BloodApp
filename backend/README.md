# BloodApp Backend

Backend API pour l'application BloodApp construite avec Node.js, Express et MongoDB.

## Prérequis

- Node.js (v14 ou plus)
- MongoDB
- npm ou yarn

## Installation

1. Cloner le repository
2. Installer les dépendances :
```bash
npm install
```

3. Créer un fichier `.env` à la racine du projet (un exemple est fourni)

4. Lancer MongoDB sur votre machine

5. Démarrer le serveur :
```bash
npm run dev
```

Le serveur démarrera sur `http://localhost:3000`

## Structure des API

### Authentification
- POST `/api/auth/register` - Inscription d'un nouvel utilisateur
- POST `/api/auth/login` - Connexion
- GET `/api/auth/profile` - Profil utilisateur (authentifié)

### Donneurs
- GET `/api/donors/search` - Rechercher des donneurs
- POST `/api/donors` - Créer un profil de donneur (authentifié)
- GET `/api/donors/profile` - Obtenir son profil de donneur (authentifié)
- PUT `/api/donors/profile` - Mettre à jour son profil (authentifié)

### Demandes de sang
- GET `/api/blood-requests` - Liste des demandes de sang
- POST `/api/blood-requests` - Créer une demande (authentifié)
- GET `/api/blood-requests/:id` - Détails d'une demande
- PUT `/api/blood-requests/:id` - Mettre à jour une demande (authentifié)
- DELETE `/api/blood-requests/:id` - Supprimer une demande (authentifié)

### Historique des dons
- POST `/api/donors/donations` - Enregistrer un don (authentifié)
- GET `/api/donors/:donorId/donations` - Historique des dons (authentifié)
- GET `/api/donors/:donorId/stats` - Statistiques des dons (authentifié)

## Technologies utilisées

- Express.js - Framework web
- MongoDB - Base de données
- Mongoose - ODM MongoDB
- JWT - Authentification
- Bcrypt - Hachage des mots de passe
- Cors - Cross-Origin Resource Sharing
- Dotenv - Variables d'environnement

## Structure du projet

```
backend/
├── config/         # Configuration de la base de données
├── controllers/    # Logique métier
├── middleware/     # Middleware d'authentification
├── models/         # Modèles Mongoose
├── routes/         # Définition des routes
├── .env           # Variables d'environnement
├── package.json   # Dépendances et scripts
└── server.js      # Point d'entrée de l'application
```

## Sécurité

- Les mots de passe sont hachés avec bcrypt
- L'authentification utilise JWT
- Les routes sensibles sont protégées par un middleware d'authentification
- Validation des données avec Mongoose et validator.js

## Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit les changements (`git commit -am 'Ajout de fonctionnalité'`)
4. Push vers la branche (`git push origin feature/amelioration`)
5. Créer une Pull Request
