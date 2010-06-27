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

package org.osgilab.tips.jaxb.parser;

import org.osgilab.tips.jaxb.model.PersonesType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

/**
 * @author dmytro.pishchukhin
 */
public class Parser {
    private JAXBContext context;

    public PersonesType parse(InputStream in)  {
        try {
            context = JAXBContext.newInstance("org.osgilab.tips.jaxb.model");
            JAXBElement<PersonesType> element = (JAXBElement<PersonesType>) context.createUnmarshaller().unmarshal(in);
            return element.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
