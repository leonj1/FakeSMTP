package com.nilhcem.fakesmtp.gui;

import com.nilhcem.fakesmtp.core.I18n;
import com.nilhcem.fakesmtp.gui.info.SaveMsgField;
import com.nilhcem.fakesmtp.model.UIModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Provides a graphical directory chooser dialog.
 * <p>
 * The directory chooser is used to select the folder where emails will be saved in.<br>
 * It can be launched from the menu bar, or from the main panel.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class DirChooser extends Observable implements Observer {

	private JFileChooser dirChooser;
	private Component parent;
	private UIModel uiModel;
	private String emailDefaultDirectory;

	/**
	 * Creates a {@code JFileChooser} component and sets it to be for directories only.
	 *
	 * @param parent the component from where the chooser will be launched <i>(should be the main panel of the application)</i>.
	 * @param emailDefaultDirectory
	 */
	public DirChooser(JFileChooser dirChooser, String applicationName, UIModel uiModel, String emailDefaultDirectory) {
		this.uiModel = uiModel;
		this.emailDefaultDirectory = emailDefaultDirectory;
		this.dirChooser = dirChooser;
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirChooser.setDialogTitle(
				String.format(
						I18n.INSTANCE.get("dirchooser.title"),
						applicationName
				)
		);
		dirChooser.setApproveButtonText(I18n.INSTANCE.get("dirchooser.approve.btn"));
	}


	public void setParent(Component parent) {
		this.parent = parent;
	}

	/**
	 * Opens the folder selection.
	 * <p>
	 * This method will be called by an {@code Observable} element:
     * </p>
	 * <ul>
	 *   <li>The {@link MenuBar};</li>
	 *   <li>Or the {@link SaveMsgField}.</li>
	 * </ul>
	 *
	 * @param o the observable element which will notify this class.
	 * @param arg optional parameters (not used).
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof MenuBar || o instanceof SaveMsgField) {
			openFolderSelection();
		}
	}

	/**
	 * Opens the folder selection dialog and notify observers once the directory is selected.
	 * <p>
	 * The only observer notified is the {@link SaveMsgField}.
	 * </p>
	 */
	private void openFolderSelection() {
		File filePath = new File(this.emailDefaultDirectory);
		this.dirChooser.setCurrentDirectory(filePath);

		int result = this.dirChooser.showOpenDialog(this.parent);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedDir = this.dirChooser.getSelectedFile();
			if (selectedDir != null) {
				this.uiModel.setSavePath(selectedDir.getAbsolutePath());
				setChanged();
				notifyObservers();
			}
		}
	}
}
