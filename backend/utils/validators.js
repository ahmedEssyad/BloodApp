const validator = require('validator');

exports.validateRegistration = (data) => {
    const errors = {};

    if (!data.fullName || data.fullName.trim().length < 3) {
        errors.fullName = 'Le nom complet doit contenir au moins 3 caractères';
    }

    if (!data.email || !validator.isEmail(data.email)) {
        errors.email = 'Email invalide';
    }

    if (!data.password || data.password.length < 6) {
        errors.password = 'Le mot de passe doit contenir au moins 6 caractères';
    }

    if (!data.phone || !validator.isMobilePhone(data.phone)) {
        errors.phone = 'Numéro de téléphone invalide';
    }

    if (!data.bloodGroup || !['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'].includes(data.bloodGroup)) {
        errors.bloodGroup = 'Groupe sanguin invalide';
    }

    if (!data.age || data.age < 18 || data.age > 65) {
        errors.age = 'L\'âge doit être entre 18 et 65 ans';
    }

    if (!data.location || data.location.trim().length < 2) {
        errors.location = 'La localisation est requise';
    }

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};

exports.validateBloodRequest = (data) => {
    const errors = {};

    if (!data.hospitalName || data.hospitalName.trim().length < 2) {
        errors.hospitalName = 'Le nom de l\'hôpital est requis';
    }

    if (!data.bloodType || !['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'].includes(data.bloodType)) {
        errors.bloodType = 'Groupe sanguin invalide';
    }

    if (!data.unitsNeeded || data.unitsNeeded < 1) {
        errors.unitsNeeded = 'Au moins une unité est requise';
    }

    if (!data.urgencyLevel || !['low', 'medium', 'high'].includes(data.urgencyLevel)) {
        errors.urgencyLevel = 'Niveau d\'urgence invalide';
    }

    if (!data.contactPerson || !data.contactPerson.name || !data.contactPerson.phone) {
        errors.contactPerson = 'Les informations de contact sont requises';
    }

    if (!data.location) {
        errors.location = 'La localisation est requise';
    }

    if (!data.deadline) {
        errors.deadline = 'La date limite est requise';
    }

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};
