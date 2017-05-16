package csci571.zhiqinliao.hw9.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.db.DatabaseService;
import csci571.zhiqinliao.hw9.pojo.Legislator;
import csci571.zhiqinliao.hw9.util.MyProgressBar;
import csci571.zhiqinliao.hw9.util.Util;

/**
 * Created by MeteorShower on 17/11/2016.
 */

public class LegislatorDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private JsonObject legInfo;

    private String legID;
    private String lastName;

    private ImageButton favBtn;
    private ImageButton facebookBtn;
    private ImageButton twitterBtn;
    private ImageButton websiteBtn;
    private ImageView legImg;
    private ImageView partyImg;
    private TextView partyTxt;
    private TextView name;
    private TextView email;
    private TextView chamber;
    private TextView contact;
    private TextView startTerm;
    private TextView endTerm;
    private ProgressBar term;
    private TextView office;
    private TextView state;
    private TextView fax;
    private TextView birthday;

    private DatabaseService dbService;
    private boolean isFavd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leg_detail);

        dbService = new DatabaseService(this);

        JsonParser parser = new JsonParser();
        legInfo = parser.parse(getIntent().getStringExtra("legInfo")).getAsJsonObject();

        initView();
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("Legislator Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavd && !dbService.whetherLegLiked(legID)) {
                    dbService.addNewLegislator(new Legislator(legID, lastName, legInfo.toString()));
                } else if(!isFavd && dbService.whetherLegLiked(legID)) {
                    dbService.removeFavLeg(legID);
                }
                finish();
            }
        });
    }

    private void initView() {
        favBtn = (ImageButton) findViewById(R.id.fav_btn);
        facebookBtn = (ImageButton) findViewById(R.id.facebook_btn);
        twitterBtn = (ImageButton) findViewById(R.id.twitter_btn);
        websiteBtn = (ImageButton) findViewById(R.id.website_btn);
        legImg = (ImageView) findViewById(R.id.leg_img);
        partyImg = (ImageView) findViewById(R.id.party_img);
        partyTxt = (TextView) findViewById(R.id.party_txt);
        name = (TextView) findViewById(R.id.leg_name);
        email = (TextView) findViewById(R.id.leg_email);
        chamber = (TextView) findViewById(R.id.leg_chamber);
        contact = (TextView) findViewById(R.id.leg_contact);
        startTerm = (TextView) findViewById(R.id.start_term);
        endTerm = (TextView) findViewById(R.id.end_term);
        term = (ProgressBar) findViewById(R.id.term);
        office = (TextView) findViewById(R.id.leg_office);
        state = (TextView) findViewById(R.id.leg_state);
        fax = (TextView) findViewById(R.id.leg_fax);
        birthday = (TextView) findViewById(R.id.leg_birthday);

        legID = legInfo.get("bioguide_id").getAsString();
        lastName = legInfo.get("last_name").getAsString();

        isFavd = dbService.whetherLegLiked(legID);

        final String facebookURL = (legInfo.get("facebook_id").isJsonNull())? "" : "https://www.facebook.com/" + legInfo.get("facebook_id").getAsString();
        final String twitterURL = (legInfo.get("twitter_id").isJsonNull())? "" : "https://twitter.com/" + legInfo.get("twitter_id").getAsString();
        final String websiteURL = (legInfo.get("website").isJsonNull())? "" : legInfo.get("website").getAsString();

        String party = legInfo.get("party").getAsString();
        String nameTxt = legInfo.get("title").getAsString() + ". " + legInfo.get("last_name").getAsString() + ", " + legInfo.get("first_name").getAsString();
        String chamberTxt = legInfo.get("chamber").getAsString();
        chamberTxt = chamberTxt.substring(0,1).toUpperCase()+chamberTxt.substring(1);
        String startTermTxt = legInfo.get("term_start").getAsString();
        String endTermTxt = legInfo.get("term_end").getAsString();

        String faxTxt = (legInfo.get("fax").isJsonNull())?"N.A" : legInfo.get("fax").getAsString();

        new setLegImg(legImg, legID, Picasso.with(this)).execute();

        if(party.equals("R")) {
            partyTxt.setText("Republican");
            partyImg.setBackgroundResource(R.drawable.r);
        } else if(party.equals("D")) {
            partyTxt.setText("Democrat");
            partyImg.setBackgroundResource(R.drawable.d);
        }

        if(isFavd)
            favBtn.setBackgroundResource(R.drawable.ic_favd_star);

        name.setText(nameTxt);
        email.setText(legInfo.get("oc_email").getAsString());
        chamber.setText(chamberTxt);
        contact.setText(legInfo.get("phone").getAsString());
        startTerm.setText(Util.formateDate(startTermTxt));
        endTerm.setText(Util.formateDate(endTermTxt));
        term.setProgress(calculateProgress(startTermTxt, endTermTxt));
        office.setText(legInfo.get("office").getAsString());
        state.setText(legInfo.get("state").getAsString());
        fax.setText(faxTxt);
        birthday.setText(Util.formateDate(legInfo.get("birthday").getAsString()));


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

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(facebookURL != "") {
                    Uri uri = Uri.parse(facebookURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "This legislator does not have facebook account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(twitterURL != "") {
                    Uri uri = Uri.parse(twitterURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "This legislator does not have twitter account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(websiteURL != "") {
                    Uri uri = Uri.parse(websiteURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "This legislator does not have website", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int calculateProgress(String start, String end) {
        DateFormat oriFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date today = new Date();

        try {
            return (int) (((double)(today.getTime() - oriFormat.parse(start).getTime()) / (double)(oriFormat.parse(end).getTime() - oriFormat.parse(start).getTime())) * 100);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    class setLegImg extends AsyncTask<String, Integer, String> {
        private ImageView legImg;
        private String imgURL;
        private String imgBasicURL = "https://theunitedstates.io/images/congress/original/";
        private Picasso picasso;
        private RequestCreator rc;


        setLegImg(ImageView legImg, String legID, Picasso picasso) {
            this.legImg = legImg;
            this.imgURL = imgBasicURL + legID + ".jpg";
            this.picasso = picasso;
        }

        @Override
        protected String doInBackground(String... strings) {
            rc = picasso.load(imgURL);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            rc.resize(100, 120).into(legImg);
        }
    }
}
