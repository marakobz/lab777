package common.util;

import java.io.Serializable;

/**
 * Class for get response value.
 */
public class ServerResponse implements Serializable {
    private final Object message;
    private final ResponseCode responseCode;

    public ServerResponse(Object message, ResponseCode responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }


    public Object getMessage() {
        return message;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "ServerResponse{"
                + " message='" + message + '\''
                + ", executeCode=" + responseCode
                + '}';
    }
}
