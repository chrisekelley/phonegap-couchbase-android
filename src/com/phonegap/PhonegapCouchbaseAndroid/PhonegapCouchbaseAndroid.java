package com.phonegap.PhonegapCouchbaseAndroid;

import android.os.Bundle;
import android.util.Log;

import java.net.PasswordAuthentication;

// Couchbase changes START
import com.couchbaseextensions.CouchMover;
import com.couchbaseextensions.CouchStarter;
import com.couchbaseextensions.CouchStarter.CouchStarterCallback;
// Couchbase changes END
    
import com.phonegap.*;

public class PhonegapCouchbaseAndroid extends DroidGap
{
   	protected static final String LOG_TAG = "PhonegapCouchbaseAndroid";

   	private CouchStarter couchStarter;
   	private Boolean couchStarted = false;

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Couchbase changes START
		startCouch();
        // Couchbase changes END

        super.loadUrl("file:///android_asset/www/index.html");
    }

	// Couchbase changes START
	@Override
	public void onRestart() 
	{
		super.onRestart();
		startCouch();
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		try 
		{
			if(couchStarter!=null) {
				Log.v(LOG_TAG, "Stopping CouchDb");
				couchStarter.stopCouch();
			} else {
				Log.v(LOG_TAG, "couchStarter is null; nothing to stop.");
			}
		} 
		catch (IllegalArgumentException e) 
		{
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!couchStarted) {
			startCouch();
		}
	}

	private void startCouch() 
	{
		if(couchStarter==null)
		{
		   	CouchStarterCallback callback = new CouchStarterCallback() 
		    {
		   		@Override
  				public void couchStarted(String host, int port)
  				{
  					String serverURL = "http://" + host + ":" + Integer.toString(port) + "/";

  					PasswordAuthentication credential = new PasswordAuthentication("admin", "admin".toCharArray());
  					credential = null; // NO USER/PASSWORD ON ANDROID
  		            //CouchMover couchMover = new CouchMover(serverURL, credential, "mycouchapp_db");
  					
  	  			    // Add MobileFuton
  					CouchMover mFCouchMover = new CouchMover(serverURL, credential, "mobilefuton");
  					mFCouchMover.loadDocumentFromAssetManager(getAssets(), "_design/mobilefuton", "mobilefuton.json", "mobilefuton.version");
			        
  					CouchMover couchMover = new CouchMover(serverURL, credential, "odk");
				    
  			    // load the coachapp if needed from a bundle (you can create non-bundle loading options through loadDocument)
  	  			    //couchMover.loadDocumentFromAssetManager(getAssets(), "_design/mycouchapp", "mycouchapp.json", "mycouchapp.version");
  	  			    couchMover.loadDocumentFromAssetManager(getAssets(), "_design/render", "odk.json", "odk.version");
  	  			    couchMover.loadDocumentFromAssetManager(getAssets(), "ArrestDocket", "ad.json", "ad.version");
  	  			    couchMover.loadDocumentFromAssetManager(getAssets(), "PatientRegistration", "pr.json", "pr.version");
  	  			    

  			    // load the data if needed from a bundle (you can create non-bundle loading options through loadDocument)
  			    //couchMover.loadDocumentFromAssetManager(getAssets(), "mydata", "mydata.json", "mydata.version");

  			    // load the data views if needed from a bundle (you can create non-bundle loading options through loadDocument)
  			    //couchMover.loadDocumentFromAssetManager(getAssets(), "_design/mydata_views", "mydata_views.json", "mydata_views.version");
					//couchMover.gotoAppPage("_design/mycouchapp", appView, "index.html");
  					couchMover.gotoAppPage("_design/render", appView, "index.html");
  					couchStarted = true;
  				} 
				
		   		@Override
				  public void failed() { Log.v(LOG_TAG, "Failed to install and start Couchbase"); }
			};

			couchStarter = new CouchStarter(this, callback);
			couchStarter.startCouch();
		}
	}
	// Couchbase changes END
}