// Agent gru in project minions

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+garbage_arrived(Position) : true <- !try_to_find_cleaner(Position).

+!try_to_find_cleaner(Position) : free(Minion) & not free(Minion2) & not free(Minion3) & 
								garbage_arrived(Position2) & time_to_clean(Position, Time1) & 
								time_to_clean(Position2, Time2) & Time1 > Time2
 <- task_cleaning(Minion, Position2);
    -garbage_arrived(Position2);
    !try_to_find_cleaner(Position).
    
+!try_to_find_cleaner(Position) : free(Minion) & free(Minion2) & free(Minion3) & 
								distance(Position, Minion, d1) & distance(Position, Minion2, d2) & 
								distance(Position, Minion3, d3) & d1 < d2 & d1 < d3
 <- task_cleaning(Minion, Position);
    -garbage_arrived(Position).

+!try_to_find_cleaner(Position) : free(Minion) & free(Minion2) & 
								distance(Position, Minion, d1) & 
								distance(Position, Minion2, d2) & d1 < d2
 <- task_cleaning(Minion, Position);
    -garbage_arrived(Position).

+!try_to_find_cleaner(Position) : free(Minion)
 <- task_cleaning(Minion, Position);
    -garbage_arrived(Position).

+!try_to_find_cleaner(Position) : true
 <- !try_to_find_cleaner(Position).