package com.nilhcem.fakesmtp.gui.listeners;

import com.nilhcem.fakesmtp.gui.MainFrame;
import com.nilhcem.fakesmtp.gui.TrayPopup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Responsible for window minimizing and closing. If SystemTray is supported,
 * the window MainFrame is minimized to an icon.
 *
 * @author Vest
 * @since 2.1
 */
public class MainWindowListener extends WindowAdapter {

	private TrayIcon trayIcon;
	private final boolean useTray;
	private String applicationName;
	private String applicationIconPath;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowListener.class);

	public MainWindowListener(boolean applicationTrayInUse, String applicationName, String applicationIconPath) {
		useTray = (SystemTray.isSupported() && applicationTrayInUse);
		this.applicationIconPath = applicationIconPath;
		this.applicationName = applicationName;
	}

	public void setMainFrame(MainFrame mainFrame) {
		if (useTray) {
			final TrayPopup trayPopup = new TrayPopup(mainFrame, applicationName);

			final Image iconImage = Toolkit.getDefaultToolkit()
					.getImage(getClass()
							.getResource(applicationIconPath)
					);

			trayIcon = new TrayIcon(iconImage);

			trayIcon.setImageAutoSize(true);
			trayIcon.setPopupMenu(trayPopup.get());
		}
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		super.windowStateChanged(e);
		if (!useTray) {
			return;
		}

		final SystemTray tray = SystemTray.getSystemTray();
		final JFrame frame = (JFrame) e.getSource();

		if ((e.getNewState() & Frame.ICONIFIED) != 0) {
			try {
				/* Displays the window when the icon is clicked twice */
				trayIcon.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						int state = frame.getExtendedState();
						state &= ~Frame.ICONIFIED;

						frame.setExtendedState(state);
						frame.setVisible(true);

						tray.remove(trayIcon);

						trayIcon.removeActionListener(this);
					}
				});

				tray.add(trayIcon);
				frame.dispose();
			} catch (AWTException ex) {
				LOGGER.error("Couldn't create a tray icon, the minimizing is not possible", ex);
			}
		} else {
			frame.setVisible(true);
		}
	}
}
