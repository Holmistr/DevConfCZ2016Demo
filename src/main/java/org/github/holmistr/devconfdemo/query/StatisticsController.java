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
package org.github.holmistr.devconfdemo.query;

import org.github.holmistr.devconfdemo.entity.Product;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Expression;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for computing statistics using queries.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Named("queryStatisticsController")
@RequestScoped
public class StatisticsController {

    @Inject
    private Logger logger;

    @Inject
    private DefaultCacheManager cacheManager;

    private String message;
    private double averagePrice;
    private int totalPieces;
    private Map<Integer, Long> countProductsWithSamePieces;

    @PostConstruct
    public void computeStatistics() {
        Cache<Long, Product> cache = cacheManager.getCache();
        QueryFactory queryFactory = Search.getQueryFactory(cache);

        Query query = queryFactory.from(Product.class)
                .select(Expression.avg("price"), Expression.sum("pieces"))
                .build();

        List<Object[]> results = query.list();

        averagePrice = (double) results.get(0)[0];
        totalPieces = (int) results.get(0)[1];

        query = queryFactory.from(Product.class)
                .select(Expression.property("pieces"), Expression.count("id"))
                .groupBy("pieces")
                .build();

        results = query.list();

        countProductsWithSamePieces = results.stream()
                .collect(Collectors.toMap(result -> (int)result[0], result -> (long)result[1]));

        logger.info("Statistics computed");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        this.totalPieces = totalPieces;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Map<Integer, Long> getCountProductsWithSamePieces() {
        return countProductsWithSamePieces;
    }

    public void setCountProductsWithSamePieces(Map<Integer, Long> countProductsWithSamePieces) {
        this.countProductsWithSamePieces = countProductsWithSamePieces;
    }
}
