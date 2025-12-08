package zorklike;

import zorklike.Zorklike;
import zorklike.Room;
import zorklike.Item;

public class Dictionary {
    private String[] roomNames;
    private String[] itemNames;
    public static String[] actions = {"list","go","move","find","search","look","examine","take","grab","get","open","close","drop","foreward","front","forewards","right","left","back","backward","backwards","inventory","backpack","around","use","xyzzy"};
    public static String[] useless = {"to","an","a","me","my","i","your","you","the","mine","at"};
    public static String[] splitters = {"with"};
    public static String[] flags = {"and",",","all"};
	public static String[] movement = {"go","move","foreward","front","forewards","right","left","back","backward","backwards"};
    public static String[] directions = {"foreward","forewards","front","right","left","backward","backwards","back"};
	public static String[] searching = {"find","search","look","examine"};
	public static String[] obtaining = {"take","grab","get"};
	public static String[] objectInteraction = {"open","close","search"};
	public static String[] inventoryActions = {"drop","inventory"};
    public Dictionary() {
        roomNames = new String[Zorklike.rooms.size()];
        int max = 0;
        for (Room room : Zorklike.rooms) {
            Item[] iteml = room.getItemL();
            if (iteml!=null) {
                max += iteml.length;
            }
        }
        itemNames = new String[max];
        for (int i=0;i<Zorklike.rooms.size();i++) {
            roomNames[i] = Zorklike.rooms.get(i).getName();
        }
        int x = 0;
        for (Room room : Zorklike.rooms) {
            Item[] iteml = room.getItemL();
            if (iteml!=null) {
                for (int i=0;i<iteml.length;i++) {
                    itemNames[x] = iteml[i].getName();
                    x++;
                }
            }
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
            if (itemName.equals(itemNames[i])) {
                return true;
            }
        }
        return false;
    }
    public String[] getItemNames() {
        return itemNames;
    }
    public String[] getRoomNames() {
        return roomNames;
    }
}
