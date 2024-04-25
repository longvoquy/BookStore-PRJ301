/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {

    int id;
    String name;
    String imageUrl;
    int price;
    String tiltle;
    String description;
    int sell_ID;
    int categoryId;
    int quantityP;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTiltle() {
        return tiltle;
    }

    public void setTiltle(String tiltle) {
        this.tiltle = tiltle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSell_ID() {
        return sell_ID;
    }

    public void setSell_ID(int sell_ID) {
        this.sell_ID = sell_ID;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getQuantityP() {
        return quantityP;
    }

    public void setQuantityP(int quantityP) {
        this.quantityP = quantityP;
    }

    public Product(int id, String name, String imageUrl, int price, String tiltle, String description, int sell_ID, int categoryId, int quantityP) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.tiltle = tiltle;
        this.description = description;
        this.sell_ID = sell_ID;
        this.categoryId = categoryId;
        this.quantityP = quantityP;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String formatPrice() {
        // Tạo một đối tượng NumberFormat với Locale của Việt Nam để định dạng tiền tệ theo đúng quy ước
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(this.price);
    }
}
