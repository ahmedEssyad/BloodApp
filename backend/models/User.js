const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const validator = require('validator');

const userSchema = new mongoose.Schema({
    fullName: {
        type: String,
        required: [true, 'Le nom complet est requis'],
        trim: true,
        minlength: [3, 'Le nom doit contenir au moins 3 caractères']
    },
    email: {
        type: String,
        required: [true, 'L\'email est requis'],
        unique: true,
        lowercase: true,
        trim: true,
        validate: [validator.isEmail, 'Veuillez fournir un email valide']
    },
    password: {
        type: String,
        required: [true, 'Le mot de passe est requis'],
        minlength: [6, 'Le mot de passe doit contenir au moins 6 caractères'],
        select: false
    },
    phone: {
        type: String,
        required: [true, 'Le numéro de téléphone est requis'],
        trim: true
    },
    bloodGroup: {
        type: String,
        required: [true, 'Le groupe sanguin est requis'],
        enum: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']
    },
    age: {
        type: Number,
        required: [true, 'L\'âge est requis'],
        min: [18, 'Vous devez avoir au moins 18 ans'],
        max: [65, 'Vous devez avoir moins de 65 ans']
    },
    location: {
        type: String,
        required: [true, 'La localisation est requise'],
        trim: true
    },
    role: {
        type: String,
        enum: ['user', 'admin'],
        default: 'user'
    },
    isActive: {
        type: Boolean,
        default: true
    },
    lastDonation: {
        type: Date
    },
    totalDonations: {
        type: Number,
        default: 0
    }
}, {
    timestamps: true
});

// Hash password before saving
userSchema.pre('save', async function(next) {
    if (!this.isModified('password')) return next();
    
    try {
        const salt = await bcrypt.genSalt(10);
        this.password = await bcrypt.hash(this.password, salt);
        next();
    } catch (error) {
        next(error);
    }
});

// Compare password method
userSchema.methods.comparePassword = async function(candidatePassword) {
    return await bcrypt.compare(candidatePassword, this.password);
};

const User = mongoose.model('User', userSchema);

module.exports = User;
