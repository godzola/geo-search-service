import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class ServerRequest {

    String requestType;
    String pathAndQuery;
    String httpVersion;

    Boolean error;
    Integer errorCode;

    private LinkedList<String> HttpPath;
    private Map<String, LinkedList<String>> QueryMap;



    public ServerRequest(String req){

        QueryMap = new LinkedHashMap<>();

        // could package this up in an object or something
        error = false;
        errorCode = 0;

        // make sure we get the request encoded
        String[] requestLines = req.split("\n");
        String[] reqLine = requestLines[0].split(" ");
        requestType = reqLine[0];
        pathAndQuery = reqLine[1];
        httpVersion = reqLine[2];

        System.out.println("[Request] Req Type: " + requestType);
        System.out.println("[Request] Path and Qry: " + pathAndQuery);
        System.out.println("[Request] Http Ver: " + httpVersion );


        final String[] pathQueryArr = pathAndQuery.split("\\?");
        System.out.println("[Request] query and path array = " + pathQueryArr.length );
        if(pathQueryArr.length == 2){
            try {
                HttpPath = new LinkedList<>(Arrays.asList(pathQueryArr[0].split("/")));
                ParseQuery(pathQueryArr[1]);
            } catch (UnsupportedEncodingException uee) {
                error = true;
                errorCode = 400;
                uee.printStackTrace();
            } catch (NullPointerException npe){
                error = true;
                errorCode = 400;
                npe.printStackTrace();
            }
            System.out.println("[Request] path: " + pathQueryArr[0]);
            System.out.println("[Request] qry:  " + pathQueryArr[1]);
        }
        else{
            error = true;
            errorCode = 400;
            System.out.println("[Request] odd, should return a 400");
        }
    }

    public Boolean pathTo(String part){
        return HttpPath.contains(part);
    }

    public LinkedList<String> getParamByName(String key){
        return QueryMap.get(key);
    }

    private void ParseQuery(String query) throws UnsupportedEncodingException {
        final String[] pairs = query.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!QueryMap.containsKey(key)) {
                QueryMap.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            QueryMap.get(key).add(value);
        }
    }

    public void printPath(){
        if(HttpPath.size() >= 1) {
            System.out.println("[Request] PATH TO STRING: " + HttpPath.toString());

        }
    }

    public void printQueryMap(){
        for(Map.Entry<String, LinkedList<String>> e : QueryMap.entrySet()){
            StringBuilder sb = new StringBuilder();
            sb.append("[ ");
            for(String s : e.getValue()){
                sb.append(s).append(" ");
            }
            sb.append("]");
            System.out.println("[Request] " + e.getKey() + " = " + sb.toString());
        }
    }
}
