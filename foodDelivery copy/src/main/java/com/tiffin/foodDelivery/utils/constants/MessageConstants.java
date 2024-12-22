package com.tiffin.foodDelivery.utils.constants;

import com.tiffin.foodDelivery.utils.functions.MessageCreator;

public class MessageConstants {

    private static String getDataIntegrityViolationMessage(String className) {
        return "This " + className + " is already used. Please check again before deleting";
    }

    public static final class JWT {
        public static final String INVALID_TOKEN = MessageCreator.getJWTErrorMessage("Invalid", "The token is not valid for the user");
        public static final String SECURITY_EXCEPTION = MessageCreator.getJWTErrorMessage("Security" ,"The token's signature is not valid");
        public static final String MALFORMED_JWT_EXCEPTION = MessageCreator.getJWTErrorMessage("Malformed Jwt" ,"The token structure is not valid");
        public static final String EXPIRED_JWT_EXCEPTION = MessageCreator.getJWTErrorMessage("Expired Jwt" ,"The token has expired and is no longer valid");
        public static final String UNSUPPORTED_JWT_EXCEPTION = MessageCreator.getJWTErrorMessage("Unsupported Jwt" ,"The token type is not supported");
        public static final String ILLEGAL_ARGUMENT_EXCEPTION = MessageCreator.getJWTErrorMessage("Illegal Argument" ,"The token is missing claims or the claims are empty");
    }

    public static final class Record {
        public static final String RECORD_FOUND = MessageCreator.getFoundMessage("Record");
        public static final String RECORD_NOT_FOUND = MessageCreator.getNotFoundMessage("Record");
    }

    public static final class UserSuccess {
        public static final String USER_FOUND = MessageCreator.getFoundMessage("User");
        public static final String USER_CREATED = MessageCreator.getCreatedMessage("User");
        public static final String USER_UPDATED = MessageCreator.getUpdatedMessage("User");
        public static final String USER_DELETED = MessageCreator.getDeletedMessage("User", false);
        public static final String USER_DELETED_PERMANENTLY = MessageCreator.getDeletedMessage("User", true);

        public static final String USER_LIST_FOUND = MessageCreator.getListFoundMessage("User");
        public static final String USER_LIST_EMPTY = MessageCreator.getListEmptyMessage("User");

        public static final String LOGIN_SUCCESS = MessageCreator.getLoggedInMessage();
        public static final String LOGOUT_SUCCESS = MessageCreator.getLoggedOutMessage();
        public static final String USER_SESSION_VERIFIED = MessageCreator.getSessionVerificationMessage(true);
        public static final String TOKEN_REFRESHED = "User token refreshed";

        public static final String PASSWORD_UPDATED_SUCCESSFULLY = MessageCreator.getCustomCRUDMessage("Password", "updated");
        public static final String PASSWORD_RESET_SUCCESS = MessageCreator.getCustomCRUDMessage("Password", "reset");
        public static final String PROFILE_UPDATED_SUCCESSFULLY = MessageCreator.getUpdatedMessage("User profile");
    }

    public static final class UserError {
        public static final String USER_NOT_FOUND = MessageCreator.getNotFoundMessage("User");

        public static final String CONFIRM_PASSWORD_DID_NOT_MATCH = "Password and Confirm Password doesn't match";
        public static final String EMAIL_EXISTS = MessageCreator.getExistsMessage("User", "email");

        public static final String USER_ROLE_NOT_FOUND = MessageCreator.getNotFoundMessage("User role");

        public static final String USER_NOT_LOGGED_IN = "You need to login first";
        public static final String AUTHORIZATION_FAILED = "Authorization failed";
        public static final String INVALID_CREDENTIALS = "Invalid credentials";
        public static final String ACCOUNT_LOCKED = "Account is locked. Please contact HR.";
        public static final String ACCOUNT_DISABLED = "Account is disabled. Please contact HR.";
        public static final String ACCOUNT_EXPIRED = "Account has expired. Please contact HR.";

        public static final String DataIntegrityViolation = getDataIntegrityViolationMessage("user");

    }

}

