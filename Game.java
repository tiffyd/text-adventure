/** * Class Game - the main class of the adventure game * *  *  This class is the main class of a very simple, text based adventure game.   *  Users can walk around some scenery. That's all.  *  It should really be extended to make it more interesting! *  *  Maybe the plot of a CYOA novel. *   *  @author  Tiffany Duong *  @version .1 */class Game {    private Parser parser;    private Room currentRoom;    String roomItems;    String playerItems;    String direction;    String previousDirection;    String back;    Room previousRoom;    private final int MAX_PURSE_WEIGHT = 15;    int purseWeight = 0;    /**     * Create the game and initialise its internal map.     */    public Game()     {        createRooms();        parser = new Parser();    }    /**     * Create all the rooms and link their exits together.     */    private void createRooms()    {        //HINT: If you want to access these in other methods, they should be changed to instance variables        Room garden, entrance, dark, circle, watery, hallway, mud, stalagmite, stairs;          // create the rooms        entrance = new Room("the entrance");        garden = new Room("the sea garden");        dark = new Room("the dark room");        circle = new Room("the room of circles");        watery = new Room("the water cave");        hallway = new Room("the secret hallway");        mud = new Room("the mud room");        stalagmite = new Room("the stalagmite room");        stairs = new Room("the stairs");        // initialise room exits (north, east, south, west);        entrance.setExits(watery, dark, null, garden);        garden.setExits(null, entrance, null, null);        dark.setExits(hallway, circle, null, entrance);        circle.setExits(null, null, null, dark);        watery.setExits(mud, null, entrance, null);        hallway.setExits(stalagmite, null, dark, null);        mud.setExits(null, stalagmite, watery, null);        stalagmite.setExits(stairs, null, hallway, mud);        stairs.setExits(null, null, stalagmite, null);        //initialise room items and locations        garden.refreshItems("food.4");        circle.refreshItems("rock.2");        mud.refreshItems("shovel.8");        hallway.refreshItems("conch.5");        currentRoom = entrance;  // start game at the entrance of the cave        roomItems = currentRoom.getItems();        playerItems = "";        back = "";    }    /**     *  Main play routine.  Loops until end of play.     */    public void play()     {                    System.out.println("\f");	        printWelcome();        // Enter the main command loop.  Here we repeatedly read commands and        // execute them until the game is over.        boolean finished = false;        while (!finished){            Command command = parser.getCommand();            finished = processCommand(command);        }        System.out.println("Thank you for playing!!!!");    }    /**     * Print out the opening message for the player.     */    private void printWelcome()    {        System.out.println();        System.out.println("Welcome to \"Will you Cave When you Sea Danger?\"!");        System.out.println("You won the lottery and decided to take you and your friends to Vietnam, because why not?");        System.out.print("After arriving in Hanoi, the northern capitol of the nation, you signed up for a kayaking trip in Ha Long Bay, a nearby popular tourist destination.");        System.out.println("With its 775 islets, you had decided to venture off alone when you discovered a narrow opening to a beachy cave.");        System.out.print("You park your oar and kayak on the shore of the islet and walk into the cave.");        System.out.println("\n The last thing you see is a million shiny eyes looking down at you and you hear a screech. \n");        System.out.println("You notice as soon as you wake up that there's a layer of water surrounding you.");        System.out.print("You hadn't realized earlier but the tide was rising, and now the opening you had entered through is hidden and covered.");        System.out.println("You're stuck in a magical sea cave and what will you do?");        System.out.println(currentRoom.longDescription());    }    /**     * Given a command, process (that is: execute) the command.     * If this command ends the game, true is returned, otherwise false is     * returned.     */    private boolean processCommand(Command command)     {        if(command.isUnknown()){            System.out.println("I don't know what you mean...");            return false;        }        String commandWord = command.getCommandWord();        if (commandWord.equals("help"))            printHelp();        else if (commandWord.equals("go")){            //back = command;            goRoom(command);        } else if (commandWord.equals("quit")){            if(command.hasSecondWord())                System.out.println("Quit what?");            else                return true;  // signal that we want to quit        }        else if (commandWord.equals("back")){            back(command);        }        else if (commandWord.equals("purse")){            getPurse();        }        else if (commandWord.equals("pick")){            pick(command);        }        else if (commandWord.equals("drop")){            drop(command);        }        else if (commandWord.equals("look")){            look(command);        }        return false;    }    // implementations of user commands:    /**     * Print out some help information.     * Here we print some stupid, cryptic message and a list of the      * command words.     */    private void printHelp()     {        System.out.println("You are in Japan.");        System.out.println("around at Lake Forest High School.");        System.out.println();        System.out.println("Your command words are:");        parser.showCommands();    }    /**      * Try to go to one direction. If there is an exit, enter the new     * room, otherwise print an error message.     */    private void goRoom(Command command)     {        if(!command.hasSecondWord()){            // if there is no second word, we don't know where to go...            System.out.println("Go where?");            return;        }        String direction = command.getSecondWord();        // Try to leave current room.        Room nextRoom = currentRoom.nextRoom(direction);        if (nextRoom == null)            System.out.println("There is no door!");        else {            previousRoom = currentRoom;            currentRoom = nextRoom;            System.out.println(currentRoom.longDescription());            roomItems = currentRoom.getItems();        }    }    private void pick(Command command)    {        if(!command.hasSecondWord()){            System.out.println("pick what?");            return;        }        String object = command.getSecondWord();        if (roomItems==null || roomItems.length()==0){            System.out.println("There's nothing here for you to pick up.");        }        else if (roomItems.indexOf(object) == -1){            System.out.println("There is no " + object + " here");        }        else if (Integer.parseInt(roomItems.substring(roomItems.indexOf(object) + object.length() +1)) + purseWeight>MAX_PURSE_WEIGHT){            System.out.println("Your purse is too heavy. You should drop something before trying to pick something else up.");        }        else{            playerItems += " " + roomItems.substring(roomItems.indexOf(object),(roomItems.indexOf(object) + object.length() +2)); //add room item w/ weight to player items            playerItems.trim(); //trim spaces of player item string            roomItems = roomItems.substring(0, roomItems.indexOf(object)) + roomItems.substring(roomItems.indexOf(object)+2 + object.length());            //remove object from room item string            purseWeight += Integer.parseInt(playerItems.substring(playerItems.indexOf(object) + object.length()+1, playerItems.indexOf(object) + object.length()+2));            System.out.println("You have picked up the " + object + ".");        }    }    /**     * This command gives a survey of the room.     */    private void look(Command command)    {        //String roomItems = currentRoom.getItems();        String weights = "1234567890";        if (roomItems!=null && weights.indexOf(roomItems.substring(roomItems.length()-1))!=-1){            if (command.hasSecondWord())                System.out.println("Look is a one word command.");            else{                if (roomItems.lastIndexOf(" ")==0){                    System.out.println("You see the" + roomItems.substring(0, roomItems.length()-2));                }                else{                    System.out.println("You see a " + roomItems.substring(0, roomItems.lastIndexOf(" ")) + " and a " +                        roomItems.substring(roomItems.lastIndexOf(" ")));                }            }        }        else{            System.out.println("There are no items in this room.");        }    }    /**     * This is a description of each room. garden, entrance, dark, circle, watery, hallway, mud, stalagmite, stairs     */    private void roomDescription(){        if (currentRoom.equals("entrance")){            System.out.println("This is the opening atrium of the cave. You see openings on your left, forward, and right.");        }        else if (currentRoom.equals("garden")) {            System.out.println("The magical sea garden is full of delicious food. Try some.");        }    }    /**     *      */    private void drop(Command command){        if(!command.hasSecondWord()){            System.out.println("drop what?");            return;        }        String object = command.getSecondWord();        if (playerItems==null || playerItems.length()==0){            System.out.println("You have no items to drop.");        }        else if (playerItems.indexOf(object) == -1){            System.out.println("There is no " + object + " for you to drop.");        }        else{            roomItems += " " +             playerItems.substring(playerItems.indexOf(object), playerItems.indexOf(object) + object.length() + 2);            purseWeight -= Integer.parseInt(playerItems.substring(playerItems.indexOf(object) + object.length()+1, playerItems.indexOf(object) + object.length()+2));            //currentRoom.updateRoomItems(roomItems);            playerItems = playerItems.substring(0, playerItems.indexOf(object)) + playerItems.substring(playerItems.indexOf(object)+object.length()+2);            System.out.println("You have dropped the " + object + ".");        }    }    /**     * This is a back command to return to the previous room.     */    private void back(Command command)    {        if (command.hasSecondWord()){            System.out.println("Back is a one word command.");        }        else if (previousRoom!=null){            currentRoom = previousRoom;            currentRoom.getItems();            roomDescription();        }        else{            System.out.println("There is no back.");        }    }    /**     *      */    private void getPurse()    {        System.out.println("Your purse has a weight of " + purseWeight + ".");        if (playerItems.length()>1)        {            playerItems.trim();            System.out.println("You have: " + playerItems);        }    }}