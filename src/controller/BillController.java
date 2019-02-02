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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.billModel;

/**
 *
 * @author mary
 */
public class BillController implements Initializable {

    @FXML
    private JFXTreeTableView<billModel> tableView;
    ObservableList<billModel> billList;
    ObservableList<String> patientIdList=FXCollections.observableArrayList();
    ObservableList<String> iName=FXCollections.observableArrayList();
    JFXDatePicker billDate;//,expiryDate;
    @FXML
    private JFXTextField searchTF;
    @FXML // for combo box
    private ComboBox<String> patientId,itemNameCB;
    @FXML
    private JFXTextField quantityTF,costTF;//,patientGenderTF,patientAddressTF,patientContactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane;

    @FXML
    private Label patientIdLabel,itemNameLabel,billDateLabel,quantityLabel,costLabel;

    String selectedPaientId2,selectedPaientId,patientID1,selectedItem,selectedPaientId1,selectedItem1,SName, billsDate, Iquantity, Icost;//, Pcontact;

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
        
        quantityTF.getValidators().add(validator("Input is required"));
//        patientAddressTF.getValidators().add(validator("Input is required"));
        costTF.getValidators().add(validator("Input is required"));
//        patientContactTF.getValidators().add(validator("Input is required"));
//	patientGenderTF.getValidators().add(validator("Input is required"));
//
        billDate = new JFXDatePicker();
        billDate.setPrefWidth(240);
        billDate.setPrefHeight(41);
        InsertGridPane.add(billDate, 1, 5);
        fetchIname();
        fechPatientId();
//        itemNameCB.setValue("Select Item Name");
        
        //code for onclick combobox
        patientId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
//                    selectedPaientId=newName;
                    patientID1=newName;
                if(newName.contains(".")){
                    try
                        {
                            
                            String[] temp=newName.split("\\.");
                            System.out.print("temp"+temp.length);
                            selectedPaientId=temp[0];
                        }catch(Exception e){
                            System.out.print("temp"+e.toString());
                        }
                
                }else{
                selectedPaientId="";
                }
            }
        });
        itemNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                    selectedItem=newName;

            }
        });
        //code for onclick combobox
        JFXTreeTableColumn<billModel, String> PIdcoloumn = new JFXTreeTableColumn<>("Paient Id");

        PIdcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().patientId;

            }
        });

        JFXTreeTableColumn<billModel, String> Bdatecoloumn = new JFXTreeTableColumn<>(" Bill Date");

        Bdatecoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().billDate;

            }
        });

        JFXTreeTableColumn<billModel, String> ItemNamecoloumn = new JFXTreeTableColumn<>("Item Name");

        ItemNamecoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().itemName;

            }
        });

        JFXTreeTableColumn<billModel, String> costColoumn = new JFXTreeTableColumn<>("Cost");

        costColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().cost;

            }
        });
        JFXTreeTableColumn<billModel, String> QuantityColoumn = new JFXTreeTableColumn<>("Quantity");

        QuantityColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().qty;

            }
        });

//        JFXTreeTableColumn<billModel, String> TotalColoumn = new JFXTreeTableColumn<>("Total");
//
//        TotalColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
//                return param.getValue().getValue().total;
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

        billList = FXCollections.observableArrayList();
        addrowsToTable();

        TreeItem<billModel> root = new RecursiveTreeItem<billModel>(billList, RecursiveTreeObject::getChildren);
        tableView.getColumns().addAll(PIdcoloumn, Bdatecoloumn, ItemNamecoloumn, costColoumn,QuantityColoumn);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<billModel>>() {

                    @Override
                    public boolean test(TreeItem<billModel> t) {

                        boolean flag = t.getValue().patientId.getValue().contains(newValue)
                                || t.getValue().itemName.getValue().contains(newValue)
                                || t.getValue().billDate.getValue().contains(newValue)
                                || t.getValue().cost.getValue().contains(newValue)
                                || t.getValue().qty.getValue().contains(newValue);
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

        String sqlSelect = "select * from test.Patient ";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();
            
            while (result.next()) {
                System.out.print("sql statement"+result.getString(2));
                
                patientIdList.add(result.getString(1).toString()+"."+result.getString(2).toString());
            }
            patientId.setItems(patientIdList);
            
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
    void fetchIname(){
        String sqlSelect = "select * from test.stock";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                iName.add(result.getString(1));
                System.out.print("result.getString(1)"+result.getString(1));
                System.out.print("result.getString(2)"+result.getString(2));
                System.out.print("result.getString(3)"+result.getString(3));
//                System.out.print("result.getString(5)"+result.getString(5));
//                itemList.add(new StockModel(result.getString(1), result.getString(2),result.getString(3)));

            }
            itemNameCB.setItems(iName);
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
    public void showDetails(TreeItem<billModel> pModel) {
        if(pModel.getValue().getPatientId().contains(".")){
            try
            {
                String ntemp=pModel.getValue().getPatientId();
                String[] temp=ntemp.split("\\.");
                System.out.print("temp"+temp.length);
                selectedPaientId=temp[0];
                selectedPaientId2=temp[0];
            }catch(Exception e){
                System.out.print("temp"+e.toString());
            }
        }
        billDate.setValue(LocalDate.parse(pModel.getValue().getBillDate()));
//      expiryDate.setValue(LocalDate.parse(pModel.getValue().getExpiryDate()));
        patientId.setValue(pModel.getValue().getPatientId());
        itemNameCB.setValue(pModel.getValue().getItemName());
        patientIdLabel.setText(pModel.getValue().getPatientId());
        itemNameLabel.setText(pModel.getValue().getItemName());
        billDateLabel.setText(pModel.getValue().getBillDate());
        quantityLabel.setText(pModel.getValue().getQty());
//        expiryDateLabel.setText(pModel.getValue().getExpiryDate());
        costLabel.setText(pModel.getValue().getCost());
        quantityTF.setText(pModel.getValue().getQty());
        costTF.setText(pModel.getValue().getCost());


        billsDate= pModel.getValue().getBillDate();
        Iquantity = pModel.getValue().getQty();
//        Pdate = pModel.getValue().getPurchaseDate();
        Icost= pModel.getValue().getCost();
//        selectedPaientId1=pModel.getValue().getSupName();
        selectedItem1=pModel.getValue().getItemName();
        
       
    }

    private static void insert(int patientid, String itemName, String billDate,int quantity,int total) {
        try {

            sqlInsert = "INSERT INTO test." + ClinicsMainWindowController.tableName + "(patient_id,bill_date,item_name,qty,cost) VALUES (?,?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setInt(1, patientid);
            stat.setString(3, itemName);
            stat.setString(2, billDate);
            stat.setInt(4, quantity);
            stat.setInt(5, total);
            

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
//        System.out.print("quantityTF.getText()"+quantityTF.getText());
//                System.out.print("costTF.getText()"+costTF.getText());
//                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
//                System.out.print("patientGenderTF.getText()"+patientGenderTF.getText());

        try {
            
            insert(Integer.parseInt(selectedPaientId), selectedItem,billDate.getValue().toString(),Integer.parseInt(quantityTF.getText()),Integer.parseInt(costTF.getText()));
            billList.add(new billModel(patientID1, selectedItem,billDate.getValue().toString(),quantityTF.getText(),costTF.getText()));
       
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
    }

    void addrowsToTable() {

        String sqlSelect = "select * from test." + ClinicsMainWindowController.tableName + " ";
        String temp="";
        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                String sqlfetchPatients = "select * from test.Patient where patient_id='"+result.getInt(2)+"'";
                pStat = conn.prepareStatement(sqlfetchPatients);
                pResult=pStat.executeQuery();
                while(pResult.next()){
                   temp=pResult.getString(1).toString()+"."+pResult.getString(2).toString(); 
                }
                System.out.print("result.getString(2)"+result.getString(2));
                System.out.print("result.getString(3)"+result.getString(3));
                System.out.print("result.getString(4)"+result.getString(4));
                System.out.print("result.getString(5)"+result.getString(5));
                billList.add(new billModel(temp,result.getString(4),result.getString(3),result.getInt(5)+"", result.getInt(6)+""));

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
//            billList.remove(index);
//            System.out.print("delete"+billList.get(index).getSupName());
            String sqlSelect = "delete  from test." + ClinicsMainWindowController.tableName + " where patient_id='" + Integer.parseInt(selectedPaientId)+ "' and" + " item_name='" + billList.get(index).getItemName() + "' and"
                + " bill_date='" + billList.get(index).getBillDate() + "' and" + " qty='" + billList.get(index).getQty() + "' and" 
                + " cost='" + billList.get(index).getCost() +  "'";
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
        TreeItem<billModel> pModel = tableView.getSelectionModel().getSelectedItem();

        billModel PatientModel = new billModel(patientID1, selectedItem,billDate.getValue().toString(),quantityTF.getText(),costTF.getText());
        pModel.setValue(PatientModel);
        System.out.print("selectedPaientId  : "+selectedPaientId);
//        System.out.print("Saddress"+Saddress);
//        System.out.print("Iquantity"+Iquantity);
//        System.out.print("Pcontact"+Pcontact);
        
        String sqlUpdat = "UPDATE  test." + ClinicsMainWindowController.tableName + " SET patient_id='" + Integer.parseInt(selectedPaientId) + "' ,item_name='" + selectedItem + "' , "
                + " bill_date='" + billDate.getValue().toString()+ "',qty='" + quantityTF.getText() + "',cost='" + costTF.getText() + "' "
                + " WHERE patient_id='" + selectedPaientId2+ "' and" + " item_name='" + selectedItem1 + "' and"
                + " bill_date='" + billsDate + "' and" + " qty='" + Iquantity + "' and" 
                + " cost='" + Icost +  "'";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlUpdat);
            stat.executeUpdate();

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
    }

    @FXML
    void clearFields(ActionEvent event) {
        clear();

    }

    public void clear() {

        quantityTF.setText(null);
        itemNameCB.setValue("choose an item name");
        patientId.setValue("choose an supplier name");
        costTF.setText(null);
        quantityTF.setText(null);
        billDate.setValue(null);
//        expiryDate.setValue(null);
//        patientContactTF.setText(null);
//        patientGenderTF.setText(null);
        
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
                billList.removeAll(billList);
                String sqlSelect = "delete  from test." + ClinicsMainWindowController.tableName;
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
