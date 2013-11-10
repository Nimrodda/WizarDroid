package org.codepond.wizardroid.infrastructure;

public interface Subscriber {
    void receive(Object event);
}
