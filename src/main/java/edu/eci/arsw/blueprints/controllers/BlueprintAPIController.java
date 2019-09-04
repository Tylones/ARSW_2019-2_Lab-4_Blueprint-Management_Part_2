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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 *
 * @author hcadavid
 */
@Service
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {
    
    @Autowired
    BlueprintsServices bs;
    
 
    @RequestMapping(method = RequestMethod.GET)
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
    
    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
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
    
    
    
    
    
    
}

