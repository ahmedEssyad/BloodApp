# BloodApp - Guide de dépannage

## Problèmes courants et solutions

### 1. Erreurs de connexion réseau

#### Symptômes
- Message "Network error" dans l'application
- "Failed to connect to server"
- "Timeout" lors des requêtes

#### Solutions
1. **Vérifiez que le serveur backend est en cours d'exécution**
   ```bash
   cd backend
   npm run dev
   ```

2. **Vérifiez l'adresse du serveur dans RetrofitClient.java**
   - Pour émulateur : `http://10.0.2.2:3000/`
   - Pour appareil physique : `http://VOTRE_IP:3000/`

3. **Vérifiez votre pare-feu**
   - Autorisez Node.js dans le pare-feu Windows
   - Autorisez le port 3000

4. **Testez la connectivité**
   - Ouvrez `http://localhost:3000/health` dans votre navigateur
   - Devrait afficher : `{"status":"OK","message":"Server is running"}`

### 2. Problèmes MongoDB

#### Symptômes
- "MongoDB connection error"
- "Failed to connect to localhost:27017"

#### Solutions
1. **Vérifiez que MongoDB est installé**
   ```bash
   mongod --version
   ```

2. **Démarrez MongoDB**
   ```bash
   # Windows
   net start MongoDB
   
   # Ou démarrez le service manuellement dans services.msc
   ```

3. **Vérifiez le port 27017**
   ```bash
   netstat -an | find "27017"
   ```

4. **Réinstallez MongoDB si nécessaire**
   - Téléchargez depuis mongodb.com
   - Suivez l'installation complète

### 3. Erreurs de build Android

#### Symptômes
- "Build failed"
- "Could not find..."
- "Gradle sync failed"

#### Solutions
1. **Nettoyez le projet**
   - Build > Clean Project
   - Build > Rebuild Project

2. **Invalidez les caches**
   - File > Invalidate Caches / Restart

3. **Vérifiez le SDK Android**
   - File > Project Structure > SDK Location
   - Assurez-vous d'avoir Android SDK 30+

4. **Mise à jour des dépendances**
   ```gradle
   // Dans build.gradle (app)
   implementation 'com.google.android.material:material:1.9.0'
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   ```

### 4. Erreurs d'authentification

#### Symptômes
- "Invalid token"
- "Unauthorized"
- Déconnexion inattendue

#### Solutions
1. **Vérifiez les identifiants**
   - Email et mot de passe corrects
   - Pas d'espace avant/après

2. **Réinitialisez le token**
   - Déconnectez-vous
   - Connectez-vous à nouveau

3. **Vérifiez la configuration JWT**
   - Le JWT_SECRET dans .env doit correspondre

### 5. Problèmes d'émulateur

#### Symptômes
- Émulateur lent
- Application ne s'installe pas
- Écran noir

#### Solutions
1. **Activez la virtualisation**
   - Dans le BIOS, activez VT-x/AMD-V

2. **Augmentez la RAM de l'émulateur**
   - AVD Manager > Edit > Show Advanced Settings
   - Définissez au moins 2048 MB

3. **Utilisez un appareil physique**
   - Activez le débogage USB
   - Connectez via câble USB

### 6. Erreurs de permission

#### Symptômes
- "Permission denied"
- Fonctionnalités ne fonctionnent pas

#### Solutions
1. **Vérifiez AndroidManifest.xml**
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```

2. **Autorisez les permissions**
   - Paramètres > Apps > BloodApp > Permissions

### 7. Erreurs de base de données

#### Symptômes
- Données non sauvegardées
- "Document not found"

#### Solutions
1. **Réinitialisez la base de données**
   ```bash
   # Dans le dossier backend
   npm run seed
   ```

2. **Vérifiez les collections MongoDB**
   ```bash
   mongo
   use bloodapp
   show collections
   ```

### 8. Logs et débogage

#### Comment obtenir les logs

1. **Logs serveur**
   - Les logs s'affichent dans le terminal où vous avez lancé `npm run dev`

2. **Logs Android**
   - Dans Android Studio : View > Tool Windows > Logcat
   - Filtrez par "BloodApp"

3. **Logs MongoDB**
   - Vérifiez `C:\Program Files\MongoDB\Server\4.x\log\`

### 9. Réinitialisation complète

Si rien ne fonctionne :

1. **Arrêtez tous les services**
   ```bash
   # Arrêtez le serveur Node.js (Ctrl+C)
   # Arrêtez MongoDB
   net stop MongoDB
   ```

2. **Supprimez les données**
   ```bash
   # Dans MongoDB shell
   mongo
   use bloodapp
   db.dropDatabase()
   ```

3. **Réinstallez les dépendances**
   ```bash
   cd backend
   rm -rf node_modules
   npm install
   ```

4. **Recréez l'admin**
   ```bash
   npm run create-admin
   ```

5. **Redémarrez tout**
   ```bash
   npm run dev
   ```

## Besoin d'aide supplémentaire ?

1. Vérifiez les logs détaillés
2. Consultez la documentation complète (README.md)
3. Recherchez le message d'erreur spécifique en ligne
4. Créez un issue sur le repository avec :
   - Description du problème
   - Étapes pour reproduire
   - Logs pertinents
   - Version de l'OS et des outils
