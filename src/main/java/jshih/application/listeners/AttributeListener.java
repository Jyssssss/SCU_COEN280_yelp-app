package jshih.application.listeners;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import jshih.application.Application;

public class AttributeListener extends BaseListener {

    public AttributeListener(Application application) {
        super(application);
        subComponents = List.of(locationBox, daysBox, fromBox, toBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            removeAndRepaintAll();
            List<String> attrChecks = getPanelChecked(attrInnerPanel);
            if (!attrChecks.isEmpty()) {
                var locations = businessQuerier.getLocations(getPanelChecked(mainInnerPanel),
                        getPanelChecked(subInnerPanel), attrChecks, getMainSearchFor(), getSubSearchFor(),
                        getAttrSearchFor());
                        
                locationBox.addItem(null);
                locations.forEach(location -> locationBox.addItem(location));
                locationBox.revalidate();
                locationBox.repaint();
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
