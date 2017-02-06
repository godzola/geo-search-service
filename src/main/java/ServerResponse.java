public class ServerResponse {

    String respStr;
    Helpers respHelper;


    // here, we could do routing, too
    // if we don't do it in the Request or Handler

    public ServerResponse(){
        respHelper = new Helpers();
        respStr = respHelper.TwoHunderdResponse("{health_check: \"alive\"}");
    }
}
