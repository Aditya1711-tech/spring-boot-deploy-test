package com.tiffin.foodDelivery.utils.functions;

public class MessageCreator {

    private MessageCreator() {
    }

    public static String getJWTErrorMessage(String exceptionName, String message) {
        return "JWT Exception [" + exceptionName + "]: " + message;
    }

    public static String getSessionVerificationMessage(boolean isVerified) {
        return "User session is " + (isVerified ? "verified" : "expired. Log in again.");
    }

    public static String getLoggedInMessage() {
        return "Credentials verified";
    }

    public static String getLoggedOutMessage() {
        return "User logged out successfully";
    }

    public static String getCreatedMessage(String entityName) {
        return entityName + " created successfully";
    }

    public static String getUpdatedMessage(String entityName) {
        return entityName + " updated successfully";
    }

    public static String getCustomCRUDMessage(String entityName, String actionType) {
        return entityName + " " + actionType + " successfully";
    }

    public static String getExistsMessage(String entityName, String fieldName) {
        return entityName + " with the same " + fieldName + " already exists";
    }

    public static String getFoundMessage(String entityName) {
        return entityName + " found successfully";
    }

    public static String getNotFoundMessage(String entityName) {
        return entityName + " not found";
    }

    public static String getDeletedMessage(String entityName, Boolean isPermanent) {
        if (isPermanent)
            return entityName + " deleted successfully [Permanent]";
        else
            return entityName + " deleted successfully";
    }

    public static String getListFoundMessage(String entityName) {
        return "All " + entityName + "s found successfully";
    }

    public static String getListEmptyMessage(String entityName) {
        return "No " + entityName + "s found";
    }

    public static String getDataIntegrityViolationMessage(String entityName) {
        return "Data integrity violation on " + entityName;
    }

}

