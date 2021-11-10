package model;

import java.io.IOException;

import controller.AddProductController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.classes.Product;

public class Driver extends Application{

	private Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.getIcons().add(new Image("/resources/images/icon.jpg"));
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Main.fxml"));
		VBox rootPane;
		try {
			rootPane = (VBox)loader.load();
			MainController mc = loader.getController();
			mc.setDriver(this);
			mc.setPrimaryStage(this.primaryStage);
			
			Scene scene = new Scene(rootPane);
			primaryStage.setTitle("Farm System");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Main.fxml not loaded");
			e.printStackTrace();
		}
	}
	
	public boolean addProduct(Product p) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddProduct.fxml"));
		VBox rootPane;
		AddProductController apc=null;
		try {
			rootPane = (VBox)loader.load();
			apc = loader.getController();
			
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/resources/images/icon.jpg"));
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			apc.setAddProductStage(stage);
			apc.setProductToAdd(p);
			
			Scene scene = new Scene(rootPane);
			stage.setTitle("Add Product");
			stage.setScene(scene);
			stage.showAndWait();
			
			
		} catch (IOException e) {
			System.out.println("AddProduct.fxml not loaded");
			e.printStackTrace();
		}
		return apc.getIsSave();
	}
	public boolean UpdateProduct(Product p) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddProduct.fxml"));
		VBox rootPane;
		AddProductController apc=null;
		try {
			rootPane = (VBox)loader.load();
			apc = loader.getController();
			
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/resources/images/icon.jpg"));
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			apc.setAddProductStage(stage);
			apc.setProductToUpdate(p);
			
			Scene scene = new Scene(rootPane);
			stage.setTitle("Update Product");
			stage.setScene(scene);
			stage.showAndWait();
			
			
		} catch (IOException e) {
			System.out.println("AddProduct.fxml not loaded");
			e.printStackTrace();
		}
		return apc.getIsSave();
	}
}
