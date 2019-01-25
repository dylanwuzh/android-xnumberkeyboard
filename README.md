# android\-xnumberkeyboard

[![](https://www.jitpack.io/v/wuzhendev/android-xnumberkeyboard.svg)](https://www.jitpack.io/#wuzhendev/android-xnumberkeyboard)

自定义的数字键盘，效果如下图：

![效果图][1]

## Gradle

``` groovy
repositories {
    maven {
        url "https://www.jitpack.io"
    }
}

dependencies {
    compile 'com.github.wuzhendev:android-xnumberkeyboard:x.y.z'
}
```
 
## Attrs

``` xml
<!--左下角按键的背景色-->
<attr name="xnkv_blKeyBackground" format="color|reference"/>

<!--右下角按键的图标-->
<attr name="xnkv_brKeyDrawable" format="reference"/>

<!--右下角按键的背景-->
<attr name="xnkv_brKeyBackground" format="color|reference"/>

<!--右下角按键图标的宽度-->
<attr name="xnkv_brKeyDrawableWidth" format="dimension"/>

<!--右下角按键图标的高度-->
<attr name="xnkv_brKeyDrawableHeight" format="dimension"/>

<attr name="android:keyTextSize"/>

<attr name="android:keyTextColor"/>

<!--键盘的类型-->
<attr name="xnkv_type">
    <!--纯数字键盘-->
    <enum name="number" value="1"/>

    <!--带小数点的数字键盘-->
    <enum name="digit" value="2"/>

    <!--输入身份证的数字键盘-->
    <enum name="idcard" value="3"/>
</attr>
```

## Sample

[Sample sources][2]

[Sample APK][3]

## Futures

1. 可以设置键盘的大小。

## License

```
Copyright 2016 wuzhen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/1.jpg
[2]: ./samples
[3]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/XNumberKeyboard_Demo_v2.0.0.apk
