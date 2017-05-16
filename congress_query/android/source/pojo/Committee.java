package csci571.zhiqinliao.hw9.pojo;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class Committee {
    private String comID;
    private String comName;
    private String comCont;
    public Committee(String comID, String comName, String comCont) {
        this.comID = comID;
        this.comName = comName;
        this.comCont = comCont;
    }

    public String getComID() {
        return comID;
    }

    public String getComName() {
        return comName;
    }

    public String getComCont() {
        return comCont;
    }
}
