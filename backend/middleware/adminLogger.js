const fs = require('fs');
const path = require('path');

const adminLogger = (req, res, next) => {
    // Créer le dossier logs s'il n'existe pas
    const logsDir = path.join(__dirname, '../logs');
    if (!fs.existsSync(logsDir)) {
        fs.mkdirSync(logsDir);
    }

    // Créer un log pour chaque action admin
    const logEntry = {
        timestamp: new Date().toISOString(),
        admin: {
            id: req.user._id,
            email: req.user.email,
            fullName: req.user.fullName
        },
        action: {
            method: req.method,
            path: req.originalUrl,
            body: req.body,
            params: req.params,
            query: req.query
        },
        ip: req.ip || req.connection.remoteAddress
    };

    // Écrire dans le fichier de log
    const logFile = path.join(logsDir, `admin-actions-${new Date().toISOString().split('T')[0]}.log`);
    
    fs.appendFile(
        logFile,
        JSON.stringify(logEntry) + '\n',
        (err) => {
            if (err) console.error('Erreur lors de l\'écriture du log admin:', err);
        }
    );

    next();
};

module.exports = adminLogger;
