package web.backend.core.customs.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import web.backend.core.customs.Constants;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseCustom<T> {

    public enum Status {
        SUCCESS(Constants.Status.SUCCESS),
        ERROR(Constants.Status.ERROR),
        WARNING(Constants.Status.WARNING),
        INFO(Constants.Status.INFO);

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    private Status status;
    private String message;
    private int code;
    private T data; // Kiểu generic T

    // Constructor thành công
    public ApiResponseCustom(Status status, String message, int code, T data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    // Constructor lỗi
    public ApiResponseCustom(Status status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    // Getter và Setter
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

