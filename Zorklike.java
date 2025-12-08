package zorklike;

//import statements
import zorklike.Room;
import zorklike.Dictionary;
import zorklike.Connection;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.HashMap;

public class Zorklike {
	//init variables
	static Scanner scan;
	static Dictionary dictionary;
	static enum Type {
        KEY,
        CD,
        WEAPON
    };
	public static List<Item> inventory;
	public static List<Room> rooms;
	@FunctionalInterface
	public static interface Command {
		int command(String action, String object, String target);
	}
	public static HashMap<String, Command> commandHashMap;
	public static final String redBackground = "\033[41m";
	public static final String resetFormatting = "\033[0m";
	public static final String blueColor = "\u001B[34m";
	public static final String boldBlueColor = "\033[1;34m";
	public static final String boldRedColor = "\033[1;31m";
	public static final String purpleColor = "\u001B[35m";
	public static final String greenColor = "\u001B[32m";
	public static final String greenBackground = "\u001B[42m";
	public static final String yellowColor = "\u001B[33m";
	public static final String yellowBackground = "\u001B[43m";
	public static final String cyanColor = "\u001B[36m";
	public static final String blueBackground = "\u001B[44m";
	public static final String blackColor = "\u001B[30m";
	public static final String brightYellowColor = "\033[0;93m";
	public static final String cyanBackground = "\033[46m";
	public static final String boldWhiteColor = "\033[1;37m";
	public static final String italics = "\033[3m";
	public static final String clear = "\033[H\033[2J";
	public static void main(String[] args) {
		//declare init variables
		boolean run = true;
		inventory = new ArrayList<Item>();
		rooms = new ArrayList<Room>();
		commandHashMap = new HashMap<String, Command>();
		scan = new Scanner(System.in);
		//create rooms

		//testroom (requires key and axe from all connections)
		rooms.add(new Room("testroom","test room","its testroom oh yeah",new Connection("front","testroom2",true),new Connection("right","testroom3",true)));
		//testroom items
		rooms.get(0).addItems(new Item(Type.KEY,"key","A simple key.",true,0,0,null),new Item(Type.WEAPON,"axe","A fireaxe.",true,10,100,null));
		// in front of you, testroom2, to your right, testroom3

		//testroom2
		rooms.add(new Room("testroom2","testing room travel","yuhhhh",new Connection("back","testroom",true,"key","axe")));
		//testroom2 items
		rooms.get(1).addItems();
		// behind you, testroom

		//testroom3
		rooms.add(new Room("testroom3","testing room travel","yuh its testroom3", new Connection("left","testroom",true)));
		rooms.get(2).addItems(new Item(Type.CD,"flash drive","A small red 16gb flash drive",true,0,0,"123.cmd"));

		//current room variable (for travel)
		Room[] curRoom = {rooms.get(0)};

		//init dictionary
		dictionary = new Dictionary();
		List<String> movementL = Arrays.asList(Dictionary.directions);
		
		//command and populate hashmap
		//inventory
		Command checkInventory = (String aciton, String target, String object) -> {
			if (inventory.size()==0) {
				System.out.println("Peeking into your backpack, you find nothing.");
				return 0;
			}
			else {
				System.out.println("You have a total of " + inventory.size() + " items in your backpack.");
				int current = 1;
				for (Item item : inventory) {
					String[] check = item.getName().split("");
					if (check[0].toLowerCase().equals("a") || check[0].toLowerCase().equals("e") || check[0].toLowerCase().equals("i") || check[0].toLowerCase().equals("o") || check[0].toLowerCase().equals("u")) {
						System.out.println(current + ": An " + item.getName());
					}
					else {
						System.out.println(current + ": A " + item.getName());
					}
					current++;
				}
				return 0;
			}
		};
		commandHashMap.put("inventory",checkInventory);
		
		//check items
		Command checkItemList = (String action, String target, String object) -> {
			for (String itemName : dictionary.getItemNames()) {
				System.out.println(itemName);
			}
			return 0;
		};
		commandHashMap.put("list",checkItemList);

		//look around
		Command lookAround = (String action, String object, String target) -> {
			String[] name = curRoom[0].getName().split("");
			name[0]=name[0].toUpperCase();
			String nameCap = String.join("",name);
			System.out.println("You look around the " + nameCap + " and see:");
			Item[] curItems = curRoom[0].getItemL();
			Connection[] curConnects = curRoom[0].getConnections();
			if (curItems==null) {
				System.out.println("Nothing...");
			}
			else {
				for (Item item : curItems) {
					String[] check = item.getName().split("");
					if (check[0].equalsIgnoreCase("a") || check[0].equalsIgnoreCase("e") || check[0].equalsIgnoreCase("i") || check[0].equalsIgnoreCase("o") || check[0].equalsIgnoreCase("u"))  {
						System.out.println("An " + item.getName());
					}
					else {
						System.out.println("A " + item.getName());
					}
				}
			}
			if (curConnects.length==0){
				System.out.println("Oddly, the room you are in is connected to " + italics + "no other room..." + resetFormatting + " Strange. How did you even get here??");
			}
			else {
				List<String> listOfConnections = new ArrayList<String>();
				for (Connection connect : curConnects) {
					String side = connect.getSide();
					if (side.equals("front")) {
						listOfConnections.add("In front of you is the " + connect.getName() + ". ");
					}
					else if (side.equals("back")) {
						listOfConnections.add("Behind you is the " + connect.getName() + ". ");
					}
					else if (side.equals("left")) {
						listOfConnections.add("To your left is the " + connect.getName() + ". ");
					}
					else if (side.equals("right")) {
						listOfConnections.add("To your right is the " + connect.getName() + ". ");
					}
				}
				String fin = String.join("",listOfConnections);
				System.out.println(fin);
				listOfConnections.clear();
			}
			return 0;
		};
		commandHashMap.put("around",lookAround);

		//movement using go or move
		Command moveRooms = (String action, String object, String target) -> {
			Connection[] connectionList = curRoom[0].getConnections();
			for (Connection connect : connectionList) {
				if (connect.getName().equalsIgnoreCase(target)) {
					for (Room room : rooms) {
						if (room.getName().equalsIgnoreCase(target)) {
							curRoom[0] = room;
							System.out.print("You are now in " + room.getName() + ". " + room.getDescription() + ". ");
							List<String> listOfConnections = new ArrayList<String>();
							for (Connection connect2 : room.getConnections()) {
								String side = connect2.getSide();
								if (side.equals("front")) {
									listOfConnections.add("In front of you is the " + connect2.getName() + ". ");
								}
								else if (side.equals("back")) {
									listOfConnections.add("Behind you is the " + connect2.getName() + ". ");
								}
								else if (side.equals("left")) {
									listOfConnections.add("To your left is the " + connect2.getName() + ". ");
								}
								else if (side.equals("right")) {
									listOfConnections.add("To your right is the " + connect2.getName() + ". ");
								}
							}
							String fin = String.join("",listOfConnections);
							System.out.println(fin);
							listOfConnections.clear();
							return 0;
						}
					}
				}
				else if (target==null) {
					System.out.println("...Where?");
					return 0;
				}
			}
			return 0;
		};
		commandHashMap.put("go",moveRooms);
		commandHashMap.put("move",moveRooms);
		//movement using directions
		Command moveDirection = (String action, String object, String target) -> {
			String act = action;
			if (true) {
				if (true) {
					if (true) {
						if (true) {
							if (true) {
								if (true) {
									if (true) {
										if (true) {
											if (true) {
												if (true) {
													if (true) {
														if (true) {
															if (true) {
																if (true&false&true|true&true^false!=true&!false^true){
																	if (true) {
																		if (true) {
																			if (true) {
																				if (true) {
																					if (true){
																						if (true) {
																							if (true) {
																								if (!!true&&true!=!true) {
																									if (true) {
																										if (true) {
																											if (true) {
																												if (true) {
																													System.out.print("");
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (act.equals("foreward") || act.equals("forewards")) {
				act = "front";
			}
			else if (act.equals("backwards") || act.equals("backward")) {
				act = "back";
			}
			for (Connection connect : curRoom[0].getConnections()) {
				if (connect.getSide().equals(act)) {
					String name = connect.getName();
					for (Room room : rooms) {
						if (room.getName().equalsIgnoreCase(name)) {
							curRoom[0] = room;
							System.out.print("You are now in " + name + ". " + room.getDescription() + ". ");
							List<String> listOfConnections = new ArrayList<String>();
							for (Connection connect2 : room.getConnections()) {
								String side = connect2.getSide();
								if (side.equals("front")) {
									listOfConnections.add("In front of you is the " + connect2.getName() + ". ");
								}
								else if (side.equals("back")) {
									listOfConnections.add("Behind you is the " + connect2.getName() + ". ");
								}
								else if (side.equals("left")) {
									listOfConnections.add("To your left is the " + connect2.getName() + ". ");
								}
								else if (side.equals("right")) {
									listOfConnections.add("To your right is the " + connect2.getName() + ". ");
								}
							}
							String fin = String.join("",listOfConnections);
							System.out.println(fin);
							listOfConnections.clear();
							return 0;
						}
					}
				}
				else {
					System.out.println("You can't walk through the wall...");
				}
			}

			return 0;
		};
		for (String command : Dictionary.directions) {
			commandHashMap.put(command,moveDirection);
		}

		//grabbing items
		Command grabItem = (String action, String object, String target) -> {
			return 0;
		};
		for (String cmd : Dictionary.obtaining) {
			commandHashMap.put(cmd,grabItem);
		}
		
		//game running
		while (run) {
			//input
			
			// action
			String action = null;

			// target (usually not items)
			String target = null;

			// object (will not be room; if action is go or forward or any movement verb, it will use target not object)
			String object = null;

			System.out.print("> ");
			String input = scan.nextLine();
			input = input.toLowerCase();
			// parser logic
			String[] stringify = input.split(" ");
			ArrayList<String> tokenized = new ArrayList<String>(Arrays.asList(stringify));
			// delete action and all words before action after setting action
			int index = -1;
			for (int i=0;i<tokenized.size();i++) {
				String token = tokenized.get(i);
				for (String verb : Dictionary.actions) {
					if (token.equalsIgnoreCase(verb)) {
						action = token;
						index = i;
						break;
					}
				}
				if (index!=-1) break;
			}
			if (action!=null) {
				// remove unneccessary words
				for (int i=0;i<tokenized.size();i++) {
					String token = tokenized.get(i);
					for (String item : Dictionary.useless) {
						if (token.equals(item)) {
							tokenized.remove(i);
						}
					}
				}

				if (index==-1) {
					System.out.println("PARSING ERROR: ERROR CODE: 0");
				}
				else {
					tokenized.subList(0,index+1).clear();
				}
				boolean objAndTarg = false;
				for (String item : tokenized) {
					for (String compare : Dictionary.splitters) {
						if (item.equals(compare)) {
							objAndTarg = true;
						}
					}
				}
				if (objAndTarg) {
					ArrayList<String> targetList = new ArrayList<String>();
					ArrayList<String> objectList= new ArrayList<String>();
					boolean targl = true;
					if (targl) {
						for (int i=0;i<tokenized.size();i++) {
							String item = tokenized.get(i);
							for (String compare : Dictionary.splitters) {
								if (!item.equals(compare)) {
									targetList.add(item);
									tokenized.remove(i);
								}
								else {
									targl = false;
									tokenized.remove(i);
								}
							}
						}
					}
					else {
						for (int i=0;i<tokenized.size();i++) {
							String item = tokenized.get(i);
							objectList.add(item);
							tokenized.remove(i);
						}
					}
					String targetListString = String.join(" ",targetList);
					String objectListString = String.join(" ",objectList);
					target = targetListString.trim();
					object = objectListString.trim();
				}
				else {
					ArrayList<String> tokenList = new ArrayList<String>();
					for (String token : tokenized) {
						tokenList.add(token);
					}
					String token = String.join(" ",tokenList);
					boolean checkRooms = dictionary.searchRooms(token.toLowerCase());
					boolean checkItems = dictionary.searchItems(token.toLowerCase());
					System.out.println("checkRooms: " + checkRooms);
					System.out.println("checkItems: " + checkItems);

					if (checkRooms) {
						target = token;
					}
					else if (checkItems) {
						object = token;
					}
					else if (token.equals("around")) {
						action = "around";
					}
					else if (token.equals("foreward") || token.equals("front") || token.equals("forewards")) {
						action = "front";
					}
					else if (token.equals("backward") || token.equals("back") || token.equals("backwards")) {
						action = "back";
					}
					else if (token.equals("left")) {
						action = "left";
					}
					else if (token.equals("right")) {
						action = "right";
					}
					else if (token.equals("inventory")) {
						action = "inventory";
					}
					else if (token.equals("backpack")) {
						action = "inventory";
					}
				}
				//response
				// debug
				System.out.println("action: " + action);
				System.out.println("target: " + target);
				System.out.println("object: " + object);
				commandHashMap.get(action).command(action,object,target);
			}
			else {
				System.out.println("Sorry, not quite sure what \"" + input + "\" means. Try again?");
			}
		}
	}
}
