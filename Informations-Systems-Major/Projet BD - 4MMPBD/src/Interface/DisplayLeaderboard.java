package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Transaction.FetchStatistics;

/**
 * Created by nguyene on 11/26/19.
 */
public class DisplayLeaderboard extends JFrame {
    private ArrayList<Object[]> numeros;
    private String themeSelected;
    private FetchStatistics fetchStatistics;

    public DisplayLeaderboard(Connection conn, String themeSelected) throws SQLException {
        super();
        this.themeSelected = themeSelected;
        this.fetchStatistics = new FetchStatistics(conn);
        this.buildUI();
    }

    private void buildUI() throws SQLException {
        setTitle("Thème");
        setSize(400, 200);
        JPanel container = new JPanel();

        container.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JPanel mainPanel = new JPanel();
        JPanel bottom = new JPanel();

        top.add(new JLabel("Classement des numéros du thème : " + this.themeSelected));

        String[] columnsNames = {"Titre du numéro", "Moyenne"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columnsNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.numeros = this.fetchStatistics.getNumeroLeaderboardByTheme(this.themeSelected);
        for (Object[] row : this.numeros) {
            model.addRow(row);
        }
        JTable statisticsTable = new JTable(model);
        JScrollPane scrollPaneContainer = new JScrollPane(statisticsTable);
        statisticsTable.setPreferredScrollableViewportSize(statisticsTable.getPreferredSize());
        mainPanel.add(scrollPaneContainer);

        JButton close = new JButton("Fermer");
        bottom.add(close, BorderLayout.CENTER);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DisplayLeaderboard.this.dispatchEvent(new WindowEvent(DisplayLeaderboard.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        this.add(container);
        container.add(top, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(bottom, BorderLayout.SOUTH);
        this.setVisible(true);
    }
}
