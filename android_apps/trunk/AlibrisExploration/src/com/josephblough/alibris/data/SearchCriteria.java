package com.josephblough.alibris.data;

public class SearchCriteria {

    public static final int SEARCH_AUTHOR_INDEX = 0;
    public static final int SEARCH_TITLE_INDEX = 1;
    public static final int SEARCH_TOPIC_INDEX = 2;
    public static final int SEARCH_ALL_INDEX = 3;

    public static final int SEARCH_MEDIA_BOOKS_INDEX = 0;
    public static final int SEARCH_MEDIA_MUSIC_INDEX = 1;
    public static final int SEARCH_MEDIA_MOVIES_INDEX = 2;
    public static final int SEARCH_MEDIA_ALL_INDEX = 3;
    
    public static final int SORT_ORDER_RATING_INDEX = 0;
    public static final int SORT_ORDER_TITLE_INDEX = 1;
    public static final int SORT_ORDER_AUTHOR_INDEX = 2;
    public static final int SORT_ORDER_PRICE_INDEX = 3;
    public static final int SORT_ORDER_DATE_INDEX = 4;
    
    
    /*
     * Pieces of information that can be searched on:
     * 	which field : author, title, topic, or all
     * 	search parameter : string to search on
     */
    
    public String searchTerm;
    public Integer field;
    public Integer media;
    public Integer sort;
    public Boolean reverseSort;
    
    public SearchCriteria(final String searchTerm, final int field) {
	this.searchTerm = searchTerm;
	this.field = field;
    }
    
    public SearchCriteria(final String searchTerm, final int field, final int media, final int sort) {
	this.searchTerm = searchTerm;
	this.field = field;
	this.media = media;
	this.sort = sort;
    }
    
    public SearchCriteria(final String searchTerm, final int field, final int media, final int sort, final boolean reverseSort) {
	this.searchTerm = searchTerm;
	this.field = field;
	this.media = media;
	this.sort = sort;
	this.reverseSort = reverseSort;
    }
    
    public String toJson() {
	return null;
    }
}
