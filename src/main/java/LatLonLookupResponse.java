import java.util.LinkedList;

public class LatLonLookupResponse  extends ServerResponse {

    ServerRequest req;

    public LatLonLookupResponse(ServerRequest req, GeoSearchManager gsm){

        super();
        this.req = req;

        LinkedList<String> lats = req.getParamByName("lat");
        LinkedList<String> lons = req.getParamByName("lon");

        if(goodVals(lats) && goodVals(lons)) {

            try {
                Double lat = Double.parseDouble(lats.getFirst());
                Double lon = Double.parseDouble(lons.getFirst());
                String body = gsm.LatLonLookup(lat, lon);
                respStr = respHelper.TwoHunderdResponse(body);
            } catch (NumberFormatException nfe){
                respStr = respHelper.FourHunderdResponse();
            }
        }
    }

    private Boolean goodVals(LinkedList<String> vals){
        return vals != null && vals.size() == 1;
    }




}
