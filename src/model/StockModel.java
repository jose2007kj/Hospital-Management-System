/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author mary
 */
public class StockModel extends RecursiveTreeObject<StockModel>{ 
    public StringProperty itemName,itemPrice,itemQuantity,itemExpirey;
//   public int sup_id;

    public StockModel(String itemName,String itemPrice,String itemQuantity,String itemExpirey) {
        this.itemName=new SimpleStringProperty(itemName);
        this.itemPrice=new SimpleStringProperty(itemPrice);
        this.itemQuantity=new SimpleStringProperty(itemQuantity);
        this.itemExpirey=new SimpleStringProperty(itemExpirey);
        
        
    }

    public void setName(String itemName) {
        this.itemName=new SimpleStringProperty(itemName);
    }

    public void setPrice(String itemPrice) {
        this.itemPrice=new SimpleStringProperty(itemPrice);
    }

    public void setQuantity(String quantity) {
        this.itemQuantity=new SimpleStringProperty(quantity);
    }

    
    public String getName() {
       return itemName.get();
    }
    public String getPrice() {
       return itemPrice.get();
    }
    public String getQuantity() {
       return itemQuantity.get();
    }
    public String getExpiryDate() {
    return itemExpirey.get();
    }

    public void setExpiryDate(String expiryDate) {
    this.itemExpirey = new SimpleStringProperty(expiryDate);
    }
    
}
