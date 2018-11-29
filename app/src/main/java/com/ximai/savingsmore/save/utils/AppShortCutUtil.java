package com.ximai.savingsmore.save.utils;

import java.util.Iterator;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.activity.MainActivity;

/***
 * ??????????
 * 
 * @author yang
 * 
 */
public class AppShortCutUtil {

	private static final String TAG = "AppShortCutUtil";
	
	//??????
	private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
	//??????
	private static final int DEFAULT_STROKE_WIDTH_DIP = 2;
	//?????
	private static final int DEFAULT_STROKE_COLOR = Color.WHITE;
	//???????
	private static final int DEFAULT_NUM_COLOR = Color.parseColor("#CCFF0000");


	/***
	 * 
	 * ????????(????)
	 * @param context
	 * @param icon ??
	 * @param isShowNum ???????
	 * @param num ?????????? ??99????"99+"
	 * @return
	 */
	public static Bitmap generatorNumIcon(Context context, Bitmap icon, boolean isShowNum, String num) {
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//??????
		float baseDensity = 1.5f;//240dpi
		float factor = dm.density/baseDensity;
		
		Log.e(TAG, "density:"+dm.density);
		Log.e(TAG, "dpi:"+dm.densityDpi);
		Log.e(TAG, "factor:"+factor);
		
		// ?????
		int iconSize = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
		Bitmap numIcon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas canvas = new Canvas(numIcon);

		// ????
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// ???
		iconPaint.setFilterBitmap(true);// ???Bitmap??????????????Drawable??????????
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(icon, src, dst, iconPaint);
		
		if(isShowNum){
			 
			if(TextUtils.isEmpty(num)){
				num = "0";
			}
			
			if(!TextUtils.isDigitsOnly(num)){
				//???
				Log.e(TAG, "the num is not digit :"+ num);
				num = "0";
			}
			
			int numInt = Integer.valueOf(num);
			
			if(numInt > 99){//??99
				
				num = "99+";
				
				// ?????????????????
				Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
				numPaint.setColor(Color.WHITE);
				numPaint.setTextSize(20f*factor);
				numPaint.setTypeface(Typeface.DEFAULT_BOLD);
				int textWidth=(int)numPaint.measureText(num, 0, num.length());
				
				Log.e(TAG, "text width:"+textWidth);
				
				int circleCenter = (int) (15*factor);//????
				int circleRadius = (int) (13*factor);//????
				
				//???????
				Paint leftCirPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				leftCirPaint.setColor(Color.RED);
				canvas.drawCircle(iconSize-circleRadius-textWidth+(10*factor), circleCenter, circleRadius, leftCirPaint);
				
				//???????
				Paint rightCirPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				rightCirPaint.setColor(Color.RED);
				canvas.drawCircle(iconSize-circleRadius, circleCenter, circleRadius, rightCirPaint);
				
				//???????
				Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				rectPaint.setColor(Color.RED);
				RectF oval = new RectF(iconSize-circleRadius-textWidth+(10*factor), 2*factor, iconSize-circleRadius, circleRadius*2+2*factor);
				canvas.drawRect(oval, rectPaint);

				//????
				canvas.drawText(num, (float)(iconSize-textWidth/2-(24*factor)), 23*factor,	numPaint);
				
			}else{//<=99
			
				// ?????????????????
				Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
				numPaint.setColor(Color.WHITE);
				numPaint.setTextSize(20f*factor);
				numPaint.setTypeface(Typeface.DEFAULT_BOLD);
				int textWidth=(int)numPaint.measureText(num, 0, num.length());
				
				Log.e(TAG, "text width:"+textWidth);
				
				//???????
				//Paint outCirPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				//outCirPaint.setColor(Color.WHITE);
				//canvas.drawCircle(iconSize - 15, 15, 15, outCirPaint);
				
				//???????
				Paint inCirPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				inCirPaint.setColor(Color.RED);
				canvas.drawCircle(iconSize-15*factor, 15*factor, 15*factor, inCirPaint);
				
				//????
				canvas.drawText(num, (float)(iconSize-textWidth/2-15*factor), 22*factor, numPaint);
			}
		}
		return numIcon;
	}


	/***
	 * 
	 * ????????(????)
	 * @param context
	 * @param icon ??
	 * @param isShowNum ???????
	 * @param num ?????????? ??99????"99+"
	 * @return
	 */
	public static Bitmap generatorNumIcon2(Context context, Bitmap icon, boolean isShowNum, String num) {
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//??????
		float baseDensity = 1.5f;//240dpi
		float factor = dm.density/baseDensity;
		
		Log.e(TAG, "density:"+dm.density);
		Log.e(TAG, "dpi:"+dm.densityDpi);
		Log.e(TAG, "factor:"+factor);
		
		// ?????
		int iconSize = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
		Bitmap numIcon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas canvas = new Canvas(numIcon);

		// ????
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// ???
		iconPaint.setFilterBitmap(true);// ???Bitmap??????????????Drawable??????????
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(icon, src, dst, iconPaint);

		if(isShowNum){
			 
			if(TextUtils.isEmpty(num)){
				num = "0";
			}
			
			if(!TextUtils.isDigitsOnly(num)){
				//???
				Log.e(TAG, "the num is not digit :"+ num);
				num = "0";
			}
			
			int numInt = Integer.valueOf(num);
			
			if(numInt > 99){//??99
				num = "99+";
			}

			//?????????????????
			//?????????
			Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
			numPaint.setColor(Color.WHITE);
			numPaint.setTextSize(20f*factor);
			numPaint.setTypeface(Typeface.DEFAULT_BOLD);
			int textWidth=(int)numPaint.measureText(num, 0, num.length());
			Log.e(TAG, "text width:"+textWidth);
			
			/**----------------------------------*
			 * TODO ???????? start
			 *------------------------------------*/
			//?????????
			int backgroundHeight = (int) (2*15*factor);
			int backgroundWidth = textWidth>backgroundHeight ? (int)(textWidth+10*factor) : backgroundHeight;
			
			canvas.save();//????
			
			ShapeDrawable drawable = getDefaultBackground(context);
			drawable.setIntrinsicHeight(backgroundHeight);
			drawable.setIntrinsicWidth(backgroundWidth);
			drawable.setBounds(0, 0, backgroundWidth, backgroundHeight);
			canvas.translate(iconSize-backgroundWidth, 0);
			drawable.draw(canvas);
			
			canvas.restore();//??????????
			
			/**----------------------------------*
			 * TODO ???????? end
			 *------------------------------------*/
			
			//????
			canvas.drawText(num, (float)(iconSize-(backgroundWidth + textWidth)/2), 22*factor, numPaint);
		}
		return numIcon;
	}
	/***
	 * 
	 * ????????(???)
	 * @param context
	 * @param icon ??
	 * @param isShowNum ???????
	 * @param num ?????????? ??99????"99+"
	 * @return
	 */
	public static Bitmap generatorNumIcon3(Context context, Bitmap icon, boolean isShowNum, String num) {
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//??????
		float baseDensity = 1.5f;//240dpi
		float factor = dm.density/baseDensity;
		
		Log.e(TAG, "density:"+dm.density);
		Log.e(TAG, "dpi:"+dm.densityDpi);
		Log.e(TAG, "factor:"+factor);
		
		// ?????
		int iconSize = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
		Bitmap numIcon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas canvas = new Canvas(numIcon);
		
		// ????
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// ???
		iconPaint.setFilterBitmap(true);// ???Bitmap??????????????Drawable??????????
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(icon, src, dst, iconPaint);
		
		if(isShowNum){
			
			if(TextUtils.isEmpty(num)){
				num = "0";
			}
			
			if(!TextUtils.isDigitsOnly(num)){
				//???
				Log.e(TAG, "the num is not digit :"+ num);
				num = "0";
			}
			
			int numInt = Integer.valueOf(num);
			
			if(numInt > 99){//??99
				num = "99+";
			}
			
			//?????????????????
			//?????????
			Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
			numPaint.setColor(Color.WHITE);
			numPaint.setTextSize(20f*factor);
			numPaint.setTypeface(Typeface.DEFAULT_BOLD);
			int textWidth=(int)numPaint.measureText(num, 0, num.length());
			Log.e(TAG, "text width:"+textWidth);
			
			/**----------------------------------*
			 * TODO ??????????????????????? start
			 *------------------------------------*/
			//?????????
			int backgroundHeight = (int) (2*15*factor);
			int backgroundWidth = textWidth>backgroundHeight ? (int)(textWidth+10*factor) : backgroundHeight;
			//?????
			int strokeThickness = (int) (2*factor);
			
			canvas.save();//????
			
			int strokeHeight = backgroundHeight + strokeThickness*2;
			int strokeWidth = textWidth>strokeHeight ? (int)(textWidth+ 10*factor + 2*strokeThickness) : strokeHeight;
			ShapeDrawable outStroke = getDefaultStrokeDrawable(context);
			outStroke.setIntrinsicHeight(strokeHeight);
			outStroke.setIntrinsicWidth(strokeWidth);
			outStroke.setBounds(0, 0, strokeWidth, strokeHeight);
			canvas.translate(iconSize-strokeWidth-strokeThickness, strokeThickness);
			outStroke.draw(canvas);
			
			canvas.restore();//??????????
			
			canvas.save();//????
			
			ShapeDrawable drawable = getDefaultBackground(context);
			drawable.setIntrinsicHeight((int) (backgroundHeight+2*factor));
			drawable.setIntrinsicWidth((int) (backgroundWidth+2*factor));
			drawable.setBounds(0, 0, backgroundWidth, backgroundHeight);
			canvas.translate(iconSize-backgroundWidth-2*strokeThickness, 2*strokeThickness);
			drawable.draw(canvas);
			
			canvas.restore();//??????????
			
			/**----------------------------------*
			 * TODO ???????? end
			 *------------------------------------*/
			
			//????
			canvas.drawText(num, (float)(iconSize-(backgroundWidth + textWidth+4*strokeThickness)/2), (22)*factor+2*strokeThickness, numPaint);
		}
		return numIcon;
	}

	/***
	 * 
	 * ????????(????)
	 * @param context
	 * @param icon ??
	 * @param isShowNum ???????
	 * @param num ?????????? ??99????"99+"
	 * @return
	 */
	public static Bitmap generatorNumIcon4(Context context, Bitmap icon, boolean isShowNum, String num) {
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//??????
		float baseDensity = 1.5f;//240dpi
		float factor = dm.density/baseDensity;
		
		Log.e(TAG, "density:"+dm.density);
		Log.e(TAG, "dpi:"+dm.densityDpi);
		Log.e(TAG, "factor:"+factor);
		
		// ?????
		int iconSize = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
		Bitmap numIcon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas canvas = new Canvas(numIcon);

		// ????
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// ????
		iconPaint.setFilterBitmap(true);// ???Bitmap??????????????Drawable??????????
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(icon, src, dst, iconPaint);

		if(isShowNum){
			 
			if(TextUtils.isEmpty(num)){
				num = "0";
			}
			
			if(!TextUtils.isDigitsOnly(num)){
				//???
				Log.e(TAG, "the num is not digit :"+ num);
				num = "0";
			}
			
			int numInt = Integer.valueOf(num);
			
			if(numInt > 99){//??99
				num = "99+";
			}

			//???????????????
			//?????????
			Paint numPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
			numPaint.setColor(Color.WHITE);
			numPaint.setTextSize(25f*factor);
			numPaint.setTypeface(Typeface.DEFAULT_BOLD);
			int textWidth=(int)numPaint.measureText(num, 0, num.length());
			Log.e(TAG, "text width:"+textWidth);
			
			/**----------------------------------*
			 * TODO ???????? start
			 *------------------------------------*/
			//?????
			int strokeThickness = (int) (DEFAULT_STROKE_WIDTH_DIP*factor);
			//?????????
			float radiusPx = 15*factor;
			int backgroundHeight = (int) (2*(radiusPx+strokeThickness));//2*(??+????)
			int backgroundWidth = textWidth>backgroundHeight ? (int)(textWidth + 10*factor + 2*strokeThickness) : backgroundHeight;
			
			canvas.save();//????
			
			ShapeDrawable drawable = getDefaultBackground2(context);
			drawable.setIntrinsicHeight(backgroundHeight);
			drawable.setIntrinsicWidth(backgroundWidth);
			drawable.setBounds(0, 0, backgroundWidth, backgroundHeight);
			canvas.translate(iconSize-backgroundWidth-strokeThickness, 2*strokeThickness);
			drawable.draw(canvas);
			
			canvas.restore();//??????????
			
			/**----------------------------------*
			 * TODO ???????? end
			 *------------------------------------*/
			
			//????
			canvas.drawText(num, (float)(iconSize-(backgroundWidth + textWidth+2*strokeThickness)/2), (float) (25*factor+2.5*strokeThickness), numPaint);
		}
		return numIcon;
	}
	
	/***
	 * ???????????
	 * @param context 
	 * @param clazz ???activity
	 * @param isShowNum ??????
	 * @param num ????????
	 * @param isStroke ??????
	 */
	
	public static void installRawShortCut(Context context, Class<?> clazz, boolean isShowNum, String num, boolean isStroke) {
		Log.e(TAG, "installShortCut....");
		
		Intent shortcutIntent = new Intent(	"com.android.launcher.action.INSTALL_SHORTCUT");
		//??
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,	context.getString(R.string.app_name));
		
		// ????????????????????true???????????????false???????
		shortcutIntent.putExtra("duplicate", false);
		
		//?????????activity
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setClass(context, clazz);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);
		
		//???????
		if(isStroke){
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
					generatorNumIcon4(
							context, 
							((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap(),
							isShowNum, 
							num));
		}else{
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
					generatorNumIcon2(
							context, 
							((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap(),
							isShowNum, 
							num));
		}
		context.sendBroadcast(shortcutIntent);
	}

																									
	/***
	 * ???????????
	 * @param context
	 * @return
	 */
	public static boolean isAddShortCut(Context context) {
		  Log.e(TAG, "isAddShortCut....");
	  
	    boolean isInstallShortcut = false;
	    final ContentResolver cr = context.getContentResolver();
	    
	    //TODO ???????????????ROM????????
	    /*int versionLevel = android.os.Build.VERSION.SDK_INT;
		        String AUTHORITY = "com.android.launcher2.settings";
		        //2.2?????????????????
		        if (versionLevel >= 8) {
		            AUTHORITY = "com.android.launcher2.settings";
		        } else {
		            AUTHORITY = "com.android.launcher.settings";
		        }*/
	
		String AUTHORITY = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
		Log.e(TAG, "AUTHORITY  :  " +AUTHORITY);
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
		+ "/favorites?notify=true");
	
		Cursor c = cr.query(CONTENT_URI,
		        new String[] { "title" }, "title=?",
		        new String[] { context.getString(R.string.app_name) }, null);
		
		if (c != null && c.getCount() > 0) {
		    isInstallShortcut = true;
		}
		
		if(c != null){
			c.close();
		}
		
		Log.e(TAG, "isAddShortCut....isInstallShortcut="+isInstallShortcut);
	    
	    return isInstallShortcut;
	}
	  
	/**
	 * ?????? 
	 * @param context
	 * @param clazz
	 */
	 public static void deleteShortCut(Context context, Class<?> clazz){
		 Log.e(TAG, "delShortcut....");
	  	
		 if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
			//??
			//??""??????????????)
			xiaoMiShortCut(context, clazz, "");
		  
		}else if(Build.MANUFACTURER.equalsIgnoreCase("samsung")){
			//??
			samsungShortCut(context, "0");
			
		}else {//????????
			//???????????
			deleteRawShortCut(context, clazz);
			//????????????
			//installRawShortCut(context, clazz, false, "0");
		}
	 }

	/***
	 * ???????????
	 * @param context
	 * @param clazz ???activity
	 */
	public static void deleteRawShortCut(Context context, Class<?> clazz) {
		Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		//???????
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		
		Intent intent2 = new Intent(); 
		intent2.setClass(context, clazz); 
		intent2.setAction(Intent.ACTION_MAIN); 
		intent2.addCategory(Intent.CATEGORY_LAUNCHER); 
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent2); 
		
		context.sendBroadcast(intent);
	}
	  
	  
	  /***
	 * ?????????URI
	 * @param context
	 * @param permission
	 * @return
	 */
	public static String getAuthorityFromPermission(Context context, String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packInfos == null) {
			return null;
		}
		for (PackageInfo info : packInfos) {
			ProviderInfo[] providers = info.providers;
			if (providers != null) {
				for (ProviderInfo provider : providers) {
					if (permission.equals(provider.readPermission)
							|| permission.equals(provider.writePermission)) {
						return provider.authority;
					}
				}
			}
		}
		return null;
	}

	/***
	 * ????????????????<br>
	 * 
	 * 
	 * @param context
	 * @param num ????????99??"99"???""??????????????)<br><br>
	 * 
	 * ????
	 * context.getPackageName()+"/."+clazz.getSimpleName() ??????activity??????"/."????
	 * 
	 */
	public static void xiaoMiShortCut(Context context,Class<?> clazz, String num)
	  {
		Log.e(TAG, "xiaoMiShortCut....");
		Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
		localIntent.putExtra("android.intent.extra.update_application_component_name", context.getPackageName()+"/."+clazz.getSimpleName());
		if(TextUtils.isEmpty(num)){
			num = "";
		}else{
		    int numInt = Integer.valueOf(num);
		    if (numInt > 0){
			  if (numInt > 99){
			        num = "99";
		      }
		    }else{
		    	num = "0";
		    }
		}
		localIntent.putExtra("android.intent.extra.update_application_message_text", num);
		context.sendBroadcast(localIntent);
	  }											
		 
	 /***
	  * ??????????????????
	  * @param context
	  * @param num
	  */
	public static void sonyShortCut(Context context, String num)
	  {
	    String activityName = getLaunchActivityName(context);
	    if (activityName == null){
	      return;
	    }
	    Intent localIntent = new Intent();
	    int numInt = Integer.valueOf(num);
	    boolean isShow = true;
	    if (numInt < 1){
	      num = "";
	      isShow = false;
	    }else if (numInt > 99){
	    	num = "99";
	    }
	    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);
	    localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
	    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", activityName);
	    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", num);
	    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());
	    context.sendBroadcast(localIntent);
	  }
		 
	 /***
	  * ??????????????????
	  * @param context
	  * @param num
	  */
	public static void samsungShortCut(Context context, String num)
	 {
		int numInt = Integer.valueOf(num);
	    if (numInt < 1)
	    {
	      num = "0";
	    }else if (numInt > 99){
	    	num = "99";
	    }
	 	String activityName = getLaunchActivityName(context);
	    Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
	    localIntent.putExtra("badge_count", Integer.parseInt(num));
	    localIntent.putExtra("badge_count_package_name", context.getPackageName());
	    localIntent.putExtra("badge_count_class_name", activityName);
	    context.sendBroadcast(localIntent);
	 }
		 
	 /***
	  * ??????????????
	  * @param clazz ???activity
	  * @param isShowNum ??????
	  * @param num ????????
	  * @param isStroke ??????
	  * 
	  */
	public static void addNumShortCut(Context context,Class<?> clazz,boolean isShowNum, String num, boolean isStroke)
	  {
		 Log.e(TAG, "manufacturer="+Build.MANUFACTURER);
	    if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
	    	//??
	      xiaoMiShortCut(context, clazz, num);
	      
	    }else if(Build.MANUFACTURER.equalsIgnoreCase("samsung")){
	    	//??
	    	samsungShortCut(context, num);
	    	
	    }else {//????????
	    	installRawShortCut(context, MainActivity.class, isShowNum, num, isStroke);
	    }
	    
	  }
		 
	 /***
	  * ?????????activity????
	  * mainfest.xml???? android:name:"
	  * @param context
	  * @return
	  */
	public static String getLaunchActivityName(Context context)
	 {
	    PackageManager localPackageManager = context.getPackageManager();
	    Intent localIntent = new Intent("android.intent.action.MAIN");
	    localIntent.addCategory("android.intent.category.LAUNCHER");
	    try
	    {
	      Iterator<ResolveInfo> localIterator = localPackageManager.queryIntentActivities(localIntent, 0).iterator();
	      while (localIterator.hasNext())
	      {
	        ResolveInfo localResolveInfo = localIterator.next();
	        if (!localResolveInfo.activityInfo.applicationInfo.packageName.equalsIgnoreCase(context.getPackageName()))
	          continue;
	        String str = localResolveInfo.activityInfo.name;
	        return str;
	      }
	    }
	    catch (Exception localException)
	    {
	      return null;
	    }
	    return null;
	 }
		 	
	/***
	 * ??????????????<br><br>
	 * ????????????????<shape>?xml???
	 * 
	 * @return
	 */
	private static ShapeDrawable getDefaultBackground(Context context) {
		
		//?????????????????????
		int r = dipToPixels(context,DEFAULT_CORNER_RADIUS_DIP);
		float[] outerR = new float[] {r, r, r, r, r, r, r, r};
	    
		//????
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setColor(DEFAULT_NUM_COLOR);//????
		return drawable;
		
	}
	/***
	 * ??????????????<br><br>
	 * ????????????????<shape>?xml???
	 * 
	 * @return
	 */
	private static ShapeDrawable getDefaultBackground2(Context context) {
		
		//?????????????????????
		int r = dipToPixels(context,DEFAULT_CORNER_RADIUS_DIP);
		float[] outerR = new float[] {r, r, r, r, r, r, r, r};
		int distance = dipToPixels(context,DEFAULT_STROKE_WIDTH_DIP);
		
		//????
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		customBorderDrawable drawable = new customBorderDrawable(context,rr);
		drawable.getFillpaint().setColor(DEFAULT_NUM_COLOR);//??????
		drawable.getStrokepaint().setColor(DEFAULT_STROKE_COLOR);//??????
		drawable.getStrokepaint().setStrokeWidth(distance);//??????
		return drawable;
		
	}
		
		
	/***
	 * ??????????????<br><br>
	 * ????????????????<shape>?xml???
	 * 
	 * @return
	 */
	private static ShapeDrawable getDefaultStrokeDrawable(Context context) {
		
		//?????????????????????
		int r = dipToPixels(context, DEFAULT_CORNER_RADIUS_DIP);
		int distance = dipToPixels(context, DEFAULT_STROKE_WIDTH_DIP);
		float[] outerR = new float[] {r, r, r, r, r, r, r, r};
	    
		//????
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setStrokeWidth(distance);
		drawable.getPaint().setStyle(Paint.Style.FILL);
		drawable.getPaint().setColor(DEFAULT_STROKE_COLOR);//????
		return drawable;
	}
		
	/***
	 * dp to px
	 * @param dip
	 * @return
	 */
	public static int dipToPixels(Context context, int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
}
