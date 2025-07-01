const BloodRequest = require('../models/BloodRequest');

// Créer une nouvelle demande de sang
exports.createBloodRequest = async (req, res) => {
    try {
        const requestData = {
            ...req.body,
            createdBy: req.user?._id
        };

        const bloodRequest = await BloodRequest.create(requestData);
        
        res.status(201).json({
            success: true,
            data: bloodRequest
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Obtenir toutes les demandes de sang
exports.getAllRequests = async (req, res) => {
    try {
        const { bloodType, urgencyLevel, status = 'active' } = req.query;
        
        let query = { status };
        
        if (bloodType) {
            query.bloodType = bloodType;
        }
        
        if (urgencyLevel) {
            query.urgencyLevel = urgencyLevel;
        }

        const bloodRequests = await BloodRequest.find(query)
            .sort({ urgencyLevel: -1, createdAt: -1 })
            .populate('createdBy', 'fullName email phone');
        
        res.json({
            success: true,
            count: bloodRequests.length,
            data: bloodRequests
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Obtenir une demande spécifique
exports.getRequest = async (req, res) => {
    try {
        const bloodRequest = await BloodRequest.findById(req.params.id)
            .populate('createdBy', 'fullName email phone');
        
        if (!bloodRequest) {
            return res.status(404).json({
                success: false,
                message: 'Demande non trouvée'
            });
        }
        
        res.json({
            success: true,
            data: bloodRequest
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Mettre à jour une demande
exports.updateRequest = async (req, res) => {
    try {
        const bloodRequest = await BloodRequest.findById(req.params.id);
        
        if (!bloodRequest) {
            return res.status(404).json({
                success: false,
                message: 'Demande non trouvée'
            });
        }
        
        // Vérifier que l'utilisateur est le créateur ou un admin
        if (bloodRequest.createdBy.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
            return res.status(403).json({
                success: false,
                message: 'Non autorisé à modifier cette demande'
            });
        }
        
        const updatedRequest = await BloodRequest.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true, runValidators: true }
        );
        
        res.json({
            success: true,
            data: updatedRequest
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Supprimer une demande
exports.deleteRequest = async (req, res) => {
    try {
        const bloodRequest = await BloodRequest.findById(req.params.id);
        
        if (!bloodRequest) {
            return res.status(404).json({
                success: false,
                message: 'Demande non trouvée'
            });
        }
        
        // Vérifier que l'utilisateur est le créateur ou un admin
        if (bloodRequest.createdBy.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
            return res.status(403).json({
                success: false,
                message: 'Non autorisé à supprimer cette demande'
            });
        }
        
        await bloodRequest.deleteOne();
        
        res.json({
            success: true,
            message: 'Demande supprimée avec succès'
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
