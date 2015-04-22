/*
 * Copyright 2015 the original author or authors.
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

package org.gradle.launcher.continuous
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.test.fixtures.server.http.CyclicBarrierHttpServer
import org.junit.Rule

class AbstractContinuousModeIntegrationSpec extends AbstractIntegrationSpec {
    @Rule CyclicBarrierHttpServer server = new CyclicBarrierHttpServer()
    def trigger = file("trigger.out")

    def setup() {
        executer.withArgument("--watch")

        buildFile << """

import org.gradle.launcher.continuous.*
import org.gradle.internal.event.*

def triggerFile = file("${trigger.toURI()}")
gradle.buildFinished {
    def trigger = null
    try {
        triggerFile.withObjectInputStream { instream ->
            trigger = instream.readObject()
        }
    } catch (Exception e) {}

    if (trigger!=null) {
        def listenerManager = gradle.services.get(ListenerManager)
        def triggerListener = listenerManager.getBroadcaster(TriggerListener)
        triggerListener.triggered(trigger)
    }
    new URL("${server.uri}").text // wait for test
}
"""
    }

    def planToRebuild() {
        writeTrigger(new DefaultTriggerDetails(TriggerDetails.Type.REBUILD, "test"))
    }

    def planToStop() {
        writeTrigger(new DefaultTriggerDetails(TriggerDetails.Type.STOP, "being done"))
    }

    private writeTrigger(DefaultTriggerDetails triggerDetails) {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(trigger))
        os.writeObject(triggerDetails)
        os.close()
    }
}
