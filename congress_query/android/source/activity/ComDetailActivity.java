package csci571.zhiqinliao.hw9.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.db.DatabaseService;
import csci571.zhiqinliao.hw9.pojo.Committee;
import csci571.zhiqinliao.hw9.util.Util;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class ComDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private JsonObject comInfo;
    private JsonParser parser;

    private String comID;
    private String comNameTxt;


    private ImageButton favBtn;
    private TextView comIDView;
    private TextView comName;
    private ImageView chamberImg;
    private TextView chamber;
    private TextView parentCom;
    private TextView contact;
    private TextView office;

    private DatabaseService dbService;
    private boolean isFavd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_detail);

        dbService = new DatabaseService(this);

        parser = new JsonParser();
        comInfo = parser.parse(getIntent().getStringExtra("comInfo")).getAsJsonObject();

        initToolBar();
        initView();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("Committee Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavd && !dbService.whetherCommitteeLiked(comID)) {
                    dbService.addNewCommittee(new Committee(comID, comNameTxt, comInfo.toString()));
            } else if(!isFavd && dbService.whetherCommitteeLiked(comID)) {
                    dbService.removeFavCommittee(comID);
                }
                finish();
            }
        });
    }

    private void initView() {
        favBtn = (ImageButton) findViewById(R.id.fav_btn);
        comIDView = (TextView) findViewById(R.id.com_id);
        comName = (TextView) findViewById(R.id.com_name);
        chamberImg = (ImageView) findViewById(R.id.chamber_img);
        chamber = (TextView) findViewById(R.id.chamber);
        parentCom = (TextView) findViewById(R.id.parent_committee);
        contact = (TextView) findViewById(R.id.contact);
        office = (TextView) findViewById(R.id.office);

        comID = comInfo.get("committee_id").getAsString();
        comNameTxt = comInfo.get("name").getAsString();

        isFavd = dbService.whetherCommitteeLiked(comID);

        String chamberTxt = comInfo.get("chamber").getAsString();
        String parentComTxt = (comInfo.has("parent_committee_id"))? comInfo.get("parent_committee_id").getAsString() : "N.A";
        String contactTxt = (comInfo.has("phone"))? comInfo.get("phone").getAsString() : "N.A";
        String officeTxt = (comInfo.has("office"))? comInfo.get("office").getAsString() : "N.A";

        if(isFavd)
            favBtn.setBackgroundResource(R.drawable.ic_favd_star);

        if(chamberTxt.equals("house"))
            chamberImg.setBackgroundResource(R.drawable.h);
        else
            chamberImg.setBackgroundResource(R.drawable.s);

        comIDView.setText(comID.toUpperCase());
        comName.setText(comNameTxt);
        chamber.setText(Util.firstCharUpper(chamberTxt));
        parentCom.setText(parentComTxt);
        contact.setText(contactTxt);
        office.setText(officeTxt);

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
