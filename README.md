# drumstAR🌟
[![Build Status](https://travis-ci.org/ZeyuKeithFu/drumstAR.svg?branch=master)](https://travis-ci.org/ZeyuKeithFu/drumstAR)
   
## Description
A mobile drum machine with a live mode and an AR mode. Basic drum machine functions of node repeat, sensitive drum pad and recording are also included. Play with drums at any time and be a rockstar! All the sampled drum sounds are from [Ableton Live 10 Lite](https://www.ableton.com/en/products/live-lite/). Final project for EC500 Software Engineering.
### Live Mode
Live mode provides 12 drum pads with 12 different drum sounds.   
   
![](https://github.com/ZeyuKeithFu/drumstAR/blob/master/assets/livemode.gif)   
   
Some useful functions are included:
* **Node repeat**   
In a real live, you may want a fixed drum sequence where drums are repeating in different speeds or frequencies. A repeat frequency ranged from single to quartic in a single tempo can be selected, and BPM can be ranged from 40 to 160.
* **Force sensitive drum**   
You may expect different responces from the drum when you hit a real drum with different forces. And just like real drum, the drum pads in live mode are also force sensitive. The volumn of drum sounds varies corresponding to how hard you hit them.
* **Record and save**   
Sometimes your inspirations just come out with no advance and you don't want to miss them. Record your genius by click the record button and save them to local. You can review them so that you won't forget. The following image shows the recording activity where you can view and play your recorded audio clips.   
   
![](https://github.com/ZeyuKeithFu/drumstAR/blob/master/assets/record.png)
     
     
   
### AR Mode
AR mode is built in [Unity](https://unity.com/), work with [Vuforia](https://developer.vuforia.com/) AR engine. Once the [image](https://github.com/ZeyuKeithFu/drumstAR/blob/master/assets/drum1.jpg) of drum in this application is detected, you will have your drum kit all set. The single piece of drum image, which is absolutely light and portable, will be your new drum machine and you can take it anywhere. **THE IMAGE IS A DRUM MACHINE!!!**    
    
***An instruction page will help with your AR experience!***   
   
![](https://github.com/ZeyuKeithFu/drumstAR/blob/master/assets/instruction.png)   
   
***And there you go!***   
   
![](https://github.com/ZeyuKeithFu/drumstAR/blob/master/assets/ARmode.png)   

* Drum pads are built with ```virtual buttons```. To play the drum, simply by cover the virtual button region (just like you push a real button) and the corresponding drum sound will be played.   
* For the reason of size and scale, only three drum pads (hihat, snare and kick) are add to AR activity. For the Unity development part, see [UnityDev](https://github.com/ZeyuKeithFu/drumstAR/tree/UnityDev) branch. The further step of developing AR activity could be working on:
   + Adding more drum pads to the scene.
   + Adding animation on different part of the drum kit when hitting a drum pad.
   + ...
 
   
## User Story
* As a ```DJ/drummer```, I need a more portable device to perform live drum and no device is more portable than your own mobile phone.
* As a ```music producer```, I need a software to record my inspirations so that they don't slip away.
* As a ```music lover```, I want to play with drums without buying an expensive physical drum machine.
   
## Technology Selection
* **IDE** : ```Android Studio```(Android activities), ```Unity 3D```(3D AR model), ```Visual Studio```(C#)
* **Supported devices** : Android devices with lowest *API level 26* (```Android 8.0.0 Oreo```)
* **APIs** : Unity for Android, Vuforia for Android, MediaPlayer, MediaRecorder
