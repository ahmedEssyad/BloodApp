require('dotenv').config();
const mongoose = require('mongoose');
const User = require('./models/User');
const appConfig = require('./config/appConfig');

const initAdminData = async () => {
    try {
        await mongoose.connect(process.env.MONGODB_URI);
        console.log('Connected to MongoDB');

        // Vérifier si des données admin existent déjà
        const admins = await User.find({ role: 'admin' });
        
        if (admins.length === 0) {
            // Créer un administrateur par défaut
            const defaultAdmin = await User.create({
                fullName: 'Administrateur Principal',
                email: 'admin@bloodapp.com',
                password: 'ChangeMe123!@#', // Mot de passe à changer immédiatement
                phone: '221777123456',
                bloodGroup: 'O+',
                age: 35,
                location: 'Dakar',
                role: 'admin'
            });

            console.log('Administrateur par défaut créé :');
            console.log('Email:', defaultAdmin.email);
            console.log('Mot de passe: ChangeMe123!@#');
            console.log('IMPORTANT: Changez ce mot de passe immédiatement !');
        } else {
            console.log(`${admins.length} administrateur(s) trouvé(s)`);
        }

        // Afficher la configuration de l'application
        console.log('\nConfiguration de l\'application :');
        console.log('- Version:', appConfig.app.version);
        console.log('- Environnement:', appConfig.app.environment);
        console.log('- Jours minimum entre dons:', appConfig.donation.minDaysBetweenDonations);
        console.log('- Âge minimum:', appConfig.donation.minAge);
        console.log('- Âge maximum:', appConfig.donation.maxAge);
        console.log('- Poids minimum:', appConfig.donation.minWeight, 'kg');

        process.exit(0);
    } catch (error) {
        console.error('Erreur lors de l\'initialisation des données admin:', error);
        process.exit(1);
    }
};

initAdminData();
