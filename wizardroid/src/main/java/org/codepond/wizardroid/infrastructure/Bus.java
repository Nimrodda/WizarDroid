package org.codepond.wizardroid.infrastructure;

import java.util.HashMap;
import java.util.Map;

public class Bus {
    private static final Bus instance = new Bus();
    private static Map<Subscriber, Class<?>> subscribers = new HashMap<Subscriber, Class<?>>();

    private Bus() {
    }

    public static Bus getInstance() {
        return instance;
    }

    public void post(Object event) {
        Class messageType = event.getClass();
        for (Map.Entry<Subscriber, Class<?>> entry : subscribers.entrySet()) {
            if (entry.getValue() == messageType) {
                entry.getKey().receive(event);
            }
        }
    }

    public void register(Subscriber subscriber, Class<?> eventType) {
        if (!subscribers.containsKey(subscriber)) {
            subscribers.put(subscriber, eventType);
        }
    }

    public void unregister(Subscriber subscriber) {
        if (subscribers.containsKey(subscriber)) {
            subscribers.remove(subscriber);
        }
    }
}
