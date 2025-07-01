# Guide d'administration - BloodApp

## Création du premier administrateur

Pour créer le premier administrateur de l'application, exécutez :

```bash
npm run create-admin
```

Cela créera un compte administrateur avec les identifiants suivants :
- Email : admin@bloodapp.com
- Mot de passe : Admin123!@#

**IMPORTANT** : Changez ce mot de passe immédiatement après la première connexion !

## Fonctionnalités d'administration

### Tableau de bord
- Statistiques générales (nombre d'utilisateurs, donneurs, demandes de sang)
- Statistiques par groupe sanguin
- Dernières demandes de sang

### Gestion des utilisateurs
- Voir tous les utilisateurs
- Modifier le rôle d'un utilisateur (user/admin)
- Supprimer un utilisateur
- Créer un nouvel administrateur

### Gestion des demandes de sang
- Voir toutes les demandes
- Modifier le statut d'une demande (active/fulfilled/cancelled)
- Supprimer une demande

### Gestion des donneurs
- Voir tous les donneurs
- Accéder aux profils des donneurs
- Voir l'historique des dons

### Rapports et statistiques
- Générer des rapports sur les dons
- Générer des rapports sur les demandes
- Exporter les données (CSV, Excel)

## Endpoints API Admin

Tous les endpoints admin nécessitent :
1. Authentification (token JWT)
2. Rôle admin

### Endpoints principaux

- GET `/api/admin/dashboard` - Statistiques du tableau de bord
- GET `/api/admin/users` - Liste des utilisateurs
- PUT `/api/admin/users/:userId/role` - Modifier le rôle d'un utilisateur
- DELETE `/api/admin/users/:userId` - Supprimer un utilisateur
- GET `/api/admin/blood-requests` - Liste des demandes de sang
- PUT `/api/admin/blood-requests/:requestId/status` - Modifier le statut d'une demande
- GET `/api/admin/donors` - Liste des donneurs
- GET `/api/admin/reports` - Générer des rapports
- POST `/api/admin/create-admin` - Créer un nouvel administrateur

## Sécurité

1. Tous les endpoints admin sont protégés par un middleware qui vérifie :
   - La présence d'un token JWT valide
   - Le rôle admin de l'utilisateur

2. Les mots de passe des administrateurs doivent :
   - Contenir au moins 8 caractères
   - Inclure des majuscules, minuscules, chiffres et caractères spéciaux
   - Être changés régulièrement

3. Les actions administratives sont enregistrées pour audit

## Bonnes pratiques

1. Ne jamais partager les identifiants administrateur
2. Utiliser des mots de passe forts et uniques
3. Changer régulièrement les mots de passe
4. Limiter le nombre d'administrateurs
5. Surveiller les logs d'activité
6. Sauvegarder régulièrement la base de données

## Support

En cas de problème avec l'administration :
1. Vérifiez les logs du serveur
2. Assurez-vous que MongoDB est en cours d'exécution
3. Vérifiez la validité du token JWT
4. Contactez l'équipe de développement
