package edu.eci.arsw.blueprints.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.AuthorNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

public class mainProgram{

    public static void main(String[] args) throws BlueprintNotFoundException, BlueprintPersistenceException, AuthorNotFoundException{
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bps = ac.getBean(BlueprintsServices.class);

        System.out.println("Creating new blueprints...");
        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15), new Point(40, 40), new Point(35, 35), new Point(35, 35), new Point(35, 35)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);

        Point[] pts1=new Point[]{new Point(20, 20),new Point(10, 10)};
        Blueprint bp1=new Blueprint("etienne", "arsw",pts1);

        Point[] pts2=new Point[]{new Point(10, 10),new Point(5, 5)};
        Blueprint bp2=new Blueprint("etienne", "third",pts2);

        System.out.println("Adding new blueprints to the BlueprintService...");
        bps.addNewBlueprint(bp0);
        bps.addNewBlueprint(bp1);
        bps.addNewBlueprint(bp2);

        System.out.println("Retrieving blue print \"mypaint\" from the author \"mack\"");
        Blueprint b = bps.getBlueprint("mack", "mypaint");

        System.out.println("Name of the retrieved blueprint : " + b.getName());
        System.out.println("Author of the retrieved blueprint : " + b.getAuthor());


        System.out.println("Number of blueprints from the author \"etienne\" : " + bps.getBlueprintsByAuthor("etienne").size());

        System.out.println("Number of points in the filtered blueprint : " + bps.getBlueprint("mack", "mypaint").getPoints().size());
        
        System.out.println("Points in the filter blueprint : ");
        for(Point p : bps.getBlueprint("mack", "mypaint").getPoints()){
            System.out.println("X : " + p.getX() + "   Y : " + p.getY());
        }
        

    }

}