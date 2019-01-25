package com.xnumberkeyboard.android.samples;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.xnumberkeyboard.android.KeyboardType;
import com.xnumberkeyboard.android.OnNumberKeyboardListener;
import com.xnumberkeyboard.android.XNumberKeyboardView;

public class MainActivity extends AppCompatActivity
        implements OnNumberKeyboardListener, RadioGroup.OnCheckedChangeListener {

    EditText editText;
    RadioGroup radioGroup;
    XNumberKeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);

        radioGroup = findViewById(R.id.rg_keyboard_type);
        radioGroup.setOnCheckedChangeListener(this);

        keyboardView = (XNumberKeyboardView) findViewById(R.id.view_keyboard);
        keyboardView.setOnNumberKeyboardListener(this);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                keyboardView.shuffleKeyboard();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                keyboardView.shuffleKeyboard(false);
            }
        });
    }

    @Override
    public void onNumberKey(int keyCode, String insert) {
        // 右下角按键的点击事件，删除一位输入的文字
        if (keyCode == XNumberKeyboardView.KEYCODE_BOTTOM_RIGHT) {
            int start = editText.length() - 1;
            if (start >= 0) {
                editText.getText().delete(start, start + 1);
            }
        }
        // 左下角按键和数字按键的点击事件，输入文字
        else {
            editText.append(insert);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 纯数字键盘
        if (checkedId == R.id.rb_pure_number) {
            keyboardView.setKeyboardType(KeyboardType.number);
            keyboardView.setSpecialKeyBackground(new ColorDrawable(getResources().getColor(R.color.colorKeyBlank)));
        }
        // 带小数点的数字键盘
        else if (checkedId == R.id.rb_digit) {
            keyboardView.setKeyboardType(KeyboardType.digit);
            keyboardView.setSpecialKeyBackground(getResources().getDrawable(R.drawable.selector_key_background));
        }
        // 身份证输入键盘
        else if (checkedId == R.id.rb_idcard) {
            keyboardView.setKeyboardType(KeyboardType.idCard);
            keyboardView.setSpecialKeyBackground(getResources().getDrawable(R.drawable.selector_key_background));
        }
    }
}
