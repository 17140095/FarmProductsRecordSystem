package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.sun.glass.ui.EventLoop.State;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Driver;
import model.classes.Product;
import model.filehandler.CategoryThread;
import model.filehandler.LoadProductsTask;
import model.filehandler.SaveProductsTask;

public class MainController implements Initializable {

	@FXML private StackPane stackpane;
	@FXML private RadioButton allRadioButton;
	@FXML private RadioButton normalRadioButton;
	@FXML private RadioButton SpeicalRadioButton;
	@FXML private ComboBox<String> categoryComboBox;
	@FXML private ProgressIndicator progressIndicator;

	@FXML TableView<Product> productTableView;
	@FXML private TableColumn<Product, Integer> idTableColumn;
	@FXML private TableColumn<Product, String> nameTableColumn;
	@FXML private TableColumn<Product, String> categoryTableColumn;
	@FXML private TableColumn<Product, String> specialTableColumn;

	@FXML private Button addButton;
	@FXML private Button updateButton;
	@FXML private Button deleteButton;
	@FXML private Button loadButton;
	@FXML private Button saveButton;
	@FXML private TextField searchTextField;

	private ObservableList<Product> productList = FXCollections.observableArrayList();
	private ObservableList<String> categoryList;
	public  CategoryThread categoryThread;
	private Driver driver;
	private Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.categoryThread = new CategoryThread();
		this.categoryThread.start();
		allRadioButton.setSelected(true);

		this.categoryList=(this.categoryThread.getCategories());

		//Product Tableview
		idTableColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("id"));
		nameTableColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
		categoryTableColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("category"));
		specialTableColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("specialCategory"));
		productTableView.setPlaceholder(new Label("No product in the table"));

		categoryComboBox.setItems(this.categoryList);

		this.addButton.setOnAction(action->addButtonAction());
		this.updateButton.setOnAction(action->updateButtonAction());
		this.deleteButton.setOnAction(action->deleteButtonAction());
		this.searchTextField.textProperty().addListener(listen->searchAction());
		this.saveButton.setOnAction(action->saveData());
		this.loadButton.setOnAction(action->loadData());
	}


	private void addButtonAction() {
		Product productToAdd = new Product(generateID(), "0", "0");
		boolean result = this.driver.addProduct(productToAdd);
		System.out.println("Result: "+result);
		if (result) {
			productList.add(productToAdd);
			referesh();
		}
	}

	private void updateButtonAction() {
		Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
		if(selectedProduct!=null) {
			boolean result = this.driver.UpdateProduct(selectedProduct);
			if (result) {
				productTableView.refresh();
			}
		}else {
			showAlert("Selection Error", "Please select product first");
		}
	}

	private void deleteButtonAction() {
		Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
		if (selectedProduct!=null) {
			Alert cAlert = new Alert(AlertType.CONFIRMATION);
			cAlert.setTitle("Delete");
			cAlert.setContentText("Do you really want to delete this item?");
			cAlert.showAndWait();
			if (cAlert.getResult()==ButtonType.OK) {
				productList.remove(selectedProduct);
				referesh();
				printProducts(productList);
				
			}
		}else {
			showAlert("Selection Error", "Please select the item first");
		}
	}

	@FXML
	void allRadioButtonAction(ActionEvent event) {
		searchByRadio();
	}

	@FXML
	void categoryComboboxAction(ActionEvent event) {
		if (!categoryComboBox.getSelectionModel().isEmpty()) 
			this.productTableView.setItems(lookupProduct(categoryComboBox.getSelectionModel().getSelectedItem()));
	}

	@FXML
	void normalRadioButtonAction(ActionEvent event) {
		searchByRadio();
	}

	@FXML
	void specialRadioButtonAction(ActionEvent event) {
		searchByRadio();
	}

	private void loadData() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Products");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV File", "*.csv"));

		File file = fileChooser.showOpenDialog(stage);
		if (file!=null) {
			loadProducts(file);
		}else {
			System.out.println("File is null to save");
		}
	}
	private void loadProducts(File productFile) {

		LoadProductsTask task = new LoadProductsTask(productFile,productList);

		Region veil = new Region();
		veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
		veil.setPrefSize(400, 440);
		ProgressIndicator p = new ProgressIndicator();
		p.setMaxSize(140, 140);
		p.setStyle(" -fx-progress-color: #227093;");
		p.progressProperty().bind(task.progressProperty());
		veil.visibleProperty().bind(task.runningProperty());
		p.visibleProperty().bind(task.runningProperty());
		stackpane.getChildren().addAll(veil, p);
		task.setOnRunning(e->referesh());

		new Thread(task).start();
	}
	private void saveData() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Products");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV File", "*.csv"));

		File file = fileChooser.showSaveDialog(this.stage);
		if (file!=null) {
			saveProducts(file);
		}else {
			System.out.println("File is null to save");
		}


	}
	private void saveProducts(File file) {
		SaveProductsTask task = new SaveProductsTask(file,productList);

		Region veil = new Region();
		veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
		veil.setPrefSize(400, 440);
		ProgressIndicator p = new ProgressIndicator();
		p.setMaxSize(140, 140);
		p.setStyle(" -fx-progress-color: #227093;");
		p.progressProperty().bind(task.progressProperty());
		veil.visibleProperty().bind(task.runningProperty());
		p.visibleProperty().bind(task.runningProperty());
		stackpane.getChildren().addAll(veil, p);
		new Thread(task).start();
	}

	private void searchByRadio() {
		this.productTableView.setItems(lookupProduct("button"));
		categoryComboBox.getSelectionModel().clearSelection();
	}
	private void searchAction() {
		categoryComboBox.getSelectionModel().clearSelection();
		productTableView.setItems(lookupProductBySearch(searchTextField.getText()));
	}
	private void showAlert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	private int generateID() {
		int maxID = 0;
		for (Product p : productList) {
			if (p.getId()>maxID) {
				maxID = p.getId();
			}
		}
		return maxID+1;
	}//generateID

	private void referesh() {
		this.productTableView.setItems(this.productList);
	}
	public void setPrimaryStage(Stage stage) {
		this.stage=stage;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	private void printProducts(ObservableList<Product> products) {
		System.out.println("\n");
		for (Product product : products) {
			System.out.println(product);
		}
		System.out.println("\n");
	}

	private void FilterNormalProducts() {
		ObservableList<Product> tempList = FXCollections.observableArrayList();
		for (Product product : productList) {
			if (product.getSpecialCategory().compareTo("Normal")==0) {
				tempList.add(product);
			}
		}
	}
	public ObservableList<Product> lookupProduct(String by){
		FilteredList<Product> filteredProductList = new FilteredList(productList);
		filteredProductList.filtered(product-> product instanceof Product);
		filteredProductList.setPredicate(product->{

			if (by.compareTo("button")==0) {

				// If filter text is empty, display all products.
				if (allRadioButton.isSelected()) {
					return true;
				}else if (normalRadioButton.isSelected()&&(product.getSpecialCategory().compareTo("Normal")==0)) {
					return true;
				}else if(SpeicalRadioButton.isSelected()&&(product.getSpecialCategory().compareTo("Normal")!=0)) {
					return true;
				}

			}else {
				if (product.getCategory().contains(by)) {
					return true;
				}
			}
			return false; // Does not match.

		});

		return FXCollections.observableArrayList(filteredProductList);
	}//lookupProduct

	public ObservableList<Product> lookupProductBySearch(String query){
		FilteredList<Product> filteredProductList = new FilteredList(productList);
		filteredProductList.setPredicate(product->{

			String q = query.toLowerCase(); 
			if (String.valueOf(product.getId()).contains(q)) {
				return true;
			}else if (product.getName().toLowerCase().contains(q)) {
				return true;
			}else if (product.getCategory().toLowerCase().contains(q)) {
				return true;
			}else if (product.getSpecialCategory().toLowerCase().contains(q)) {
				return true;
			}else {
				return false; //do not match
			}
		});

		return FXCollections.observableArrayList(filteredProductList);
	}//lookupProduct

}//class
