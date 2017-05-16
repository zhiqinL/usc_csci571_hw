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
import csci571.zhiqinliao.hw9.util.Util;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class BillListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> billList;

    public BillListArrayAdapter(Context context, List<String> billList) {
        super(context, R.layout.bill_list_item, billList);
        this.context = context;
        this.billList = billList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(R.layout.bill_list_item, parent, false);
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject curBill = jsonParser.parse(billList.get(position)).getAsJsonObject();

            TextView billID = (TextView) listItem.findViewById(R.id.bill_id);
            TextView billTitle = (TextView) listItem.findViewById(R.id.bill_title);
            TextView introduceOn = (TextView) listItem.findViewById(R.id.introduce_on);

            String billIDTxt = curBill.get("bill_id").getAsString().toUpperCase();
            String billTitleTxt = (curBill.get("short_title").isJsonNull())? curBill.get("official_title").getAsString() : curBill.get("short_title").getAsString();
            String introduceOnTxt = Util.formateDate(curBill.get("introduced_on").getAsString());

            billID.setText(billIDTxt);
            billTitle.setText(billTitleTxt);
            introduceOn.setText(introduceOnTxt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
}