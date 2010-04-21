/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.tips.shell.felix;

import org.apache.felix.shell.Command;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dmytro.pishchukhin
 */
public class FelixCommand implements Command {
    private static final Logger LOG = Logger.getLogger(FelixCommand.class.getName());

    private String name;
    private String usage;
    private Object service;
    private Method method;

    public FelixCommand(String name, String usage, Object service) throws NoSuchMethodException {
        this.name = name;
        this.usage = usage;
        this.service = service;
        method = service.getClass().getMethod(name, PrintStream.class, String[].class);
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getShortDescription() {
        return usage;
    }

    public void execute(String line, PrintStream out, PrintStream err) {
        String[] parsedLine = line.split(" ");
        String[] args;
        if (parsedLine.length <= 1) {
            args = new String[0];
        } else {
            args = new String[parsedLine.length - 1];
            System.arraycopy(parsedLine, 1, args, 0, args.length);
        }
        try {
            method.invoke(service, out, args);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Unable to execute command: " + name + " with args: " + Arrays.toString(args), e);
            e.printStackTrace(err);
        }
    }
}
