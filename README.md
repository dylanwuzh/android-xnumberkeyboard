# android\-xnumberkeyboard

自定义的数字键盘，效果如下图：

![效果图][1]

## Gradle

[![](https://www.jitpack.io/v/wuzhendev/android-xnumberkeyboard.svg)](https://www.jitpack.io/#wuzhendev/android-xnumberkeyboard)

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
<!-- 删除按键的图标 -->
<attr name="xnkv_deleteDrawable" format="reference" />

<!-- 删除按键图标的宽度 -->
<attr name="xnkv_deleteWidth" format="dimension|reference" />

<!-- 删除按键图标的高度 -->
<attr name="xnkv_deleteHeight" format="dimension|reference" />

<!-- 删除按键图标的颜色 -->
<attr name="xnkv_deleteBackgroundColor" format="color|reference" />
```

## Sample

[Sample sources][2]

[Sample APK][3]

## Futures

1. 可以设置键盘的大小。
2. Shuffle 带有动画效果。

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

[1]: ./assets/1.jpg
[2]: ./samples
[3]: ./assets/XNumberKeyboard_Demo_v1.0.1.apk
