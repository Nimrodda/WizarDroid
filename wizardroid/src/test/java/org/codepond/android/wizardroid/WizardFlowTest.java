package org.codepond.android.wizardroid;

import org.codepond.wizardroid.WizardFlow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WizardFlowTest {
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

   /* @Test
    public void testGetSteps() throws Exception {

    }

    @Test
    public void testGetFragmentContainerId() throws Exception {

    }

    @Test
    public void testGetContext() throws Exception {

    }

    @Test
    public void testGetTagForWizardStep() throws Exception {

    }*/

    @Test(expected = RuntimeException.class)
    public void testBuilder_CallingCreateWhenBuilderInvlid_ThrowsRuntimeException() {
        WizardFlow actualFlow = new WizardFlow.Builder().create();


    }
}
