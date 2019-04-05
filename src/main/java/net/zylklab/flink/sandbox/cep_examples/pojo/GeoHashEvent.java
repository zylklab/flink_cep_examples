package net.zylklab.flink.sandbox.cep_examples.pojo;

import java.io.Serializable;

public class GeoHashEvent implements Serializable {
	private static final long serialVersionUID = -4317766693494358699L;
	private String geohash;
	private int totalGPRSEvents;
	private int deltaGPRSEvents;
	private long timestamp;
	
	public GeoHashEvent(String geohash, int totalGPRSEvents, long timestamp) {
		super();
		this.geohash = geohash;
		this.totalGPRSEvents = totalGPRSEvents;
		this.timestamp = timestamp;
	}
	public int getTotalGPRSEvents() {
		return totalGPRSEvents;
	}
	public void setTotalGPRSEvents(int totalGPRSEvents) {
		this.totalGPRSEvents = totalGPRSEvents;
	}
	public String getGeohash() {
		return geohash;
	}
	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	public int getDeltaGPRSEvents() {
		return deltaGPRSEvents;
	}
	public void setDeltaGPRSEvents(int deltaGPRSEvents) {
		this.deltaGPRSEvents = deltaGPRSEvents;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString() {
		return "{"+
				"\"geohash\":\""+this.getGeohash()+"\","+
				"\"totalGPRSEvents\":"+this.getTotalGPRSEvents()+
				"\"timestamp\":"+this.getTimestamp()+","+
				"\"deltaGPRSEvents\":"+this.getDeltaGPRSEvents()+
				"}";
	}
}
