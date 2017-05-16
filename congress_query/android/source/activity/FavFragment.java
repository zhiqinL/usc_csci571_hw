package csci571.zhiqinliao.hw9.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.db.DatabaseService;
import csci571.zhiqinliao.hw9.util.ImgLruCache;
import csci571.zhiqinliao.hw9.widget.BillListArrayAdapter;
import csci571.zhiqinliao.hw9.widget.ComListArrayAdapter;
import csci571.zhiqinliao.hw9.widget.LegListArrayAdapter;

/**
 * Created by MeteorShower on 15/11/2016.
 */

public class FavFragment extends Fragment {
    private TabHost tabHost;
    private View favFragment;
    private ListView listView;
    private ListView wordListView;
    private LinearLayout wordListContainer;

    private List<String> favLegList;
    private List<String> favBillList;
    private List<String> favComList;
    private List<Character> wordList;
    private List<Integer> wordPos;

    private ArrayAdapter<String> legListAdapter;
    private ArrayAdapter<String> billListAdapter;
    private ArrayAdapter<String> comListAdapter;

    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private final int cacheSize = maxMemory / 5;

    private ImgLruCache imgLruCache;

    private DatabaseService dbService;

    private String curList = "";
    private boolean firstCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favFragment = inflater.inflate(R.layout.fav_fragment, container, false);
        dbService = new DatabaseService(getActivity());
        imgLruCache = new ImgLruCache(cacheSize);
        initTab(inflater);
        initListView();
        initLegListView();
        curList = "leg";
        firstCreate = true;
        return favFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!firstCreate) {
            if(curList.equals("leg")) {
                initLegListView();
            } else if(curList.equals("bill"))
                initBillListView();
            else if(curList.equals("com"))
                initComListView();
        }
        firstCreate = false;
    }

    private void initTab(LayoutInflater inflater) {
        tabHost = (TabHost) favFragment.findViewById(R.id.tabhost);
        tabHost.setup();

        View stateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legStataTabText = (TextView) stateTab.findViewById(R.id.tab_label);
        legStataTabText.setText("LEGISLATORS");

        View houseTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legHouseTabText = (TextView) houseTab.findViewById(R.id.tab_label);
        legHouseTabText.setText("BILLS");

        View senateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legSenateTabText = (TextView) senateTab.findViewById(R.id.tab_label);
        legSenateTabText.setText("COMMITTEES");

        tabHost.addTab(tabHost.newTabSpec("leg").setIndicator(stateTab).setContent(R.id.leg_tab));
        tabHost.addTab(tabHost.newTabSpec("bill").setIndicator(houseTab).setContent(R.id.bill_tab));
        tabHost.addTab(tabHost.newTabSpec("com").setIndicator(senateTab).setContent(R.id.com_tab));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                curList = s;
                if(s == "leg") {
                    wordListContainer.setVisibility(View.VISIBLE);
                    initLegListView();
                } else if(s == "bill") {
                    wordListContainer.setVisibility(View.GONE);
                    initBillListView();
                } else if(s == "com") {
                    wordListContainer.setVisibility(View.GONE);
                    initComListView();
                }
            }
        });
    }

    private void initListView() {
        listView = (ListView) favFragment.findViewById(R.id.list_view);
        wordListView = (ListView) favFragment.findViewById(R.id.word_list);
        wordListContainer = (LinearLayout) favFragment.findViewById(R.id.word_list_container);
        listView.setEmptyView(favFragment.findViewById(R.id.emptyElement));
        wordListView.setDivider(null);
    }

    private void initLegListView() {

        favLegList = dbService.getAllLegislators();
        constructWordList(favLegList);

        listView.setAdapter(new LegListArrayAdapter(getActivity(), favLegList, imgLruCache));

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



        wordListView.setAdapter(new ArrayAdapter<Character>(getActivity(), R.layout.word_list_item ,wordList));
        wordListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                int listHeight = view.getHeight();
                int position = (int) y / (listHeight / wordList.size());
                if(position >= wordList.size())
                    position = wordList.size() - 1;
                listView.setSelection(wordPos.get(position));
                return true;
            }
        });
        if(wordList.size() == 0)
            wordListContainer.setVisibility(View.GONE);
        else
            wordListContainer.setVisibility(View.VISIBLE);
    }

    private void initBillListView() {
        favBillList = dbService.getAllBills();
        listView.setAdapter(new BillListArrayAdapter(getActivity(), favBillList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = (String) listView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.setClass(getActivity(), BillDetailActivity.class);
                intent.putExtra("billInfo", selectedValue);
                startActivity(intent);
            }
        });
    }

    private void initComListView() {
        favComList = dbService.getAllCommittees();
        listView.setAdapter(new ComListArrayAdapter(getActivity(), favComList));
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

    private void constructWordList(List<String> legList) {
        List<Character> wordList = new ArrayList<>();
        List<Integer> wordPos = new ArrayList<>();
        JsonParser parser = new JsonParser();
        int i = 0;
        for(String leg:legList) {
            JsonObject curLeg = parser.parse(leg).getAsJsonObject();
            char wordFirstChar = curLeg.get("last_name").getAsString().charAt(0);
            if(wordList.indexOf(wordFirstChar) == -1) {
                wordList.add(wordFirstChar);
                wordPos.add(i);
            }
            i++;
        }
        this.wordList = wordList;
        this.wordPos = wordPos;
    }
}
