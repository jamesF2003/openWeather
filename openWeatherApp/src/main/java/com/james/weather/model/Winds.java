package com.james.weather.model;

import java.io.Serializable;

public class Winds implements Serializable {

  private String direction;
  private String speed;



  public Winds(String direction, String speed) {
    this.direction = direction;
    this.speed = speed;
  }



  public String getDirection() {
    return direction;
  }



  public void setDirection(String direction) {
    this.direction = direction;
  }



  public String getSpeed() {
    return speed;
  }



  public void setSpeed(String speed) {
    this.speed = speed;
  }



  @Override
  public String toString() {
    return "Winds [direction=" + direction + ", speed=" + speed + "]";
  }

}
