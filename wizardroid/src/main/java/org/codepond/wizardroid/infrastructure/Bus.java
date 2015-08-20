package org.codepond.wizardroid.infrastructure;

import java.util.HashMap;
import java.util.Map;

public class Bus {
    private static final Bus sInstance = new Bus();
    private static Map<Subscriber, Class<?>> sSubscribers = new HashMap<Subscriber, Class<?>>();

    private Bus() {
    }

    public static Bus getInstance() {
        return sInstance;
    }

    public void post(Object event) {
        Class messageType = event.getClass();
        for (Map.Entry<Subscriber, Class<?>> entry : sSubscribers.entrySet()) {
            if (entry.getValue() == messageType) {
                entry.getKey().receive(event);
            }
        }
    }

    public void register(Subscriber subscriber, Class<?> eventType) {
        if (!sSubscribers.containsKey(subscriber)) {
            sSubscribers.put(subscriber, eventType);
        }
    }

    public void unregister(Subscriber subscriber) {
        if (sSubscribers.containsKey(subscriber)) {
            sSubscribers.remove(subscriber);
        }
    }
}
