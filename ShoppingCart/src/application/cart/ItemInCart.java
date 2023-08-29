package application.cart;



import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemInCart  {
	private final StringProperty itemName;
    private final DoubleProperty quantity;
    private final DoubleProperty totalPrice;
    
    public ItemInCart(String itemName, double quantity, double totalPrice) {
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
    }


	public final StringProperty itemNameProperty() {
		return this.itemName;
	}
	


	@Override
	public String toString() {
		return ",\nQuantity=" + quantity.get();
	}


	public final String getItemName() {
		return this.itemNameProperty().get();
	}
	


	public final void setItemName(final String itemName) {
		this.itemNameProperty().set(itemName);
	}
	


	public final DoubleProperty quantityProperty() {
		return this.quantity;
	}
	


	public final double getQuantity() {
		return this.quantityProperty().get();
	}
	


	public final void setQuantity(final double quantity) {
		this.quantityProperty().set(quantity);
	}


	public final DoubleProperty totalPriceProperty() {
		return this.totalPrice;
	}
	


	public final double getTotalPrice() {
		return this.totalPriceProperty().get();
	}
	


	public final void setTotalPrice(final double totalPrice) {
		this.totalPriceProperty().set(totalPrice);
	}
	
	
}
