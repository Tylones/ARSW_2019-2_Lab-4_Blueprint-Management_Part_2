package edu.eci.arsw.blueprints.test.filters.impl;

import static org.junit.Assert.*;


import edu.eci.arsw.blueprints.filters.impl.RedundancyFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import org.junit.Test;


public class RedundancyFilterTest {

    @Test
    public void saveNewAndLoadFilteredTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        RedundancyFilter rf = new RedundancyFilter();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15), new Point(40, 40), new Point(40, 40), new Point(15, 15), new Point(30, 30)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);
        
        ibpp.saveBlueprint(bp0);

        Blueprint bpToTest = rf.filterBlueprint(ibpp.getBlueprint("mack", "mypaint"));

        assertArrayEquals("Redundant filter didn't filter well",new Object[]{new Point(40, 40),new Point(15, 15), new Point(30, 30)}, bpToTest.getPoints().toArray());  
    }
}