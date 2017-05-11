// Agent minion in project minions

/* Initial beliefs and rules */

pos(station,6,0).

/* Initial goals */

/* Plans */

pos(station,0,6).

+told_to_clean(Minion, x1, y1) : true
 <- +pos(place_to_clean, x1, y1);
 	adjust_resource_limits(Minion, x1, y1);
    !go(position);
    -pos(place_to_clean, x1, y1);
    !check_resources(Minion).

+garbage(Minion) : is_allowed_to_clean(Minion)
 <- ?pos(Minion, x1, y1);
    clean(x1, y1);
    !check_resources(Minion).

+!check_resources(Minion) : has_fuel(Fuel, Minion)  & needs_fuel(Fuel_limit, Minion) &
							 Fuel < Fuel_Limit
 <- !go(station);
    refuel(Minion);
    !check_resources(Minion).
    
+!check_resources(Minion) :  has_liquid(Liquid, Minion) & needs_liquid(Liquid_limit, Minion) & 
							Liquid < Liquid_Limit
 <- !go(station);
    restock(Minion).
    
+!check_resources(Minion) : true
	<- true.

+!go(Position) : pos(Position,Xl,Yl) & pos(Minion,Xl,Yl) 
    <- true.

+!go(Position) : true 
    <- ?pos(Position,Xl,Yl);
    moveTowards(Xl,Yl);
    !go(Position).