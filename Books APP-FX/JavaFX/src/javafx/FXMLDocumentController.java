
package javafx;

import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class FXMLDocumentController  {
    
    @FXML
    TextField user;
    @FXML
    PasswordField password;
    @FXML
    AnchorPane parent;
    @FXML
   public void LOgin(ActionEvent event) throws SQLException, IOException {
        try {
            Connection con =DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "");
            PreparedStatement ps =(PreparedStatement) con.prepareStatement(" select * from users ");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(user.getText() != null  && password.getText() != null ){
                
                     if(rs.getString("username").equalsIgnoreCase(user.getText())&& rs.getString("password").equalsIgnoreCase(password.getText())){
                       
                        AnchorPane p = FXMLLoader.load(getClass().getResource("Main.fxml"));
                         parent.getChildren().setAll(p); 
                   }
                     else
                     {
                         Alert errorAlert = new Alert(AlertType.ERROR);
                         errorAlert.setHeaderText("Input not valid");
                         errorAlert.setContentText("Enter correct user and password ");
                         errorAlert.showAndWait();
                     }
                }
                        
            }
     } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
}
}
