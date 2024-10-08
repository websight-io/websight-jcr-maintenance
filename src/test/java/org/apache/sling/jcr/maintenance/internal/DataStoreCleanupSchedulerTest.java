/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.jcr.maintenance.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;

import java.lang.annotation.Annotation;

import javax.management.openmbean.CompositeData;

import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean;
import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean.StatusCode;
import org.apache.sling.jcr.maintenance.CompositeDataMock;
import org.apache.sling.jcr.maintenance.DataStoreCleanupConfig;
import org.junit.Test;
import org.mockito.Mockito;

public class DataStoreCleanupSchedulerTest {

    @Test
    public void testRunnable() {

        Integer id = 1;
        final RepositoryManagementMBean repositoryManager = Mockito.mock(RepositoryManagementMBean.class);
        CompositeData startingCd = CompositeDataMock.init().put("id", id).build();
        Mockito.when(repositoryManager.startDataStoreGC(false)).thenReturn(startingCd);
        CompositeData doneCd = CompositeDataMock.init().put("id", id).put("code", StatusCode.SUCCEEDED.ordinal())
                .build();
        Mockito.when(repositoryManager.getDataStoreGCStatus()).thenReturn(doneCd);
        DataStoreCleanupConfig config = Mockito.mock(DataStoreCleanupConfig.class);
        Mockito.when(config.enabled()).thenReturn(true);
        final DataStoreCleanupScheduler dscs = new DataStoreCleanupScheduler(config,
                repositoryManager);
        dscs.run();

        Mockito.verify(repositoryManager).startDataStoreGC(Mockito.anyBoolean());
    }

    @Test
    public void testRunCheck() {

        Integer id = 1;
        final RepositoryManagementMBean repositoryManager = Mockito.mock(RepositoryManagementMBean.class);
        CompositeData startingCd = CompositeDataMock.init().put("id", id).build();
        Mockito.when(repositoryManager.startDataStoreGC(false)).thenReturn(startingCd);
        CompositeData doneCd = CompositeDataMock.init().put("id", id).put("code", StatusCode.RUNNING.ordinal()).build();
        Mockito.when(repositoryManager.getDataStoreGCStatus()).thenReturn(doneCd);
        final DataStoreCleanupScheduler dscs = new DataStoreCleanupScheduler(Mockito.mock(DataStoreCleanupConfig.class),
                repositoryManager);

        dscs.run();

        Mockito.verify(repositoryManager, never()).startDataStoreGC(Mockito.anyBoolean());
    }

    @Test
    public void testNoRunWhenDisabled() {

        Integer id = 1;
        final RepositoryManagementMBean repositoryManager = Mockito.mock(RepositoryManagementMBean.class);
        CompositeData startingCd = CompositeDataMock.init().put("id", id).build();
        Mockito.when(repositoryManager.startDataStoreGC(false)).thenReturn(startingCd);
        CompositeData doneCd = CompositeDataMock.init().put("id", id).put("code", StatusCode.SUCCEEDED.ordinal())
            .build();
        Mockito.when(repositoryManager.getDataStoreGCStatus()).thenReturn(doneCd);
        DataStoreCleanupConfig config = Mockito.mock(DataStoreCleanupConfig.class);
        Mockito.when(config.enabled()).thenReturn(false);
        final DataStoreCleanupScheduler dscs = new DataStoreCleanupScheduler(config,
            repositoryManager);
        dscs.run();

        Mockito.verify(repositoryManager, never()).startDataStoreGC(Mockito.anyBoolean());
    }

    @Test
    public void testSheduledExpression() {
        final String EXPECTED = "* * * * *";
        final DataStoreCleanupScheduler dscs = new DataStoreCleanupScheduler(new DataStoreCleanupConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String scheduler_expression() {
                return EXPECTED;
            }

            @Override
            public boolean enabled() {
                return true;
            }

        }, null);

        assertEquals(EXPECTED, dscs.getSchedulerExpression());

    }

}