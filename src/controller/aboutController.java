package controller;

import Main.Signin;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author mary
 */
public class aboutController implements Initializable{
 

    
 Signin su;
        Stage stage;
        public  void Main( Signin su,Stage stage){
            this.stage=stage;
        this.su=su;
        }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

       
    }

    @FXML
    void openTwitterLink(ActionEvent event) {
     try {
 java.awt.Desktop.getDesktop().browse(new URI(""));
     } catch (URISyntaxException ex) {
        ex.printStackTrace();   
     } catch (IOException ex) {
        ex.printStackTrace();   
     }
    }
}
