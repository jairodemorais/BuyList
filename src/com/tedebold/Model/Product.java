package com.tedebold.Model;

public class Product {
  private int id;
  private String barcode;
  private String name;

  public Product(int i, String b, String n){
    this.id = i;
    this.barcode = b;
    this.name = n;
  }

  public int getId(){
    return this.id;
  }

  public void setId(int i) {
    this.id = i;
  }

  public void setName(String n) {
    this.name = n;
  }

  public String getName() {
    return this.name;
  }

  public void setBarcode(String b) {
    this.barcode = b;
  }

  public String getBarcode() {
    return this.barcode;
  }
}
