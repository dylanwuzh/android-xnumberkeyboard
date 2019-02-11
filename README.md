# android\-xnumberkeyboard

[![](https://www.jitpack.io/v/wuzhendev/android-xnumberkeyboard.svg)](https://www.jitpack.io/#wuzhendev/android-xnumberkeyboard)

自定义的数字键盘，支持纯数字键盘、带小数点的数字键盘、输入身份证的数字键盘。

效果如下图：

**纯数字键盘：**

![效果图][3]

**带小数点的数字键盘：**

![效果图][4]

**身份证键盘：**

![效果图][5]

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

## Methods

**1\. 设置键盘的类型：**

```java
/**
 * @param type
 *         KeyboardType.number: 纯数字键盘
 *         KeyboardType.digit: 带小数点的键盘
 *         KeyboardType.idCard: 输入身份证的数字键盘
 */
XNumberKeyboardView.setKeyboardType(int type);
```

**2\. 打乱键盘顺序：**

```java
/**
 * 随机打乱数字键盘上键位的排列顺序，带有动画。
 */
public void shuffleKeyboard() { }

/**
 * 随机打乱数字键盘上键位的排列顺序。
 *
 * @param anim 是否带有动画
 */
public void shuffleKeyboard(boolean anim) { }
```

## Sample

[Sample sources][1]

[Sample APK][2]

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

[1]: ./samples
[2]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/XNumberKeyboard_Demo_v2.0.0.apk
[3]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/2.jpg
[4]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/3.jpg
[5]: https://raw.githubusercontent.com/wuzhendev/assets/master/xnumberkeyboardview/4.jpg
