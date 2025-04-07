package app.exceptions;

import lombok.Getter;

@Getter

public class ApiException extends RuntimeException {
    private final int statusCode;

    public ApiException(int code, String msg){
        super(msg);
        this.statusCode = code;
    }
}
