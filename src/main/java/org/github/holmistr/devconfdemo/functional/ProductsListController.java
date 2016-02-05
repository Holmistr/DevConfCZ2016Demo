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
package org.github.holmistr.devconfdemo.functional;

import org.github.holmistr.devconfdemo.entity.Product;
import org.infinispan.Cache;
import org.infinispan.commons.api.functional.FunctionalMap.ReadOnlyMap;
import org.infinispan.functional.impl.FunctionalMapImpl;
import org.infinispan.functional.impl.ReadOnlyMapImpl;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Controller for listing entries from cache using Functional Map API.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Named("functionalListController")
@RequestScoped
public class ProductsListController {

    @Inject
    private Logger logger;

    @Inject
    private DefaultCacheManager cacheManager;

    private int listStart;
    private int listEnd;

    public List<Product> getAll() {
        logger.info("Retrieving list of products.");

        Cache<Long, Product> cache = cacheManager.getCache();
        FunctionalMapImpl<Long, Product> functionalMap = FunctionalMapImpl.create(cache.getAdvancedCache());

        ReadOnlyMap<Long, Product> readOnlyMap = ReadOnlyMapImpl.create(functionalMap);

        Set<Long> keys = LongStream.rangeClosed(listStart, listEnd).boxed().collect(Collectors.toSet());
        List<Product> products = readOnlyMap.evalMany(keys, entry -> entry.get()).collect(Collectors.toList());

        return products;
    }

    public List<String> getAllNames() {
        logger.info("Retrieving list of product names.");

        Cache<Long, Product> cache = cacheManager.getCache();
        FunctionalMapImpl<Long, Product> functionalMap = FunctionalMapImpl.create(cache.getAdvancedCache());

        ReadOnlyMap<Long, Product> readOnlyMap = ReadOnlyMapImpl.create(functionalMap);

        Set<Long> keys = LongStream.rangeClosed(listStart, listEnd).boxed().collect(Collectors.toSet());
        List<String> products = readOnlyMap.evalMany(keys, entry -> entry.get().getName()).collect(Collectors.toList());

        return products;
    }

    @PostConstruct
    public void init() {
        listStart = 1;
        listEnd = cacheManager.getCache().size();
    }

    public int getListStart() {
        return listStart;
    }

    public void setListStart(int listStart) {
        this.listStart = listStart;
    }

    public int getListEnd() {
        return listEnd;
    }

    public void setListEnd(int listEnd) {
        this.listEnd = listEnd;
    }
}
