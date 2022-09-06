package org.apache.coyote.http11.response;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.coyote.http11.request.RequestHeaders;

public class ResponseHeaders {

    private final Map<String, String> values;

    public ResponseHeaders(Map<String, String> values) {
        this.values = values;
    }

    public static ResponseHeaders of(RequestHeaders requestHeaders, ResponseEntity responseEntity) {
        Map<String, String> responseHeaders = new LinkedHashMap<>();
        checkJSessionId(requestHeaders, responseHeaders);
        if (responseEntity.getHttpStatus().isRedirect()) {
            String responseBody = responseEntity.getResponseBody();
            responseHeaders.put("Location", responseBody.split(":")[1]);
            return new ResponseHeaders(responseHeaders);
        }
        ContentType contentType = ContentType.findContentType(responseEntity.getResponseBody());
        responseHeaders.put("Content-Type", "text/" + contentType.name().toLowerCase() + ";charset=utf-8");
        return new ResponseHeaders(responseHeaders);
    }

    private static void checkJSessionId(RequestHeaders requestHeaders, Map<String, String> responseHeaders) {
        if (!requestHeaders.existsJSessionId()) {
            responseHeaders.put("Set-Cookie", "JSESSIONID=".concat(String.valueOf(UUID.randomUUID())));
        }
    }

    public String asString() {
        return this.values.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue() + " ")
                .collect(Collectors.joining("\r\n"));
    }

    public void setContentLength(int length) {
        values.put("Content-Length", String.valueOf(length));
    }
}
