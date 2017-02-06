import java.util.HashMap;
import java.util.TreeSet;


public class NameSearcher {

    private HashMap<String,  TreeSet<String>> nameToGeoID;

    public NameSearcher(){
        nameToGeoID = new HashMap<String, TreeSet<String>>();
    }

    public Boolean AddNameIDPair(String n, String id)  {
        Boolean success = true;

        String name = Helpers.UTF8Normalize(n);
        if(name != null) {
            if(nameToGeoID.containsKey(name)) {
                TreeSet<String> update = nameToGeoID.get(name);
                update.add(id);
                nameToGeoID.put(name, update);
            }
            else{
                TreeSet<String> firstEntry = new TreeSet<String>();
                firstEntry.add(id);
                nameToGeoID.put(name, firstEntry);
            }
        }
        else{
            success = false;
        }
        return success;
    }

    public TreeSet<String> NameQuery(String n){
        String name = Helpers.UTF8Normalize(n);
        if(name != null){
            return nameToGeoID.get(name);
        }
        return null;
    }

}
