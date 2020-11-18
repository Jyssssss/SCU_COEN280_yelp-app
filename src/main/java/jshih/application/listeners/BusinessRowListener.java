package jshih.application.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jshih.application.Application;
import jshih.application.services.ReviewQuerier;
import jshih.application.viewmodels.ReviewResult;

public class BusinessRowListener extends MouseAdapter {
    ReviewQuerier reviewQuerier;
    JTable reviewResultTable;
    JDialog reviewDialog;

    public BusinessRowListener(Application application) {
        this.reviewQuerier = application.getReviewQuerier();
        this.reviewResultTable = application.getReviewResultTable();
        this.reviewDialog = application.getReviewDialog();
    }

    public void mouseClicked(MouseEvent me) {
        try {
            if (me.getClickCount() == 2) {
                JTable businessTable = (JTable) me.getSource();
                int row = businessTable.getSelectedRow();
                int column = businessTable.getColumn("BusinessUId").getModelIndex();
                List<ReviewResult> reviews = reviewQuerier
                        .getReviewResults((String) businessTable.getValueAt(row, column));

                DefaultTableModel model = (DefaultTableModel) reviewResultTable.getModel();
                model.setRowCount(0);
                reviews.forEach(r -> model.addRow(new Object[] { r.getReviewDate(), r.getUserName(), r.getStars(),
                        r.getText(), r.getFunnyVotes(), r.getCoolVotes(), r.getUsefulVotes() }));

                reviewDialog.pack();
                reviewDialog.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
