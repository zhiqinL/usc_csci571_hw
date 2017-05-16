package csci571.zhiqinliao.hw9.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.util.Util;
import csci571.zhiqinliao.hw9.util.WebService;
import csci571.zhiqinliao.hw9.widget.ComListArrayAdapter;

/**
 * Created by MeteorShower on 15/11/2016.
 */

public class ComFragment extends Fragment {

    private TabHost tabHost;
    private View comFragment;
    private ListView listView;

    private List<String> houseComList;
    private List<String> senateComList;
    private List<String> jointComList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        comFragment = inflater.inflate(R.layout.com_fragment, container, false);
        initTab(inflater);
        initComListView();
        return comFragment;
    }

    private void initTab(LayoutInflater inflater) {
        tabHost = (TabHost) comFragment.findViewById(R.id.tabhost);
        tabHost.setup();

        View stateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legStataTabText = (TextView) stateTab.findViewById(R.id.tab_label);
        legStataTabText.setText("HOUSE");

        View houseTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legHouseTabText = (TextView) houseTab.findViewById(R.id.tab_label);
        legHouseTabText.setText("SENATE");

        View senateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legSenateTabText = (TextView) senateTab.findViewById(R.id.tab_label);
        legSenateTabText.setText("JOINT");

        tabHost.addTab(tabHost.newTabSpec("house").setIndicator(stateTab).setContent(R.id.house_tab));
        tabHost.addTab(tabHost.newTabSpec("senate").setIndicator(houseTab).setContent(R.id.senate_tab));
        tabHost.addTab(tabHost.newTabSpec("joint").setIndicator(senateTab).setContent(R.id.joint_tab));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s == "house") {
                    if (houseComList == null)
                        new GetComInfo("house").execute();
                    else {
                        listView.setAdapter(new ComListArrayAdapter(getActivity(), houseComList));
                    }
                } else if(s == "senate") {
                    if (senateComList == null)
                        new GetComInfo("senate").execute();
                    else {
                        listView.setAdapter(new ComListArrayAdapter(getActivity(), senateComList));
                    }
                } else if(s == "joint") {
                    if (jointComList == null)
                        new GetComInfo("joint").execute();
                    else
                        listView.setAdapter(new ComListArrayAdapter(getActivity(), jointComList));
                }
            }
        });
    }

    private void initComListView() {
        listView = (ListView) comFragment.findViewById(R.id.list_view);
        listView.setEmptyView(comFragment.findViewById(R.id.emptyElement));
        new GetComInfo("house").execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = (String) listView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.setClass(getActivity(), ComDetailActivity.class);
                intent.putExtra("comInfo", selectedValue);
                startActivity(intent);
            }
        });
    }

    class GetComInfo extends AsyncTask<String, Integer, String> {
        private String type;
        private JsonArray comInfo;
        private List<String> comListItems;

        GetComInfo(String type) {
            this.type = type;
        }

        @Override
        protected String doInBackground(String... strings) {
            comInfo = WebService.getComInfo(type);
            comListItems = setComList();
            if(type == "house")
                houseComList = comListItems;
            else if(type == "senate")
                senateComList = comListItems;
            else if(type == "joint")
                jointComList = comListItems;
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            listView.setAdapter(new ComListArrayAdapter(getActivity(), comListItems));
        }

        private List<String> setComList() {
            List<String> comItems = new ArrayList<>();
            Iterator comListIt = comInfo.iterator();
            while(comListIt.hasNext()) {
                JsonElement billItem = (JsonElement) comListIt.next();
                comItems.add(billItem.toString());
            }
            return comItems;
        }
    }
}
