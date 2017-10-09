package com.nilhcem.fakesmtp.gui;

import com.nilhcem.fakesmtp.core.Configuration;
import com.nilhcem.fakesmtp.core.exception.UncaughtExceptionHandler;
import com.nilhcem.fakesmtp.gui.listeners.MainWindowListener;
import com.nilhcem.fakesmtp.model.UIModel;
import com.nilhcem.fakesmtp.server.SMTPServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Provides the main window of the application.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class MainFrame {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

	private JFrame mainFrame;
	private MenuBar menu;
	private MainPanel panel;
	private SMTPServerHandler smtpServerHandler;
	private Configuration configuration;
	private int defaultPort;
	private String emailDefaultDirectory;

	/**
	 * Creates the main window and makes it visible.
	 * <p>
	 * First, assigns the main panel to the default uncaught exception handler to display exceptions in this panel.<br><br>
	 * Before creating the main window, the application will have to set some elements, such as:
	 * </p>
	 * <ul>
	 *   <li>The minimum and default size;</li>
	 *   <li>The menu bar and the main panel;</li>
	 *   <li>An icon image;</li>
	 *   <li>A shutdown hook to stop the server, once the main window is closed.</li>
	 * </ul><br>
	 * <p>
	 * The icon of the application is a modified version from the one provided in "{@code WebAppers.com}"
	 * <i>(Creative Commons Attribution 3.0 License)</i>.
	 * </p>
	 */
	public MainFrame(MainPanel mainPanel, Dimension frameSize, MenuBar menuBar, MainWindowListener windowListener, String applicationTitle, String appIconPath, final SMTPServerHandler smtpServerHandler, int smtpPort, String outputDirectory, Configuration configuration, UIModel uiModel, String emailDefaultDirectory, int defaultPort) {
		this.configuration = configuration;
		this.defaultPort = defaultPort;
		this.mainFrame = new JFrame(applicationTitle);
		this.emailDefaultDirectory = emailDefaultDirectory;
		menu = menuBar;
        this.smtpServerHandler = smtpServerHandler;
        panel = mainPanel;
		((UncaughtExceptionHandler) Thread.getDefaultUncaughtExceptionHandler()).setParentComponent(panel.get());
		Image iconImage = Toolkit.getDefaultToolkit().getImage(
			getClass().getResource(appIconPath));

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		mainFrame.addWindowStateListener(windowListener); // used for TrayIcon
		mainFrame.setSize(frameSize);
		mainFrame.setMinimumSize(frameSize);

		mainFrame.setJMenuBar(menu.get());
		mainFrame.getContentPane().add(panel.get());
		mainFrame.setLocationRelativeTo(null); // Center main frame
		mainFrame.setIconImage(iconImage);

		// Add shutdown hook to stop server if enabled
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				smtpServerHandler.stopServer();
			}
		});

		// Restore last saved smtp port (if not overridden by the user)
		String port;
		if (smtpPort == 0) {
			port = String.valueOf(this.defaultPort);
		} else {
			port = String.valueOf(smtpPort);
		}
		panel.getPortText().setText(port);

		// Restore last emails directory (if not overridden by the user)
		String emailsDir = outputDirectory;
		if (emailsDir == null) {
			emailsDir = this.emailDefaultDirectory;
		}
		if (emailsDir != null && !emailsDir.isEmpty()) {
			panel.getSaveMsgTextField().get().setText(emailsDir);
			uiModel.setSavePath(emailsDir);
		}

		mainFrame.setVisible(true);
	}

	public void close() {
		LOGGER.debug("Closing the application and saving the configuration");

		this.configuration.set("smtp.default.port", panel.getPortText().get().getText());
		this.configuration.set("emails.default.dir", panel.getSaveMsgTextField().get().getText());

		try {
			this.configuration.saveToUserProfile();
		} catch (IOException ex) {
			LOGGER.error("Could not save configuration", ex);
		}
		// Check for SMTP server running and stop it
		if (this.smtpServerHandler.getSmtpServer().isRunning()) {
			this.smtpServerHandler.getSmtpServer().stop();
		}

		mainFrame.dispose();
	}
}
