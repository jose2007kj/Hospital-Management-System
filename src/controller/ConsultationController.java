/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Main.Signin;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import static controller.SupplierWindowController.showError;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.CunsultationModel;
import model.patientsModel;

/**
 *
 * @author mary
 */
public class ConsultationController implements Initializable {

    @FXML
    private JFXTreeTableView<CunsultationModel> tableView;
    ObservableList<CunsultationModel> CunsultationList;
    ObservableList<patientsModel> patientList=FXCollections.observableArrayList();
    ObservableList<String> pId=FXCollections.observableArrayList();
    JFXDatePicker consultationDate;//,expiryDate;
    @FXML
    private JFXTextField searchTF;
    @FXML // for combo box
    private ComboBox<String> patientIdCB;//,itemNameCB;
    @FXML
    private JFXTextField consultFeeTF,consultStatusTF,diseaseTF,durationTF,medicinePescribedTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane;

    @FXML
    private Label patientIdLabel,consultationDateLabel,consultFeeLabel,consultStatusLabel,diseaseLabel,durationLabel,medicinePescribedLabel;//,supplierNameLabel,itemNameLabel,consultationDateLabel,expiryDateLabel,quantityLabel,totalLabel;

    String patientID,patientID1,consultationDate1,consultFee,consultStatus,disease,duration,medicinePescribed;
    Date today =new Date();
    private static Connection conn = null;
    private static PreparedStatement stat = null,pStat=null;
    private static String url = "jdbc:mysql://localhost:3306";
    private static String Password = "";
    private static String username = "mary";
    private static String sqlInsert;
    ResultSet result,pResult;

    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error ");
        alert.setHeaderText("there is an error happened !");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    Signin signin;
    Stage stage;

    public void main(Signin signin, Stage stage) {
        this.signin = signin;
        this.stage = stage;
    }

    public  RequiredFieldValidator validator(String msg){
        RequiredFieldValidator validator=new RequiredFieldValidator();
        validator.setOpacity(0.5);
        validator.setMessage(msg);         
        return validator;
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        consultFeeTF.getValidators().add(validator("Input is required"));
        consultFeeTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    consultFeeTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
//        durationTF.getValidators().add(validator("Input is required"));
        consultStatusTF.getValidators().add(validator("Input is required"));
//        medicinePescribedTF.getValidators().add(validator("Input is required"));
//	diseaseTF.getValidators().add(validator("Input is required"));
//
        consultationDate = new JFXDatePicker();
        consultationDate.setPrefWidth(240);
        consultationDate.setPrefHeight(41);
        consultationDate.setValue(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        InsertGridPane.add(consultationDate, 1, 4);
        
        fechPatientId();
        
        
        //code for onclick combobox
        patientIdCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                patientID1=newName;
                if(newName.contains(".")){
                    try
                        {
                            
                            String[] temp=newName.split("\\.");
                            System.out.print("temp"+temp.length);
                            patientID=temp[0];
                        }catch(Exception e){
                            System.out.print("temp"+e.toString());
                        }
                
                }else{
                patientID="";
                }
                
                    

            }
        });
       
        //code for onclick combobox
        JFXTreeTableColumn<CunsultationModel, String> PId = new JFXTreeTableColumn<>("Patient ID");

        PId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().patientId;

            }
        });

        JFXTreeTableColumn<CunsultationModel, String> CDate = new JFXTreeTableColumn<>("Consultation Date");

        CDate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().consultDate;

            }
        });

        JFXTreeTableColumn<CunsultationModel, String> CFee = new JFXTreeTableColumn<>("Consulation Fee");

        CFee.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().consultFee;

            }
        });

        JFXTreeTableColumn<CunsultationModel, String> CStatus = new JFXTreeTableColumn<>("Consultation Status");

        CStatus.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().consultStatus;

            }
        });
        JFXTreeTableColumn<CunsultationModel, String> Disease = new JFXTreeTableColumn<>("Disease");

        Disease.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().disease;

            }
        });

        JFXTreeTableColumn<CunsultationModel, String> Duration = new JFXTreeTableColumn<>("Duration");

        Duration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().duration;

            }
        });
        JFXTreeTableColumn<CunsultationModel, String> MPrescribed = new JFXTreeTableColumn<>("Medicine Prescribed");

        MPrescribed.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CunsultationModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CunsultationModel, String> param) {
                return param.getValue().getValue().medicinePrescribed;

            }
        });


        CunsultationList = FXCollections.observableArrayList();
        addrowsToTable();

        TreeItem<CunsultationModel> root = new RecursiveTreeItem<CunsultationModel>(CunsultationList, RecursiveTreeObject::getChildren);
        tableView.getColumns().addAll(PId, CDate, CFee, CStatus,Disease,Duration,MPrescribed);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<CunsultationModel>>() {

                    @Override
                    public boolean test(TreeItem<CunsultationModel> t) {

                        boolean flag = t.getValue().patientId.getValue().contains(newValue)
                                || t.getValue().consultDate.getValue().contains(newValue)
                                || t.getValue().consultFee.getValue().contains(newValue)
                                || t.getValue().consultStatus.getValue().contains(newValue)
                                || t.getValue().duration.getValue().contains(newValue)
                                || t.getValue().medicinePrescribed.getValue().contains(newValue)
                                || t.getValue().disease.getValue().contains(newValue);
                        return flag;

                    }
                });
            }

        });

        tableView.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue)
                -> 
                showDetails(newValue)
        );
    }
    void fechPatientId() {

        String sqlSelect = "select * from DrJayaramHomeoClinic.Patient ";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();
            
            while (result.next()) {
                System.out.print("sql statement"+result.getString(2));
                patientList.add(new patientsModel(result.getString(1).toString(),result.getString(2), result.getString(3),result.getString(5), result.getString(4),result.getString(6)));
                pId.add(result.getString(1).toString()+"."+result.getString(2).toString());
            }
            patientIdCB.setItems(pId);
            
        } catch (SQLException r) {
            showError(r.getMessage());
        } catch (ClassNotFoundException n) {
            showError(n.getMessage());
        } catch (NullPointerException l) {
            showError(l.getMessage());
        } finally {
            try {
                conn.close();
                stat.close();
            } catch (SQLException rr) {
                showError(rr.getMessage());
            }
        }

    }
   
    public void showDetails(TreeItem<CunsultationModel> pModel) {
         System.out.print("pModel.getValue().getPatientId()"+pModel.getValue().getPatientId());
        consultationDate.setValue(LocalDate.parse(pModel.getValue().getConsultDate()));
        
        if(pModel.getValue().getPatientId().contains(".")){
            try
            {
                String ntemp=pModel.getValue().getPatientId();
                String[] temp=ntemp.split("\\.");
                System.out.print("temp"+temp.length);
                patientID=temp[0];
            }catch(Exception e){
                System.out.print("temp"+e.toString());
            }
        }
        patientIdCB.setValue(pModel.getValue().getPatientId());
        patientIdLabel.setText(pModel.getValue().getPatientId());
        consultFeeLabel.setText(pModel.getValue().getConsultFee());
        consultationDateLabel.setText(pModel.getValue().getConsultDate());
        durationLabel.setText(pModel.getValue().getDuration());
        diseaseLabel.setText(pModel.getValue().getDisease());
        medicinePescribedLabel.setText(pModel.getValue().getMedicinePrescribed());
        medicinePescribedTF.setText(pModel.getValue().getMedicinePrescribed());
        diseaseTF.setText(pModel.getValue().getDisease());
        durationTF.setText(pModel.getValue().getDuration());
        consultFeeTF.setText(pModel.getValue().getConsultFee());
        consultStatusTF.setText(pModel.getValue().getConsultStatus());
        consultStatusLabel.setText(pModel.getValue().getConsultStatus());
//        patientID=pModel.getValue().getPatientId();
        consultationDate1=pModel.getValue().getConsultDate();
        consultFee=pModel.getValue().getConsultFee();
        consultStatus=pModel.getValue().getConsultStatus();
        disease=pModel.getValue().getDisease();
        duration=pModel.getValue().getDuration();
        medicinePescribed=pModel.getValue().getMedicinePrescribed();
//        Edate= pModel.getValue().getExpiryDate();
//        Iquantity = pModel.getValue().getQty();
//        Pdate = pModel.getValue().getPurchaseDate();
//        Itotal= pModel.getValue().getTotal();
//        selectedSup1=pModel.getValue().getSupName();
//        selectedItem1=pModel.getValue().getItemName();
        
       
    }

    private static void insert(int patientsID,String consultDate, int fee, String status, String disease,String duration,String medicine) {
        try {

            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(patient_id,consult_date,consult_fee,consult_status,disease,duration,medicine_prescribed) VALUES (?,?,?,?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setInt(1, patientsID);
            stat.setString(2, consultDate);
            stat.setInt(3, fee);
            stat.setString(4, status);
            stat.setString(5, disease);
            stat.setString(6, duration);
            stat.setString(7, medicine);
            
            

            stat.executeUpdate();

        } catch (SQLException ex) {
            showError(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showError(ex.getMessage());

        } catch (NumberFormatException c) {
            showError(c.getMessage());

        } catch (NullPointerException cc) {
            showError(cc.getMessage());

        } catch (Error e) {
            showError(e.getMessage());
        } catch (Exception f) {
            showError(f.getMessage());

        } finally {
            try {
                conn.close();
                stat.close();
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }
    }

    @FXML
    void insertPatientData(ActionEvent event) {
   if ( consultFeeTF.getText().isEmpty()||consultStatusTF.getText().isEmpty()||diseaseTF.getText().isEmpty()||durationTF.getText().isEmpty()||medicinePescribedTF.getText().isEmpty()) {

                consultFeeTF.validate();
                consultStatusTF.validate();
                diseaseTF.validate();
                durationTF.validate();
                medicinePescribedTF.validate();
//                
            }else if(patientID=="patient"||patientID==null){
                showError("please select a patient");
            }else{
        try {
            
            insert(Integer.parseInt(patientID),consultationDate.getValue().toString(),Integer.parseInt(consultFeeTF.getText()),consultStatusTF.getText(),diseaseTF.getText(),durationTF.getText(),medicinePescribedTF.getText());
            CunsultationList.add(new CunsultationModel(patientID1,consultationDate.getValue().toString(),consultFeeTF.getText(),consultStatusTF.getText(),diseaseTF.getText(),durationTF.getText(),medicinePescribedTF.getText()));
       
        }
        catch (NullPointerException cc) {
            showError("Please , All inputs are requires");

        } 
        catch (NumberFormatException c) {
            showError("Age must be number");
            
        } catch (Error e) {
            showError(e.getMessage());
        } catch (Exception f) {
            showError(f.getMessage());

        }
    }}

    void addrowsToTable() {

        String sqlSelect = "select * from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " ";
        String temp="";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                System.out.print("result.getString(2)"+result.getString(2));
                String sqlfetchPatients = "select * from DrJayaramHomeoClinic.Patient where patient_id='"+result.getInt(2)+"'";
                pStat = conn.prepareStatement(sqlfetchPatients);
                pResult=pStat.executeQuery();
                while(pResult.next()){
                   temp=pResult.getString(1).toString()+"."+pResult.getString(2).toString(); 
                }

                System.out.print("result.getString(3)"+result.getString(3));
                System.out.print("result.getString(4)"+result.getString(4));
                System.out.print("result.getString(5)"+result.getString(5));
                CunsultationList.add(new CunsultationModel(temp,result.getString(3),result.getString(4)+"",result.getString(5), result.getString(6),result.getString(7),result.getString(8)));

            }
        } catch (SQLException r) {
            showError(r.getMessage());
        } catch (ClassNotFoundException n) {
            showError(n.getMessage());
        } catch (NullPointerException l) {
            showError(l.getMessage());
        } finally {
            try {
                conn.close();
                stat.close();
            } catch (SQLException rr) {
                showError(rr.getMessage());
            }
        }

    }

    @FXML
    void delelePatientRow(ActionEvent event) {
        try {
            int index = tableView.getSelectionModel().getSelectedIndex();
//            CunsultationList.remove(index);
//            System.out.print("delete"+CunsultationList.get(index).getSupName());
            String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " where patient_id='" + Integer.parseInt(patientID)+ "' and" + " consult_date='" + CunsultationList.get(index).getConsultDate() + "' and"
                + " consult_fee='" + Integer.parseInt(CunsultationList.get(index).getConsultFee()) + "' and" + " medicine_prescribed='" + CunsultationList.get(index).getMedicinePrescribed()+ "' and" + " consult_status='" + CunsultationList.get(index).getConsultStatus() + "' and" + " disease='" + CunsultationList.get(index).getDisease() + "' and" 
                + " duration='" + CunsultationList.get(index).getDuration() +  "'";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);
            stat.executeUpdate();
            CunsultationList.remove(index);
            clear();
        } catch (SQLException r) {
            showError(r.getMessage());
        } catch (ClassNotFoundException n) {
            showError(n.getMessage());
        } catch (NullPointerException l) {
            showError(l.getMessage());
        } finally {
            try {
                conn.close();
                stat.close();
            } catch (SQLException rr) {
                showError(rr.getMessage());
            }
        }

    }

    @FXML
    void updatePatientRow(ActionEvent event) {

        int index = tableView.getSelectionModel().getSelectedIndex();
        TreeItem<CunsultationModel> pModel = tableView.getSelectionModel().getSelectedItem();

        String sqlUpdat = "UPDATE  DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " SET patient_id='" + Integer.parseInt(patientID) + "' ,consult_date='" + consultationDate.getValue().toString() + "' , "
                + " consult_fee='" + Integer.parseInt(consultFeeTF.getText())+ "', consult_status='" + consultStatusTF.getText() +"',disease='" + diseaseTF.getText() + "',duration='" + durationTF.getText() + "',"
                + "medicine_prescribed='" + medicinePescribedTF.getText() + "' "
                + " WHERE patient_id='" + Integer.parseInt(patientID)+ "' and" + " consult_date='" + consultationDate1 + "' and"
                + " consult_fee='" + Integer.parseInt(consultFee) + "' and" + " consult_status='" + consultStatus + "' and" + " disease='" + disease + "' and" 
                + " duration='" + duration + "' and" + " medicine_prescribed='" + medicinePescribed +  "'";
        if ( consultFeeTF.getText().isEmpty()||consultStatusTF.getText().isEmpty()||diseaseTF.getText().isEmpty()||durationTF.getText().isEmpty()||medicinePescribedTF.getText().isEmpty()) {

                consultFeeTF.validate();
                consultStatusTF.validate();
                diseaseTF.validate();
                durationTF.validate();
                medicinePescribedTF.validate();
//                
            }else if(patientID=="patient"||patientID==null){
                showError("please select a patient");
            }else{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.print("Sname"+sqlUpdat);
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlUpdat);
            stat.executeUpdate();
            CunsultationModel PatientModel = new CunsultationModel(patientID1,consultationDate.getValue().toString(),consultFeeTF.getText(),consultStatusTF.getText(),diseaseTF.getText(),durationTF.getText(),medicinePescribedTF.getText());
        pModel.setValue(PatientModel);

        } catch (SQLException e) {
            showError(e.getMessage());
        } catch (ClassNotFoundException n) {

            showError(n.getMessage());

        } catch (NumberFormatException f) {
            showError(f.getMessage());

        } catch (NullPointerException l) {
            showError(l.getMessage());

        } finally {
            try {
                conn.close();
                stat.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }}
    }

    @FXML
    void clearFields(ActionEvent event) {
        clear();

    }

    public void clear() {

        patientIdCB.setValue("patient");
        consultStatusTF.setText(null);
        consultFeeTF.setText(null);
        durationTF.setText(null);
        diseaseTF.setText(null);
        medicinePescribedTF.setText(null);
        consultationDate.setValue(null);
        
    }

    @FXML
    void deleteAll(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confimation");

        alert.setHeaderText("Confirmation");
        alert.setContentText("Make sure that You Will delete all patient data ... ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                CunsultationList.removeAll(CunsultationList);
                String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName;
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, username, Password);
                stat = conn.prepareStatement(sqlSelect);
                stat.executeUpdate();
                clear();
            } catch (SQLException r) {
                showError(r.getMessage());
            } catch (ClassNotFoundException n) {
                showError(n.getMessage());
            } catch (NullPointerException l) {
                showError(l.getMessage());
            } finally {
                try {
                    conn.close();
                    stat.close();
                } catch (SQLException rr) {
                    showError(rr.getMessage());
                }
            }
        } else if (result.get() == ButtonType.CANCEL) {
            alert.close();
        }

    }

    @FXML
    void back(ActionEvent event) {
        signin.clinicsWindow();
        signin.SubClinicWindowClose();
    }

}
 