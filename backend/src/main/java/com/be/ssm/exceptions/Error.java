package com.be.ssm.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum Error {
        // Client Error
        NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),
        BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST),
        UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
        FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
        CONFLICT(409, "Conflict", HttpStatus.CONFLICT),
        METHOD_NOT_ALLOWED(405, "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
        TOO_MANY_REQUESTS(429, "Too many requests", HttpStatus.TOO_MANY_REQUESTS),
        INVALID_ENUM(422, "Invalid enum", HttpStatus.BAD_REQUEST),

        // Server Error
        UNCATEGORIZED_EXCEPTION(9999, "Unclassified error", HttpStatus.INTERNAL_SERVER_ERROR),
        // Database Error
        DATABASE_ACCESS_ERROR(9998, "Database access error", HttpStatus.INTERNAL_SERVER_ERROR),
        DUPLICATE_KEY(9996, "Duplicate key found", HttpStatus.CONFLICT),
        EMPTY_RESULT(9995, "No result found", HttpStatus.NOT_FOUND),
        NON_UNIQUE_RESULT(9994, "Non-unique result found", HttpStatus.CONFLICT),

        // Account-related errors
        ACCOUNT_NOT_FOUND(1001, "Account not found", HttpStatus.NOT_FOUND),
        ACCOUNT_ALREADY_EXISTS(1002, "Account already exists", HttpStatus.CONFLICT),
        ACCOUNT_UNABLE_TO_SAVE(1003, "Unable to save account", HttpStatus.INTERNAL_SERVER_ERROR),
        ACCOUNT_UNABLE_TO_UPDATE(1004, "Unable to update account", HttpStatus.INTERNAL_SERVER_ERROR),
        ACCOUNT_UNABLE_TO_DELETE(1005, "Unable to delete account", HttpStatus.INTERNAL_SERVER_ERROR),
        ACCOUNT_INVALID_USERNAME(1006, "Invalid username", HttpStatus.BAD_REQUEST),
        ACCOUNT_INVALID_PASSWORD(1007, "Invalid password", HttpStatus.BAD_REQUEST),
        ACCOUNT_LOCKED(1008, "Account is locked", HttpStatus.FORBIDDEN),
        PASSWORD_RESET_FAILED(1009, "Password reset failed", HttpStatus.INTERNAL_SERVER_ERROR),
        PASSWORD_RESET_INVALID_REQUEST(1011, "Invalid password reset request", HttpStatus.BAD_REQUEST),
        ACCOUNT_EMAIL_ALREADY_EXISTS(1012, "Email already exists", HttpStatus.CONFLICT),
        ACCOUNT_USERNAME_ALREADY_EXISTS(1013, "Username already exists", HttpStatus.CONFLICT),
        ACCOUNT_DISABLED(1014, "Account is disabled", HttpStatus.FORBIDDEN),
        REFRESH_TOKEN_NOT_EXPIRED(1015, "Refresh token is not expired", HttpStatus.BAD_REQUEST),

        // JWT token-related errors
        JWT_INVALID(2001, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
        JWT_EXPIRED(2002, "JWT token expired", HttpStatus.UNAUTHORIZED),
        JWT_MALFORMED(2003, "Malformed JWT token", HttpStatus.UNAUTHORIZED),
        INVALID_REFRESH_TOKEN(2004, "Invalid refresh token", HttpStatus.UNAUTHORIZED),

        // Employee errors
        EMPLOYEE_NOT_FOUND(3001, "Employee not found", HttpStatus.NOT_FOUND),

        // Role errors
        ROLE_NOT_FOUND(4001, "Role not found", HttpStatus.NOT_FOUND),

        // Work shift errors
        WORK_SHIFT_NOT_FOUND(5001, "Work shift not found", HttpStatus.NOT_FOUND),

        // Categories errors
        CATEGORIES_NOT_FOUND(6001, "Categories not found", HttpStatus.NOT_FOUND),

        // Products errors
        PRODUCT_NOT_FOUND(7001, "Product not found", HttpStatus.NOT_FOUND),

        // Product Variants errors
        PRODUCT_VARIANT_NOT_FOUND(8001, "Product variant not found", HttpStatus.NOT_FOUND),

        // Customers errors
        CUSTOMER_NOT_FOUND(9001, "Customer not found", HttpStatus.NOT_FOUND),

        // Invoices errors
        INVOICE_NOT_FOUND(100001, "Invoice not found", HttpStatus.NOT_FOUND),

        // Orders errors
        ORDER_NOT_FOUND(110002, "Order not found", HttpStatus.NOT_FOUND),

        // Order items errors
        ORDER_ITEM_NOT_FOUND(110003, "Order item not found", HttpStatus.NOT_FOUND),

        // Payment errors
        PAYMENT_NOT_FOUND(110004, "Payment not found", HttpStatus.NOT_FOUND),

        // Stores error
        STORE_NOT_FOUND(110005, "Store not found", HttpStatus.NOT_FOUND),

        // Store table errors
        STORE_TABLE_NOT_FOUND(110006, "Store table not found", HttpStatus.NOT_FOUND),

        // Store zone errors
        STORE_ZONE_NOT_FOUND(110007, "Store zone not found", HttpStatus.NOT_FOUND),

        // Business logic errors
        INSUFFICIENT_PRIVILEGES(34001, "Insufficient privileges to perform this action", HttpStatus.FORBIDDEN),
        OPERATION_NOT_PERMITTED(34002, "Operation not permitted in current state", HttpStatus.BAD_REQUEST),
        RESOURCE_IN_USE(34003, "Resource is currently in use and cannot be modified", HttpStatus.CONFLICT),
        DEADLINE_EXCEEDED(34004, "Deadline has been exceeded", HttpStatus.BAD_REQUEST),
        QUOTA_EXCEEDED(34005, "Quota limit exceeded", HttpStatus.BAD_REQUEST),
        WORKFLOW_VIOLATION(34006, "Action violates workflow rules", HttpStatus.BAD_REQUEST),
        DATA_INTEGRITY_VIOLATION(34007, "Data integrity constraint violation", HttpStatus.CONFLICT),

        ;

        private final int code;
        private final String message;
        private final HttpStatusCode statusCode;

        /**
         * Constructor for ErrorCode.
         *
         * @param code       the error code
         * @param message    the error message
         * @param statusCode the corresponding HTTP status code
         */
        Error(int code, String message, HttpStatusCode statusCode) {
                this.code = code;
                this.message = message;
                this.statusCode = statusCode;
        }
}