import java.io.UnsupportedEncodingException;


public class Helpers {


    final static String twoHundredResp = "HTML/1.1 200 \r\n";
    final static String fourHundred = "HTML/1.1 400 \r\n";
    final static String fiveHundred = "HTML/1.1 500 \r\n";
    final static String serverName = "Server: Server/1.0 \r\n";
    final static String contentType = "Content-Type: application/json \r\n";
    final static String closeConnection = "Connection: close \r\n";
    final static String blankHeaderLine = "\r\n";

    // one of these methods for each type of response, maybe
    public String TwoHunderdResponse(String body){
        String respStr = twoHundredResp;
        respStr += contentType;
        respStr += serverName;
        respStr += closeConnection;
        respStr += "Content-Length: " + body.length();
        respStr += blankHeaderLine;
        respStr += body;
        return respStr;
    }

    public String FourHunderdResponse(){
        String body = "{ \"error\": 400, \"message\": \"malformed request, check for bad number format\" }\n";
        String respStr = fourHundred;
        respStr += contentType;
        respStr += serverName;
        respStr += closeConnection;
        respStr += "Content-Length: " + body.length();
        respStr += blankHeaderLine;
        respStr += body;
        return respStr;
    }

    // be really conservative about the encoding of the text we're looking up.
    public static String UTF8Normalize(String in){
        String out = null;
        if(in != null) {
            try {
                out = new String(in.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                out = null;
            }
        }
        return out;
    }
}