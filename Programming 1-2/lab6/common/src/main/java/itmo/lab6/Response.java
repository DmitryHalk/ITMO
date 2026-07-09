package itmo.lab6;

import java.io.Serializable;
import java.util.HashMap;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private boolean status;
    private HashMap<String, TypeArgs> args;

    public Response(String message, boolean status, HashMap<String, TypeArgs> args) {
        this.message = message;
        this.status = status;
        this.args = args;
    }

    public Response(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public HashMap<String, TypeArgs> getArgs() {
        return args;
    }
}