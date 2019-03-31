/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Main.Signin;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import static controller.ConsultationController.showError;
import java.io.Console;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.StockModel;

/**
 *
 * @author mary
 */
public class StockController implements Initializable {

    @FXML
    private JFXTreeTableView<StockModel> tableView;
    ObservableList<StockModel> itemList;
    JFXDatePicker expiryDate;
//    JFXDatePicker datePicker;
    @FXML
    private JFXTextField searchTF;
    @FXML
    public JFXButton ClearBtn,DeleteBtn,UpdateBtn,InsertBtn,deletealljfx;
    @FXML
    private HBox hbox_b;
    @FXML
    private JFXTextField stockNameTF,itemPriceTF,itemQuanityTF;//,patientAddressTF,patientContactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane,viewGridPane1;

    @FXML
    private Label stockNameLabel,itemPriceLabel,expireyLabel,itemQuantityLabel;//,patientContactLabel,patientAddressLabel;

    String Sname,Iprice, Iquantity,Edate;// Paddress, , Pcontact;
    Date today=new Date();
    private static Connection conn = null;
    private static PreparedStatement stat = null;
    private static String url = "jdbc:mysql://localhost:3306";
    private static String Password = "";
    private static String username = "mary";
    private static String sqlInsert;
    private static String mode="pharmacyist";
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
        System.out.println("doctor \n"+this.signin.role);
        System.out.println("doctor main \n"+new java.util.Date());
        if(this.signin.role.contains("doctor")){
        this.mode="doctor";
//      
        
        ClearBtn.setVisible(false);
        DeleteBtn.setVisible(false);
        UpdateBtn.setVisible(false);
        InsertBtn.setVisible(false);
        deletealljfx.setVisible(false);
//        hbox_b.setVisible(false);
        InsertGridPane.setVisible(false);
        viewGridPane1.setVisible(false);
        }
//        if(this.mode.contains("doctor")){
//        
//        }
    }

    public  RequiredFieldValidator validator(String msg){
        RequiredFieldValidator validator=new RequiredFieldValidator();
        validator.setOpacity(0.5);
        validator.setMessage(msg);         
        return validator;
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("doctor iniialise \n"+new java.util.Date());
        
        stockNameTF.getValidators().add(validator("Input is required"));
//        patientAddressTF.getValidators().add(validator("Input is required"));
        itemPriceTF.getValidators().add(validator("Input is required"));
//        patientContactTF.getValidators().add(validator("Input is required"));
	itemQuanityTF.getValidators().add(validator("Input is required"));
        itemPriceTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    itemPriceTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
          itemQuanityTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    itemQuanityTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        expiryDate = new JFXDatePicker();
        expiryDate.setPrefWidth(240);
        expiryDate.setPrefHeight(41);
        expiryDate.setValue(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        InsertGridPane.add(expiryDate, 1, 3);


        JFXTreeTableColumn<StockModel, String> SNcoloumn = new JFXTreeTableColumn<>("Stock Name");

        SNcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
                return param.getValue().getValue().itemName;

            }
        });

//        JFXTreeTableColumn<StockModel, String> PAcoloumn = new JFXTreeTableColumn<>(" Address");
//
//        PAcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
//                return param.getValue().getValue().patientAddress;
//
//            }
//        });

        JFXTreeTableColumn<StockModel, String> SPricecoloumn = new JFXTreeTableColumn<>("Price");

        SPricecoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
                return param.getValue().getValue().itemPrice;

            }
        });

//        JFXTreeTableColumn<StockModel, String> PContactcoloumn = new JFXTreeTableColumn<>("Contact");
//
//        PContactcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
//                return param.getValue().getValue().patientContact;
//
//            }
//        });
        JFXTreeTableColumn<StockModel, String> SQuantitycoloumn = new JFXTreeTableColumn<>("Quantity");

        SQuantitycoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
                return param.getValue().getValue().itemQuantity;

            }
        });
        JFXTreeTableColumn<StockModel, String> ExpiryColoumn = new JFXTreeTableColumn<>("Expirey Date");

        ExpiryColoumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<StockModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StockModel, String> param) {
                return param.getValue().getValue().itemExpirey;

            }
        });

        itemList = FXCollections.observableArrayList();
        addrowsToTable();

        TreeItem<StockModel> root = new RecursiveTreeItem<StockModel>(itemList, RecursiveTreeObject::getChildren);
        tableView.getColumns().addAll(SNcoloumn,SPricecoloumn,SQuantitycoloumn,ExpiryColoumn);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<StockModel>>() {

                    @Override
                    public boolean test(TreeItem<StockModel> t) {

                        boolean flag = t.getValue().itemName.getValue().contains(newValue)
                                || t.getValue().itemPrice.getValue().contains(newValue)
                                || t.getValue().itemQuantity.getValue().contains(newValue);
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

    public void showDetails(TreeItem<StockModel> pModel) {
        expiryDate.setValue(LocalDate.parse(pModel.getValue().getExpiryDate()));
        expireyLabel.setText(pModel.getValue().getExpiryDate());
        Edate= pModel.getValue().getExpiryDate();
        stockNameLabel.setText(pModel.getValue().getName());
//        patientAddressLabel.setText(pModel.getValue().getPatientAddress());
        itemPriceLabel.setText(pModel.getValue().getPrice());
//        patientContactLabel.setText(pModel.getValue().getPatientContact());
        itemQuantityLabel.setText(pModel.getValue().getQuantity());
        stockNameTF.setText(pModel.getValue().getName());
//        patientAddressTF.setText(pModel.getValue().getPatientAddress());
        itemPriceTF.setText(pModel.getValue().getPrice());
//        patientContactTF.setText(pModel.getValue().getPatientContact());
		itemQuanityTF.setText(pModel.getValue().getQuantity());

        Sname= pModel.getValue().getName();
        Iprice = pModel.getValue().getPrice();
//        Paddress = pModel.getValue().getPatientAddress();
//        Pcontact = pModel.getValue().getPatientContact();
        Iquantity= pModel.getValue().getQuantity();
       
    }

    private static void insert(String itemName, String itemPrice,String itemQuantity,String expiry) {
        try {

            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(item_name,item_price,item_quantity,expiry_date) VALUES (?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setString(1, itemName);
            stat.setString(2, itemPrice);
//            stat.setString(3, patientAddress);
            stat.setString(3, itemQuantity);
            stat.setString(4, expiry);
            

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
        System.out.print("stockNameTF.getText()"+stockNameTF.getText());
                System.out.print("itemPriceTF.getText()"+itemPriceTF.getText());
//                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
                System.out.print("itemQuanityTF.getText()"+itemQuanityTF.getText());
        if ( stockNameTF.getText().isEmpty()||itemPriceTF.getText().isEmpty()||itemQuanityTF.getText().isEmpty()) {

                stockNameTF.validate();
                itemPriceTF.validate();
                itemQuanityTF.validate();
                
//                
            }else{
        try {
            
            insert(stockNameTF.getText(), itemPriceTF.getText(),itemQuanityTF.getText(),expiryDate.getValue().toString());
            itemList.add(new StockModel(stockNameTF.getText(), itemPriceTF.getText(),itemQuanityTF.getText(),expiryDate.getValue().toString()));
       
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
//                System.out.print("result.getString(1)"+result.getString(1));
//                System.out.print("result.getString(2)"+result.getString(2));
//                System.out.print("result.getString(3)"+result.getString(3));
//                System.out.print("result.getString(5)"+result.getString(5));
                itemList.add(new StockModel(result.getString(1), result.getString(3),result.getString(4),result.getString(2)));

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
//            itemList.remove(index);
            System.out.print("delete"+itemList.get(index).getName());
            String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " where item_name='" + itemList.get(index).getName()+"'";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);
            stat.executeUpdate();
            itemList.remove(index);
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
        TreeItem<StockModel> pModel = tableView.getSelectionModel().getSelectedItem();

        
//        System.out.print("Sname"+Sname);
//        System.out.print("Saddress"+Saddress);
//        System.out.print("Iprice"+Iprice);
//        System.out.print("Pcontact"+Pcontact);
        
        String sqlUpdat = "UPDATE  DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " SET item_name='" + stockNameTF.getText() +"' , "
                + " item_quantity='" + itemQuanityTF.getText()+"', expiry_date='" + expiryDate.getValue().toString()+ "', item_price='" + itemPriceTF.getText() + "' "
                + " WHERE item_name='" + Sname+ "' and"
                + " item_price='" + Iprice + "' and" + " item_quantity='" + Iquantity + "'";
        if ( stockNameTF.getText().isEmpty()||itemPriceTF.getText().isEmpty()||itemQuanityTF.getText().isEmpty()) {

                stockNameTF.validate();
                itemPriceTF.validate();
                itemQuanityTF.validate();
                
//                
            }else{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlUpdat);
            stat.executeUpdate();
            StockModel PatientModel = new StockModel(stockNameTF.getText(), itemPriceTF.getText(),itemQuanityTF.getText(),expiryDate.getValue().toString());
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

        stockNameTF.setText(null);
//        patientAddressTF.setText(null);
        itemPriceTF.setText(null);
//        patientContactTF.setText(null);
        itemQuanityTF.setText(null);
        
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
                itemList.removeAll(itemList);
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



