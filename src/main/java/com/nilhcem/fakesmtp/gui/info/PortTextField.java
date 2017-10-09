package com.nilhcem.fakesmtp.gui.info;

import com.nilhcem.fakesmtp.core.I18n;
import com.nilhcem.fakesmtp.model.UIModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Text field in which will be written the desired SMTP port.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class PortTextField extends Observable implements Observer {

	private JTextField portTextField;
	private final UIModel uiModel;

	/**
	 * Creates the port field object and adds a listener on change to alert the presentation model.
	 * <p>
	 * The default port's value is defined in the configuration.properties file.<br>
	 * Each time the port is modified, the port from the {@link UIModel} will be reset.
	 * </p>
	 */
	public PortTextField(final UIModel uiModel, String defaultPort, JTextField jTextField) {
		this.uiModel = uiModel;
		this.portTextField = jTextField;
		this.portTextField.setToolTipText(I18n.INSTANCE.get("porttextfield.tooltip"));
		this.portTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				warn();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				warn();
			}

			private void warn() {
				expect(portTextField, "portTextField").not().toBeNull().check();
//				expect(portTextField.getText(), "portTextField.getText()").not().toBeNull().not().toBeBlank().check();
				expect(uiModel, "uiModel").not().toBeNull().check();
//				uiModel.setPort(Integer.parseInt(portTextField.getText()));
				uiModel.setPort(8025);
			}
		});

		portTextField.setText(defaultPort);
		portTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
			}
		});
	}

	/**
	 * Returns the JTextField object.
	 *
	 * @return the JTextField object.
	 */
	public JTextField get() {
		return portTextField;
	}

	/**
	 * Sets the specified port in the text field only if this latter is not {@code null}.
	 *
	 * @param portStr the port to set.
	 */
	public void setText(String portStr) {
		if (portStr != null && !portStr.isEmpty()) {
			this.portTextField.setText(portStr);
		}
	}

	/**
	 * Enables or disables the port text field.
	 * <p>
	 * When the element will receive an action from the {@link StartServerButton} object,
	 * it will enable or disable the port, so that the user can't modify it
	 * when the server is already launched.
	 * </p>
	 *
	 * @param o the observable element which will notify this class.
	 * @param arg optional parameters (not used).
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof StartServerButton) {
			portTextField.setEnabled(!this.uiModel.isStarted());
		}
	}
}
