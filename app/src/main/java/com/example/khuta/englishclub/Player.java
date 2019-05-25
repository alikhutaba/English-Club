package com.example.khuta.englishclub;


import android.support.annotation.NonNull;
import android.util.Log;


public class Player implements Comparable<Player>{

    private String UID;
    private String email;
    private String password;
    private String 	name;
    private String city;
    private double latitude;
    private double longitude;
    private  int Points;


    public Player(){

    }

    public Player(String UID ,  String email , String password, String name, int Points ,double latitude, double longitude , String city) {

        setUID(UID);
        setEmail(email);
        setPassword(password);
        setName(name);
        setPoints(Points);
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);

    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        this.Points = points;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    @Override
    public int compareTo(@NonNull Player another) {
        if (this.getPoints() < another.getPoints()){
            return 1;
        }else{
            return -1;
        }
    }


    @Override
    public String toString() {

        String string = String.format(" %s %20s %20s" , getName(), getCity() , getPoints());
        Log.d("Home" , "Player toString "+string);
        return string;

        //return getName()+"          "+getPoints()+"\n"+latitude+" || "+longitude;

    }
}
