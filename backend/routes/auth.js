const express = require('express');
const router = express.Router();
const { register, login, getProfile } = require('../controllers/authController');
const { auth } = require('../middleware/auth');
const { loginLimiter } = require('../middleware/rateLimiter');

// Routes publiques
router.post('/register', register);
router.post('/login', loginLimiter, login);

// Routes protégées
router.get('/profile', auth, getProfile);

module.exports = router;
