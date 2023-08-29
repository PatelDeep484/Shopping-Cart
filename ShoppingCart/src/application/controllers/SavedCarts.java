package application.controllers;

import java.io.IOException;
import java.util.List;

import application.cart.ItemInCart;
import application.cart.carts;
import application.database.JdbcDA;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SavedCarts {

    @FXML
    private Button CancelButton;

    @FXML
    private TableColumn<carts, Integer> CartId;

    @FXML
    private TableColumn<carts, Double> TotaPrice;

    @FXML
    private TableView<carts> savedCart;

    @FXML
    void LoadCart(ActionEvent event) {
    	carts selectedCart = savedCart.getSelectionModel().getSelectedItem();
    	if (selectedCart != null) {
    		int cartId = selectedCart.getCartId();
        	JdbcDA da = new JdbcDA();
    		List<ItemInCart> cartItems = da.getCartItemsForUser(cartId);
    		openShoppingCartScene(cartItems);
    	}else {
    		
    		Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No cart selected");
            alert.setHeaderText("Cannot Load cart");
            alert.setContentText( "Please select a cart to load the cart");
            alert.showAndWait();
    	}
    }
    
    public void initialize() {
		List<carts> cartData = JdbcDA.getCartDataForUser();

		ObservableList<carts> observableCartData = FXCollections.observableArrayList(cartData);

		CartId.setCellValueFactory(cellData -> cellData.getValue().cartIdProperty().asObject());
		TotaPrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

		savedCart.setItems(observableCartData);
    }
    
    private void openShoppingCartScene(List<ItemInCart> cartItems) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ShoppingCart.fxml"));
            Parent cartRoot = loader.load();

            // Get the controller for the ShoppingCart.fxml scene
            ShoppingCart shoppingCartController = loader.getController();
            
            shoppingCartController.getCartObservableList().clear();
            
            shoppingCartController.getCartObservableList().addAll(cartItems);

            Stage cartStage = new Stage();
            cartStage.setTitle("Shopping Cart");
            Scene cartScene = new Scene(cartRoot, 800, 600);
            cartStage.setScene(cartScene);
            cartStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
