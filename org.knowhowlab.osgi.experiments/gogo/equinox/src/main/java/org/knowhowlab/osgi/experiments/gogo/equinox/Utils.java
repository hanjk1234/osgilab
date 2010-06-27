/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.knowhowlab.osgi.experiments.gogo.equinox;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import java.lang.reflect.Method;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * Utility class to create GoGo service that is based on Equinox CommandProvider serice
 *
 * @author dmytro.pishchukhin
 */
public class Utils {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    /**
     * Default scope
     */
    private static final String DEFAULT_SCOPE = "equinox";

    /**
     * Create GoGo service based on CommandProvider service
     *
     * @param provider Equinox CommandProvider service instance
     * @return GoGo service info or <code>null</code>
     */
    public static <T extends CommandProvider> ShellInfo createGogoService(T provider) {
        Class<EquinoxGogoAdapter> shellClass = null;

        SortedSet<String> commands = new TreeSet<String>();

        Class<? extends CommandProvider> providerClass = provider.getClass();
        Method[] methods = providerClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("_")) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0].equals(CommandInterpreter.class)) {
                    commands.add(method.getName().substring(1));
                }
            }
        }

        return new ShellInfo(DEFAULT_SCOPE, commands.toArray(new String[commands.size()]), shellClass);
    }
}
