package csci571.zhiqinliao.hw9.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import csci571.zhiqinliao.hw9.R;
import csci571.zhiqinliao.hw9.util.Util;
import csci571.zhiqinliao.hw9.util.WebService;
import csci571.zhiqinliao.hw9.widget.BillListArrayAdapter;

/**
 * bill fragent to show all bills information
 */

public class BillFragment extends Fragment {

    private View billFragment;
    private TabHost tabHost;
    private ListView listView;


    private List<String> activeBillInfo;
    private List<String> newBillInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        billFragment = inflater.inflate(R.layout.bill_fragment, container, false);
        initTab(inflater);
        initBillListView();
        return billFragment;
    }

    /**
     * init two bar in bill activity
     * @param inflater
     */
    private void initTab(LayoutInflater inflater) {
        tabHost = (TabHost) billFragment.findViewById(R.id.tabhost);
        tabHost.setup();

        View stateTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legStataTabText = (TextView) stateTab.findViewById(R.id.tab_label);
        legStataTabText.setText("ACTIVE BILLS");

        View houseTab = (View) inflater.inflate(R.layout.tab_layout, null);
        TextView legHouseTabText = (TextView) houseTab.findViewById(R.id.tab_label);
        legHouseTabText.setText("NEW BILLS");

        tabHost.addTab(tabHost.newTabSpec("active").setIndicator(stateTab).setContent(R.id.active_tab));
        tabHost.addTab(tabHost.newTabSpec("new").setIndicator(houseTab).setContent(R.id.new_tab));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s == "active") {
                    if (activeBillInfo == null)
                        new GetBillInfo("active").execute();
                    else {
                        listView.setAdapter(new BillListArrayAdapter(getActivity(), activeBillInfo));
                    }
                } else if(s == "new") {
                    if (newBillInfo == null)
                        new GetBillInfo("new").execute();
                    else {
                        listView.setAdapter(new BillListArrayAdapter(getActivity(), newBillInfo));
                    }
                }
            }
        });
    }

    /**
     * init basic view in bill activity
     */
    private void initBillListView() {
        listView = (ListView) billFragment.findViewById(R.id.list_view);
        new GetBillInfo("active").execute();
        listView.setEmptyView(billFragment.findViewById(R.id.emptyElement));
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

    /**
     * child to request internet to get bills information
     */
    class GetBillInfo extends AsyncTask<String, Integer, String> {
        private JsonArray billInfo;
        private List<String> billListItems;
        private String type;

        GetBillInfo(String type) {
            this.type = type;
        }

        @Override
        protected String doInBackground(String... strings) {
            billInfo = WebService.getBillInfo(type);
            billListItems = setBillList();

            if(type == "active")
                activeBillInfo = billListItems;
            else if(type == "new")
                newBillInfo = billListItems;

            return null;
        }

        private List<String> setBillList() {
            List<String> billItems = new ArrayList<>();
            Iterator billListIt = billInfo.iterator();
            while(billListIt.hasNext()) {
                JsonElement billItem = (JsonElement) billListIt.next();
                billItems.add(billItem.toString());
            }
            return billItems;
        }

        @Override
        protected void onPostExecute(String result) {
            listView.setAdapter(new BillListArrayAdapter(getActivity(), billListItems));
        }
    }

}
