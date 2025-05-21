package com.cyntra.kds.printer.epson;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.cyntra.kds.R;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeviceList {

    public static final String TAG = DeviceList.class.getSimpleName();
    public static final boolean TEST = false;
    public static final boolean EPOS_DISCOVER_LOCAL = true;
    public enum Model {Epson, Star, Cravrr, PosIn, PosIn80,Pax,EpsonTM220,AnyPos, All}

    public enum Port {USB, TCP, All}

    public interface Listener {
        void update(HashMap<String, Object> printer);
    }

    private final Context activity;
    private Listener listener;

    public DeviceList(Context activity) {
        this.activity = activity;
    }


    public void discover(Model model, Port port, Listener listener) {

        this.listener = listener;

        // first find tcp devices and then usb devices
        // but results depend on delay in tcp response

        if (model == Model.All || model == Model.Epson) {
            eposDiscover(port);
        }
    //Modifie By Chandan
    //Date-2/19/2018
    //Reason-To set EpsonTM220 printer
        if (model == Model.All || model == Model.EpsonTM220) {
            eposTM220Discover(port);
        }
        if (model == Model.All || model == Model.Cravrr) {
            usbDiscover();
        }
        if (model == Model.All ||  model == Model.AnyPos) {
            usbAnyPosDiscover();
        }
    }

    private static final String KEY_MODEL = "Model";
    private static final String KEY_PORT = "Port";
    private static final String KEY_PRINTER_NAME = "PrinterName";  // no longer used
    private static final String KEY_TARGET = "Target";

    private void updateListener(Model model, Port port, String printerName, String target) {
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(KEY_MODEL, model);
        item.put(KEY_PORT, port);
        item.put(KEY_PRINTER_NAME, printerName);
        item.put(KEY_TARGET, target);
        listener.update(item);
    }


    private void eposDiscover(Port port) {
        if (EPOS_DISCOVER_LOCAL) {
            eposDiscover_Local(port);
        } else {
            eposDiscover_RemoteAPK(port);
        }
    }

    //Modifie by Chandan
    //Date-2/19/2018
    //Reason-To setup epsonTM220
    private void eposTM220Discover(Port port) {
        if (EPOS_DISCOVER_LOCAL) {
            eposTM220Discover_Local(port);
        } else {
            eposDiscover_RemoteAPK(port);
        }
    }

    private void stopDiscovery() {
        if (EPOS_DISCOVER_LOCAL) {
            stopDiscovery_Local();
        } else {
            stopDiscovery_RemoteAPK();
        }
    }


//	private void eposDiscover() {
//
//		//TODO: or change to AsyncTask
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					// Finder.start(context.getBaseContext(), DevType.TCP,
//					// "255.255.255.0");
//					Finder.start(context.getApplicationContext(), DevType.TCP,
//							"255.255.255.0");
//					String[] results = Finder.getResult();
//					if (results != null) {
//						for (String result : results) {
//							updateListener(result, result);
//						}
//					}
//					Finder.stop();
//				} catch (EpsonIoException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}

    private void eposDiscover_Local(final Port port) {
        FilterOption filterOption = new FilterOption();
        if (port == Port.TCP) {
            filterOption.setPortType(Discovery.PORTTYPE_TCP);
        } else if (port == Port.USB) {
            filterOption.setPortType(Discovery.PORTTYPE_USB);
        } else {
            filterOption.setPortType(Discovery.PORTTYPE_ALL);
        }
        // filterOption.setBroadcast(mEdtBroadCast.getText().toString());
        filterOption.setDeviceModel(Discovery.MODEL_ALL);
        filterOption.setEpsonFilter(Discovery.FILTER_NONE);
        filterOption.setDeviceType(Discovery.TYPE_ALL);
        try {
            Discovery.start(activity, filterOption, new DiscoveryListener() {
                @Override
                public void onDiscovery(DeviceInfo deviceInfo) {
                    //updateListener(deviceInfo.getDeviceName(),
                    //		deviceInfo.getTarget());
                    String target;
                    if (port == Port.TCP) {
                        target = deviceInfo.getIpAddress();
                    } else {
                        target = deviceInfo.getTarget();
                    }
                    updateListener(Model.Epson, port, deviceInfo.getDeviceName(), target);

                }
            });

            if (TEST) {
                new Thread() {
                    @Override
                    public void run() {
                        sleepy(2000);
                        //updateListener("TCP_1", "192.168.1.100");
                        updateListener(Model.Epson, port, "TCP_1", "a5:s7:23:45:32:11");
                        sleepy(5000);
                        //updateListener("TCP_2", "192.168.2.200");
                        updateListener(Model.Epson, port, "TCP_2", "a5:s7:23:45:32:12");
                    }
                }.start();
            }

        } catch (Epos2Exception e) {
            e.printStackTrace();
        }
    }

    //Modifie by Chandan
    //Date-2/19/2018
    //Reason-Discover epsonTM220 printer
    private void eposTM220Discover_Local(final Port port) {
        FilterOption filterOption = new FilterOption();
        if (port == Port.TCP) {
            filterOption.setPortType(Discovery.PORTTYPE_TCP);
        } else if (port == Port.USB) {
            filterOption.setPortType(Discovery.PORTTYPE_USB);
        } else {
            filterOption.setPortType(Discovery.PORTTYPE_ALL);
        }
        // filterOption.setBroadcast(mEdtBroadCast.getText().toString());
        filterOption.setDeviceModel(Discovery.MODEL_ALL);
        filterOption.setEpsonFilter(Discovery.FILTER_NONE);
        filterOption.setDeviceType(Discovery.TYPE_ALL);
        try {
            Discovery.start(activity, filterOption, new DiscoveryListener() {
                @Override
                public void onDiscovery(DeviceInfo deviceInfo) {
                    //updateListener(deviceInfo.getDeviceName(),
                    //		deviceInfo.getTarget());
                    String target;
                    if (port == Port.TCP) {
                        target = deviceInfo.getIpAddress();
                    } else {
                        target = deviceInfo.getTarget();
                    }
                    updateListener(Model.EpsonTM220, port, deviceInfo.getDeviceName(), target);

                }
            });

            if (TEST) {
                new Thread() {
                    @Override
                    public void run() {
                        sleepy(2000);
                        //updateListener("TCP_1", "192.168.1.100");
                        updateListener(Model.EpsonTM220, port, "TCP_1", "a5:s7:23:45:32:11");
                        sleepy(5000);
                        //updateListener("TCP_2", "192.168.2.200");
                        updateListener(Model.EpsonTM220, port, "TCP_2", "a5:s7:23:45:32:12");
                    }
                }.start();
            }

        } catch (Epos2Exception e) {
            e.printStackTrace();
        }
    }



    private void sleepy(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    private void stopDiscovery_Local() {
        try {
            Discovery.stop();
        } catch (Epos2Exception e) {
            if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                // ShowMsg.showException(e, "stop", mContext);
            }
        }
    }


    private void usbDiscover() {
        new UsbTask(Model.Cravrr).execute();

        if (TEST) {
            new Thread() {
                @Override
                public void run() {
                    sleepy(200);
                    updateListener(Model.Cravrr, Port.USB, "USB_1", "1101");
                    sleepy(100);
                    updateListener(Model.Cravrr, Port.USB, "USB_2", "1102");
                }
            }.start();
        }
    }

    private void usbAnyPosDiscover() {
        new UsbTask(Model.AnyPos).execute();

        if (TEST) {
            new Thread() {
                @Override
                public void run() {
                    sleepy(200);
                    updateListener(Model.AnyPos, Port.USB, "USB_1", "1101");
                    sleepy(100);
                    updateListener(Model.AnyPos, Port.USB, "USB_2", "1102");
                }
            }.start();
        }
    }

    private class UsbTask extends AsyncTask<Void, String, Void> {
        Model model;
        public UsbTask(Model model) {
            this.model=model;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            UsbDevice mUsbDevice;

            UsbManager mUsbManager = (UsbManager) activity
                    .getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            mUsbDevice = null;
            int count = 0;
            while (deviceIterator.hasNext()) {
                mUsbDevice = deviceIterator.next();
                Log.i("info", "Device No " + count + "........");
                Log.i("info", "Vendor id : " + mUsbDevice.getVendorId());
                // Log.i("info", "Product Name : " + mUsbDevice.getProductName());
                Log.i("info", "Product id : " + mUsbDevice.getProductId());
                Log.i("info", "Device  name : " + mUsbDevice.getDeviceName());
                Log.i("info", "Device class : " + mUsbDevice.getClass().getName());
                Log.i("info", "Device protocol: " + mUsbDevice.getDeviceProtocol());
                Log.i("info", "Device subclass : " + mUsbDevice.getDeviceSubclass());

                Log.d("info", "Device No " + count + "........");
                Log.d("info", "Vendor id : " + mUsbDevice.getVendorId());
                // Log.i("info", "Product Name : " + mUsbDevice.getProductName());
                Log.d("info", "Product id : " + mUsbDevice.getProductId());
                Log.d("info", "Device  name : " + mUsbDevice.getDeviceName());
                Log.d("info", "Device class : " + mUsbDevice.getClass().getName());
                Log.d("info", "Device protocol: " + mUsbDevice.getDeviceProtocol());
                Log.d("info", "Device subclass : " + mUsbDevice.getDeviceSubclass());

                //if (mUsbDevice.getDeviceClass() == UsbConstants.USB_CLASS_PRINTER) {
                //TODO: map printer model from vendor id here... or from a field above...
                publishProgress(""+model, "" + mUsbDevice.getVendorId());
                //}
                count++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateListener(values[0], values[1]);
            String printerName = values[0];
            String id = values[1];
            updateListener(printerName.equalsIgnoreCase("Cravrr")? Model.Cravrr: Model.AnyPos, Port.USB, printerName, id);
        }
    }


    public interface DeviceInfoListener {
        void update(String name,Model model, Port port, String id);
    }

    public static void dialog(final Context context, final EditText targetText, Model model, Port port, final DeviceInfoListener listener) {
        ListView dialog_ListView;



           final Dialog dailogSearchReq = new Dialog(context, R.style.dialog); // Context, this, etc.
           dailogSearchReq.setContentView(R.layout.search_printer_layout);
           dailogSearchReq.getWindow().setLayout(800, 350);

//        PrinterIpAdapter printerIpAdapter = new PrinterIpAdapter(context);
        try {
           final DeviceList deviceList = new DeviceList(context);

           //Prepare ListView in dialog
           dialog_ListView = (ListView) dailogSearchReq.findViewById(R.id.dialoglist);
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_singlechoice);

           final List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();

           dialog_ListView.setAdapter(adapter);
           dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

               @Override
               public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                   //  TODO Auto-generated method stub

                   String strName = adapter.getItem(position);
                   //Toast.makeText(Home_Screen.context, strName + " is selected", Toast.LENGTH_SHORT).show();
                   //if (strName)
                   targetText.setText(strName);
                   deviceList.stopDiscovery();
                   dailogSearchReq.dismiss();

                   Map<String, Object> map = info.get(position);
                   Model model = (Model) map.get(KEY_MODEL);
                   Port port = (Port) map.get(KEY_PORT);
                   String id1 = (String) map.get(KEY_TARGET);
                   String printerName = (String) map.get(KEY_PRINTER_NAME);
                   if (listener != null) {
                       listener.update(printerName, model, port, id1);
                   }
               }
           });


           deviceList.discover(model, port, new Listener() {

               @Override
               public void update(final HashMap<String, Object> printer) {
                   ((Activity) context).runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           //ListView list = dialog.getListView();
                           //ArrayAdapter<String> adapter = (ArrayAdapter<String>)list.getAdapter();


                           //TODO: also now have Model and Port
                           info.add(printer);

                           adapter.add((String) printer.get("Target"));
                           adapter.notifyDataSetChanged();
                       }
                   });
               }
           });
            ((ImageView) dailogSearchReq.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceList.stopDiscovery();
                    dailogSearchReq.dismiss();
                }
            });

        }catch (Exception e){}


//        dailogSearchReq.findViewById(R.id.btn_confirm)

           dailogSearchReq.show();



    }


    // below code is for running epos discovery in another apk
    // it was needed because china printer would not work correctly with new epos driver
    // it is no longer needed, because using own driver for china closeOrderForWendysPrint now


    public static final String DISCOVERY_STATUS_INTENT = "com.restaurants.cravrr.discovery.STATUS";
    public static final String DISCOVERY_STATUS_MSG = "com.restaurants.cravrr.discovery.STATUS_MSG";

    public static final String EPOS_PING = "EPOS_PING";
    public static final String EPOSTM220_PING = "EPOS220_PING";
    public static final String EPOS_START = "EPOS_START";
    public static final String EPOS_STOP = "EPOS_STOP";

    private void eposDiscover_RemoteAPK(Port port) {

        // first be sure service is avaliable
        if (startService(EPOS_PING, null) != null) {

            mMessageReceiver = new DiscoverReceiver();

            // register the receiver
            activity.registerReceiver(mMessageReceiver, new IntentFilter(
                    DISCOVERY_STATUS_INTENT));

//			LocalBroadcastManager.getInstance(activity)
//					.registerReceiver(mMessageReceiver,
//							new IntentFilter(DISCOVERY_STATUS_INTENT));

            // start the service
            startService(EPOS_START, port.toString());
        }
    }

    //Modifie by chandan
    //Date-2/19/2018
    //Reason-To setUp epson220
    private void eposTM220Discover_RemoteAPK(Port port) {

        // first be sure service is avaliable
        if (startService(EPOS_PING, null) != null) {

            mMessageReceiver = new DiscoverReceiver();

            // register the receiver
            activity.registerReceiver(mMessageReceiver, new IntentFilter(
                    DISCOVERY_STATUS_INTENT));

//			LocalBroadcastManager.getInstance(activity)
//					.registerReceiver(mMessageReceiver,
//							new IntentFilter(DISCOVERY_STATUS_INTENT));

            // start the service
            startService(EPOS_START, port.toString());
        }
    }

    private void stopDiscovery_RemoteAPK() {

        // shut down the receiver
        if (mMessageReceiver != null) {
            activity.unregisterReceiver(mMessageReceiver);
            mMessageReceiver = null;

            // stop the service
            startService(EPOS_STOP, null);

            // LocalBroadcastManager.getInstance(activity).unregisterReceiver(
            // mMessageReceiver);
        }
    }

    private ComponentName startService(String msg, String port) {
        Intent intent = new Intent();
        intent.setClassName("com.restaurants.cravrr.discovery", "com.restaurants.cravrr.service.DiscoveryService");
        intent.setData(Uri.parse(msg));
        intent.putExtra("port", port);
        return activity.startService(intent);
    }

    private class DiscoverReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(DISCOVERY_STATUS_MSG);
            Log.d("receiver", "Got message: " + message);

            String[] params = message.split(",");
            Port port;
            String target;
            if (params.length > 1) {
                port = lookupPort(params[0]);
                target = params[1];
            } else {
                Log.e(TAG, "wrong version of Discovery?");
                port = Port.All;
                target = params[0];
            }

            updateListener(Model.Epson, port, "", target);
        }
    }



    private static Port lookupPort(String name) {
        try {
            return (name == null ? Port.All : Port.valueOf(name));
        } catch (IllegalArgumentException e) {
            return Port.All;
        }
    }

    private BroadcastReceiver mMessageReceiver;

//	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// Get extra data included in the Intent
//			String message = intent.getStringExtra(DISCOVERY_STATUS_MSG);
//			Log.d("receiver", "Got message: " + message);
//
//			String[] params = message.split(",");
//			String deviceName = params[0];
//			String target = params[1];
//
//			updateListener(deviceName, target);
//		}
//	};
}
