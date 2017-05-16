package csci571.zhiqinliao.hw9.db;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import csci571.zhiqinliao.hw9.pojo.Bill;
import csci571.zhiqinliao.hw9.pojo.Committee;
import csci571.zhiqinliao.hw9.pojo.Legislator;

/**
 * An service class provides database service for application and split database and interface operation.
 */

public class DatabaseService {

    private DatabaseHelper myDatabaseHelper;

    public DatabaseService(Context context) {
        myDatabaseHelper = new DatabaseHelper(context, 1);
    }

    public void addNewLegislator(Legislator legislator) {
        myDatabaseHelper.addNewLegislator(legislator);
    }

    public boolean whetherLegLiked(String legID) {
        if(myDatabaseHelper.getLegislatorByID(legID) == null)
            return false;
        else
            return true;
    }

    public void removeFavLeg(String legID) {
        myDatabaseHelper.deleteLegByID(legID);
    }

    public List<String> getAllLegislators() {
        List<Legislator> legList = myDatabaseHelper.getAllLegislators();
        List<String> returnList = new ArrayList<>();
        Collections.sort(legList, new Comparator<Legislator>() {
            @Override
            public int compare(Legislator l1, Legislator l2) {
                if(l1.getLastName().charAt(0) > l2.getLastName().charAt(0))
                    return 1;
                else if(l1.getLastName().charAt(0) < l2.getLastName().charAt(0))
                    return -1;
                else
                    return 0;
            }
        });
        for(Legislator legislator:legList) {
            returnList.add(legislator.getLegCont());
        }
        return returnList;
    }


    public void addNewBill(Bill bill) {
        myDatabaseHelper.addNewBill(bill);
    }

    public boolean whetherBillLiked(String billID) {
        if(myDatabaseHelper.getBillByID(billID) == null)
            return false;
        else
            return true;
    }

    public void removeFavBill(String billID) {
        myDatabaseHelper.deleteBillByID(billID);
    }

    public List<String> getAllBills() {
        List<Bill> billList = myDatabaseHelper.getAllBills();
        List<String> returnList = new ArrayList<>();
        Collections.sort(billList, new Comparator<Bill>() {
            @Override
            public int compare(Bill b1, Bill b2) {
                DateFormat oriFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    System.out.println(b1.getBillIntroduceOn());
                    System.out.println(b2.getBillIntroduceOn());
                    if(oriFormat.parse(b1.getBillIntroduceOn()).before(oriFormat.parse(b2.getBillIntroduceOn())))
                        return 1;
                    else if(oriFormat.parse(b2.getBillIntroduceOn()).before(oriFormat.parse(b1.getBillIntroduceOn())))
                        return -1;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        for(Bill bill:billList)
            returnList.add(bill.getBillCont());
        return returnList;
    }


    public void addNewCommittee(Committee committee) {
        myDatabaseHelper.addNewCom(committee);
    }

    public boolean whetherCommitteeLiked(String comID) {
        if(myDatabaseHelper.getComByID(comID) == null)
            return false;
        else
            return true;
    }

    public void removeFavCommittee(String comID) {
        myDatabaseHelper.deleteComByID(comID);
    }

    public List<String> getAllCommittees() {
        List<Committee> comList = myDatabaseHelper.getAllCommittees();
        List<String> returnList = new ArrayList<>();
        Collections.sort(comList, new Comparator<Committee>() {
            @Override
            public int compare(Committee c1, Committee c2) {
                if(c1.getComName().charAt(0) > c2.getComName().charAt(0))
                    return 1;
                else if(c1.getComName().charAt(0) < c2.getComName().charAt(0))
                    return -1;
                else
                    return 0;
            }
        });
        for(Committee committee:comList)
            returnList.add(committee.getComCont());
        return returnList;
    }
}
