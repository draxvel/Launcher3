package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class AutoAddWidgetReceiver extends BroadcastReceiver {

    private static final String TAG = "AutoAddWidgetReceiver";

    public static final String ACTION_AUTOADDWIDGET = "com.android.launcher3.action.AUTOADDWIDGET";

    public static final String EXTRA_PACKAGE_NAME = "packageName";

    public static final String EXTRA_CLASS_NAME = "className";

//    private LauncherApps.PinItemRequest mRequest;
//    private LauncherAppState mApp;
//    private InvariantDeviceProfile mIdp;
//
//    // Widget request specific options.
//    private LauncherAppWidgetHost mAppWidgetHost;
//    private AppWidgetManagerCompat mAppWidgetManager;
//    private PendingAddWidgetInfo mPendingWidgetInfo;
//    private int mPendingBindWidgetId;
//    private Bundle mWidgetOptions;

    public AutoAddWidgetReceiver() {
    }

//    public Intent createShortcutResultIntent(@NonNull ShortcutInfo inShortcut, int userId) {
        // Find the default launcher activity
//        final int launcherUserId = mService.getParentOrSelfUserId(userId);
//        final ComponentName defaultLauncher = mService.getDefaultLauncher(launcherUserId);
//        if (defaultLauncher == null) {
//            Log.e(TAG, "Default launcher not found.");
//            return null;
//        }
//
//        // Make sure the launcher user is unlocked. (it's always the parent profile, so should
//        // really be unlocked here though.)
//        mService.throwIfUserLockedL(launcherUserId);
//
//        // Next, validate the incoming shortcut, etc.
//        final LauncherApps.PinItemRequest request = requestPinShortcutLocked(inShortcut, null,
//                Pair.create(defaultLauncher, launcherUserId));
//        return new Intent().putExtra(LauncherApps.EXTRA_PIN_ITEM_REQUEST, request);
//    }

//    private boolean setupWidget() {
//        LauncherAppWidgetProviderInfo widgetInfo = LauncherAppWidgetProviderInfo
//                .fromProviderInfo(this, mRequest.getAppWidgetProviderInfo(this));
//        if (widgetInfo.minSpanX > mIdp.numColumns || widgetInfo.minSpanY > mIdp.numRows) {
//            // Cannot add widget
//            return false;
//        }
//        mWidgetCell.setPreview(PinItemDragListener.getPreview(mRequest));
//
//        mAppWidgetManager = AppWidgetManagerCompat.getInstance(this);
//        mAppWidgetHost = new LauncherAppWidgetHost(context);
//
//        mPendingWidgetInfo = new PendingAddWidgetInfo(widgetInfo);
//        mPendingWidgetInfo.spanX = Math.min(mIdp.numColumns, widgetInfo.spanX);
//        mPendingWidgetInfo.spanY = Math.min(mIdp.numRows, widgetInfo.spanY);
//        mWidgetOptions = WidgetHostViewLoader.getDefaultOptionsForWidget(this, mPendingWidgetInfo);
//
//        WidgetItem item = new WidgetItem(widgetInfo, getPackageManager(), mIdp);
//        mWidgetCell.getWidgetView().setTag(mPendingWidgetInfo);
//        mWidgetCell.applyFromCellItem(item, mApp.getWidgetCache());
//        mWidgetCell.ensurePreview();
//        return true;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void acceptWidget(int widgetId) {
//        InstallShortcutReceiver.queueWidget(mRequest.getAppWidgetProviderInfo(this), widgetId, this);
//        mWidgetOptions.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
//        mRequest.accept(mWidgetOptions);
//    }


//    mRequest = LauncherAppsCompatVO.getPinItemRequest(intent);
//    if (mRequest == null) {
//        Log.d(TAG, "onReceive: mRequest == null");
//    }

//    mApp = LauncherAppState.getInstance(context);
//    mIdp = mApp.getInvariantDeviceProfile();


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "I'm in AutoAddWidgetReceiver", Toast.LENGTH_SHORT).show();

        if (ACTION_AUTOADDWIDGET.equals(intent.getAction())) {
            String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            String className = intent.getStringExtra(EXTRA_CLASS_NAME);
            Log.d(TAG, "onReceive: packageName "+packageName);
            Log.d(TAG, "onReceive: className "+className);

            int screen = 0;
            int x = 1;
            int y = -1;
            int spanX = 1;
            int spanY = 1;

            try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setIgnoringComments(true);
//                DocumentBuilder builder = domFactory.newDocumentBuilder();

                AssetManager assetManager = context.getAssets();

                InputStream inputStream = null;
                inputStream = assetManager.open("articles.xml");

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);

//                File is = context.getResources().getXml(R.xml.dw_phone_hotseat);
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document doc = builder.parse(is);
//
//                File file = context.getResources().getXml(R.xml.dw_phone_hotseat);
//
//                Document doc = builder.parse(file);

                Element dataTag = doc.getDocumentElement();
                Element favorites =  (Element) dataTag.getElementsByTagName("favorites").item(0);

                Element appwidget = doc.createElement("appwidget");

                Element screenEl = doc.createElement("screen");
                screenEl.setTextContent(String.valueOf(screen));

                Element xEl = doc.createElement("x");
                xEl.setTextContent(String.valueOf(x));

                Element yEl = doc.createElement("y");
                yEl.setTextContent(String.valueOf(y));

                Element spanXEl = doc.createElement("spanX");
                spanXEl.setTextContent(String.valueOf(spanX));

                Element spanYEl = doc.createElement("spanY");
                spanYEl.setTextContent(String.valueOf(spanY));

                appwidget.appendChild(screenEl);
                appwidget.appendChild(xEl);
                appwidget.appendChild(yEl);
                appwidget.appendChild(spanXEl);
                appwidget.appendChild(spanYEl);

                favorites.appendChild(appwidget);

                LauncherAppState app = LauncherAppState.getInstanceNoCreate();
                if (app != null) {
                    app.getModel().forceReload();
                }

                //read
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                StreamResult result = new StreamResult(new StringWriter());
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);

                String xmlOutput = result.getWriter().toString();
                Log.d(TAG, xmlOutput);

            }catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }


}
