import java.util.LinkedList;

public class NameLookupResponse extends ServerResponse{


    ServerRequest req;

    public NameLookupResponse(ServerRequest req, GeoSearchManager gsm){
        super();
        this.req = req;

        LinkedList<String> names = req.getParamByName("name");

        StringBuilder sb = new StringBuilder();

        if(names != null && names.size() >= 1){
            for(String name : names) {
                sb.append(gsm.NameLookup(name));
            }
        }

        respStr = respHelper.TwoHunderdResponse(sb.toString());
    }

}
