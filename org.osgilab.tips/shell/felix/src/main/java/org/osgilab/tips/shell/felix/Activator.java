/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.tips.shell.felix;

import org.apache.felix.shell.Command;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Felix shell adapter activator
 *
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator {
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    private static final String COMMANDS_DESCRIPTION_PROPERTY = "org.osgilab.tips.shell.commands";
    /**
     * Felix shell API does not support commands grouping. Filter requires only shell commands description
     */
    private static final String SHELL_COMMANDS_SERVICE_FILTER = "(org.osgilab.tips.shell.commands=*)";

    private BundleContext bc;
    private ServiceTracker shellCommandsTracker;

    private Map<ServiceReference, List<ServiceRegistration>> commandRegistrations = new HashMap<ServiceReference, List<ServiceRegistration>>();

    public void start(BundleContext context) throws Exception {
        bc = context;
        shellCommandsTracker = new ServiceTracker(bc, bc.createFilter(SHELL_COMMANDS_SERVICE_FILTER),
                new ShellCommandsCustomizer());
        shellCommandsTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        shellCommandsTracker.close();
        shellCommandsTracker = null;

        bc = null;
    }

    private boolean isValidCommandMethod(Object service, String commandName) {
        try {
            service.getClass().getMethod(commandName, PrintStream.class, String[].class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private class ShellCommandsCustomizer implements ServiceTrackerCustomizer {
        public Object addingService(ServiceReference reference) {
            Object commandsDescription = reference.getProperty(COMMANDS_DESCRIPTION_PROPERTY);
            if (commandsDescription == null) {
                LOG.warning(COMMANDS_DESCRIPTION_PROPERTY + " property is null. Ignore service");
                return null;
            } else if (!(commandsDescription instanceof String[][])) {
                LOG.warning(COMMANDS_DESCRIPTION_PROPERTY + " property has wrong format: not String[][]");
                return null;
            } else {
                Object service = bc.getService(reference);
                Integer ranking = (Integer) reference.getProperty(Constants.SERVICE_RANKING);
                String[][] commands = (String[][]) commandsDescription;
                for (String[] commandInfo : commands) {
                    List<ServiceRegistration> registrations = new ArrayList<ServiceRegistration>();
                    if (commandInfo != null) {
                        if (commandInfo.length != 2) {
                            LOG.warning(COMMANDS_DESCRIPTION_PROPERTY + " property has wrong format: not String[][]");
                            continue;
                        }
                        String commandName = commandInfo[0];
                        String commandHelp = commandInfo[1];
                        if (isValidCommandMethod(service, commandName)) {
                            Dictionary<String, Object> props = new Hashtable<String, Object>();
                            if (ranking != null) {
                                props.put(Constants.SERVICE_RANKING, ranking);
                            }
                            try {
                                FelixCommand command = new FelixCommand(commandName, commandHelp, service);
                                registrations.add(bc.registerService(Command.class.getName(), command, props));
                            } catch (Exception e) {
                                LOG.log(Level.WARNING, "Unable to initialize command: " + commandName, e);
                            }
                        }
                    }
                    if (!registrations.isEmpty()) {
                        commandRegistrations.put(reference, registrations);
                    }
                }
                return service;
            }
        }

        public void modifiedService(ServiceReference reference, Object service) {
            // ignore
        }

        public void removedService(ServiceReference reference, Object service) {
            List<ServiceRegistration> registrations = commandRegistrations.remove(reference);
            if (registrations != null) {
                for (ServiceRegistration registration : registrations) {
                    registration.unregister();
                }
            }
            bc.ungetService(reference);
        }
    }
}
