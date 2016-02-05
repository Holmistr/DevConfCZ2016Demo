/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.holmistr.devconfdemo.resources;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheManagerProvider {

    private DefaultCacheManager manager;

    public DefaultCacheManager getCacheManager() {
        if (manager == null) {
            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                    .clusteredDefault() // Builds a default clustered configuration
                    .transport().addProperty("configurationFile", "jgroups-udp.xml") // provide a specific JGroups configuration
                    .globalJmxStatistics().allowDuplicateDomains(true).enable() // This method enables the jmx statistics of
                                                                                // the global configuration and allows for duplicate JMX domains
                    .build(); // Builds the GlobalConfiguration object

            Configuration loc = new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.REPL_SYNC) // Set Cache mode to DISTRIBUTED with SYNCHRONOUS replication
                    .indexing().index(Index.LOCAL)
                    .build();

            manager = new DefaultCacheManager(glob, loc, true);
        }

        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }

}
