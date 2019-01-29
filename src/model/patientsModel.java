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
public class patientsModel extends RecursiveTreeObject<patientsModel>{
    
   public StringProperty patientId;
public StringProperty patientName;
public StringProperty patientAge;
public StringProperty patientGender;
public StringProperty patientAddress;
public StringProperty patientContact;
public patientsModel(String patientName,String patientAge,String patientAddress,String patientGender,String patientContact) {
        this.patientName=new SimpleStringProperty(patientName);
        this.patientAge=new SimpleStringProperty(patientAge);
        this.patientGender=new SimpleStringProperty(patientGender);
        this.patientAddress=new SimpleStringProperty(patientAddress);
        this.patientContact=new SimpleStringProperty(patientContact);
        
        
    }
public String getPatientId() {
return patientId.get();
}

public void setPatientId(String patientId) {
this.patientId = new SimpleStringProperty(patientId);
}

public String getPatientName() {
return patientName.get();
}

public void setPatientName(String patientName) {
this.patientName =  new SimpleStringProperty(patientName);
}

public String getPatientAge() {
return patientAge.get();
}

public void setPatientAge(String patientAge) {
this.patientAge =  new SimpleStringProperty(patientAge);
}

public String getPatientGender() {
return patientGender.get();
}

public void setPatientGender(String patientGender) {
this.patientGender =  new SimpleStringProperty(patientGender);
}

public String getPatientAddress() {
return patientAddress.get();
}

public void setPatientAddress(String patientAddress) {
this.patientAddress =  new SimpleStringProperty(patientAddress);
}

public String getPatientContact() {
return patientContact.get();
}

public void setPatientContact(String patientContact) {
this.patientContact =  new SimpleStringProperty(patientContact);
}



    
}
