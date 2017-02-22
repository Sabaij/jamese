package com.jaernest;

import com.jaernest.reader.PropertyManager;
import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

public class AppTest {

    //protected static final Properties frameworkProperty = PropertyManager.loadFrameworkPropertyFile("framework.properties");

    @BeforeClass
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        }
        else{
            RestAssured.port = Integer.valueOf(port);
        }


        String basePath = System.getProperty("server.base");
        if(basePath==null){
            basePath = "/api/Errors/4313";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if(baseHost==null){
            baseHost = "https://pe-dev-vh.mcoe.davita.com/api/swagger/docs/v1";
        }
        RestAssured.baseURI = baseHost;

    }

}


