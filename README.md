SnackValues
==================
1. This is a Snack nutritional value prediction Application built on caffe-android-demo. It can predict snacks on the mobile phone. The application is designed based on Material Design Principle.

2. The CNN(convolutional neural networks) model is trained by Caffe with 94.7% accuracy and it can classify 15 kinds of snacks.

3. To run the application, you need to have the trained snack model, the mean file and the deploy file, which you can download it [Here](https://drive.google.com/open?id=0B8Z5vuz1VS_TWHpoRU4xZnhLT2c) (Just downliad the whole directory and then push to your mobile phone follow the following instructions.)

4. A simple NodeJs server is included in this project, it can receive images sent from the application.

5. Contact me if you want to know more details.
   Email: 39872463@qq.com


### Some useful Directory:
NodeServer Dictory: A simple server implemented by NodeJs, which can receive images and labels from mobile phone.

test_images Dictory: some images which can be used to test the application

## Quick Look:

Main Page 1:

<img src="https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-29-22-50-05.png?raw=true" width = "200" height = "300" alt="图片名称" align=center />


Main Page 2: After click Camera icon

<img src="https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-29-22-50-22.png?raw=true" width = "200" height = "300" alt="图片名称" align=center />


Main Page 3: After click FAB

<img src="https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-29-22-50-11.png?raw=true" width = "200" height = "300" alt="图片名称" align=center />




Result Page:

<img src="https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-29-22-51-24.png?raw=true " width = "200" height = "300" alt="图片名称" align=center />

Correct Page:

<img src="https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-29-22-51-34.png?raw=true" width = "200" height = "300" alt="图片名称" align=center />

## Quick Start

### Basic

If you want to have a try on this app, please follow the steps below to get the required stuff:

```shell

# 1. push things to your device
adb shell mkdir -p /sdcard/caffe_mobile/
adb push new_snack_model /sdcard/caffe_mobile/new_snack_model/
```
If the app crashes, first make sure it is not an out of memory issue - modify deploy.prototxt such that the mini batch size is 1 instead of 10
