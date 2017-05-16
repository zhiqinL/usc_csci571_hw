package csci571.zhiqinliao.hw9.pojo;

import java.util.List;

/**
 * Created by MeteorShower on 16/11/2016.
 */

public class Legislator {
    private String legID;
    private String lastName;
    private String legCont;

    public Legislator(String legID,String lastName ,String legCont) {
        this.legID = legID;
        this.lastName = lastName;
        this.legCont = legCont;
    }

    public String getLegID() {
        return legID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLegCont() {
        return legCont;
    }

}
