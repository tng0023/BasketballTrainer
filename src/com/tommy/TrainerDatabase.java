package com.tommy;

import sun.util.calendar.BaseCalendar;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class TrainerDatabase {

        private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
        private static final String DB_NAME = "testing";
        private static final String USER = "root";
        private static final String PASS = "Tng621180!";

        static Statement statement = null;
        static Connection conn = null;
        static ResultSet rs = null;

    public final static String Trainer_Table_Name = "Training_Appointments";
    public final static String PK_COLUMN = "id";                   //Primary key column. Each trainee will have a unique ID.
    //A primary key is needed to allow updates to the database on modifications to ResultSet
    public final static String Trainer_COLUMN = "Trainer";
    public final static String Trainee_COLUMN = "Student";
    public final static String Phone_COLUMN = "Phone_Number";
    public final static String Date_COLUMN = "Training_Date";
    //public final static String Time_COLUMN = "Training_Time";
    public final static String Facility_COLUMN = "Facility";
    public final static String Training_COLUMN = "Training_Drills";
    public final static String Comments_COLUMN = "Notes";

    private static TrainerDataModel trainerDataModel;

    public static void main (String args[]) {

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllTrainers()) {
            System.exit(-1);
        }
        TrainerForm tableGUI = new TrainerForm(trainerDataModel);
    }

    public static boolean loadAllTrainers(){

        try{
            if (rs!=null) {
                rs.close();
            }
            String getAllData = "SELECT * FROM " + Trainer_Table_Name;
            rs = statement.executeQuery((getAllData));

            if(trainerDataModel == null) {
                trainerDataModel = new TrainerDataModel(rs);
            }else{
                trainerDataModel.updateResultSet(rs);
            }
            return true;
        }catch (Exception e){
            System.out.println("Error loading or reloading trainers");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
    public static boolean setup(){
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);

            // The first argument ResultSet.TYPE_SCROLL_INSENSITIVE
            // allows us to move the cursor both forward and backwards through the RowSet
            // we get from this statement.

            // Another option is TYPE_SCROLL_SENSITIVE, which means the ResultSet will be updated when
            // something *else* changes the database. If your DB server was shared, you might need to be concerned about this.)

            // The TableModel will need to go forward and backward through the ResultSet.
            // by default, you can only move forward - it's less
            // resource-intensive than being able to go in both directions.
            // If you set one argument, you need the other.
            // The second one (CONCUR_UPDATABLE) means you will be able to change the ResultSet and these
            // changes will be made to the DB.... so long as you have a table with a primary key in it. (Otherwise
            // your database isn't able to definitively identify what has been changed).
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Does the table exist? If not, create it.
            if (!trainerTableExists()) {

                //Create a table in the database
                String createTableSQL = "CREATE TABLE " + Trainer_Table_Name + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + Trainer_COLUMN + " varchar(50), " + Trainee_COLUMN + " varchar(50), " + Phone_COLUMN + " varchar(15), " + Date_COLUMN + " varchar(30) ," + Training_COLUMN + " VARCHAR(50)," +
                        Facility_COLUMN + " VARCHAR(50)," + Comments_COLUMN + " varchar(200), PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created Training_Appointments table");

                // Add some test data -
                //Here we have to specify which columns the data will go into, because we want to omit the ID column and have MySQL fill it in for us.
                //But, since we are only adding 3 pieces of data for 4 columns, we have to specify which columns each data item is for.
//                String addDataSQL = "INSERT INTO " + Trainer_Table_Name + "(" + Trainer_COLUMN + ", " + Trainee_COLUMN + ", " + Date_COLUMN + ", " + Facility_COLUMN + ")" + " VALUES ('Michael Jordan', 'Tommy Ng', 'Jan 7', 'United Center')";
//                statement.executeUpdate(addDataSQL);
//                addDataSQL = "INSERT INTO " + Trainer_Table_Name +  "(" + Trainer_COLUMN + ", " + Trainee_COLUMN + ", " + Date_COLUMN + "," + Facility_COLUMN + ")" + " VALUES('Magic', 'Tommy Ng', 'Jan 7', 'United Center')";
//                statement.executeUpdate(addDataSQL);
//                addDataSQL = "INSERT INTO " + Trainer_Table_Name +  "(" + Trainer_COLUMN + ", " + Trainee_COLUMN + ", " + Date_COLUMN + "," + Facility_COLUMN + ")" + " VALUES ('Tracy Mcgrady', 'Tommy Ng', 'Jan 7', 'United Center')";
//                statement.executeUpdate(addDataSQL);

            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean trainerTableExists() throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + Trainer_Table_Name + "'";   //Can query the database schema
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;

    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown(){
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
