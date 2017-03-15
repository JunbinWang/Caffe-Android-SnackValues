SnackValues
==================
This is an Snack nutritional value prediction Applicaton built base on caffe-android-demo.

NodeServer Dictory: A simple server implemented by NodeJs, which can receive images and labels from mobile phone.

test_images Dictory: some images which can be used to test the application

An android caffe demo app exploiting caffe pre-trained 
ImageNet model for image classification

To run the application, you need to have the trained snack model and the deploy file, which you can download it [Here](https://drive.google.com/open?id=0B8Z5vuz1VS_TWHpoRU4xZnhLT2c)
## Quick Look:

Main Page:

![GitHub](https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-15-14-20-21.png?raw=true "GitHub,Social Coding")

Result Page:

![GitHub](https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-15-14-20-32.png?raw=true "GitHub,Social Coding")

Correct Page:

![GitHub](https://github.com/JunbinWang/SnackValues/blob/master/Other%20Stuff/Original%20UI/Screenshot_2017-03-15-14-21-01.png?raw=true "GitHub,Social Coding")

## Quick Start

### Basic

If you want to have a try on this app, please follow the steps below to get the required stuff:

```shell

# 1. push things to your device
adb shell mkdir -p /sdcard/caffe_mobile/
adb push new_snack_model /sdcard/caffe_mobile/new_snack_model/
```
If the app crashes, first make sure it is not an out of memory issue - modify deploy.prototxt such that the mini batch size is 1 instead of 10
