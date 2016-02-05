package org.github.holmistr.devconfdemo.query;

import org.github.holmistr.devconfdemo.entity.Product;
import org.infinispan.query.continuous.ContinuousQueryListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener for events of continuous query.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class NotificationsListener implements ContinuousQueryListener<Long, Product> {

    private List<Product> notifications = new ArrayList<>();

    @Override
    public void resultJoining(Long key, Product value) {
        notifications.add(value);
    }

    @Override
    public void resultLeaving(Long key) {
        // no operation
    }

    public void clearNotifications() {
        notifications.clear();
    }

    public List<Product> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Product> notifications) {
        this.notifications = notifications;
    }
}
