package com.josephblough.alibris.data;

import org.json.JSONObject;

public class WorkSearchResult extends SearchResult {

    /**
     * 
	1	title	Bartleby and Benito Cereno	
	2	author	Melville, Herman, Professor	
	3	basic	Fiction / Literary # Fiction / Classics	Keywords, separated by #
	4	geo_code	New York	Locality of the work.  For example this story is set in New York.
	5	language	English	Language work is written in
	6	lc_subject	Fiction # New York # New York (State) # Wall Street # Psychological fiction	Library of Congress subject list.
	7	media_type	B=Book
				M=Music
				C=Classical Music
				V=Movie
	8	minprice	1.04	
	9	qty_avail	211
	10	synopsis		
	11	ttl	Not Used
	12	imageurl	URL	
	13	cover	Not Used
	14	work_id	575838	Work Id
     */
    
    public double minPrice;
    public String synopsis;
    
    public WorkSearchResult(JSONObject json) {
	super(json);
	this.minPrice = json.optDouble("minprice", 0.00);
	this.synopsis = json.optString("synopsis", "");
    }
}
