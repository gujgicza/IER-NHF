package minions;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Building extends Environment{

    public static final Term	clean = Literal.parseLiteral("clean(x1, y1)");
    public static final Term	refuel = Literal.parseLiteral("refuel(minion)");
    public static final Term	restock = Literal.parseLiteral("restock(minion)");
    
    static Logger logger = Logger.getLogger(Building.class.getName());

    BuildingModel model;
    BuildingView  view;
    private Location kevinPrevious;
    private Location bobPrevious;
    private Location stuartPrevious;
    ArrayList<Location> garbages = new ArrayList<Location>();
    ArrayList<Boolean> garbageValidity = new ArrayList<Boolean>();

    int kevinFuel = 50;
    int bobFuel = 50;
    int stuartFuel = 50;
    int kevinSupplies = 30;
    int bobSupplies = 30;
    int stuartSupplies = 30;

    int kevinOldFuel;
    int bobOldFuel;
    int stuartOldFuel;
    int kevinOldSupplies;
    int bobOldSupplies;
    int stuartOldSupplies;
   

    @Override
    public void init(String[] args) {
        model = new BuildingModel();

        Location kevinLoc = model.getAgPos(0);
        Location bobLoc = model.getAgPos(1);
        Location stuartLoc = model.getAgPos(2);
        kevinPrevious = kevinLoc;
        bobPrevious = bobLoc;
        stuartPrevious = stuartLoc;    

        Literal self1 = Literal.parseLiteral("isSelf(kevin)");
        Literal self2 = Literal.parseLiteral("isSelf(bob)");
        Literal self3 = Literal.parseLiteral("isSelf(stuart)");

        addPercept("kevin", self1);
        addPercept("bob", self2);
        addPercept("stuart", self3);

        Literal limit1 = Literal.parseLiteral("needs_fuel(25, kevin)");
        Literal limit2 = Literal.parseLiteral("needs_fuel(25, bob)");
        Literal limit3 = Literal.parseLiteral("needs_fuel(25, stuart)");
        addPercept("kevin", limit1);
        addPercept("bob", limit2);
        addPercept("stuart", limit3);

        Literal limit4 = Literal.parseLiteral("needs_liquid(15, kevin)");
        Literal limit5 = Literal.parseLiteral("needs_liquid(15, bob)");
        Literal limit6 = Literal.parseLiteral("needs_liquid(15, stuart)");
        addPercept("kevin", limit4);
        addPercept("bob", limit5);
        addPercept("stuart", limit6);

        Location garbage = new Location(2,4);
        garbages.add(garbage);
        garbageValidity.add(true);

        Literal free1 = Literal.parseLiteral("free(kevin)");
        Literal free2 = Literal.parseLiteral("free(bob)");
        Literal free3 = Literal.parseLiteral("free(stuart)");

        addPercept("gru", free1);
        addPercept("gru", free2);
        addPercept("gru", free3);

        view  = new BuildingView(model);
		view.addClickListener(this);
        
        model.setView(view);
        Literal garbage_arrived = Literal.parseLiteral("garbage_arrived(garbage1)");
        Literal garbage_place = Literal.parseLiteral("pos(garbage1, 2, 4)");
        addPercept("gru", garbage_arrived);
        addPercept(garbage_place);
        updatePercepts();
    }
    
    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);
        try {
        	if (action.getFunctor().equals("moveTowards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(ag,x,y);
                int agent = 0;
                if(ag.equals("bob"))
                    agent = 1;
                if(ag.equals("stuart"))
                    agent = 2;
                Location loc = model.getAgPos(agent);

                int fuelCost = model.findPathAndDistanceTo(loc.x, loc.y, x, y).get(2);
                if(ag.equals("kevin"))
                    kevinFuel -= fuelCost;
                if(ag.equals("bob"))
                    bobFuel -= fuelCost;
                if(ag.equals("stuart"))
                    stuartFuel -= fuelCost;
                
            } else if (action.getFunctor().equals("clean")) {
            	int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.clean(ag,x,y);
                garbageValidity.set(garbages.indexOf(new Location(x, y)), false);
                if(ag.equals("kevin"))
                    kevinSupplies -= 5;
                if(ag.equals("bob"))
                    bobSupplies -= 5;
                if(ag.equals("stuart"))
                    stuartSupplies -= 5;
                
            } else if (action.getFunctor().equals("adjust_resource_limits")) {
            	int x = (int)((NumberTerm)action.getTerm(1)).solve();
                int y = (int)((NumberTerm)action.getTerm(2)).solve();
                int fuel;
                if(ag.equals("kevin")){
                    fuel = kevinFuel;
                    Literal allowance = Literal.parseLiteral("is_allowed_to_clean(kevin)");
                    if(model.adjustResourceLimits(ag,x,y, fuel)){
                        addPercept("bob", allowance);
                    }
                    else{
                        removePercept("kevin", allowance);
                    }
                }
                if(ag.equals("bob")){
                    fuel = bobFuel;
                    Literal allowance = Literal.parseLiteral("is_allowed_to_clean(bob)");
                    if(model.adjustResourceLimits(ag,x,y, fuel)){
                        addPercept("bob", allowance);
                    }
                    else{
                        removePercept("bob", allowance);
                    }
                }
                if(ag.equals("stuart")){
                    fuel = stuartFuel;
                    Literal allowance = Literal.parseLiteral("is_allowed_to_clean(stuart)");
                    if(model.adjustResourceLimits(ag,x,y, fuel)){
                        addPercept("stuart", allowance);
                    }
                    else{
                        removePercept("stuart", allowance);
                    }
                }
                
            } else if (action.getFunctor().equals("refuel")) {
                model.refuel(ag);
                System.out.println(kevinFuel);
                if(ag.equals("kevin"))
                    kevinFuel = 50;
                if(ag.equals("bob"))
                    bobFuel = 50;
                if(ag.equals("stuart"))
                    stuartFuel = 50;
                
            } else if (action.getFunctor().equals("restock")) {
                model.restock(ag);
                System.out.println("kevinSupplies " + kevinSupplies);
                if(ag.equals("kevin"))
                    kevinSupplies = 25;
                if(ag.equals("bob"))
                    bobSupplies = 25;
                if(ag.equals("stuart"))
                    stuartSupplies = 25;
                
            } else if(action.getFunctor().equals("task_cleaning")){
                String minion = action.getTerm(0).toString();
                String position = action.getTerm(1).toString();
                Literal command = Literal.parseLiteral("told_to_clean(" + minion + ", " + position + ")");
                Literal busy = Literal.parseLiteral("free(" + minion + ")");
                Literal cleaned = Literal.parseLiteral("garbage_arrived(" + position + ")");
                removePercept("gru", cleaned);
                removePercept("gru", busy);
                addPercept(minion, command);
            }
            else if(action.getFunctor().equals("done")){
                Literal free = Literal.parseLiteral("free(" + action.getTerm(0).toString() + ")");
                addPercept("gru", free);
            }
            else {
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
    
    /** creates the agents' perception based on the BuildingModel */
    void updatePercepts() {
        
        Literal oldPos1 = Literal.parseLiteral("pos(kevin," + kevinPrevious.x + ", " + kevinPrevious.y + ")");
        Literal oldPos2 = Literal.parseLiteral("pos(bob," + bobPrevious.x + ", " + bobPrevious.y + ")");
        Literal oldPos3 = Literal.parseLiteral("pos(stuart," + stuartPrevious.x + ", " + stuartPrevious.y + ")");
        removePercept("kevin", oldPos1);
        removePercept("bob", oldPos2);
        removePercept("stuart", oldPos3);

        Literal oldFuel1 = Literal.parseLiteral("has_fuel(" + kevinOldFuel + ", kevin)");
        Literal oldFuel2 = Literal.parseLiteral("has_fuel(" + bobOldFuel + ", bob)");
        Literal oldFuel3 = Literal.parseLiteral("has_fuel(" + stuartOldFuel + ", stuart)");
        removePercept("kevin", oldFuel1);
        removePercept("bob", oldFuel2);
        removePercept("stuart", oldFuel3);

        kevinOldFuel = kevinFuel;
        bobOldFuel = bobFuel;
        stuartOldFuel = stuartFuel;

        Literal newFuel1 = Literal.parseLiteral("has_fuel(" + kevinFuel + ", kevin)");
        Literal newFuel2 = Literal.parseLiteral("has_fuel(" + bobFuel + ", bob)");
        Literal newFuel3 = Literal.parseLiteral("has_fuel(" + stuartOldFuel + ", stuart)");
        addPercept("kevin", newFuel1);
        addPercept("bob", newFuel2);
        addPercept("stuart", newFuel3);



        Literal oldLiquid1 = Literal.parseLiteral("has_liquid(" + kevinOldSupplies + ", kevin)");
        Literal oldLiquid2 = Literal.parseLiteral("has_liquid(" + bobOldSupplies + ", bob)");
        Literal oldLiquid3 = Literal.parseLiteral("has_liquid(" + stuartOldSupplies + ", stuart)");
        removePercept("kevin", oldLiquid1);
        removePercept("bob", oldLiquid2);
        removePercept("stuart", oldLiquid3);

        kevinOldSupplies = kevinSupplies;
        bobOldSupplies = bobSupplies;
        stuartOldSupplies = stuartSupplies;

        Literal newSupplies1 = Literal.parseLiteral("has_liquid(" + kevinOldSupplies + ", kevin)");
        Literal newSupplies2 = Literal.parseLiteral("has_liquid(" + bobOldSupplies + ", bob)");
        Literal newSupplies3 = Literal.parseLiteral("has_liquid(" + stuartOldSupplies + ", stuart)");
        addPercept("kevin", newSupplies1);
        addPercept("bob", newSupplies2);
        addPercept("stuart", newSupplies3);




        Literal garbage1 = Literal.parseLiteral("garbage(kevin)");
        Literal garbage2 = Literal.parseLiteral("garbage(bob)");
        Literal garbage3 = Literal.parseLiteral("garbage(stuart)");

        Location kevinLoc = model.getAgPos(0);
        Location bobLoc = model.getAgPos(1);
        Location stuartLoc = model.getAgPos(2);

        if(garbages.contains(kevinLoc) && !garbageValidity.get(garbages.indexOf(kevinLoc))){
            removePercept("kevin", garbage1);
        }
        if(garbages.contains(bobLoc) && !garbageValidity.get(garbages.indexOf(bobLoc))){
            removePercept("bob", garbage2);
        }
        if(garbages.contains(stuartLoc) && !garbageValidity.get(garbages.indexOf(stuartLoc))){
            removePercept("stuart", garbage3);
        }

        if(garbages.contains(kevinLoc) && garbageValidity.get(garbages.indexOf(kevinLoc))){
            Literal warning = Literal.parseLiteral("garbage(kevin)");
            addPercept("kevin", warning);

            Literal cleaning = Literal.parseLiteral("is_allowed_to_clean(kevin)");
            addPercept("kevin", cleaning);
        }
        if(garbages.contains(bobLoc) && garbageValidity.get(garbages.indexOf(bobLoc))){
            Literal warning = Literal.parseLiteral("garbage(bob)");
            addPercept("bob", warning);

            Literal cleaning = Literal.parseLiteral("is_allowed_to_clean(bob)");
            addPercept("bob", cleaning);
        }
        if(garbages.contains(stuartLoc) && garbageValidity.get(garbages.indexOf(stuartLoc))){
            Literal warning = Literal.parseLiteral("garbage(stuart)");
            addPercept("stuart", warning);

            Literal cleaning = Literal.parseLiteral("is_allowed_to_clean(stuart)");
            addPercept("stuart", cleaning);
        }


        kevinPrevious = kevinLoc;
        bobPrevious = bobLoc;
        stuartPrevious = stuartLoc;    
        
        Literal pos1 = Literal.parseLiteral("pos(kevin," + kevinLoc.x + "," + kevinLoc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(bob," + bobLoc.x + "," + bobLoc.y + ")");
        Literal pos3 = Literal.parseLiteral("pos(stuart," + stuartLoc.x + "," + stuartLoc.y + ")");

        addPercept("kevin",pos1);
        addPercept("bob",pos2);
        addPercept("stuart",pos3);

        model.setAgPos(0, kevinLoc);
        model.setAgPos(1, bobLoc);
        model.setAgPos(2, stuartLoc);

        informAgsEnvironmentChanged();

    }

    void addGarbage(int x, int y){
        garbages.add(new Location(x, y));
        garbageValidity.add(true);
        model.addGarbage(x, y);

        Literal garbage_arrived = Literal.parseLiteral("garbage_arrived(garbage" + garbages.size() +")");
        Literal garbage_place = Literal.parseLiteral("pos(garbage" + garbages.size() + ", " + x + ", " + y + ")");
        addPercept("gru", garbage_arrived);
        addPercept(garbage_place);
    }
}
