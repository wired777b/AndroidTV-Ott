package com.hiveview.domybox.service.entity;

public class WeatherInfo extends BaseEntity {

	private String hsienNo;
	private String date;
	private String weekDay;
	private String weather;
	private String temperature;
	private String windDir;
	private String wind;
	private String name;
	private String dayIcon;
	private String nightIcon;
	private String dayWallpaper;
	private String nightWallpaper;
	private String hsienName;

	private int type;
	private int id;
	public String getHsienNo() {
		return hsienNo;
	}
	public void setHsienNo(String hsienNo) {
		this.hsienNo = hsienNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWindDir() {
		return windDir;
	}
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDayIcon() {
		return dayIcon;
	}
	public void setDayIcon(String dayIcon) {
		this.dayIcon = dayIcon;
	}
	public String getNightIcon() {
		return nightIcon;
	}
	public void setNightIcon(String nightIcon) {
		this.nightIcon = nightIcon;
	}
	public String getDayWallpaper() {
		return dayWallpaper;
	}
	public void setDayWallpaper(String dayWallpaper) {
		this.dayWallpaper = dayWallpaper;
	}
	public String getNightWallpaper() {
		return nightWallpaper;
	}
	public void setNightWallpaper(String nightWallpaper) {
		this.nightWallpaper = nightWallpaper;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setHsienName(String hsienName) {
		this.hsienName = hsienName;
	}
	public String getHsienName() {
		return hsienName;
	}
	
}
