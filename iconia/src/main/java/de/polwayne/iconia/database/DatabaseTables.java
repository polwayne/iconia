package de.polwayne.iconia.database;

/**
 * Created by Paul on 29.05.14.
 */
public class DatabaseTables {

    public interface ItemColumns{

        public String ID = "_id";
        public String TIMESTAMP = "timestamp";
        public String TITLE = "title";
        public String DETAILS = "details";
        public String DATA = "_data";
        public String CATEGORY = "category";

        public static final String[] ALL_COLLUMNS =
                {
                        ID,
                        TIMESTAMP,
                        TITLE,
                        DETAILS,
                        DATA,
                        CATEGORY
                };
    }

    public class ItemTbl implements ItemColumns{

        public static final String TABLE_NAME = "items";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TIMESTAMP + " INTEGER, " +
                TITLE + " TEXT, " +
                DETAILS + " TEXT, " +
                DATA + " TEXT, " +
                CATEGORY + " INTEGER);";

        public static final String SQL_DROP =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

}
