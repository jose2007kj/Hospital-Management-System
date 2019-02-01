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
public class BillController {//implements Initializable {

   /** @FXML
    private JFXTreeTableView<billModel> tableView;
    ObservableList<billModel> billList;
    ObservableList<String> sName=FXCollections.observableArrayList();
    ObservableList<String> iName=FXCollections.observableArrayList();
    JFXDatePicker purchaseDate,expiryDate;
    @FXML
    private JFXTextField searchTF;
    @FXML // for combo box
    private ComboBox<String> supplierNameCB,itemNameCB;
    @FXML
    private JFXTextField quantityTF,totalTF;//,patientGenderTF,patientAddressTF,patientContactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane;

    @FXML
    private Label supplierNameLabel,itemNameLabel,purchaseDateLabel,expiryDateLabel,quantityLabel,totalLabel;

    String selectedSup,selectedItem,selectedSup1,selectedItem1,SName, Edate, Pdate, Iquantity, Itotal;//, Pcontact;

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
        
        quantityTF.getValidators().add(validator("Input is required"));
//        patientAddressTF.getValidators().add(validator("Input is required"));
        totalTF.getValidators().add(validator("Input is required"));
//        patientContactTF.getValidators().add(validator("Input is required"));
//	patientGenderTF.getValidators().add(validator("Input is required"));
//
        purchaseDate = new JFXDatePicker();
        purchaseDate.setPrefWidth(240);
        purchaseDate.setPrefHeight(41);
        expiryDate = new JFXDatePicker();
        expiryDate.setPrefWidth(240);
        expiryDate.setPrefHeight(41);
        InsertGridPane.add(purchaseDate, 1, 2);
        InsertGridPane.add(expiryDate, 1, 3);
        fetchSname();
        fetchIname();
//        itemNameCB.setValue("Select Item Name");
        
        //code for onclick combobox
        supplierNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                    selectedSup=newName;

            }
        });
        itemNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                    selectedItem=newName;

            }
        });
        //code for onclick combobox
        JFXTreeTableColumn<billModel, String> SNcoloumn = new JFXTreeTableColumn<>("Supplier Name");

        SNcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().supName;

            }
        });

        JFXTreeTableColumn<billModel, String> INcoloumn = new JFXTreeTableColumn<>(" Item Name");

        INcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().itemName;

            }
        });

        JFXTreeTableColumn<billModel, String> ExpiryColoumn = new JFXTreeTableColumn<>("Expirey Date");

        ExpiryColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().expiryDate;

            }
        });

        JFXTreeTableColumn<billModel, String> PurchaseDateColoumn = new JFXTreeTableColumn<>("Purchase Date");

        PurchaseDateColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().purchaseDate;

            }
        });
        JFXTreeTableColumn<billModel, String> QuantityColoumn = new JFXTreeTableColumn<>("Quantity");

        QuantityColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().qty;

            }
        });

        JFXTreeTableColumn<billModel, String> TotalColoumn = new JFXTreeTableColumn<>("Total");

        TotalColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<billModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<billModel, String> param) {
                return param.getValue().getValue().total;

            }
        });
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
        tableView.getColumns().addAll(SNcoloumn, INcoloumn, ExpiryColoumn, PurchaseDateColoumn,QuantityColoumn,TotalColoumn);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<billModel>>() {

                    @Override
                    public boolean test(TreeItem<billModel> t) {

                        boolean flag = t.getValue().supName.getValue().contains(newValue)
                                || t.getValue().itemName.getValue().contains(newValue)
                                || t.getValue().purchaseDate.getValue().contains(newValue)
                                || t.getValue().expiryDate.getValue().contains(newValue)
                                || t.getValue().qty.getValue().contains(newValue)
                                || t.getValue().total.getValue().contains(newValue);
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
    void fetchSname() {

        String sqlSelect = "select * from test.Supplier ";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();
            
            while (result.next()) {
                System.out.print("sql statement"+result.getString(2));
                sName.add(result.getString(2));
                
//                System.out.print("result.getString(2)"+result.getString(2));
//                System.out.print("result.getString(3)"+result.getString(3));
//                System.out.print("result.getString(4)"+result.getString(4));
//                System.out.print("result.getString(5)"+result.getString(5));
//                supplierList.add(new supplierModel(result.getString(2), result.getString(3),result.getString(5), result.getString(4) + ""));

            }
           supplierNameCB.setItems(sName);
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
        purchaseDate.setValue(LocalDate.parse(pModel.getValue().getPurchaseDate()));
        expiryDate.setValue(LocalDate.parse(pModel.getValue().getExpiryDate()));
        supplierNameCB.setValue(pModel.getValue().getSupName());
        itemNameCB.setValue(pModel.getValue().getItemName());
        supplierNameLabel.setText(pModel.getValue().getSupName());
        itemNameLabel.setText(pModel.getValue().getItemName());
        purchaseDateLabel.setText(pModel.getValue().getPurchaseDate());
        quantityLabel.setText(pModel.getValue().getQty());
        expiryDateLabel.setText(pModel.getValue().getExpiryDate());
        totalLabel.setText(pModel.getValue().getTotal());
        quantityTF.setText(pModel.getValue().getQty());
        totalTF.setText(pModel.getValue().getTotal());


        Edate= pModel.getValue().getExpiryDate();
        Iquantity = pModel.getValue().getQty();
        Pdate = pModel.getValue().getPurchaseDate();
        Itotal= pModel.getValue().getTotal();
        selectedSup1=pModel.getValue().getSupName();
        selectedItem1=pModel.getValue().getItemName();
        
       
    }

    private static void insert(String supName, String itemName, String purchaseDate, String expiryDate,int quantity,int total) {
        try {

            sqlInsert = "INSERT INTO test." + ClinicsMainWindowController.tableName + "(sup_name,item_name,purchase_date,expiry_date,qty,total) VALUES (?,?,?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setString(1, supName);
            stat.setString(2, itemName);
            stat.setString(3, purchaseDate);
            stat.setString(4, expiryDate);
            stat.setInt(5, quantity);
            stat.setInt(6, total);
            

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
//                System.out.print("totalTF.getText()"+totalTF.getText());
//                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
//                System.out.print("patientGenderTF.getText()"+patientGenderTF.getText());

        try {
            
            insert(selectedSup, selectedItem,purchaseDate.getValue().toString(),expiryDate.getValue().toString(),Integer.parseInt(quantityTF.getText()),Integer.parseInt(totalTF.getText()));
            billList.add(new billModel(selectedSup, selectedItem,purchaseDate.getValue().toString(),expiryDate.getValue().toString(),quantityTF.getText(),totalTF.getText()));
       
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
                billList.add(new billModel(result.getString(2),result.getString(4),result.getString(3),result.getString(5), result.getInt(6)+"",result.getInt(7)+""));

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
            System.out.print("delete"+billList.get(index).getSupName());
            String sqlSelect = "delete  from test." + ClinicsMainWindowController.tableName + " where sup_name='" + billList.get(index).getSupName()+ "' and" + " item_name='" + billList.get(index).getItemName() + "' and"
                + " purchase_date='" + billList.get(index).getPurchaseDate() + "' and" + " expiry_date='" + billList.get(index).getExpiryDate() + "' and" + " qty='" + billList.get(index).getQty() + "' and" 
                + " total='" + billList.get(index).getTotal() +  "'";
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

        billModel PatientModel = new billModel(selectedSup, selectedItem,purchaseDate.getValue().toString(),expiryDate.getValue().toString(),quantityTF.getText(),totalTF.getText());
        pModel.setValue(PatientModel);
//        System.out.print("Sname"+Sname);
//        System.out.print("Saddress"+Saddress);
//        System.out.print("Iquantity"+Iquantity);
//        System.out.print("Pcontact"+Pcontact);
        
        String sqlUpdat = "UPDATE  test." + ClinicsMainWindowController.tableName + " SET sup_name='" + selectedSup + "' ,item_name='" + selectedItem + "' , "
                + " purchase_date='" + purchaseDate.getValue().toString()+ "', expiry_date='" + expiryDate.getValue().toString() +"',qty='" + quantityTF.getText() + "',total='" + totalTF.getText() + "' "
                + " WHERE sup_name='" + selectedSup1+ "' and" + " item_name='" + selectedItem1 + "' and"
                + " purchase_date='" + Pdate + "' and" + " expiry_date='" + Edate + "' and" + " qty='" + Iquantity + "' and" 
                + " total='" + Itotal +  "'";
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
        supplierNameCB.setValue("choose an supplier name");
        totalTF.setText(null);
        quantityTF.setText(null);
        purchaseDate.setValue(null);
        expiryDate.setValue(null);
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
**/
}
