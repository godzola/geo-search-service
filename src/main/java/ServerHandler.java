import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerHandler extends Thread {

    Socket s;
    BufferedReader br;
    PrintWriter pw;
    GeoSearchManager gsm;


    public ServerHandler(Socket s, GeoSearchManager gsmgr) throws IOException {
        this.s = s;
        this.gsm = gsmgr;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pw = new PrintWriter(s.getOutputStream());

    }

    @Override
    public void run(){
        try {
            // 1 - get req as string
            String reqStr = "";

            while (br.ready() || reqStr.length() == 0) {
                reqStr += (char) br.read();
            }

            System.out.println("[Server Handler] Request: " + reqStr);

            // 2 - objectify it!
            ServerRequest req = new ServerRequest(reqStr);

            // 3 - check path and query so we can route it correctly
            ServerResponse resp;

            if(req.pathTo("namelookup") && req.requestType.equalsIgnoreCase("GET")){
                resp = new NameLookupResponse(req, gsm);
            }
            else if(req.pathTo("latlon") && req.requestType.equalsIgnoreCase("GET")){
                resp = new LatLonLookupResponse(req, gsm);
            }
            else{
                resp = new ServerResponse();
            }

            // 4 - write the response
            pw.write(resp.respStr.toCharArray());

            // 5 - close these in the correct order
            //     first the streams, then the socket
            pw.close();
            br.close();
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}

