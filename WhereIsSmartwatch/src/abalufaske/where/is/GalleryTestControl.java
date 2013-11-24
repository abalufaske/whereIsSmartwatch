/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package abalufaske.where.is;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;

/**
 * GalleryTestControl displays a swipeable gallery, based on a string array.
 */
public class GalleryTestControl extends ManagedControlExtension {

    protected int mLastKnowPosition = 0;
    public final static String EXTRA_INITIAL_POSITION = "EXTRA_INITIAL_POSITION";

    /**
     * String array with dummy data to be displayed in gallery.
     */
    private String[] mGalleryContent = {
            "Parking", "Location 1", "Location 2", "Location 3", "Location 4",
    };
    
    private String[] mButtonContent = {
            "Button 0", "Button 1", "Button 2", "Button 3", "Button 4",
    };
    
    private List<Place> mListPlaces;
    private Activity mainActivity;

    /**
     * @see ManagedControlExtension#ManagedControlExtension(Context, String,
     *      ControlManagerCostanza, Intent)
     */
    public GalleryTestControl(Context context, String hostAppPackageName,
            ControlManagerSmartWatch2 controlManager, Intent intent) {
    	
        super(context, hostAppPackageName, controlManager, intent);
        
        this.mContext = context;
        
		ItemsDB mPlaceDB = new ItemsDB(context);
		mListPlaces = mPlaceDB.getAll(context, 0);
		mPlaceDB.close();
		mGalleryContent[1] = mListPlaces.get(0).getName();
		mGalleryContent[2] = mListPlaces.get(1).getName();
		mGalleryContent[3] = mListPlaces.get(2).getName();
		mGalleryContent[4] = mListPlaces.get(3).getName();
    }
    
    private Context mContext;
    
    @Override
    public void onResume() {
        Log.d("Debug", "onResume");
        showLayout(R.layout.layout_test_gallery, null);
        sendListCount(R.id.gallery, mGalleryContent.length);

        // If requested, move to the correct position in the list.
        int startPosition = getIntent().getIntExtra(EXTRA_INITIAL_POSITION, 0);
        mLastKnowPosition = startPosition;
        sendListPosition(R.id.gallery, startPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Position is saved into Control's Intent, possibly to be used later.
        getIntent().putExtra(EXTRA_INITIAL_POSITION, mLastKnowPosition);
    }

    @Override
    public void onRequestListItem(final int layoutReference, final int listItemPosition) {
        Log.d("Debug", "onRequestListItem() - position " + listItemPosition);
        if (layoutReference != -1 && listItemPosition != -1 && layoutReference == R.id.gallery) {
            ControlListItem item = createControlListItem(listItemPosition);
            if (item != null) {
                sendListItem(item);
            }
        }
    }

    @Override
    public void onListItemSelected(ControlListItem listItem) {
        super.onListItemSelected(listItem);
        // We save the last "selected" position, this is the current visible
        // list item index. The position can later be used on resume
        mLastKnowPosition = listItem.listItemPosition;
    }
    
    @Override
    public void onListItemClick(final ControlListItem listItem, final int clickType, final int itemLayoutReference) {
        
    	final int position = listItem.listItemPosition;
    	
    	if (position != 0) {
    		
    		lunchApplication(mContext);
    		
    		final Handler handler = new Handler();
    		
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							MainActivity.saveLocation(mContext, position-1);
						}
					});
				}
			}).start();
		}
    	
    	Log.d("Debug", "Item clicked. Position " + listItem.listItemPosition
                + ", itemLayoutReference " + itemLayoutReference + ". Type was: "
                + (clickType == Control.Intents.CLICK_TYPE_SHORT ? "SHORT" : "LONG"));
    }
    
    private void lunchApplication(Context mContext) {
    	
    	Intent intent = new Intent(mContext, InitActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
    
    protected ControlListItem createControlListItem(int position) {
    	
    	String parking = "";
    	
    	if (position == 0) {
    		ParkingDB mParkingDB = new ParkingDB(mContext);
    		List<Parking> park = mParkingDB.getAll(mContext, 0);
    		parking = (mContext.getString(R.string.FloorParking) +":"+ park.get(0).getFloor()+" - " + mContext.getString(R.string.NumberParking) +":"+ park.get(0).getNumber()+" - " + mContext.getString(R.string.SectionParking) +":"+ park.get(0).getSection() );
		}
    	
        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.gallery;
        item.dataXmlLayout = R.layout.item_gallery;
        item.listItemId = position;
        item.listItemPosition = position;
        
        // Header data
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Control.Intents.EXTRA_TEXT, mGalleryContent[position]);
        
        // Body data
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Control.Intents.EXTRA_TEXT, parking);
        
        // Header data
        Bundle button1Bundle = new Bundle();
        button1Bundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.mButtonText1);
        button1Bundle.putString(Control.Intents.EXTRA_TEXT, mButtonContent[position]);
        
        item.layoutData = new Bundle[4];
        item.layoutData[0] = headerBundle;
        item.layoutData[1] = bodyBundle;
        item.layoutData[2] = button1Bundle;
        
        return item;
    }
}
