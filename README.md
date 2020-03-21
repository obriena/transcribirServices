# Development
Hay es un archivo aquí: ./src/main/java/resources llamada credenciales.txt. 


Esta archivo mira igual a:
```
{
  "apikey": "your API Key",
  "iam_apikey_description": "Auto-generated for key ",
  "iam_apikey_name": "Auto-generated service credentials",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/34662a492545e209b6e1f0178bb0131b::serviceid:ServiceId-1da383b3-70f2-4aaf-8c9e-ed7748ba1153",
  "url": "<url>/speech-to-text/api"
}
```
Necesitas va a http://cloud.ibm.com y crear esta archivo.

Puedes leer más [aquí](../README.md)

# Dev Ops
## Docker:
Comando a construir imagen:
```
docker build -t flyingspheres/transcribirservices:0.1 .
```

comand a correar la imagen:
```
docker run --env-file=docker.env -p 9080:9080 flyingspheres/transcribirservices:0.2
```

abierto un linea comando ventana
```
'docker exec -it <container id> /bin/bash' 
```

push the docker image to github
```
docker push flyingspheres/transcribirservices:0.1
```

# Deploying to Heroku
Heroku solo funciona después de instal las herramientas de línea de comando. 

Cuando corriendo sobre Heroku esta es un variable ambiento llamada PORT.  Esta variable deberia agregar en la archivo: server.xml igual:
```
    <httpEndpoint httpPort="${PORT}" httpsPort="${default.https.port}"
      id="defaultHttpEndpoint" host="*" />
```
 
```
 heroku login
 heroku container:login
 heroku container:push web -a transcribirservices
 heroku container:release web -a transcribirservices
 heroku logs --tail -a transcribirservices
 heroku open -a transcribirservices
 ```


# Deploying to Cloud Foundry:
```
ibmcloud login

ibmcloud target --cf
aarons-mbp:services aaron$ ibmcloud target --cf
Targeted Cloud Foundry (https://api.us-south.cf.cloud.ibm.com)

Targeted org flyingspheres

Targeted space dev


                      
API endpoint:      https://cloud.ibm.com   
Region:            us-south   
User:              aaron@flyingspheres.com   
Account:           Aaron OBrien's Account (34662a492545e209b6e1f0178bb0131b)   
Resource group:    No resource group targeted, use 'ibmcloud target -g RESOURCE_GROUP'   
CF API endpoint:   https://api.us-south.cf.cloud.ibm.com (API version: 2.142.0)   
Org:               flyingspheres   
Space:             dev   

```

OpenSSL> s_client -connect cluster0-shard-00-02-gmcxk.mongodb.net:27017 -showcerts

https://stacktracelog.blogspot.com/2018/09/certificate-chaining-error-ssl.html

keytool -importcert -file ./cert1.cer -keystore ./key.jks -storepass transcribir -storetype jks
keytool -importcert -file ./src/test/resources/cert1.cer -keystore ./target/liberty/wlp/usr/servers/defaultServer/resources/security/key.jks -storepass transcribir -storetype jks


## Cloud Foundry Kubernetes
## Helpful commands
ibmcloud ks cluster ls
ibmcloud ks worker ls --cluster transcribir

kubectl create deployment transcribir --image=us.icr.io/flyingspheres/transcribir:1.0
kubectl get deployments
kubectl expose deployment transcribir --port=9080 --name=transcribir
service/transcribir exposed