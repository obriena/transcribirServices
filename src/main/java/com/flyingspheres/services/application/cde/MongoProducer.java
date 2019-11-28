package com.flyingspheres.services.application.cde;

import com.flyingspheres.services.application.util.GerenteDeCredenciales;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import javax.annotation.Resource;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class MongoProducer {

    @Resource(lookup = "mongoHostName", name = "mongoHostName")
    String hostName;

    @Resource( lookup = "mongoPort")
    Integer port;

    @Resource( lookup = "userDataBase", name= "userDataBase")
    String dataBase;

    @Resource( lookup = "mongoConnectString", name = "mongoConnectString")
    String mongoConnectionString; //mongodb+srv://<userId>:<password>@<server>

    @Resource( lookup = "mongoUserId", name = "mongoUserId")
    String mongoUserId;

    @Resource( lookup = "mongoPassword", name = "mongoPassword")
    String mongoPassword;

    @Resource( lookup = "environment", name = "environment")
    String environment;  //valores válidos cloud o local


    @Produces
    public MongoClient createMongo() {
        MongoClient client = null;
        if (environment.equalsIgnoreCase("cloud")){
            String parsedConnection = mongoConnectionString.replace("|", "&");
            parsedConnection = parsedConnection.replace("{password}", GerenteDeCredenciales.getMongoPwd());
            System.out.println("Mongo URL: " + parsedConnection);
            MongoClientURI uri = new MongoClientURI(parsedConnection);

            client = new MongoClient(uri);
        } else {
            client = new MongoClient(new ServerAddress(hostName, port.intValue()), new MongoClientOptions.Builder().build());
        }

        return client;
    }

    @Produces
    public MongoDatabase createDB(MongoClient client) {
        System.out.println("Creating database using collection: " + dataBase);
        return client.getDatabase(dataBase);
    }

    public void close(@Disposes MongoClient toClose) {
        toClose.close();
    }

}
