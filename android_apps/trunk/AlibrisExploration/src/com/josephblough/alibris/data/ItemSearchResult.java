package com.josephblough.alibris.data;

import org.json.JSONObject;

public class ItemSearchResult extends SearchResult {

    /**
	1	title	Art Forms in Nature	Title
	2	author	Haeckel, Ernst Heinrich Philip	Author
	3	bin	10284207844	10 digits only	Alibris SKU
	4	binding	Trade paperback	Binding type - filled in by the seller during upload.
	5	comments	Trade paperback (US). Glued binding. 100 p. Dover Pictorial Archives. Audience: General/trade.	Seller's comments
	6	condition	Good. No dust jacket as issued.	Seller's description of condition.
	7	cost	8	USD$	Partner cost
	8	date_pub	1974	Multiple date formats supported	Publication date
	9	edition	Rev ed	Edition as stated by seller
	10	fl_bookcond	3	6=New
					5=Fine / like new
					4=Very Good
					3=Good
					2=Fair
					1=Poor	Item condition
	11	fl_cover	S	S = Softcover
					H=Hardcover	This is the code used to denote the binding.  It can be added by the seller, or inferred from the binding field.
	12	fl_dustjacket	1	field not present:  No Dustjacket present 1 Dustjacket present	Flag: dustjacket present
	13	fl_edition	1	Field not present: Not a first edition 1 First edition	Flag: first edition
	14	fl_jacketcond	6	6=New
					5=Fine / like new
					4=Very Good
					3=Good
					2=Fair
					1=Poor	Condition of jacket
	15	fl_signed	1	Flag: signed by author
	16	illus	Sendak, Maurice	Often unreliable and can have editor or "This is illustrated!" in the field.
	17	imageurl	http://images.alibris.com/isbn/9780486229874.gif	URL	url for image
	18	isbn	9.78049E+12	13 digits	ISBN
	19	media_type	B=Book
				M=Music
				C=Classical Music
				V=Movie
	20	keywords	BIOLOGY|BIOLOGY|ART|ART FORMS	Keywords
	21	notes	Seller notes
	22	place_pub	Mineola, New York, U.S.A.	Publication location
	23	price	8.4	USD$	Customer price (retail)
	24	publisher	Dover Publications	Publisher name
	25	qty_avail	1	Integer	Quantity available
	26	reliability	5	6, 5, 4, 3, 2, 1	Seller reliability rating
	27	shiploc	DOM	DOM=ships from US
				INT=ships from location outside US
				STOCK=ships from Alibris' Nevada warehouse
				STOCKNEW=new book ships from Ingram	"Ships-from" code. 
	28	sellercity	Los Angeles	Seller's city
	29	sellerstate	NV	Seller
	30	sellercountry	USA	Seller's country
	31	sellername	Pico Books	Seller's name
	32	sellerid 	Not used	Not used
	33	shipping	3	Integer	Seller's supported ship methods (bit flag)
	34	work_id	427531	Integer	Alibris Work ID
     * 
     */
    
    public String sku; 	// bin
    public String seller;	// sellername
    public String condition; 	// condition
    public double price;	// price
    public String binding;	// binding
    public String publisher;	// publisher
    public String notes;	// notes
    public String sellerReliability; // reliability
    public String sellerCity;	// sellercity
    public String sellerState;	// sellerstate
    public String sellerCountry;// sellercountry
    
    public ItemSearchResult(final JSONObject json) {
	super(json);
	this.sku = json.optString("bin", "");
	this.seller = json.optString("sellername", "");
	this.condition = json.optString("condition", "");
	this.price = json.optDouble("price", 0.0);
	this.binding = json.optString("binding", "");
	this.publisher = json.optString("publisher", "");
	this.notes = json.optString("notes", "");
	this.sellerReliability = json.optString("sellerReliability", "");
	this.sellerCity = json.optString("sellerCity", "");
	this.sellerState = json.optString("sellerState", "");
	this.sellerCountry = json.optString("sellerCountry", "");
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((binding == null) ? 0 : binding.hashCode());
	result = prime * result
		+ ((condition == null) ? 0 : condition.hashCode());
	result = prime * result + ((notes == null) ? 0 : notes.hashCode());
	long temp;
	temp = Double.doubleToLongBits(price);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result
		+ ((publisher == null) ? 0 : publisher.hashCode());
	result = prime * result + ((seller == null) ? 0 : seller.hashCode());
	result = prime * result
		+ ((sellerCity == null) ? 0 : sellerCity.hashCode());
	result = prime * result
		+ ((sellerCountry == null) ? 0 : sellerCountry.hashCode());
	result = prime
		* result
		+ ((sellerReliability == null) ? 0 : sellerReliability
			.hashCode());
	result = prime * result
		+ ((sellerState == null) ? 0 : sellerState.hashCode());
	result = prime * result + ((sku == null) ? 0 : sku.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ItemSearchResult other = (ItemSearchResult) obj;
	if (binding == null) {
	    if (other.binding != null)
		return false;
	} else if (!binding.equals(other.binding))
	    return false;
	if (condition == null) {
	    if (other.condition != null)
		return false;
	} else if (!condition.equals(other.condition))
	    return false;
	if (notes == null) {
	    if (other.notes != null)
		return false;
	} else if (!notes.equals(other.notes))
	    return false;
	if (Double.doubleToLongBits(price) != Double
		.doubleToLongBits(other.price))
	    return false;
	if (publisher == null) {
	    if (other.publisher != null)
		return false;
	} else if (!publisher.equals(other.publisher))
	    return false;
	if (seller == null) {
	    if (other.seller != null)
		return false;
	} else if (!seller.equals(other.seller))
	    return false;
	if (sellerCity == null) {
	    if (other.sellerCity != null)
		return false;
	} else if (!sellerCity.equals(other.sellerCity))
	    return false;
	if (sellerCountry == null) {
	    if (other.sellerCountry != null)
		return false;
	} else if (!sellerCountry.equals(other.sellerCountry))
	    return false;
	if (sellerReliability == null) {
	    if (other.sellerReliability != null)
		return false;
	} else if (!sellerReliability.equals(other.sellerReliability))
	    return false;
	if (sellerState == null) {
	    if (other.sellerState != null)
		return false;
	} else if (!sellerState.equals(other.sellerState))
	    return false;
	if (sku == null) {
	    if (other.sku != null)
		return false;
	} else if (!sku.equals(other.sku))
	    return false;
	return true;
    }
}
