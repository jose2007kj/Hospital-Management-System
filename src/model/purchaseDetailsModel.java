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
public class purchaseDetailsModel extends RecursiveTreeObject<purchaseDetailsModel> {
private StringProperty purchaseId;
private StringProperty supName;
private StringProperty purchaseDate;
private StringProperty itemName;
private StringProperty expiryDate;
private StringProperty qty;
private StringProperty total;
//private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public purchaseDetailsModel(String supName,String purchaseDate,String itemName,String expiryDate,String qty,String total) {
        this.supName=new SimpleStringProperty(supName);
        this.purchaseDate=new SimpleStringProperty(purchaseDate);
        this.itemName=new SimpleStringProperty(itemName);
        this.expiryDate=new SimpleStringProperty(expiryDate);
        this.qty=new SimpleStringProperty(qty);
        this.total=new SimpleStringProperty(total);
        
        
        
    }
public String getPurchaseId() {
return purchaseId.get();
}

public void setPurchaseId(String purchaseId) {
this.purchaseId = new SimpleStringProperty(purchaseId);
}

public String getSupName() {
return supName.get();
}

public void setSupName(String supName) {
this.supName = new SimpleStringProperty(supName);
}

public String getPurchaseDate() {
return purchaseDate.get();
}

public void setPurchaseDate(String purchaseDate) {
this.purchaseDate = new SimpleStringProperty(purchaseDate);
}

public String getItemName() {
return itemName.get();
}

public void setItemName(String itemName) {
this.itemName = new SimpleStringProperty(itemName);
}

public String getExpiryDate() {
return expiryDate.get();
}

public void setExpiryDate(String expiryDate) {
this.expiryDate = new SimpleStringProperty(expiryDate);
}

public String getQty() {
return qty.get();
}

public void setQty(String qty) {
this.qty = new SimpleStringProperty(qty);
}

public String getTotal() {
return total.get();
}

public void setTotal(String total) {
this.total = new SimpleStringProperty(total);
}


}
