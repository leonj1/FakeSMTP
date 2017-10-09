package com.nilhcem.fakesmtp;

import com.apple.eawt.Application;
import com.josemleon.CommandlineParser;
import com.josemleon.GetEffectiveProperty;
import com.josemleon.GetProperty;
import com.josemleon.Parser;
import com.josemleon.exceptions.PropertiesFileNotFoundException;
import com.nilhcem.fakesmtp.configs.AppProperties;
import com.nilhcem.fakesmtp.core.Configuration;
import com.nilhcem.fakesmtp.core.I18n;
import com.nilhcem.fakesmtp.core.exception.UncaughtExceptionHandler;
import com.nilhcem.fakesmtp.gui.DirChooser;
import com.nilhcem.fakesmtp.gui.MainFrame;
import com.nilhcem.fakesmtp.gui.MainPanel;
import com.nilhcem.fakesmtp.gui.MenuBar;
import com.nilhcem.fakesmtp.gui.info.ClearAllButton;
import com.nilhcem.fakesmtp.gui.info.NbReceivedLabel;
import com.nilhcem.fakesmtp.gui.info.PortTextField;
import com.nilhcem.fakesmtp.gui.info.SaveMsgField;
import com.nilhcem.fakesmtp.gui.info.StartServerButton;
import com.nilhcem.fakesmtp.gui.listeners.MainWindowListener;
import com.nilhcem.fakesmtp.gui.tab.LastMailPane;
import com.nilhcem.fakesmtp.gui.tab.LogsPane;
import com.nilhcem.fakesmtp.gui.tab.MailsListPane;
import com.nilhcem.fakesmtp.model.UIModel;
import com.nilhcem.fakesmtp.server.MailListener;
import com.nilhcem.fakesmtp.server.MailSaver;
import com.nilhcem.fakesmtp.server.SMTPServerHandler;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Entry point of the application.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class FakeSMTP {
    private static final Logger log = LoggerFactory.getLogger(FakeSMTP.class);
    private static final String APPLICATION_PROPERTIES = "application.properties";


    private FakeSMTP() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks command line arguments, sets some specific properties, and runs the main window.
     * <p>
     * Before opening the main window, this method will:
     * </p>
     * <ul>
     * <li>check command line arguments, and possibly display an error dialog,</li>
     * <li>set a default uncaught exception handler to intercept every uncaught exception;</li>
     * <li>use a custom icon in the Mac Dock;</li>
     * <li>set a property for Mac OS X to take the menu bar off the JFrame;</li>
     * <li>set a property for Mac OS X to set the name of the application menu item;</li>
     * <li>turn off the bold font in all components for swing default theme;</li>
     * <li>use the platform look and feel.</li>
     * </ul>
     *
     * @param args a list of command line parameters.
     */
    public static void main(final String[] args) throws Exception {
        // Starting here, we are going the job Spring would normally do
        Parser cmdlineParser = new CommandlineParser(args);
        final AppProperties appProperties = new AppProperties(
                new GetEffectiveProperty(
                        new GetProperty(
                                APPLICATION_PROPERTIES,
                                cmdlineParser
                        ),
                        cmdlineParser
                )
        );

        MailSaver mailSaver = new MailSaver(
                appProperties.getMemoryMode(),
                appProperties.emailSuffix()
        );
        MailListener mailListener = new MailListener(mailSaver);
        final SMTPServerHandler smtpServerHandler = new SMTPServerHandler(
                mailSaver,
                mailListener,
                new SMTPServer(
                        new SimpleMessageListenerAdapter(mailListener)
                )
        );
        final UIModel uiModel = new UIModel(
                smtpServerHandler,
                appProperties.getBindAddress(),
                appProperties.getPort()
        );
        mailSaver.setUiModel(uiModel);

        final Configuration configuration = new Configuration();

        if (appProperties.startInBackground()) {
            try {
                smtpServerHandler.startServer(
                        appProperties.getPort(),
                        appProperties.getBindAddress()
                );
            } catch (NumberFormatException e) {
                log.error("Error: Invalid port number", e);
            } catch (UnknownHostException e) {
                log.error("Error: Invalid bind address", e);
            } catch (Exception e) {
                log.error("Failed to auto-start server in background", e);
            }
        } else {
            System.setProperty("mail.mime.decodetext.strict", "false");
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
            EventQueue.invokeLater(new Runnable() {
                                       @Override
                                       public void run() {
                                           try {
                                               URL envelopeImage = getClass().getResource(appProperties.getApplicationIconPath());
                                               if (envelopeImage != null) {
                                                   Application.getApplication()
                                                           .setDockIconImage(
                                                                   Toolkit.getDefaultToolkit()
                                                                           .getImage(envelopeImage)
                                                           );
                                               }
                                           } catch (RuntimeException e) {
                                               log.debug("Error: {} - This is probably because we run on a non-Mac platform and these components are not implemented", e.getMessage());
                                           } catch (Exception e) {
                                               log.error(e.getMessage(), e);
                                           }

                                           System.setProperty("apple.laf.useScreenMenuBar", "true");
                                           try {
                                               System.setProperty("com.apple.mrj.application.apple.menu.about.name", appProperties.applicationName());
                                           } catch (PropertiesFileNotFoundException e) {
                                               log.error(e.getMessage(), e);
                                           } catch (IOException e) {
                                               log.error(e.getMessage(), e);
                                           }
                                           UIManager.put("swing.boldMetal", Boolean.FALSE);
                                           try {
                                               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                           } catch (Exception e) {
                                               log.error(e.getMessage(), e);
                                           }

                                           I18n i18n = I18n.INSTANCE;
                                           try {
                                               MenuBar menuBar = new MenuBar(
                                                       appProperties.getMemoryMode(),
                                                       appProperties.applicationName()
                                               );
                                               MainWindowListener mainWindowListener = new MainWindowListener(
                                                       appProperties.applicationTrayInUse(),
                                                       appProperties.applicationName(),
                                                       appProperties.getApplicationIconPath()
                                               );
                                               DirChooser dirChooser = new DirChooser(
                                                       new JFileChooser(),
                                                       appProperties.applicationName(),
                                                       uiModel,
                                                       appProperties.emailDefaultDirectory()
                                               );
                                               MigLayout migLayout = new MigLayout(
                                                       "", // Layout constraints
                                                       "[] 10 [] [] [grow,fill]", // Column constraints
                                                       "[] [] 5 [] 5 [grow,fill] []"
                                               );
                                               JPanel jPanel = new JPanel(migLayout);
                                               JTextField portTextField = new JTextField();
                                               portTextField.setText(String.valueOf(appProperties.getPort()));
                                               MainPanel mainPanel = new MainPanel(
                                                       menuBar,
                                                       uiModel,
                                                       smtpServerHandler,
                                                       new PortTextField(
                                                               uiModel,
                                                               String.valueOf(appProperties.getPort()),
                                                               portTextField
                                                       ),
                                                       new MailsListPane(
                                                               uiModel,
                                                               appProperties.getMemoryMode(),
                                                               appProperties.getEmlViewer(),
                                                               appProperties.applicationName()
                                                       ),
                                                       dirChooser,
                                                       migLayout,
                                                       new JLabel(i18n.get("mainpanel.listening.port")),
                                                       new StartServerButton(
                                                               uiModel,
                                                               appProperties.applicationName()
                                                       ),
                                                       new JLabel(i18n.get("mainpanel.messages.received")),
                                                       new NbReceivedLabel(uiModel),
                                                       new JLabel(i18n.get("mainpanel.save.messages")),
                                                       new SaveMsgField(
                                                               uiModel,
                                                               appProperties.getMemoryMode()
                                                       ),
                                                       new JTabbedPane(),
                                                       new LogsPane(appProperties.logAppenderName()),
                                                       new LastMailPane(),
                                                       new ClearAllButton(
                                                               smtpServerHandler,
                                                               appProperties.applicationName()
                                                       ),
                                                       appProperties.getPort(),
                                                       jPanel
                                               );
                                               dirChooser.setParent(jPanel);
                                               mainPanel.buildGUI();
                                               MainFrame mainFrame = new MainFrame(
                                                       mainPanel,
                                                       new Dimension(
                                                               appProperties.applicationMinWidth(),
                                                               appProperties.applicationMinHeight()
                                                       ),
                                                       menuBar,
                                                       mainWindowListener,
                                                       appProperties.applicationName(),
                                                       appProperties.getApplicationIconPath(),
                                                       smtpServerHandler,
                                                       appProperties.getPort(),
                                                       appProperties.getOutputDirectory(),
                                                       configuration,
                                                       uiModel,
                                                       appProperties.emailDefaultDirectory(),
                                                       appProperties.defaultPort()
                                               );
                                               menuBar.setMainFrame(mainFrame);
                                               mainWindowListener.setMainFrame(mainFrame);
                                           } catch (PropertiesFileNotFoundException e) {
                                               e.printStackTrace();
                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }
            );
        }
    }
}
