// Agent minion in project minions

/* Initial beliefs and rules */

pos(station,6,0).


/* Initial goals */

/* Plans */

+told_to_clean(Minion, Place) : pos(Place, X1, Y1) & isSelf(Minion)
 <- adjust_resource_limits(Minion, X1, Y1);
    !go(Place);
    !check_resources(Minion).

+garbage(Minion) : is_allowed_to_clean(Minion)
 <- ?pos(Minion, X1, Y1);
    clean(X1, Y1);
    !check_resources(Minion).


+!check_resources(Minion) : has_fuel(Fuel, Minion)  & needs_fuel(Fuel_limit, Minion) & Fuel < Fuel_limit & isSelf(Minion)
 <- !go(station);
    refuel(Minion);
    !check_resources(Minion).
    
+!check_resources(Minion) :  has_liquid(Liquid, Minion) & needs_liquid(Liquid_limit, Minion) & Liquid < Liquid_limit & isSelf(Minion)
 <- !go(station);
    restock(Minion);
    !check_resources(Minion).

+!check_resources(Minion) :  true
 <- done(Minion).
    
+!check_resources(Minion) : true
	<- true.

+!go(Position) : pos(Position,Xl,Yl) & pos(Minion,Xl,Yl) & isSelf(Minion) & not isSelf(Position)
    <- true.

+!go(Position) : true 
    <- ?pos(Position,Xl,Yl);
    moveTowards(Xl,Yl);
    !go(Position).