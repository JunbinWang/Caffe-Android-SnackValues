SnackValues
==================
This is an Snack nutritional value prediction Applicaton built base on caffe-android-demo.

The model is stored on : model directory, it can classify 15 type of snacks currently.
Some test images are stored on test directory

An android caffe demo app exploiting caffe pre-trained ImageNet model for image classification

## Quick Start

### Basic

If you want to have a try on this app, please follow the steps below to get the required stuff:
```shell

# 1. push things to your device
adb shell mkdir -p /sdcard/caffe_mobile/
adb push new_snack_model /sdcard/caffe_mobile/new_snack_model/
```
If the app crashes, first make sure it is not an out of memory issue - modify deploy.prototxt such that the mini batch size is 1 instead of 10
