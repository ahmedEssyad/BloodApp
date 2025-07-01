# BloodApp - Guide d'installation et d'exécution

BloodApp est une application mobile de gestion de dons de sang qui permet aux utilisateurs de faire des dons, de trouver des donneurs et aux administrateurs de gérer l'ensemble du système.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

1. **Node.js** (version 14 ou supérieure)
   - Téléchargez depuis : https://nodejs.org/

2. **MongoDB** (version 4.4 ou supérieure)
   - Téléchargez depuis : https://www.mongodb.com/try/download/community

3. **Android Studio** (dernière version)
   - Téléchargez depuis : https://developer.android.com/studio

4. **Git** (optionnel)
   - Téléchargez depuis : https://git-scm.com/downloads

## Structure du projet

```
BloodApp/
├── backend/          # Serveur Node.js/Express
└── BloodApp/         # Application Android
```

## Installation du Backend

### Étape 1 : Configuration de MongoDB

1. Installez MongoDB si ce n'est pas déjà fait
2. Démarrez le service MongoDB :
   ```bash
   # Windows
   net start MongoDB
   
   # Linux/Mac
   sudo service mongod start
   ```

### Étape 2 : Installation des dépendances

1. Ouvrez un terminal/invite de commande
2. Naviguez vers le dossier backend :
   ```bash
   cd C:\Users\lapto\Downloads\BloodApp\backend
   ```
3. Installez les dépendances :
   ```bash
   npm install
   ```

### Étape 3 : Configuration de l'environnement

1. Le fichier `.env` est déjà configuré avec :
   ```
   PORT=3000
   MONGODB_URI=mongodb://localhost:27017/bloodapp
   JWT_SECRET=bloodapp_secret_key_2024_secure
   NODE_ENV=development
   ```

### Étape 4 : Création du premier administrateur

1. Exécutez la commande suivante :
   ```bash
   npm run create-admin
   ```
2. Notez les identifiants créés :
   - Email : admin@bloodapp.com
   - Mot de passe : Admin123!@#

### Étape 5 : Démarrage du serveur

1. Démarrez le serveur en mode développement :
   ```bash
   npm run dev
   ```
2. Le serveur démarrera sur `http://localhost:3000`

## Installation de l'Application Android

### Étape 1 : Ouverture du projet

1. Ouvrez Android Studio
2. Sélectionnez "Open an existing project"
3. Naviguez vers : `C:\Users\lapto\Downloads\BloodApp\BloodApp`
4. Cliquez sur "OK"

### Étape 2 : Synchronisation du projet

1. Android Studio détectera automatiquement le fichier `build.gradle`
2. Cliquez sur "Sync Now" si demandé
3. Attendez que la synchronisation se termine

### Étape 3 : Configuration de l'émulateur

1. Cliquez sur "Device Manager" dans Android Studio
2. Créez un nouvel appareil virtuel (AVD) :
   - Sélectionnez un appareil (ex: Pixel 4)
   - Choisissez une image système (ex: Android 11.0)
   - Cliquez sur "Finish"

### Étape 4 : Configuration de l'adresse du serveur

Si vous utilisez :
- **Un émulateur** : L'adresse par défaut `http://10.0.2.2:3000/` fonctionnera
- **Un appareil physique** : Modifiez `RetrofitClient.java` :
  ```java
  private static final String BASE_URL = "http://VOTRE_IP_LOCALE:3000/";
  ```
  Remplacez `VOTRE_IP_LOCALE` par l'adresse IP de votre machine (ex: 192.168.1.100)

### Étape 5 : Exécution de l'application

1. Sélectionnez votre appareil cible (émulateur ou appareil physique)
2. Cliquez sur le bouton "Run" (triangle vert)
3. Attendez que l'application se compile et s'installe

## Utilisation de l'application

### Connexion en tant qu'administrateur

1. Utilisez les identifiants admin :
   - Email : admin@bloodapp.com
   - Mot de passe : Admin123!@#

2. Fonctionnalités admin disponibles :
   - Tableau de bord avec statistiques
   - Gestion des utilisateurs
   - Gestion des demandes de sang
   - Création de nouveaux administrateurs

### Connexion en tant qu'utilisateur

1. Créez un nouveau compte via l'écran d'inscription
2. Remplissez tous les champs requis
3. Fonctionnalités utilisateur disponibles :
   - Profil personnel
   - Faire un don de sang
   - Voir l'historique des dons
   - Trouver des donneurs
   - Répondre aux demandes de sang

## Dépannage

### Problèmes courants

1. **"Network error" ou "Failed to connect"**
   - Vérifiez que le serveur backend est en cours d'exécution
   - Vérifiez l'adresse IP dans RetrofitClient.java
   - Assurez-vous que votre pare-feu autorise les connexions

2. **"MongoDB connection failed"**
   - Vérifiez que MongoDB est installé et en cours d'exécution
   - Vérifiez que le port 27017 est libre

3. **Build failed dans Android Studio**
   - Essayez : Build > Clean Project
   - Puis : Build > Rebuild Project
   - Vérifiez que vous avez le SDK Android approprié

4. **Application crash au démarrage**
   - Vérifiez les logs dans Logcat
   - Assurez-vous que les permissions INTERNET sont accordées

## Scripts disponibles

### Backend
- `npm start` : Démarre le serveur en production
- `npm run dev` : Démarre le serveur en développement
- `npm run seed` : Insère des données de test
- `npm run create-admin` : Crée le premier administrateur

### Android
- Dans Android Studio :
  - Run > Run 'app' : Exécute l'application
  - Build > Generate Signed Bundle/APK : Génère un APK

## Support

Pour toute question ou problème :
1. Vérifiez d'abord cette documentation
2. Consultez les logs du serveur et de l'application
3. Créez un issue sur le repository

## Technologies utilisées

### Backend
- Node.js & Express
- MongoDB & Mongoose
- JWT pour l'authentification

### Android
- Java
- Retrofit pour les appels API
- Material Design Components

## Licence

Ce projet est sous licence MIT.
