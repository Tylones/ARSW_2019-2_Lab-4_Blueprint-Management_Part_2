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

I then added the functions supporting a GET request for the specific ressource */blueprints/{author}* :

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







## Part II

## Part III

A race condition that could occur from our RestAPI working in a concurrent environment is that, for example, if two clients send a PUT request on the same ressource (e.g. : */blueprints/etienne/third*) that the server receives at the same thime, both threads could be updating the ressource at the same time, resulting in a new value being either one of the two "updated" values of the ressource, or a mix of both.
