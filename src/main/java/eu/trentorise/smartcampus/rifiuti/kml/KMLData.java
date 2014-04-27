package eu.trentorise.smartcampus.rifiuti.kml;

import java.util.HashMap;
import java.util.Map;

public class KMLData {

	private String name;
	private double lat;
	private double lon;
	
	private Map<Integer,String> attributes = new HashMap<Integer, String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public Map<Integer, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<Integer, String> attributes) {
		this.attributes = attributes;
	}

}
