const User = require('../models/User');
const BloodRequest = require('../models/BloodRequest');
const Donor = require('../models/Donor');
const DonationHistory = require('../models/DonationHistory');

// Obtenir les statistiques générales
exports.getDashboardStats = async (req, res) => {
    try {
        const totalUsers = await User.countDocuments();
        const totalDonors = await Donor.countDocuments();
        const totalRequests = await BloodRequest.countDocuments();
        const activeRequests = await BloodRequest.countDocuments({ status: 'active' });
        const totalDonations = await DonationHistory.countDocuments();
        
        // Statistiques par groupe sanguin
        const bloodGroupStats = await Donor.aggregate([
            {
                $group: {
                    _id: '$bloodGroup',
                    count: { $sum: 1 }
                }
            }
        ]);

        // Dernières demandes de sang
        const recentRequests = await BloodRequest.find()
            .sort({ createdAt: -1 })
            .limit(5)
            .populate('createdBy', 'fullName email');

        res.json({
            success: true,
            data: {
                totalUsers,
                totalDonors,
                totalRequests,
                activeRequests,
                totalDonations,
                bloodGroupStats,
                recentRequests
            }
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Gestion des utilisateurs
exports.getAllUsers = async (req, res) => {
    try {
        const users = await User.find().select('-password');
        res.json({
            success: true,
            count: users.length,
            data: users
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

exports.updateUserRole = async (req, res) => {
    try {
        const { userId } = req.params;
        const { role } = req.body;

        if (!['user', 'admin'].includes(role)) {
            return res.status(400).json({
                success: false,
                message: 'Rôle invalide'
            });
        }

        const user = await User.findByIdAndUpdate(
            userId,
            { role },
            { new: true }
        ).select('-password');

        if (!user) {
            return res.status(404).json({
                success: false,
                message: 'Utilisateur non trouvé'
            });
        }

        res.json({
            success: true,
            data: user
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

exports.deleteUser = async (req, res) => {
    try {
        const { userId } = req.params;

        // Ne pas permettre la suppression du dernier admin
        if (req.user._id.toString() === userId) {
            return res.status(400).json({
                success: false,
                message: 'Vous ne pouvez pas supprimer votre propre compte admin'
            });
        }

        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({
                success: false,
                message: 'Utilisateur non trouvé'
            });
        }

        await user.deleteOne();

        res.json({
            success: true,
            message: 'Utilisateur supprimé avec succès'
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Gestion des demandes de sang
exports.getAllBloodRequests = async (req, res) => {
    try {
        const requests = await BloodRequest.find()
            .populate('createdBy', 'fullName email phone')
            .sort({ createdAt: -1 });

        res.json({
            success: true,
            count: requests.length,
            data: requests
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

exports.updateRequestStatus = async (req, res) => {
    try {
        const { requestId } = req.params;
        const { status } = req.body;

        if (!['active', 'fulfilled', 'cancelled'].includes(status)) {
            return res.status(400).json({
                success: false,
                message: 'Statut invalide'
            });
        }

        const request = await BloodRequest.findByIdAndUpdate(
            requestId,
            { status },
            { new: true }
        );

        if (!request) {
            return res.status(404).json({
                success: false,
                message: 'Demande non trouvée'
            });
        }

        res.json({
            success: true,
            data: request
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Gestion des donneurs
exports.getAllDonors = async (req, res) => {
    try {
        const donors = await Donor.find()
            .populate('user', 'fullName email phone bloodGroup')
            .sort({ createdAt: -1 });

        res.json({
            success: true,
            count: donors.length,
            data: donors
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Rapports et statistiques
exports.generateReport = async (req, res) => {
    try {
        const { startDate, endDate, type } = req.query;
        
        let report;
        
        switch (type) {
            case 'donations':
                report = await DonationHistory.aggregate([
                    {
                        $match: {
                            donationDate: {
                                $gte: new Date(startDate),
                                $lte: new Date(endDate)
                            }
                        }
                    },
                    {
                        $group: {
                            _id: '$bloodType',
                            totalUnits: { $sum: '$unitsdonated' },
                            count: { $sum: 1 }
                        }
                    }
                ]);
                break;
                
            case 'requests':
                report = await BloodRequest.aggregate([
                    {
                        $match: {
                            createdAt: {
                                $gte: new Date(startDate),
                                $lte: new Date(endDate)
                            }
                        }
                    },
                    {
                        $group: {
                            _id: {
                                bloodType: '$bloodType',
                                status: '$status'
                            },
                            count: { $sum: 1 },
                            totalUnits: { $sum: '$unitsNeeded' }
                        }
                    }
                ]);
                break;
                
            default:
                return res.status(400).json({
                    success: false,
                    message: 'Type de rapport invalide'
                });
        }
        
        res.json({
            success: true,
            data: report
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Changer le mot de passe admin
exports.changeAdminPassword = async (req, res) => {
    try {
        const { currentPassword, newPassword } = req.body;

        // Validation du nouveau mot de passe
        if (!newPassword || newPassword.length < 8) {
            return res.status(400).json({
                success: false,
                message: 'Le nouveau mot de passe doit contenir au moins 8 caractères'
            });
        }

        // Vérifier le mot de passe actuel
        const admin = await User.findById(req.user._id).select('+password');
        const isMatch = await admin.comparePassword(currentPassword);
        
        if (!isMatch) {
            return res.status(401).json({
                success: false,
                message: 'Mot de passe actuel incorrect'
            });
        }

        // Mettre à jour le mot de passe
        admin.password = newPassword;
        await admin.save();

        res.json({
            success: true,
            message: 'Mot de passe modifié avec succès'
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Créer un admin
exports.createAdmin = async (req, res) => {
    try {
        const { fullName, email, password, phone } = req.body;

        // Vérifier si l'email existe déjà
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({
                success: false,
                message: 'Un compte existe déjà avec cet email'
            });
        }

        // Créer l'utilisateur admin
        const admin = await User.create({
            fullName,
            email,
            password,
            phone,
            role: 'admin',
            bloodGroup: 'O+', // Valeur par défaut
            age: 30, // Valeur par défaut
            location: 'Administration' // Valeur par défaut
        });

        res.status(201).json({
            success: true,
            data: {
                _id: admin._id,
                fullName: admin.fullName,
                email: admin.email,
                role: admin.role
            }
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};
