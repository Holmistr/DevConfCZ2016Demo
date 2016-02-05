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
import org.infinispan.query.continuous.ContinuousQuery;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for page with notifications using continuous queries.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Named("notificationsController")
@RequestScoped
public class NotificationsController {

    private static boolean initialized = false;

    @Inject
    private Logger logger;

    @Inject
    private DefaultCacheManager cacheManager;

    private static NotificationsListener notificationsListener;

    private String message;

    @PostConstruct
    public void init() {
        Cache<Long, Product> cache = cacheManager.getCache();

        if (!initialized) {
            QueryFactory queryFactory = Search.getQueryFactory(cache);

            Query query = queryFactory.from(Product.class).build();

            notificationsListener = new NotificationsListener();
            ContinuousQuery<Long, Product> continuousQuery = new ContinuousQuery<>(cache);
            continuousQuery.addContinuousQueryListener(query, notificationsListener);

            logger.info("Continuous query attached.");
            initialized = true;
        }
    }

    public void clearNotifications() {
        notificationsListener.clearNotifications();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getNotifications() {
        return notificationsListener.getNotifications();
    }
}
