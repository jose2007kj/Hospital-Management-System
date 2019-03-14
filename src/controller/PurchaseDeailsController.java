///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package controller;
//import Main.Signin;
//import com.jfoenix.controls.JFXDatePicker;
//import com.jfoenix.controls.JFXTextArea;
//import com.jfoenix.controls.JFXTextField;
//import com.jfoenix.controls.JFXTreeTableColumn;
//import com.jfoenix.controls.JFXTreeTableView;
//import com.jfoenix.controls.RecursiveTreeItem;
//import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
//import com.jfoenix.validation.RequiredFieldValidator;
//import static controller.StockController.showError;
//import static controller.SupplierWindowController.showError;
//import java.io.Console;
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.Optional;
//import java.util.ResourceBundle;
//import java.util.function.Predicate;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeTableColumn;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//import javafx.util.Callback;
//import model.StockModel;
//import model.purchaseDetailsModel;
//import model.supplierModel;
//
///**
// *
// * @author mary
// */
//public class PurchaseDeailsController implements Initializable {
//
//    @FXML
//    private JFXTreeTableView<purchaseDetailsModel> tableView;
//    ObservableList<purchaseDetailsModel> purchaseList;
//    ObservableList<String> sName=FXCollections.observableArrayList();
//    ObservableList<String> iName=FXCollections.observableArrayList();
//    JFXDatePicker purchaseDate,expiryDate;
//    @FXML
//    private JFXTextField searchTF;
//    @FXML // for combo box
//    private ComboBox<String> supplierNameCB,itemNameCB;
//    @FXML
//    private JFXTextField quantityTF,totalTF;//,patientGenderTF,patientAddressTF,patientContactTF;
////    @FXML
////    private JFXTextArea treatmentTF, diagnosisTF;
//
//    @FXML
//    private GridPane InsertGridPane;
//
//    @FXML
//    private Label supplierNameLabel,itemNameLabel,purchaseDateLabel,expiryDateLabel,quantityLabel,totalLabel;
//
//    String selectedSup,selectedItem,selectedSup1,selectedItem1,SName, Edate, Pdate, Iquantity, Itotal;//, Pcontact;
//    Date today = new Date();
//    private static Connection conn = null;
//    private static PreparedStatement stat = null;
//    private static String url = "jdbc:mysql://localhost:3306";
//    private static String Password = "";
//    private static String username = "mary";
//    private static String sqlInsert;
//    ResultSet result;
//
//    public static void showError(String msg) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error ");
//        alert.setHeaderText("there is an error happened !");
//        alert.setContentText(msg);
//        alert.showAndWait();
//    }
//
//    Signin signin;
//    Stage stage;
//
//    public void main(Signin signin, Stage stage) {
//        this.signin = signin;
//        this.stage = stage;
//    }
//
//    public  RequiredFieldValidator validator(String msg){
//        RequiredFieldValidator validator=new RequiredFieldValidator();
//        validator.setOpacity(0.5);
//        validator.setMessage(msg);         
//        return validator;
//        }
//    
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        
//        quantityTF.getValidators().add(validator("Input is required"));
////        patientAddressTF.getValidators().add(validator("Input is required"));
//        totalTF.getValidators().add(validator("Input is required"));
////        patientContactTF.getValidators().add(validator("Input is required"));
////	patientGenderTF.getValidators().add(validator("Input is required"));
//        totalTF.setEditable(false);
//        purchaseDate = new JFXDatePicker();
//        purchaseDate.setPrefWidth(240);
//        purchaseDate.setPrefHeight(41);
////        expiryDate = new JFXDatePicker();
////        expiryDate.setPrefWidth(240);
////        expiryDate.setPrefHeight(41);
//        purchaseDate.setValue(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        
//        InsertGridPane.add(purchaseDate, 1, 2);
////        InsertGridPane.add(expiryDate, 1, 3);
//        fetchSname();
//        fetchIname();
////        itemNameCB.setValue("Select Item Name");
//        
//        //code for onclick combobox
//        quantityTF.textProperty().addListener(change->totalUpdate());
//        supplierNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
//                    selectedSup=newName;
//                    
//
//            }
//        });
//        itemNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
//                    selectedItem=newName;
//                    totalUpdate();
//
//            }
//        });
//        //code for onclick combobox
//        JFXTreeTableColumn<purchaseDetailsModel, String> SNcoloumn = new JFXTreeTableColumn<>("Supplier Name");
//
//        SNcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
//                return param.getValue().getValue().supName;
//
//            }
//        });
//
//        JFXTreeTableColumn<purchaseDetailsModel, String> INcoloumn = new JFXTreeTableColumn<>(" Item Name");
//
//        INcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
//                return param.getValue().getValue().itemName;
//
//            }
//        });
//
////        JFXTreeTableColumn<purchaseDetailsModel, String> ExpiryColoumn = new JFXTreeTableColumn<>("Expirey Date");
////
////        ExpiryColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
////
////            @Override
////            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
////                return param.getValue().getValue().expiryDate;
////
////            }
////        });
//
//        JFXTreeTableColumn<purchaseDetailsModel, String> PurchaseDateColoumn = new JFXTreeTableColumn<>("Purchase Date");
//
//        PurchaseDateColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
//                return param.getValue().getValue().purchaseDate;
//
//            }
//        });
//        JFXTreeTableColumn<purchaseDetailsModel, String> QuantityColoumn = new JFXTreeTableColumn<>("Quantity");
//
//        QuantityColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
//                return param.getValue().getValue().qty;
//
//            }
//        });
//
//        JFXTreeTableColumn<purchaseDetailsModel, String> TotalColoumn = new JFXTreeTableColumn<>("Total");
//
//        TotalColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
//                return param.getValue().getValue().total;
//
//            }
//        });
////
////        JFXTreeTableColumn<patientModel, String> Teatmentcolumn = new JFXTreeTableColumn<>("Treatment");
////        Teatmentcolumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientModel, String>, ObservableValue<String>>() {
////
////            @Override
////            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientModel, String> param) {
////                return param.getValue().getValue().treatment;
////
////            }
////        });
//
//        purchaseList = FXCollections.observableArrayList();
//        addrowsToTable();
//
//        TreeItem<purchaseDetailsModel> root = new RecursiveTreeItem<purchaseDetailsModel>(purchaseList, RecursiveTreeObject::getChildren);
//        tableView.getColumns().addAll(SNcoloumn, INcoloumn, PurchaseDateColoumn,QuantityColoumn,TotalColoumn);
//        tableView.setRoot(root);
//        tableView.setShowRoot(false);
//
//        searchTF.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//
//                tableView.setPredicate(new Predicate<TreeItem<purchaseDetailsModel>>() {
//
//                    @Override
//                    public boolean test(TreeItem<purchaseDetailsModel> t) {
//
//                        boolean flag = t.getValue().supName.getValue().contains(newValue)
//                                || t.getValue().itemName.getValue().contains(newValue)
//                                || t.getValue().purchaseDate.getValue().contains(newValue)
//                                || t.getValue().qty.getValue().contains(newValue)
//                                || t.getValue().total.getValue().contains(newValue);
//                        return flag;
//
//                    }
//                });
//            }
//
//        });
//
//        tableView.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue)
//                -> 
//                showDetails(newValue)
//        );
//    }
//    void totalUpdate(){
//        if(selectedItem!=null){
//        String sqlSelect = "select * from DrJayaramHomeoClinic.stock WHERE item_name='"+selectedItem+"'";
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlSelect);
//
//            result = stat.executeQuery();
//
//            while (result.next()) {
//                int total=Integer.parseInt(result.getString(3))*Integer.parseInt(quantityTF.getText());
//                totalTF.setText(total+"");
//                System.out.print("result.getString(1)"+result.getString(1));
//                System.out.print("result.getString(2)"+result.getString(2));
//                System.out.print("result.getString(3)"+result.getString(3));
////                System.out.print("result.getString(5)"+result.getString(5));
////                itemList.add(new StockModel(result.getString(1), result.getString(2),result.getString(3)));
//
//            }
////            itemNameCB.setItems(iName);
//        } catch (SQLException r) {
//            showError(r.getMessage());
//        } catch (ClassNotFoundException n) {
//            showError(n.getMessage());
//        } catch (NullPointerException l) {
//            showError(l.getMessage());
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException rr) {
//                showError(rr.getMessage());
//            }
//        }
//
//        }
//    }
//    void fetchSname() {
//
//        String sqlSelect = "select * from DrJayaramHomeoClinic.Supplier ";
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlSelect);
//
//            result = stat.executeQuery();
//            
//            while (result.next()) {
//                System.out.print("sql statement"+result.getString(2));
//                sName.add(result.getString(2));
//                
////                System.out.print("result.getString(2)"+result.getString(2));
////                System.out.print("result.getString(3)"+result.getString(3));
////                System.out.print("result.getString(4)"+result.getString(4));
////                System.out.print("result.getString(5)"+result.getString(5));
////                supplierList.add(new supplierModel(result.getString(2), result.getString(3),result.getString(5), result.getString(4) + ""));
//
//            }
//           supplierNameCB.setItems(sName);
//        } catch (SQLException r) {
//            showError(r.getMessage());
//        } catch (ClassNotFoundException n) {
//            showError(n.getMessage());
//        } catch (NullPointerException l) {
//            showError(l.getMessage());
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException rr) {
//                showError(rr.getMessage());
//            }
//        }
//
//    }
//    void fetchIname(){
//        String sqlSelect = "select * from DrJayaramHomeoClinic.stock";
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlSelect);
//
//            result = stat.executeQuery();
//
//            while (result.next()) {
//                iName.add(result.getString(1));
//                System.out.print("result.getString(1)"+result.getString(1));
//                System.out.print("result.getString(2)"+result.getString(2));
//                System.out.print("result.getString(3)"+result.getString(3));
////                System.out.print("result.getString(5)"+result.getString(5));
////                itemList.add(new StockModel(result.getString(1), result.getString(2),result.getString(3)));
//
//            }
//            itemNameCB.setItems(iName);
//        } catch (SQLException r) {
//            showError(r.getMessage());
//        } catch (ClassNotFoundException n) {
//            showError(n.getMessage());
//        } catch (NullPointerException l) {
//            showError(l.getMessage());
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException rr) {
//                showError(rr.getMessage());
//            }
//        }
//
//    }
//    public void showDetails(TreeItem<purchaseDetailsModel> pModel) {
//        purchaseDate.setValue(LocalDate.parse(pModel.getValue().getPurchaseDate()));
////        expiryDate.setValue(LocalDate.parse(pModel.getValue().getExpiryDate()));
//        supplierNameCB.setValue(pModel.getValue().getSupName());
//        itemNameCB.setValue(pModel.getValue().getItemName());
//        supplierNameLabel.setText(pModel.getValue().getSupName());
//        itemNameLabel.setText(pModel.getValue().getItemName());
//        purchaseDateLabel.setText(pModel.getValue().getPurchaseDate());
//        quantityLabel.setText(pModel.getValue().getQty());
////        expiryDateLabel.setText(pModel.getValue().getExpiryDate());
//        totalLabel.setText(pModel.getValue().getTotal());
//        quantityTF.setText(pModel.getValue().getQty());
//        totalTF.setText(pModel.getValue().getTotal());
//
//
////        Edate= pModel.getValue().getExpiryDate();
//        Iquantity = pModel.getValue().getQty();
//        Pdate = pModel.getValue().getPurchaseDate();
//        Itotal= pModel.getValue().getTotal();
//        selectedSup1=pModel.getValue().getSupName();
//        selectedItem1=pModel.getValue().getItemName();
//        
//       
//    }
//
//    private static void insert(String supName, String itemName, String purchaseDate,int quantity,int total) {
//        try {
//
//            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(sup_name,item_name,purchase_date,qty,total) VALUES (?,?,?,?,?)";
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlInsert);
//
//            stat.setString(1, supName);
//            stat.setString(2, itemName);
//            stat.setString(3, purchaseDate);
////            stat.setString(4, expiryDate);
//            stat.setInt(4, quantity);
//            stat.setInt(5, total);
//            
//
//            stat.executeUpdate();
//
//        } catch (SQLException ex) {
//            showError(ex.getMessage());
//        } catch (ClassNotFoundException ex) {
//            showError(ex.getMessage());
//
//        } catch (NumberFormatException c) {
//            showError(c.getMessage());
//
//        } catch (NullPointerException cc) {
//            showError(cc.getMessage());
//
//        } catch (Error e) {
//            showError(e.getMessage());
//        } catch (Exception f) {
//            showError(f.getMessage());
//
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException ex) {
//                showError(ex.getMessage());
//            }
//        }
//    }
//
//    @FXML
//    void insertPatientData(ActionEvent event) {
////        System.out.print("quantityTF.getText()"+quantityTF.getText());
////                System.out.print("totalTF.getText()"+totalTF.getText());
////                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
////                System.out.print("patientGenderTF.getText()"+patientGenderTF.getText());
//
//        try {
//            
//            insert(selectedSup, selectedItem,purchaseDate.getValue().toString(),Integer.parseInt(quantityTF.getText()),Integer.parseInt(totalTF.getText()));
//            purchaseList.add(new purchaseDetailsModel(selectedSup, selectedItem,purchaseDate.getValue().toString(),quantityTF.getText(),totalTF.getText()));
//       
//        }
//        catch (NullPointerException cc) {
//            showError("Please , All inputs are requires");
//
//        } 
//        catch (NumberFormatException c) {
//            showError("Age must be number");
//            
//        } catch (Error e) {
//            showError(e.getMessage());
//        } catch (Exception f) {
//            showError(f.getMessage());
//
//        }
//    }
//
//    void addrowsToTable() {
//
//        String sqlSelect = "select * from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " ";
//
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlSelect);
//
//            result = stat.executeQuery();
//
//            while (result.next()) {
//                System.out.print("result.getString(2)"+result.getString(2));
//                System.out.print("result.getString(3)"+result.getString(3));
//                System.out.print("result.getString(4)"+result.getString(4));
//                System.out.print("result.getString(5)"+result.getString(5));
//                purchaseList.add(new purchaseDetailsModel(result.getString(2),result.getString(4),result.getString(3), result.getInt(5)+"",result.getInt(6)+""));
//
//            }
//        } catch (SQLException r) {
//            showError(r.getMessage());
//        } catch (ClassNotFoundException n) {
//            showError(n.getMessage());
//        } catch (NullPointerException l) {
//            showError(l.getMessage());
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException rr) {
//                showError(rr.getMessage());
//            }
//        }
//
//    }
//
//    @FXML
//    void delelePatientRow(ActionEvent event) {
//        try {
//            int index = tableView.getSelectionModel().getSelectedIndex();
////            purchaseList.remove(index);
//            System.out.print("delete"+purchaseList.get(index).getSupName());
//            String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " where sup_name='" + purchaseList.get(index).getSupName()+ "' and" + " item_name='" + purchaseList.get(index).getItemName() + "' and"
//                + " purchase_date='" + purchaseList.get(index).getPurchaseDate() + "' and" + " qty='" + purchaseList.get(index).getQty() + "' and" 
//                + " total='" + purchaseList.get(index).getTotal() +  "'";
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlSelect);
//            stat.executeUpdate();
//            clear();
//        } catch (SQLException r) {
//            System.out.print("delete error is "+r.getMessage());
//            showError(r.getMessage());
//        } catch (ClassNotFoundException n) {
//            System.out.print("delete error is "+n.getMessage());
//            showError(n.getMessage());
//        } catch (NullPointerException l) {
//            System.out.print("delete error is "+l.getMessage());
////            showError(l.getMessage());
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException rr) {
//                showError(rr.getMessage());
//            }
//        }
//
//    }
//
//    @FXML
//    void updatePatientRow(ActionEvent event) {
//
//        int index = tableView.getSelectionModel().getSelectedIndex();
//        TreeItem<purchaseDetailsModel> pModel = tableView.getSelectionModel().getSelectedItem();
//
//        purchaseDetailsModel PatientModel = new purchaseDetailsModel(selectedSup, selectedItem,purchaseDate.getValue().toString(),quantityTF.getText(),totalTF.getText());
//        pModel.setValue(PatientModel);
////        System.out.print("Sname"+Sname);
////        System.out.print("Saddress"+Saddress);
////        System.out.print("Iquantity"+Iquantity);
////        System.out.print("Pcontact"+Pcontact);
//        
//        String sqlUpdat = "UPDATE  DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " SET sup_name='" + selectedSup + "' ,item_name='" + selectedItem + "' , "
//                + " purchase_date='" + purchaseDate.getValue().toString()+ "',qty='" + quantityTF.getText() + "',total='" + totalTF.getText() + "' "
//                + " WHERE sup_name='" + selectedSup1+ "' and" + " item_name='" + selectedItem1 + "' and"
//                + " purchase_date='" + Pdate + "' and"+ " qty='" + Iquantity + "' and" 
//                + " total='" + Itotal +  "'";
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url, username, Password);
//            stat = conn.prepareStatement(sqlUpdat);
//            stat.executeUpdate();
//
//        } catch (SQLException e) {
//            showError(e.getMessage());
//        } catch (ClassNotFoundException n) {
//
//            showError(n.getMessage());
//
//        } catch (NumberFormatException f) {
//            showError(f.getMessage());
//
//        } catch (NullPointerException l) {
//            showError(l.getMessage());
//
//        } finally {
//            try {
//                conn.close();
//                stat.close();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    @FXML
//    void clearFields(ActionEvent event) {
//        clear();
//
//    }
//
//    public void clear() {
//
//        quantityTF.setText(null);
//        itemNameCB.setValue("choose an item name");
//        supplierNameCB.setValue("choose an supplier name");
//        totalTF.setText(null);
//        quantityTF.setText(null);
//        purchaseDate.setValue(null);
//        expiryDate.setValue(null);
////        patientContactTF.setText(null);
////        patientGenderTF.setText(null);
//        
//    }
//
//    @FXML
//    void deleteAll(ActionEvent event) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confimation");
//
//        alert.setHeaderText("Confirmation");
//        alert.setContentText("Make sure that You Will delete all patient data ... ");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            try {
//                purchaseList.removeAll(purchaseList);
//                String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName;
//                Class.forName("com.mysql.jdbc.Driver");
//                conn = DriverManager.getConnection(url, username, Password);
//                stat = conn.prepareStatement(sqlSelect);
//                stat.executeUpdate();
//                clear();
//            } catch (SQLException r) {
//                showError(r.getMessage());
//            } catch (ClassNotFoundException n) {
//                showError(n.getMessage());
//            } catch (NullPointerException l) {
//                showError(l.getMessage());
//            } finally {
//                try {
//                    conn.close();
//                    stat.close();
//                } catch (SQLException rr) {
//                    showError(rr.getMessage());
//                }
//            }
//        } else if (result.get() == ButtonType.CANCEL) {
//            alert.close();
//        }
//
//    }
//
//    @FXML
//    void back(ActionEvent event) {
//        signin.clinicsWindow();
//        signin.SubClinicWindowClose();
//    }
//
//}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Main.Signin;
import java.awt.Desktop;
import java.io.File;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import static controller.PurchaseDeailsController.showError;
import static controller.SupplierWindowController.showError;
import java.io.FileOutputStream;
import java.net.URL;
import javafx.concurrent.Task;
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
//import model.billModel;
import model.StockModel;
import model.purchaseDetailsModel;
import model.supplierModel;

/**
 *
 * @author mary
 */
public class PurchaseDeailsController implements Initializable {

    @FXML
    private JFXTreeTableView<purchaseDetailsModel> tableView;
    ObservableList<purchaseDetailsModel> purchaseList;
    ObservableList<String> sName=FXCollections.observableArrayList();
    ObservableList<String> iName=FXCollections.observableArrayList();
//    JFXDatePicker purchaseDate,expiryDate;
    @FXML
    private JFXDatePicker purchaseDate;//,expiryDate;
    @FXML // for combo box
    private ComboBox<String> supplierNameCB,itemNameCB;
    @FXML
    private JFXTextField quantityTF,costTF,totalTF;//,patientGenderTF,patientAddressTF,patientContactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;
    @FXML
    private JFXButton reportButton;
    @FXML
    private GridPane InsertGridPane;
    String homeLocation=System.getProperty("user.home");
    String pdfLocation=homeLocation+"/purchase_bill.pdf";
    @FXML
    private Label supplierNameLabel,itemNameLabel,purchaseDateLabel,quantityLabel,costLabel;
    Date today=new Date();
    String selectedSup,selectedItem,selectedSup1,selectedItem1,SName, billsDate, Iquantity, Icost;//, Pcontact;

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
        costTF.setEditable(false);
        totalTF.setEditable(false);
        quantityTF.textProperty().addListener(change->totalUpdate());
        purchaseDate.setValue(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        itemNameCB.setValue("choose an item name");
        supplierNameCB.setValue("choose an supplier name");
//        InsertGridPane.add(billDate, 1, 5);
        fetchIname();
        fetchSname();
        reportButton.setVisible(false);
        
         quantityTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    quantityTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
//        itemNameCB.setValue("Select Item Name");
        
        //code for onclick combobox
//        hs = 
        supplierNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                    selectedSup=newName;
                
            }
        });
        itemNameCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldName, String newName) {
                    selectedItem=newName;
                    totalUpdate();

            }
        });
        

        JFXTreeTableColumn<purchaseDetailsModel, String> ItemNamecoloumn = new JFXTreeTableColumn<>("Item Name");

        ItemNamecoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
                return param.getValue().getValue().itemName;

            }
        });

        JFXTreeTableColumn<purchaseDetailsModel, String> costColoumn = new JFXTreeTableColumn<>("Cost");

        costColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
                return param.getValue().getValue().total;

            }
        });
        JFXTreeTableColumn<purchaseDetailsModel, String> QuantityColoumn = new JFXTreeTableColumn<>("Quantity");

        QuantityColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<purchaseDetailsModel, String> param) {
                return param.getValue().getValue().qty;

            }
        });

        purchaseList = FXCollections.observableArrayList();
//        addrowsToTable();

        TreeItem<purchaseDetailsModel> root = new RecursiveTreeItem<purchaseDetailsModel>(purchaseList, RecursiveTreeObject::getChildren);
//        tableView.getColumns().addAll(PIdcoloumn, Bdatecoloumn, ItemNamecoloumn, costColoumn,QuantityColoumn);
        tableView.getColumns().addAll(ItemNamecoloumn, costColoumn,QuantityColoumn);
        
        tableView.setRoot(root);
        tableView.setShowRoot(false);


    }

        void totalUpdate(){
        if(selectedItem!=null){
        String sqlSelect = "select * from DrJayaramHomeoClinic.stock WHERE item_name='"+selectedItem+"'";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                int total=Integer.parseInt(result.getString(3))*Integer.parseInt(quantityTF.getText());
                costTF.setText(total+"");
                System.out.print("result.getString(1)"+result.getString(1));
                System.out.print("result.getString(2)"+result.getString(2));
                System.out.print("result.getString(3)"+result.getString(3));
//                System.out.print("result.getString(5)"+result.getString(5));
//                itemList.add(new StockModel(result.getString(1), result.getString(2),result.getString(3)));

            }
//            itemNameCB.setItems(iName);
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
    }
    void fetchSname() {

        String sqlSelect = "select * from DrJayaramHomeoClinic.Supplier ";

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
        String sqlSelect = "select * from DrJayaramHomeoClinic.stock";

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
    private static void insert(String supName, String billDate,int total) {
        try {

            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(sup_name,purchase_date,total) VALUES (?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setString(1, supName);
//            stat.setString(3, itemName);
            stat.setString(2, billDate);
//            stat.setInt(4, quantity);
            stat.setInt(3, total);
            

            stat.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sucessfully saved ");

        alert.setHeaderText("Sucessfully saved");
        alert.setContentText("Sucessfully saved the purchase data ... ");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
    
    }
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
    void generateReport(){
     try{
//            int index = tableView.getSelectionModel().getSelectedIndex();
            Document my_pdf_report = new Document();
            PdfWriter.getInstance (my_pdf_report, new FileOutputStream(pdfLocation));
            my_pdf_report.open();
            String text;
            Font BOLDFont = new Font(Font.getFamily("TIMES_ROMAN"),12,Font.BOLD);
           
            text  = "\t ************Dr N K Jayaram Memorial Homeo Clinic************ ";
            my_pdf_report.add(new Paragraph(text,BOLDFont));
            text  = "\n \t =========================Purchase Info=========================";
            my_pdf_report.add(new Paragraph(text,BOLDFont)); 
            text="\n \t Date :"+purchaseDate.getValue().toString()+""
                    + "\n \t Supplier Name :"+ selectedSup+"\n \t ==========================================================";
            my_pdf_report.add(new Paragraph(text));
             PdfPTable table = new PdfPTable(3);
             
            PdfPCell c1 = new PdfPCell(new Phrase("Item Name"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
//
//            c1 = new PdfPCell(new Phrase("Patient Id"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);
//             c1 = new PdfPCell(new Phrase("Item Name"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Quantity"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Cost"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);
//
//            table.addCell(billList.get(index).getBillDate());
//            table.addCell(selectedPaientId);
        for(purchaseDetailsModel bm:purchaseList){
        table.addCell(bm.getItemName());
             table.addCell(bm.getQty());
            table.addCell(bm.getTotal());
        }
            text="\n \t Total Cost is :"+totalTF.getText()+"\n \t ==========================================================";
            my_pdf_report.add(new Paragraph(text));
            my_pdf_report.add(table);                       
            my_pdf_report.close();
            try{
                if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop not supported");
                return;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                System.out.println("File opening not supported");
                return;
            }

            final Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
                        Desktop.getDesktop().open(new File(pdfLocation));
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    return null;
                }
            };

            final Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
            }catch(Exception ex){
                System.out.print("openin gpdf  error"+pdfLocation);
                showError(ex.getMessage()); 
            }
        }catch(Exception e){
           showError(e.getMessage()); 
        }   
    }
    @FXML
    void addItem(ActionEvent event){
        System.out.print("add est"+selectedItem);
        boolean tempQ=true,tempC=true;
        try{
        int quantity=Integer.parseInt(quantityTF.getText());
        tempQ=true;
        }catch(NumberFormatException ex) {
        tempQ= false;
    }
        try{
        int cost=Integer.parseInt(costTF.getText());
        tempC=true;
        }catch(NumberFormatException ex) {
        tempC= false;
    }
        if(quantityTF.getText().isEmpty() || costTF.getText().isEmpty()){
            quantityTF.validate();
            costTF.validate();
        }else if(!tempQ||!tempC){
            showError("Quantity and Cost must be numerical");
        }else if(selectedItem=="choose an item name"||selectedItem==null){
            showError("please select a valid itemname");
        }
        else{
            purchaseList.add(new purchaseDetailsModel(selectedItem,quantityTF.getText(),costTF.getText()));
            clear();
            int total=0;
//            billModel bm;
            for(purchaseDetailsModel bm:purchaseList){
                try{
                    total=Integer.parseInt(bm.getTotal())+total;
                }catch(NumberFormatException ex){
                    
                }
               
            }
            totalTF.setText(total+"");
            reportButton.setVisible(true);
        }
        
       
    }
    @FXML
    void insertPatientData(ActionEvent event) {
//        System.out.print("quantityTF.getText()"+quantityTF.getText());
//                System.out.print("costTF.getText()"+costTF.getText());
//                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
//                System.out.print("patientGenderTF.getText()"+patientGenderTF.getText());

        try {
            insert(selectedSup, purchaseDate.getValue().toString(),Integer.parseInt(totalTF.getText()));
//          
//            insert(Integer.parseInt(selectedPaientId), selectedItem,billDate.getValue().toString(),Integer.parseInt(quantityTF.getText()),Integer.parseInt(costTF.getText()));
//            billList.add(new billModel(patientID1, selectedItem,billDate.getValue().toString(),quantityTF.getText(),costTF.getText()));
       
        }
        catch (NullPointerException cc) {
            showError("Please , All inputs are requires");

        } 
        catch (NumberFormatException c) {
            showError("total must be number");
            
        } catch (Error e) {
            showError(e.getMessage());
        } catch (Exception f) {
            showError(f.getMessage());

        }
    }

    public void clear() {

        quantityTF.setText(null);
        itemNameCB.setValue("choose an item name");
//        patientId.setValue("choose an supplier name");
        costTF.setText(null);
        quantityTF.setText(null);
    }

    @FXML
    void back(ActionEvent event) {
        signin.clinicsWindow();
        signin.SubClinicWindowClose();
    }
}
