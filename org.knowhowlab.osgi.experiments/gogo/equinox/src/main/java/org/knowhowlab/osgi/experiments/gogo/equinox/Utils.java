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

import javassist.*;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
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
     * Classes pool
     */
    private static final ClassPool POOL = ClassPool.getDefault();

    /**
     * Initialization of Classes pool
     */
    static {
        POOL.appendClassPath(new ClassClassPath(EquinoxGogoAdapter.class));
        POOL.appendClassPath(new ClassClassPath(CommandInterpreter.class));
    }

    /**
     * Create GoGo service based on CommandProvider service
     *
     * @param provider Equinox CommandProvider service instance
     * @return GoGo service info or <code>null</code>
     */
    public static <T extends CommandProvider> ShellInfo createGogoService(T provider) {
        // todo: change generation algorithm
        CtClass ctClass = POOL.makeClass(EquinoxGogoAdapter.class.getName() + "_" + provider.getClass().getCanonicalName());

        SortedSet<String> commands = new TreeSet<String>();
        Class<EquinoxGogoAdapter> shellClass;

        try {
            if (!ctClass.isFrozen()) {
                // set superclass
                CtClass abstractCtClass = POOL.getCtClass(EquinoxGogoAdapter.class.getName());
                ctClass.setSuperclass(abstractCtClass);

                // todo: add constructor
                CtClass serviceCtClass = POOL.getCtClass(Object.class.getName());
                CtClass stringCtClass = POOL.getCtClass(String.class.getName());
                CtClass setCtClass = POOL.getCtClass(Set.class.getName());
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{
                        serviceCtClass, stringCtClass, setCtClass
                }, ctClass);
                ctConstructor.setModifiers(Modifier.PUBLIC);
                ctConstructor.setBody("super($1, $2, $3);");
                ctClass.addConstructor(ctConstructor);

                // todo
                Class<? extends CommandProvider> providerClass = provider.getClass();
                Method[] methods = providerClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("_")) {
                        Class<?>[] params = method.getParameterTypes();
                        if (params.length == 1 && params[0].equals(CommandInterpreter.class)) {
                            commands.add(method.getName().substring(1));
                            // todo: generate methods with annotations
                        }
                    }
                }

            }
            shellClass = ctClass.toClass(EquinoxGogoAdapter.class.getClassLoader(),
                    EquinoxGogoAdapter.class.getProtectionDomain());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Unable to create Equinox GoGo adapter for: " + provider.getClass(), e);
            return null;
        }
        return new ShellInfo(DEFAULT_SCOPE, commands.toArray(new String[commands.size()]), shellClass);
    }

    /**
     * Detach generated class
     */
    public static void clean(String className) {
        try {
            CtClass ctClass = POOL.getCtClass(className);
            ctClass.defrost();
            ctClass.detach();
        } catch (NotFoundException e) {
            LOG.log(Level.WARNING, "Unable to clean Console Service. " + e.getMessage(), e);
        }
    }
}
