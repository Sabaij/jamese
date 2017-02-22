package com.jaernest;

//mport org.json.JSONObject;
//import org.json.JSONString;
//import java.io.FileReader;
import org.junit.Test;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import org.codehaus.jackson.map.ObjectMapper;
import java.util.Map;
import java.lang.reflect.Type;

import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.response.ResponseBodyData;
import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.core.IsEqual.equalTo;

public class McoeRestTest extends AppTest {

    public static String jsonAsString;
    public static JsonObject paths;
    public static String accessToken;

    @Test
    public void verifyStatus() {
        RestAssured.given().when().get("https://pe-dev-vh.mcoe.davita.com/api/Errors/4313").then().statusCode(400);

    }

    @Test
    public void portalHealth() {
        RestAssured.given().when().get("https://pe-dev-vh.mcoe.davita.com/portal/login").then().statusCode(200);

    }

    @Test
    public void readJsonSwagger(){
        //THIS SECTION WORKS FOR PULLING URL INTO STRING, SHOULD BE USED LATER
        //String sURL = "https://pe-dev-home.mcoe.davita.com/api/swagger/docs/v1";
        String sURL = baseURI;
        System.out.println("SwaggerURL=" + sURL + "!");
        String jsonString = "null";
        try {
            jsonString = readUrl(sURL);
        }catch (Exception e){
            e.printStackTrace();
        }
        // PULL THE FILE INTO JSON/GSON OBJECT
        try {
            //JsonElement jsonElement = new JsonParser()
              //      .parse(new FileReader("/Users/jaernest/Documents/workspace/MavenJson/MavenJson/src/test/Resources/pe.json"));
            JsonElement jsonElement = new JsonParser()
                  .parse(jsonString);

            JsonObject allJson = jsonElement.getAsJsonObject();
            String tempBase = allJson.get("host").toString();
            baseURI = "https://" + tempBase.substring(1, tempBase.length()-1);
            paths = allJson.getAsJsonObject("paths");
            //This works, but not needed to write data to file at this time
            /*FileWriter writeFile = new FileWriter("/Users/jaernest/Documents/workspace/MavenJson/MavenJson/src/test/Resources/paths.json");
            writeFile.write(paths.toString());
            writeFile.close();*/
        }catch (Exception e){
            e.printStackTrace();
        }
        // SEND PATHS SECTION OF JSON TO JACKSON MAP
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(paths.toString(), Map.class);
            //Map map = mapper.readValue(new File("/Users/jaernest/Documents/workspace/MavenJson/MavenJson/src/test/Resources/paths.json"), Map.class);
            for (Object key : map.keySet()){
                String keyString = (String)key;
                basePath = "/api" + keyString;
                Object keyValue = map.get(keyString);
                //String userName = frameworkProperties.getProperty("UserName");
                if(keyString.equals("/Accounts/Login")){
                    System.out.println("BaseURL=" + baseURI + ", BasePath=" + basePath + "!");

                    JsonObject patient = new JsonObject();
                    patient.addProperty("UserName", "jamestest");
                    patient.addProperty("Password", "tester123");
                    patient.addProperty("DeviceId", "HT69W0001574=");

                    String bodyString = patient.toString();
                    System.out.println("PATIENT=" + bodyString + "!");
                    jsonAsString = RestAssured.given()
                            .contentType("application/json")
                            .body(bodyString)
                            .when().post().then()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("Message", equalTo("Success"))
                            .extract().path("Data").toString();

                    JsonElement jsonElement = new JsonParser().parse(jsonAsString);
                    accessToken = jsonElement.getAsJsonObject().get("TokenDetails").getAsJsonObject().get("AccessToken").getAsString();

                }

            }
            //System.out.println(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        // THIS ALL WORKS BUT GSON DOES NOT HAVE .KEY FOR DYNAMIC JSON PATHS ELEMENTS SO USING JACKSON ABOVE
        /*JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        JsonObject allJson = null;
        JsonObject paths, node = null;
        if(element.isJsonObject()){
            allJson = element.getAsJsonObject();
            paths = allJson.get("paths").getAsJsonObject();

        }
            JsonElement oneElement = paths.getAsJsonObject().get("/Accounts/Register");
            paths cannot be converted to JSON array
            String accounts = paths.getAsJsonObject().get("/Accounts/Register").toString();
            */
    }


    @Test
    public void patientGetNurses() {
        System.out.println("NEXT TEST TOKEN=" + accessToken + "!");
        RestAssured.basePath = "/api/Patients/421/Nurse";

        jsonAsString = RestAssured.given()
                .header("AccessToken", accessToken)
                .when().get().then()
                .statusCode(200)
                .body("Phone",equalTo("+18447186797"))
                .extract().path("Phone", "FirstName", "LastName").toString();
        System.out.println("NURSE API CALL URL=" + baseURI + basePath + "!");
        System.out.println("NURSE TEST PHONE RETURNED=" + jsonAsString + "!");
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }


    public class myAccessToken {

        private String token;


        public void setToken(String s) {
            token = s;
        }

        public String getToken() {
            return token;
        }

    }
}