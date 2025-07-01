const mongoose = require('mongoose');

const donorSchema = new mongoose.Schema({
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    bloodGroup: {
        type: String,
        required: true,
        enum: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']
    },
    lastDonationDate: {
        type: Date
    },
    nextEligibleDate: {
        type: Date
    },
    totalDonations: {
        type: Number,
        default: 0
    },
    isAvailable: {
        type: Boolean,
        default: true
    },
    location: {
        type: String,
        required: true
    },
    medicalConditions: {
        type: String
    },
    weight: {
        type: Number,
        min: [50, 'Le poids minimum pour donner du sang est de 50 kg']
    },
    allergies: {
        type: String
    }
}, {
    timestamps: true
});

// Méthode pour vérifier si le donneur est éligible
donorSchema.methods.isEligibleToDonate = function() {
    if (!this.lastDonationDate) return true;
    
    const daysSinceLastDonation = Math.floor((new Date() - this.lastDonationDate) / (1000 * 60 * 60 * 24));
    return daysSinceLastDonation >= 56; // 56 jours minimum entre deux dons
};

// Mise à jour de la date de prochain don après un don
donorSchema.methods.updateDonationDates = function() {
    this.lastDonationDate = new Date();
    this.nextEligibleDate = new Date(Date.now() + 56 * 24 * 60 * 60 * 1000);
    this.totalDonations += 1;
};

const Donor = mongoose.model('Donor', donorSchema);

module.exports = Donor;
