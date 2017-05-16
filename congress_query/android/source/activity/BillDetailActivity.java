package csci571.zhiqinliao.hw9.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.db.DatabaseService;
import csci571.zhiqinliao.hw9.pojo.Bill;
import csci571.zhiqinliao.hw9.util.Util;

/**
 * Created by MeteorShower on 18/11/2016.
 */

public class BillDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private JsonObject billInfo;
    private JsonParser parser;

    private ImageButton favBtn;
    private TextView billIDView;
    private TextView title;
    private TextView type;
    private TextView sponsor;
    private TextView chamber;
    private TextView status;
    private TextView introduceOn;
    private TextView congressURL;
    private TextView versionStatus;
    private TextView billURL;

    private String billID;
    private String billIntroduceOn;

    private DatabaseService dbService;
    private boolean isFavd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);

        dbService = new DatabaseService(this);

        parser = new JsonParser();
        billInfo = parser.parse(getIntent().getStringExtra("billInfo")).getAsJsonObject();

        initToolBar();
        initView();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("Bill Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavd && !dbService.whetherBillLiked(billID)) {
                    dbService.addNewBill(new Bill(billID, billIntroduceOn, billInfo.toString()));
                } else if(!isFavd && dbService.whetherBillLiked(billID)) {
                    dbService.removeFavBill(billID);
                }
                finish();
            }
        });
    }

    private void initView() {
        favBtn = (ImageButton) findViewById(R.id.fav_btn);
        billIDView = (TextView) findViewById(R.id.bill_id);
        title = (TextView) findViewById(R.id.bill_title);
        type = (TextView) findViewById(R.id.bill_type);
        sponsor = (TextView) findViewById(R.id.bill_sponsor);
        chamber = (TextView) findViewById(R.id.bill_chamber);
        status = (TextView) findViewById(R.id.bill_status);
        introduceOn = (TextView) findViewById(R.id.introduce_on);
        congressURL = (TextView) findViewById(R.id.congress_url);
        versionStatus = (TextView) findViewById(R.id.version_status);
        billURL = (TextView) findViewById(R.id.bill_url);

        billID = billInfo.get("bill_id").getAsString();
        billIntroduceOn = billInfo.get("introduced_on").getAsString();

        isFavd = dbService.whetherBillLiked(billID);

        String titleTxt = billInfo.get("official_title").getAsString();
        String typeTxt = billInfo.get("bill_type").getAsString().toUpperCase();

        JsonObject sponsorObject = billInfo.get("sponsor").getAsJsonObject();
        String sponsorTxt = sponsorObject.get("title").getAsString() + ". " + sponsorObject.get("last_name").getAsString() + ", " + sponsorObject.get("first_name").getAsString();

        String chamberTxt = Util.firstCharUpper(billInfo.get("chamber").getAsString());
        String statusTxt = (billInfo.get("history").getAsJsonObject().get("active").getAsBoolean())? "Active" : "New";
        String introduceOnTxt = Util.formateDate(billIntroduceOn);
        String congressURLTxt = (billInfo.get("urls").getAsJsonObject().get("congress").isJsonNull())? "N.A": billInfo.get("urls").getAsJsonObject().get("congress").getAsString();
        String versionStatusTxt = billInfo.get("last_version").getAsJsonObject().get("version_name").getAsString();
        String billURLTxt = (billInfo.get("last_version").getAsJsonObject().get("urls").getAsJsonObject().get("pdf").isJsonNull())? "N.A" : billInfo.get("last_version").getAsJsonObject().get("urls").getAsJsonObject().get("pdf").getAsString();

        if(isFavd)
            favBtn.setBackgroundResource(R.drawable.ic_favd_star);

        billIDView.setText(billID.toUpperCase());
        title.setText(titleTxt);
        type.setText(typeTxt);
        sponsor.setText(sponsorTxt);
        chamber.setText(chamberTxt);
        status.setText(statusTxt);
        introduceOn.setText(introduceOnTxt);
        congressURL.setText(congressURLTxt);
        versionStatus.setText(versionStatusTxt);
        billURL.setText(billURLTxt);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavd = !isFavd;
                if(isFavd)
                    view.setBackgroundResource(R.drawable.ic_favd_star);
                else
                    view.setBackgroundResource(R.drawable.ic_fav_img_btn);
            }
        });
    }
}
