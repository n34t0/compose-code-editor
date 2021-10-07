package com.intellij.ide.ui.laf;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.Disposable;
import com.intellij.ui.CollectionComboBoxModel;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import org.jetbrains.annotations.NotNull;


final class IpwLafManagerImpl extends LafManager {

    @Override
    public UIManager.LookAndFeelInfo[] getInstalledLookAndFeels() {
        return new UIManager.LookAndFeelInfo[0];
    }

    @Override
    public UIManager.LookAndFeelInfo getCurrentLookAndFeel() {
        return null;
    }

    @Override
    public LafReference getLookAndFeelReference() {
        return null;
    }

    @Override
    public ListCellRenderer<LafReference> getLookAndFeelCellRenderer() {
        return null;
    }

    @Override
    @NotNull
    public JComponent getSettingsToolbar() {
        return new JComponent() {};
    }

    @Override
    public void setCurrentLookAndFeel(UIManager.LookAndFeelInfo lookAndFeelInfo, boolean lockEditorScheme) { }

    @Override
    public CollectionComboBoxModel<LafReference> getLafComboBoxModel() {
        return new CollectionComboBoxModel<>();
    }

    @Override
    public UIManager.LookAndFeelInfo findLaf(LafReference reference) {
        return null;
    }

    @Override
    public void updateUI() { }

    @Override
    public void repaintUI() { }

    @Override
    public boolean getAutodetect() {
        return false;
    }

    @Override
    public void setAutodetect(boolean value) {}

    @Override
    public boolean getAutodetectSupported() {
        return false;
    }

    @Override
    public void setPreferredDarkLaf(UIManager.LookAndFeelInfo myPreferredDarkLaf) { }

    @Override
    public void setPreferredLightLaf(UIManager.LookAndFeelInfo myPreferredLightLaf) { }

    @Override
    public void addLafManagerListener(@NotNull LafManagerListener listener) { }

    @Override
    public void addLafManagerListener(@NotNull LafManagerListener listener, @NotNull Disposable disposable) { }

    @Override
    public void removeLafManagerListener(@NotNull LafManagerListener listener) { }
}
