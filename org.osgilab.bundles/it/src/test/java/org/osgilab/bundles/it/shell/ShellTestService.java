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

package org.osgilab.bundles.it.shell;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.PrintWriter;

/**
 * @author dmytro.pishchukhin
 */
public class ShellTestService {
    private BundleContext bc;

    public ShellTestService(BundleContext bc) {
        this.bc = bc;
    }

    public void bndinfo(PrintWriter out, String... args) {
        if (args == null || args.length != 1) {
            out.println("Bundle id argument is missed");
            return;
        }
        try {
            int bundleId = Integer.parseInt(args[0]);
            Bundle bundle = bc.getBundle(bundleId);
            if (bundle == null) {
                out.println("Bundle id is invalid: " + bundleId);
                return;
            }
            printBundleInfo(bundle, out);
        } catch (NumberFormatException e) {
            out.println("Bundle id has wrong format: " + args[0]);
        }
    }

    public void bndsinfo(PrintWriter out, String... args) {
        Bundle[] bundles = bc.getBundles();
        for (Bundle bundle : bundles) {
            printBundleInfo(bundle, out);
        }
    }

    private void printBundleInfo(Bundle bundle, PrintWriter out) {
        StringBuilder builder = new StringBuilder();
        builder.append("Bundle ID: ").append(bundle.getBundleId()).append('\n');
        builder.append("Symbolic Name: ").append(bundle.getSymbolicName()).append('\n');
        builder.append("Location: ").append(bundle.getLocation()).append('\n');
        out.println(builder.toString());
    }
}
