/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Lenovo
 */
public class MainController implements Initializable {
    
  
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfYear;
    @FXML
    private TextField tfPages;
    @FXML
    private TableView<Books> tvBooks;
    @FXML
    private TableColumn<Books, Integer> colId;
    @FXML
    private TableColumn<Books, String> colTitle;
    @FXML
    private TableColumn<Books, String> colAuthor;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private TableColumn<Books, Integer> colPages;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnExit;
    @FXML
    private TextField tfSearch;    
    @FXML
    private Button btnSearch;
    AlertShow alert = new AlertShow();
    @FXML
    private Button btnClear;
    @FXML
    private void handleButtonAction(ActionEvent event) {
      
        if(event.getSource()== btnInsert){
            if (tfId.getText().equals("") && tfTitle.getText().equals("") && tfAuthor.getText().equals("") && tfYear.getText().equals("") && tfPages.getText().equals("") ) {
            alert.show("Missing required Fields", "Id and Title , Author , Year , Pages  fields cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            else if (tfId.getText().equals("")) {
            alert.show("Missing required Fields", "Id field cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            else if (tfTitle.getText().equals("")) {
            alert.show("Missing required Fields", "Title field cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            else if (tfAuthor.getText().equals("")) {
            alert.show("Missing required Fields", "Author field cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            else if (tfYear.getText().equals("")) {
            alert.show("Missing required Fields", "Year field cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            else if (tfPages.getText().equals("")) {
            alert.show("Missing required Fields", "Pages field cannot be empty!", Alert.AlertType.WARNING);
            return ;
            }
            
            insertRecord();
             alert.show("Added ' ",
                    "Information has been successfully added",
                    Alert.AlertType.INFORMATION);
            tfId.setText("");
            tfTitle.setText("");
            tfAuthor.setText("");
            tfYear.setText("");
            tfPages.setText("");
        }
        else if (event.getSource() == btnUpdate) {
            if (tvBooks.getSelectionModel().getSelectedItem() == null) {
            alert.show("Message", "Select the item that you want to update", Alert.AlertType.INFORMATION);
            return;
            }
            updateRecord();
             alert.show("Updated '",
                    "Information has been successfully updated",
                    Alert.AlertType.INFORMATION);
            tfId.setText("");
            tfTitle.setText("");
            tfAuthor.setText("");
            tfYear.setText("");
            tfPages.setText("");
        }
        else if (event.getSource() == btnDelete) {
            if (tvBooks.getSelectionModel().getSelectedItem() == null) {
            alert.show("Message", "Select the item that you want to delete first", Alert.AlertType.INFORMATION);
            return;
            }
            deleteRecord() ;
            alert.show("Deleted '",
                    "Information has been successfully deleted",
                    Alert.AlertType.INFORMATION);
            tfId.setText("");
            tfTitle.setText("");
            tfAuthor.setText("");
            tfYear.setText("");
            tfPages.setText("");
        }
        else if(event.getSource() == btnSearch){
            if (tfSearch.getText().equals("")) {
            alert.show("Missing '", "Enter title of book!", Alert.AlertType.WARNING);
            return ;
            }
            tfSearch.textProperty().addListener((obs, oldText, newText) -> {
            search();
        });
        } 
        else if (event.getSource() == btnExit) {
            exit();
        }
        
    }
   
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBooks();
    }
    public Connection getConnection(){
        
        Connection conn ;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library" , "root" , "");
            return conn ;
        }catch(Exception ex){
            System.out.println("Error " + ex.getMessage());
            return null ;
        }
        
    }
        ObservableList<Books> bookList ;
    public ObservableList<Books> getBooksList(){
     
        bookList = FXCollections.observableArrayList() ;
        Connection conn = getConnection() ;
        String query = "SELECT * FROM books" ;
        Statement st ;
        ResultSet rs ;
        try {
            st = conn.createStatement() ;
            rs = st.executeQuery(query) ;
            Books books ;
            while(rs.next()){
                
                books = new Books(rs.getInt("id") , rs.getString("title") , rs.getString("author") , rs.getInt("year") , rs.getInt("pages")) ;
                bookList.add(books );
                
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return bookList ;
    }
    
    public void showBooks(){
        ObservableList<Books> list = getBooksList() ;
        colId.setCellValueFactory(new PropertyValueFactory<Books , Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books , String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books , String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books , Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Books , Integer>("pages"));
        
        tvBooks.setItems(list);
        
    }    

    private void insertRecord(){
        String query = "INSERT INTO books VALUES (" + tfId.getText() + ",'" + tfTitle.getText() + "','" + tfAuthor.getText() + "',"
                + tfYear.getText() + "," + tfPages.getText() + ")";
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
      
        String query = "UPDATE  books SET title  = '" + tfTitle.getText() + "', author = '" + tfAuthor.getText() + "', year = " +
                tfYear.getText() + ", pages = " + tfPages.getText() + " WHERE id = " + tfId.getText() + "";       
        executeQuery(query);
        showBooks();
    }
    private void deleteRecord(){
        String query = "DELETE FROM books WHERE id =" + tfId.getText() + "";
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection() ;
        Statement st ;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
       

    }

    @FXML
    private void handleMouseAction(MouseEvent event) {
       Books book =  tvBooks.getSelectionModel().getSelectedItem();
       tfId.setText(""+book.getId());
       tfTitle.setText(book.getTitle());
       tfAuthor.setText(book.getAuthor());
       tfYear.setText(""+book.getYear());
       tfPages.setText(""+book.getPages());
       btnClear.setOnMouseClicked(e->{
        alert.show("Message", "Data in all fields cleared ' ", Alert.AlertType.INFORMATION);  
        tfId.setText("");
        tfTitle.setText("");
        tfAuthor.setText("");
        tfYear.setText("");
        tfPages.setText("");  
       });
       
    }
    private void search() {
        String keyword = tfSearch.getText();

        if (keyword.equals("")) {
            tvBooks.setItems(bookList);
        } 
        else {
         ObservableList<Books> filteredData = FXCollections.observableArrayList();
            for (Books book : bookList) {
                if (book.getTitle().contains(keyword)) {
                    filteredData.add(book);
                }
            }
            tvBooks.setItems(filteredData);
        }
    }
    private void exit(){
         System.exit(0);
    }
    
}
