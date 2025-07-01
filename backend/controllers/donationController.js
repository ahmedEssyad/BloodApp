const DonationHistory = require('../models/DonationHistory');
const Donor = require('../models/Donor');
const BloodRequest = require('../models/BloodRequest');

// Enregistrer un don
exports.recordDonation = async (req, res) => {
    try {
        const { donorId, bloodRequestId, location, unitsdonated } = req.body;

        // Vérifier que le donneur existe
        const donor = await Donor.findById(donorId);
        if (!donor) {
            return res.status(404).json({
                success: false,
                message: 'Donneur non trouvé'
            });
        }

        // Vérifier l'éligibilité du donneur
        if (!donor.isEligibleToDonate()) {
            return res.status(400).json({
                success: false,
                message: 'Le donneur n\'est pas éligible pour un don en ce moment'
            });
        }

        // Créer l'enregistrement du don
        const donation = await DonationHistory.create({
            donor: donorId,
            bloodRequest: bloodRequestId,
            location,
            unitsdonated,
            donationDate: new Date()
        });

        // Mettre à jour le profil du donneur
        donor.updateDonationDates();
        await donor.save();

        // Si une demande de sang est associée, mettre à jour son statut
        if (bloodRequestId) {
            const bloodRequest = await BloodRequest.findById(bloodRequestId);
            if (bloodRequest) {
                bloodRequest.unitsNeeded -= unitsdonated;
                if (bloodRequest.unitsNeeded <= 0) {
                    bloodRequest.status = 'fulfilled';
                }
                await bloodRequest.save();
            }
        }

        res.status(201).json({
            success: true,
            data: donation
        });
    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

// Obtenir l'historique des dons d'un donneur
exports.getDonationHistory = async (req, res) => {
    try {
        const { donorId } = req.params;
        
        const donations = await DonationHistory.find({ donor: donorId })
            .populate('bloodRequest', 'hospitalName bloodType')
            .sort({ donationDate: -1 });
        
        res.json({
            success: true,
            count: donations.length,
            data: donations
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

// Obtenir les statistiques des dons
exports.getDonationStats = async (req, res) => {
    try {
        const { donorId } = req.params;
        
        const totalDonations = await DonationHistory.countDocuments({ donor: donorId });
        const totalUnits = await DonationHistory.aggregate([
            { $match: { donor: mongoose.Types.ObjectId(donorId) } },
            { $group: { _id: null, total: { $sum: '$unitsdonated' } } }
        ]);
        
        const lastDonation = await DonationHistory.findOne({ donor: donorId })
            .sort({ donationDate: -1 });
        
        res.json({
            success: true,
            data: {
                totalDonations,
                totalUnits: totalUnits[0]?.total || 0,
                lastDonationDate: lastDonation?.donationDate || null
            }
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
