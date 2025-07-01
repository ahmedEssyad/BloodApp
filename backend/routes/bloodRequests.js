const express = require('express');
const router = express.Router();
const {
    createBloodRequest,
    getAllRequests,
    getRequest,
    updateRequest,
    deleteRequest
} = require('../controllers/bloodRequestController');
const { auth } = require('../middleware/auth');

// Routes publiques
router.get('/', getAllRequests);
router.get('/:id', getRequest);

// Routes protégées
router.post('/', auth, createBloodRequest);
router.put('/:id', auth, updateRequest);
router.delete('/:id', auth, deleteRequest);

module.exports = router;
