const rateLimit = require('express-rate-limit');

// Limiteur pour les tentatives de connexion
const loginLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 5, // 5 tentatives maximum
    message: {
        success: false,
        message: 'Trop de tentatives de connexion. Veuillez réessayer dans 15 minutes.'
    },
    standardHeaders: true,
    legacyHeaders: false,
});

// Limiteur pour les routes admin
const adminLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100, // 100 requêtes maximum
    message: {
        success: false,
        message: 'Trop de requêtes. Veuillez réessayer plus tard.'
    },
    standardHeaders: true,
    legacyHeaders: false,
});

// Limiteur pour les API générales
const apiLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 200, // 200 requêtes maximum
    message: {
        success: false,
        message: 'Trop de requêtes. Veuillez réessayer plus tard.'
    },
    standardHeaders: true,
    legacyHeaders: false,
});

module.exports = {
    loginLimiter,
    adminLimiter,
    apiLimiter
};
