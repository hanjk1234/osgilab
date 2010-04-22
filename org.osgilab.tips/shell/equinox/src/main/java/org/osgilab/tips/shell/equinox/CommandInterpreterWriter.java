/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.tips.shell.equinox;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer that writes to CommandInterpreter
 *
 * @author dmytro.pishchukhin
 */
public class CommandInterpreterWriter extends Writer {
    private CommandInterpreter commandInterpreter;

    public CommandInterpreterWriter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        commandInterpreter.print(new String(cbuf, off, len));
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
