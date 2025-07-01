module.exports = {
    // Configuration de l'application
    app: {
        name: 'BloodApp',
        version: '1.0.0',
        environment: process.env.NODE_ENV || 'development'
    },
    
    // Configuration des dons
    donation: {
        minDaysBetweenDonations: 56, // Minimum de jours entre deux dons
        minAge: 18,
        maxAge: 65,
        minWeight: 50 // kg
    },
    
    // Configuration des demandes de sang
    bloodRequest: {
        urgencyLevels: ['low', 'medium', 'high'],
        statuses: ['active', 'fulfilled', 'cancelled'],
        bloodTypes: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']
    },
    
    // Configuration de sécurité
    security: {
        jwtExpiration: '7d',
        passwordMinLength: 6,
        adminPasswordMinLength: 8,
        maxLoginAttempts: 5,
        lockoutTime: 15 * 60 * 1000 // 15 minutes
    },
    
    // Configuration des notifications
    notifications: {
        emailEnabled: false, // À activer quand le service email est configuré
        smsEnabled: false    // À activer quand le service SMS est configuré
    },
    
    // Configuration des rapports
    reports: {
        maxExportRecords: 10000,
        dateFormat: 'YYYY-MM-DD'
    }
};
