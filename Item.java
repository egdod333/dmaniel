package zorklike;

import zorklike.Zorklike;

public class Item {
    enum En=Zorklike.Type;
    private String name;
    private String desc;
    private int bdmg;
    private String file;
    public Item(Zorklike.Type type, String nm, String dc, int dg, String fl) {
        name=nm;
        desc=dc;
        if (type == En.KEY) {

        }
    }
}