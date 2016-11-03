/**
 * The item class. Holds a name and takes a spot as an Object inside the inventory.
 */
public class Item {

	String name;
	//Constructor.
	public Item(String newName) {
		name = newName;
	}
	
	public String getName(){
		return name;
	}

}
