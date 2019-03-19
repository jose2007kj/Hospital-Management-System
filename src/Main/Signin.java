package Main;

import controller.BillController;
import controller.ClinicsMainWindowController;
import controller.ConsultationController;
import controller.PatientConroller;
import controller.SigninFXMLController;
//import controller.SubClinicFxmlDocumentController;
import controller.PurchaseDeailsController;
import static controller.PurchaseDeailsController.showError;
import controller. StockController;
import controller.SupplierWindowController;
import controller.aboutController;
import controller.signupFXMLConroller;
import controller.splashcontroller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Application;
/**
 *
 * @author mary
 */
public class Signin extends Application {

    static Stage stageprim, stage1, stage2, stage4, stage3, stage5, stage6,stage7;
    public String clinicName = "";
    public String pageName="";
     public String role="";
    private static Connection conn = null;
    private static PreparedStatement stat = null,pStat=null;
    private static String url = "jdbc:mysql://localhost:3306";
    private static String Password = "";
    private static String username = "mary";
    private static String sqlInsert;
//    public static HostServices hs;
//    Application application; 
    ResultSet result;
    @Override
    public void start(Stage stage) throws Exception {
        
        this.stage1 = stage;
        splashWindow();
    }

    public void splashWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/splashfxml.fxml"));
            AnchorPane pane = loader.load();
            splashcontroller controller = loader.getController();
            controller.Main(this, stage1);
            Scene scene = new Scene(pane);
            stage1.initStyle(StageStyle.UNDECORATED);
            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
            stage1.setScene(scene);
            stage1.show();
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void splashWindowClose() {
        stage1.close();
    }

    public void signinWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/FXMLSignInDocument.fxml"));
            AnchorPane pane = loader.load();
            SigninFXMLController controller = loader.getController();
            stageprim = new Stage();
            controller.Main(this, stageprim);
            Scene scene = new Scene(pane);
            stageprim.setTitle("Sign in");
            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
            stageprim.setResizable(false);
            stageprim.setScene(scene);
            stageprim.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signInClose() {
        stageprim.close();
    }
    public void createTables(){
        String createDb = "CREATE DATABASE IF NOT EXISTS DrJayaramHomeoClinic;";
        String createTablePatient="CREATE TABLE IF NOT EXISTS Patient(`patient_id` int(8) NOT NULL AUTO_INCREMENT,`patient_name` varchar(20) NOT NULL,`patient_age` int(4) NOT NULL,`patient_gender` text NOT NULL,`patient_address` varchar(30) NOT NULL,`patient_contact` varchar(12) NOT NULL,PRIMARY KEY (`patient_id`),KEY `patient_name` (`patient_name`));";
        String createTableBill="CREATE TABLE IF NOT EXISTS bill_details(`bill_no` int(8) NOT NULL AUTO_INCREMENT,`patient_id` int(11) NOT NULL,`bill_date` varchar(12) NOT NULL,`cost` int(4) NOT NULL,PRIMARY KEY (`bill_no`),KEY `patient_id` (`patient_id`),CONSTRAINT `bill_details_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `Patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE);";
        String createTableStock="CREATE TABLE IF NOT EXISTS stock(`item_name` varchar(12) NOT NULL,`expiry_date` varchar(12) NOT NULL,`item_price` int(12) NOT NULL,`item_quantity` int(12) NOT NULL,PRIMARY KEY (`item_name`),KEY `item_name` (`item_name`));";
        String createTableRegister="CREATE TABLE IF NOT EXISTS `register` (`Role` varchar(32) NOT NULL,`username` varchar(32) NOT NULL,`password` varchar(32) NOT NULL,PRIMARY KEY (`username`));";
        String createTableSupplier="CREATE TABLE IF NOT EXISTS `Supplier` (`sup_id` int(4) NOT NULL AUTO_INCREMENT,`sup_name` varchar(12) NOT NULL,`s_address` varchar(30) NOT NULL,`contact` varchar(12) NOT NULL,`branch` varchar(12) NOT NULL,KEY `sup_id` (`sup_id`),PRIMARY KEY (`sup_name`));";
        String createTablePurchaseDetails="CREATE TABLE IF NOT EXISTS `purchase_details` (`purchase_id` int(12) NOT NULL AUTO_INCREMENT,`sup_name` varchar(12) NOT NULL,`purchase_date` varchar(12) NOT NULL,`total` int(12) NOT NULL,PRIMARY KEY (`purchase_id`),KEY `sup_name` (`sup_name`),CONSTRAINT `purchase_details_ibfk_2` FOREIGN KEY (`sup_name`) REFERENCES `Supplier` (`sup_name`) ON DELETE CASCADE ON UPDATE CASCADE);";
        String createTableConsultataion="CREATE TABLE IF NOT EXISTS `consultation` (`consultation_id` int(11) NOT NULL AUTO_INCREMENT,`patient_id` int(12) NOT NULL,`consult_date` varchar(12) NOT NULL,`consult_fee` int(12) NOT NULL,`consult_status` varchar(24) NOT NULL,`disease` varchar(24) NOT NULL,`duration` varchar(12) NOT NULL,`medicine_prescribed` varchar(24) NOT NULL,PRIMARY KEY (`consultation_id`),KEY `patient_id` (`patient_id`),CONSTRAINT `consultation_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `Patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE);";
        String sqlInsert = "INSERT INTO DrJayaramHomeoClinic.register(role,username,password) VALUES (?,?,?)";
        String [] role = new String [2];
        role[0]="doctor";
        role[1]="pharmacy";
        String [] uname = new String [2];
        uname[0]="drnkjayaram";
        uname[1]="pharmacy";
        
        String [] pword = new String [2];
        pword[0]="drnkjayaram123";
        pword[1]="pharmacy123";
        

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, Password);
            stat = conn.prepareStatement(createDb);

            int result = stat.executeUpdate();
            System.out.print("sql statement"+result);
//            below code for patient
            conn = DriverManager.getConnection(url+"/DrJayaramHomeoClinic", username, Password);
            stat=conn.prepareStatement(createTablePatient);
            result=stat.executeUpdate();
            
            stat=conn.prepareStatement(createTableStock);
            result=stat.executeUpdate();
            
            stat=conn.prepareStatement(createTableBill);
            result=stat.executeUpdate();
            
            stat=conn.prepareStatement(createTableRegister);
            result=stat.executeUpdate();
            
            if(result ==0){
                
                stat = conn.prepareStatement(sqlInsert);
                int rows=0;
                do{
                    try{
                    stat.setString(1, role[rows]);
                    stat.setString(2, uname[rows]);
        //            stat.setString(3, patientAddress);
                    stat.setString(3, pword[rows]);
        //            stat.setString(5, patientContact);


                    stat.executeUpdate();
                    }catch(Exception e){
                        System.out.print("exception r"+e.getMessage());
                    }
                    rows++;
                }while(rows<2);

            }
            
            stat=conn.prepareStatement(createTableSupplier);
            result=stat.executeUpdate();
            
            stat=conn.prepareStatement(createTablePurchaseDetails);
            result=stat.executeUpdate();
            
            stat=conn.prepareStatement(createTableConsultataion);
            result=stat.executeUpdate();
            
//            stat=conn.prepareStatement(createTablePurchaseDetails);
//            result=stat.executeUpdate();
            
//            while (result.next()) {
//                System.out.print("sql statement"+createDb);
////                sName.add(result.getString(2));
//                
////                System.out.print("result.getString(2)"+result.getString(2));
////                System.out.print("result.getString(3)"+result.getString(3));
////                System.out.print("result.getString(4)"+result.getString(4));
////                System.out.print("result.getString(5)"+result.getString(5));
////                supplierList.add(new supplierModel(result.getString(2), result.getString(3),result.getString(5), result.getString(4) + ""));
//
//            }
           
        } catch (SQLException r) {
            System.out.print("exception r"+r.getMessage());
            showError(r.getMessage());
        } catch (ClassNotFoundException n) {
            System.out.print("exception r"+n.getMessage());
            showError(n.getMessage());
        } catch (NullPointerException l) {
            System.out.print("exception r"+l.getMessage());
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
    public void signupWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/signUpfxmlDocument.fxml"));
            AnchorPane pane = loader.load();
            signupFXMLConroller controller = loader.getController();
            controller.main(this);
            stage2 = new Stage();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
            stage2.setTitle("Register");
            stage2.setResizable(false);
            stage2.setScene(scene);
            stage2.initStyle(StageStyle.UNDECORATED);
            stage2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signUPClose() {
        stage2.close();
    }

    public void clinicsWindow() {
        FXMLLoader loader;
        try {
            switch(role){
                case "doctor":
                                loader = new FXMLLoader(Signin.class.getResource("/view/FXMLhomeDoctor.fxml"));
            
                                break;
                default:
                        loader = new FXMLLoader(Signin.class.getResource("/view/FXMLPharmacy.fxml"));
            
            };
//            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/FXMLhomeDocument.fxml"));
            AnchorPane pane = loader.load();
            ClinicsMainWindowController controller = loader.getController();
            stage3 = new Stage();
            controller.main(this, stage3);
            Scene scene = new Scene(pane);
            stage3.setTitle("Home");
            stage3.setResizable(true);
            stage3.setScene(scene);
            stage3.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clinicsWindowClose() {
        stage3.close();
    }

    public void SubClinicWindow() {

        try {
            FXMLLoader loader;
            AnchorPane pane;
            stage4 = new Stage();
//            SubClinicFxmlDocumentController controller = loader.getController();
            switch(pageName){
                case "Supplier":
                    loader = new FXMLLoader(Signin.class.getResource("/view/supplierFxmlDocument.fxml"));
                    pane = loader.load();
                    SupplierWindowController controller = loader.getController();
                    controller.main(this, stage4);
                    break;
                case "Purchase":
                    loader = new FXMLLoader(Signin.class.getResource("/view/PurchaseFXML.fxml"));
                    pane = loader.load();
                    PurchaseDeailsController purchaseController = loader.getController();
                    purchaseController.main(this, stage4);
                    
                    
                    break;
                case "Stock":
                    loader = new FXMLLoader(Signin.class.getResource("/view/StockFXML.fxml"));
                    pane = loader.load();
                    StockController StockController = loader.getController();
                    StockController.main(this, stage4);
                    
                    break;
                case "Consultation":
                    loader = new FXMLLoader(Signin.class.getResource("/view/ConsultationFXML.fxml"));
                    pane = loader.load();
                    ConsultationController consultation=loader.getController();
                    consultation.main(this, stage4);
                    
                    break;
                case "Bill":
                    loader = new FXMLLoader(Signin.class.getResource("/view/FXMLbill.fxml"));
                    pane = loader.load();
                    BillController bill=loader.getController();
                    bill.main(this, stage4);
                    
                    break;
                case "Patient":
                    loader = new FXMLLoader(Signin.class.getResource("/view/PatientFXML.fxml"));
                    pane = loader.load();
                    PatientConroller patientController=loader.getController();
                    patientController.main(this, stage4);
                    break;
                
                   
                default:
                    loader = new FXMLLoader(Signin.class.getResource("/view/dentalFxmlDocument.fxml"));
                    pane = loader.load();
                    
                    
                    
            };
            
//            PatientConroller patient=loader.get
            
            
//            controller.main(this, stage4);
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
            stage4.setTitle(clinicName + " Clinic");
            stage4.setFullScreen(true);
            stage4.setResizable(true);
            stage4.setScene(scene);

            stage4.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SubClinicWindowClose() {
        stage4.close();
    }


    public static void AboutWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/aboutFxmlDocument.fxml"));
            AnchorPane pane = loader.load();
            aboutController controller = loader.getController();
            stage6 = new Stage();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
            stage6.setTitle("About");
            stage6.setResizable(false);
            stage6.setScene(scene);
            stage6.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//     public static void usersWindow() {
//
//        try {
//            FXMLLoader loader = new FXMLLoader(Signin.class.getResource("/view/FXMLUsersDocument.fxml"));
//            AnchorPane pane = loader.load();
//            UsersController controller = loader.getController();
//            stage7 = new Stage();
//            Scene scene = new Scene(pane);
//            scene.getStylesheets().add(Signin.class.getResource("/style/StyleSheet.css").toExternalForm());
//            stage7.setTitle("Users");
//            stage7.setResizable(false);
//            stage7.initOwner(stageprim);
//            stage7.initModality(Modality.WINDOW_MODAL);
//            stage7.setScene(scene);
//            stage7.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }

}
