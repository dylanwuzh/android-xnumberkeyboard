package com.xnumberkeyboard.android;

/**
 * 特殊按键的逻辑处理。
 *
 * @author Dylan Wu
 * @since 2019-01-25
 */
public class NumberSpecialKey {

    private int type;

    NumberSpecialKey(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String keyLabel() {
        if (type == KeyboardType.digit) {
            return ".";
        } else if (type == KeyboardType.idCard) {
            return "X";
        }
        return "";
    }

    @Override
    public String toString() {
        return "SpecialKey{" +
                "type=" + type +
                '}';
    }
}
