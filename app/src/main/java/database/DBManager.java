package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import Modle.Order;

public class DBManager {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public DBManager(Context c) {
        this.context = c;
    }

    public DBManager open() throws SQLException {
        this.dbHelper = new SQLiteHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void insert(Order order) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        sqLiteHelper.onCreate(database);
        ContentValues contentValue = new ContentValues();

        contentValue.put(SQLiteHelper.PRODICTID, order.getProdictId());
        contentValue.put(SQLiteHelper.PROVIDERID, order.getProviderID());
        contentValue.put(SQLiteHelper.PRODICTNAME, order.getProdictName());
        contentValue.put(SQLiteHelper.QUANTITY, order.getQuantity());
        contentValue.put(SQLiteHelper.PRICE, order.getPrice());
        contentValue.put(SQLiteHelper.DICSOUNT, order.getDiscount());
        contentValue.put(SQLiteHelper.BANKACCOUNT, order.getBankAccount());

        this.database.insert(SQLiteHelper.TABLE_NAME_ORDER, null, contentValue);

//        Log.e(MainActivity.TAG, "insert: " + this.database.getPath() );
    }
    public Cursor fetch() {

        Cursor cursor = this.database.query(SQLiteHelper.TABLE_NAME_ORDER, new String[]{SQLiteHelper.ID,SQLiteHelper.PRODICTID, SQLiteHelper.PROVIDERID,SQLiteHelper.PRODICTNAME,SQLiteHelper.QUANTITY, SQLiteHelper.PRICE,SQLiteHelper.DICSOUNT, SQLiteHelper.BANKACCOUNT}, null, null, null, null, null);
//        Log.d(MainActivity.TAG, "fetch: 1");
        if (cursor.getCount() != 0 ) {
//            Log.d(MainActivity.TAG, "fetch: cursor " + cursor.getCount());
//            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public void deleteFromCart(){
       SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
       sqLiteHelper.onUpgrade(database,1,2);
//        String query = String.format("DROP TABLE IF EXISTS ORDER_DِETALIS");
//        this.database.execSQL(query);
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLiteHelper.NAME, name);
//        contentValues.put(SQLiteHelper.AGE, desc);
        return this.database.update(SQLiteHelper.TABLE_NAME_ORDER, contentValues, "_id = " + _id, null);
    }

    public boolean deleteTitle(Long id) {
        return this.database.delete(SQLiteHelper.TABLE_NAME_ORDER, "ID" + "=" + id, null) > 0;
    }
    public void delete(long _id) {
        this.database.delete(SQLiteHelper.TABLE_NAME_ORDER, "ID = " + _id, null);
    }
}