package com.codepix.main;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;
/**
 * @author DEVENDRA
 * @class DummyTabContent for contents of Tab 
 */
public class DummyTabContent implements TabContentFactory{
    private Context mContext;
   /**
    * @method DummyTabContent() constructor for initialize variable
    * @return */
    public DummyTabContent(Context context){
        mContext = context;
    }
    /**
     * @method createTabContent()  for setting tag name for Tab
     * @return View of Tab */
    @Override
	public View createTabContent(String tag) {
        View v = new View(mContext);
        return v;
    }
}
