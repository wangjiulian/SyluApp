package com.dpl.syluapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.dpl.syluapp.R;
import com.umeng.fb.fragment.FeedbackFragment;

/**
 * Demo Activity to use {@link com.umeng.fb.fragment.FeedbackFragment}
 */
public class ConversationDetailActivity extends FragmentActivity {

    private FeedbackFragment mFeedbackFragment;
    private RelativeLayout umeng_fb_rrlt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conver);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String conversation_id = getIntent().getStringExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID);
            mFeedbackFragment = FeedbackFragment.newInstance(conversation_id);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_conver,mFeedbackFragment)
                    .commit();
            
            
        }
        umeng_fb_rrlt_back = (RelativeLayout)findViewById(R.id.rrlt_back);
        umeng_fb_rrlt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
 				finish();
				
			}
		});
    }

    @Override
    protected void onNewIntent(android.content.Intent intent) {
        mFeedbackFragment.refresh();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
