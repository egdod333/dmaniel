package zorklike;

//make open command open things, make locked furniture not tell you whats in them, and get locked rooms working properly
//search functions dont work for single words in furniture names (i.e. wooden table returns wooden table but table returns null) whatever man fuck this ion wanan fix that 
//update: it is now fixed :D. only issue is uhhhhhhhh well the parser recognizes the words but the commands dont :/... still progress tho!!!!

//import statements
import zorklike.Room;
import zorklike.Dictionary;
import zorklike.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Zorklike {
	//init variables
	static Scanner scan;
	static Dictionary dictionary;
	static enum Type {
        KEY,
        CD,
        WEAPON,
		RANDOM
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
	// custom functions
	public static boolean containsExactWord(String mainStr, String word) {
		// \\b is a regex word boundary
		// Pattern.CASE_INSENSITIVE makes it not case sensitive
		Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(mainStr);
		return matcher.find();
	}

	// entry plug
	public static void main(String[] args) {
		//declare init variables
		boolean run = true;
		inventory = new ArrayList<Item>();
		rooms = new ArrayList<Room>();
		commandHashMap = new HashMap<String, Command>();
		scan = new Scanner(System.in);
		//create rooms

		//testroom (requires key and axe from all connections)
		rooms.add(new Room("testroom","test room","its testroom oh yeah",new Connection("front","testroom2",false,"key"),new Connection("right","testroom3",true)));
		//testroom items
		rooms.get(0).addFurniture(new Furniture("table","wooden table", "A wooden table stands in the corner",false,true,new Item(Type.KEY,"key","simple key","A small silver key. It feels cold to the touch.",true,0,0,null),new Item(Type.WEAPON,"axe","fireaxe","A hefty red and yellow axe similar to those that the local fire department uses. How did this end up here?",true,10,100,null)));
		// in front of you, testroom2, to your right, testroom3

		//testroom2
		rooms.add(new Room("testroom2","testing room travel","yuhhhh",new Connection("back","testroom",true,"key","axe")));
		//testroom2 items
		rooms.get(1).addFurniture(new Furniture("metal chest","a metal chest","A locked iron chest sits in the center of the room",true,false,new Item(Type.RANDOM,"lint","ball of lint","A ball of lint",false,0,0,null)).addRequirements("gray"));
		
		// behind you, testroom

		//testroom3
		rooms.add(new Room("testroom3","testing room travel","yuh its testroom3", new Connection("left","testroom",true)));
		rooms.get(2).addFurniture(new Furniture("countertop","a marble countertop","the marble glistens",false,true,new Item(Type.CD,"flash drive","small red 16gb flash drive","A small red flash drive, labeled \"Daravi 16GB\". A ring attached at the back of the drive lets you push the port outwards and pull it inwards.",true,0,0,"123.cmd"),new Item(Type.KEY,"gray","the color gray","its literally just gray",true,0,0,null)));

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
					String[] check = item.getDescription().split("");
					if (check[0].toLowerCase().equals("a") || check[0].toLowerCase().equals("e") || check[0].toLowerCase().equals("i") || check[0].toLowerCase().equals("o") || check[0].toLowerCase().equals("u")) {
						System.out.println(current + ": An " + item.getDescription() + ".");
					}
					else {
						System.out.println(current + ": A " + item.getDescription() + ".");
					}
					current++;
				}
				return 0;
			}
		};
		commandHashMap.put("inventory",checkInventory);
		commandHashMap.put("backpack",checkInventory);
		
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
			curRoom[0].getInfo(null);
			return 0;
		};
		commandHashMap.put("around",lookAround);

		//movement using go or move
		Command moveRooms = (String action, String object, String target) -> {
			if (target!=null) {
				Connection[] connectionList = curRoom[0].getConnections();
				for (Connection connect : connectionList) {
					// multi word target gets here then just doesnt print anything.... why???
					if (containsExactWord(target,connect.getName())) {
						if (connect.isOpen()) {
							for (Room room : rooms) {
								if (containsExactWord(target,room.getName())) {
									curRoom[0] = room;
									System.out.print("You are now in " + room.getName() + ". " + room.getDescription() + ".\n");
									curRoom[0].getInfo(null);
									return 0;
								}
							}
						}
						else {
							System.out.println("That door is locked. Sorry!");
							return 0;
						}
					}
					else if (target==null) {
						System.out.println("...Where?");
						return 0;
					}
				}
			}
			System.out.println("This room you speak of... uh... it doesn't exist.");
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
																											if (true^false) {
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
																				} else if(true){}{}{}{}{};;;;;;;
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
					if (connect.isOpen()) {
						String name = connect.getName();
						String[] upper = name.split("");
						upper[0] = upper[0].toUpperCase();
						String fixed = String.join("",upper);
						for (Room room : rooms) {
							if (containsExactWord(name,room.getName())) {
								curRoom[0] = room;
								System.out.print("You are now in " + fixed + ". " + room.getDescription() + ".\n");
								curRoom[0].getInfo(null);
								return 0;
							}
						}
					}
					else {
						System.out.println("That door is locked. Sorry!");
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
			List<Furniture> furnl = curRoom[0].getFurnL();
			Iterator<Furniture> iterate = furnl.iterator();
			while (iterate.hasNext()) {
				Furniture curfurn = iterate.next();
				List<Item> iteml = curfurn.getItemL();
				List<String> requirements = curfurn.getRequirements();
				Iterator<Item> iterate2 = iteml.iterator();
				if (requirements==null) {
					while (iterate2.hasNext()) {
						Item item = iterate2.next();
						if (containsExactWord(object,item.getName()) || containsExactWord(target,item.getName())) {
							inventory.add(item);
							iterate2.remove();
							System.out.println("You grab the " + item.getName() + " and put it into your backpack.");
							return 0;
						}
					}
				}
				else {
					System.out.println("It's locked. No can do, bucakroo.");
				}
			}
			System.out.println("You can't pick up that.");
			return 0;
		};
		for (String cmd : Dictionary.obtaining) {
			commandHashMap.put(cmd,grabItem);
		}

		//examine an object/item
		Command examine = (String action, String object, String target) -> {
			if (!(target==null && object==null)) {
				if (target==null) {
					boolean none = false;
					for (Item item : inventory) {
						if (containsExactWord(object,item.getName())) {
							System.out.println(item.getExtendedDescription());
							none = true;
						}
					}
					if (none) {
						System.out.println("That item isn't in your inventory... Sorry!");
					}
				}
				else if (target!=null) {
					boolean checkRooms = dictionary.searchRooms(target.toLowerCase());
					boolean roomSuccess = false;
					boolean checkFurniture = dictionary.searchFurniture(target.toLowerCase());
					boolean furnSuccess = false;
					if (checkRooms) {
						for (Room room : rooms) {
							if (containsExactWord(target,room.getName())) {
								room.getInfo(curRoom[0].getConnections());
								roomSuccess = true;
							}
						}
					}
					else if (checkFurniture) {
						List<Furniture> furnl = new ArrayList<Furniture>();
						for (Room room : rooms) {
							List<Furniture> rfurnl = new ArrayList<Furniture>(room.getFurnL());
							for (Furniture furn : rfurnl) {
								furnl.add(furn);
							}
						}
						for (Furniture furn : furnl) {
							if (containsExactWord(target,furn.getName())) {
								for (Furniture furnr : curRoom[0].getFurnL()) {
									if (containsExactWord(furn.getName(),furnr.getName())) {
										System.out.println("You examine the " + furn.getName());
										System.out.println("   " + furn.getExtendedDescription());
										List<Item> citeml = furn.getItemL();
										if (furn.isOpen()) {
											if (citeml!=null) {
												List<String> tempNameStorage = new ArrayList<String>();
												for (Item it : citeml) {
													String itnm = it.getName();
													List<String> aOrAn = new ArrayList<String>(Arrays.asList(itnm.split("")));
													if (aOrAn.get(0).toLowerCase().equals("a")||aOrAn.get(0).toLowerCase().equals("e")||aOrAn.get(0).toLowerCase().equals("i")||aOrAn.get(0).toLowerCase().equals("o")||aOrAn.get(0).toLowerCase().equals("u")) {
                            							tempNameStorage.add("an " + itnm);
                        							}
                        							else {
                            							tempNameStorage.add("a " + itnm);
                        							}
												}
												tempNameStorage.set(tempNameStorage.size()-1,"and " + (tempNameStorage.get(tempNameStorage.size()-1)));
												if (tempNameStorage.size()>2) {
													if (furn.isContainer()) {
														System.out.println("   Inside, there is " + String.join(", ",tempNameStorage) + ".");
													}
													else {
														System.out.println("   On top, there is " + String.join(", ",tempNameStorage) + ".");
													}
												}
												else {
													if (furn.isContainer()) {
														System.out.println("   Inside, there is " + String.join(" ",tempNameStorage) + ".");
													}
													else {
														System.out.println("   On top, there is " + String.join(" ",tempNameStorage) + ".");
													}
												}
											}
										}
										else {
											System.out.println("   You try to peer inside, but it is closed.");
										}
										furnSuccess = true;
									}
								}
							}
						}
					}
					if (checkRooms && !roomSuccess) {
						System.out.println("Unfortunately, that room seems to not exist.");
					}
					else if (checkFurniture && !furnSuccess) {
						System.out.println("There is no " + target + " in this room.");
					}
				}
			}
			else {
				System.out.println("No clue what you're trying to examine, sorry.");
			}
			return 0;
		};
		commandHashMap.put("examine",examine);
		commandHashMap.put("look",examine);
		commandHashMap.put("peer",examine);
		
		//find command
		Command find = (String action, String object, String target) -> {
			if (object==null && target==null) {
				System.out.println("Find... what, exactly?");
			}
			else {
				System.out.println("I already told you where that is, idiot.");
			}
			return 0;
		};
		commandHashMap.put("find",find);

		//open/unlocking
		Command open = (String action, String object, String target) -> {

			return 0;
		};

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
			// parser logic
			ArrayList<String> tokenized = new ArrayList<String>(Arrays.asList(input.split(" ")));
			// delete action and all words before action after setting action variable
			int index = -1;
			for (int i=0;i<tokenized.size();i++) {
				String token = tokenized.get(i);
				for (String verb : Dictionary.actions) {
					if (token.equalsIgnoreCase(verb)) {
						action = token.toLowerCase();
						index = i;
						break;
					}
				}
				// will loop until it finds an action verb
				if (index!=-1) break;
			}
			if (action!=null) {
				if (index==-1) {
					// literally isnt possible
					System.out.println("yo how the fuck");
				}
				else {
					// deletes all words before action word and the action word from the tokenized arraylist
					tokenized.subList(0,index+1).clear();
				}
				// remove unneccessary words (highkey forgot how this code works idk man)
				tokenized.removeIf(token ->
					Arrays.stream(Dictionary.useless)
						.anyMatch(u -> u.equalsIgnoreCase(token))
				);
				boolean objAndTarg = false;
				// if there is a splitter word, that means there is an object and a target in the sentence
				for (String item : tokenized) {
					for (String compare : Dictionary.splitters) {
						if (item.equalsIgnoreCase(compare)) {
							objAndTarg = true;
						}
					}
				}
				// if there is an object and a target
				if (objAndTarg) {
					ArrayList<String> targetList = new ArrayList<String>();
					ArrayList<String> objectList= new ArrayList<String>();
					boolean targl = true;

					for (String item : tokenized) {
						if (Arrays.asList(Dictionary.splitters).contains(item.toLowerCase())) {
							targl=false;
							continue;
						}
						if (targl) {
							targetList.add(item);
						}
						else {
							objectList.add(item);
						}
					}
					String targetListString = String.join(" ",targetList);
					String objectListString = String.join(" ",objectList);
					if (targetListString!=""){
						target=targetListString.trim();
					}
					else {
						target=null;
					}
					if (objectListString!="") {
						object = objectListString.trim();
					}
					else {
						object=null;
					}
				}
				// if there is only an object or a target
				else {
					ArrayList<String> tokenList = new ArrayList<String>();
					for (String token : tokenized) {
						tokenList.add(token);
					}
					String token = String.join(" ",tokenList);
					System.out.println("token: " + token);
					boolean checkRooms = dictionary.searchRooms(token.toLowerCase());
					boolean checkItems = dictionary.searchItems(token.toLowerCase());
					boolean checkFurniture = dictionary.searchFurniture(token.toLowerCase());
					System.out.println("checkRooms: " + checkRooms);
					System.out.println("checkItems: " + checkItems);
					System.out.println("checkFurniture: " + checkFurniture);
					System.out.println("objAndTarg: " + objAndTarg);

					if (checkRooms) {
						target = token.toLowerCase();
					}
					else if (checkItems) {
						object = token.toLowerCase();
					}
					else if (checkFurniture) {
						target = token.toLowerCase();
					}
					else if (token.equalsIgnoreCase("around")) {
						action = "around";
					}
					else if (token.equalsIgnoreCase("foreward") || token.equalsIgnoreCase("front") || token.equalsIgnoreCase("forewards")) {
						action = "front";
					}
					else if (token.equalsIgnoreCase("backward") || token.equalsIgnoreCase("back") || token.equalsIgnoreCase("backwards")) {
						action = "back";
					}
					else if (token.equalsIgnoreCase("left")) {
						action = "left";
					}
					else if (token.equalsIgnoreCase("right")) {
						action = "right";
					}
					else if (token.equalsIgnoreCase("inventory")) {
						action = "inventory";
					}
					else if (token.equalsIgnoreCase("backpack")) {
						action = "inventory";
					}
					else if (token.equalsIgnoreCase("door")) {
						target = "door";
					}
				}

				//response
				// debug
				System.out.println("action: " + action);
				System.out.println("target: " + target);
				System.out.println("object: " + object);
				//command
				commandHashMap.get(action).command(action,object,target);
			}
			else {
				System.out.println("Sorry, not quite sure what \"" + input + "\" means. Try again?");
			}
		}
	}
}
