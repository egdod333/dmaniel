package zorklike;

import zorklike.Item;
import zorklike.Zorklike;

public class Room {
    private String name;
    private String desc;
    private String extdesc;
    private String[] require;
    private boolean open;
    public Room(String nm, String dc, String exdc, String[] rq, boolean op) {
        name=nm;
        desc=dc;
        extdesc=exdc;
        require=rq;
        open=op;
    }
    public Room() {
        name="null";
        desc="N/A";
        extdesc="N/A";
        require=null;
        open=false;
    }
}