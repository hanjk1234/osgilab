package org.osgilab.tips.shell.knopflerfish;

import org.knopflerfish.service.console.CommandGroup;
import org.knopflerfish.service.console.Session;

import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Knopflerfish CommandGroup service implementation
 *
 * @author dpishchukhin
 */
public class KnopflerfishCommandGroup implements CommandGroup {
    private static final Logger LOG = Logger.getLogger(KnopflerfishCommandGroup.class.getName());

    private String groupId;
    private String groupName;
    private Object service;

    private SortedMap<String, String> commands = new TreeMap<String, String>();

    /**
     * Command Group constructor
     *
     * @param groupId   group id
     * @param groupName group name
     * @param service   commands provider service instance
     */
    public KnopflerfishCommandGroup(String groupId, String groupName, Object service) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.service = service;
    }

    public String getGroupName() {
        return groupId;
    }

    public String getShortHelp() {
        return groupName;
    }

    public String getLongHelp() {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = commands.keySet();
        for (String key : keys) {
            builder.append(commands.get(key)).append('\n');
        }
        return builder.toString();
    }

    public int execute(String[] lineArgs, Reader in, PrintWriter out, Session session) {
        if (lineArgs != null && lineArgs.length >= 1) {
            String commandName = lineArgs[0];
            String[] args;
            // if no additional agruments - do nothing
            if (lineArgs.length <= 1) {
                args = new String[0];
            } else {
                // remove command name from command line arguments
                args = new String[lineArgs.length - 1];
                System.arraycopy(lineArgs, 1, args, 0, args.length);
            }
            try {
                // invoke command method
                Method method = service.getClass().getMethod(commandName, PrintWriter.class, String[].class);
                method.invoke(service, out, args);
                out.flush();
            } catch (NoSuchMethodException e) {
                out.println("No such command: " + commandName);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Unable to execute command: " + commandName + " with args: " + Arrays.toString(args), e);
                e.printStackTrace(out);
            }
        }
        return 0;
    }

    /**
     * Add command to group
     *
     * @param commandName command name
     * @param commandHelp command help
     */
    public void addCommand(String commandName, String commandHelp) {
        commands.put(commandName, commandHelp);
    }

    /**
     * Get commands count in the group
     *
     * @return commands count
     */
    public int getCommandsCount() {
        return commands.size();
    }
}
