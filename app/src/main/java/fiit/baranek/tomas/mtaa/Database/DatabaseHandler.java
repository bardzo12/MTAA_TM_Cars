package fiit.baranek.tomas.mtaa.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fiit.baranek.tomas.mtaa.Car;

/**
 * Created by TomasPC on 7.4.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "carsManager";

    // Contacts table name
    private static final String TABLE_CARS = "cars";

    /*
    private String c_engine;
    private String created;
    private String c_phoneNumber;
    private int c_price;
    private String c_location;
    private CategoryBrand c_categoryBrand;
    private int c_yearOfProduction;
    private String c_model;
    private int c_mileAge;
    private String c_photo;
    private CategoryFuel c_categoryFuel;
    private CategoryTransmission c_categoryTransmission;
    private String c_driveType;
    private String c_interiorColor;
    private String objectId;
     */
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_OBJECT_ID = "object_id";
    private static final String KEY_ENGINE = "engine";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PRICE = "price";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_CATEGORY_BRAND = "category_brand";
    private static final String KEY_YEAR_OF_PRODUCTION = "year_of_production";
    private static final String KEY_MODEL = "model";
    private static final String KEY_MILE_AGE = "mile_age";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_CATEGORY_FUEL = "category_fuel";
    private static final String KEY_CATEGORY_TRANSMISSION = "category_transmission";
    private static final String KEY_DRIVE_TYPE = "drive_type";
    private static final String KEY_INTERIOR_COLOR = "iterior_color";
    private static final String KEY_UPDATED = "updated";


    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
    }

    void addCar(Car car){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENGINE, car.getC_engine());
        values.put(KEY_PHONE_NUMBER, car.getC_phoneNumber());
        values.put(KEY_PRICE, car.getC_price());
        values.put(KEY_LOCATION, car.getC_location());
        values.put(KEY_CATEGORY_BRAND, car.getC_categoryBrandInt());
        values.put(KEY_YEAR_OF_PRODUCTION, car.getC_yearOfProduction());
        values.put(KEY_MODEL, car.getC_model());
        values.put(KEY_MILE_AGE, car.getC_mileAge());
        values.put(KEY_PHOTO,car.getC_photo());
        values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
        values.put(KEY_CATEGORY_TRANSMISSION,car.getC_categoryTransmissionInt());
        values.put(KEY_DRIVE_TYPE, car.getC_driveType());
        values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
        values.put(KEY_OBJECT_ID, car.getObjectId());

        db.insert(TABLE_CARS,null,values);
    }


    public void addCars(List<Car> cars){

        System.out.println("TUSOMSA2");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE);
        // db.needUpgrade(db.getVersion()+1);
        for(Car car : cars) {
            ContentValues values = new ContentValues();
            values.put(KEY_ENGINE, car.getC_engine());
            values.put(KEY_PHONE_NUMBER, car.getC_phoneNumber());
            values.put(KEY_PRICE, car.getC_price());
            values.put(KEY_LOCATION, car.getC_location());
            values.put(KEY_CATEGORY_BRAND, car.getC_categoryBrandInt());
            values.put(KEY_YEAR_OF_PRODUCTION, car.getC_yearOfProduction());
            values.put(KEY_MODEL, car.getC_model());
            values.put(KEY_MILE_AGE, car.getC_mileAge());
            values.put(KEY_PHOTO, car.getC_photo());
            values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
            values.put(KEY_CATEGORY_TRANSMISSION, car.getC_categoryTransmissionInt());
            values.put(KEY_DRIVE_TYPE, car.getC_driveType());
            values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
            values.put(KEY_OBJECT_ID, car.getObjectId());

            db.insert(TABLE_CARS, null, values);
        }
    }

    public List<Car> getAllCars(){
        System.out.println("TUSOMSA3");
        List<Car> carList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                //car.setObjectId(cursor.getString(0));
                car.setC_engine(cursor.getString(1));
                car.setC_phoneNumber(cursor.getString(2));
                car.setC_price(Integer.parseInt(cursor.getString(3)));
                car.setC_location(cursor.getString(4));
                car.setC_categoryBrand(Integer.parseInt(cursor.getString(5))+1);
                car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
                car.setC_model(cursor.getString(7));
                car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
                car.setC_photo(cursor.getString(9));
                car.setC_categoryFuel(Integer.parseInt(cursor.getString(10))+1);
                car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11))+1);
                car.setC_driveType(cursor.getString(12));
                car.setC_interiorColor(cursor.getString(13));
                car.setObjectId(cursor.getString(14));
                carList.add(car);
            } while (cursor.moveToNext());
        }


        return  carList;
    }

    public int getCarsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }
}
