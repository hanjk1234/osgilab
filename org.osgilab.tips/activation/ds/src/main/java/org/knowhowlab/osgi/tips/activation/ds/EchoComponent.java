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

package org.knowhowlab.osgi.tips.activation.ds;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.tips.activation.core.Echo;
import org.osgi.service.prefs.PreferencesService;

/**
 * @author dmytro.pishchukhin
 */
@Component(name = "Echo", immediate = true)
@Service(value = Echo.class)
@Property(name = Echo.ECHO_TYPE_PROP, value = "Declarative Services")
public class EchoComponent implements Echo {
    @Reference(name = "preferencesService", referenceInterface = PreferencesService.class,
            cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private PreferencesService preferencesService;

    public String echo(String str) {
        return str;
    }

    public void bindPreferencesService(PreferencesService preferencesService) {
        System.out.println("PreferencesService is linked");
        this.preferencesService = preferencesService;
    }

    public void unbindPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = null;
        System.out.println("PreferencesService is unlinked");
    }

}
