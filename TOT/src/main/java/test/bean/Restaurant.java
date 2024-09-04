package test.bean;

public class Restaurant { 
	private int id;
	private String num;
	private String address;
	private String name;
	private String category;
	private String addX;
	private String addY;
	
	public Restaurant() {
	}

	public Restaurant(int id, String num, String address, String name, String category, String addX, String addY) {
		super();
		this.id = id;
		this.num = num;
		this.address = address;
		this.name = name;
		this.category = category;
		this.addX = addX;
		this.addY = addY;
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", num=" + num + ", address=" + address + ", name=" + name + ", category="
				+ category + ", addX=" + addX + ", addY=" + addY + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getAddX() {
		return addX;
	}

	public void setAddX(String addX) {
		this.addX = addX;
	}

	public String getAddY() {
		return addY;
	}

	public void setAddY(String addY) {
		this.addY = addY;
	}

	
	

	
	
	
}
