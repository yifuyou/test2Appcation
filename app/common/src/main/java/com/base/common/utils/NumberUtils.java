package com.base.common.utils;

import java.util.Stack;

public class NumberUtils {
    private static final String[] CHINESE_NUMBERS = {"零", "一", "两", "三", "四", "五", "六", "七", "八", "九", "十"};
    private static final ChineseUnit[] CHINESE_UNIT = {ChineseUnit.zero, ChineseUnit.ten, ChineseUnit.hundred, ChineseUnit.thousand, ChineseUnit.ten_thousand, ChineseUnit.billion, ChineseUnit.million, ChineseUnit.ten_million, ChineseUnit.hundred_mullion};
    private static final String[] CHINESE_NUMBERS_2 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

    public static String translateNumber2Chinese(int number) {
        String s = String.valueOf(number);
        if (number <= 10) {
            return CHINESE_NUMBERS[number];
        }
        Stack<NumberUnit> stack = new Stack<>();
        int index = 0;
        for (int i = s.length() - 1; i >= 0; i --) {
            NumberUnit numberUnit = new NumberUnit();
            numberUnit.chineseNumber = CHINESE_NUMBERS_2[Integer.parseInt(String.valueOf(s.charAt(i)))];
            numberUnit.chineseUnit = CHINESE_UNIT[index];
            numberUnit.originalNumber = Integer.parseInt(String.valueOf(s.charAt(i)));
            stack.push(numberUnit);
            index ++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (!stack.isEmpty()) {
            NumberUnit numberUnit = stack.pop();
            if (numberUnit.originalNumber > 0) {
                stringBuilder.append(numberUnit.chineseNumber);
                if (numberUnit.chineseUnit != ChineseUnit.zero) {
                    stringBuilder.append(numberUnit.chineseUnit.getValue());
                }
            } else if (numberUnit.chineseUnit != ChineseUnit.zero) {
                NumberUnit nextNumber = stack.peek();
                if (nextNumber != null && nextNumber.originalNumber != 0) {
                    stringBuilder.append(numberUnit.chineseNumber);
                }
            }
        }
        return stringBuilder.toString();
    }

    private static class NumberUnit {

        protected ChineseUnit chineseUnit;

        protected String chineseNumber;

        protected int originalNumber;
    }

    enum ChineseUnit {
        /**
         *
         */
        zero("零"),
        /**
         *
         */
        ten("十"),
        /**
         *
         */
        hundred("百"),
        thousand("千"),
        ten_thousand("万"),
        billion("十"),
        million("百"),
        ten_million("千"),
        hundred_mullion("亿")
        ;

        private String value;

        ChineseUnit(String value) {
            this.value = value;
        }

        /**
         * Getter method for property <tt>value</tt>.
         *
         * @return property value of value
         */
        public String getValue() {
            return value;
        }

        /**
         * Setter method for property <tt>value</tt>.
         *
         * @param value  value to be assigned to property value
         */
        public void setValue(String value) {
            this.value = value;
        }
    }
}
