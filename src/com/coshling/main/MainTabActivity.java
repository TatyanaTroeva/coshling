package com.coshling.main;

import java.util.HashMap;
import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.TabHost;

import com.coshling.constants.Constants;
import com.coshling.fragments.CardsFragment;
import com.coshling.fragments.MyReminderFragment;

public class MainTabActivity extends FragmentActivity {
    /* Your Tab host */
    public TabHost mTabHost;
	private static MainTabActivity mainTabActivity = null;
    /* A HashMap of stacks, where we use tab identifier as keys..*/
    public HashMap<String, Stack<Fragment>> mStacks;

    /*Save current tabs identifier in this..*/
    public String mCurrentTab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_maintab);
        /*  
         *  Navigation stacks for each tab gets created.. 
         *  tab identifier is used as key to get respective stack for each tab
         */
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(Constants.MENU_CARDS, new Stack<Fragment>());
        mStacks.put(Constants.MENU_MYREMINDER, new  Stack<Fragment>());
        
        initData();
    }
    public void initData() {
    	mainTabActivity = this;
    	onClickedMenuItem(0);
    }
    public static MainTabActivity getInstance() {
		return MainTabActivity.mainTabActivity;
	}
    
    public void onClickedMenuItem(int position) {
    	switch (position) {
    	case 0: {
    		mCurrentTab = Constants.MENU_CARDS;
    	}
    	    break;
    	case 1: {
    		mCurrentTab = Constants.MENU_MYREMINDER;
    	}
    	    break;
    	} 
    	if(mStacks.get(mCurrentTab).size() == 0){
  	        if(mCurrentTab.equals(Constants.MENU_CARDS)){
  	        	pushFragments(mCurrentTab, new CardsFragment(), false, true);
  	        } else if(mCurrentTab.equals(Constants.MENU_MYREMINDER)){
  	        	pushFragments(mCurrentTab, new MyReminderFragment(), false, true);
  	        } 
	    } else {
	        /*
	         *    We are switching tabs, and target tab is already has atleast one fragment. 
	         *    No need of animation, no need of stack pushing. Just show the target fragment
	         */
	        pushFragments(mCurrentTab, mStacks.get(mCurrentTab).lastElement(), false,false);
	    }
    }
    /*Comes here when user switch tab, or we do programmatically*/
    TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
      public void onTabChanged(String tabId) {
        /*Set current tab..*/
        mCurrentTab =   tabId;
        
        if(mStacks.get(tabId).size() == 0){
        	
          /*
           *    First time this tab is selected. So add first fragment of that tab.
           *    Dont need animation, so that argument is false.
           *    We are adding a new fragment which is not present in stack. So add to stack is true.
           */
	        if(tabId.equals(Constants.MENU_CARDS)){
	        	pushFragments(mCurrentTab, new CardsFragment(), false, true);
	        } else if(tabId.equals(Constants.MENU_MYREMINDER)){
	        	pushFragments(mCurrentTab, new MyReminderFragment(), false, true);
	        } 
        }else {
          /*
           *    We are switching tabs, and target tab is already has atleast one fragment. 
           *    No need of animation, no need of stack pushing. Just show the target fragment
           */
          pushFragments(tabId, mStacks.get(tabId).lastElement(), false,false);
        }
      }
    };


    /* Might be useful if we want to switch tab programmatically, from inside any of the fragment.*/
    public void setCurrentTab(int val) {
          mTabHost.setCurrentTab(val);
    }
    /* 
     *      To add fragment to a tab. 
     *  tag             ->  Tab identifier
     *  fragment        ->  Fragment to show, in tab identified by tag
     *  shouldAnimate   ->  should animate transaction. false when we switch tabs, or adding first fragment to a tab
     *                      true when when we are pushing more fragment into navigation stack. 
     *  shouldAdd       ->  Should add to fragment navigation stack (mStacks.get(tag)). false when we are switching tabs (except for the first time)
     *                      true in all other cases.
     */
    public void pushFragments(String tag, Fragment fragment,boolean shouldAnimate, boolean shouldAdd){
      if(shouldAdd)
          mStacks.get(tag).push(fragment);
      FragmentManager   manager         =   getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      if(shouldAnimate)
    	  ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);

      ft.replace(R.id.contentRelativeLayout, fragment);
      ft.commit();
    }
    public void popRootFragment() {
    	mStacks.get(mCurrentTab).clear();
    	if(mCurrentTab.equals(Constants.MENU_CARDS)){
    		pushFragments(mCurrentTab, new CardsFragment(), false, true);
    	} else if(mCurrentTab.equals(Constants.MENU_MYREMINDER)){
    		pushFragments(mCurrentTab, new MyReminderFragment(), false, true);
        } 
    }
  
    public void popFragments(){
      if (mStacks.get(mCurrentTab).size() - 2 < 0) {
    	  finish();
    	  return;
      }
    	  
      /*    
       *    Select the second last fragment in current tab's stack.. 
       *    which will be shown after the fragment transaction given below 
       */
      Fragment fragment  =   mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

      /*pop current fragment from stack.. */
      mStacks.get(mCurrentTab).pop();

      /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
      FragmentManager   manager         =   getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
      ft.replace(R.id.contentRelativeLayout, fragment);
      ft.commit();
    }   
    @Override
    public void onBackPressed() {
    	popFragments();
    }
    /*
     *   Imagine if you wanted to get an image selected using ImagePicker intent to the fragment. Ofcourse I could have created a public function
     *  in that fragment, and called it from the activity. But couldn't resist myself.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mStacks.get(mCurrentTab).size() == 0){
            return;
        }
        /*Now current fragment on screen gets onActivityResult callback..*/
        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }
}
