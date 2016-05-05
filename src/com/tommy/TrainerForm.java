package com.tommy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

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

    TrainerForm(final TrainerDataModel trainerDataTableModel) {

        setContentPane(rootPanel);


        facilityLocation();
        trainingDrill();
        setPreferredSize(new Dimension(1000,800));
        pack();
        setTitle("My Basketball Trainer");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //set date spinner
        dateSpinner.setModel(new SpinnerDateModel());

        //set up JTable
        trainerDataTable.setGridColor(Color.BLACK);
        trainerDataTable.setModel(trainerDataTableModel);


        addToScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Get Trainer name, make sure not blank
                String trainerData = trainerTextField.getText();

                if (trainerData == null || trainerData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a trainer name");
                    return;
                }

                String traineeData = traineeTextField.getText();

                if (traineeData == null || traineeData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a trainee name");
                    return;
                }

                String phoneData = phoneTextField.getText();

                if(phoneData == null || phoneData.trim().equals("")){
                    JOptionPane.showMessageDialog(rootPane,"Please enter a phone number");
                    return;
                }

                trainerTextField.setText("");
                traineeTextField.setText("");
                phoneTextField.setText("");


                Date date1 = (Date)dateSpinner.getModel().getValue();

                    boolean insertedRow = trainerDataTableModel.insertRow(trainerData, traineeData, phoneData, date1.toString(), (String)trainingDrillComboBox.getSelectedItem(),(String)facilityComboBox.getSelectedItem());

                    if (!insertedRow) {
                        JOptionPane.showMessageDialog(rootPane, "Error adding new appointment");
                    }else {
                        JOptionPane.showMessageDialog(rootPane, "New appointment scheduled!");

                    }

                //date1.toString().compareTo(TrainerDatabase.Date_COLUMN);
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
                    JOptionPane.showMessageDialog(rootPane,"Appointment deleted!");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting appointment");
                }
            }
        });
    }
        private void facilityLocation(){

            facilityComboBox.addItem("MCTC Gym");
            facilityComboBox.addItem("Metropolitan State Gym");
            facilityComboBox.addItem("University of Minnesota Rec Center");
            facilityComboBox.addItem("YMCA Downtown Minneapolis");
            facilityComboBox.addItem("Lifetime Fitness - Target Center");

    }

        private void trainingDrill(){

            trainingDrillComboBox.addItem("Ballhandling");
            trainingDrillComboBox.addItem("Shooting");
            trainingDrillComboBox.addItem("Defensive Drills");
            trainingDrillComboBox.addItem("Offensive Drills");
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
