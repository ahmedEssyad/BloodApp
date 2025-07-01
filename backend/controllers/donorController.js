const Donor = require('../models/Donor');
const User = require('../models/User');

// Créer un profil de donneur
exports.createDonorProfile = async (req, res) => {
    try {
        // Vérifier si un profil de donneur existe déjà
        const existingDonor = await Donor.findOne({ user: req.user._id });
        if (existingDonor) {
            return res.status(400).json({
                success: false,
                message: 'Un profil de donneur existe déjà pour cet utilisateur'
            });
        }

        const donorData = {
            user: req.user._id,
            bloodGroup: req.user.bloodGroup,
            location: req.user.location,
            ...req.body
        };

        const donor = await Donor.create(donorData);
        
        res.status(201).json({
            success: true,
            data: donor
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Obtenir le profil d'un donneur
exports.getDonorProfile = async (req, res) => {
    try {
        const donor = await Donor.findOne({ user: req.params.id || req.user._id })
            .populate('user', 'fullName email phone bloodGroup location');
        
        if (!donor) {
            return res.status(404).json({
                success: false,
                message: 'Profil de donneur non trouvé'
            });
        }
        
        res.json({
            success: true,
            data: donor
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Mettre à jour le profil d'un donneur
exports.updateDonorProfile = async (req, res) => {
    try {
        const donor = await Donor.findOne({ user: req.user._id });
        
        if (!donor) {
            return res.status(404).json({
                success: false,
                message: 'Profil de donneur non trouvé'
            });
        }
        
        const updatedDonor = await Donor.findByIdAndUpdate(
            donor._id,
            req.body,
            { new: true, runValidators: true }
        );
        
        res.json({
            success: true,
            data: updatedDonor
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Rechercher des donneurs
exports.searchDonors = async (req, res) => {
    try {
        const { bloodGroup, location } = req.query;
        
        let query = { isAvailable: true };
        
        if (bloodGroup) {
            query.bloodGroup = bloodGroup;
        }
        
        if (location) {
            query.location = new RegExp(location, 'i');
        }
        
        const donors = await Donor.find(query)
            .populate('user', 'fullName phone location')
            .select('-medicalConditions -allergies');
        
        // Filtrer pour ne montrer que les donneurs éligibles
        const eligibleDonors = donors.filter(donor => donor.isEligibleToDonate());
        
        res.json({
            success: true,
            count: eligibleDonors.length,
            data: eligibleDonors
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
