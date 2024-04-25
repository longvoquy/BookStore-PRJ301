/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.Product;
import model.Shipping;

/**
 *
 * @author Admin
 */
public class OrderDAO extends DBContext {

    public int createReturnId(Order order) {
        try {
            String sql = "INSERT INTO [dbo].[Orders]\n"
                    + "           ([account_id]\n"
                    + "           ,[totalPrice]\n"
                    + "           ,[note]\n"
                    + "           ,[shipping_id])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,?)";
            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, order.getAccountId());
            stm.setDouble(2, order.getTotalPrice());
            stm.setString(3, order.getNote());
            stm.setInt(4, order.getShippingId());
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Order> getAllOrder() {
        List<Order> Orders = new ArrayList<>();
        try {
            String sql = "select * from [Orders]";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt(1));
                order.setAccountId(rs.getInt(2));
                order.setTotalPrice(rs.getDouble(3));
                order.setNote(rs.getString(4));
                order.setCreatedDate(rs.getString(5));
                order.setShippingId(rs.getInt(6));

                Orders.add(order);
            }
        } catch (Exception ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Orders;
    }

    public List<Order> getOrderByAccountId(int accountId) {
        List<Order> Orders = new ArrayList<>();
        try {
//            String sql = "SELECT o.*, s.* "
//                    + "FROM [Orders] o "
//                    + "INNER JOIN Shipping s ON o.shipping_id = s.id "
//                    + "WHERE o.account_id = ?";
            String sql = "select * from Orders where account_id = ?";
            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt(1));
                order.setAccountId(rs.getInt(2));
                order.setTotalPrice(rs.getDouble(3));
                order.setNote(rs.getString(4));
                order.setCreatedDate(rs.getString(5));
                order.setShippingId(rs.getInt(6));

//                // Đọc thông tin vận chuyển từ ResultSet và thiết lập cho đối tượng Shipping
//                Shipping shipping = new Shipping();
//                stm.setString(1, shipping.getName());
//                stm.setString(2, shipping.getPhone());
//                stm.setString(3, shipping.getAddress());
//                stm.setString(4, shipping.getStatus());
//                // Thiết lập thông tin vận chuyển cho đơn hàng
//                order.setShipping(shipping);
                Orders.add(order);
            }
        } catch (Exception ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Orders;
    }

    public static void main(String[] args) {
        OrderDAO a = new OrderDAO();
        List<Order> list = a.getOrderByAccountId(7);
        for (Order Order : list) {
            System.out.println(Order.getId());
            
        }
        System.out.println(list.size());
    }
}
