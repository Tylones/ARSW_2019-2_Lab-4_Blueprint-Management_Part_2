/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import java.util.HashMap;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
/**
 *
 * @author hcadavid
 */
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
    
    @RequestMapping(value = "/blueprints/{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> UpdateRessourceBlueprint(@PathVariable String author, @PathVariable String name, @RequestBody String body){
        try {
            
        //registrar dato
        System.out.println("Received Post");
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
        
        bs.updateBlueprint(author,name,bp);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception ex) {
        Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
        return new ResponseEntity<>("Failed Insert Post Blueprint",HttpStatus.FORBIDDEN);            
    }
    }
    
    
    
    
}

