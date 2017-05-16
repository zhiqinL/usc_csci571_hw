package csci571.zhiqinliao.hw9.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.pojo.Legislator;
import csci571.zhiqinliao.hw9.util.ImgLruCache;
import csci571.zhiqinliao.hw9.util.WebService;
import csci571.zhiqinliao.hw9.widget.LegListArrayAdapter;

/**
 * Created by MeteorShower on 15/11/2016.
 */

public class LegFragment extends Fragment {

    private TabHost tabHost;
    private ListView listView;
    private ListView wordListView;
    private LinearLayout wordListViewContainer;

    private View legFragment;

    private List<String> alllegInfo;
    private List<String> allHouseLegInfo;
    private List<String> allSenateLegInfo;

    private List<Character> allLegWordList;
    private List<Character> houseLegWordList;
    private List<Character> senateLegWordList;
    private List<Integer> allLegWordPos;
    private List<Integer> houseLegWordPos;
    private List<Integer> senateLegWordPos;

    private List<Character> wordList;
    private List<Integer> wordPos;

    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private final int cacheSize = maxMemory / 5;

    private ImgLruCache imgLruCache;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        legFragment = inflater.inflate(R.layout.leg_fragment, container, false);
        imgLruCache = new ImgLruCache(cacheSize);
        initListView();
        initTab(inflater);
        return legFragment;
    }

    /**
     * init tab bar in legislator view
     * @param inflater
     */
    private void initTab(LayoutInflater inflater) {
        tabHost = (TabHost) legFragment.findViewById(R.id.tabhost);
        tabHost.setup();

        View stateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legStataTabText = (TextView) stateTab.findViewById(R.id.tab_label);
        legStataTabText.setText("BY STATES");

        View houseTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legHouseTabText = (TextView) houseTab.findViewById(R.id.tab_label);
        legHouseTabText.setText("HOUSE");

        View senateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legSenateTabText = (TextView) senateTab.findViewById(R.id.tab_label);
        legSenateTabText.setText("SENATE");

        tabHost.addTab(tabHost.newTabSpec("state").setIndicator(stateTab).setContent(R.id.state_tab));
        tabHost.addTab(tabHost.newTabSpec("house").setIndicator(houseTab).setContent(R.id.house_tab));
        tabHost.addTab(tabHost.newTabSpec("senate").setIndicator(senateTab).setContent(R.id.senate_tab));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s == "state") {
                    if (alllegInfo == null)
                        new GetAllLegInfo("state").execute();
                    else {
                        wordList = allLegWordList;
                        wordPos = allLegWordPos;
                        listView.setAdapter(new LegListArrayAdapter(getActivity(), alllegInfo, imgLruCache));
                        wordListView.setAdapter(new ArrayAdapter<Character>(getActivity(), R.layout.word_list_item, wordList));
                    }
                } else if(s == "house") {
                    if (allHouseLegInfo == null)
                        new GetAllLegInfo("house").execute();
                    else {
                        wordList = houseLegWordList;
                        wordPos = houseLegWordPos;
                        listView.setAdapter(new LegListArrayAdapter(getActivity(), allHouseLegInfo, imgLruCache));
                        wordListView.setAdapter(new ArrayAdapter<Character>(getActivity(), R.layout.word_list_item, wordList));
                    }
                } else if(s == "senate") {
                    if (allSenateLegInfo == null)
                        new GetAllLegInfo("senate").execute();
                    else {
                        wordList = senateLegWordList;
                        wordPos = senateLegWordPos;
                        listView.setAdapter(new LegListArrayAdapter(getActivity(), allSenateLegInfo, imgLruCache));
                        wordListView.setAdapter(new ArrayAdapter<Character>(getActivity(), R.layout.word_list_item, wordList));
                    }
                }
            }
        });
    }

    /**
     * init basic legislator list view
     */
    private void initListView() {
        listView = (ListView) legFragment.findViewById(R.id.list_view);
        listView.setEmptyView(legFragment.findViewById(R.id.emptyElement));

        new GetAllLegInfo("state").execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = (String) listView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.setClass(getActivity(), LegislatorDetailActivity.class);
                intent.putExtra("legInfo", selectedValue);
                startActivity(intent);
            }
        });
    }

    private void initWordListView() {
        wordListView = (ListView) legFragment.findViewById(R.id.word_list);
        wordListViewContainer = (LinearLayout) legFragment.findViewById(R.id.word_list_container);
        wordListView.setDivider(null);
        wordListView.setAdapter(new ArrayAdapter<Character>(getActivity(), R.layout.word_list_item ,wordList));
        wordListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                int listHeight = view.getHeight();
                int position = (int) y / (listHeight / wordList.size());
                if(position >= wordList.size())
                    position = wordList.size() - 1;
                else if(position < 0)
                    position = 0;
                listView.setSelection(wordPos.get(position));
                return true;
            }
        });
        wordListViewContainer.setVisibility(View.VISIBLE);
    }

    /**
     * child thread request internet to get all legislators' information
     * include image, title.
     */
    class GetAllLegInfo extends AsyncTask<String, Integer, String> {
        private JsonArray allLegInfo;
        private List<String> legListItems;
        private List<Character> innerWordList;
        private List<Integer> innerWordPos;
        private String type;
        private String wordType;

        GetAllLegInfo(String type) {
            this.type = type;
            this.innerWordList = new ArrayList<>();
            this.innerWordPos = new ArrayList<>();
            if(type == "state")
                this.wordType = "state";
            else
                this.wordType = "last_name";
        }

        @Override
        protected String doInBackground(String... strings) {
            allLegInfo = WebService.getLegInfo(type);
            legListItems = setLegList(allLegInfo);

            if(type == "state") {
                alllegInfo = legListItems;
                allLegWordList = innerWordList;
                allLegWordPos = innerWordPos;
            } else if(type == "house") {
                allHouseLegInfo = legListItems;
                houseLegWordList = innerWordList;
                houseLegWordPos = innerWordPos;
            } else if(type == "senate") {
                allSenateLegInfo = legListItems;
                senateLegWordList = innerWordList;
                senateLegWordPos = innerWordPos;
            }
            wordList = innerWordList;
            wordPos = innerWordPos;
            return null;
        }

        private List<String> setLegList(JsonArray allLegInfo) {
            List<String> leglistItems = new ArrayList<String>();
            Iterator legListIt = allLegInfo.iterator();
            int i = 0;
            while(legListIt.hasNext()) {
                JsonElement legItem = (JsonElement) legListIt.next();
                constructWordList(legItem.getAsJsonObject(), i);
                leglistItems.add(legItem.toString());
                i++;
            }
            return leglistItems;
        }

        private void constructWordList(JsonObject curLeg, int position) {
            char wordFirstChar = curLeg.get(wordType).getAsString().charAt(0);
            if(innerWordList.indexOf(wordFirstChar) == -1) {
                innerWordList.add(wordFirstChar);
                innerWordPos.add(position);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listView.setAdapter(new LegListArrayAdapter(getActivity(), legListItems, imgLruCache));
            initWordListView();
        }
    }
}
