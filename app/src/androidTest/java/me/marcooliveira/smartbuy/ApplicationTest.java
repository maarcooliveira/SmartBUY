package me.marcooliveira.smartbuy;

import com.robotium.solo.Solo;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import me.marcooliveira.smartbuy.activity.DetailActivity_;
import me.marcooliveira.smartbuy.activity.MainActivity_;

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity_> {
    private Solo solo;

    public ApplicationTest() {
        super(MainActivity_.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testOpenProductDetails() throws Exception {
        solo.unlockScreen();
        solo.clickInList(0);
        solo.assertCurrentActivity("Expected DetailActivity_", DetailActivity_.class);
    }

    public void testBackToMainActivity() throws Exception {
        solo.clickInList(0);
        solo.goBack();
        solo.assertCurrentActivity("Expected MainActivity_", MainActivity_.class);
    }

    public void testImageZoom() throws Exception {
        solo.clickInList(2);
        solo.clickOnView(solo.getView(R.id.product_detail_image));
        solo.clickOnView(solo.getView(R.id.zoom_back_button));
        solo.assertCurrentActivity("Expected DetailActivity_", DetailActivity_.class);
    }

    public void testShareFab() throws Exception {
        solo.clickInList(3);
        solo.clickOnView(solo.getView(R.id.fab));
    }

}