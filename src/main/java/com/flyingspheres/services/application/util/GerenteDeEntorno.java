package com.flyingspheres.services.application.util;

import javax.inject.Named;
import java.util.Set;

@Named("Entorno")
public class GerenteDeEntorno implements GerenteSensible{
    @Override
    public String getApiKeyString() {
        return getEnvironmentVariable("IBM_API_KEY");
    }

    @Override
    public String getMongoPwd() {
        return getEnvironmentVariable("MONGO_PWD");
    }

    @Override
    public String getMongoUser() {
        return getEnvironmentVariable("MONGO_USER");

    }

    private String getEnvironmentVariable(String key){
        String value =  System.getenv(key);
        if (value == null) {
            System.err.println("System Environment Variable: " + key + " was null");

            Set<String> sysKeys = System.getenv().keySet();
            for (String aKey: sysKeys){
                System.out.println("Key: " + aKey + " Value:" + System.getenv(aKey));
            }

            throw new RuntimeException("System Environment Variable: " + key + " was null");
        }
        return value;
    }
}
