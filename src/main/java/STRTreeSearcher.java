import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.io.WKTReader;

import java.util.ArrayList;

public class STRTreeSearcher {

    private STRtree strTree;
    private GeometryFactory gf;
    private WKTReader reader;

    public STRTreeSearcher(){
        strTree = new STRtree();
        gf = new GeometryFactory(new PrecisionModel(), 4326);
        reader = new WKTReader(gf);
    }

    // TODO: An extension of the search manager question about the STRTree being volatile
    // TODO: so we'd need to have 2 methods here
    // TODO:
    // TODO: something like
    // TODO: 1 - GetSTRTree() that returned the reference to the STRTree data member
    // TODO: 2 - ReloadSTRTree() that made STRTree newTree() =  new STRTree() like the constructor and then set this.strTree = newTree()


    public void InsertGeographicArea (GeographicArea geo){
        // inserting the WKT (not WKB) version inside the geo object
        strTree.insert(geo.Geom().getEnvelopeInternal(), geo);
    }

    public ArrayList<GeographicArea> LatLonQuery(Double lat, Double lon){
        ArrayList<GeographicArea> areasContainingPoint = new ArrayList<>();
        Coordinate c = new Coordinate(lon, lat);
        Envelope e = new Envelope(c);
        final Point p = gf.createPoint(c);

        // TODO: make this a lambda?
        for(GeographicArea candidate : (ArrayList<GeographicArea>) strTree.query(e)) {
            if(candidate.Geom().contains(p)) {
                areasContainingPoint.add(candidate);
            }
        }

        return areasContainingPoint;
    }

    public Integer NumAreas(){
        return strTree.size();
    }
}
