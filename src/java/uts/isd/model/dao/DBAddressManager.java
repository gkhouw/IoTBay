/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uts.isd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uts.isd.model.Address;

/**
 *
 * @author Gabrielle K
 */
public class DBAddressManager {
    
    private Connection conn;
    
    public DBAddressManager(Connection conn) throws SQLException {
        this.conn = conn;
    }
    
    public int findAddressID(String unit_number, String street_number, String street, String city, int postcode, String state) throws SQLException {       
       //setup the select sql query string       
       //execute this query using the statement field       
       //add the results to a ResultSet       
       //search the ResultSet for a user using the parameters    
       PreparedStatement selectStatement;
       if (unit_number == null){
            selectStatement = conn.prepareStatement("SELECT Address_ID FROM tblAddress WHERE Unit_Number IS NULL AND Street_Number = ? AND Street = ? AND City = ? AND Postcode = ? AND Address_State = ?");
            selectStatement.setString(1, street_number);
            selectStatement.setString(2, street);
            selectStatement.setString(3, city);
            selectStatement.setInt(4, postcode);
            selectStatement.setString(5,state);
       } else {
            selectStatement = conn.prepareStatement("SELECT Address_ID FROM tblAddress WHERE Unit_Number = ? AND Street_Number = ? AND Street = ? AND City = ? AND Postcode = ? AND Address_State = ?");
            selectStatement.setString(1, unit_number);
            selectStatement.setString(2, street_number);
            selectStatement.setString(3, street);
            selectStatement.setString(4, city);
            selectStatement.setInt(5, postcode);
            selectStatement.setString(6,state);
       }
       
       
       ResultSet rs = selectStatement.executeQuery();
       
       while (rs.next()){
           int addressID = rs.getInt(1);
           
           return addressID;
       }
       selectStatement.close();
       throw new SQLException("No such address exists."); 
       
    }
    
    public Address findAddressByID(int addressID) throws SQLException {
        PreparedStatement selectStatement = conn.prepareStatement("SELECT Unit_Number, Street_Number, Street, City, Postcode, State FROM tblAddress WHERE Address_ID = ?");
        selectStatement.setInt(1, addressID);
        
        ResultSet rs = selectStatement.executeQuery();
        
        while (rs.next()){
            int address_ID = rs.getInt(1);
            String unit_number = rs.getString(2);
            String street_number = rs.getString(3);
            String street = rs.getString(4);
            String city = rs.getString(5);
            int postcode = rs.getInt(6);
            String state = rs.getString(7);
            
            return new Address(address_ID,unit_number, street_number, street, city, postcode, state);
        }
        selectStatement.close();
        throw new SQLException("No such address exists.");
    }

    //Add a user-data into the database   
    public int addAddress(String unit_number, String street_number, String street, String city, int postcode, String state) throws SQLException {                    
        //code for add-operation       
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO tblAddress(Unit_Number, Street_Number, Street, City, Postcode, State) VALUES (?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        insertStatement.setString(1, unit_number);
        insertStatement.setString(2, street_number);
        insertStatement.setString(3, street);
        insertStatement.setString(4, city);
        insertStatement.setInt(5, postcode);
        insertStatement.setString(6, state);
         
        insertStatement.executeUpdate();
        
        int id;
        
        try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);   

            }
            else {
                insertStatement.close();
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        
        insertStatement.close();
        return id;      
    }

    //update a user details in the database   
    public void updateAddress(int addressID, String unit_number, String street_number, String street, String city, int postcode, String state) throws SQLException {       
       //code for update-operation   
       try {
           int id = findAddressID(unit_number, street_number, street, city, postcode, state);
           //UPDATE FK IN SHIPMENT DETAILS
       } catch (SQLException ex) {
           int id = addAddress(unit_number, street_number, street, city, postcode, state);
           //update FK IN SHIPMENT DETAILS - update statement addressid = id SET addressID = id  WHERE addressID = ?
        }
    }       

    //delete a user from the database   
//    public void deleteAddress(String name) throws SQLException{       
//       //code for delete-operation   
//       PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM tblAddress WHERE Product_Name = ?");
//        deleteStatement.setString(1, name);
//         
//        deleteStatement.executeUpdate();
//        deleteStatement.close();
//    }
}