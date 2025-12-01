package zorklike;

import zorklike.Zorklike;
import zorklike.Room;
import zorklike.Item;

public class Dictionary {
    private String[] roomNames;
    private String[] itemNames;
    public static String[] actions = {"list","go","move","find","search","look","examine","take","grab","get","open","close","drop","foreward","front","forewards","right","left","back","backward","backwards","inventory"};
    
	public static String[] movement = {"go","move","foreward","front","forewards","right","left","back","backward","backwards"};
    public static String[] directions = {"foreward","forewards","front","right","left","backward","backwards","back"};
	public static String[] searching = {"find","search","look","examine"};
	public static String[] obtaining = {"take","grab","get"};
	public static String[] objectInteraction = {"open","close","search"};
	public static String[] inventoryActions = {"drop","inventory"};
    public Dictionary() {
        roomNames = new String[Zorklike.rooms.size()];
        itemNames = new String[Zorklike.items.size()];
        for (int i=0;i<Zorklike.rooms.size();i++) {
            roomNames[i] = Zorklike.rooms.get(i).getName();
        }
        for (int i=0;i<Zorklike.items.size();i++) { 
			itemNames[i] = Zorklike.items.get(i).getName();
		} 
	} 
	public boolean searchRooms(String roomName) { 
		for (int i=0;i<roomNames.length;i++) { 
			if (roomNames[i].equals(roomName)) { 
				return true; 
			} else {}
        }
        return false;
    }
    public boolean searchItems(String itemName) {
        for (int i=0;i<itemNames.length;i++) {
            String[] save = itemNames[i].split(" ");
            int max = save.length-1;
            if (save[max].equals(itemName)) {
                return true;
            }
            else {}
        }
        return false;
    }
}
