package com.josephblough.alibris.data;

public class SearchCriteria {

    /*
     * Pieces of information that can be searched on:
     * 	which field : author, title, topic, or all
     * 	search parameter : string to search on
     */
    
    public String parameter;
    public String searchTerm;
    
    public SearchCriteria(final String parameter, final String searchTerm) {
	this.parameter = parameter;
	this.searchTerm = searchTerm;
    }
    
    public String toJson() {
	return null;
    }
}
