package edu.uoc.practica.bd.uocdb.exercise1;

import java.sql.Date;

public class Exercise1Row {

	private String wine_name;
	private String winery_name;
	private String pdo_name;
	private long vintage;
	private long prizes;
	private double price;
	private Date BestBeforeDate;
	
	public Exercise1Row(String wine_name,String winery_name,String pdo_name,long vintage,long prizes,double price,Date BestBeforeDate) {
		super();
		this.wine_name = wine_name;
		this.winery_name = winery_name;
		this.pdo_name = pdo_name;
		this.vintage = vintage;
		this.prizes = prizes;
		this.price = price;
		this.BestBeforeDate = BestBeforeDate;
	}
	/**
	 * @return the wine_name
	 */
	public String get_wine_name() {
		return wine_name;
	}
	/**
	 * @param the wine_name to set
	 */
	public void set_wine_name(String wine_name) {
		this.wine_name = wine_name;
	}
	/**
	 * @return the winery_name
	 */
	public String get_winery_name() {
		return winery_name;
	}
	/**
	 * @param the winery_name to set
	 */
	public void set_winery_name(String winery_name) {
		this.winery_name = winery_name;
	}
	/**
	 * @return the pdo_name
	 */
	public String get_pdo_name() {
		return pdo_name;
	}
	/**
	 * @param the pdo_name to set
	 */
	public void set_pdo_name(String pdo_name) {
		this.pdo_name = pdo_name;
	}
	/**
	 * @return the vintage
	 */
	public long get_vintage() {
		return vintage;
	}
	/**
	 * @param the vintage to set
	 */
	public void set_vintage(long vintage) {
		this.vintage = vintage;
	}
	/**
	 * @return the prizes
	 */
	public long get_prizes() {
		return prizes;
	}
	/**
	 * @param the prizes to set
	 */
	public void set_prizes(long prizes) {
		this.prizes = prizes;
	}
	/**
	 * @return the price
	 */
	public double get_price() {
		return price;
	}
	/**
	 * @param the price to set
	 */
	public void set_price(double price) {
		this.price = price;
	}
	/**
	 * @return the BestBeforeDate
	 */
	public Date get_BestBeforeDate() {
		return BestBeforeDate;
	}
	/**
	 * @param the BestBeforeDate to set
	 */
	public void set_BestBeforeDate(Date BestBeforeDate) {
		this.BestBeforeDate = BestBeforeDate;
	}
	
}
