const mongoose = require('mongoose');

const bloodRequestSchema = new mongoose.Schema({
    hospitalName: {
        type: String,
        required: [true, 'Le nom de l\'hôpital est requis'],
        trim: true
    },
    bloodType: {
        type: String,
        required: [true, 'Le groupe sanguin est requis'],
        enum: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']
    },
    unitsNeeded: {
        type: Number,
        required: [true, 'Le nombre d\'unités est requis'],
        min: [1, 'Au moins une unité est requise']
    },
    urgencyLevel: {
        type: String,
        required: [true, 'Le niveau d\'urgence est requis'],
        enum: ['low', 'medium', 'high'],
        default: 'medium'
    },
    contactPerson: {
        name: {
            type: String,
            required: true
        },
        phone: {
            type: String,
            required: true
        }
    },
    status: {
        type: String,
        enum: ['active', 'fulfilled', 'cancelled'],
        default: 'active'
    },
    location: {
        type: String,
        required: true
    },
    deadline: {
        type: Date,
        required: true
    },
    description: {
        type: String
    },
    createdBy: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }
}, {
    timestamps: true
});

bloodRequestSchema.index({ bloodType: 1, status: 1 });
bloodRequestSchema.index({ createdAt: -1 });

const BloodRequest = mongoose.model('BloodRequest', bloodRequestSchema);

module.exports = BloodRequest;
