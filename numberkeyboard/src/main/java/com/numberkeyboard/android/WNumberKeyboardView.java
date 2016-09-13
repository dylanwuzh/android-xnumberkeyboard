package com.numberkeyboard.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数字键盘。
 *
 * @author wuzhen
 * @version Version 1.0, 2016-09-08
 */
public class WNumberKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    // 用于区分左下角空白的按键
    private static final int KEYCODE_EMPTY = -10;

    private int mDeleteWidth;
    private int mDeleteHeight;
    private int mDeleteBackgroundColor;
    private Drawable mDeleteDrawable;
    private Rect mDeleteDrawRect;

    private IOnKeyboardListener mOnKeyboardListener;

    public WNumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WNumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WNumberKeyboardView,
                defStyleAttr, 0);
        mDeleteDrawable = a.getDrawable(R.styleable.WNumberKeyboardView_wnkv_deleteDrawable);
        mDeleteBackgroundColor = a.getColor(
                R.styleable.WNumberKeyboardView_wnkv_deleteBackgroundColor, Color.TRANSPARENT);
        mDeleteWidth = a.getDimensionPixelOffset(R.styleable.WNumberKeyboardView_wnkv_deleteWidth,
                -1);
        mDeleteHeight = a.getDimensionPixelOffset(R.styleable.WNumberKeyboardView_wnkv_deleteHeight,
                -1);
        a.recycle();

        // 设置软键盘按键的布局
        Keyboard keyboard = new Keyboard(context, R.xml.keyboard_number);
        setKeyboard(keyboard);

        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 遍历所有的按键
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            // 如果是左下角空白的按键，重画按键的背景
            if (key.codes[0] == KEYCODE_EMPTY) {
                drawKeyBackground(key, canvas, mDeleteBackgroundColor);
            }
            // 如果是右下角的删除按键，重画背景，并且绘制删除的图标
            else if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                drawKeyBackground(key, canvas, mDeleteBackgroundColor);
                drawDeleteButton(key, canvas);
            }
        }
    }

    // 绘制按键的背景
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas, int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    // 绘制删除按键
    private void drawDeleteButton(Keyboard.Key key, Canvas canvas) {
        if (mDeleteDrawable == null) {
            return;
        }

        // 计算删除图标绘制的坐标
        if (mDeleteDrawRect == null || mDeleteDrawRect.isEmpty()) {
            int drawWidth, drawHeight;
            int intrinsicWidth = mDeleteDrawable.getIntrinsicWidth();
            int intrinsicHeight = mDeleteDrawable.getIntrinsicHeight();

            if (mDeleteWidth > 0 && mDeleteHeight > 0) {
                drawWidth = mDeleteWidth;
                drawHeight = mDeleteHeight;
            } else if (mDeleteWidth > 0 && mDeleteHeight <= 0) {
                drawWidth = mDeleteWidth;
                drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
            } else if (mDeleteWidth <= 0 && mDeleteHeight > 0) {
                drawHeight = mDeleteHeight;
                drawWidth = drawHeight * intrinsicWidth / intrinsicHeight;
            } else {
                drawWidth = intrinsicWidth;
                drawHeight = intrinsicHeight;
            }

            // 限制图标的大小，防止图标超出按键
            if (drawWidth > key.width) {
                drawWidth = key.width;
                drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
            }
            if (drawHeight > key.height) {
                drawHeight = key.height;
                drawWidth = drawHeight * intrinsicWidth / intrinsicHeight;
            }

            // 获取删除图标绘制的坐标
            int left = key.x + (key.width - drawWidth) / 2;
            int top = key.y + (key.height - drawHeight) / 2;
            mDeleteDrawRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        }

        // 绘制删除的图标
        if (mDeleteDrawRect != null && !mDeleteDrawRect.isEmpty()) {
            mDeleteDrawable.setBounds(mDeleteDrawRect.left, mDeleteDrawRect.top,
                    mDeleteDrawRect.right, mDeleteDrawRect.bottom);
            mDeleteDrawable.draw(canvas);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        // 处理按键的点击事件
        // 点击删除按键
        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            if (mOnKeyboardListener != null)
                mOnKeyboardListener.onDeleteKeyEvent();
        }
        // 点击了非左下角按键的其他按键
        else if (primaryCode != KEYCODE_EMPTY) {
            if (mOnKeyboardListener != null) {
                mOnKeyboardListener.onInsertKeyEvent(Character.toString(
                        (char) primaryCode));
            }
        }
    }


    //
    // ========= 随机键盘 Start =========
    //

    // 0-9
    private final List<Character> keyCodes = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9');

    /**
     * 随机打乱数字键盘上键位的排列顺序。
     */
    public void shuffleKeyboard() {
        Keyboard keyboard = getKeyboard();
        if (keyboard != null && keyboard.getKeys() != null && keyboard.getKeys().size() > 0) {
            // 随机排序数字
            Collections.shuffle(keyCodes);

            // 遍历所有的按键
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            int index = 0;
            for (Keyboard.Key key : keys) {
                // 如果按键是数字
                if (key.codes[0] != KEYCODE_EMPTY && key.codes[0] != Keyboard.KEYCODE_DELETE) {
                    char code = keyCodes.get(index++);
                    key.codes[0] = code;
                    key.label = Character.toString(code);
                }
            }
            setKeyboard(keyboard);
        }
    }

    //
    // ========= 随机键盘 End =========
    //


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * 设置键盘的监听事件。
     *
     * @param listener 监听事件
     */
    public void setIOnKeyboardListener(IOnKeyboardListener listener) {
        this.mOnKeyboardListener = listener;
    }

    public interface IOnKeyboardListener {

        void onInsertKeyEvent(String text);

        void onDeleteKeyEvent();
    }
}
