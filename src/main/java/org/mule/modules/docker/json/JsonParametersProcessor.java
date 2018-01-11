/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonParametersProcessor is used to process input JSON file. This class deserialize input JSON to POJO class and set docker CMD parameters. This class uses JsonNameStrategy while
 * deserialization of JSON to pojo class.
 *
 */

public class JsonParametersProcessor {

    private static final Logger logger = LogManager.getLogger(JsonParametersProcessor.class.getName());

    private JsonParametersProcessor() {
    }

    /**
     * Deserialize JSON and set docker CMD parameters.
     * 
     * @param filePath
     *            Path of the JSON file
     * @param dockerCmd
     *            Docker command object to set CMD parameters
     * @param pojoClass
     *            Class type of used while deserialization
     * @return Docker command by setting parameter values
     */
    public static final <T> T parseJsonParameters(String filePath, Object dockerCmd, Class<T> pojoClass) {
        T pojo = null;
        try {
            pojo = getPojoFromJson(filePath, pojoClass);
            setCmdParameters(dockerCmd, pojo);

        } catch (JsonMappingException | FileNotFoundException e) {
            logger.error("Unable to parse input JSON file");
            logger.error(e.getMessage(), e);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pojo;
    }

    /**
     * Deserialize JSON to POJO class.
     * 
     * @param filePath
     *            Path of the JSON file
     * @param pojoClass
     *            Class type of used while deserialization
     * @return Deserialized pojo class object
     * @throws IOException
     */
    private static <T> T getPojoFromJson(String filePath, Class<T> pojoClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(new JsonNameStrategy());
        InputStream inputStream = new FileInputStream(filePath);
        Reader fileReader = new InputStreamReader(inputStream, "UTF-8");
        return mapper.readValue(fileReader, pojoClass);
    }

    /**
     * Get parameters from POJO and set to docker CMD.
     * 
     * @param dockerCmd
     *            Docker command object to set CMD parameters
     * @param pojo
     *            Deserialized pojo
     * @return Docker command by setting parameter values
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    private static Object setCmdParameters(Object dockerCmd, Object pojo) throws IllegalAccessException, InvocationTargetException, IntrospectionException {

        BeanInfo info = Introspector.getBeanInfo(pojo.getClass());
        String attributeName = null;
        Object parameter = null;
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null && !"getClass".equals(reader.getName())) {
                attributeName = Character.toUpperCase(pd.getName().charAt(0)) + pd.getName().substring(1);
                parameter = reader.invoke(pojo);
                if (parameter != null) {
                    setParameter(dockerCmd, attributeName, parameter);
                }
            }
        }
        return dockerCmd;
    }

    /**
     * Set docker CMD parameters.
     * 
     * @param dockerCmd
     *            Docker command object to set CMD parameters
     * @param attributeName
     *            Name of the attribute from pojo used to invoke CMD method
     * @param parameter
     *            Value of the attribute name parameter
     */
    private static void setParameter(Object dockerCmd, String attributeName, Object parameter) {
        logger.debug("Getting method for : " + attributeName + " with value" + parameter);
        Method method = getMethod(dockerCmd, attributeName, parameter);
        if (method != null) {
            try {
                method.invoke(dockerCmd, parameter);
                logger.info("Set:" + attributeName + " with value " + parameter);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Get docker method of attribute name
     * 
     * @param dockerCmd
     *            Docker command object used to search attribute method
     * @param attributeName
     *            Name of the attribute from pojo used to search method
     * @param parameterValue
     *            Value of the attribute
     * @return
     */
    private static Method getMethod(Object dockerCmd, String attributeName, Object parameterValue) {
        Class<?> parameterClass = getParameterClass(parameterValue);
        Method method = null;
        try {
            method = dockerCmd.getClass().getDeclaredMethod("with" + attributeName, parameterClass);
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error(e.getMessage(), e);
            logger.warn("Method not found for:" + attributeName + " With value:" + parameterValue + " Of type" + parameterClass);
        }
        return method;

    }

    /**
     * Get appropriate class for parameter value
     * 
     * @param parameterValue
     *            Parameter value used to search method in docker command class
     * @return Type of the parameter value class
     */
    private static Class<?> getParameterClass(Object parameterValue) {
        if (parameterValue.getClass() == ArrayList.class) {
            return List.class;
        } else if (parameterValue.getClass() == LinkedHashMap.class) {
            return Map.class;
        }
        return parameterValue.getClass();
    }
}
