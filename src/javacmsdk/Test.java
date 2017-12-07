package javacmsdk;

import java.beans.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;

public class Test extends JFrame
{
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  JPanel Geometry = new JPanel();
  GridLayout gridLayout1 = new GridLayout(0, 1);
  JCheckBox invert = new JCheckBox();
  JCheckBox flipHorizontal = new JCheckBox();
  JCheckBox flipVertical = new JCheckBox();

  JPanel Rotation = new JPanel();
  GridLayout gridLayout2 = new GridLayout(0, 1);
  ButtonGroup vRotation = new ButtonGroup();
  JRadioButton Rot_0 = new JRadioButton();
  JRadioButton Rot_90 = new JRadioButton();
  JRadioButton Rot_180 = new JRadioButton();
  JRadioButton Rot_270 = new JRadioButton();

  JPanel Video = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JSlider vBrightness = new JSlider();
  JTextField Brightness = new JTextField();
  JLabel jLabelBrightness = new JLabel();
  JSlider vContrast = new JSlider();
  JTextField Contrast = new JTextField();
  JLabel jLabelContrast = new JLabel();
  JSlider vGain = new JSlider();
  JTextField Gain = new JTextField();
  JLabel jLabelGain = new JLabel();

  JPanel Buttons = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JButton saveImage = new JButton();
  JButton liveMode = new JButton();
  JButton saveSettings = new JButton();
  JButton vImage = new JButton();
  boolean bScannerIsConnected = false;
  boolean bUSB2_Scanner = false;
  byte cProductCode = 0x00;
  ActionListener jniAL;

  public static void main(String[] args) {
    new Test().show();
  }

  public Test() {
    super("jVerifier");
    jni.addActionListener(jniAL = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jVerifier_eventHandler(e);
      }
    });

    jbInit();

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        // Close USB SDK when application exits
        jni.USB_Uninitialize();
        jni.removeActionListener(jniAL);
        jni.USB_Dispose();
        System.exit(0);
      }
    });

    // must wait until scanner has connected to DLL
    if (!bScannerIsConnected) {
      try {
        this.thread.sleep(500);
      }
      catch (InterruptedException e) {}
    }
    try {
      // Initialize scanner on startup
      jni.USB_Initialize( (short) 0, (short) 0);
    }
    catch (Exception e) {
      System.out.print(e.getMessage());
      //
      // The USB_Initialize function will fail during the FIRST call, usually during
      // application startup, with a USB_STATUS of USB_INIT_FAIL_FINGER_PRESENT
      // if a finger is detected during the  first initalization.
      // This catch statement will display the error relating to this failure
      // as "#30 V300LC first init failed... remove finger and clean platen" to
      // the console output window. It is up to the developer to display an error
      // to the user to remove finger and clean platen. The developer must then call
      // USB_Initialize again
      //
    }
    cProductCode = jni.USB_GetProductCode();
    if (cProductCode > 0x2F) {
      bUSB2_Scanner = true;
      if (cProductCode == 0x30) {
        // this will enhance images for V300LC 2.0 scnners only and
        // may be removed if desired
        jni.USB_SetSlopesCompensation(true);
      }
    }
    else {
      bUSB2_Scanner = false;
    }

    invert.setSelected(jni.USB_GetInverse());
    flipHorizontal.setSelected(jni.USB_GetFlipHorizontal());
    flipVertical.setSelected(jni.USB_GetFlipVertical());

     int value = 0xFF & jni.USB_GetBrightness();
     vBrightness.setValue(0xFF & value);
     Brightness.setText(String.valueOf(value));

     value = jni.USB_GetContrast();
     vContrast.setValue(0xFF & value);
     Contrast.setText(String.valueOf(value));

     value= jni.USB_GetGain();
     vGain.setValue(0xFF & value);
     Gain.setText(String.valueOf(value));

    Dimension size = jni.USB_GetImageSize();
    imgWidth.setText(String.valueOf(size.width));
    imgHeight.setText(String.valueOf(size.height));
    setRotation(jni.USB_GetRotation());

    //Image Auto Capture on available on licensed V300 LC 2.0 scanners
    autoCapture.setSelected(jni.USB_IAC_GetAutoCapture());
    if (autoCapture.isSelected()) {
      size = jni.USB_IAC_GetImageSizeThresholds();
      widthT.setText(String.valueOf(size.width));
      heightT.setText(String.valueOf(size.height));
      hRidge.setText(String.valueOf(jni.USB_IAC_GetMinHorRidgeCount()));
      vRidge.setText(String.valueOf(jni.USB_IAC_GetMinVerRidgeCount()));
      white.setText(String.valueOf(jni.USB_IAC_GetGrayscaleWhiteThresholds()));
      black.setText(String.valueOf(jni.USB_IAC_GetGrayscaleBlackThresholds()));
    }
  }

  public void show() {
    super.pack();
    Dimension f = getSize();
    Dimension s = getToolkit().getScreenSize();
    setLocation( (s.width - f.width) / 2, (s.height - f.height) / 2);
    super.show();
  }

  private void jbInit() {
    this.getContentPane().setLayout(gridBagLayout1);
    Geometry.setBorder(BorderFactory.createLineBorder(Color.black));
    Geometry.setMinimumSize(new Dimension(110, 100));
    Geometry.setPreferredSize(new Dimension(110, 100));
    Geometry.setToolTipText("Geometry");
    Geometry.setLayout(gridLayout1);

    invert.setText("Invert");
    invert.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        invert_actionPerformed(e);
      }
    });

    flipHorizontal.setText("Flip Horizontal");
    flipHorizontal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        flipHorizontal_actionPerformed(e);
      }
    });

    flipVertical.setText("Flip Vertical");
    flipVertical.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        flipVertical_actionPerformed(e);
      }
    });

    Rotation.setBorder(BorderFactory.createLineBorder(Color.black));
    Rotation.setMinimumSize(new Dimension(50, 100));
    Rotation.setPreferredSize(new Dimension(50, 100));
    Rotation.setToolTipText("Rotation");
    Rotation.setLayout(gridLayout2);
    Rot_0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Rot_0_actionPerformed(e);
      }
    });
    Rot_90.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Rot_90_actionPerformed(e);
      }
    });
    Rot_180.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Rot_180_actionPerformed(e);
      }
    });
    Rot_270.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Rot_270_actionPerformed(e);
      }
    });

    initialize.setText("Initialize");
    initialize.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        initialize_actionPerformed(e);
      }
    });

    ImageSize.setBorder(BorderFactory.createLineBorder(Color.black));
    ImageSize.setMinimumSize(new Dimension(220, 60));
    ImageSize.setPreferredSize(new Dimension(220, 60));
    ImageSize.setToolTipText("Image Size");
    ImageSize.setLayout(gridBagLayout5);
    jLabelImgSize.setText("Image Size: ");
    imgWidth.setText("0");
    imgWidth.setColumns(3);
    imgWidth.setHorizontalAlignment(SwingConstants.RIGHT);
    imgHeight.setText("0");
    imgHeight.setColumns(3);
    imgHeight.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabelImgSizeX.setText("x");
    applySize.setMargin(new Insets(2, 2, 2, 2));
    applySize.setText("apply");
    applySize.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        applySize_actionPerformed(e);
      }
    });

    //Image Auto Capture on available on licensed V300 LC 2.0 scanners
    AutoCapturing.setBorder(BorderFactory.createLineBorder(Color.black));
    AutoCapturing.setMinimumSize(new Dimension(220, 100));
    AutoCapturing.setPreferredSize(new Dimension(220, 100));
    AutoCapturing.setToolTipText("Auto Capture");
    AutoCapturing.setLayout(gridBagLayout6);
    autoCapture.setText("Auto Capture");
    autoCapture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        autoCapture_actionPerformed(e);
      }
    });
    jLabelIAC_RidgeCnt.setText("Ridge Count : ");
    hRidge.setText("0");
    hRidge.setColumns(3);
    hRidge.setHorizontalAlignment(SwingConstants.RIGHT);
    jX2_Text.setText("x");
    vRidge.setText("0");
    vRidge.setColumns(3);
    vRidge.setHorizontalAlignment(SwingConstants.RIGHT);
    applyRidgeC.setMargin(new Insets(2, 2, 2, 2));
    applyRidgeC.setText("apply");
    applyRidgeC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        applyRidgeC_actionPerformed(e);
      }
    });
    jLabelIAC_GrayThreshold.setText("Gray Thresh : ");
    white.setText("0");
    white.setColumns(3);
    white.setHorizontalAlignment(SwingConstants.RIGHT);
    jX1_Text.setText("-");
    black.setBackground(Color.black);
    black.setForeground(Color.white);
    black.setCaretColor(Color.white);
    black.setText("0");
    black.setColumns(3);
    black.setHorizontalAlignment(SwingConstants.RIGHT);
    applyGreyT.setMargin(new Insets(2, 2, 2, 2));
    applyGreyT.setText("apply");
    applyGreyT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        applyGreyT_actionPerformed(e);
      }
    });
    jLabel1IAC_SizeThreshold.setText("Size Thresh :");
    widthT.setText("0");
    widthT.setColumns(3);
    widthT.setHorizontalAlignment(SwingConstants.RIGHT);
    jX3_Text.setText("x");
    heightT.setText("0");
    heightT.setColumns(3);
    heightT.setHorizontalAlignment(SwingConstants.RIGHT);
    applySizeT.setMargin(new Insets(2, 2, 2, 2));
    applySizeT.setText("apply");
    applySizeT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        applySizeT_actionPerformed(e);
      }
    });


    vRotation.add(Rot_0);
    vRotation.add(Rot_90);
    vRotation.add(Rot_180);
    vRotation.add(Rot_270);
    Rot_0.setText("0");
    Rot_90.setText("90");
    Rot_180.setText("180");
    Rot_270.setText("270");

    Video.setBorder(BorderFactory.createLineBorder(Color.black));
    Video.setMinimumSize(new Dimension(220, 80));
    Video.setPreferredSize(new Dimension(220, 80));
    Video.setToolTipText("Video");
    Video.setLayout(gridBagLayout3);

    Brightness.setEditable(false);
    Brightness.setText("0");
    Brightness.setColumns(3);
    vBrightness.setMaximum(255);
    vBrightness.setMinimumSize(new Dimension(80, 24));
    vBrightness.setPreferredSize(new Dimension(80, 24));
    vBrightness.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        vBrightness_propertyChange(e);
      }
    });
    vBrightness.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        vBrightness_stateChanged(e);
      }
    });
    jLabelBrightness.setText("Brightness");

    Contrast.setEditable(false);
    Contrast.setText("0");
    Contrast.setColumns(3);
    vContrast.setMaximum(255);
    vContrast.setMinimumSize(new Dimension(80, 24));
    vContrast.setPreferredSize(new Dimension(80, 24));
    vContrast.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        vContrast_propertyChange(e);
      }
    });
    vContrast.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        vContrast_stateChanged(e);
      }
    });
    jLabelContrast.setText("Contrast");

    Gain.setEditable(false);
    Gain.setText("0");
    Gain.setColumns(3);
    vGain.setMaximum(255);
    vGain.setMinimumSize(new Dimension(80, 24));
    vGain.setPreferredSize(new Dimension(80, 24));
    vGain.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        vGain_propertyChange(e);
      }
    });
    vGain.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        vGain_stateChanged(e);
      }
    });
    jLabelGain.setText("Gain");

    Buttons.setBorder(BorderFactory.createLineBorder(Color.black));
    Buttons.setMinimumSize(new Dimension(220, 60));
    Buttons.setPreferredSize(new Dimension(220, 60));
    Buttons.setLayout(gridBagLayout4);
    saveImage.setText("Save Image");
    saveImage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveImage_actionPerformed(e);
      }
    });

    liveMode.setText("Go Live");
    liveMode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        liveMode_actionPerformed(e);
      }
    });

    saveSettings.setText("Save Settings");
    saveSettings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveSettings_actionPerformed(e);
      }
    });

    vImage.setMaximumSize(new Dimension(640, 480));
    vImage.setMinimumSize(new Dimension(320, 240));
    vImage.setPreferredSize(new Dimension(640, 480));

    this.getContentPane().add(Geometry
                              , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.NONE
                                                       , new Insets(5, 5, 5, 5), 0, 0));
    Geometry.add(invert, null);
    Geometry.add(flipHorizontal, null);
    Geometry.add(flipVertical, null);

    this.getContentPane().add(Rotation
                              , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.NONE
                                                       , new Insets(5, 5, 5, 5), 0, 0));
    Rotation.add(Rot_0, null);
    Rotation.add(Rot_90, null);
    Rotation.add(Rot_180, null);
    Rotation.add(Rot_270, null);

    this.getContentPane().add(Video
                              , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.NONE
                                                       , new Insets(5, 5, 5, 5), 0, 0));
    Video.add(vBrightness
              , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));
    Video.add(Brightness
              , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));
    Video.add(jLabelBrightness
              , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.WEST
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));

    Video.add(vContrast
              , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       ,new Insets(0, 0, 0, 0), 0, 0));
    Video.add(Contrast
              , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));
    Video.add(jLabelContrast
              , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.WEST
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));

    Video.add(vGain
              , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));
    Video.add(Gain
              , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));
    Video.add(jLabelGain
              , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.WEST
                                       , GridBagConstraints.NONE
                                       , new Insets(0, 0, 0, 0), 0, 0));

    this.getContentPane().add(Buttons
                              , new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.NONE
                                                       , new Insets(0, 0, 0, 0), 0, 0));
    Buttons.add(liveMode
                , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.CENTER
                                         , GridBagConstraints.BOTH
                                         , new Insets(0, 0, 0, 0), 0, 0));
    Buttons.add(saveImage
                , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.CENTER
                                         , GridBagConstraints.BOTH
                                         , new Insets(0, 0, 0, 0), 0, 0));
    Buttons.add(saveSettings
                , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.CENTER
                                         , GridBagConstraints.BOTH
                                         , new Insets(0, 0, 0, 0), 0, 0));
    Buttons.add(initialize
                , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.CENTER
                                         , GridBagConstraints.NONE
                                         , new Insets(0, 0, 0, 0), 0, 0));

    this.getContentPane().add(vImage
                              , new GridBagConstraints(2, 0, 1, 7, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.BOTH
                                                       , new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(ImageSize
                              , new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
                              , GridBagConstraints.CENTER
                              , GridBagConstraints.NONE
                              , new Insets(0, 0, 0, 0), 0, 0));
    ImageSize.add(jLabelImgSize
                  , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.EAST
                                           , GridBagConstraints.NONE
                                           , new Insets(5, 5, 5, 5), 0, 0));
    ImageSize.add(imgWidth
                  , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER
                                           , GridBagConstraints.NONE
                                           , new Insets(0, 0, 0, 0), 0, 0));
    ImageSize.add(imgHeight
                  , new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER
                                           , GridBagConstraints.NONE
                                           , new Insets(0, 0, 0, 0), 0, 0));
    ImageSize.add(jLabelImgSizeX
                  , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER
                                           , GridBagConstraints.NONE
                                           , new Insets(3, 3, 3, 3), 0, 0));
    ImageSize.add(applySize
                  , new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER
                                           , GridBagConstraints.NONE
                                           , new Insets(5, 5, 5, 5), 0, 0));


    //Image Auto Capture on available on licensed V300 LC 2.0 scanners
    this.getContentPane().add(AutoCapturing
                              , new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0
                                                       , GridBagConstraints.CENTER
                                                       , GridBagConstraints.NONE
                                                       , new Insets(0, 0, 0, 0), 0, 28));
    AutoCapturing.add(autoCapture
                      , new GridBagConstraints(0, 0, 5, 1, 1.0, 1.0
                                               , GridBagConstraints.NORTHWEST
                                               , GridBagConstraints.NONE
                                              , new Insets(13, 5, 5, 5), 0, 0));
    AutoCapturing.add(jLabelIAC_RidgeCnt
                      , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(5, 5, 0, 5), 0, 0));
    AutoCapturing.add(hRidge
                      , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(jX2_Text
                      , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(3, 3, 3, 3), 0, 0));
    AutoCapturing.add(vRidge
                      , new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(applyRidgeC
                      , new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(jLabelIAC_GrayThreshold
                      , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(white
                      , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(jX1_Text
                      , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(3, 3, 3, 3), 0, 0));
    AutoCapturing.add(black
                      , new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(applyGreyT
                      , new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(jLabel1IAC_SizeThreshold
                      , new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(5, 5, 5, 5), 0, 0));
    AutoCapturing.add(widthT
                      , new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(jX3_Text
                      , new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(3, 3, 3, 3), 0, 0));
    AutoCapturing.add(heightT
                      , new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
    AutoCapturing.add(applySizeT
                      , new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER
                                               , GridBagConstraints.NONE
                                               , new Insets(0, 0, 0, 0), 0, 0));
  }

  JFileChooser fileDlg = new JFileChooser();
  JButton initialize = new JButton();
  JPanel ImageSize = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  JLabel jLabelImgSize = new JLabel();
  JTextField imgWidth = new JTextField();
  JTextField imgHeight = new JTextField();
  JLabel jLabelImgSizeX = new JLabel();
  JButton applySize = new JButton();
  JPanel AutoCapturing = new JPanel();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  JCheckBox autoCapture = new JCheckBox();
  JLabel jLabelIAC_RidgeCnt = new JLabel();
  JTextField hRidge = new JTextField();
  JLabel jX2_Text = new JLabel();
  JTextField vRidge = new JTextField();
  JButton applyRidgeC = new JButton();
  JLabel jLabelIAC_GrayThreshold = new JLabel();
  JTextField white = new JTextField();
  JLabel jX1_Text = new JLabel();
  JTextField black = new JTextField();
  JButton applyGreyT = new JButton();
  JLabel jLabel1IAC_SizeThreshold = new JLabel();
  JTextField widthT = new JTextField();
  JLabel jX3_Text = new JLabel();
  JTextField heightT = new JTextField();
  JButton applySizeT = new JButton();


  ImageThread thread = null;
  void liveMode_actionPerformed(ActionEvent e) {
    boolean live = !jni.USB_GetLiveMode();
    jni.USB_SetLiveMode(live);
    if (live) thread = new ImageThread();
    else thread.interrupt();
    if (live) liveMode.setText("Grab");
    else liveMode.setText("Go Live");
  }

  void saveImage_actionPerformed(ActionEvent e) {
    int ret = fileDlg.showSaveDialog(null);
    if (ret == JFileChooser.CANCEL_OPTION) return;
    java.io.File file = fileDlg.getSelectedFile();
    jni.USB_SaveImage(file.getAbsolutePath());
  }

  void saveSettings_actionPerformed(ActionEvent e)
  {
    jni.USB_SaveSettings();
  }

  // NOTE: Do not try to set brightness, contrast or gain on the V310 or V310LC
  // V310 and V310LC will return erro #35 - USB_NOT_SUPPORTED
  void vBrightness_propertyChange(PropertyChangeEvent e)
  {
    vBrightness_stateChanged(null);
  }

  void vBrightness_stateChanged(ChangeEvent e)
  {
      if(cProductCode != 0x31 && cProductCode != 0x34)
      {
          jni.USB_SetBrightness( (byte) vBrightness.getValue());
          int value = 0xFF & jni.USB_GetBrightness();
          Brightness.setText(String.valueOf(value));
      }
  }

  // NOTE: Do not try to set brightness, contrast or gain on the V310 or V310LC
  // V310 and V310LC will return erro #35 - USB_NOT_SUPPORTED
  void vContrast_propertyChange(PropertyChangeEvent e)
  {
    vContrast_stateChanged(null);
  }

  void vContrast_stateChanged(ChangeEvent e)
  {
      if(cProductCode != 0x31 && cProductCode != 0x34)
      {
          jni.USB_SetContrast( (byte) vContrast.getValue());
          int value = 0xFF & jni.USB_GetContrast();
          Contrast.setText(String.valueOf(value));
      }
  }

  // NOTE: Do not try to set brightness, contrast or gain on the V310 or V310LC
  // V310 and V310LC will return erro #35 - USB_NOT_SUPPORTED
  void vGain_propertyChange(PropertyChangeEvent e)
  {
    vGain_stateChanged(null);
  }

  void vGain_stateChanged(ChangeEvent e)
  {
      if(cProductCode != 0x31 && cProductCode != 0x34)
      {
          jni.USB_SetGain( (byte) vGain.getValue());
          int value = 0xFF & jni.USB_GetGain();
          Gain.setText(String.valueOf(value));
      }
  }

  void invert_actionPerformed(ActionEvent e)
  {
    JCheckBox c = (JCheckBox)e.getSource();
    jni.USB_SetInverse(c.isSelected());
  }

  void flipHorizontal_actionPerformed(ActionEvent e)
  {
    JCheckBox c = (JCheckBox)e.getSource();
    jni.USB_SetFlipHorizontal(c.isSelected());
  }

  void flipVertical_actionPerformed(ActionEvent e)
  {
    JCheckBox c = (JCheckBox)e.getSource();
    jni.USB_SetFlipVertical(c.isSelected());
  }

  void Rot_0_actionPerformed(ActionEvent e)
  {
    jni.USB_SetRotation(jni.USB_ROTATION_0);
  }

  void Rot_90_actionPerformed(ActionEvent e)
  {
    jni.USB_SetRotation(jni.USB_ROTATION_90);
  }

  void Rot_180_actionPerformed(ActionEvent e)
  {
    jni.USB_SetRotation(jni.USB_ROTATION_180);
  }

  void Rot_270_actionPerformed(ActionEvent e)
  {
    jni.USB_SetRotation(jni.USB_ROTATION_270);
  }

  void initialize_actionPerformed(ActionEvent e)
  {
    jni.USB_Initialize((short)0,(short)0);
  }

  void applySize_actionPerformed(ActionEvent e)
  {
    boolean live = jni.USB_GetLiveMode();
    short width = Short.parseShort(imgWidth.getText());
    short height = Short.parseShort(imgHeight.getText());
    if ( live )
    {
      width = (short)(width-(width%8));
      imgWidth.setText(String.valueOf(width));
      height = (short)(height-(height%8));
      imgHeight.setText(String.valueOf(height));
    }
    else
    {
      width = (short)(width-(width%4));
      imgWidth.setText(String.valueOf(width));
      height = (short)(height-(height%4));
      imgHeight.setText(String.valueOf(height));
    }
    jni.USB_SetImageSize(width,height);
  }


  //Image Auto Capture on available on licensed V300 LC 2.0 scanners
  void autoCapture_actionPerformed(ActionEvent e)
  {
    jni.USB_IAC_SetAutoCapture(autoCapture.isSelected());
  }

  void applyRidgeC_actionPerformed(ActionEvent e)
  {
    jni.USB_IAC_SetMinHorRidgeCount(Short.parseShort(hRidge.getText()));
    jni.USB_IAC_SetMinVerRidgeCount(Short.parseShort(vRidge.getText()));
  }

  void applyGreyT_actionPerformed(ActionEvent e)
  {
    jni.USB_IAC_SetGrayscaleWhiteThresholds(Short.parseShort(white.getText()));
    jni.USB_IAC_SetGrayscaleBlackThresholds(Short.parseShort(black.getText()));
  }

  void applySizeT_actionPerformed(ActionEvent e)
  {
    short w  = Short.parseShort(widthT.getText());
    short h  = Short.parseShort(heightT.getText());
    jni.USB_IAC_SetImageSizeThresholds(w,h);
  }



  /**
   * jVerifier_eventHandler
   *
   * @param e ActionEvent
   */
  public void jVerifier_eventHandler(ActionEvent e)
  {
      switch (e.getID())
      {
          case jni.CONNECT_EVENT:
             boolean bConnected = true;
             System.out.print("Connected\n");	//DEBUG
             /* TODO: Process connection of new device
              * scannerConnected();
              */
             bScannerIsConnected = true;
             break;
          case jni.DISCONNECT_EVENT:
              boolean bDisConnected = false;
              System.out.print("Disconnected\n");	//DEBUG
              /* TODO: Process disconnection of new device
               * jni.USB_Uninitialize( (short) 0, (short) 0);
               * Disable buttons and reset gui
               */
              bScannerIsConnected = false;
              break;
          case jni.AUTOCAPTURE_EVENT:
              boolean bAutoCapture = false;
              System.out.print("AutoCaptured\n");	//DEBUG
              /* TODO: Process auto capture compete
               * saveImage_actionPerformed(e);
               */
              break;
          case jni.FINGER_DETECT_EVENT:
              boolean bFingerDetect = false;
              System.out.print("FingerDetect\n");	//DEBUG
              /* TODO: Process finger detect change event
               */
              break;
          case jni.LIVE_IMAGE_READY_EVENT:
              boolean bImageReady = false;
              System.out.print("ImageReady\n");	//DEBUG
              /* TODO: Process next image
               * byte[] pixels = jni.USB_GetCroppedStillImage(0,0,640,480);
               * size = jni.USB_GetCroppedStillImageSive();
               * image = createImage(pixels, size.width, size.height);
               */
              break;
      }
  }

  //----------------------------------------------------------------------------
  private void setRotation(int rotation)
  {
    switch ( rotation )
    {
      case jni.USB_ROTATION_0: Rot_0.setSelected(true); break;
      case jni.USB_ROTATION_90: Rot_90.setSelected(true); break;
      case jni.USB_ROTATION_180: Rot_180.setSelected(true); break;
      case jni.USB_ROTATION_270: Rot_270.setSelected(true); break;
    }
  }

  public final static Image createImage(byte[] pixels, int width, int height)
  {
    int[] iPixels = new int[width*height];
    int[] jPixels = new int[width*height];
    int iOffset;
    int jOffset;
    for (int i = 0; i < iPixels.length; i++)
    {
        iPixels[i] = (0xFF000000|((pixels[i]&0xFF)<<16|(pixels[i]&0xFF)<<8|(pixels[i]&0xFF)));
    }
    for (int i=0; i<height; ++i)
    {
      for (int j=0; j<width; j++)
      {
        iOffset = i*width + j;
        jOffset = (((height - 1) - i)*width) + j;
        jPixels[jOffset] = iPixels[iOffset];
      }
    }
    Toolkit tk = Toolkit.getDefaultToolkit();
    return tk.createImage(new MemoryImageSource(width,height,jPixels,0,width));
  }

    /**
     * scannerConnected
     */
    private void scannerConnected()
    {
        try
        {
            jni.USB_Initialize( (short) 0, (short) 0);
        }
        catch(Exception e)
        {
           System.out.print(e.getMessage());
           //
           // The USB_Initialize function will fail during the FIRST call, usually during
           // application startup, with a USB_STATUS of USB_INIT_FAIL_FINGER_PRESENT
           // if a finger is detected during the  first initalization.
           // This catch statement will display the error relating to this failure
           // as "#30 V300LC first init failed... remove finger and clean platen" to
           // the console output window. It is up to the developer to display an error
           // to the user to remove finger and clean platen. The developer must then call
           // USB_Initialize again
           //
        }

        try
        {
            invert.setSelected(jni.USB_GetInverse());
            flipHorizontal.setSelected(jni.USB_GetFlipHorizontal());
            flipVertical.setSelected(jni.USB_GetFlipVertical());

            int value = 0xFF & jni.USB_GetBrightness();
            vBrightness.setValue(0xFF & value);
            Brightness.setText(String.valueOf(value));

            value = jni.USB_GetContrast();
            vContrast.setValue(0xFF & value);
            Contrast.setText(String.valueOf(value));

            value= jni.USB_GetGain();
            vGain.setValue(0xFF & value);
            Gain.setText(String.valueOf(value));

            Dimension size = jni.USB_GetImageSize();
            imgWidth.setText(String.valueOf(size.width));
            imgHeight.setText(String.valueOf(size.height));
            setRotation(jni.USB_GetRotation());

            //Image Auto Capture on available on licensed V300 LC 2.0 scanners
            autoCapture.setEnabled(false);
            autoCapture.setSelected(jni.USB_IAC_GetAutoCapture());
            if (autoCapture.isSelected())
            {
                size = jni.USB_IAC_GetImageSizeThresholds();
                widthT.setText(String.valueOf(size.width));
                heightT.setText(String.valueOf(size.height));
                hRidge.setText(String.valueOf(jni.USB_IAC_GetMinHorRidgeCount()));
                vRidge.setText(String.valueOf(jni.USB_IAC_GetMinVerRidgeCount()));
                white.setText(String.valueOf(jni.USB_IAC_GetGrayscaleWhiteThresholds()));
                black.setText(String.valueOf(jni.USB_IAC_GetGrayscaleBlackThresholds()));
            }
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
            //
            // The USB_Initialize function will fail during the FIRST call, usually during
            // application startup, with a USB_STATUS of USB_INIT_FAIL_FINGER_PRESENT
            // if a finger is detected during the  first initalization.
            // This catch statement will display the error relating to this failure
            // as "#30 V300LC first init failed... remove finger and clean platen" to
            // the console output window. It is up to the developer to display an error
            // to the user to remove finger and clean platen. The developer must then call
            // USB_Initialize again
            //
        }

    }

////------------------------------------------------------------------------------
  class ImageThread extends Thread
  {
    public ImageThread()
    {
      this.start();
    }

    public void run()
    {
      Image image = null;
      Dimension size = null;

      // GetImageSize return size for Still image
      while ( !isInterrupted() && jni.USB_GetLiveMode() )
      {
        if (bUSB2_Scanner)
        {
          //byte[] pixelsLive = jni.USB_GetLiveImage();
          //size = jni.USB_GetLiveImageSize();
          //image = createImage(pixelsLive, size.width, size.height);
          // Only get still for USB 2.0 scanners
          // Live image size varies
          byte[] pixels = jni.USB_GetStillImage();
          size = jni.USB_GetStillImageSize();
          image = createImage(pixels, size.width, size.height);
        }
        else
        {
          // Assume Live images are decimated on non USB 2.0 scanners
          byte[] pixels = jni.USB_GetLiveImage();
          size = jni.USB_GetLiveImageSize();
          image = createImage(pixels, size.width, size.height);
        }

        vImage.setIcon(new ImageIcon(image));
      }

    // Get final image
    byte[] pixels = jni.USB_GetStillImage();
    size = jni.USB_GetStillImageSize();
    image = createImage(pixels,size.width,size.height);

    vImage.setIcon(new ImageIcon(image));
    }
  }
}
