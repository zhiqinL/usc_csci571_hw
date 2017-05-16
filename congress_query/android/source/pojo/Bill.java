package csci571.zhiqinliao.hw9.pojo;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class Bill {
    private String billID;
    private String billIntroduceOn;
    private String billCont;

    public Bill(String billID, String billIntroduceOn, String billCont) {
        this.billID = billID;
        this.billIntroduceOn = billIntroduceOn;
        this.billCont = billCont;
    }

    public String getBillID() {
        return billID;
    }

    public String getBillIntroduceOn() {
        return billIntroduceOn;
    }

    public String getBillCont() {
        return billCont;
    }
}
