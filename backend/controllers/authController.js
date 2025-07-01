const User = require('../models/User');
const jwt = require('jsonwebtoken');

// Générer un token JWT
const generateToken = (id) => {
    return jwt.sign({ id }, process.env.JWT_SECRET, {
        expiresIn: '7d'
    });
};

// Inscription
exports.register = async (req, res) => {
    try {
        const { fullName, email, password, phone, bloodGroup, age, location } = req.body;

        // Vérifier si l'utilisateur existe déjà
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({
                success: false,
                message: 'Un compte existe déjà avec cet email'
            });
        }

        // Créer nouvel utilisateur
        const user = await User.create({
            fullName,
            email,
            password,
            phone,
            bloodGroup,
            age,
            location
        });

        // Générer token
        const token = generateToken(user._id);

        // Retourner la réponse
        res.status(201).json({
            success: true,
            token,
            user: {
                _id: user._id,
                fullName: user.fullName,
                email: user.email,
                bloodGroup: user.bloodGroup,
                phone: user.phone,
                age: user.age,
                location: user.location
            }
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Connexion
exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;

        // Vérifier si l'email et le mot de passe sont fournis
        if (!email || !password) {
            return res.status(400).json({
                success: false,
                message: 'Veuillez fournir un email et un mot de passe'
            });
        }

        // Trouver l'utilisateur et inclure le mot de passe
        const user = await User.findOne({ email }).select('+password');
        
        if (!user) {
            return res.status(401).json({
                success: false,
                message: 'Email ou mot de passe incorrect'
            });
        }

        // Vérifier le mot de passe
        const isMatch = await user.comparePassword(password);
        
        if (!isMatch) {
            return res.status(401).json({
                success: false,
                message: 'Email ou mot de passe incorrect'
            });
        }

        // Générer token
        const token = generateToken(user._id);

        // Retourner la réponse sans le mot de passe
        res.json({
            success: true,
            token,
            user: {
                _id: user._id,
                fullName: user.fullName,
                email: user.email,
                bloodGroup: user.bloodGroup,
                phone: user.phone,
                age: user.age,
                location: user.location
            }
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Erreur lors de la connexion',
            error: error.message
        });
    }
};

// Récupérer le profil utilisateur
exports.getProfile = async (req, res) => {
    try {
        const user = await User.findById(req.user._id);
        
        res.json({
            success: true,
            user: {
                _id: user._id,
                fullName: user.fullName,
                email: user.email,
                bloodGroup: user.bloodGroup,
                phone: user.phone,
                age: user.age,
                location: user.location,
                totalDonations: user.totalDonations,
                lastDonation: user.lastDonation
            }
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Erreur lors de la récupération du profil',
            error: error.message
        });
    }
};
