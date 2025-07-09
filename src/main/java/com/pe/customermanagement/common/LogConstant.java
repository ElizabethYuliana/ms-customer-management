package com.pe.customermanagement.common;

public class LogConstant {

    public static final String LOG_SUCCESS = """
                                           \s
        ============================================ Trace Event =================================================================
        TraceParent     : {}
        Method          : {}
        Uri             : {}
        Header          : {}
        Request Body    : {}
        Response Status : {}
        Response Body   : {}
        ==========================================================================================================================""";

    public static final String LOG_ERROR = """
                                           \s
        ============================================ Trace Event =================================================================
        TraceParent     : {}
        Method          : {}
        Uri             : {}
        Header          : {}
        Response Status : {}
        Response Body   : {}
        ==========================================================================================================================""";

    public static final String EVENT_HUB = "Event Hub: {}";

    private LogConstant() {
        // Private constructor to prevent instantiation
    }
}
