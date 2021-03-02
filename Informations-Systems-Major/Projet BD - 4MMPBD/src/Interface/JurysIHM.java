package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;

public class JurysIHM extends JPanel {
    private Connection conn;
    private JComboBox<KeyValue> listeNumeros;
    private DefaultTableModel modelExpertNonSpe;
    private DefaultTableModel modelExpertSpe;
    private JurysController controller;
    private JButton btnValider;
    private int selectedNumero;

    public JurysIHM(Connection conn) throws SQLException {
        this.conn = conn;
        this.listeNumeros = new JComboBox<>();
        Object[] columnNames = {"Identifiant", "Nom", "Participe"};
        this.modelExpertNonSpe = new DefaultTableModel(new Object[][]{}, columnNames);
        this.modelExpertSpe = new DefaultTableModel(new Object[][]{}, columnNames);
        this.controller = new JurysController(conn, this);
        this.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(top, BorderLayout.NORTH);
        top.setLayout(new GridLayout(1,2));
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Select Numero
        JPanel containerNumero = new JPanel();
        top.add(containerNumero);
        containerNumero.add(new JLabel("Choisir un numero :", JLabel.TRAILING));
        containerNumero.add(listeNumeros);
        this.listeNumeros.addActionListener(actionEvent -> {
            controller.clearExpertsSelectionnes();
            KeyValue s = (KeyValue) listeNumeros.getSelectedItem();//get the selected item
            if (s != null) {
                if (s.getKey() != -1) {
                    modelExpertNonSpe.setRowCount(0);
                    modelExpertSpe.setRowCount(0);
                    controller.traiterSelectNumero(s.getKey());
                    selectedNumero = s.getKey();
                } else {
                    modelExpertNonSpe.setRowCount(0);
                    modelExpertSpe.setRowCount(0);
                    controller.traiterSelectNumero(s.getKey());
                    selectedNumero = -1;
                }
            }
        });



        // Select Experts
        JPanel tablesPanel = new JPanel();
        this.add(tablesPanel, BorderLayout.CENTER);
        tablesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        JPanel container1 = new JPanel();
        JLabel labelNonSpe = new JLabel("Selectionnez 2 experts non spécialisés");
        container1.add(labelNonSpe);
        JPanel container2 = new JPanel();
        JLabel labelSpe = new JLabel("Selectionnez 3 experts spécialisés");
        container2.add(labelSpe);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        tablesPanel.add(container1, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        tablesPanel.add(container2, c);

        JTable tableExpertNonSpe = new JurysJTable(modelExpertNonSpe);
        tableExpertNonSpe.getModel().addTableModelListener(new CheckBoxModelListener());
        tableExpertNonSpe.setPreferredScrollableViewportSize(tableExpertNonSpe.getPreferredSize());
        JScrollPane scrollPaneExpertNonSpe = new JScrollPane(tableExpertNonSpe);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1;
        tablesPanel.add(scrollPaneExpertNonSpe, c);



        JTable tableExpertSpe = new JurysJTable(modelExpertSpe);
        tableExpertSpe.getModel().addTableModelListener(new CheckBoxModelListener());
        tableExpertSpe.setPreferredScrollableViewportSize(tableExpertSpe.getPreferredSize());
        JScrollPane scrollPaneExpertSpe = new JScrollPane(tableExpertSpe);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1;
        tablesPanel.add(scrollPaneExpertSpe, c);



        // Bottom
        JPanel bottom = new JPanel();
        this.add(bottom, BorderLayout.SOUTH);
        bottom.setLayout(new BorderLayout());
        this.btnValider = new JButton();
        JPanel containerBtn = new JPanel();
        bottom.add(containerBtn);
        this.btnValider.setText("Valider");
        this.btnValider.addActionListener(controller);
        this.btnValider.setEnabled(false);
        containerBtn.add(this.btnValider, BorderLayout.CENTER);
    }

    public JComboBox<KeyValue> getListeNumeros() {
        return listeNumeros;
    }

    public DefaultTableModel getModelExpertNonSpe() {
        return modelExpertNonSpe;
    }

    public DefaultTableModel getModelExpertSpe() {
        return modelExpertSpe;
    }

    public int getSelectedNumero() {
        return selectedNumero;
    }

    public class CheckBoxModelListener implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 2) {
                TableModel model = (TableModel) e.getSource();
                Boolean checked = (Boolean) model.getValueAt(row, column);
                Integer id = (Integer) model.getValueAt(row, 0);
                if (checked) {
                    boolean isSpe = model == modelExpertSpe;
                    String nom = (String) model.getValueAt(row, 1);
                    controller.addExpertsSelectionne(id, nom, isSpe);
                } else {
                    controller.removeExpertsSelectionne(id);
                }

                if (controller.getExpertsSelectionnes(false).size() == 2 && controller.getExpertsSelectionnes(true).size() == 3) {
                    btnValider.setEnabled(true);
                } else {
                    btnValider.setEnabled(false);
                }
            }
        }
    }
}

