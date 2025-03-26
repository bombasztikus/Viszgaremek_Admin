package com.example.teszt.lib;

import java.util.HashMap;

public class Api_error extends Exception {

    public final String error;
    public final String error_code;

    private Api_error(String error, String error_code) {
        super(error);
        this.error = error;
        this.error_code = error_code;
    }

    static public Api_error from_json(HashMap json) {

        return new Api_error(
                (String) json.get("error"),
                (String) json.get("error_code")
        );
    }
}
