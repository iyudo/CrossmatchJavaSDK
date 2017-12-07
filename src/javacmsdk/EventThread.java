package javacmsdk;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.AWTEventMulticaster;

public class EventThread extends Thread
{
	public EventThread()
	{
		setPriority(Thread.MIN_PRIORITY);
		setDaemon(true);
		start();
	}

	public void run()
	{
		while (true) try
			{
				if (jni.resetConnect())
				{
					fireEvent(jni.CONNECT_EVENT);
				}
				if (jni.resetDisconnect())
				{
					fireEvent(jni.DISCONNECT_EVENT);
				}
				if (jni.resetAutoCapture())
				{
					fireEvent(jni.AUTOCAPTURE_EVENT);
				}
				if (jni.resetFingerDetect())
				{
					fireEvent(jni.FINGER_DETECT_EVENT);
				}
				if (jni.resetLiveImageReady())
				{
					fireEvent(jni.LIVE_IMAGE_READY_EVENT);
				}
				// Thread sleep of 300 msec is fast enought for Connect, Disconnect and Autocapture
				// Thread sleep of 100 msec is needed for responsiveness of Finger detect and Image ready
				Thread.sleep(100);
			}
			catch (InterruptedException ie) { }
	}


	//----------------------------------------------------------------------------
	protected void fireEvent(int EventID)
	{
		if (actionListener != null)
		{
			String cmd = new String();
			switch (EventID)
			{
				case jni.CONNECT_EVENT:
					cmd = "CONNECT_EVENT";
					break;
				case jni.DISCONNECT_EVENT:
					cmd = "DISCONNECT_EVENT";
					break;
				case jni.AUTOCAPTURE_EVENT:
					cmd = "AUTOCAPTURE_EVENT";
					break;
				case jni.FINGER_DETECT_EVENT:
					cmd = "FINGER_DETECT_EVENT";
					break;
				case jni.LIVE_IMAGE_READY_EVENT:
					cmd = "LIVE_IMAGE_READY_EVENT";
					break;
			}
			actionListener.actionPerformed(new ActionEvent(this, EventID, cmd));
		}
	}

	protected static ActionListener actionListener;
	public static void addActionListener(ActionListener listener)
	{
		actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	public static void removeActionListener(ActionListener listener)
	{
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
}
