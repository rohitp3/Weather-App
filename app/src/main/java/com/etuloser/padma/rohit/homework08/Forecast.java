package com.etuloser.padma.rohit.homework08;

/**
 * Created by Rohit on 4/8/2017.
 */

public class Forecast {

    String Headlines,city,country;

    String locationId;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    String date;
    String mintemp;
    String maxtemp;
    String dayphrase;
    String nightphrase;
    String firsturl;
    String secondurl;
    String dayimg;
    String nightimg;

    public Forecast() {
    }

    public String getHeadlines() {
        return Headlines;
    }

    public void setHeadlines(String headlines) {
        Headlines = headlines;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Forecast(String headlines, String city, String country, String date, String mintemp, String maxtemp, String dayphrase, String nightphrase, String firsturl, String secondurl, String dayimg, String nightimg) {
        Headlines = headlines;
        this.city = city;
        this.country = country;
        this.date = date;
        this.mintemp = mintemp;
        this.maxtemp = maxtemp;
        this.dayphrase = dayphrase;
        this.nightphrase = nightphrase;
        this.firsturl = firsturl;
        this.secondurl = secondurl;
        this.dayimg = dayimg;
        this.nightimg = nightimg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMintemp() {
        return mintemp;
    }

    public void setMintemp(String mintemp) {
        this.mintemp = mintemp;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp) {
        this.maxtemp = maxtemp;
    }

    public String getDayphrase() {
        return dayphrase;
    }

    public void setDayphrase(String dayphrase) {
        this.dayphrase = dayphrase;
    }

    public String getNightphrase() {
        return nightphrase;
    }

    public void setNightphrase(String nightphrase) {
        this.nightphrase = nightphrase;
    }

    public String getFirsturl() {
        return firsturl;
    }

    public void setFirsturl(String firsturl) {
        this.firsturl = firsturl;
    }

    public String getSecondurl() {
        return secondurl;
    }

    public void setSecondurl(String secondurl) {
        this.secondurl = secondurl;
    }

    public String getDayimg() {
        return dayimg;
    }

    public void setDayimg(String dayimg) {
        this.dayimg = dayimg;
    }

    public String getNightimg() {
        return nightimg;
    }

    public void setNightimg(String nightimg) {
        this.nightimg = nightimg;
    }
}
