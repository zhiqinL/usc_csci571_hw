package csci571.zhiqinliao.hw9.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import csci571.zhiqinliao.hw9.pojo.Bill;
import csci571.zhiqinliao.hw9.pojo.Committee;
import csci571.zhiqinliao.hw9.pojo.Legislator;

/**
 * Created by MeteorShower on 18/11/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    final private static String databaseName = "zhiqin_liao.hw9";

    final private String legTable = "Legislator";
    final private String legID = "legislator_id";
    final private String legLastName = "last_name";
    final private String legCont = "legislator_cont";

    final private String billTable = "Bill";
    final private String billID = "bill_id";
    final private String billIntroduceOn = "introduce_on";
    final private String billCont = "bill_cont";

    final private String comTable = "Committee";
    final private String comID = "committee_id";
    final private String comName = "committee_name";
    final private String comCont = "committee_cont";


    public DatabaseHelper(Context context, int version) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createLegTable = "CREATE TABLE " + legTable + "(" +
                legID + " TEXT PRIMARY KEY," +
                legLastName + " TEXT," +
                legCont + " TEXT)";
        sqLiteDatabase.execSQL(createLegTable);

        String createBillTable = "CREATE TABLE " + billTable + "(" +
                billID + " TEXT PRIMARY KEY," +
                billIntroduceOn + " TEXT," +
                billCont + " TEXT)";
        sqLiteDatabase.execSQL(createBillTable);

        String createComTable = "CREATE TABLE " + comTable + "(" +
                comID + " TEXT PRIMARY KEY," +
                comName + " TEXT," +
                comCont + " TEXT)";
        sqLiteDatabase.execSQL(createComTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addNewLegislator(Legislator legislator) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(legID, legislator.getLegID());
        cv.put(legLastName, legislator.getLastName());
        cv.put(legCont, legislator.getLegCont());
        db.insert(legTable, legID, cv);
        db.close();
    }

    public Legislator getLegislatorByID(String legID) {
        Legislator legislator;
        String queryStr = "SELECT * FROM " + legTable + " WHERE " + this.legID + " = \"" + legID + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStr, null);
        if(cursor.moveToNext())
            legislator = new Legislator(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        else
            legislator = null;
        db.close();
        return legislator;
    }

    public void deleteLegByID(String legID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(legTable, this.legID + "=?", new String[]{legID});
        db.close();
    }

    public List<Legislator> getAllLegislators() {
        List<Legislator> legList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + legTable, null);
        while(cursor.moveToNext())
            legList.add(new Legislator(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        return legList;
    }

    public void addNewBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(billID, bill.getBillID());
        cv.put(billIntroduceOn, bill.getBillIntroduceOn());
        cv.put(billCont, bill.getBillCont());
        db.insert(billTable, billID, cv);
        db.close();
    }

    public Bill getBillByID(String billID) {
        Bill bill;
        String queryStr = "SELECT * FROM " + billTable + " WHERE " + this.billID + " = \"" + billID + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStr, null);
        if(cursor.moveToNext())
            bill = new Bill(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        else
            bill = null;
        db.close();
        return bill;
    }

    public void deleteBillByID(String billID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(billTable, this.billID + "=?", new String[]{billID});
        db.close();
    }

    public List<Bill> getAllBills() {
        List<Bill> billList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + billTable, null);
        while(cursor.moveToNext())
            billList.add(new Bill(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        return billList;
    }

    public void addNewCom(Committee com) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(comID, com.getComID());
        cv.put(comName, com.getComName());
        cv.put(comCont, com.getComCont());
        db.insert(comTable, comID, cv);
        db.close();
    }

    public Committee getComByID(String comID) {
        Committee com;
        String queryStr = "SELECT * FROM " + comTable + " WHERE " + this.comID + " = \"" + comID + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStr, null);
        if(cursor.moveToNext())
            com = new Committee(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        else
            com = null;
        db.close();
        return com;
    }

    public void deleteComByID(String comID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(comTable, this.comID + "=?", new String[]{comID});
        db.close();
    }

    public List<Committee> getAllCommittees() {
        List<Committee> comList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + comTable, null);
        while(cursor.moveToNext())
            comList.add(new Committee(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        return comList;
    }
}
