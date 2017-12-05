/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * 
 * Custom name strategy used to deserialize input JSON file
 *
 */
public class JsonNameStrategy extends PropertyNamingStrategy {
    private static final long serialVersionUID = 1L;

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return Character.toUpperCase(defaultName.charAt(0)) + defaultName.substring(1);
    }
}
