/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Main.Signin;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.Console;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.supplierModel;

/**
 *
 * @author mary
 */
public class SupplierWindowController implements Initializable {

    @FXML
    private JFXTreeTableView<supplierModel> tableView;
    ObservableList<supplierModel> supplierList;
//    JFXDatePicker datePicker;
    @FXML
    private JFXTextField searchTF;

    @FXML
    private JFXTextField supNameTF,supAddressTF,branchTF,sup_idTF,contactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane;

    @FXML
    private Label supNameLabel,supAddressLabel,branchLabel,sup_idLabel,contactLabel;

    String Sname, Saddress, Sbranch, Sid, Scontact;

    private static Connection conn = null;
    private static PreparedStatement stat = null;
    private static String url = "jdbc:mysql://localhost:3306";
    private static String Password = "";
    private static String username = "mary";
    private static String sqlInsert;
    ResultSet result;

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
        
        supNameTF.getValidators().add(validator("Valid Input is required"));
        supAddressTF.getValidators().add(validator("Valid Input is required"));
        branchTF.getValidators().add(validator("Valid Input is required"));
        contactTF.getValidators().add(validator("Valid 10 digit number is required"));
        
        contactTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    contactTF.setText(newValue.replaceAll("[^\\d]", ""));
                    
                }
                if(newValue.length()!=10){
                    contactTF.validate();}
            }
        });
//        treatmentTF.getValidators().add(validator("Input is required"));
//
//        datePicker = new JFXDatePicker();
//        datePicker.setPrefWidth(240);
//        datePicker.setPrefHeight(41);
//
//        InsertGridPane.add(datePicker, 1, 3);

        JFXTreeTableColumn<supplierModel, String> SNcolumn = new JFXTreeTableColumn<>("Supplier Name");

        SNcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<supplierModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<supplierModel, String> param) {
                return param.getValue().getValue().supName;

            }
        });

        JFXTreeTableColumn<supplierModel, String> SAcolumn = new JFXTreeTableColumn<>(" Address");

        SAcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<supplierModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<supplierModel, String> param) {
                return param.getValue().getValue().supAddress;

            }
        });

        JFXTreeTableColumn<supplierModel, String> Branchcolumn = new JFXTreeTableColumn<>("Branch");

        Branchcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<supplierModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<supplierModel, String> param) {
                return param.getValue().getValue().branch;

            }
        });

        JFXTreeTableColumn<supplierModel, String> Contactcolumn = new JFXTreeTableColumn<>("Contact");

        Contactcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<supplierModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<supplierModel, String> param) {
                return param.getValue().getValue().contact;

            }
        });

//        JFXTreeTableColumn<patientModel, String> Diagnosiscolumn = new JFXTreeTableColumn<>("Diagnosis");
//
//        Diagnosiscolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientModel, String> param) {
//                return param.getValue().getValue().diagnosis;
//
//            }
//        });
//
//        JFXTreeTableColumn<patientModel, String> Teatmentcolumn = new JFXTreeTableColumn<>("Treatment");
//        Teatmentcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientModel, String> param) {
//                return param.getValue().getValue().treatment;
//
//            }
//        });

        supplierList = FXCollections.observableArrayList();
        addrowsToTable();

        TreeItem<supplierModel> root = new RecursiveTreeItem<supplierModel>(supplierList, RecursiveTreeObject::getChildren);
        tableView.getColumns().addAll(SNcolumn, SAcolumn, Branchcolumn, Contactcolumn);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<supplierModel>>() {

                    @Override
                    public boolean test(TreeItem<supplierModel> t) {

                        boolean flag = t.getValue().supName.getValue().contains(newValue)
                                || t.getValue().supAddress.getValue().contains(newValue)
                                || t.getValue().branch.getValue().contains(newValue)
                                || t.getValue().contact.getValue().contains(newValue);
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

    public void showDetails(TreeItem<supplierModel> pModel) {
        supNameLabel.setText(pModel.getValue().getName());
        supAddressLabel.setText(pModel.getValue().getAddress());
        branchLabel.setText(pModel.getValue().getBranch());
        contactLabel.setText(pModel.getValue().getContact());
        supNameTF.setText(pModel.getValue().getName());
        supAddressTF.setText(pModel.getValue().getAddress());
        branchTF.setText(pModel.getValue().getBranch());
        contactTF.setText(pModel.getValue().getContact());

        Sname = pModel.getValue().getName();
        Sbranch = pModel.getValue().getBranch();
        Saddress = pModel.getValue().getAddress();
        Scontact = pModel.getValue().getContact();
       
    }

    private static void insert(String suppliername, String address, String branch, String contact) {
        try {

            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(sup_name,s_address,branch,contact) VALUES (?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setString(1, suppliername);
            stat.setString(2, address);
            stat.setString(3, branch);
            stat.setString(4, contact);
            

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
//        System.out.print("delete fjfjfj"+contactTF.getText());   
//        String value=contactTF.getText();
//        boolean temp=true;
//        try{
//        int contactNo=Integer.parseInt(value);
//        temp=true;
//        }catch(NumberFormatException ex) {
//         System.out.print("delete"+ex.getMessage());   
////         showError(ex.getMessage());   
//        temp= false;
//    }
         if (supNameTF.getText().isEmpty() || supAddressTF.getText().isEmpty()||contactTF.getText().isEmpty()||branchTF.getText().isEmpty()) {

                supNameTF.validate();
                supAddressTF.validate();
                branchTF.validate();
//                contactTF.validate();
//                
            }else if(contactTF.getText().length()!=10){
                showError("phone number must be 10 digit nuberical");
            }else{
        try {
//            contactTF.getValidators().clear();
            insert(supNameTF.getText(), supAddressTF.getText(),branchTF.getText(),contactTF.getText());
            supplierList.add(new supplierModel(supNameTF.getText(),
            supAddressTF.getText(), branchTF.getText(), contactTF.getText()));
       
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

        }}
    }

    void addrowsToTable() {

        String sqlSelect = "select * from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " ";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                System.out.print("result.getString(2)"+result.getString(2));
                System.out.print("result.getString(3)"+result.getString(3));
                System.out.print("result.getString(4)"+result.getString(4));
                System.out.print("result.getString(5)"+result.getString(5));
                supplierList.add(new supplierModel(result.getString(2), result.getString(3),result.getString(5), result.getString(4) + ""));

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
//            supplierList.remove(index);
            System.out.print("delete"+supplierList.get(index).getName());
            String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " where sup_name='" + supplierList.get(index).getName()+"'";
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

    }

    @FXML
    void updatePatientRow(ActionEvent event) {

        int index = tableView.getSelectionModel().getSelectedIndex();
        TreeItem<supplierModel> pModel = tableView.getSelectionModel().getSelectedItem();

        
        System.out.print("Sname"+Sname);
        System.out.print("Saddress"+Saddress);
        System.out.print("Sbranch"+Sbranch);
        System.out.print("Scontact"+Scontact);
        
        String sqlUpdat = "UPDATE  DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " SET sup_name='" + supNameTF.getText() + "' ,s_address='" + supAddressTF.getText() + "' , "
                + "  branch='" + branchTF.getText() + "',contact='" + contactTF.getText() + "' "
                + " WHERE sup_name='" + Sname + "' and" + " s_address='" + Saddress + "' and"
                + " branch='" + Sbranch + "' and"
                + " contact='" + Scontact +  "'";
        if (supNameTF.getText().isEmpty() || supAddressTF.getText().isEmpty()||contactTF.getText().isEmpty()||branchTF.getText().isEmpty()) {

                supNameTF.validate();
                supAddressTF.validate();
                branchTF.validate();
//                contactTF.validate();
//                
            }else if(contactTF.getText().length()!=10){
                showError("phone number must be 10 digit nuberical");
            }else{
        try {
//            contactTF.getValidators().clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlUpdat);
            stat.executeUpdate();
            supplierModel PatientModel = new supplierModel(supNameTF.getText(), supAddressTF.getText(), branchTF.getText(),contactTF.getText());
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
        }
    }}

    @FXML
    void clearFields(ActionEvent event) {
        clear();

    }

    public void clear() {

        supNameTF.setText(null);
        supAddressTF.setText(null);
        branchTF.setText(null);
        contactTF.setText(null);
        
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
                supplierList.removeAll(supplierList);
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
