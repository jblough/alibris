package com.josephblough.alibris.data;

public class OfferFilterCriteria {

    public static final int FILTER_CONDITION_NEW = 0;
    public static final int FILTER_CONDITION_FINE = 1;
    public static final int FILTER_CONDITION_VERY_GOOD = 2;
    public static final int FILTER_CONDITION_GOOD = 3;
    public static final int FILTER_CONDITION_FAIR = 4;
    public static final int FILTER_CONDITION_POOR = 5;
    public static final int FILTER_CONDITION_ANY = 6;

    public static final int FILTER_SELLER_RATING_BEST = 0;
    public static final int FILTER_SELLER_RATING_HIGH = 1;
    public static final int FILTER_SELLER_RATING_AVERAGE = 2;
    public static final int FILTER_SELLER_RATING_LOW = 3;
    public static final int FILTER_SELLER_RATING_POOR = 4;
    public static final int FILTER_SELLER_RATING_ANY = 5;

    public static final int SORT_ORDER_RATING = 0;
    public static final int SORT_ORDER_TITLE = 1;
    public static final int SORT_ORDER_AUTHOR = 2;
    public static final int SORT_ORDER_PRICE = 3;
    public static final int SORT_ORDER_DATE = 4;
    public static final int SORT_ORDER_CONDITION = 5;
    
    public Double minPrice;
    public Double maxPrice;
    public int minCondition = FILTER_CONDITION_ANY;
    public int maxCondition = FILTER_CONDITION_ANY;
    public int minSellerRating = FILTER_SELLER_RATING_ANY;
    public int sort = SORT_ORDER_RATING;
    public boolean reverseSort = false;
}
