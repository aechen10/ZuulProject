import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Class Room - a room in an adventure game.
 *
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class Room 
{
    private String description;
    private HashMap exits;        // stores exits of this room.
    ArrayList<Item> roomInventory = new ArrayList<Item>();

    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court 
     * yard".
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap();
    }
    public void changeDescription(String description){
    	this.description = description;
    }
    /**
     * Define an exit from this room.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Return the description of the room (the one that was defined in the
     * constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of this room, in the form:
     *     You are in the kitchen.
     *     Exits: north west
     */
    public String getLongDescription()
    {
        return "You are " + description + "\n" + getRoomString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     */
    private String getRoomString()
    {
        String returnString = "\nExits:\n";
        Set keys = exits.keySet();
        for(Iterator iter = keys.iterator(); iter.hasNext(); )
            returnString +=iter.next() + "\n";
        returnString += "\n";
        returnString += getRoomItems();
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) 
    {
        return (Room)exits.get(direction);
    }
    //Given an index, return the item with that index in the room.
    public Item getItem(int index) {
    	return roomInventory.get(index);
    }
    //Given a name, return the item with that name in the room's inventory.
    public Item getItem(String itemName) {
    	for (int i = 0; i < roomInventory.size(); i++) {
    		if(roomInventory.get(i).getName().equals(itemName)){
    			return roomInventory.get(i);
    		}
		}
    	return null;
    }
    //Remove the item with the given name.
    public void removeItem(String itemName) {
    	for (int i = 0; i < roomInventory.size(); i++) {
    		if(roomInventory.get(i).getName().equals(itemName)){
    			roomInventory.remove(i);
    		}
		}
    	
    }
    //Add the item with the given name.
    public void setItem(Item name){
    	roomInventory.add(name);
    }
    //Returns a list of all the items in the room.
    public String getRoomItems() {
		String items = "";
		for (int i = 0; i < roomInventory.size(); i++) {
			items += roomInventory.get(i).getName() + "\n";
		}
		return "This room contains:\n" + items;
	}
}

