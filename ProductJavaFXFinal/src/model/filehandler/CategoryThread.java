package model.filehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.classes.Product;

public class CategoryThread extends Thread{

	private ObservableList<String> categoryList;
	
	
	public CategoryThread() {
		this.categoryList = FXCollections.observableArrayList();
	}
	
	
	public ObservableList<String> getCategories(){
		return this.categoryList;
	}


	private void readCategories() {
		try {
			File categoryFile = new File("src//resources//files//categories.jobj");
			Scanner myScanner  = new Scanner(categoryFile);
			String dataStr = "";
			while (myScanner.hasNext()) {
				String data = myScanner.nextLine();
				dataStr+=data;
			}
			List<String> list = Arrays.asList(binaryToString(dataStr).split("\\s+"));
			categoryList.addAll(list);
		} catch (FileNotFoundException e) {
			System.out.println("Categoris.dat file not loaded.");
			e.printStackTrace();
		}
	}

	private String binaryToString(String binaryString) {

		String raw = Arrays.stream(binaryString.split(" "))
				.map(binary -> Integer.parseInt(binary, 2))
				.map(Character::toString)
				.collect(Collectors.joining()); // cut the space

		return raw;
	}

	private String convertByteArraysToBinary(byte[] input) {

		StringBuilder result = new StringBuilder();
		for (byte b : input) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				result.append((val & 128) == 0 ? 0 : 1);      // 128 = 1000 0000
				val <<= 1;
			}
		}
		return prettyBinary(result.toString(), 8, " ");
	}

	private String prettyBinary(String binary, int blockSize, String separator) {
		int index = 0;
		int length = binary.length();
		String str="";
		while (index<length-1) {
			str+=binary.substring(index, index+blockSize)+separator;
			index+=blockSize;
		}
		return str;
	}

	@Override
	public void run() {
		readCategories();
	}
}
