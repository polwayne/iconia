package de.polwayne.iconia.pojo;

/**
 * Created by Paul on 30.05.14.
 */
public class Item {

    public static final int CATEGORY_THOUGHT = 0;
    public static final int CATEGORY_DREAM = 1;
    public static final int CATEGORY_MEMORY = 2;

    public long id;
    public long time;
    public String title;
    public String details;
    public String data;
    public int category;

}
