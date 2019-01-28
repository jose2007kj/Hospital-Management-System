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
public class supplierModel extends RecursiveTreeObject<supplierModel>{
    
   public StringProperty supName,supAddress,branch,contact;
   public int sup_id;

    public supplierModel(String supName,String supAddress,String branch, String contact) {
        this.supName=new SimpleStringProperty(supName);
        this.supAddress=new SimpleStringProperty(supAddress);
        this.branch=new SimpleStringProperty(branch);
        this.contact=new SimpleStringProperty(contact);
//        this.sup_id=sup_id;
        
        
    }

    public void setName(String supName) {
        this.supName=new SimpleStringProperty(supName);
    }

    public void setAddress(String supAddress) {
        this.supAddress=new SimpleStringProperty(supAddress);
    }

    public void setBranch(String branch) {
        this.branch=new SimpleStringProperty(branch);
    }

    public void setContact(String contact) {
        this.contact=new SimpleStringProperty(contact);
        
    }
    

    public void setsupId(int sup_id) {
        this.sup_id=sup_id;
    }

    
    public String getName() {
       return supName.get();
    }
    public String getAddress() {
       return supAddress.get();
    }
    public String getBranch() {
       return branch.get();
    }
    public int getSupId(){
        return this.sup_id;
    }
    public String getContact(){
        return contact.get();
    }

    
    
   
    
}
