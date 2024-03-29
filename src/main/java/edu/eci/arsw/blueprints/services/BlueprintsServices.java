/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.AuthorNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {
   
    @Autowired
    BlueprintsPersistence bpp=null;

    @Autowired
    BlueprintsFilter bpf = null;
    
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException{
        this.bpp.saveBlueprint(bp);
    }
    
    public Set<Blueprint> getAllBlueprints(){
        HashSet<Blueprint> setToReturn = new HashSet<Blueprint>();
        for(Blueprint bp : this.bpp.getAllBlueprints()){
            setToReturn.add(bpf.filterBlueprint(bp));
        }
        return setToReturn;
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{

        return  this.bpf.filterBlueprint(this.bpp.getBlueprint(author, name));
    }
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws AuthorNotFoundException{
        Set<Blueprint> setToReturn = this.bpp.getBlueprintsByAuthor(author);
        for(Blueprint b : setToReturn){
            b = bpf.filterBlueprint(b);
        }
        return setToReturn;
    }
    
    public void updateBlueprint(String author, String bpname, Blueprint bp){
        this.bpp.updateBlueprint(author, bpname, bp);
    }
    
}
