const mongoose = require('mongoose');

const donationHistorySchema = new mongoose.Schema({
    donor: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Donor',
        required: true
    },
    bloodRequest: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'BloodRequest'
    },
    donationDate: {
        type: Date,
        required: true,
        default: Date.now
    },
    location: {
        type: String,
        required: true
    },
    unitsdonated: {
        type: Number,
        required: true,
        default: 1
    },
    status: {
        type: String,
        enum: ['completed', 'cancelled', 'scheduled'],
        default: 'completed'
    },
    notes: {
        type: String
    }
}, {
    timestamps: true
});

const DonationHistory = mongoose.model('DonationHistory', donationHistorySchema);

module.exports = DonationHistory;
