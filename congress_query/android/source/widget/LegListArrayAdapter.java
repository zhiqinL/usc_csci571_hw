package csci571.zhiqinliao.hw9.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;

import csci571.zhiqinliao.hw9.R;

/**
 * Created by MeteorShower on 19/11/2016.
 */

public class LegListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> legList;
    private Picasso picasso;

    private LruCache<String, RequestCreator> mLruCache;

    public LegListArrayAdapter(Context context, List<String> legList, LruCache mLruCache) {
        super(context, R.layout.leg_list_item, legList);
        this.context = context;
        this.legList = legList;
        this.picasso = Picasso.with(context);
        this.mLruCache = mLruCache;
        //new GetAllLegImg(legList, mLruCache).execute();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(R.layout.leg_list_item, parent, false);
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject curLeg = jsonParser.parse(legList.get(position)).getAsJsonObject();

            ImageView legImg = (ImageView) listItem.findViewById(R.id.leg_list_item_img);
            TextView nameText = (TextView) listItem.findViewById(R.id.leg_list_item_name);
            TextView districtText = (TextView) listItem.findViewById(R.id.leg_list_item_district);

            String legFullName = curLeg.get("last_name").getAsString() + ", " + curLeg.get("first_name").getAsString();

            String legDistName = (curLeg.get("district").isJsonNull())? "0" : curLeg.get("district").getAsString();
            String legInfo = "(" + curLeg.get("party").getAsString() + ")" + curLeg.get("state_name").getAsString() + " - District " + legDistName;

            nameText.setText(legFullName);
            districtText.setText(legInfo);
            loadLegImg(curLeg.get("bioguide_id").getAsString(), legImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }

    private void loadLegImg(String legID, ImageView legImg) {
        GetLegImg getLegImg = new GetLegImg(legImg, legID, mLruCache);
        RequestCreator requestCreator = mLruCache.get(legID);
        if (requestCreator != null) {
            requestCreator.resize(60, 75).into(legImg);
        } else {
            getLegImg.execute();
        }
    }

    class GetLegImg extends AsyncTask<String, Integer, String> {
        private ImageView legImg;
        private String legID;
        private String imgURL;
        private LruCache<String, RequestCreator> lruCache;
        private String imgBasicURL = "https://theunitedstates.io/images/congress/original/";
        private RequestCreator rc;

        GetLegImg(ImageView legImg, String legID, LruCache<String, RequestCreator> lruCache) {
            this.legImg = legImg;
            this.legID = legID;
            this.lruCache = lruCache;
            this.imgURL = imgBasicURL + legID + ".jpg";
        }

        @Override
        protected String doInBackground(String... strings) {
            rc = picasso.load(imgURL);
            try {
                addBitmapToMemoryCache(legID, rc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            rc.resize(60, 75).into(legImg);
        }

        private void addBitmapToMemoryCache(String key, RequestCreator requestCreator) throws IOException {
            if (getBitmapFromMemoryCache(key) == null) {
                lruCache.put(key, requestCreator);
            }
        }

        public RequestCreator getBitmapFromMemoryCache(String key) {
            return lruCache.get(key);
        }
    }
}
