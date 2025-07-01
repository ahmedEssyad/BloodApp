const express = require('express');
const router = express.Router();
const {
    getDashboardStats,
    getAllUsers,
    updateUserRole,
    deleteUser,
    getAllBloodRequests,
    updateRequestStatus,
    getAllDonors,
    generateReport,
    createAdmin,
    changeAdminPassword
} = require('../controllers/adminController');
const { auth, isAdmin } = require('../middleware/auth');
const adminLogger = require('../middleware/adminLogger');
const { adminLimiter } = require('../middleware/rateLimiter');

// Toutes les routes admin nécessitent l'authentification, le rôle admin, le logging et le rate limiting
router.use(auth, isAdmin, adminLogger, adminLimiter);

// Dashboard
router.get('/dashboard', getDashboardStats);

// Gestion des utilisateurs
router.get('/users', getAllUsers);
router.put('/users/:userId/role', updateUserRole);
router.delete('/users/:userId', deleteUser);

// Gestion des demandes de sang
router.get('/blood-requests', getAllBloodRequests);
router.put('/blood-requests/:requestId/status', updateRequestStatus);

// Gestion des donneurs
router.get('/donors', getAllDonors);

// Rapports
router.get('/reports', generateReport);

// Créer un nouvel admin (seul un admin existant peut le faire)
router.post('/create-admin', createAdmin);

// Changer le mot de passe admin
router.put('/change-password', changeAdminPassword);

module.exports = router;
