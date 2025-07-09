package com.pe.customermanagement.common;

public class Constant {

    public static final String SPACE = " ";
    public static final String APPLICATION = "application-";
    public static final String REGION = "east";

    //Message Error
    public static final String INTERNAL_ERROR_MESSAGE   = "Internal server error";
    public static final String NOT_FOUND_ERROR_MESSAGE  = "Customer not found %s";
    public static final String CUSTOMER_NOT_FOUND       = "Customers not found";
    public static final String HEADER_MISSING_MESSAGE   = "Headers are missing: %s";
    public static final String BAD_REQUEST_MESSAGE      = "Please check the request parameters";
    public static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred";
    public static final String RESOURCE_NOT_FOUND_MSG   = "Resource not found";
    public static final String DEVICE_TYPE_ERROR_MSG    = "Invalid device type (only IOS or AND allowed)";
    public static final String HEADERS_ERROR_MSG        = "Headers cannot be empty";
    public static final String EVENT_HUB_OK             = "Event sent successfully";
    public static final String EVENT_HUB_NOK            = "Error sending event to EventHub";


    public static final String CONSUMER_HEADER     = "consumerId";
    public static final String TRACE_PARENT_HEADER = "traceparent";
    public static final String DEVICE_TYPE_HEADER  = "deviceType";
    public static final String DEVICE_ID_HEADER    = "deviceId";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    public static final String ERROR_CODE      = "9999";
    public static final String SUCCESS_CODE    = "0000";

    public static final String COMMA           = ";";
    public static final String COMMA_SPACE     = ", ";

    public static final String IOS_DEVICE      = "IOS";
    public static final String ANDROID_DEVICE  = "AND";


    private Constant() {
    }
}
