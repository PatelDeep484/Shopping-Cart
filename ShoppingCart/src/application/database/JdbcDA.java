package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.cart.ItemInCart;
import application.cart.carts;
import javafx.collections.ObservableList;

public class JdbcDA {

	public static Integer userId;
	public static Integer cartId;
	private static final String DB_CONNECTION = "C:\\Users\\Deep Patel\\eclipse-workspace\\new\\1\\Workshop5\\src\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_NAME = "users.db";
	private static final String DB_TABLE = "Users";
	private static final String DB_CART_TABLE = "Cart";
	private static final String DB_ITEM_TABLE = "Items";
	
	private static final String CREATE_TBL_QRY = "Create table if not exists "+DB_TABLE+
			" (id integer not null primary key autoincrement, full_name varchar(20) not null,"
			+" email_id varchar(50), password varchar(20))";
	private static final String CREATE_CART_TBL_QRY = "CREATE TABLE IF NOT EXISTS " + DB_CART_TABLE +
		    " (Cart_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, cartfullprise FLOAT, user_id INTEGER, " +
		    "FOREIGN KEY(user_id) REFERENCES " + DB_TABLE + "(id) )";
	private static final String CREATE_CARTITEM_TBL_QRY = "CREATE TABLE IF NOT EXISTS " + DB_ITEM_TABLE +
		    " (item_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, itemName varchar(20), itemtotalprise FLOAT, quantity INTEGER, cart_id INTEGER, " +
		    "FOREIGN KEY(cart_id ) REFERENCES " + DB_TABLE + "(Cart_id) )";
	private static final String INSERT_QRY = "insert into "+ DB_TABLE+" (full_name,email_id,password) values (?,?,?)";		
	private static final String INSERT_CART_QRY = "insert into "+ DB_CART_TABLE+" (cartfullprise,user_id) values (?,?)";
	private static final String INSERT_CARTITEM_QRY = "insert into "+ DB_ITEM_TABLE+" (itemName,itemtotalprise,quantity,cart_id) values (?,?,?,?)";
	
	private static final String SELECT_QRY = "select * from " + DB_TABLE + " where email_id = ? AND password = ?";
	
	public void insertRecord(String fn, String email, String pass) {
		
		try(Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME)){
			PreparedStatement ps = null;
			ps = conn.prepareStatement(CREATE_TBL_QRY);
			ps.execute();
			
			ps = conn.prepareStatement(INSERT_QRY);
			ps.setString(1, fn);
			ps.setString(2, email);
			ps.setString(3, pass);
			
			ps.executeUpdate();
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean validation(String email, String pass) {
		try(Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
				PreparedStatement ps = conn.prepareStatement(SELECT_QRY)){
			
			ps.setString(1, email);
			ps.setString(2, pass);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				userId = rs.getInt("id");
				return true;
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void insertCart(Integer userId, Double Price,ObservableList<ItemInCart> cartItems ) {
		
		try(Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME)){
			PreparedStatement ps = null;
			ps = conn.prepareStatement(CREATE_CART_TBL_QRY);
			ps.execute();
			
			ps = conn.prepareStatement(INSERT_CART_QRY);
			ps.setDouble(1, Price);
			ps.setInt(2, userId);
			ps.executeUpdate();
			ResultSet gk = ps.getGeneratedKeys();
			if (gk.next()) {
                cartId = gk.getInt(1); 
            }
			cartItems.forEach(item -> {
				String name = item.getItemName();
				Double qantity = item.getQuantity();
				Double totalPrice = item.getTotalPrice();
				insertCartItem(name,qantity,totalPrice,cartId);
			});

			
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	public void insertCartItem(String itemName, Double quantity, Double totalPrice, Integer cart_id) {
		
		try(Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME)){
			PreparedStatement ps = null;
			ps = conn.prepareStatement(CREATE_CARTITEM_TBL_QRY);
			ps.execute();
			
			ps = conn.prepareStatement(INSERT_CARTITEM_QRY);
			ps.setString(1, itemName);
			ps.setDouble(2, totalPrice);
			ps.setDouble(3, quantity);
			ps.setInt(4, cart_id);
			ps.executeUpdate();
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static List<carts> getCartDataForUser()  {
        List<carts> cartList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM " + DB_CART_TABLE + " WHERE user_id = " + userId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int cartId = rs.getInt(1);
                double totalPrice = rs.getDouble(2);

                // Retrieve and set other cart details from the database if available

                // Create a new Cart object and add it to the list
                carts cart = new carts(cartId, totalPrice);
                cartList.add(cart);
            }
        }catch (SQLException ex) {
			ex.printStackTrace();
		}

        return cartList;
    }
	public List<ItemInCart> getCartItemsForUser(int cart_id)  {
        List<ItemInCart> itemList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM " + DB_ITEM_TABLE + " WHERE cart_id = " + cart_id;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
            	String itemName = rs.getString(2);
                double totalPrice = rs.getDouble(3);
                double quantity = rs.getDouble(4);

                ItemInCart itemInCart = new ItemInCart(itemName, quantity, totalPrice);
                itemList.add(itemInCart);
            }
        }catch (SQLException ex) {
			ex.printStackTrace();
		}

        return itemList;
    }

	
}
