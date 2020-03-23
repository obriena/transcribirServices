package com.flyingspheres.services.application.cde;

import com.flyingspheres.services.application.util.GerenteSensible;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import javax.annotation.Resource;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

public class MongoProducer {

    @Resource(lookup = "mongoHostName", name = "mongoHostName")
    String hostName;

    @Resource( lookup = "mongoPort")
    Integer port;

    @Resource( lookup = "userDataBase", name= "userDataBase")
    String dataBase;

    @Resource( lookup = "mongoConnectString", name = "mongoConnectString")
    String mongoConnectionString; //mongodb+srv://<userId>:<password>@<server>

    @Resource( lookup = "environment", name = "environment")
    String environment;  //valores válidos cloud o local

    @Inject
    private @Named("Entorno") GerenteSensible gerenteData;


    @Produces
    public MongoClient createMongo() {
        MongoClient client = null;
        System.out.println("Mongo Raw URL: " + mongoConnectionString);
        try {
            if (environment.equalsIgnoreCase("cloud")){
                String parsedConnection = mongoConnectionString.replace("|", "&");
                parsedConnection = parsedConnection.replace("{mongo_user}", gerenteData.getMongoUser());
                System.out.println("Parsed Connection: " + parsedConnection);
                parsedConnection = parsedConnection.replace("{mongo_password}", gerenteData.getMongoPwd());

                System.out.println("Mongo URL: " + parsedConnection);
                MongoClientURI uri = new MongoClientURI(parsedConnection);

                client = new MongoClient(uri);
            } else {
                client = new MongoClient(new ServerAddress(hostName, port.intValue()), new MongoClientOptions.Builder().build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    @Produces
    public MongoDatabase createDB(MongoClient client){
        if (client == null) {
            throw new RuntimeException("Mongo Client is null.  Environment is missing variables");
        }
        System.out.println("Creating database using collection: " + dataBase);
        return client.getDatabase(dataBase);
    }

    public void close(@Disposes MongoClient toClose) {
        toClose.close();
    }

}
