package application.controllers;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.cart.ItemInCart;
import application.database.JdbcDA;
import application.item.item;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ShoppingCart {

	@FXML
    private TextArea ItemOverView;
    @FXML
    private ComboBox<item> itemMenu;
    
    @FXML
    private Label totalAmount;

    @FXML
    private Label pricePerUnit;

    @FXML
    private Label purchasedUnit;

    @FXML
    private Label unit;

    @FXML
    private Button addBtn;
    
    @FXML
    private Button removeBtn;
    
    @FXML
    private Slider unitSlider;
    private List<item> itemsObservableList = new ArrayList<>();

    @FXML
    private TableView<ItemInCart> tableCart;
    
    @FXML
    private TableColumn<ItemInCart, String> tableName;

    @FXML
    private TableColumn<ItemInCart, Double > tablePrice;

    @FXML
    private TableColumn<ItemInCart, Double> tableUnits;
    
    private ObservableList<ItemInCart> cartObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    	 loadData();
    	     	     	 
    	 itemMenu.setItems(FXCollections.observableList(itemsObservableList));
    	 itemMenu.getSelectionModel().clearSelection();
    	 ObjectBinding<item> selectedItemBinding = Bindings.createObjectBinding(() ->
    	 itemMenu.getSelectionModel().getSelectedItem(), itemMenu.getSelectionModel().selectedItemProperty());

    	 
    	 unit.textProperty().bind(Bindings.createStringBinding(() ->
         selectedItemBinding.get() != null ? "1.00 " + selectedItemBinding.get().getUnit() : "0.00", selectedItemBinding));
    	 
    	 pricePerUnit.textProperty().bind(Bindings.createStringBinding(() ->
         selectedItemBinding.get() != null ? String.valueOf(selectedItemBinding.get().getUnitPrice()) : "0.00", selectedItemBinding));
    	 
    	 purchasedUnit.textProperty().bind(Bindings.format("%.1f", unitSlider.valueProperty()));
    	 
    	 
    	
    	 tableName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
    	 tableUnits.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
    	 tablePrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
    	 
    	 tableCart.setItems(getCartObservableList());
    	 tableCart.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    	        if (newSelection != null) {
    	            ItemInCart selectedItem = newSelection;
    	            String selectedItemName = selectedItem.getItemName();
    	            String message = "";
    	            for (item item : itemMenu.getItems()) {
    	                if (item.getName().equals(selectedItemName)) {
    	                    itemMenu.getSelectionModel().select(item);
    	                    message = item.message();
    	                    break;
    	                }
    	            }
    	            unitSlider.setValue(selectedItem.getQuantity());
    	            message += selectedItem.toString();
    	            ItemOverView.setText(message);
    	        }
    	    });
    	 
    	 DoubleBinding totalBinding = Bindings.createDoubleBinding(() -> {
    	        double total = 0.0;
    	        double unitPrice = 0.0;
    	        for (ItemInCart Item : getCartObservableList()) {
    	        	for (item item : itemsObservableList) {
    	                if (item.getName().equals(Item.getItemName())) {
    	                	unitPrice = item.getUnitPrice();
    	                }
    	            }
    	        	total += Item.getQuantity() * unitPrice;
    	        }
    	        return total;
    	    }, getCartObservableList());

    	 totalAmount.textProperty().bind(Bindings.format("%.2f", totalBinding));

    	   
    	 
    }

    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/application/ItemsMaster.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String name = data[0].trim();
                    String unit = data[1].trim();
                    double unitQuantity = Double.parseDouble(data[3].trim());
                    double unitPrice = Double.parseDouble(data[3].trim());
                    itemsObservableList.add(new item(name, unit, unitQuantity, unitPrice));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addToCart(ActionEvent event) {
    	item selectedItem = itemMenu.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Invalid item", "Please select an item.");
            return;
        }

        double quantity = Double.parseDouble(purchasedUnit.getText());
        double totalPrice = selectedItem.getUnitPrice() * quantity;
        if (quantity > 0) {
            ItemInCart itemInCart = new ItemInCart(selectedItem.getName(), quantity, totalPrice);
            getCartObservableList().add(itemInCart);
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Invalid quantity", "Please enter a valid quantity.");
        }
        
    }
    
    @FXML
    void removeFromCart(ActionEvent event) {
    	ItemInCart selectedItem = tableCart.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            getCartObservableList().remove(selectedItem);
            tableCart.getSelectionModel().clearSelection();
        }else {
            // Show alert when no item is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Item Selected");
            alert.setHeaderText("No item selected for removal");
            alert.setContentText("Please select an item to remove from the cart.");
            alert.showAndWait();
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    @FXML
    void saveCart(ActionEvent event) {
    	if (getCartObservableList().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cart Empty", "Cannot save empty cart",
                    "Please add items to the cart before saving.");
        } else {
        	JdbcDA da = new JdbcDA();
        	//cartObservableList.forEach(item -> {
        	Integer str = da.userId;
        	Double totalPrice = Double.parseDouble(totalAmount.getText());
        	da.insertCart(str, totalPrice, getCartObservableList());
        	
        }
    }

    @FXML
    void loadCart(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/savedCarts.fxml"));
            Parent regLoad = loader.load();

            Stage st = new Stage();
            st.setTitle("Load cart");
            Scene sc = new Scene(regLoad, 260, 400);
            st.setResizable(false);
            st.setScene(sc);
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    @FXML
    void cashOut(ActionEvent event) {
    	Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Cash Out");
        confirmDialog.setHeaderText("Are you sure you want to cash out?");
        confirmDialog.setContentText("Click 'OK' to clear the cart and proceed with the cash out.");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        	tableCart.getItems().clear();
        	tableCart.getSelectionModel().clearSelection();
        	getCartObservableList().clear();
        	itemMenu.getSelectionModel().clearSelection();
        	unitSlider.setValue(0);
        	ItemOverView.clear();
            }
    }

	public ObservableList<ItemInCart> getCartObservableList() {
		return cartObservableList;
	}

	public void setCartObservableList(ObservableList<ItemInCart> cartObservableList) {
		this.cartObservableList = cartObservableList;
	}
}
