package system.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang3.ObjectUtils;

import java.util.logging.Logger;

public class Log {

    public static void trace(Class cls, String method, String message, Object... objects) {
        Logger.getLogger(cls.getName()).finer(format(cls, method, message, objects));
   }

    private static String format(Class cls, String method, String message, Object... objects) {
        return String.format("%s [%s]: %s", cls.getName(), method, String.format(message, objects));
    }

    public static void debug(Class cls, String method, String message, Object... objects) {
        Logger.getLogger(cls.getName()).fine(format(cls, method, message, objects));
   }

    public static void debug(Object o, String method, String message, Object... objects) {
        Logger.getLogger(o.getClass().getName()).fine(format(o, method, message, objects));
    }

    private static String format(Object o, String method, String message, Object... objects) {
        return String.format("%s [%s]: %s", ObjectUtils.identityToString(o), method, String.format(message, objects));
    }

    public static void info(Class cls, String method, String message, Object... objects) {
        Logger.getLogger(cls.getName()).info(format(cls, method, message, objects));
    }

    public static void info(Object o, String method, String message, Object... objects) {
        Logger.getLogger(o.getClass().getName()).info(format(o, method, message, objects));
    }

    public static void warn(Class cls, String method, String message, Object... objects) {
        Logger.getLogger(cls.getName()).warning(format(cls, method, message, objects));
    }

    public static void warn(Object o, String method, String message, Object... objects) {
        Logger.getLogger(o.getClass().getName()).warning(format(o, method, message, objects));
    }

    public static void error(Class cls, String method, String message, Object... objects) {
        Logger.getLogger(cls.getName()).severe(format(cls, method, message, objects));
    }

    public static void error(Object o, String method, String message, Object... objects) {
        Logger.getLogger(o.getClass().getName()).severe(format(o, method, message, objects));
    }

}