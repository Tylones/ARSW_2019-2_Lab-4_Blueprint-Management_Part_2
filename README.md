# ARSW_2019-2_Lab-4_Blueprint-Management_Part_2

## Name :

```
Etienne Maxence Eugene REITZ
GitHub username : Tylones
```

## Build and test instructions : 

Go in the project directory :

* To build the project , run the command : ```mvn package```
* To test the project, run the command : ```mvn test```
* To compile the project, run the command : ```mvn compile```
* To run the project, run the command : ```mvn spring-boot:run```


## Part I

### Modifying the constructor

The constructor of the *InMemoryBlueprintPersistence* class has been modified to add *Blueprints* automatically when it gets created :

```
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("_authorname_", "_bpname_ ",pts);
        
        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15), new Point(40, 40), new Point(35, 35), new Point(35, 35), new Point(35, 35)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);

        Point[] pts1=new Point[]{new Point(20, 20),new Point(10, 10)};
        Blueprint bp1=new Blueprint("etienne", "arsw",pts1);

        Point[] pts2=new Point[]{new Point(10, 10),new Point(5, 5)};
        Blueprint bp2=new Blueprint("etienne", "third",pts2);
        
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        blueprints.put(new Tuple<>(bp0.getAuthor(),bp0.getName()), bp0);
        blueprints.put(new Tuple<>(bp1.getAuthor(),bp1.getName()), bp1);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
        
    }
    
    /*
    ...
    */
}
```

### GET /blueprints

Then, I've implemented the function returning a JSON representation of the list of blueprints. To do so, I used the Gson package allowing me to generate a JSON from a Set :
```
@Service
@RestController
public class BlueprintAPIController {
    
    @Autowired
    BlueprintsServices bs;
    
    @RequestMapping(value = "/blueprints", method = RequestMethod.GET)
    public ResponseEntity<?> getRessourceBlueprints(){
        try{
            Gson gson  = new Gson();
            String jsonToReturn = gson.toJson(bs.getAllBlueprints());
            
            return new ResponseEntity<>(jsonToReturn,HttpStatus.ACCEPTED);
            
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Blueprints not found", HttpStatus.NOT_FOUND);
        }
    }
    
    /*
    ...
    */
}    
```
Here's the result when we go on *https://localhost:80/blueprints* :

![](https://i.imgur.com/0e3RXDA.png)

### GET /blueprints/{author}

I then added the function supporting a GET request for the specific ressource */blueprints/{author}* :

```
@RequestMapping(value = "/blueprints/{author}", method = RequestMethod.GET)
public ResponseEntity<?> getRessourceBlueprintsAuthor(@PathVariable String author){
    try{

        Gson gson  = new Gson();
        String jsonToReturn = gson.toJson(bs.getBlueprintsByAuthor(author));

        return new ResponseEntity<>(jsonToReturn,HttpStatus.ACCEPTED);

    }catch(Exception ex){
        Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE,null,ex);
        return new ResponseEntity<>("Blueprints not found", HttpStatus.NOT_FOUND);
    }
}
```

Here's the result when we want to get the ressource *http://localhost:8080/blueprints/etienne* :

![](https://i.imgur.com/FSdoAOK.png)


### GET /blueprints/{author}/{name}

Finally, I added the function supporting a GET request for a specific ressource, with the author's name and the blueprint's name :

```
@RequestMapping(value = "/blueprints/{author}/{name}", method = RequestMethod.GET)
public ResponseEntity<?> getRessourceBlueprintAuthorAndName(@PathVariable String author, @PathVariable String name){
try{

        Gson gson  = new Gson();
        String jsonToReturn = gson.toJson(bs.getBlueprint(author,name));

        return new ResponseEntity<>(jsonToReturn,HttpStatus.ACCEPTED);

    }catch(Exception ex){
        Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE,null,ex);
        return new ResponseEntity<>("Blueprints not found", HttpStatus.NOT_FOUND);
    }
}
```

Here´s the result when we want to get the ressource *http://localhost:8080/blueprints/etienne/third* :

![](https://i.imgur.com/AqDoSQd.png)

## Part II

### POST /plane

To implement a function allowing users to use a POST request on *http://localhost:80/plane* to create a new blueprint, I needed to parse the body of the request to retrieve the different elements (the name, the bpname and the points). To do so, I parsed the body using *JSONOject* and *JSONArray* :

```
@RequestMapping(value = "/plane", method = RequestMethod.POST)
public ResponseEntity<?> addNewRessourceBlueprint(@RequestBody String body){
    try {
    //registrar dato
        System.out.println("Received Post");
        final JSONObject obj = new JSONObject(body);
        final String name = obj.getString("name");
        final String author = obj.getString("author");

        final JSONArray pointsJson = obj.getJSONArray("points");
        Point[] points = new Point[pointsJson.length()];
        for(int i = 0; i < points.length; i++){
            final JSONObject point = pointsJson.getJSONObject(i);
            points[i] = new Point(point.getInt("x"), point.getInt("y"));
        }


        Blueprint bp = new Blueprint(author, name, points);

        bs.addNewBlueprint(bp);

        return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception ex) {
        Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
        return new ResponseEntity<>("Failed Insert Post Blueprint",HttpStatus.FORBIDDEN);            
    }
}
```

Here's the result when requesting GET on *https://localhost:80/blueprints* :

![](https://i.imgur.com/0e3RXDA.png)

We then execute the command : 

```curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://localhost:8080/plane -d '{"author":"curl","points":[{"x":8,"y":8},{"x":7,"y":7}],"name":"testPost"}'
```

![](https://i.imgur.com/GYx3ETa.png)

We can see that this command returns 201 meaning that the blueprint has been inserted, here's now what we get when we go on *https://localhost:8080/blueprints* :

![](https://i.imgur.com/cZgi5sP.png)

We can see, highlighted, that the blueprint that we passed in the body of the request has been added, and that it is returned and can be seen when requesting the ressource *https://localhost:80/blueprints*.

### PUT /blueprints/{author}

Finally, I've implemented the function allowing users to update the value of a blueprint by executing a PUT request on *https://localhost:8080//blueprints/{author}/{name}". To do so, I´ve implemented a function in the *InMemoryBlueprintPersistence* allowing the program to update a blueprint : 
```
@Override
public void updateBlueprint(String author, String bprintname, Blueprint bp) {
    blueprints.replace(new Tuple<>(author, bprintname), bp);
}
```

I then implemented the function supporting the request :

```
@RequestMapping(value = "/blueprints/{author}/{bpname}", method = RequestMethod.PUT)
public ResponseEntity<?> UpdateRessourceBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody String body){
    try {

        final JSONObject obj = new JSONObject(body);
        final String jsonName = obj.getString("name");
        final String jsonauthor = obj.getString("author");

        final JSONArray pointsJson = obj.getJSONArray("points");
        Point[] points = new Point[pointsJson.length()];
        for(int i = 0; i < points.length; i++){
            final JSONObject point = pointsJson.getJSONObject(i);
            points[i] = new Point(point.getInt("x"), point.getInt("y"));
        }


        Blueprint bp = new Blueprint(jsonauthor, jsonName, points);

        bs.updateBlueprint(author,bpname,bp);
        return new ResponseEntity<>(HttpStatus.CREATED);


    } catch (Exception ex) {
        Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
        return new ResponseEntity<>("Failed Insert Post Blueprint",HttpStatus.FORBIDDEN);            
    }
}
```

Here's what we get when we request *http://localhost:8080/blueprints/curl/testPost* before executing the PUT request :

![](https://i.imgur.com/8TeHW7p.png)

We then execute this command : 

```
curl -i -X PUT -HContent-Type:application/json -HAccept:application/json http://localhost:8080/blueprints/curl/testPost -d '{"author":"curl","points":[{"x":12,"y":12},{"x":11,"y":11}],"name":"testPost"}'
```

![](https://i.imgur.com/eEo3tLC.png)

We can see that we get a 200 response code, meaning that the request was a success and that the value has been updated. Here's what we get when we request *http://localhost:8080/blueprints/curl/testPost* after executing the PUT request :

![](https://i.imgur.com/UMG4RAg.png)

We can see that the values of the points changed, meaning that the function works.


## Part III

A race condition that could occur from our RestAPI working in a concurrent environment is that, for example, if two clients send a PUT request on the same ressource (e.g. : */blueprints/etienne/third*) that the server receives at the same thime, both threads could be updating the ressource at the same time, resulting in a new value being either one of the two "updated" values of the ressource, or a mix of both.
