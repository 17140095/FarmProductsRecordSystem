package model.filehandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.classes.Product;

public class SaveProductsTask extends Task<ObservableList<Product>> {

	private File file;
	private ObservableList<Product> list;
	
	public SaveProductsTask(File file,ObservableList<Product> list) {
		this.file = file;
		this.list = list;
		
	}
	@Override
	protected ObservableList<Product> call() throws Exception {
		Thread.sleep(1000);
		saveTextToFile();
		return this.list;
	}
	
	private void saveTextToFile() throws InterruptedException {
		try {
			Thread.sleep(500);
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(getListOfProductsToSave());
			writer.close();
			Thread.sleep(1000);
			
		} catch (IOException ex) {
			System.out.println("Save file exception: "+ex.getMessage());
		}
	}
	private String getListOfProductsToSave() {
		String toReturn = "";
		for (Product product : this.list) {
			toReturn+=product.getId()+","+product.getName()+","+
					product.getCategory()+","+product.getSpecialCategory()+"\n";
		}
		return toReturn;
	}

}
