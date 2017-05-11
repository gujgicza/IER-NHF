package minions;

import java.util.Random;
import jason.environment.grid.GridWorldModel;

class BuildingModel extends GridWorldModel {
	
	public static final int GSize = 7; 			// grid size
	public static final int STATION  = 8; 		// station code in grid model
    public static final int GARB  = 16; 		// garbage code in grid model

    Random random = new Random(System.currentTimeMillis());
    
    Minion kevin = new Minion("Kevin");
    Minion bob = new Minion("Bob");
    Minion stuart = new Minion("Stuart");
    
    BuildingModel() {
        super(GSize, GSize, 3);
        
        // initial location of agents
        try {
            setAgPos(0, 0, 0);
            setAgPos(1, 1, 0);
            setAgPos(2, 2, 0);
        
        // location of walls    
            addWall(0, 1, 0, 6);
            addWall(6, 1, 6, 6);
            addWall(2, 1, 4, 1);
            addWall(3, 1, 3, 6);
            addWall(0, 6, 6, 6);
            
       // location of station 
            add(STATION, 6, 0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // initial location of a garbage
        add(GARB, 2, 4);
    }
    
    public void restock(String ag) {
    	switch(ag) {
		case "kevin":
			kevin.restock();
			break;
		case "bob":
			bob.restock();
			break;
		case "stuart":
			stuart.restock();
			break;		
		}	
	}

	public void refuel(String ag) {
		switch(ag) {
		case "kevin":
			kevin.refuel();
			break;
		case "bob":
			bob.refuel();
			break;
		case "stuart":
			stuart.refuel();
			break;		
		}	
	}

	public void adjustResourceLimits(String ag, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void clean(String ag, int x, int y) throws Exception {
		
		switch(ag) {
		case "kevin":
			kevin.reducePower(Minion.CleaningFuelCost);
			kevin.reduceSupply(Minion.CleaningSupplyCost);
			remove(GARB, x, y);
			break;
		case "bob":
			bob.reducePower(Minion.CleaningFuelCost);
			bob.reduceSupply(Minion.CleaningSupplyCost);
			remove(GARB, x, y);
			break;
		case "stuart":
			stuart.reducePower(Minion.CleaningFuelCost);
			stuart.reduceSupply(Minion.CleaningSupplyCost);
			remove(GARB, x, y);
			break;		
		}
    }
    
    public void moveTowards(String ag, int x, int y) {
    	// TODO Auto-generated method stub
    }
}
