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
public class CunsultationModel extends RecursiveTreeObject<CunsultationModel> {
 public StringProperty consultationId;
public StringProperty patientId;
public StringProperty consultDate;
public StringProperty consultFee;
public StringProperty consultStatus;
public StringProperty disease;
public StringProperty duration;
public StringProperty medicinePrescribed;
public CunsultationModel(String patientId,String consultDate,String consultFee,String consultStatus,String disease,String duration,String medicinePrescribed) {
        this.patientId=new SimpleStringProperty(patientId);
        this.consultDate=new SimpleStringProperty(consultDate);
        this.consultFee=new SimpleStringProperty(consultFee);
        this.consultStatus=new SimpleStringProperty(consultStatus);
        this.disease=new SimpleStringProperty(disease);
        this.duration=new SimpleStringProperty(duration);
        this.medicinePrescribed=new SimpleStringProperty(medicinePrescribed);
        
        
        
    }
//public StringProperty getConsultationId() {
//return consultationId.get();
//}

//public void setConsultationId(String consultationId) {
//this.consultationId = new SimpleStringProperty(consultationId);
//}

public String getPatientId() {
return patientId.get();
}

public void setPatientId(String patientId) {
this.patientId = new SimpleStringProperty(patientId);
}

public String getConsultDate() {
return consultDate.get();
}

public void setConsultDate(String consultDate) {
this.consultDate = new SimpleStringProperty(consultDate);
}

public String getConsultFee() {
return consultFee.get();
}

public void setConsultFee(String consultFee) {
this.consultFee = new SimpleStringProperty(consultFee);
}

public String getConsultStatus() {
return consultStatus.get();
}

public void setConsultStatus(String consultStatus) {
this.consultStatus = new SimpleStringProperty(consultStatus);
}

public String getDisease() {
return disease.get();
}

public void setDisease(String disease) {
this.disease = new SimpleStringProperty(disease);
}

public String getDuration() {
return duration.get();
}

public void setDuration(String duration) {
this.duration = new SimpleStringProperty(duration);
}

public String getMedicinePrescribed() {
return medicinePrescribed.get();
}

public void setMedicinePrescribed(String medicinePrescribed) {
this.medicinePrescribed = new SimpleStringProperty(medicinePrescribed);
}   
}
