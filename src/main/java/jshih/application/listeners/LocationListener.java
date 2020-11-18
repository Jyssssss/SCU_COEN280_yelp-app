package jshih.application.listeners;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;

import jshih.application.Application;
import jshih.application.viewmodels.BusinessDaysTimes;

public class LocationListener extends BaseListener {

    public LocationListener(Application application) {
        super(application);
        subComponents = List.of(daysBox, fromBox, toBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox) e.getSource();
        String location = (String) comboBox.getSelectedItem();
        try {
            BusinessDaysTimes daysTimes = businessQuerier.getDaysTimes(getPanelChecked(mainInnerPanel),
                    getPanelChecked(subInnerPanel), getPanelChecked(attrInnerPanel), location, getMainSearchFor(),
                    getSubSearchFor(), getAttrSearchFor());
            removeAndRepaintAll();
            daysBox.addItem(null);
            fromBox.addItem(null);
            toBox.addItem(null);
            daysTimes.getDays().forEach(d -> daysBox.addItem(d));
            daysTimes.getFroms().forEach(t -> fromBox.addItem(String.format("%02d:%02d", t.getHour(), t.getMinute())));
            daysTimes.getTos().forEach(t -> toBox.addItem(String.format("%02d:%02d", t.getHour(), t.getMinute())));
            daysBox.revalidate();
            fromBox.revalidate();
            toBox.revalidate();
            daysBox.repaint();
            fromBox.repaint();
            toBox.repaint();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
