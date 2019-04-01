# Mobile Drum Machine
An Android drum machine include a live mode and an AR mode. Final project for EC500 Software Engineering.
## Description
A mobile drum machine with a basic live mode and an AR mode. Play with drums at any time and be a rockstar! All the sampled drum sounds are from [Ableton Live 10 Lite](https://www.ableton.com/en/products/live-lite/). 
### Live Mode
Live mode provides 12 drum pads with 12 different drum sounds.   
   
![](https://github.com/ZeyuKeithFu/Drum500/blob/master/assets/live_mode.png)   
   
Some useful functions are included:
* **Node repeat**   
In a real live, you may want a fixed drum sequence where drums are repeating in different speeds or frequencies. A repeat frequency ranged from single to quartic in a single tempo can be selected, and BPM can be ranged from 40 to 160.
* **Force sensitive drum**   
You may expect different responces from the drum when you hit a real drum with different forces. And just like real drum, the drum pads in live mode are also force sensitive. The volumn of drum sounds varies corresponding to how hard you hit them.
* **Record and save**   
Sometimes your inspirations just come out with no advance and you don't want to miss them. Record your genius by click the record button and save them to local. You can review them so that you won't forget.   
   
   
### AR Mode
With AR mode, you can take the drum machine anywhere with a platform (a table, ground, wall etc,.) where drum pads will attach once platform is detected, and then you play it with your hands. Here is a reference [demo effect](https://www.youtube.com/watch?v=Zas5JCjQb40&feature=youtu.be). Functions still on building.   

![](https://github.com/ZeyuKeithFu/Drum500/blob/master/assets/AR_mode.png)

* Device must support ```ARCore by Google``` for using AR mode, here is a list of [avaliable devices](https://developers.google.com/ar/discover/supported-devices).   
   
## User Story
* As a ```DJ/drummer```, I need a more portable device to perform live drum and no device is more portable than your on mobile phone.
* As a ```music producer```, I need a software to record my inspirations so that they don't slip away.
* As a ```music lover```, I want to play with drums without buying an expensive physical drum machine.
   
## Technology Selection
* **IDE** : Android Studio
* **Lowest Android Version** : ```7.0``` (For AR capability)
* **APIs** : ARCore, Unity for Android, MediaPlayer, MediaRecorder
