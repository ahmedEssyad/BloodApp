const express = require('express');
const router = express.Router();
const {
    createDonorProfile,
    getDonorProfile,
    updateDonorProfile,
    searchDonors
} = require('../controllers/donorController');
const { recordDonation, getDonationHistory, getDonationStats } = require('../controllers/donationController');
const { auth } = require('../middleware/auth');

// Routes publiques
router.get('/search', searchDonors);

// Routes protégées
router.post('/', auth, createDonorProfile);
router.get('/profile', auth, getDonorProfile);
router.get('/:id', auth, getDonorProfile);
router.put('/profile', auth, updateDonorProfile);

// Routes pour les dons
router.post('/donations', auth, recordDonation);
router.get('/:donorId/donations', auth, getDonationHistory);
router.get('/:donorId/stats', auth, getDonationStats);

module.exports = router;
