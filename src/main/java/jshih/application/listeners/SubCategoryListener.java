package jshih.application.listeners;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JCheckBox;

import jshih.application.Application;

public class SubCategoryListener extends BaseListener {

    public SubCategoryListener(Application application) {
        super(application);
        subComponents = List.of(attrInnerPanel, locationBox, daysBox, fromBox, toBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            removeAndRepaintAll();
            List<String> subChecks = getPanelChecked(subInnerPanel);
            if (!subChecks.isEmpty()) {
                List<String> attributes = businessQuerier.getAttributeNames(getPanelChecked(mainInnerPanel), subChecks,
                        getMainSearchFor(), getSubSearchFor());
                attributes.forEach(attr -> {
                    JCheckBox checkBox = new JCheckBox(attr);
                    checkBox.addActionListener(new AttributeListener(this.application));
                    attrInnerPanel.add(checkBox);
                });

                attrInnerPanel.revalidate();
                attrInnerPanel.repaint();
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}