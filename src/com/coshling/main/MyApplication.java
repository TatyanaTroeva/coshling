package com.coshling.main;

import android.app.Application;
import android.content.Context;

import com.coshling.constants.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
public class MyApplication extends Application {
	private static Context mContext;
	public static Context getContext() {
		//  return instance.getApplicationContext();
		return mContext;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = getApplicationContext();		
			    
		initImageLoader(getApplicationContext());
		Constants.imageLoader = ImageLoader.getInstance();
	    
	}
	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
