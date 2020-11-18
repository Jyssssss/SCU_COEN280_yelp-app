package jshih.application.listeners;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JCheckBox;

import jshih.application.Application;

public class MainCategoryListener extends BaseListener {
    public MainCategoryListener(Application application) {
        super(application);
        subComponents = List.of(subInnerPanel, attrInnerPanel, locationBox, daysBox, fromBox, toBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            removeAndRepaintAll();
            List<String> subCategories = categoryQuerier.getSubCategoryNames(getPanelChecked(mainInnerPanel),
                    getMainSearchFor());

            subCategories.forEach(category -> {
                JCheckBox checkBox = new JCheckBox(category);
                checkBox.addActionListener(new SubCategoryListener(this.application));
                subInnerPanel.add(checkBox);
            });

            subInnerPanel.revalidate();
            subInnerPanel.repaint();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}