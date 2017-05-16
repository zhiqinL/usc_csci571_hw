package csci571.zhiqinliao.hw9.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import csci571.zhiqinliao.hw9.R;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class ComListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> comList;

    public ComListArrayAdapter(Context context, List<String> comList) {
        super(context, R.layout.com_list_item, comList);
        this.context = context;
        this.comList = comList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(R.layout.com_list_item, parent, false);
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject curCom = jsonParser.parse(comList.get(position)).getAsJsonObject();

            TextView comID = (TextView) listItem.findViewById(R.id.com_id);
            TextView comName = (TextView) listItem.findViewById(R.id.com_name);
            TextView chamber = (TextView) listItem.findViewById(R.id.chamber);

            String comIDTxt = curCom.get("committee_id").getAsString().toUpperCase();
            String comNameTxt = curCom.get("name").getAsString();
            String chamberTxt = curCom.get("chamber").getAsString();

            comID.setText(comIDTxt);
            comName.setText(comNameTxt);
            chamber.setText(chamberTxt.substring(0, 1).toUpperCase() + chamberTxt.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
}