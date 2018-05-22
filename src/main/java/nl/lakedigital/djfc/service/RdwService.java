//package nl.lakedigital.djfc.service;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//import com.sun.jersey.api.json.JSONConfiguration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RdwService {
//    public List<String> leesHashTags(String kenteken) {
//        List<String> ret = new ArrayList();
//
//        kenteken = kenteken.replace("-","");
//
//        ClientConfig clientConfig = new DefaultClientConfig();
//        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
//        Client client = Client.create(clientConfig);
//        WebResource webResource = client.resource("https://opendata.rdw.nl/resource/m9d7-ebf2.json?kenteken=" + kenteken);
//        ClientResponse response = webResource.header("X-App-Token", "9HYD14uUG3kN5aobEr3phPHJe").accept("application/json").type("application/json").get(ClientResponse.class);
//
//        String res = response.getEntity(String.class);
////        System.out.println(res);
//
//        int posMerk = res.indexOf("\"merk\"");
//        if (posMerk > 1) {
//            int posMerkEnd = res.indexOf("\"", posMerk + 8);
//            String merk = res.substring(posMerk + 8, posMerkEnd);
//            int posHandelsbenaming = res.indexOf("\"handelsbenaming\"");
//            int posHandelsbenamingEnd = res.indexOf("\"", posHandelsbenaming + 19);
//            String handelsbenaming = res.substring(posHandelsbenaming + 19, posHandelsbenamingEnd);
//
//            //        System.out.println(merk);
//            //        System.out.println(handelsbenaming);
//            //handelsbenaming
//            //merk
//
//            handelsbenaming = handelsbenaming.replace(" ", "").replace(";", "").replace(".", "");
//
//            ret.add(verwerkHashtag(merk));
//            ret.add(verwerkHashtag(handelsbenaming));
//        }
//
//        return ret;
//    }
//
//    private String verwerkHashtag(String hash){
//        String hashtag = hash.replace(" ","").replace("-","").replace("*","").replace(",","").replace("/","");
//
//        try{
//            Integer.parseInt(hashtag);
//
//            return null;
//        }catch (Exception e){
//            return hashtag.toLowerCase();
//        }
//    }
//}
