package com.example.bloodapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EligibilityCalculator {
    // Standard waiting period between blood donations (in days)
    private static final int MALE_WAITING_PERIOD = 90; // 3 months for men
    private static final int FEMALE_WAITING_PERIOD = 120; // 4 months for women

    /**
     * Calculate if a donor is eligible to donate based on their last donation date
     * @param lastDonationDate String date in format "yyyy-MM-dd"
     * @param gender String "M" for male, "F" for female
     * @return true if eligible, false otherwise
     */
    public static boolean isEligible(String lastDonationDate, String gender) {
        if (lastDonationDate == null || lastDonationDate.isEmpty()) {
            return true; // No previous donation, so eligible
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date lastDonation = sdf.parse(lastDonationDate);
            Date currentDate = new Date();
            
            long diffInMillis = currentDate.getTime() - lastDonation.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            
            int waitingPeriod = gender.equalsIgnoreCase("M") ? MALE_WAITING_PERIOD : FEMALE_WAITING_PERIOD;
            
            return diffInDays >= waitingPeriod;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Calculate the next eligible donation date
     * @param lastDonationDate String date in format "yyyy-MM-dd"
     * @param gender String "M" for male, "F" for female
     * @return String date in format "yyyy-MM-dd" representing the next eligible donation date
     */
    public static String getNextEligibleDate(String lastDonationDate, String gender) {
        if (lastDonationDate == null || lastDonationDate.isEmpty()) {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date lastDonation = sdf.parse(lastDonationDate);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastDonation);
            
            int waitingPeriod = gender.equalsIgnoreCase("M") ? MALE_WAITING_PERIOD : FEMALE_WAITING_PERIOD;
            calendar.add(Calendar.DAY_OF_YEAR, waitingPeriod);
            
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Calculate days remaining until eligible for next donation
     * @param lastDonationDate String date in format "yyyy-MM-dd"
     * @param gender String "M" for male, "F" for female
     * @return int representing days remaining, 0 if already eligible
     */
    public static int getDaysRemaining(String lastDonationDate, String gender) {
        if (lastDonationDate == null || lastDonationDate.isEmpty()) {
            return 0; // No previous donation, so eligible immediately
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date lastDonation = sdf.parse(lastDonationDate);
            Date currentDate = new Date();
            
            int waitingPeriod = gender.equalsIgnoreCase("M") ? MALE_WAITING_PERIOD : FEMALE_WAITING_PERIOD;
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastDonation);
            calendar.add(Calendar.DAY_OF_YEAR, waitingPeriod);
            Date eligibleDate = calendar.getTime();
            
            if (currentDate.after(eligibleDate)) {
                return 0; // Already eligible
            } else {
                long diffInMillis = eligibleDate.getTime() - currentDate.getTime();
                return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
