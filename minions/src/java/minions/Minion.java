package minions;

public class Minion {
	
	public static final int FuelInit = 50;				// initial values
	public static final int SupplyInit = 30;			// initial values
	
	public static final int FuelLimit = 25; 			// fuel limit
	public static final int SupplyLimit = 15; 			// liquid limit
	
	public static final int CleaningFuelCost = 5;		// cost of cleaning a dust
	public static final int MovingFuelCost = 1;			// cost of moving one step
	
	public static final int CleaningSupplyCost = 5;		// cost of cleaning a dust
	
	String name;
	int power;
	int supply;
	public boolean inside;
	
	Minion(String name) {
		this.name = name;
		power = FuelInit;		
		supply = SupplyInit;
		inside = false;	
	}
	
	void reducePower(int cost) throws Exception {
		if (power - cost < 0)
			throw new Exception( name + " is tired");
		power -= cost;
	}
	
	void reduceSupply(int cost) throws Exception {
		if (supply - cost < 0)
			throw new Exception( name +" has no cleaning supply");
		supply -= cost;
	}
	
	void refuel() {
		power = FuelInit;
	}
	
	void restock() {
		supply = SupplyInit;
	}
}
