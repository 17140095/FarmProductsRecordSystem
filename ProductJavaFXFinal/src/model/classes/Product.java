package model.classes;

public class Product {

	private int id;
	private String name;
	private String category;
	private String specialCategory;
	
	public Product(int id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.specialCategory = "Normal";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSpecialCategory() {
		return specialCategory;
	}

	public void setSpecialCategory(String specialCategory) {
		this.specialCategory = specialCategory;
	}
	public void setNormalCategory() {
		this.specialCategory = "Normal";
	}
	
	public String toString() {
		
		return "ID: "+this.getId()+"\tName: "+this.getName()+"\tCategory: "+
		this.getCategory()+"\t Special: "+this.getSpecialCategory();
	}
	
	
}
