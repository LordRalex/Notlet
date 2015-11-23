package net.ae97.notlet;

import java.io.Serializable;

/**
 * Created by john on 11/22/15.
 */
public class Location implements Serializable{

    double x;
    double y;

    public Location(){
        this.x = 0;
        this.y = 0;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public double getX() {return x;}
    public double getY() {return y;}

}
