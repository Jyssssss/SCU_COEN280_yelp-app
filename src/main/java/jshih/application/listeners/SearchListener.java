package jshih.application.listeners;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import jshih.application.Application;
import jshih.application.viewmodels.BusinessResult;
import jshih.model.helpers.DayTimeHelper;

public class SearchListener extends BaseListener {

    public SearchListener(Application application) {
        super(application);
        subComponents = List.of(daysBox, fromBox, toBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            List<String> mains = getPanelChecked(mainInnerPanel);
            if (mains.isEmpty())
                JOptionPane.showMessageDialog(null, "Please select at least one main category.");
            else {
                List<String> subs = getPanelChecked(subInnerPanel);
                List<String> attrs = getPanelChecked(attrInnerPanel);
                String location = (String) locationBox.getSelectedItem();
                String day = (String) daysBox.getSelectedItem();
                String from = (String) fromBox.getSelectedItem();
                String to = (String) toBox.getSelectedItem();
                Integer fromTime = from != null ? DayTimeHelper.getOpenTime(from).getTime() : null;
                Integer toTime = to != null ? DayTimeHelper.getOpenTime(to).getTime() : null;
                List<BusinessResult> results = businessQuerier.getBusinessResults(mains, subs, attrs, location, day,
                        fromTime, toTime, getMainSearchFor(), getSubSearchFor(), getAttrSearchFor());

                DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
                model.setRowCount(0);
                results.forEach(r -> model.addRow(new Object[] { r.getName(), r.getAddress(), r.getCity(), r.getState(),
                        r.getStars(), r.getReviews(), r.getCheckins(), r.getBusinessUid() }));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
