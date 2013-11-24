package abalufaske.where.is;

import android.util.Log;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;



/**
 * The extension receiver receives the extension intents and starts the
 * extension service when it arrives.
 */
public class SmartWatchService  extends ExtensionService {

    public static final String EXTENSION_KEY = "com.sonymobile.smartconnect.extension.advancedcontrolsample.key";

    public static final String LOG_TAG = "AdvancedControlExtension";

    public SmartWatchService() {
        super(EXTENSION_KEY);
    }

    /**
     * {@inheritDoc}
     *
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Debug", "Service: onCreate");
    }

    @Override
    protected RegistrationInformation getRegistrationInformation() {
        return new SampleRegistrationInformation(this);
    }

    /*
     * (non-Javadoc)
     * @see com.sonyericsson.extras.liveware.aef.util.ExtensionService#
     * keepRunningWhenConnected()
     */
    @Override
    protected boolean keepRunningWhenConnected() {
        return false;
    }

    @Override
    public ControlExtension createControlExtension(String hostAppPackageName) {
        Log.d("Debug", "Service: createControlExtension");
        boolean advancedFeaturesSupported = DeviceInfoHelper.isSmartWatch2ApiAndScreenDetected(
                this, hostAppPackageName);
        if (advancedFeaturesSupported) {
            Log.d("Debug",
                    "Service: Advanced features supported, returning SmartWatch2 extension control manager");
            return new ControlManagerSmartWatch2(this, hostAppPackageName);
        } else {
            Log.d("Debug",
                    "Service: Advanced features not supported, exiting");
            // Possible to return a legacy control extension here
            throw new IllegalArgumentException("No control for: " +
                    hostAppPackageName);
        }
    }
}
