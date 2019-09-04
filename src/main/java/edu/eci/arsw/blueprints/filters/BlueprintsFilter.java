package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;

public interface BlueprintsFilter{

    /**
     * 
     * @param bp The blueprint to filter
     * @return the filtered blueprint
     */
    public Blueprint filterBlueprint(Blueprint bp);




}