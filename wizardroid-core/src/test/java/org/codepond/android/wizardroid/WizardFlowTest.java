package org.codepond.android.wizardroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WizardFlowTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

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

    @Test
    public void testBuilder_Create_ThrowsRuntimeException() {
        WizardFlow actualFlow = new WizardFlow.Builder().create();


    }
}
