/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.gradle.api

/**
 * This exception is thrown if circular references exists between tasks, the project evaluation order or the
 * project dependsOn order.
 * 
 * @author Hans Dockter
 */
class CircularReferenceException extends GradleException {
    CircularReferenceException() {
    }

    CircularReferenceException(String message) {
        super(message);
    }

    CircularReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    CircularReferenceException(Throwable cause) {
        super(cause);
    }
}