package com.nilhcem.fakesmtp.gui;

import com.nilhcem.fakesmtp.core.I18n;
import com.nilhcem.fakesmtp.gui.info.ClearAllButton;
import com.nilhcem.fakesmtp.gui.info.NbReceivedLabel;
import com.nilhcem.fakesmtp.gui.info.PortTextField;
import com.nilhcem.fakesmtp.gui.info.SaveMsgField;
import com.nilhcem.fakesmtp.gui.info.StartServerButton;
import com.nilhcem.fakesmtp.gui.tab.LastMailPane;
import com.nilhcem.fakesmtp.gui.tab.LogsPane;
import com.nilhcem.fakesmtp.gui.tab.MailsListPane;
import com.nilhcem.fakesmtp.model.UIModel;
import com.nilhcem.fakesmtp.server.MailSaver;
import com.nilhcem.fakesmtp.server.SMTPServerHandler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Observable;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Provides the main panel of the application, which will contain all the components.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class MainPanel {
	private UIModel uiModel;
	// I18n
	private I18n i18n = I18n.INSTANCE;

	// Panel and layout
	private MigLayout layout;
	private JPanel mainPanel;

	// Directory chooser
	private DirChooser dirChooser;

	// Port
	private JLabel portLabel;
	private PortTextField portText;
	private StartServerButton startServerBtn;

	// Messages received
	private JLabel receivedLabel;
	private NbReceivedLabel nbReceivedLabel;

	// Save incoming messages to
	private JLabel saveMessages;
	private SaveMsgField saveMsgTextField;

	// Tab pane
	private JTabbedPane tabbedPane;
	private LogsPane logsPane;
	private MailsListPane mailsListPane;
	private LastMailPane lastMailPane;

	// Clear all
	private ClearAllButton clearAll;
	private SMTPServerHandler smtpServerHandler;
	private int port;

	/**
	 * Creates the main panel.
	 * <p>
	 * To create the main panel, the method will first have to handle components interactions by
	 * adding observers to observable elements, then the method will build the GUI by placing all the
	 * components in the main panel.
	 * </p>
	 *
	 * @param menu the menu bar which will notify the directory file chooser.
	 */
	public MainPanel(Observable menu, UIModel uiModel, SMTPServerHandler smtpServerHandler, PortTextField portTextField, MailsListPane mailsListPane, DirChooser dirChooser, MigLayout migLayout, JLabel portLabel, StartServerButton startServerButton, JLabel receivedLabel, NbReceivedLabel nbReceivedLabel, JLabel saveMessages, SaveMsgField saveMsgField, JTabbedPane tabbedPane, LogsPane logsPane, LastMailPane lastMailPane, ClearAllButton clearAllButton, int port, JPanel mainPanel) {
		this.uiModel = uiModel;
		this.smtpServerHandler = smtpServerHandler;
		this.layout = migLayout;
		this.dirChooser = dirChooser;
		this.portLabel = portLabel;
		this.portText = portTextField;
		this.startServerBtn = startServerButton;
		this.receivedLabel = receivedLabel;
		this.nbReceivedLabel = nbReceivedLabel;
		this.saveMessages = saveMessages;
		this.saveMsgTextField = saveMsgField;
		this.tabbedPane = tabbedPane;
		this.logsPane = logsPane;
		this.mailsListPane = mailsListPane;
		this.lastMailPane = lastMailPane;
		this.clearAll = clearAllButton;
		this.port = port;
		this.mainPanel = mainPanel;
		assignLabelsToFields();
		addObservers(menu);
	}

	/**
	 * Returns the JPanel object.
	 *
	 * @return the JPanel object.
	 */
	public JPanel get() {
		return mainPanel;
	}

	/**
	 * Handles components interactions by adding observers to observable elements.
	 * <p>
	 * The interactions are the following:
	 * <ul>
	 *   <li>Open the directory chooser when clicking on the menu/the save message field;</li>
	 *   <li>Enable/Disable the port field when the server starts;</li>
	 *   <li>Set the new directory, once a folder is selected;<li>
	 *   <li>Notify components when a message is received;</li>
	 *   <li>Notify components when the user wants to clear them all.</li>
	 * </ul>
	 * </p>
	 *
	 * @param menu the menu bar which will notify the directory file chooser.
	 */
	private void addObservers(Observable menu) {
		// When we want to select a directory
		menu.addObserver(this.dirChooser);
		this.saveMsgTextField.addObserver(this.dirChooser);

		// When we click on "start server" button
		this.startServerBtn.addObserver(this.portText);

		// When we press "Enter" on the PortTextField
		this.portText.addObserver(this.startServerBtn);

		// Once we chose a directory
		this.dirChooser.addObserver(this.saveMsgTextField);

		// When a message is received
		MailSaver mailSaver = this.smtpServerHandler.getMailSaver();
		mailSaver.addObserver(this.nbReceivedLabel);
		mailSaver.addObserver(this.mailsListPane);
		mailSaver.addObserver(this.lastMailPane);
		mailSaver.addObserver(this.clearAll);

		// When we click on "clear all"
		this.clearAll.addObserver(this.nbReceivedLabel);
		this.clearAll.addObserver(this.mailsListPane);
		this.clearAll.addObserver(this.logsPane);
		this.clearAll.addObserver(this.lastMailPane);
	}

	/**
	 * Places all components in the panel.
	 */
	public void buildGUI() {
		// Port / Start server
		this.mainPanel.add(this.portLabel);
		this.mainPanel.add(this.portText.get(), "w 60!");
		this.mainPanel.add(this.startServerBtn.get(), "span, w 165!");

		// Save messages to...
		this.mainPanel.add(this.saveMessages);
		this.mainPanel.add(this.saveMsgTextField.get(), "span, w 230!");

		// Nb received
		this.mainPanel.add(this.receivedLabel);
		this.mainPanel.add(this.nbReceivedLabel.get(), "span");

		// Tab pane
		this.tabbedPane.add(
				this.mailsListPane.get(),
				this.i18n.get("mainpanel.tab.mailslist")
		);
		this.tabbedPane.add(
				this.logsPane.get(),
				this.i18n.get("mainpanel.tab.smtplog")
		);
		this.tabbedPane.add(
				this.lastMailPane.get(),
				this.i18n.get("mainpanel.tab.lastmessage")
		);
		this.mainPanel.add(
				this.tabbedPane,
				"span, grow"
		);

		// Clear all
		this.mainPanel.add(
				this.clearAll.get(),
				"span, center"
		);
		checkArgs();
	}

	/**
	 * Checks command line arguments and toggles components if necessary.
	 * <p>
	 * <ul><li>if the user has chosen a different port, then specifies it in the port text field.</li>
	 * <li>if the user has chosen to auto-start the SMTP server, then it toggles automatically the "start server" button.</li></ul>
	 * </p>
	 */
	private void checkArgs() {
		expect(this.port, "port").not().toBeEqual(0).check();
		expect(this.portText, "portText").not().toBeNull().check();
		this.portText.setText(String.valueOf(this.port));
		this.startServerBtn.toggleButton();
		this.saveMsgTextField.get().setEnabled(false);
	}

	/**
	 * Assigns labels to components, for accessibility purpose.
	 */
	private void assignLabelsToFields() {
		this.portLabel.setLabelFor(this.portText.get());
		this.saveMessages.setLabelFor(this.saveMsgTextField.get());
		this.receivedLabel.setLabelFor(this.nbReceivedLabel.get());
	}

	/**
	 * Returns reference to portText field. Used for saving last values to file
	 *
	 * @return reference to portText field. Used for saving last values to file
	 */
	public PortTextField getPortText() {
		return this.portText;
	}

	/**
	 * Returns reference to saveMsgTextField. Used for saving last values to file
     *
     * @return reference to saveMsgTextField. Used for saving last values to file
	 */
	public SaveMsgField getSaveMsgTextField() {
		return this.saveMsgTextField;
	}
}
