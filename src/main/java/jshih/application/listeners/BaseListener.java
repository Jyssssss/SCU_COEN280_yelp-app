package jshih.application.listeners;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;

import jshih.application.Application;
import jshih.application.enums.SearchFor;
import jshih.application.services.BusinessQuerier;
import jshih.application.services.CategoryQuerier;

public abstract class BaseListener implements ActionListener {
    protected Application application;
    protected CategoryQuerier categoryQuerier;
    protected BusinessQuerier businessQuerier;
    protected JPanel mainInnerPanel;
    protected JPanel subInnerPanel;
    protected JPanel attrInnerPanel;
    protected JComboBox<String> locationBox;
    protected JComboBox<String> daysBox;
    protected JComboBox<String> fromBox;
    protected JComboBox<String> toBox;
    protected JComboBox<String> mainSearchForBox;
    protected JComboBox<String> subSearchForBox;
    protected JComboBox<String> attrSearchForBox;
    protected JTable resultTable;
    protected List<JComponent> subComponents;

    protected BaseListener(Application application) {
        this.application = application;
        this.categoryQuerier = application.getCategoryQuerier();
        this.businessQuerier = application.getBusinessQuerier();
        this.mainInnerPanel = application.getMainInnerPanel();
        this.subInnerPanel = application.getSubInnerPanel();
        this.attrInnerPanel = application.getAttrInnerPanel();
        this.locationBox = application.getLocationBox();
        this.daysBox = application.getDaysBox();
        this.fromBox = application.getFromBox();
        this.toBox = application.getToBox();
        this.mainSearchForBox = application.getMainSearchForBox();
        this.subSearchForBox = application.getSubSearchForBox();
        this.attrSearchForBox = application.getAttrSearchForBox();
        this.resultTable = application.getResultTable();
    }

    protected final void removeAndRepaintAll() {
        subComponents.forEach(c -> {
            if (c instanceof JPanel) {
                c.removeAll();
            } else if (c instanceof JComboBox) {
                ((JComboBox) c).removeAllItems();
            }
            c.repaint();
        });
    }

    protected final List<String> getPanelChecked(JPanel panel) {
        return Arrays.stream(panel.getComponents()).filter(c -> c instanceof JCheckBox && ((JCheckBox) c).isSelected())
                .map(c -> ((JCheckBox) c).getText()).collect(Collectors.toList());
    }

    protected final SearchFor getMainSearchFor() {
        return getSearchFor(mainSearchForBox);
    }

    protected final SearchFor getSubSearchFor() {
        return getSearchFor(subSearchForBox);
    }

    protected final SearchFor getAttrSearchFor() {
        return getSearchFor(attrSearchForBox);
    }

    private SearchFor getSearchFor(JComboBox<String> searchForBox) {
        return ((String) searchForBox.getSelectedItem()).equals("AND") ? SearchFor.AND : SearchFor.OR;
    }
}