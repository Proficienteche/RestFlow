package com.proficient.restapi.restclient;

public class Http {

    public enum Status {
        CONTINUE(100, "Continue"),
        SWITCHING_PROTOCOLS(101, "Switching Protocols"),
        PROCESSING(102, "Processing"),
        EARLY_HINTS(103, "Early Hints"),
        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
        NO_CONTENT(204, "No Content"),
        RESET_CONTENT(205, "Reset Content"),
        PARTIAL_CONTENT(206, "Partial Content"),
        MULTI_STATUS(207, "Multi Status"),
        ALREADY_REPORTED(208, "Already Reported"),
        IM_USED(226, "IM Used"),
        MULTIPLE_CHOICES(300, "Multiple Choices"),
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        FOUND(302, "Found"),
        SEE_OTHER(303, "See Other"),
        NOT_MODIFIED(304, "Not Modified"),
        USE_PROXY(305, "Use Proxy"),
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        PERMANENT_REDIRECT(308, "Permanent Redirect"),
        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        PAYMENT_REQUIRED(402, "Payment Required"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
        REQUEST_TIMEOUT(408, "Request Timeout"),
        CONFLICT(409, "Conflict"),
        GONE(410, "Gone"),
        LENGTH_REQUIRED(411, "Length Required"),
        PRECONDITION_FAILED(412, "Precondition Failed"),
        PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
        URI_TOO_LONG(414, "URI Too Long"),
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
        EXPECTATION_FAILED(417, "Expectation Failed"),
        IM_A_TEAPOT(418, "I'm a Teapot"),
        MISDIRECTED_REQUEST(421, "Misdirected Request"),
        UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
        LOCKED(423, "Locked"),
        FAILED_DEPENDENCY(424, "Failed Dependency"),
        TOO_EARLY(425, "Too Early"),
        UPGRADE_REQUIRED(426, "Upgrade Required"),
        PRECONDITION_REQUIRED(428, "Precondition Required"),
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
        UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable for Legal Reasons"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented"),
        BAD_GATEWAY(502, "Bad Gateway"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
        GATEWAY_TIMEOUT(504, "Gateway Timeout"),
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
        VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
        INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
        LOOP_DETECTED(508, "Loop Detected"),
        NOT_EXTENDED(510, "Not Extended"),
        NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),
        UNABLE_TO_CONNECT(600,"Unable to connect server");
        private int code;
        private String reason;

        private Status(int code) {
            this.code = code;
        }

        private Status(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int code() {
            return code;
        }

        public String reason() {
            return reason;
        }
    }

    public static String messageOf(int statusCode) {
        for (Status value : Status.values())
            if (value.code() == statusCode)
                return value.reason();
        return "No reason found from the http status code list";
    }


    public enum ContentType {
        APPLICATION_JSON("application/json"), X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");
        private String name;

        private ContentType(String name) {
            this.name = name;
        }

        public String value() {
            return name;
        }
    }

    public enum Method {
        GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");
        private String name;

        private Method(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Header {
        CONTENT_TYPE("Content-Type"), AUTHORIZATION("Authorization");
        private String name;

        private Header(String name) {
            this.name = name;
        }

        public String value() {
            return name;
        }
        public String caseInsensitiveValue() {
            return name.toLowerCase();
        }
    }
}
