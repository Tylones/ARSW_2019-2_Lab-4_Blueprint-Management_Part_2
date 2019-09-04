package edu.eci.arsw.blueprints.filters.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;


@Service
public class RedundancyFilter implements BlueprintsFilter {

    @Override
    public Blueprint filterBlueprint(Blueprint bp) {
        Blueprint filteredBlueprint;
        List<Point> filteredPointList = new ArrayList<Point>();
        
        for(Point p : bp.getPoints()){
            if(!filteredPointList.contains(p))
                filteredPointList.add(p);
        }

        Point[] array = new Point[filteredPointList.size()];

        for(int i = 0; i < filteredPointList.size(); i++)
            array[i] = filteredPointList.get(i);
        filteredBlueprint = new Blueprint(bp.getAuthor(), bp.getName(),array);

        return filteredBlueprint;

	}

}