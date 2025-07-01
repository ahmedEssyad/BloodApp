package com.example.bloodapp.utils;

import com.example.bloodapp.network.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public class ErrorHandler {
    
    public static String parseError(Response<?> response) {
        ErrorResponse errorResponse = null;
        try {
            if (response.errorBody() != null) {
                errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                if (errorResponse != null && errorResponse.getMessage() != null) {
                    return errorResponse.getMessage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Retourner un message par défaut si l'erreur ne peut pas être parsée
        return "Une erreur s'est produite. Veuillez réessayer.";
    }
    
    public static String getErrorMessage(Throwable t) {
        if (t instanceof IOException) {
            return "Erreur de connexion. Vérifiez votre connexion internet.";
        } else {
            return t.getMessage() != null ? t.getMessage() : "Une erreur s'est produite.";
        }
    }
}
