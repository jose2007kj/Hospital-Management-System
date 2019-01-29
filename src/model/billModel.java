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
public class billModel extends RecursiveTreeObject<billModel> {
private StringProperty billNo;
private StringProperty patientId;
private StringProperty billDate;
private StringProperty itemName;
private StringProperty qty;
private StringProperty cost;

public String getBillNo() {
return billNo.get();
}

public void setBillNo(String billNo) {
this.billNo = new SimpleStringProperty(billNo);
}

public String getPatientId() {
return patientId.get();
}

public void setPatientId(String patientId) {
this.patientId = new SimpleStringProperty(patientId);
}

public String getBillDate() {
return billDate.get();
}

public void setBillDate(String billDate) {
this.billDate = new SimpleStringProperty(billDate);
}

public String getItemName() {
return itemName.get();
}

public void setItemName(String itemName) {
this.itemName = new SimpleStringProperty(itemName);
}

public String getQty() {
return qty.get();
}

public void setQty(String qty) {
this.qty = new SimpleStringProperty(qty);
}

public String getCost() {
return cost.get();
}

public void setCost(String cost) {
this.cost = new SimpleStringProperty(cost);
} 
}
