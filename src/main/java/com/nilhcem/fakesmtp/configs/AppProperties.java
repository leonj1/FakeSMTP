package com.nilhcem.fakesmtp.configs;

import com.josemleon.AppProperty;
import com.josemleon.exceptions.PropertiesFileNotFoundException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2016
 **/
public class AppProperties {

    private AppProperty getProperty;

    public AppProperties(AppProperty getProperty) {
        this.getProperty = getProperty;
    }

    public boolean startServer() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("start-server"));
    }

    public boolean startInBackground() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("background"));
    }

    public int getPort() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("port"));
    }

    public String getOutputDirectory() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("output-dir");
    }

    public String getRelayDomains() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("relay-domains");
    }

    public boolean getMemoryMode() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("memory-mode"));
    }

    public InetAddress getBindAddress() throws PropertiesFileNotFoundException, IOException {
        return InetAddress.getByName(this.getProperty.value("bind-address"));
    }

    public String getEmlViewer() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("eml-viewer");
    }

    public String getApplicationIconPath() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("application.icon.path");
    }

    public boolean shouldStartInBackground() throws PropertiesFileNotFoundException, IOException {
        return startServer() && startInBackground();
    }

    public String emailSuffix() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("email.suffix");
    }

    public String applicationName() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("application.name");
    }

    public int applicationMinHeight() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("app.min.height"));
    }

    public int applicationMinWidth() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("app.min.width"));
    }

    public int defaultPort() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("default.port"));
    }

    public String emailDefaultDirectory() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("email.default.directory");
    }

    public String logAppenderName() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("log.appender.name");
    }

    public boolean applicationTrayInUse() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("application.tray.in.use"));
    }
}
