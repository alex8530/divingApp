package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
//    public static final String AGE = "age";
    public static final String CREATE_TABLE_ORDER = " create table IF NOT EXISTS ORDER_DِETALIS ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ProductId TEXT , ProviderID TEXT,ProductName TEXT,Quantity INTEGER,Price DOUBLE,Discount INTEGER,BankAccount TEXT );";
//private static final String CREATE_TABLE_STUDENT = " create table STUDENTS ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL , age TEXT );";

//    String[] sqlSelect= {"ProductId","ProviderID","ProductName","Quantity","Price","Discount"};

    private static final String DB_NAME = "Diver-App.db";
    private static final int DB_VERSION = 1;

    // String prodictId, 1
// String provider2
// , String prodictName3
// , int quantity4
// , int price 5
// , int discount 6

    public static final String ID = "ID";
    public static final String PRODICTID = "ProductId";
    public static final String PROVIDERID = "ProviderID";
    public static final String PRODICTNAME = "ProductName";
    public static final String QUANTITY = "Quantity";
    public static final String PRICE = "Price";
    public static final String DICSOUNT = "Discount";
    public static final String BANKACCOUNT = "BankAccount";


    public static final String TABLE_NAME_ORDER = "ORDER_DِETALIS";
//    public static final String _ID = "_id";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        Log.e("SQLITE", "table reated");
        db.execSQL(CREATE_TABLE_ORDER);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ORDER_DِETALIS");
        onCreate(db);
    }

}
