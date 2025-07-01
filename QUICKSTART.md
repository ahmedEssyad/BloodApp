# BloodApp - Guide de démarrage rapide

## Installation rapide

### 1. Backend (5 minutes)

```bash
# 1. Assurez-vous que MongoDB est en cours d'exécution

# 2. Naviguez vers le dossier backend
cd C:\Users\lapto\Downloads\BloodApp\backend

# 3. Installez les dépendances
npm install

# 4. Créez l'administrateur
npm run create-admin

# 5. Démarrez le serveur
npm run dev
```

### 2. Application Android (10 minutes)

1. Ouvrez Android Studio
2. Ouvrez le projet : `C:\Users\lapto\Downloads\BloodApp\BloodApp`
3. Attendez la synchronisation Gradle
4. Lancez l'application (bouton Run)

## Identifiants de test

**Admin**
- Email : admin@bloodapp.com
- Mot de passe : Admin123!@#

**Utilisateur**
- Créez un compte via l'écran d'inscription

## Problèmes fréquents

1. **Erreur réseau** : Vérifiez que le serveur est démarré
2. **MongoDB erreur** : Vérifiez que MongoDB est en cours d'exécution
3. **Build failed** : Clean Project puis Rebuild Project

## Commandes utiles

```bash
# Backend
npm run dev          # Démarre le serveur
npm run create-admin # Crée un admin

# Android (dans Android Studio)
Run > Run 'app'      # Lance l'application
```

Pour plus de détails, consultez le README.md complet.
