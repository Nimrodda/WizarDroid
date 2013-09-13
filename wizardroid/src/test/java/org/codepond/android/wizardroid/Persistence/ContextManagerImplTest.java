package org.codepond.android.wizardroid.Persistence;

import android.os.Bundle;
import org.codepond.wizardroid.TestStep;
import org.codepond.wizardroid.persistence.ContextManager;
import org.codepond.wizardroid.persistence.ContextManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

@RunWith(RobolectricTestRunner.class)
public class ContextManagerImplTest {

    private TestStep sourceStep;
    private TestStep destStep;
    private ContextManager contextManager;

    private static final String CONTEXT_VARIABLE_NAME = "timestamp";

    @Before
    public void setUp() {
        sourceStep = new TestStep();
        sourceStep.setTimestamp(new Date());
        destStep = new TestStep();
        contextManager = new ContextManagerImpl();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testLoadStepContext() {
        Bundle context = new Bundle();
        context.putLong(CONTEXT_VARIABLE_NAME, new Date().getTime());
        contextManager.setContext(context);
        contextManager.loadStepContext(destStep);
        assertNotNull(destStep.getArguments().getLong(CONTEXT_VARIABLE_NAME));
    }

    @Test
    public void testPersistStepContext() {
        contextManager.setContext(new Bundle());
        contextManager.persistStepContext(sourceStep);
        assertTrue(String.format("contextManager does not contain value for context variable named: '%s'", CONTEXT_VARIABLE_NAME),
                contextManager.getContext().containsKey(CONTEXT_VARIABLE_NAME));
    }
}
