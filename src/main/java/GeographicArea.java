import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
//import java.security.MessageDigest;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class GeographicArea  {


    // byte[] ptext = String.getBytes("UTF-8")
    private String id;
    private String englishName;
    private String spanishName;
    private String frenchName;
    private String russianName;
    private String chineseName;
    private String arabicName;

    private Double pop2000;
    private GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);;
    private WKTReader reader = new WKTReader(gf);
    private Geometry WKTgeom = null;

    // TODO: make this GeoJSON?
    //
    public String JSONString(){
        String engVal = (englishName != null) ? englishName : "";
        String spaVal = (spanishName != null) ? spanishName : "";
        String freVal = (frenchName!= null) ? frenchName : "";
        String rusVal = (russianName != null) ? russianName : "";
        String chiVal = (chineseName != null) ? chineseName : "";
        String araVal = (arabicName != null) ? arabicName : "";

        JsonObject value = Json.createObjectBuilder()
                .add("english", engVal)
                .add("spanish", spaVal)
                .add("french", freVal)
                .add("russian", rusVal)
                .add("chinese", chiVal)
                .add("arabic", araVal)
                .add("population", pop2000)
                .add("wkt", WKTgeom.toString())
                .build();


        return value.toString();
    }

    public String ID(){
        return id;
    }

    public String EnglishName() {
        return englishName;
    }

    public String SpanishName() {
        return spanishName;
    }

    public String FrenchName() {
        return frenchName;
    }

    public String RussianName() {
        return russianName;
    }

    public String ChineseName() {
        return chineseName;
    }

    public String ArabicName() {
        return arabicName;
    }

    public Geometry Geom() {
        return WKTgeom;
    }

    public Double Population(){
        return pop2000;
    }

    public GeographicArea Population(Double pop){
        this.pop2000 = pop;
        return this;
    }

    public GeographicArea ID(String id){
        this.id = id;
        return this;
    }

    public GeographicArea EnglishName(String name) throws UnsupportedEncodingException {
        this.englishName = Helpers.UTF8Normalize(name);
        return this;
    }

    public GeographicArea SpanishName(String name) throws UnsupportedEncodingException {
        this.spanishName = Helpers.UTF8Normalize(name);
        return this;
    }

    public GeographicArea FrenchName(String name) throws UnsupportedEncodingException {
        this.frenchName = Helpers.UTF8Normalize(name);
        return this;
    }

    public GeographicArea RussianhName(String name) throws UnsupportedEncodingException {
        this.russianName = Helpers.UTF8Normalize(name);
        return this;
    }
    public GeographicArea ChineseName(String name) throws UnsupportedEncodingException {
        this.chineseName = Helpers.UTF8Normalize(name);
        return this;
    }

    public GeographicArea ArabicName(String name) throws UnsupportedEncodingException {
        this.arabicName = Helpers.UTF8Normalize(name);
        return this;
    }

    public GeographicArea Geom(Geometry geom) throws NoSuchAlgorithmException {
        // MessageDigest md = MessageDigest.getInstance("SHA");

        // geom is currently wkb, we'll convert it to wkt
        try {
            Geometry geomAsWKT = reader.read(geom.toString());
            this.WKTgeom = geomAsWKT;
        }
        catch(ParseException pe){
            pe.printStackTrace();
        }
        return this;
    }

}
