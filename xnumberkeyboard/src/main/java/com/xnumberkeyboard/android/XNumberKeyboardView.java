package com.xnumberkeyboard.android;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数字键盘，只显示数字。
 *
 * @author Dylan Wu
 * @since 2019-01-25
 */
public class XNumberKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    /**
     * 键盘左下角按键的Keycode。
     */
    public static final int KEYCODE_BOTTOM_LEFT = -11;

    /**
     * 键盘右下角按键的Keycode。
     */
    public static final int KEYCODE_BOTTOM_RIGHT = -12;

    /**
     * 打乱按键动画的步进，控制打乱的动画多久刷新执行。
     */
    private static final int STEP_SHUFFLE_ANIM = 25;

    /**
     * 0-9 数字的 Character 值。
     */
    private final List<Character> keyCodes = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    /**
     * 键盘文字的大小。
     */
    private int mKeyTextSize;
    /**
     * 键盘文字的颜色。
     */
    private int mKeyTextColor;

    /**
     * 右下角按键图标的绘制区域。
     */
    private Rect mBRKeyDrawRect;
    /**
     * 右下角按键的图标。
     */
    private Drawable mBRKeyDrawable;
    /**
     * 右下角按键图标的宽度。
     */
    private int mBRKeyDrawableWidth;
    /**
     * 右下角按键图标的高度。
     */
    private int mBRKeyDrawableHeight;
    /**
     * 右下角按键的背景。
     */
    private Drawable mBRKeyBackground;

    /**
     * 左下角的特殊按键。
     */
    private NumberSpecialKey mSpecialKey;
    /**
     * 左下角特殊按键的背景。
     */
    private Drawable mSpecialKeyBackground;
    /**
     * 左下角特殊按键绘制文字的 Paint。
     */
    private Paint mSpecialKeyPaint;

    /**
     * 键盘的事件。
     */
    private OnKeyboardActionListener mOnKeyboardActionListener;

    /**
     * 键盘点击事件。
     */
    private OnNumberKeyboardListener mOnKeyboardListener;

    public XNumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public XNumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XNumberKeyboardView,
                defStyleAttr, 0);
        mSpecialKeyBackground = a.getDrawable(R.styleable.XNumberKeyboardView_xnkv_blKeyBackground);

        mBRKeyDrawable = a.getDrawable(R.styleable.XNumberKeyboardView_xnkv_brKeyDrawable);
        mBRKeyBackground = a.getDrawable(R.styleable.XNumberKeyboardView_xnkv_brKeyBackground);
        mBRKeyDrawableWidth = a.getDimensionPixelOffset(R.styleable.XNumberKeyboardView_xnkv_brKeyDrawableWidth,
                -1);
        mBRKeyDrawableHeight = a.getDimensionPixelOffset(R.styleable.XNumberKeyboardView_xnkv_brKeyDrawableHeight,
                -1);

        int keyboardType = a.getInt(R.styleable.XNumberKeyboardView_xnkv_type, KeyboardType.number);

        if (a.hasValue(R.styleable.XNumberKeyboardView_android_keyTextSize)) {
            // 默认值18sp
            final int defValue = (int) (context.getResources().getDisplayMetrics().scaledDensity * 18 + 0.5f);
            mKeyTextSize = a.getDimensionPixelSize(R.styleable.XNumberKeyboardView_android_keyTextSize,
                    defValue);
        } else {
            mKeyTextSize = parentFieldValue("mKeyTextSize");
        }

        if (a.hasValue(R.styleable.XNumberKeyboardView_android_keyTextColor)) {
            mKeyTextColor = a.getColor(R.styleable.XNumberKeyboardView_android_keyTextColor, Color.BLACK);
        } else {
            mKeyTextColor = parentFieldValue("mKeyTextColor");
        }
        a.recycle();

        mSpecialKey = new NumberSpecialKey(keyboardType);

        initKeyboard();
    }

    private void initKeyboard() {
        // 设置软键盘按键的布局
        Keyboard keyboard = new Keyboard(getContext(), R.xml.keyboard_number);
        settingSpecialKey();
        setKeyboard(keyboard);

        // 设置按键没有点击放大镜显示的效果
        setPreviewEnabled(false);

        setEnabled(true);
        super.setOnKeyboardActionListener(this);
    }

    private int parentFieldValue(String fieldName) {
        try {
            Field field = this.getClass().getSuperclass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.getInt(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 重写设置监听事件的方法，避免自定义的事件被覆盖掉。
     *
     * @param listener 监听事件
     */
    @Override
    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        this.mOnKeyboardActionListener = listener;
    }

    /**
     * 设置键盘左下角特殊按键的背景。
     *
     * @param drawable Drawable
     */
    public void setSpecialKeyBackground(Drawable drawable) {
        this.mSpecialKeyBackground = drawable;
        invalidate();
    }

    /**
     * 设置键盘的类型。
     *
     * @param type 键盘的类型
     */
    public void setKeyboardType(@KeyboardType int type) {
        this.mSpecialKey.setType(type);
        Keyboard keyboard = getKeyboard();
        settingSpecialKey();
        setKeyboard(keyboard);
    }

    /**
     * 配置左下角的特殊按键。
     */
    private void settingSpecialKey() {
        if (getKeyboard() == null || getKeyboard().getKeys() == null) {
            return;
        }

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == KEYCODE_BOTTOM_LEFT) {
                key.label = mSpecialKey.keyLabel();
            }
        }
    }

    /**
     * 设置键盘的监听事件。
     *
     * @param listener 监听事件
     */
    public void setOnNumberKeyboardListener(OnNumberKeyboardListener listener) {
        this.mOnKeyboardListener = listener;
    }

    /**
     * 随机打乱数字键盘上键位的排列顺序，带有动画。
     */
    public void shuffleKeyboard() {
        this.shuffleKeyboard(true);
    }

    /**
     * 随机打乱数字键盘上键位的排列顺序。
     *
     * @param anim 是否带有动画
     */
    public void shuffleKeyboard(boolean anim) {
        if (!anim) {
            shuffleKeys();
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(0, 300);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private int lastValue;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value - lastValue < STEP_SHUFFLE_ANIM) {
                    return;
                }
                lastValue = value;
                shuffleKeys();
            }
        });
        animator.start();
    }

    /**
     * 打乱按键的顺序。
     */
    private void shuffleKeys() {
        Keyboard keyboard = getKeyboard();
        if (keyboard != null && keyboard.getKeys() != null && keyboard.getKeys().size() > 0) {
            // 随机排序数字
            Collections.shuffle(keyCodes);

            // 遍历所有的按键
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            int index = 0;
            for (Keyboard.Key key : keys) {
                int code = key.codes[0];
                // 如果按键是数字
                if (code != KEYCODE_BOTTOM_LEFT && code != KEYCODE_BOTTOM_RIGHT) {
                    char keyCode = keyCodes.get(index++);
                    key.codes[0] = keyCode;
                    key.label = Character.toString(keyCode);
                }
            }
            setKeyboard(keyboard);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 重新绘制左下角和右下角的按键
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            // 如果是左下角按键
            if (key.codes[0] == KEYCODE_BOTTOM_LEFT) {
                drawKeyBackground(key, canvas, mSpecialKeyBackground);
                drawSpecialKeyLabel(key, canvas);
            }
            // 如果是右下角按键
            else if (key.codes[0] == KEYCODE_BOTTOM_RIGHT) {
                drawKeyBackground(key, canvas, mBRKeyBackground);
                drawBRKeyDrawable(key, canvas, mBRKeyDrawable);
            }
        }
    }

    /**
     * 绘制按键的背景。
     */
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas, Drawable drawable) {
        if (drawable == null) {
            return;
        }

        int[] drawableState = key.getCurrentDrawableState();
        drawable.setState(drawableState);
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    /**
     * 绘制左下角特殊按键显示的文字。
     *
     * @param key    Key
     * @param canvas canvas
     */
    private void drawSpecialKeyLabel(Keyboard.Key key, Canvas canvas) {
        if (TextUtils.isEmpty(mSpecialKey.keyLabel())) {
            return;
        }

        if (mSpecialKeyPaint == null) {
            mSpecialKeyPaint = new Paint();
            mSpecialKeyPaint.setTextSize(mKeyTextSize);
            mSpecialKeyPaint.setTextAlign(Paint.Align.CENTER);
            mSpecialKeyPaint.setColor(mKeyTextColor);
            mSpecialKeyPaint.setStyle(Paint.Style.STROKE);
        }

        canvas.drawText(mSpecialKey.keyLabel(), key.x + key.width / 2,
                key.y + key.height / 2 + (mSpecialKeyPaint.getTextSize() - mSpecialKeyPaint.descent()) / 2,
                mSpecialKeyPaint);
    }

    /**
     * 绘制按键的图标。
     */
    private void drawBRKeyDrawable(Keyboard.Key key, Canvas canvas, Drawable drawable) {
        if (drawable == null) {
            return;
        }

        // 计算图标绘制的坐标
        if (mBRKeyDrawRect == null || mBRKeyDrawRect.isEmpty()) {
            int drawWidth, drawHeight;
            int intrinsicWidth = mBRKeyDrawable.getIntrinsicWidth();
            int intrinsicHeight = mBRKeyDrawable.getIntrinsicHeight();

            if (mBRKeyDrawableWidth > 0 && mBRKeyDrawableHeight > 0) {
                drawWidth = mBRKeyDrawableWidth;
                drawHeight = mBRKeyDrawableHeight;
            } else if (mBRKeyDrawableWidth > 0) {
                drawWidth = mBRKeyDrawableWidth;
                drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
            } else if (mBRKeyDrawableHeight > 0) {
                drawHeight = mBRKeyDrawableHeight;
                drawWidth = drawHeight * intrinsicWidth / intrinsicHeight;
            } else {
                drawWidth = intrinsicWidth;
                drawHeight = intrinsicHeight;
            }

            // 限制图标的大小，防止图标超出按键的范围
            if (drawWidth > key.width) {
                drawWidth = key.width;
                drawHeight = drawWidth * drawHeight / drawWidth;
            }
            if (drawHeight > key.height) {
                drawHeight = key.height;
                drawWidth = drawHeight * drawWidth / drawHeight;
            }

            // 获取按键图标绘制的坐标
            int left = key.x + (key.width - drawWidth) / 2;
            int top = key.y + (key.height - drawHeight) / 2;
            mBRKeyDrawRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        }

        // 绘制按键的图标
        if (!mBRKeyDrawRect.isEmpty()) {
            drawable.setBounds(mBRKeyDrawRect.left, mBRKeyDrawRect.top, mBRKeyDrawRect.right, mBRKeyDrawRect.bottom);
            drawable.draw(canvas);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.onKey(primaryCode, keyCodes);
        }

        if (mOnKeyboardListener == null) {
            return;
        }
        String insertText = "";
        if (primaryCode == KEYCODE_BOTTOM_LEFT) {
            insertText = mSpecialKey.keyLabel();
        } else if (primaryCode != KEYCODE_BOTTOM_RIGHT) {
            insertText = Character.toString((char) primaryCode);
        }
        mOnKeyboardListener.onNumberKey(primaryCode, insertText);
    }

    @Override
    public void onPress(int primaryCode) {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.onPress(primaryCode);
        }
    }

    @Override
    public void onRelease(int primaryCode) {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.onPress(primaryCode);
        }
    }

    @Override
    public void onText(CharSequence text) {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.onText(text);
        }
    }

    @Override
    public void swipeLeft() {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.swipeLeft();
        }
    }

    @Override
    public void swipeRight() {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.swipeRight();
        }
    }

    @Override
    public void swipeDown() {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.swipeDown();
        }
    }

    @Override
    public void swipeUp() {
        if (mOnKeyboardActionListener != null) {
            mOnKeyboardActionListener.swipeUp();
        }
    }
}
