package com.akson.invaders.server.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ControllerException extends RuntimeException {

    public ControllerException(String msg) {
        super(msg);
    }

}
