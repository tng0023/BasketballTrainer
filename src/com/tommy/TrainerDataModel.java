package com.tommy;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TransferQueue;

/**
 * Created by Tommy on 4/27/2016.
 */
    public class TrainerDataModel extends AbstractTableModel {

    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;

    public TrainerDataModel(ResultSet rs){
        this.resultSet = rs;
        setup();
    }

    private void setup(){

        countRows();

        try{
            colCount = resultSet.getMetaData().getColumnCount();

        }catch (SQLException se){
            System.out.println("Error counting columns" + se);
        }
    }

    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        setup();
    }

    private void countRows() {
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }
    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount(){
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col){
        try{
              //System.out.println("get value at, row = " +row);
            resultSet.absolute(row+1);
            Object o = resultSet.getObject(col+1);
            return o.toString();
        }catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return se.toString();

        }
    }



    //Delete row, return true if successful, false otherwise
    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 2) {
            return true;
        }
        return false;
    }

    @Override
    //This is called when user edits an editable cell
    public void setValueAt(Object newValue, int row, int col) {

        try {
            String updatedTrainee = newValue.toString();
            resultSet.absolute(row + 1);
            resultSet.updateString(TrainerDatabase.Trainee_COLUMN, updatedTrainee);
            resultSet.updateRow();
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("error changing rating " + e);
        }
    }


    //returns true if successful, false if error occurs
    public boolean insertRow(String trainer, String student,String phone, String date, String trainingDrill, String facility, String comments) {

        try {
            //Move to insert row, insert the appropriate data in each column, insert the row, move cursor back to where it was before we started
            resultSet.moveToInsertRow();
            resultSet.updateString(TrainerDatabase.Trainer_COLUMN, trainer);
            resultSet.updateString(TrainerDatabase.Trainee_COLUMN, student);
            resultSet.updateString(TrainerDatabase.Phone_COLUMN, phone);
            resultSet.updateString(TrainerDatabase.Date_COLUMN, date);
//            resultSet.updateString(TrainerDatabase.Time_COLUMN, time);
            resultSet.updateString(TrainerDatabase.Facility_COLUMN, facility);
            resultSet.updateString(TrainerDatabase.Training_COLUMN, trainingDrill);
            resultSet.updateString(TrainerDatabase.Comments_COLUMN, comments);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }

    }

    @Override
    public String getColumnName(int col){
        //Get from ResultSet metadata, which contains the database column names
        try {
            return resultSet.getMetaData().getColumnName(col + 1);
        } catch (SQLException se) {
            System.out.println("Error fetching column names" + se);
            return "?";
        }
}


}

