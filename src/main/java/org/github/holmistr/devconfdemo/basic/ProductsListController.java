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
package org.github.holmistr.devconfdemo.basic;

import org.github.holmistr.devconfdemo.entity.Product;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for listing the entries in cache using "classic" Cache API.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Named("basicListController")
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
        List<Product> products = new ArrayList<>();

        Set<Long> keySet = cache.keySet();
        for (Long key : keySet) {
            if (key >= listStart && key <= listEnd) {
                products.add(cache.get(key));
            }
        }

        return products;
    }

    public List<String> getAllNames() {
        return getAll().stream().map(Product::getName).collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        listStart = 0;
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
