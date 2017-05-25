
# Simple AD Server To Store AD's for Partners

This Server expose a RESTful API that could be used to manage a list of Ads for Partners. 

# Operations Supported

Following are the operations it will support :
* List all of Campaigns
* Create a Campaign for a given partner
* The details of a Campaign associated with a partner

The information the Campaign consists below:

* partner_id - Partner Id
* ad_content - Content of the Ad

The API responses will be be in JSON.



## Building

```
$ mvnw package
```

## Running

You can run the app through Maven:

```
$ mvnw spring-boot:run
```

or you can run the jar file from the build:

```
$ java -jar target/adserver-0.1.0.jar
```

