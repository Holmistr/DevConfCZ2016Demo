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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Controller for adding to cache via "classic" Cache API
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Named("basicAddController")
@RequestScoped
public class ProductAddController {

    @Inject
    private Logger logger;

    @Inject
    private DefaultCacheManager cacheManager;

    private Long id;
    private String name;
    private String description;
    private int pieces;
    private double price;

    private String message;

    public void add() {
        Cache<Long, Product> cache = cacheManager.getCache();

        Product product = new Product(id, name, description, pieces, price);

        cache.put(id, product);

        message = "Product successfully added.";
        logger.info("Added product: " + product);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }
}
