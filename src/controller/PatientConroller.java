/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Main.Signin;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import static controller.PurchaseDeailsController.showError;
import static controller.SupplierWindowController.showError;
import java.awt.Desktop;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
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
import javafx.concurrent.Task;
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
import model.patientsModel;
import model.purchaseDetailsModel;
import model.CunsultationModel;
/**
 *
 * @author mary
 */
public class PatientConroller implements Initializable {

    @FXML
    private JFXTreeTableView<patientsModel> tableView;
    ObservableList<patientsModel> patientsList;
    ObservableList<CunsultationModel> CunsultationList;
//    JFXDatePicker datePicker;
    @FXML
    private JFXTextField searchTF;

    @FXML
    private JFXTextField patientNameTF,patientAgeTF,patientGenderTF,patientAddressTF,patientContactTF;
//    @FXML
//    private JFXTextArea treatmentTF, diagnosisTF;

    @FXML
    private GridPane InsertGridPane;
    @FXML
    private JFXButton reportButton;
    
    String homeLocation=System.getProperty("user.home");
    String pdfLocation=homeLocation+"/patient_report.pdf";
    @FXML
    private Label patientNameLabel,patientAddressLabel,patientAgeLabel,patientGenderLabel,patientContactLabel;

    String Pname, Paddress, Page, Pgender, Pcontact;

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
       reportButton.setVisible(false);
        patientNameTF.getValidators().add(validator("Input is required"));
        patientAddressTF.getValidators().add(validator("Input is required"));
        patientAgeTF.getValidators().add(validator("Input is required"));
        patientAgeTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    patientAgeTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        patientContactTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    patientContactTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        patientContactTF.getValidators().add(validator("Input is required"));
	patientGenderTF.getValidators().add(validator("Input is required"));
      CunsultationList = FXCollections.observableArrayList();
//        datePicker = new JFXDatePicker();
//        datePicker.setPrefWidth(240);
//        datePicker.setPrefHeight(41);
//
//        InsertGridPane.add(datePicker, 1, 3);

        JFXTreeTableColumn<patientsModel, String> PNcoloumn = new JFXTreeTableColumn<>("Patient Name");

        PNcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientsModel, String> param) {
                return param.getValue().getValue().patientName;

            }
        });

        JFXTreeTableColumn<patientsModel, String> PAcoloumn = new JFXTreeTableColumn<>(" Address");

        PAcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientsModel, String> param) {
                return param.getValue().getValue().patientAddress;

            }
        });

        JFXTreeTableColumn<patientsModel, String> PAgecoloumn = new JFXTreeTableColumn<>("Age");

        PAgecoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientsModel, String> param) {
                return param.getValue().getValue().patientAge;

            }
        });

        JFXTreeTableColumn<patientsModel, String> PContactcoloumn = new JFXTreeTableColumn<>("Contact");

        PContactcoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientsModel, String> param) {
                return param.getValue().getValue().patientContact;

            }
        });
        JFXTreeTableColumn<patientsModel, String> PGendercoloumn = new JFXTreeTableColumn<>("Gender");

        PGendercoloumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<patientsModel, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<patientsModel, String> param) {
                return param.getValue().getValue().patientGender;

            }
        });

        patientsList = FXCollections.observableArrayList();
        addrowsToTable();

        TreeItem<patientsModel> root = new RecursiveTreeItem<patientsModel>(patientsList, RecursiveTreeObject::getChildren);
        tableView.getColumns().addAll(PNcoloumn, PAcoloumn, PAgecoloumn, PContactcoloumn,PGendercoloumn);
        tableView.setRoot(root);
        tableView.setShowRoot(false);

        searchTF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tableView.setPredicate(new Predicate<TreeItem<patientsModel>>() {

                    @Override
                    public boolean test(TreeItem<patientsModel> t) {

                        boolean flag = t.getValue().patientName.getValue().contains(newValue)
                                || t.getValue().patientAddress.getValue().contains(newValue)
                                || t.getValue().patientAge.getValue().contains(newValue)
                                || t.getValue().patientContact.getValue().contains(newValue)
                                || t.getValue().patientGender.getValue().contains(newValue);
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

    public void showDetails(TreeItem<patientsModel> pModel) {
        reportButton.setVisible(true);
        patientNameLabel.setText(pModel.getValue().getPatientName());
        patientAddressLabel.setText(pModel.getValue().getPatientAddress());
        patientAgeLabel.setText(pModel.getValue().getPatientAge());
        patientContactLabel.setText(pModel.getValue().getPatientContact());
        patientGenderLabel.setText(pModel.getValue().getPatientGender());
        patientNameTF.setText(pModel.getValue().getPatientName());
        patientAddressTF.setText(pModel.getValue().getPatientAddress());
        patientAgeTF.setText(pModel.getValue().getPatientAge());
        patientContactTF.setText(pModel.getValue().getPatientContact());
		patientGenderTF.setText(pModel.getValue().getPatientGender());

        Pname= pModel.getValue().getPatientName();
        Page = pModel.getValue().getPatientAge();
        Paddress = pModel.getValue().getPatientAddress();
        Pcontact = pModel.getValue().getPatientContact();
        Pgender= pModel.getValue().getPatientGender();
       
    }

    private void insert(String patientName, String patientAge, String patientAddress, String patientGender,String patientContact) {
        if (patientNameTF.getText().isEmpty() || patientAddressTF.getText().isEmpty()||patientGenderTF.getText().isEmpty()||patientContactTF.getText().isEmpty()) {

                patientAddressTF.validate();
                patientAgeTF.validate();
                patientContactTF.validate();
                patientGenderTF.validate();
                patientNameTF.validate();
//                
            }else if(patientContactTF.getText().length()!=10){
                showError("phone number must be 10 digit nuberical");
            }else{
        try {

            sqlInsert = "INSERT INTO DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + "(patient_name,patient_age,patient_address,patient_gender,patient_contact) VALUES (?,?,?,?,?)";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlInsert);

            stat.setString(1, patientName);
            stat.setString(2, patientAge);
            stat.setString(3, patientAddress);
            stat.setString(4, patientGender);
            stat.setString(5, patientContact);
            

            stat.executeUpdate();
            patientsList.add(new patientsModel("0",patientNameTF.getText(), patientAgeTF.getText(),patientAddressTF.getText(),patientGenderTF.getText(),patientContactTF.getText()));
       
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
        }}
    }

    @FXML
    void insertPatientData(ActionEvent event) {
        System.out.print("patientNameTF.getText()"+patientNameTF.getText());
                System.out.print("patientAgeTF.getText()"+patientAgeTF.getText());
                System.out.print("patientAddressTF.getText()"+patientAddressTF.getText());
                System.out.print("patientGenderTF.getText()"+patientGenderTF.getText());

        try {
            
            insert(patientNameTF.getText(), patientAgeTF.getText(),patientAddressTF.getText(),patientGenderTF.getText(),patientContactTF.getText());
            
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
                patientsList.add(new patientsModel("0",result.getString(2), result.getString(3),result.getString(5), result.getString(4),result.getString(6)));

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
//            patientsList.remove(index);
            System.out.print("delete"+patientsList.get(index).getPatientName());
            String sqlSelect = "delete  from DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " where patient_name='" + patientsList.get(index).getPatientName()+"'";
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
        TreeItem<patientsModel> pModel = tableView.getSelectionModel().getSelectedItem();

        
        String sqlUpdat = "UPDATE  DrJayaramHomeoClinic." + ClinicsMainWindowController.tableName + " SET patient_name='" + patientNameTF.getText() + "' ,patient_address='" + patientAddressTF.getText() + "' , "
                + " patient_gender='" + patientGenderTF.getText()+ "', patient_age='" + patientAgeTF.getText() + "',patient_contact='" + patientContactTF.getText() + "' "
                + " WHERE patient_name='" + Pname+ "' and" + " patient_address='" + Paddress + "' and"
                + " patient_age='" + Page + "' and" + " patient_gender='" + Pgender + "' and"
                + " patient_contact='" + Pcontact +  "'";
        
        if (patientNameTF.getText().isEmpty() || patientAddressTF.getText().isEmpty()||patientGenderTF.getText().isEmpty()||patientContactTF.getText().isEmpty()) {

                patientAddressTF.validate();
                patientAgeTF.validate();
                patientContactTF.validate();
                patientGenderTF.validate();
                patientNameTF.validate();
//                
            }else if(patientContactTF.getText().length()!=10){
                showError("phone number must be 10 digit nuberical");
            }else{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlUpdat);
            stat.executeUpdate();
            patientsModel PatientModel = new patientsModel("0",patientNameTF.getText(), patientAgeTF.getText(),patientAddressTF.getText(),patientGenderTF.getText(),patientContactTF.getText());
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
        reportButton.setVisible(false);
        clear();

    }
    void fetchPatientConsultation(int patientId){
        String sqlSelect = "select * from DrJayaramHomeoClinic.consultation WHERE patient_id='" + patientId+ "'";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();

            while (result.next()) {
                System.out.print("patient controller fetched"+Pname+result.getString(3)+result.getString(4)+""+result.getString(5)+ result.getString(6)+ result.getString(7)+ result.getString(8));
                try{
                    CunsultationList.add(new CunsultationModel(Pname,result.getString(3),result.getString(4)+"",result.getString(5), result.getString(6),result.getString(7),result.getString(8)));
//                CunsultationList.add(new CunsultationModel(Pname,result.getString(3),result.getString(4)+"",result.getString(5), result.getString(6),result.getString(7),result.getString(8)));
                }catch(Exception e){
                    System.err.println("Exception caught:");
                    e.printStackTrace();
                    System.out.print("patient controller exception"+e.getMessage());
                
                }
//                System.out.print("result.getString(5)"+result.getString(5));
//                itemList.add(new StockModel(result.getString(1), result.getString(2),result.getString(3)));

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
    
    public void clear() {

        patientNameTF.setText(null);
        patientAddressTF.setText(null);
        patientAgeTF.setText(null);
        patientContactTF.setText(null);
        patientGenderTF.setText(null);
        
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
                patientsList.removeAll(patientsList);
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
    public int fetchPatientId() {

        String sqlSelect = "select * from DrJayaramHomeoClinic.Patient WHERE patient_name='" + Pname+ "' and" + " patient_address='" + Paddress + "' and" +
              " patient_age='" + Page + "' and" + " patient_gender='" + Pgender + "' and" +
" patient_contact='" + Pcontact +  "'";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(sqlSelect);

            result = stat.executeQuery();
            
            while (result.next()) {
                
                System.out.println("patient fetcheed and id is:"+result.getString(1));
                return(result.getInt(1));
//                patientList.add(new patientsModel(result.getString(1).toString(),result.getString(2), result.getString(3),result.getString(5), result.getString(4),result.getString(6)));
//                pId.add(result.getString(1).toString()+"."+result.getString(2).toString());
            }
//            patientIdCB.setItems(pId);
            return(-1);
            
        } catch (SQLException r) {
            
            showError(r.getMessage());
            return(-1);
        } catch (ClassNotFoundException n) {
            showError(n.getMessage());
            return(-1);
        } catch (NullPointerException l) {
            showError(l.getMessage());
            return(-1);
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
    void generateReport(){
          int fetched_id=fetchPatientId();
            fetchPatientConsultation(fetched_id);
     try{
//            int index = tableView.getSelectionModel().getSelectedIndex();
            Document my_pdf_report = new Document();
            PdfWriter.getInstance (my_pdf_report, new FileOutputStream(pdfLocation));
            my_pdf_report.open();
            String text;
            Font BOLDFont = new Font(Font.getFamily("TIMES_ROMAN"),12,Font.BOLD);
           
            text  = "\t ************Dr N K Jayaram Memorial Homeo Clinic************ ";
            my_pdf_report.add(new Paragraph(text,BOLDFont));
            text  = "\n \t =========================Patient Report=========================";
            my_pdf_report.add(new Paragraph(text,BOLDFont)); 
            text="\n \t Date :"+new java.util.Date()+""
                    + "\n \t Patient Name :"+ Pname+"\n \t ==========================================================";
            my_pdf_report.add(new Paragraph(text));
             PdfPTable table = new PdfPTable(5);
             
            PdfPCell c1 = new PdfPCell(new Phrase("Consultation Date"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
//
//            c1 = new PdfPCell(new Phrase("Patient Id"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);
//             c1 = new PdfPCell(new Phrase("Item Name"));
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Disease"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Duration"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);
            c1 = new PdfPCell(new Phrase("Conulation Status"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);
            c1 = new PdfPCell(new Phrase("Medicines Prescribed"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);
//
//            table.addCell(billList.get(index).getBillDate());
//            table.addCell(selectedPaientId);
        for(CunsultationModel bm:CunsultationList){
        table.addCell(bm.getConsultDate());
             table.addCell(bm.getDisease());
            table.addCell(bm.getDuration());
            table.addCell(bm.getConsultStatus());
            table.addCell(bm.getMedicinePrescribed());
        }
//            text="\n \t Total Cost is :"+totalTF.getText()+"\n \t ==========================================================";
//            my_pdf_report.add(new Paragraph(text));
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

}
