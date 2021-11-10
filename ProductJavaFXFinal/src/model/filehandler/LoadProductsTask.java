package model.filehandler;

import java.io.File;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.classes.Product;

public class LoadProductsTask extends Task<ObservableList<Product>> {

	private File loadFile;
	private ObservableList<Product> productList;
	
	public LoadProductsTask(File loadFile, ObservableList<Product> productList) {
		this.loadFile = loadFile;
		this.productList = productList;
	}
	@Override
	protected ObservableList<Product> call() throws Exception {
		Thread.sleep(500);
		loadProducts();
		Thread.sleep(500);
		
		return productList;
	}
	

	private void loadProducts() throws Exception {
		
			this.productList.clear();
			Scanner myScanner = new Scanner(this.loadFile);
			for (int i=1; myScanner.hasNextLine();i++) {
				String productString = myScanner.nextLine();
				String[] product = productString.split(",");
				try{
					Thread.sleep(1000);
					Product p = new Product(Integer.valueOf(product[0]), product[1], product[2]);
					p.setSpecialCategory(product[3]);
					productList.add(p);
				}catch (Exception e) {
					throw e;
				}
			}
	}

}
