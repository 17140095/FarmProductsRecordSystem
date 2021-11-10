package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.classes.Product;
import model.filehandler.CategoryThread;

public class AddProductController implements Initializable{

	@FXML private Label addProductLabel;
	@FXML private TextField nameTextField;
	@FXML private ComboBox<String> categoryComboBox;
	@FXML private RadioButton specialNoRadioButton;
	@FXML private RadioButton specialYesRadioButton;
	@FXML private TextField specialTextField;
	@FXML private Button saveButton;

	private ObservableList<String> categoryList;
	private Stage addProductStage;
	private Product product;
	private boolean isProductForUpdate;
	private boolean isSave=false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CategoryThread ft=new CategoryThread();
		ft.start();
		this.categoryList =ft.getCategories(); 
		this.restore();
	}

	@FXML
	void SaveButtonAction(ActionEvent event) {
		if (isValidEnteries()) {
			product.setName(nameTextField.getText());
			product.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
			if (specialYesRadioButton.isSelected()) {
				product.setSpecialCategory(specialTextField.getText());
			}else {
				product.setNormalCategory();
			}
			isSave=true;
			addProductStage.close();
			restore();

		}
	}//saveButtonAction

	@FXML
	void noRadioButtonAction(ActionEvent event) {
		specialTextField.setDisable(true);
	}

	@FXML
	void yesRadioButtonAction(ActionEvent event) {
		specialTextField.setDisable(false);
	}

	public boolean getIsSave() {
		return this.isSave;
	}
	private boolean isValidEnteries() {
		boolean v = true;
		
		if (specialYesRadioButton.isSelected()) {
			if(nameTextField.getText().trim().isEmpty()||
					categoryComboBox.getSelectionModel().getSelectedItem().length()<0||
					specialTextField.getText().trim().isEmpty()) {
				v = false;
			}
		}else {
			if(nameTextField.getText().trim().isEmpty()||
					categoryComboBox.getSelectionModel().getSelectedIndex()<0) {
				v = false;
			}
		}

		if (!v) {
			alert("Requuired Fiellds", "All fields are required. Please select or enter the data.");
		}
		return v;
	}

	private void alert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	} 

	public void setAddProductStage(Stage addProductStage) {
		this.addProductStage = addProductStage;
	}
	public void setProductToAdd(Product product) {
		this.isSave = false;
		this.product = product;
	}
	public void setProductToUpdate(Product product) {
		this.isSave = false;
		this.product = product;
		this.nameTextField.setText(this.product.getName());
		this.categoryComboBox.getSelectionModel().select(this.product.getCategory());
		if (this.product.getSpecialCategory().compareTo("Normal")!=0) {
			this.specialYesRadioButton.setSelected(true);
			this.specialTextField.setDisable(false);
			this.specialTextField.setText(this.product.getSpecialCategory());
		}
		this.isProductForUpdate = true;
		this.saveButton.setText("Update");
		this.addProductLabel.setText("Update Product");
	}
	private void restore() {
		this.categoryComboBox.setItems(categoryList);
		this.isProductForUpdate = false;
		this.saveButton.setText("Save");
		this.addProductLabel.setText("Add New Product");
	}
}//class
