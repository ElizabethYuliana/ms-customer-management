package com.pe.customermanagement.validator;

import com.pe.customermanagement.dto.AuditContext;
import com.pe.customermanagement.excepcion.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.pe.customermanagement.common.Constant.*;

public class RequiredHeaderValidator  {

    public static Mono<Void> validate(AuditContext context) {
        Map<String, String> headers = context.getHeaders();
        List<String> missingHeaders = new ArrayList<>();

        if (Objects.isNull(headers)) {
            return Mono.error(new BadRequestException(HEADERS_ERROR_MSG));
        }

        validateRequiredHeader(headers, CONSUMER_HEADER, missingHeaders);
        validateRequiredHeader(headers, TRACE_PARENT_HEADER, missingHeaders);
        validateRequiredHeader(headers, DEVICE_TYPE_HEADER, missingHeaders);
        validateRequiredHeader(headers, DEVICE_ID_HEADER, missingHeaders);

        String deviceType = headers.get(DEVICE_TYPE_HEADER);
        if (Objects.nonNull(deviceType) && !List.of(IOS_DEVICE, ANDROID_DEVICE).contains(deviceType.toUpperCase())) {
            missingHeaders.add(DEVICE_TYPE_ERROR_MSG);
        }

        if (!missingHeaders.isEmpty()) {
            return Mono.error(new BadRequestException(String.format(HEADER_MISSING_MESSAGE, String.join(COMMA, missingHeaders))));
        }

        return Mono.empty();
    }

    private static void validateRequiredHeader(Map<String, String> headers, String headerName, List<String> missingHeaders) {
        String value = headers.get(headerName);
        if (StringUtils.isBlank(value)) {
            missingHeaders.add(headerName);
        }
    }

    private RequiredHeaderValidator() {
    }
}
