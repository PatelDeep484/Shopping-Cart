package application.item;

public class item {
	private String name;
	private String unit;
	private double unitQuantity;
    private double unitPrice;
    public item() {
    }
	@Override
	public String toString() {
		return name;
	}
	
	public String message() {
		return "Name=" + name + ",\nUnit=" + unit + ",\nUnit Price=" + unitPrice;
	}
	public item(String name, String unit, double unitQuantity, double unitPrice) {
		super();
		this.name = name;
		this.unit = unit;
		this.unitQuantity = unitQuantity;
		this.unitPrice = unitPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(int unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
