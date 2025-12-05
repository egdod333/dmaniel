package zorklike;

import java.util.Iterator;

public class Connection {
    private String side;
    private String room;
    private boolean open;
    private String[] requirements;
    public Connection(String sd, String rm, boolean op, String... req) {
        side=sd;
        room=rm;
        open=op;
        requirements=req;
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
    public String[] getRequirements() {
        return requirements;
    }
    public boolean useItem(String item) {
        if (requirements==null) {
            return false;
        }
        for (int i=0;i<requirements.length;i++) {
            String require = requirements[i];
            if (requirements[i]==null) {
                return false;
            }
            else if (requirements[i].equalsIgnoreCase(item)) {
                requirements[i]=null;
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
            for (int i=0;i<requirements.length;i++) {
                if (requirements[i] != null) {
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