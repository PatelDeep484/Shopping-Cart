package application.cart;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class carts {
	private final IntegerProperty cartId;
    private final DoubleProperty totalPrice;
    
    public carts(int cartId, double totalPrice) {
        this.cartId = new SimpleIntegerProperty(cartId);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
    }

	public final IntegerProperty cartIdProperty() {
		return this.cartId;
	}
	
	public final int getCartId() {
		return this.cartIdProperty().get();
	}
	
	public final void setCartId(final int cartId) {
		this.cartIdProperty().set(cartId);
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
