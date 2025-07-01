require('dotenv').config();
const mongoose = require('mongoose');
const User = require('./models/User');

const createFirstAdmin = async () => {
    try {
        await mongoose.connect(process.env.MONGODB_URI);
        console.log('Connected to MongoDB');

        // Vérifier si un admin existe déjà
        const adminExists = await User.findOne({ role: 'admin' });
        
        if (adminExists) {
            console.log('Un administrateur existe déjà');
            process.exit(0);
        }

        // Créer le premier admin
        const admin = await User.create({
            fullName: 'Admin Principal',
            email: 'admin@bloodapp.com',
            password: 'Admin123!@#', // Mot de passe fort
            phone: '0123456789',
            bloodGroup: 'O+',
            age: 30,
            location: 'Administration',
            role: 'admin'
        });

        console.log('Premier administrateur créé avec succès :');
        console.log('Email:', admin.email);
        console.log('Mot de passe: Admin123!@#');
        console.log('IMPORTANT: Changez ce mot de passe immédiatement après la première connexion !');

        process.exit(0);
    } catch (error) {
        console.error('Erreur lors de la création de l\'administrateur:', error);
        process.exit(1);
    }
};

createFirstAdmin();
