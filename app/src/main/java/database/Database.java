package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import Modle.Order;

public class Database extends SQLiteAssetHelper {

    private static final String dbName ="Diver-App.db";
    private static final int dbVresion = 1 ;

    public Database(Context context) {
        super(context, dbName, null, dbVresion);
    }
    public List<Order> getCart(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect= {"ProductId","ProviderID","ProductName","Quantity","Price","Discount"};
//        String[] sqlSelect= {"ID","NAME","AGE"};

        String sqlTable = "ORDER_DِETALIS";


        //        CREATE TABLE `OrderDetails` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `ProductId` TEXT, `ProviderID` TEXT, `ProductName` TEXT, `Quantity` TEXT, `Price` TEXT, `Discount` TEXT )
//        db.execSQL("CREATE TABLE OrderDetails (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,ProductId TEXT, ProviderID TEXT, ProductName TEXT, Quantity TEXT, Price TEXT, Discount TEXT ) ");

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null,null);
        final List<Order> resulte = new ArrayList<>();

        /*
        	`ID`
	`ProductId`
	`ProviderID`
	`ProductName`
	`Quantity`
	`Price`
	`Discount`
         */
//        if (c.moveToFirst()){
//            do{
//                resulte.add(new Order(c.getString(c.getColumnIndex("ProductId")),
//                        c.getString(c.getColumnIndex("ProviderID")),
//                        c.getString(c.getColumnIndex("ProductName")),
//                        c.getInt(c.getColumnIndex("Quantity")),
//                        c.getInt(c.getColumnIndex("Price")),
//                        c.getInt(c.getColumnIndex("Discount"))
//                ));
//            } while (c.moveToNext());
//        }
        return  resulte;
    }
    public void selectFromDB() {
    }
    public void addToCart(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("INSERT INTO ORDER_DِETALIS (ProductId,ProviderID,ProductName,Quantity, Price, Discount) VALUES ('%s','%s','%s','%s','%s','%s');",
                order.getProdictId(),
                order.getProviderID(),
                order.getProdictName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);
    }
    public void deleteFromCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails");
        db.execSQL(query);
    }
}
