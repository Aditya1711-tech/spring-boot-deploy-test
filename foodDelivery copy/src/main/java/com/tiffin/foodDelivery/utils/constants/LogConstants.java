package com.tiffin.foodDelivery.utils.constants;

public class LogConstants {

    public static String getSessionVerifiedForToken(String token, boolean isVerified) {
        return buildMessage("User Session", isVerified ? "Verified" : "Not Verified, Need To Login Again", "Attempt to check if the user token is valid with token-" + token);
    }

    public static String getLoggedInSuccessMessage(String email, String userId) {
        return buildMessage("Log In Successful", null, "Attempt to login user with Email-" + email + " and ID-" + userId);
    }

    public static String getLogInFailedMessage(String email, String userId, String specificMessage) {
        return buildMessage("Log In Failed", specificMessage, "Attempt to login user with Email-" + email + " and ID-" + userId);
    }

    public static String getAccountActionTakenMessage(String email, String action, String userId, String specificMessage) {
        return buildMessage("Account " + action, specificMessage, "Attempt to login user with Email-" + email + " and ID-" + userId);
    }

    public static String getEntityMapperLogPrefix(String mapperClass, String specificMessage) {
        return buildMessage(mapperClass + " Mapper", specificMessage, "Attempt to map a dto to an entity");
    }

    public static String getAuditorNotFoundMessage(String className, String email, String specificMessage) {
        return buildMessage("Auditor Not Found", specificMessage, "Setting 'createdBy' or 'lastUpdatedBy' to system for " + className + " with Email-" + email);
    }

    public static String getCreatedSuccessfullyMessage(String className, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Created Successfully", specificMessage, "Attempt to create a " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getAlreadyExistsMessage(String className, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Already Exists", specificMessage, "Attempt to create a " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getUpdatedSuccessfullyMessage(String className, String fieldName, Object fieldValue, String toUpdateField, Object toUpdateValue, String specificMessage) {
        return buildMessage("Updated Successfully", specificMessage, "Attempt to update a " + className + " of " + fieldName + "-" + fieldValue + " with " + toUpdateField + "-" + toUpdateValue);
    }

    public static String getAlreadyUpdatedByIdMessage(String className, Long id, String specificMessage) {
        return buildMessage("Already Updated", specificMessage, "Attempt to update a " + className + " of ID-" + id);
    }

    public static String getIsUsedSomewhereMessage(String className, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Is Used", specificMessage, "Attempt to delete a " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getFoundAllMessage(String className, String action, String specificMessage) {
        return buildMessage("Found All", specificMessage, "Attempt to " + action + " " + className);
    }

    public static String getFoundMessage(String className, String action, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Found", specificMessage, "Attempt to " + action + " " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getNotFoundMessage(String className, String action, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Not Found", specificMessage, "Attempt to " + action + " " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getDeletedSuccessfullyMessage(String className, String deleteType, String fieldName, Object fieldValue, String specificMessage) {
        return buildMessage("Deleted " + deleteType, specificMessage, "Attempt to delete a " + className + " with " + fieldName + "-" + fieldValue);
    }

    public static String getWelcomeMailSentSuccessfullyMessage(String email) {
        return buildMessage("Email Sent", null, "New user added to the system with email-" + email);
    }

    public static String getEmailNotSentMessage(String className, String action, String email, String specificMessage) {
        return buildMessage("Email Not Sent", specificMessage, "Attempt to " + action + " " + className + " and send email to-" + email);
    }

    // Helper method to dynamically build the message
    private static String buildMessage(String action, String specificMessage, String baseMessage) {
        if (specificMessage != null && !specificMessage.isEmpty()) {
            return action + " [" + specificMessage + "]: " + baseMessage;
        }
        return action + ": " + baseMessage;
    }
}
