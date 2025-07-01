require('dotenv').config();
const mongoose = require('mongoose');
const User = require('./models/User');
const BloodRequest = require('./models/BloodRequest');
const Donor = require('./models/Donor');

const seedData = async () => {
    try {
        await mongoose.connect(process.env.MONGODB_URI);
        console.log('Connected to MongoDB');

        // Clear existing data
        await User.deleteMany({});
        await BloodRequest.deleteMany({});
        await Donor.deleteMany({});

        // Create users
        const users = await User.create([
            {
                fullName: 'Jean Dupont',
                email: 'jean@example.com',
                password: 'password123',
                phone: '0123456789',
                bloodGroup: 'A+',
                age: 30,
                location: 'Dakar'
            },
            {
                fullName: 'Marie Martin',
                email: 'marie@example.com',
                password: 'password123',
                phone: '0123456790',
                bloodGroup: 'O-',
                age: 25,
                location: 'Dakar'
            },
            {
                fullName: 'Ahmed Diop',
                email: 'ahmed@example.com',
                password: 'password123',
                phone: '0123456791',
                bloodGroup: 'B+',
                age: 35,
                location: 'Dakar'
            }
        ]);

        // Create donors
        const donors = await Donor.create([
            {
                user: users[0]._id,
                bloodGroup: 'A+',
                location: 'Dakar',
                weight: 70,
                isAvailable: true
            },
            {
                user: users[1]._id,
                bloodGroup: 'O-',
                location: 'Dakar',
                weight: 65,
                isAvailable: true
            }
        ]);

        // Create blood requests
        const bloodRequests = await BloodRequest.create([
            {
                hospitalName: 'Hôpital Principal de Dakar',
                bloodType: 'A+',
                unitsNeeded: 3,
                urgencyLevel: 'high',
                contactPerson: {
                    name: 'Dr. Sow',
                    phone: '0123456789'
                },
                location: 'Dakar',
                deadline: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
                createdBy: users[2]._id
            },
            {
                hospitalName: 'Clinique El Wafa',
                bloodType: 'O-',
                unitsNeeded: 2,
                urgencyLevel: 'medium',
                contactPerson: {
                    name: 'Dr. Fall',
                    phone: '0123456790'
                },
                location: 'Dakar',
                deadline: new Date(Date.now() + 14 * 24 * 60 * 60 * 1000),
                createdBy: users[2]._id
            }
        ]);

        console.log('Data seeded successfully');
        console.log('Created users:', users.length);
        console.log('Created donors:', donors.length);
        console.log('Created blood requests:', bloodRequests.length);

        process.exit(0);
    } catch (error) {
        console.error('Error seeding data:', error);
        process.exit(1);
    }
};

seedData();
