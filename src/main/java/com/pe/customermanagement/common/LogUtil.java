package com.pe.customermanagement.common;

import com.pe.customermanagement.dto.AuditContext;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

import static com.pe.customermanagement.common.Constant.UNEXPECTED_ERROR_MESSAGE;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Slf4j
public class LogUtil {

    public static void buildLogging(AuditContext auditContext) {
        log.info(LogConstant.LOG_SUCCESS,
                auditContext.getTraceParent(),
                auditContext.getMethod(),
                auditContext.getUri(),
                auditContext.getHeaders().toString(),
                Objects.isNull(auditContext.getRequest()) ? EMPTY : Util.convertObjectToJsonString(auditContext.getRequest()),
                responseString(auditContext),
                auditContext.getStatus());
    }

    public static void buildLoggingError(AuditContext auditContext, Exception ex) {
        log.info(LogConstant.LOG_ERROR,
                auditContext.getTraceParent(),
                auditContext.getMethod(),
                auditContext.getUri(),
                auditContext.getHeaders().toString(),
                Util.convertObjectToJsonString(auditContext.getError()),
                auditContext.getStatus());
        log.error(UNEXPECTED_ERROR_MESSAGE, ex);
    }

    public static void buildLoggingError(Throwable ex) {
        log.error(UNEXPECTED_ERROR_MESSAGE, ex);
    }



    private static String responseString(AuditContext context) {
        String response =  Objects.isNull(context.getResponse()) ? EMPTY : Util.convertObjectToJsonString(context.getResponse());
        String responsePage =  Objects.isNull(context.getResponsePage()) ? EMPTY : Util.convertObjectToJsonString(context.getResponsePage());
        return response.isEmpty() ? responsePage : response;
    }

    private LogUtil() {
    }
}
