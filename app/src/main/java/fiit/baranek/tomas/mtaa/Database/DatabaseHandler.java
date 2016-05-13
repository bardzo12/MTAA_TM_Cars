package fiit.baranek.tomas.mtaa.Database;

import android.content.ContentValues;
import android.content.Context;
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
    private static final String DATABASE_NAME = "carsManagerTables102";

    // Contacts table name
    private static final String TABLE_CARS = "cars";
    private static final String TABLE_CARS_DELETED = "cars_deleted";
    private static final String TABLE_CARS_UPDATE = "cars_updated";
    private static final String TABLE_CARS_CREATED = "cars_created";

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
    private static final String KEY_IMAGE = "image";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT," + KEY_IMAGE + " BLOB" + ")";

        String CREATE_CARS_TABLE_DELETE = "CREATE TABLE " + TABLE_CARS_DELETED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";

        String CREATE_CARS_TABLE_UPDATE = "CREATE TABLE " + TABLE_CARS_UPDATE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";

        String CREATE_CARS_TABLE_CREATED = "CREATE TABLE " + TABLE_CARS_CREATED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";

        db.execSQL(CREATE_CARS_TABLE_CREATED);
        db.execSQL(CREATE_CARS_TABLE_UPDATE);
        db.execSQL(CREATE_CARS_TABLE_DELETE);
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
    }


    //operations on the first table
    public void addCar(Car car){

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
        values.put(KEY_UPDATED, car.getC_update());
        values.put(KEY_OBJECT_ID, car.getObjectId());
        values.put(KEY_IMAGE, car.getC_image());

        db.insert(TABLE_CARS,null,values);
    }


    public void addCars(List<Car> cars){
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
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT," + KEY_IMAGE + " BLOB" +  ")";

        db.execSQL(CREATE_CARS_TABLE);


        for(Car car : cars) {
            System.out.println("Titi " + car.getObjectId());
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
            values.put(KEY_UPDATED, car.getC_update());
            values.put(KEY_OBJECT_ID, car.getObjectId());
            values.put(KEY_IMAGE, car.getC_image());

            db.insert(TABLE_CARS, null, values);
        }
    }

    public void deleteCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARS, KEY_OBJECT_ID + " = ?",
                new String[]{String.valueOf(car.getObjectId())});
        db.close();
    }

    public List<Car> getAllCars(){

        List<Car> carList = new ArrayList<Car>();
        String selectQuery = "SELECT * FROM " + TABLE_CARS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("Počet prvkov v db je: " + cursor);
        System.out.println("Velikosť: " + cursor.getCount());
        if(cursor.getCount() >0) {
            if (cursor.moveToFirst()) {
                do {
                    Car car = new Car();
                    System.out.println("V kurzore jedna: " + cursor.getString(1));
                    car.setC_engine(cursor.getString(1));
                    car.setC_phoneNumber(cursor.getString(2));
                    car.setC_price(Integer.parseInt(cursor.getString(3)));
                    car.setC_location(cursor.getString(4));
                    car.setC_categoryBrand(Integer.parseInt(cursor.getString(5)) + 1);
                    car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
                    car.setC_model(cursor.getString(7));
                    car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
                    car.setC_photo(cursor.getString(9));
                    car.setC_categoryFuel(Integer.parseInt(cursor.getString(10)) + 1);
                    car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11)) + 1);
                    car.setC_driveType(cursor.getString(12));
                    car.setC_interiorColor(cursor.getString(13));
                    if (cursor.getString(14) != null)
                        car.setC_update(Long.parseLong(cursor.getString(14)));
                    car.setObjectId(cursor.getString(15));
                    car.setC_image(cursor.getBlob(16));
                    carList.add(car);
                } while (cursor.moveToNext());
            }
        }
        return  carList;
    }

    public int getCarsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    // Getting single contact
    public Car getCar(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CARS, new String[]{KEY_ID,
                        KEY_ENGINE, KEY_PHONE_NUMBER, KEY_PRICE,
                        KEY_LOCATION, KEY_CATEGORY_BRAND, KEY_YEAR_OF_PRODUCTION,
                        KEY_MODEL, KEY_MILE_AGE, KEY_PHOTO, KEY_CATEGORY_FUEL,
                        KEY_CATEGORY_TRANSMISSION, KEY_DRIVE_TYPE, KEY_INTERIOR_COLOR,
                        KEY_UPDATED, KEY_OBJECT_ID,KEY_IMAGE}, KEY_OBJECT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Car car = new Car();
        car.setC_engine(cursor.getString(1));
        car.setC_phoneNumber(cursor.getString(2));
        car.setC_price(Integer.parseInt(cursor.getString(3)));
        car.setC_location(cursor.getString(4));
        car.setC_categoryBrand(Integer.parseInt(cursor.getString(5)) + 1);
        car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
        car.setC_model(cursor.getString(7));
        car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
        car.setC_photo(cursor.getString(9));
        car.setC_categoryFuel(Integer.parseInt(cursor.getString(10)) + 1);
        car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11)) + 1);
        car.setC_driveType(cursor.getString(12));
        car.setC_interiorColor(cursor.getString(13));
        if(cursor.getString(14) != null)
            car.setC_update(Long.parseLong(cursor.getString(14)));
        car.setObjectId(cursor.getString(15));
        car.setC_image(cursor.getBlob(16));
        return car;
    }


    //operations on the TABLE_CARS_UPDATE
    public int updateCar(Car car) {
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
        values.put(KEY_PHOTO, car.getC_photo());
        values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
        values.put(KEY_CATEGORY_TRANSMISSION, car.getC_categoryTransmissionInt());
        values.put(KEY_DRIVE_TYPE, car.getC_driveType());
        values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
        values.put(KEY_UPDATED, car.getC_update());
        values.put(KEY_OBJECT_ID, car.getObjectId());
        values.put(KEY_IMAGE, car.getC_image());
        db.insert(TABLE_CARS_UPDATE, null, values);

        // updating row
        return db.update(TABLE_CARS, values, KEY_OBJECT_ID + " = ?",
                new String[] { String.valueOf(car.getObjectId()) });
    }

    //tables for updated cars
    public void addCarUpdated(Car car){
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
        values.put(KEY_PHOTO, car.getC_photo());
        values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
        values.put(KEY_CATEGORY_TRANSMISSION, car.getC_categoryTransmissionInt());
        values.put(KEY_DRIVE_TYPE, car.getC_driveType());
        values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
        values.put(KEY_UPDATED, car.getC_update());
        values.put(KEY_OBJECT_ID, car.getObjectId());
        db.insert(TABLE_CARS_UPDATE, null, values);
    }

    public List<Car> getAllCarsUpdated(){
        List<Car> carList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS_UPDATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setC_engine(cursor.getString(1));
                car.setC_phoneNumber(cursor.getString(2));
                car.setC_price(Integer.parseInt(cursor.getString(3)));
                car.setC_location(cursor.getString(4));
                car.setC_categoryBrand(Integer.parseInt(cursor.getString(5)) + 1);
                car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
                car.setC_model(cursor.getString(7));
                car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
                car.setC_photo(cursor.getString(9));
                car.setC_categoryFuel(Integer.parseInt(cursor.getString(10)) + 1);
                car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11)) + 1);
                car.setC_driveType(cursor.getString(12));
                car.setC_interiorColor(cursor.getString(13));
                if(cursor.getString(14) != null)
                    car.setC_update(Long.parseLong(cursor.getString(14)));
                car.setObjectId(cursor.getString(15));
                carList.add(car);
            } while (cursor.moveToNext());
        }


        return  carList;
    }

    public void restartTableCarUpdated(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS_UPDATE);
        String CREATE_CARS_TABLE_UPDATED = "CREATE TABLE " + TABLE_CARS_UPDATE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE_UPDATED);
    }



    //tables for updated cars
    public void addCarCreated(Car car){
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
        values.put(KEY_PHOTO, car.getC_photo());
        values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
        values.put(KEY_CATEGORY_TRANSMISSION, car.getC_categoryTransmissionInt());
        values.put(KEY_DRIVE_TYPE, car.getC_driveType());
        values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
        values.put(KEY_UPDATED, car.getC_update());
        values.put(KEY_OBJECT_ID, car.getObjectId());
        db.insert(TABLE_CARS_CREATED, null, values);
    }

    public List<Car> getAllCarsCreated(){
        List<Car> carList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS_CREATED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setC_engine(cursor.getString(1));
                car.setC_phoneNumber(cursor.getString(2));
                car.setC_price(Integer.parseInt(cursor.getString(3)));
                car.setC_location(cursor.getString(4));
                car.setC_categoryBrand(Integer.parseInt(cursor.getString(5)) + 1);
                car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
                car.setC_model(cursor.getString(7));
                car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
                car.setC_photo(cursor.getString(9));
                car.setC_categoryFuel(Integer.parseInt(cursor.getString(10)) + 1);
                car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11)) + 1);
                car.setC_driveType(cursor.getString(12));
                car.setC_interiorColor(cursor.getString(13));
                if(cursor.getString(14) != null)
                    car.setC_update(Long.parseLong(cursor.getString(14)));
                car.setObjectId(cursor.getString(15));
                carList.add(car);
            } while (cursor.moveToNext());
        }


        return  carList;
    }

    public void restartTableCarCreated(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS_CREATED);
        String CREATE_CARS_TABLE_CREATED = "CREATE TABLE " + TABLE_CARS_CREATED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE_CREATED);
    }

    //tables for deleted cars
    public void addCarDeleted(Car car){
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
        values.put(KEY_PHOTO, car.getC_photo());
        values.put(KEY_CATEGORY_FUEL, car.getC_categoryFuelInt());
        values.put(KEY_CATEGORY_TRANSMISSION, car.getC_categoryTransmissionInt());
        values.put(KEY_DRIVE_TYPE, car.getC_driveType());
        values.put(KEY_INTERIOR_COLOR, car.getC_interiorColor());
        values.put(KEY_UPDATED, car.getC_update());
        values.put(KEY_OBJECT_ID, car.getObjectId());
        db.insert(TABLE_CARS_DELETED, null, values);
    }

    public List<Car> getAllCarsDeleted(){
        List<Car> carList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS_DELETED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setC_engine(cursor.getString(1));
                car.setC_phoneNumber(cursor.getString(2));
                car.setC_price(Integer.parseInt(cursor.getString(3)));
                car.setC_location(cursor.getString(4));
                car.setC_categoryBrand(Integer.parseInt(cursor.getString(5)) + 1);
                car.setC_yearOfProduction(Integer.parseInt(cursor.getString(6)));
                car.setC_model(cursor.getString(7));
                car.setC_mileAge(Integer.parseInt(cursor.getString(8)));
                car.setC_photo(cursor.getString(9));
                car.setC_categoryFuel(Integer.parseInt(cursor.getString(10)) + 1);
                car.setC_categoryTransmission(Integer.parseInt(cursor.getString(11)) + 1);
                car.setC_driveType(cursor.getString(12));
                car.setC_interiorColor(cursor.getString(13));
                if(cursor.getString(14) != null)
                    car.setC_update(Long.parseLong(cursor.getString(14)));
                car.setObjectId(cursor.getString(15));
                carList.add(car);
            } while (cursor.moveToNext());
        }


        return  carList;
    }

    public void restartTableCarDeleted(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS_DELETED);
        String CREATE_CARS_TABLE_DELETED = "CREATE TABLE " + TABLE_CARS_DELETED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENGINE + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT," + KEY_PRICE + " INTEGER,"
                + KEY_LOCATION + " TEXT," + KEY_CATEGORY_BRAND + " INTEGER,"
                + KEY_YEAR_OF_PRODUCTION + " INTEGER," + KEY_MODEL + " TEXT,"
                + KEY_MILE_AGE + " INTEGER," + KEY_PHOTO + " TEXT,"
                + KEY_CATEGORY_FUEL + " INTEGER," + KEY_CATEGORY_TRANSMISSION + " INTEGER,"
                + KEY_DRIVE_TYPE + " TEXT," + KEY_INTERIOR_COLOR + " INTEGER,"
                + KEY_UPDATED + " INTEGER," + KEY_OBJECT_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE_DELETED);
    }

}
