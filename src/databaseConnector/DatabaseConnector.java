package databaseConnector;

import java.sql.*;

public class DatabaseConnector {
    private Connection connection = null;
    private Statement statement = null;
    private String url;

    public DatabaseConnector(String url){
        this.url = url;
    }

    public void buildConnection(){
        try{
        String databaseUrl = ""+ url +"";
        connection = DriverManager.getConnection(databaseUrl);
        statement = connection.createStatement();
        }
        catch(SQLException ex){
            System.out.println("Could not build connection");
            ex.printStackTrace();
        }
    }
    public void closeConnection(){
        try{
            if(connection != null){
                connection.close();
            }
            if(statement != null){
                statement.close();
            }
        }
        catch (SQLException ex){
            System.out.println("Could not close connection");
            ex.printStackTrace();
        }
    }
    public ResultSet fetchData(String sql){
        buildConnection();
        try{
            return statement.executeQuery(sql);
        }
        catch (SQLException ex){
            System.out.println("Could not fetch data");
            ex.printStackTrace();
            closeConnection();
        }
        return null;
    }
    public void updateData(String sql){
        buildConnection();
        try{
            statement.executeUpdate(sql);
            connection.close();
        }
        catch (SQLException ex){
            System.out.println("Could not update data");
            ex.printStackTrace();
            closeConnection();
        }
    }
    public void insertData(String sql){
        buildConnection();
        try{
            statement.executeUpdate(sql);
            connection.close();
        }
        catch (SQLException ex){
            System.out.println("Could not insert data");
            ex.printStackTrace();
            closeConnection();
        }
    }

}
