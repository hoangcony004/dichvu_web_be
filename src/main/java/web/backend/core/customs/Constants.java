package web.backend.core.customs;

public final class Constants {

    // Ngăn không cho khởi tạo class
    private Constants() {
    }

    // Ví dụ các hằng số
    public static final String API_VERSION = "/api/v1";
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final int TIMEOUT_IN_SECONDS = 30;

    public static final class Role {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }

    public static final class Status {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String WARNING = "warning";
        public static final String INFO = "info";
    }

    public static class ApiCode {
        // 2xx - Thành công
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int ACCEPTED = 202;
        public static final int NON_AUTHORITATIVE_INFORMATION = 203; // Không có thông tin
        public static final int NO_CONTENT = 204;
        public static final int RESET_CONTENT = 205; // Yêu cầu đã được xử lý, nhưng không có nội dung trả về
        public static final int PARTIAL_CONTENT = 206; // Trả về một phần nội
        public static final int MULTI_STATUS = 207; // Trả về nhiều trạng thái khác nhau
        public static final int IM_USED = 226; // Đã xử lý yêu cầu, nhưng có thể có các thông tin khác

        // 3xx - Chuyển hướng
        public static final int MULTIPLE_CHOICES = 300; // Nhiều lựa chọn
        public static final int MOVED_PERMANENTLY = 301; // Di chuyển vĩnh viễn
        public static final int FOUND = 302; // Tạm thời di chuyển
        public static final int SEE_OTHER = 303; // Xem tài nguyên khác
        public static final int NOT_MODIFIED = 304; // Tài nguyên không thay đổi
        public static final int USE_PROXY = 305; // Sử dụng proxy
        public static final int TEMPORARY_REDIRECT = 307; // Chuyển hướng tạm thời
        public static final int PERMANENT_REDIRECT = 308; // Chuyển hướng vĩnh viễn
        public static final int TOO_MANY_REDIRECTS = 310; // Quá nhiều chuyển hướng
        public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451; // Không khả dụng vì lý do pháp lý

        // 4xx - Lỗi phía client
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int PAYMENT_REQUIRED = 402; // Yêu cầu thanh toán
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int CONFLICT = 409;
        public static final int GONE = 410; // Tài nguyên đã bị xóa
        public static final int LENGTH_REQUIRED = 411; // Yêu cầu có độ dài
        public static final int PRECONDITION_FAILED = 412;
        public static final int UNPROCESSABLE_ENTITY = 422;
        public static final int TOO_MANY_REQUESTS = 429;
        public static final int EXPECTATION_FAILED = 417;
        public static final int IM_A_TEAPOT = 418; // Chỉ để vui

        // 5xx - Lỗi phía server
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int NOT_IMPLEMENTED = 501;
        public static final int BAD_GATEWAY = 502;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int GATEWAY_TIMEOUT = 504;
        public static final int HTTP_VERSION_NOT_SUPPORTED = 505; // Phiên bản HTTP không được hỗ trợ
        public static final int INSUFFICIENT_STORAGE = 507; // Không đủ dung lượng
        public static final int LOOP_DETECTED = 508; // Vòng lặp phát hiện
        public static final int NOT_EXTENDED = 510; // Yêu cầu mở rộng không được hỗ trợ
        public static final int NETWORK_AUTHENTICATION_REQUIRED = 511; // Cần xác thực
    }

    public static class ApiMessage {
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
        public static final String LOGIN_FALSE = "Sai thông tin đăng nhập";
        public static final String LOGOUT_SUCCESS = "Đăng xuất thành công";
        public static final String USER_NOT_FOUND = "Người dùng không tồn tại";
        public static final String INVALID_CREDENTIALS = "Thông tin đăng nhập không hợp lệ";
        public static final String TOKEN_BLACKLISTED = "Token đã bị chặn";
        
    }
}
