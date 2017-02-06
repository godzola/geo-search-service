import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class GeoSearchManager {

    private HashMap<String, GeographicArea> areaCache;
    private NameSearcher nameSearcher;
    private STRTreeSearcher strTreeSearcher;
    private GeometryFactory gf;

    private WKBReader wkbReader;

    // TODO: can we reliably reload a lot of data w/o restarting the server (600MB+) if manager,
    // TODO: or at least the RSTRTree in the searcher is volatile ???


    private final static String bigQuery =
            "select gid, " +
                    "       pop2000, " +
                    "       name_engli, " +
                    "       name_local, " +
                    "       name_frenc, " +
                    "       name_spani, " +
                    "       name_russi, " +
                    "       name_arabi, " +
                    "       name_chine, " +
                    "       ST_AsBinary(geom) as wkb " +
                    "from gadm0 " +
                    "limit 10";

    public GeoSearchManager(){
        System.out.println("[GeoSearchManager] Creating Search Manager");
        areaCache = new HashMap<String, GeographicArea>();
        nameSearcher = new NameSearcher();
        strTreeSearcher = new STRTreeSearcher();
        gf = new GeometryFactory(new PrecisionModel(), 4326);
        wkbReader = new WKBReader(gf);
        System.out.println("[GeoSearchManager] loading data");
        LoadData();
    }

    private void LoadData(){
        String url = "jdbc:postgresql://localhost/gadm0?user=scot";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try{
            System.out.println("[GeoSearchManager] Connecting to DB");
            conn = DriverManager.getConnection(url);
            st = conn.createStatement();
            System.out.println("[GeoSearchManager] Executing Query:");
            System.out.println("[GeoSearchManager] \"" + bigQuery + "\"");
            rs = st.executeQuery(bigQuery);

            while(rs.next()){
                GeographicArea geo = new GeographicArea()
                        .ID(rs.getString("gid"))
                        .ArabicName(rs.getString("name_arabi"))
                        .ChineseName(rs.getString("name_chine"))
                        .EnglishName(rs.getString("name_engli"))
                        .FrenchName(rs.getString("name_frenc"))
                        .RussianhName(rs.getString("name_russi"))
                        .SpanishName(rs.getString("name_spani"))
                        .Population(rs.getDouble("pop2000"))
                        .Geom(wkbReader.read(rs.getBytes("wkb")));

                // TODO: think this through
                //
                // Odd state of affairs,
                // the STRTree search will return a list of areas, but
                // the name search will return a list of IDs which need to
                // be looked up in the cache to get the areas.
                //
                // We might want to make smaller objects to store in
                // the data structures, and only return the full info if the
                // caller asks for it, i.e. make use of our REST interface.
                //
                strTreeSearcher.InsertGeographicArea(geo);

                nameSearcher.AddNameIDPair(geo.ArabicName(), geo.ID());
                nameSearcher.AddNameIDPair(geo.EnglishName(), geo.ID());
                nameSearcher.AddNameIDPair(geo.FrenchName(), geo.ID());
                nameSearcher.AddNameIDPair(geo.ChineseName(), geo.ID());
                nameSearcher.AddNameIDPair(geo.RussianName(), geo.ID());
                nameSearcher.AddNameIDPair(geo.SpanishName(), geo.ID());

                areaCache.put(geo.ID(), geo);
            }
            System.out.println("[GeoSearchManager] Data Loaded");
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
            System.exit(1);
        }
        catch(UnsupportedEncodingException uee){
            uee.printStackTrace();
            System.exit(2);
        }
        catch(ParseException pe){
            pe.printStackTrace();
            System.exit(3);
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
            System.exit(4);
        }


    }

    //
    // Each method below corresponds to a different rest call
    //
    public String LatLonLookup(Double lat, Double lon){
        System.out.println("[GeoSearchManager] Lat Lon Lookup");
        StringBuilder sb = new StringBuilder();
        ArrayList<GeographicArea> resp = strTreeSearcher.LatLonQuery(lat, lon);
        if(resp != null && resp.size() > 0) {
            for (GeographicArea geo : resp) {
                sb.append(geo.JSONString()).append("\n");
            }
            return sb.toString();
        }
        return "No Results";
    }

    public String NameLookup(String name){
        System.out.println("[GeoSearchManager] NameLookup");
        StringBuilder sb = new StringBuilder();
        TreeSet<String> resp = nameSearcher.NameQuery(name);
        if(resp != null && resp.size() > 0) {
            for (String id : resp) {
                sb.append(areaCache.get(id).JSONString()).append("\n");
            }
            return sb.toString();
        }
        return "No Results";
    }

    // TODO: In case we want to reload the STRTree
    public String ReloadSTRTree(){
        System.out.println("[GeoSearchManager] NameLookup");
        return "OK";
    }
}
