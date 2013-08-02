package org.codepond.android.wizardroid;

import android.support.v4.app.FragmentActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * JUnit test class for {@link Wizard}
 */
@RunWith(RobolectricTestRunner.class)
public class WizardTest {
    private Wizard wizard;
    private WizardFlow mockFlow;
    private List mockSteps;
    private FragmentActivity mockContext;
    private TestStep mockStep1;
    private TestStep mockStep2;
    private WizardStep.OnStepStateChangedListener mockStepStateChangedListener;

    @Before
    public void setUp() throws Exception {
        mockStep1 = new TestStep();
        mockStep1.setTimestamp(new Date());
        mockStep2 = new TestStep();
        mockStepStateChangedListener = createMock(WizardStep.OnStepStateChangedListener.class);
        mockStepStateChangedListener.onStepStateChanged(anyObject(TestStep.class));
        expectLastCall().anyTimes();
        mockStep1.setOnStepChangedListener(mockStepStateChangedListener);
        mockStep2.setOnStepChangedListener(mockStepStateChangedListener);
        mockSteps = new ArrayList();
        mockSteps.add(mockStep1);
        mockSteps.add(mockStep2);
        mockFlow = createMock("mockFlow", WizardFlow.class);
        mockContext = Robolectric.buildActivity(FragmentActivity.class).create().get();

        expect(mockFlow.getFragmentContainerId()).andReturn(1234);
        expect(mockFlow.getFragmentManager()).andReturn(mockContext.getSupportFragmentManager());
        expect(mockFlow.getSteps()).andReturn(mockSteps).anyTimes();
        replay(mockFlow);

        wizard = new Wizard(mockFlow);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNext_AdvanceOneStep_StepPositionIsOne() {
        int expectedStepPosition = 1;
        wizard.next();
        assertEquals(String.format("expectedStepPosition should be 1, actual: %s", wizard.getCurrentStepPosition()),
                expectedStepPosition, wizard.getCurrentStepPosition());
    }

    @Test
    public void testBack_BackOneStep_StepPositionIsZero() {
        wizard.setCurrentStep(1);
        int expectedStepPosition = 0;

        wizard.back();
        assertEquals(String.format("expectedStepPosition should be 0, actual: %s", wizard.getCurrentStepPosition()),
                expectedStepPosition, wizard.getCurrentStepPosition());
    }
    /*
    @Test
    public void testSetCurrentStep() throws Exception {

    }

    @Test
    public void testGetCurrentStepPosition() throws Exception {

    }

    @Test
    public void testGetCurrentStep() throws Exception {

    }

    @Test
    public void testGetStepAtPosition() throws Exception {

    }

    @Test
    public void testIsLastStep() throws Exception {

    }

    @Test
    public void testIsFirstStep() throws Exception {

    }

    @Test
    public void testGetContext() throws Exception {

    }

    @Test
    public void testSetContext() throws Exception {

    }*/
}
