package com.tommy;

import javafx.scene.control.Spinner;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.StringContent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tommy on 4/27/2016.
 */
public class TrainerForm extends JFrame implements WindowListener {


    private JPanel rootPanel;
    private JTable trainerDataTable;
    private JTextField trainerTextField;
    private JTextField traineeTextField;
    private JButton addToScheduleButton;
    private JButton deleteFromScheduleButton;
    private JButton quitButton;
    private JLabel traineeName;
    private JLabel dateLabel;
    private JLabel facilityLabel;
    private JLabel trainerLabel;
    private JComboBox facilityComboBox;
    private JComboBox trainingDrillComboBox;
    private JLabel trainingDrillLabel;
    private JSpinner dateSpinner;
    private JTextField phoneTextField;
    private JComboBox trainerComboBox;
    private JButton addTrainer;
    private JTextField commentsTextField;
    private JLabel commentsLabel;
    private JButton addNewFacilityButton;

    TrainerForm(final TrainerDataModel trainerDataTableModel) {

        setContentPane(rootPanel);

        trainers();
        facilityLocation();
        trainingDrill();
        setPreferredSize(new Dimension(1500, 900));
        pack();
        setTitle("My Basketball Trainer");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //set date spinner
        dateSpinner.setModel(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "EEEE, MM/dd/yyyy, hh:00 a"));


        //set time spinner
        //spinnerTime.setModel(new SpinnerDateModel());
        //spinnerTime.setEditor(new JSpinner.DateEditor(spinnerTime, "h:mm a"));

        //set up JTable
        trainerDataTable.setGridColor(Color.BLACK);
        trainerDataTable.setModel(trainerDataTableModel);


        addToScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String traineeData = traineeTextField.getText();

                if (traineeData == null || traineeData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a trainee name");
                    return;
                }

                //takes phone number and formats it
                String phoneData = "(" + phoneTextField.getText().substring(0,3) + ") " + phoneTextField.getText().substring(3, 6) + "-" +
                                        phoneTextField.getText().substring(6, 10);


                boolean valid = phoneNumber(phoneData);
                if (!valid) {
                    phoneTextField.setText("");
                    return;
                }

                if (phoneData == null || phoneData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a phone number");
                    return;
                }

                String commentsData = commentsTextField.getText();

                if (commentsData == null || commentsData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter notes regarding appointment");
                    return;
                }

                traineeTextField.setText("");
                phoneTextField.setText("");
                commentsTextField.setText("");

                Date date1 = (Date) dateSpinner.getModel().getValue();
                //Date time1 = (Date)spinnerTime.getModel().getValue();


                boolean insertedRow = trainerDataTableModel.insertRow((String) trainerComboBox.getSelectedItem(), traineeData, phoneData, date1.toString(), (String) trainingDrillComboBox.getSelectedItem(),
                        (String) facilityComboBox.getSelectedItem(), commentsData);

                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new appointment");
                }else
                    JOptionPane.showMessageDialog(rootPane, "New appointment scheduled!");
                }

        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrainerDatabase.shutdown();
                System.exit(0);
            }
        });

        deleteFromScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = trainerDataTable.getSelectedRow();

                if (currentRow == -1) { // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose an appointment to delete");
                }
                boolean deleted = trainerDataTableModel.deleteRow(currentRow);
                if (deleted) {
                    TrainerDatabase.loadAllTrainers();
                    JOptionPane.showMessageDialog(rootPane, "Appointment deleted!");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting appointment");
                }
            }
        });

        //button to prompt user to add new trainer to list
        addTrainer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cc = JOptionPane.showInputDialog(rootPane, "Add new trainer");
                if (cc == null) {
                    JOptionPane.showMessageDialog(rootPane, "Type in a new trainer");
                    return;
                } else if (cc.isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "Type in a new trainer");
                return;}
            else
                    trainerComboBox.addItem(cc);
                    JOptionPane.showMessageDialog(rootPane, "New trainer added!");
            }
        });
        addNewFacilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cc = JOptionPane.showInputDialog(rootPane, "Add new facility");
                if (cc == null) {
                    JOptionPane.showMessageDialog(rootPane, "Type in a new facility");
                    return;
                } else if (cc.isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "Type in a new facility");
                    return;}
                else
                    facilityComboBox.addItem(cc);
                JOptionPane.showMessageDialog(rootPane, "New facility added!");
            }
        });
    }

    // adds facilities to combobox
    private void facilityLocation() {

        facilityComboBox.addItem("MCTC Gym");
        facilityComboBox.addItem("Metropolitan State Gym");
        facilityComboBox.addItem("University of Minnesota Rec Center");
        facilityComboBox.addItem("YMCA Downtown Minneapolis");
        facilityComboBox.addItem("Lifetime Fitness - Target Center");

    }

    //adds training  drills to combobox
    private void trainingDrill() {

        trainingDrillComboBox.addItem("Ballhandling");
        trainingDrillComboBox.addItem("Shooting");
        trainingDrillComboBox.addItem("Defense");
        trainingDrillComboBox.addItem("Offense");
    }

    //adds trainer to combobox
    private void trainers() {

        trainerComboBox.addItem("Michael Jordan");
        trainerComboBox.addItem("Steve Kerr");
        trainerComboBox.addItem("Greg Popovich");
        trainerComboBox.addItem("Tom Thibideau");
    }

    //validates phone number
    public boolean phoneNumber(String ph) {
        if (ph.length() < 14) {
            JOptionPane.showMessageDialog(rootPane, "Please enter a valid phone number (do not use dashes)");
            return false;
        } else {
        }
        return true;
    }


    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        TrainerDatabase.shutdown();}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}