package minions;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class Building extends Environment{

    public static final Term	clean = Literal.parseLiteral("clean(x1, y1)");
    public static final Term	refuel = Literal.parseLiteral("refuel(minion)");
    public static final Term	restock = Literal.parseLiteral("restock(minion)");
    
    static Logger logger = Logger.getLogger(Building.class.getName());

    private BuildingModel model;
    private BuildingView  view;
    
    @Override
    public void init(String[] args) {
        model = new BuildingModel();
        view  = new BuildingView(model);
        model.setView(view);
        updatePercepts();
    }
    
    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);
        try {
        	if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(ag,x,y);
                
            } else if (action.getFunctor().equals("clean")) {
            	int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.clean(ag,x,y);
                
            } else if (action.getFunctor().equals("adjust_resource_limits")) {
            	int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.adjustResourceLimits(ag,x,y);
                
            } else if (action.equals(refuel)) {
                model.refuel(ag);
                
            } else if (action.equals(restock)) {
                model.restock(ag);
                
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        updatePercepts();

        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }
    
    /** creates the agents perception based on the MarsModel */
    void updatePercepts() {
        clearPercepts();
        
        Location kevinLoc = model.getAgPos(0);
        Location bobLoc = model.getAgPos(1);
        Location stuartLoc = model.getAgPos(2);    
        
        Literal pos1 = Literal.parseLiteral("pos(Kevin," + kevinLoc.x + "," + kevinLoc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(Bob," + bobLoc.x + "," + bobLoc.y + ")");
        Literal pos3 = Literal.parseLiteral("pos(Stuart," + stuartLoc.x + "," + stuartLoc.y + ")");

        addPercept("kevin",pos1);
        addPercept("bob",pos2);
        addPercept("stuart",pos3);

    }
}
