package com.josephblough.alibris.transport;

public class SearchRequestConstants {

    // Works search constants
    public static final String WORKS_SEARCH_FIELD_AUTHOR = "wauth";
    public static final String WORKS_SEARCH_FIELD_TITLE = "wtit";
    public static final String WORKS_SEARCH_FIELD_TOPIC = "wtopic";
    public static final String WORKS_SEARCH_FIELDS_ALL = "wquery";
    
    // Items search constants
    public static final String ITEMS_SEARCH_FIELD_AUTHOR = "qauth";
    public static final String ITEMS_SEARCH_FIELD_TITLE = "qtit";
    public static final String ITEMS_SEARCH_FIELD_TOPIC = "qtopic";
    public static final String ITEMS_SEARCH_FIELD_WORK_ID = "qwork";
    public static final String ITEMS_SEARCH_FIELDS_ALL = "query";
    public static final String ITEMS_SEARCH_FIELD_PUBLISHER = "qpub";
    public static final String ITEMS_SEARCH_FIELD_ISBN = "qisbn";
    public static final String ITEMS_SEARCH_FIELD_ISBN_LIST = "qisbnlist";
    public static final String ITEMS_SEARCH_FIELD_CONDITION_MIN = "qcond";
    public static final String ITEMS_SEARCH_FIELD_CONDITION_MAX = "qcondhi";
    public static final String ITEMS_SEARCH_FIELD_RATING = "qrating";
    public static final String ITEMS_SEARCH_FIELD_CLASSIFICATION = "qlccl";
    public static final String ITEMS_SEARCH_FIELD_INVENTORY_ID = "Invid";
    public static final String ITEMS_SEARCH_FIELD_SELLER_ID = "quserid";
    public static final String ITEMS_SEARCH_FIELD_BINDING = "binding";
    public static final String ITEMS_SEARCH_FIELD_SIGNED = "signed";
    public static final String ITEMS_SEARCH_FIELD_FIRST_EDITION = "first";
    public static final String ITEMS_SEARCH_FIELD_DUST_JACKET = "dj";
    public static final String ITEMS_SEARCH_FIELD_LANGUAGE = "language";
    //Between
    public static final String ITEMS_SEARCH_FIELD_PRICE_MIN = "qprice";
    public static final String ITEMS_SEARCH_FIELD_PRICE_MAX = "qpricehi";
    // Between
    public static final String ITEMS_SEARCH_FIELD_YEAR_MIN = "qyear";
    public static final String ITEMS_SEARCH_FIELD_YEAR_MAX = "qyearhi";
    public static final String ITEMS_SEARCH_FIELD_YEAR = "qyearonly";
    public static final String ITEMS_SEARCH_FIELD_LISTING_DAYS = "qdays";
    
    public static final String ITEMS_CONDITION_NEW = "6";
    public static final String ITEMS_CONDITION_LIKE_NEW = "5";
    public static final String ITEMS_CONDITION_VERY_GOOD = "4";
    public static final String ITEMS_CONDITION_GOOD = "3";
    public static final String ITEMS_CONDITION_FAIR = "2";
    public static final String ITEMS_CONDITION_POOR = "1";
    
    public static final String ITEMS_RATING_BEST = "5";
    public static final String ITEMS_RATING_HIGH = "4";
    public static final String ITEMS_RATING_AVERAGE = "3";
    public static final String ITEMS_RATING_LOW = "2";
    public static final String ITEMS_RATING_POOR = "1";
    
    // Book Item bindings
    public static final String ITEMS_BINDING_BOOK_HARDCOVER = "B";
    public static final String ITEMS_BINDING_BOOK_SOFTCOVER = "S";
    public static final String ITEMS_BINDING_BOOK_AUDIOBOOK_CD = "G";
    public static final String ITEMS_BINDING_BOOK_AUDIOBOOK_TAPE = "J";
    
    // Music Item bindings
    public static final String ITEMS_BINDING_MUSIC_CD = "C";
    public static final String ITEMS_BINDING_MUSIC_CD_SINGLE = "1";
    public static final String ITEMS_BINDING_MUSIC_SACD = "Q";
    public static final String ITEMS_BINDING_MUSIC_DVD_AUDIO = "U";
    public static final String ITEMS_BINDING_MUSIC_VINYL = "R";
    public static final String ITEMS_BINDING_MUSIC_LP = "5";
    public static final String ITEMS_BINDING_MUSIC_7_INCH_SINGLE = "7";
    public static final String ITEMS_BINDING_MUSIC_12_INCH_SINGLE = "6";
    public static final String ITEMS_BINDING_MUSIC_78_RPM = "9";
    public static final String ITEMS_BINDING_MUSIC_TAPE = "T";
    public static final String ITEMS_BINDING_MUSIC_8_TRACK = "8";
    public static final String ITEMS_BINDING_MUSIC_MINI_DISC = "K";
    public static final String ITEMS_BINDING_MUSIC_REEL_TO_REEL = "P";
    
    // Movie Item bindings
    public static final String ITEMS_BINDING_MOVIE_DVD = "D";
    public static final String ITEMS_BINDING_MOVIE_VHS = "X";
    public static final String ITEMS_BINDING_MOVIE_BLU_RAY = "Y";
    public static final String ITEMS_BINDING_MOVIE_HD_DVD = "Z";
    public static final String ITEMS_BINDING_MOVIE_UMD = "2";
    public static final String ITEMS_BINDING_MOVIE_LASER_DISC = "L";
    public static final String ITEMS_BINDING_MOVIE_BETA = "3";
    public static final String ITEMS_BINDING_MOVIE_FILM_REEL = "F";
    public static final String ITEMS_BINDING_MOVIE_VCD = "O";
    
    // Shared search constants
    public static final String SEARCH_TYPE = "mtype";
    public static final String SEARCH_RESULTS_COUNT = "chunk";
    public static final String SEARCH_RESULTS_OFFSET = "skip";
    public static final String SEARCH_SORT = "qsort";
    
    public static final String SORT_RATING = "r"; 
    public static final String SORT_TITLE = "t"; 
    public static final String SORT_AUTHOR = "a"; 
    public static final String SORT_PRICE = "p"; 
    public static final String SORT_DATE = "d";
    public static final String REVERSE_SORT = "r";

    public static final String SORT_CONDITION = "c";
    
    public static final String SEARCH_TYPE_BOOKS = "B";
    public static final String SEARCH_TYPE_MUSIC = "M";
    public static final String SEARCH_TYPE_VIDEO = "V";
    public static final String SEARCH_TYPE_ALL = "A";
}
