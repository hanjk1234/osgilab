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

package org.knowhowlab.osgi.tips.activation.blueprint;

import org.knowhowlab.osgi.tips.activation.core.Echo;
import org.osgi.service.prefs.PreferencesService;

import java.util.Map;

/**
 * @author dmytro.pishchukhin
 */
public class EchoBean implements Echo {
    private PreferencesService preferencesService;

    public String echo(String str) {
        return str;
    }

    public void bindPreferencesService(PreferencesService preferencesService, Map props) {
        this.preferencesService = preferencesService;
        System.out.println("PreferencesService is linked");
    }

    public void unbindPreferencesService(PreferencesService preferencesService, Map props) {
        this.preferencesService = null;
        System.out.println("PreferencesService is unlinked");
    }
}
