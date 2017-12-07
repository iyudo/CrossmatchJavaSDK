package javacmsdk;

import java.awt.event.*;

public class jni
{
  static {System.loadLibrary("jVerifier");init();}

  public final static int MAX_WIDTH = 640;
  public final static int MAX_HEIGHT = 512;

  public final static int USB_ROTATION_0 = 0;
  public final static int USB_ROTATION_90 = 1;
  public final static int USB_ROTATION_180 = 2;
  public final static int USB_ROTATION_270 = 3;

  public native static void init();

// This function initializes the USB device to capture images at the dimensions passed in.
  public native static void USB_Initialize(short Width, short Height);

//  This function un-initializes the USB device. USB_Uninitialize should only be called
//  when scanner is disconnected or the application exits!
  public native static void USB_Uninitialize();

// This function will free all dynamically allocated memory stop threads and free
// any resources that have been allocated. Always call this when application exits.
  public native static void USB_Dispose();

  public native static void USB_Reset();

//  This function retrieves the product code of the scanner.
  public native static byte USB_GetProductCode();

  public native static void USB_LoadSettings();
  public native static void USB_SaveSettings();

//  This function retrieves the current brightness setting of the scanner.
  public native static byte USB_GetBrightness();
//  This function sets the brightness setting of the scanner.
//  This feature ONLY applicable for V300 LC 2.0 series scanners.
  public native static void USB_SetBrightness(byte Brightness);

//  This function retrieves the current contrast setting of the scanner.
  public native static byte USB_GetContrast();
//  This function sets the contrast setting of the scanner.
//  This feature ONLY applicable for V300 LC 2.0 series scanners.
  public native static void USB_SetContrast(byte Contrast);

//  This function retrieves the current gain setting of the scanner.
  public native static byte USB_GetGain();
//  This function sets the gain setting of the scanner.
//  This feature ONLY applicable for V300 LC 2.0 series scanners.
  public native static void USB_SetGain(byte Gain);

//  This function retrieves the flag indicating whether the image is flipped horizontally.
  public native static boolean USB_GetFlipHorizontal();
//  This function sets the flag indicating whether the image is flipped horizontally.
  public native static void USB_SetFlipHorizontal(boolean FlipHorizontal);

//  This function retrieves the flag indicating whether the image is flipped vertically.
  public native static boolean USB_GetFlipVertical();
//  This function sets the flag indicating whether the image is flipped vertically.
  public native static void USB_SetFlipVertical(boolean FlipVertical);

//  This function retrieves the flag indicating whether the image is inverted.
  public native static boolean USB_GetInverse();
//  This function sets the flag indicating whether the image is inverted.
  public native static void USB_SetInverse(boolean Inverse);

// This function retrieves the current rotation of the image.
  public native static int USB_GetRotation();
  // This function sets the current rotation of the image.
  public native static void USB_SetRotation(int Rotation);


//  This function retrieves the image size at which the device is currently capturing.
  public native static java.awt.Dimension USB_GetImageSize();
//  This function sets the image size at which the device is capturing.
  public static void USB_SetImageSize(java.awt.Dimension size) {
    USB_SetImageSize( (short) size.width, (short) size.height);
  }
  public native static void USB_SetImageSize(short Width, short Height);


 ////////////////////////////////////////////////////////////////////////////////
// Function prototypes for fingerprint auto-capture licensed scanners
////////////////////////////////////////////////////////////////////////////////
  public native static boolean USB_IAC_GetAutoCapture();
  public native static void USB_IAC_SetAutoCapture(boolean AutoCapture);
  public native static java.awt.Dimension USB_IAC_GetImageSizeThresholds();
  public static void USB_IAC_SetImageSizeThresholds(java.awt.Dimension size) {
    USB_IAC_SetImageSizeThresholds( (short) size.width, (short) size.height);
  }
  public native static void USB_IAC_SetImageSizeThresholds(short MinWidth, short MinHeight);
  public native static short USB_IAC_GetMinHorRidgeCount();
  public native static void USB_IAC_SetMinHorRidgeCount(short HorRidgeCnt);
  public native static short USB_IAC_GetMinVerRidgeCount();
  public native static void USB_IAC_SetMinVerRidgeCount(short VerRidgeCnt);
  public native static short USB_IAC_GetGrayscaleWhiteThresholds();
  public native static void USB_IAC_SetGrayscaleWhiteThresholds(short WhiteThresh);
  public native static short USB_IAC_GetGrayscaleBlackThresholds();
  public native static void USB_IAC_SetGrayscaleBlackThresholds(short BlackThresh);


//  This function retrieves the flag indicating whether the scanner is currently capturing images.
  public native static boolean USB_GetLiveMode();
  //  This function sets the flag telling the scanner to start or stop capturing images.
  public native static void USB_SetLiveMode(boolean LiveMode);

//  This function retrieves a full resolution image from the scanner.
  public native static byte[] USB_GetStillImage();
//  This function retrieves the dimensions of a full resolution image.
  public native static java.awt.Dimension USB_GetStillImageSize();

//  This function retrieves a less than full resolution image from the scanner.
  public native static byte[] USB_GetLiveImage();
  //  This function retrieves the dimensions of a live resolution image.
  public native static java.awt.Dimension USB_GetLiveImageSize();

  public native static byte[] USB_GetCroppedStillImage(short nLeft, short nTop, short nWidth, short nHeight);
  public native static java.awt.Dimension USB_GetCroppedStillImageSize();

  public native static byte[] USB_GetCroppedLiveImage(short nLeft, short nTop, short nWidth, short nHeight);
  public native static java.awt.Dimension USB_GetCroppedLiveImageSize();


// This function saves the image to disk.
  public native static void USB_SaveImage(String filename);

// This function returns the scanner's serial number.
  public native static String USB_GetScannerSerialNumber();

// This function reads and resets the scanner CONNECT_EVENT flag
  public native static boolean resetConnect();
// This function reads and resets the scanner DISCONNECT_EVENT flag
  public native static boolean resetDisconnect();
// This function reads and resets the scanner AUTOCAPTURE_EVENT flag
  public native static boolean resetAutoCapture();
// This function reads and resets the scanner FINGER_DETECT_EVENT flag
  public native static boolean resetFingerDetect();
// This function reads and resets the scanner LIVE_IMAGE_READY_EVENT flag
  public native static boolean resetLiveImageReady();


  public native static void USB_SetDetectFirstFinger(boolean Enable);

  public native static void USB_SetSlopesCompensation(boolean Enable);

  public native static void USB_SetFingerSensorMode(boolean EnableUp, boolean EnableDown);

  public native static int USB_GetFingerDetectStatus();

  public native static void USB_SetImageUniqueFrameMode(boolean bUniqueFrame);

//----------------------------------------------------------------------------
  public final static int CONNECT_EVENT = 1;
  public final static int DISCONNECT_EVENT = 2;
  public final static int AUTOCAPTURE_EVENT = 3;
  public final static int FINGER_DETECT_EVENT =4;
  public final static int LIVE_IMAGE_READY_EVENT =5;

  final static EventThread et = new EventThread();

  public static void addActionListener(ActionListener listener) {
    et.addActionListener(listener);
  }

  public static void removeActionListener(ActionListener listener) {
  }
}
