package zorklike;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Connection {
    private String side;
    private String room;
    private boolean open;
    private List<String> requirements;
    public Connection(String sd, String rm, boolean op, String... req) {
        side=sd;
        room=rm;
        open=op;
        requirements= new ArrayList<String>(Arrays.asList(req));
    }
    public Connection(String sd, String rm, boolean op) {
        side=sd;
        room=rm;
        open=op;
        requirements=null;
    }
    public Connection() {
        side=null;
        room=null;
        open=false;
        requirements=null;
    }
    public String getSide() {
        return side;
    }
    public String getName() {
        return room;
    }
    public boolean isOpen() {
        return open;
    }
    public List<String> getRequirements() {
        return requirements;
    }
    public boolean useItem(String item) {
        if (requirements==null) {
            return false;
        }
        Iterator<String> iterate1 = requirements.iterator();
       while (iterate1.hasNext()) {
            String nextReq = iterate1.next();
            String require = nextReq;
            if (nextReq==null) {
                return false;
            }
            else if (nextReq.equalsIgnoreCase(item)) {
                iterate1.remove();
                Iterator<Item> iterate = Zorklike.inventory.iterator();
                while (iterate.hasNext()) {
                    Item currentItem = iterate.next();
                    if (currentItem.getName().equalsIgnoreCase(item)) {
                        iterate.remove();
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }
    public boolean openClose(boolean forceOpenClose) {
        if (!forceOpenClose) {
            for (int i=0;i<requirements.size();i++) {
                if (requirements.get(i) != null) {
                    return false;
                }
                else {
                    if (open) {
                        open=false;
                        return true;
                    }
                    else {
                        open=true;
                        return true;
                    }
                }
            }
        } 
        else {
            if (open) {
                open=false;
                return true;
            }
            else {
                open=true;
                return true;
            }
        }
        return false;
    }
}