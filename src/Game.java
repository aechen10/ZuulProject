import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

// designed by Albert Chen

class Game 
{
	//Initializes some stuff.
    private Parser parser;
    private Room currentRoom;
    Room starting, upstairs, upupstairs, storageroom, laboratory, gallery, furnace, kitchen, library, ballroom, chamber, winecellar, laundry, downstairs, outside;
    ArrayList<Item> inventory = new ArrayList<Item>();
    Scanner reader = new Scanner(System.in);
    
    /**
     * Create the game and initialize its internal map.
     */
    //The constructor.
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }
    //The main method.
    public static void main(String[] args){
    	Game myGame = new Game();
    	myGame.play();
    }
    
    
    /**
     * Create all the rooms and link their exits together.
     */
    //The rooms are constructed using the Room class.
    private void createRooms()
    {
        // Create the rooms
        starting = new Room("in a large, open room. It looks like some sort of spoopy manor.");
        downstairs = new Room("downstairs. You hear some rodents skittering around on the groud.");
        kitchen = new Room("in a room. Plates of dirty dishes pile above the sink. If only I could do that at home...");
        library = new Room("in a large, open library. You see hundreds of books line the shelves even the ones that were release this year. You look west and you spot a huge ice wall. What??");
        upstairs = new Room("in a upstairs. You hear a distant scream. You recognize it as a friend. Ke shen?");
        upupstairs = new Room("above upstairs. Maybe call it upupstairs?");
        gallery = new Room ("in a large, open gallery. You see hundreds of paintings, all tattered and worn out. Perhaps one of these is worth a million dollars?");
        furnace = new Room ("in a cramped, hot furnace room. You see a furnace in the corner, glowing brightly.");
        storageroom = new Room ("in a dark, cold room. You can hear something in the corner, probably a rodent or something. Whatever. There are a lot of weird toys in here...");
        laboratory = new Room("in a large, cold laboratory. You see some acid in the corner of the room.");
        ballroom = new Room ("in a large, open ballroom. Mirror line the walls, and you see yourself in every direction. You see a door south of you. You try it, but it is locked...");
        laundry = new Room ("in a small, cramped room. There is a washing machine and a drying machine, probably not touched in decades by now. You spot a wall south of you, but this one is different. Is it made of limestone?");
        chamber = new Room ("in a large, spooky chamber. You see a huge inverted pentagram, probably used in witchcraft and occult rituals to conjure up evil spirits.");
        winecellar = new Room ("in a cold, damp room. You smell alcohol and gag. Who even drinks this stuff??");
        outside = new Room ("");
        
        // Initialize room exits.
        starting.setExit("north", upstairs);
        starting.setExit("east", kitchen);
        starting.setExit("south", downstairs);
        starting.setExit("west", library);
        
        kitchen.setExit("west", starting);
        
        library.setExit("east", starting);

        downstairs.setExit("north", starting);
        downstairs.setExit("east", laundry);
        downstairs.setExit("west", ballroom);
        
        ballroom.setExit("east", downstairs);
        
        chamber.setExit("north", ballroom);
        
        laundry.setExit("west", downstairs);
        
        winecellar.setExit("north", laundry);

        upstairs.setExit("north", upupstairs);
        upstairs.setExit("south", starting);
        upstairs.setExit("east", furnace);
        upstairs.setExit("west", gallery);
        
        gallery.setExit("east", upstairs);
        
        furnace.setExit("west", upstairs);

        upupstairs.setExit("north", storageroom);
        upupstairs.setExit("west", laboratory);
        upupstairs.setExit("south", upstairs);

        laboratory.setExit("east", upupstairs);
        
        storageroom.setExit("south", upupstairs);
        
        
        // Start the game in the starting room
        currentRoom = starting;
        
        inventory.add(new Item("flask_of_acid"));
        
        //Puts items in rooms where applicable.
        laboratory.setItem(new Item("acid"));
        winecellar.setItem(new Item("lighter"));
        storageroom.setItem(new Item("metal"));
        chamber.setItem(new Item("flask"));
        

    }
    /**
     *  Main play routine.  Loops until end of play.
     */
    boolean finished = false;
    //Loops the game until the player wins or wants to quit.
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and execute them until the game is over.
                
        while (! finished) {
        	//If you've escaped...
        	if(currentRoom.equals(outside)){
        		System.out.println("You win!");
        		//Smash that loop!
        		break;
        	}
        	//Otherwise, if the "quit" command was entered, make finished true.
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    //Prints the message seen on initial startup.
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Spooky Manor!");
        System.out.println("You are here to rescue your dumb friend, Ke Shen, whom walked in to find his GF Lang Ming and never came out again.");
        System.out.println("I would advise you do get some paper if you want to finish this game fast...");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    //Processes commands with an if-else statements and makes actions in the game accordingly.
    private boolean processCommand(Command command)
    {
    	//This value is only affected by the "quit" function.
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        //All of these "redirect" to their own methods.
        if (commandWord.equals("help")){
            printHelp();
        }
        else if (commandWord.equals("go")){
            goRoom(command);
        }
        else if (commandWord.equals("inventory")) {
			printInventory();
		}
        else if (commandWord.equals("get")){
        	getItem(command);
        }
        else if (commandWord.equals("drop")){
        	dropItem(command);
        }
        else if (commandWord.equals("use")){
        	useItem(command);
        }
        else if (commandWord.equals("quit")) {
        	//Typing quit will make wantToQuit true, and this in turn will make finished true.
            wantToQuit = quit(command);
        }
        return wantToQuit;
    }

	// Implementations of user commands:
    
    //Goes to pick up items.
    public void getItem(Command command) 
    {
    	if(!command.hasSecondWord()) {
            // If there is no second word, we don't know what to pick up...
            System.out.println("Get what?");
            return;
        }

        String itemName = command.getSecondWord();

        // Try to pick up the item.
        Item newItem = currentRoom.getItem(itemName);

        if (newItem == null){
        	//Prevents picking what DNE.
            System.out.println("There is no such item here.");
        }
        /*else if (currentRoom.equals(laboratory)){
        	System.out.println("As you picked up the acid with your hand, you died.");
        	System.out.println("GAME OVER");
        	System.exit(0);
        }*/
        else {
        	//Add the item to your inventory, knock it out of the room's, and send a message confirming the pickup.
            inventory.add(newItem);
            currentRoom.removeItem(itemName);
            System.out.println("You picked up: " + itemName);
        }
    }
    //Drops items.
    public void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // If there is no second word, we don't know what to drop...
            System.out.println("Drop what?");
            return;
        }

        String itemName = command.getSecondWord();

        // Try to drop the item
        Item newItem = null;
        int index = 0;
        for (int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getName().equals(itemName)){
				//If the item is in the inventory, set it to newItem and record the index.
				newItem = inventory.get(i);
				index = i;	
			}
		}
        if (newItem == null){
        	//Prevents dropping what you don't have.
            System.out.println("You can't drop what you don't have.");
        }
        else {
        	//Removes from inventory, adds to room, and confirms this.
            inventory.remove(index);
            currentRoom.setItem(new Item(itemName));
            System.out.println("You dropped: " + itemName);
            System.out.println("\n" + currentRoom.getRoomItems());
        }
    }
    //Uses items.
    public void useItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // If there is no second word, we don't know what to use...
            System.out.println("Use what?");
            return;
        }

        String itemName = command.getSecondWord();

        // Try to use the item.
        Item newItem = null;
        int index = 0;
        for (int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getName().equals(itemName)){
				newItem = inventory.get(i);
				index = i;
			}
		}
        if (newItem == null){
            //Prevents you from using what you don't have.
            System.out.println("You can't use what you don't have.");
        }
        //The cases of when an item can be used:
        else if (newItem.getName().equals("metal")&&currentRoom.equals(furnace)){
        	System.out.println("You see a furnace in the corner of the room. You see it glowing orange, you can feel the heat even from five meters away. You place the scrap metal inside of the furnace.");
        	System.out.println("You close the door, and in a few seconds, the furnace spits out a key! How did that even happen??");
        	inventory.remove(index);
        	inventory.add(new Item("key"));
        	System.out.println("\nYou lose:\nmetal\n\nYou obtain:\nkey");
        }
        else if (newItem.getName().equals("key")&&currentRoom.equals(ballroom)){
        	System.out.println("You slip the key into the keyhole. You hear a click, and you turn the lock. It fits perfectly!");
        	System.out.println("Unfortunately, the key wont budge when you try to pull it out. Whatever.");
        	ballroom.setExit("south", chamber);
        	inventory.remove(index);
        	System.out.println("\nYou lose: key \n\nNew Exit: South");
        	ballroom.changeDescription("in a large, cold room. The door to the south of you is now unlocked.");
        }
        else if (newItem.getName().equals("flask")&&currentRoom.equals(laboratory)){
        	System.out.println("Using the flask, you carefully pour some of the acid into your flask, filling it up.");
        	System.out.println("You smell a pungent odor, almost choking you. Or maybe it's just your body odor?");
        	inventory.remove(index);
        	inventory.add(new Item("flask_of_acid"));
        	System.out.println("\nYou lose:\nflask\n\nYou obtain:\nflask_of_acid");
        }
        else if (newItem.getName().equals("flask_of_acid")&&currentRoom.equals(laundry)){
        	System.out.println("You slowly spray your acid onto the limestone wall, trying your best to disperse it evenly.");
        	System.out.println("The acid starts to eat at the wall, bubbling slowly. You continue to spray until you run out.");
        	System.out.println("Just after a few minutes, it creates a hole in the wall just big enough so that you can crawl through it.");
        	System.out.println("You break the flask becuase you can. Relax, geez.");
        	laundry.setExit("south", winecellar);
        	inventory.remove(index);
        	laundry.changeDescription("in a putrid, disgusting, laundry room. The wall that was here is now a glob of slime.");
        	System.out.println("\nYou lose:\nflask_of_acid\n\nNew Exit: South");
        }
        else if (newItem.getName().equals("lighter")&&currentRoom.equals(library)){
        	System.out.println("You take all the books you can find and pile it near the wall of ice.");
        	System.out.println("You take out the lighter and try to start a fire. Flick. Flick. Flick. You manage to light up the lighter.");
        	System.out.println("Holding the lighter, you put it up to the nearest book. You see it ignite in an instant.");
        	System.out.println("You decide to throw the lighter into the fire to make the fire burn faster.");
        	System.out.println("In a matter of minutes, the whole pile of books are on fire, and you slowly see it melting the wall of ice.");
        	library.setExit("west", outside);
        	inventory.remove(index);
        	library.changeDescription("in a large, burnt down library. Puddles flood the ground, and a there is a small fire where the books used to be glows faintly. ");
        	System.out.println("\nYou lose:\nlighter\n\nNew Exit: West");
        }
        else if (newItem.getName().equals("lighter")&&currentRoom.equals(winecellar)) {
        	System.out.println("You take out your lighter and bring the small fire near a bottle of 35% ABV (Alcohol by Volume) beer. ");
        	System.out.println("It instantly ignites. Well, I really dont know why you did that, becasue there is a gunpower barrel behind you.");
        	System.out.println("BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM!!!!!!!!!!!!");
        	System.out.println("GAME OVER");
        	System.out.println("ggwp hehexd u ded kid LMAO");
        	System.out.println("Thanks for playing. Goodbye.");
        	System.exit(0);
        }
        else {
        	System.out.println("Professor Oak's words echoed... There's a time and place for everthing, but not now.");
        }
    }
    //Searches the inventory for the item and returns true if it's there.
    private boolean containsItem(String itemName, ArrayList<Item> inventory){
	    for (int i = 0; i < inventory.size(); i++) {
			if(( inventory.get(i)).getName().equals(itemName)){
				return true;
			}
		}
	    return false;
    }
    //Does what containsItem does but returns an index instead.
    private int itemIndex(String itemName, ArrayList<Item> inventory){
	    for (int i = 0; i < inventory.size(); i++) {
			if((inventory.get(i)).getName().equals(itemName)){
				return i;
			}
		}
	    //Returns -1 if the item DNE.
	    return -1;
    }
    //Prints the inventory.
	private void printInventory() {
		String items = "";
		//Walks through the inventory and adds each item to the String items.
		for (int i = 0; i < inventory.size(); i++) {
			items += inventory.get(i).getName() + " ";
		}
		System.out.println("You have:\n" + items);
	}

    /**
     * Print out some help information.
     */
    private void printHelp() 
    {
        System.out.println("You med bro? u stuck on level bro? u bad bro?? y u heff to be mad???");
        System.out.println("Relax. I'm just kidding.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // If there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        String direction = command.getSecondWord();
        // Try to leave the current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            System.out.println("You can't go that direction.");
        //If the player exits storage via the exit, they win the game.
        else if (direction.equals("west")&&currentRoom.equals(library)){
        	System.out.println("As you crawl forward, you spot a figure. 'KE??' you scream.");
        	System.out.println("lANg miNG XD ehhexXDdjlkhgweiojk;lfjKDKJGOIEG;EWIGJWjdlkfj;WIGHEIOJ;FKLJ WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        	System.out.println("You've rescued ke shen!!");
        	currentRoom.equals(outside);
        	System.out.println("Thank you for playing. Goodbye.");
        	System.exit(0);
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // Signal that we want to quit
    }
}