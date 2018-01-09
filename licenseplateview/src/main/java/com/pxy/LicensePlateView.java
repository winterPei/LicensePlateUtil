package com.pxy;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xingyang.pei
 * @date 2018/1/5.
 */

public class LicensePlateView extends RelativeLayout implements View.OnClickListener {

    private EditText editText;
    private TextView[] TextViews;
    private Activity mActivity;
    private View mNumView;
    private View mProvinceView;

    private int count = 7;
    private int lastPosition;
    private int updateViewPosition;
    private static int ITEM_VIEW_COUNT = 7;

    private LayoutInflater mInflater;

    private String inputContent;
    private boolean isUpdateView = false;//是否更新view内容

    private StringBuffer stringBuffer = new StringBuffer();
    private OnFrameTouchListener mTouchListener = new OnFrameTouchListener();

    private static final int[] VIEW_IDS = new int[]{
            R.id.item_code_iv1, R.id.item_code_iv2, R.id.item_code_iv3,
            R.id.item_code_iv4, R.id.item_code_iv5, R.id.item_code_iv6,
            R.id.item_code_iv7, R.id.item_code_iv8
    };

    private static final int[] VIEW_PROVINCE_IDS = new int[]{
            R.id.select_province_11_tv, R.id.select_province_12_tv, R.id.select_province_13_tv,
            R.id.select_province_14_tv, R.id.select_province_15_tv, R.id.select_province_16_tv,
            R.id.select_province_17_tv, R.id.select_province_18_tv, R.id.select_province_19_tv,
            R.id.select_province_110_tv,
            R.id.select_province_21_tv, R.id.select_province_22_tv, R.id.select_province_23_tv,
            R.id.select_province_24_tv, R.id.select_province_25_tv, R.id.select_province_26_tv,
            R.id.select_province_27_tv, R.id.select_province_28_tv, R.id.select_province_29_tv,
            R.id.select_province_210_tv,
            R.id.select_province_31_tv, R.id.select_province_32_tv, R.id.select_province_33_tv,
            R.id.select_province_34_tv, R.id.select_province_35_tv, R.id.select_province_35_tv,
            R.id.select_province_36_tv, R.id.select_province_37_tv, R.id.select_province_38_tv,
            R.id.select_province_41_tv, R.id.select_province_42_tv, R.id.select_province_43_tv,
            R.id.select_province_delete_tv
    };

    private static final int[] VIEW_NUM_IDS = new int[]{
            R.id.select_num_100_tv, R.id.select_num_101_tv, R.id.select_num_102_tv,
            R.id.select_num_103_tv, R.id.select_num_104_tv, R.id.select_num_105_tv,
            R.id.select_num_106_tv, R.id.select_num_107_tv, R.id.select_num_108_tv,
            R.id.select_num_109_tv,
            R.id.select_num_200_tv, R.id.select_num_201_tv, R.id.select_num_202_tv,
            R.id.select_num_203_tv, R.id.select_num_204_tv, R.id.select_num_205_tv,
            R.id.select_num_206_tv, R.id.select_num_207_tv, R.id.select_num_208_tv,
            R.id.select_num_209_tv,
            R.id.select_num_300_tv, R.id.select_num_301_tv, R.id.select_num_302_tv,
            R.id.select_num_303_tv, R.id.select_num_304_tv, R.id.select_num_305_tv,
            R.id.select_num_306_tv, R.id.select_num_307_tv, R.id.select_num_308_tv,
            R.id.select_num_309_tv,
            R.id.select_num_400_tv, R.id.select_num_401_tv, R.id.select_num_402_tv,
            R.id.select_num_403_tv, R.id.select_num_404_tv, R.id.select_num_405_tv,
            R.id.select_num_406_tv,
            R.id.select_num_delete_tv
    };

    public LicensePlateView(Context context) {
        this(context, null);
    }

    public LicensePlateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LicensePlateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        TextViews = new TextView[8];
        View.inflate(context, R.layout.layout_license_plate_frame, this);
        int textsLength = VIEW_IDS.length;
        for (int i = 0; i < textsLength; i++) {
            //textview放进数组中，方便修改操作
            TextViews[i] = (TextView) findViewById(VIEW_IDS[i]);
            TextViews[i].setOnTouchListener(mTouchListener);
        }
        editText = (EditText) findViewById(R.id.item_edittext);
        TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_blue);//第一个输入框默认设置点中效果
        editText.setCursorVisible(false);//将光标隐藏
        setListener();
        hideSoftInputMethod();
    }

    private void setListener() {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //如果字符不为""时才进行操作
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > ITEM_VIEW_COUNT - 1) {
                        //当文本长度大于 ITEM_VIEW_COUNT - 1 位时 EditText 置空
                        editText.setText("");
                        return;
                    } else {
                        //将文字添加到 StringBuffer 中
                        stringBuffer.append(editable);
                        //添加后将 EditText 置空  造成没有文字输入的错局
                        editText.setText("");
                        //记录 stringBuffer 的长度
                        count = stringBuffer.length();
                        inputContent = stringBuffer.toString();
                        if (count == 1) {
                            mProvinceView.setVisibility(GONE);
                            mNumView.setVisibility(VISIBLE);
                        }
                        if (stringBuffer.length() == ITEM_VIEW_COUNT) {
                            //文字长度为 sbLength  则调用完成输入的监听
                            if (inputListener != null) {
                                inputListener.inputComplete(inputContent);
                                mNumView.setVisibility(GONE);
                            }
                        }
                    }
                    for (int i = 0; i < stringBuffer.length(); i++) {
                        TextViews[i].setText(String.valueOf(inputContent.charAt(i)));
                        TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_blue);
                        if (i <= ITEM_VIEW_COUNT - 3) {
                            TextViews[i + 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                        }
                        if (i == ITEM_VIEW_COUNT - 2) {
                            TextViews[i + 1].setBackgroundResource(R.drawable.license_plate_last_view_blue);
                        }
                    }

                }
            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onKeyDelete()) {
                        return true;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setKeyboardContainerLayout(RelativeLayout layout) {
        mInflater = LayoutInflater.from(mActivity);
        mProvinceView = mInflater.inflate(R.layout.layout_keyboard_province, null);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mProvinceView.setLayoutParams(rlParams);
        mNumView = mInflater.inflate(R.layout.layout_keyboard_num, null);
        mNumView.setLayoutParams(rlParams);
        int provinceLength = VIEW_PROVINCE_IDS.length;
        View view;
        for (int i = 0; i < provinceLength; i++) {
            view = mProvinceView.findViewById(VIEW_PROVINCE_IDS[i]);
            view.setOnClickListener(this);
        }
        int numLength = VIEW_NUM_IDS.length;
        for (int i = 0; i < numLength; i++) {
            view = mNumView.findViewById(VIEW_NUM_IDS[i]);
            view.setOnClickListener(this);
        }
        layout.addView(mProvinceView);
        layout.addView(mNumView);
        mNumView.setVisibility(GONE);
    }

    /**
     * 显示 8 个输入框
     */
    public boolean showLastView() {
        TextViews[7].setVisibility(VISIBLE);
        if (TextUtils.isEmpty(TextViews[6].getText())) {
            TextViews[6].setBackgroundResource(R.drawable.license_plate_mid_view_bg);
            TextViews[7].setBackgroundResource(R.drawable.license_plate_last_view_bg);
        } else {
            TextViews[6].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
            TextViews[7].setBackgroundResource(R.drawable.license_plate_last_view_blue);
            mProvinceView.setVisibility(GONE);
            mNumView.setVisibility(VISIBLE);
        }
        if (updateViewPosition == 6) {
            if (!isUpdateView) {
                TextViews[5].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
            } else {
                TextViews[6].setBackgroundResource(R.drawable.license_plate_choice_view_bg);
            }
        }
        ITEM_VIEW_COUNT = 8;
        return true;
    }

    /**
     * 显示 7 个输入框
     */
    public boolean hideLastView() {
        TextViews[7].setVisibility(GONE);
        if (stringBuffer.length() == 8 || stringBuffer.length() == 7) {
            TextViews[7].setText("");
            stringBuffer.delete(7, 8);
            TextViews[6].setBackgroundResource(R.drawable.license_plate_last_view_blue);
            inputContent = stringBuffer.toString();
            count = stringBuffer.length();
            inputListener.inputComplete(inputContent);
            mNumView.setVisibility(GONE);
        } else {
            TextViews[6].setBackgroundResource(R.drawable.license_plate_last_view_bg);
        }
        ITEM_VIEW_COUNT = 7;
        return false;
    }

    private boolean onKeyDelete() {
        if (count == 0) {
            count = 7;
            return true;
        }
        if (stringBuffer.length() > 0) {
            //删除相应位置的字符
            stringBuffer.delete((count - 1), count);
            count--;
            if (count == 0) {
                //切换回省份选择
                mProvinceView.setVisibility(VISIBLE);
                mNumView.setVisibility(GONE);
            }
            inputContent = stringBuffer.toString();
            TextViews[stringBuffer.length()].setText("");
            if (stringBuffer.length() < ITEM_VIEW_COUNT - 2) {
                TextViews[stringBuffer.length() + 1].setBackgroundResource(R.drawable.license_plate_mid_view_bg);
            }
            if (stringBuffer.length() == ITEM_VIEW_COUNT - 2) {
                TextViews[stringBuffer.length()].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                TextViews[stringBuffer.length() + 1].setBackgroundResource(R.drawable.license_plate_last_view_bg);
            }
            //有删除就通知manger
            inputListener.deleteContent();
        }
        return false;
    }

    /**
     * 清空输入内容
     */
    public void clearEditText() {
        stringBuffer.delete(0, stringBuffer.length());
        inputContent = stringBuffer.toString();
        for (int i = 0; i < TextViews.length; i++) {
            TextViews[i].setText("");
            TextViews[i].setBackgroundResource(R.drawable.license_plate_code_gray_bg);
        }
    }

    private @NonNull
    InputListener inputListener;

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    /**
     * 键盘的点击事件
     */
    @Override
    public void onClick(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setSelected(true);
            String text = tv.getText().toString();
            if (view.getId() == R.id.select_province_delete_tv || view.getId() == R.id.select_num_delete_tv) {
                inputListener.deleteContent();
            }
            setEditContent(text);
        }
    }

    /**
     * 输入完成监听回调接口
     */
    public interface InputListener {

        /**
         * @param content 当输入完成时的全部内容
         */
        void inputComplete(String content);

        /**
         * 删除操作
         */
        void deleteContent();

    }

    /**
     * 获取输入文本
     *
     * @return
     */
    public String getEditContent() {
        return inputContent;
    }

    /**
     * 设置 EditText 的输入内容
     * 根据isUpdateView 判断修改/删除操作
     */
    private void setEditContent(String content) {
        if (!isUpdateView) {
            if (!TextUtils.isEmpty(content)) {
                editText.setText(content);
            } else {
                onKeyDelete();
            }
        } else {
            if (!TextUtils.isEmpty(content)) {
                stringBuffer.replace(updateViewPosition, updateViewPosition + 1, content);
                isUpdateView = !isUpdateView;
            } else {
                TextViews[updateViewPosition].setText(content);
                if (updateViewPosition + 1 == ITEM_VIEW_COUNT) {
                    isUpdateView = !isUpdateView;
                    stringBuffer.delete(updateViewPosition, updateViewPosition + 1);
                    TextViews[updateViewPosition - 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                    TextViews[updateViewPosition].setBackgroundResource(R.drawable.license_plate_last_view_blue);
                    count--;
                }
                inputListener.deleteContent();
                return;
            }
            TextViews[updateViewPosition].setText(content);
            inputContent = stringBuffer.toString();
            count = stringBuffer.length();
            if (updateViewPosition == 0) {
                TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_blue);
            } else if (updateViewPosition == 1) {
                TextViews[0].setBackgroundResource(R.drawable.license_plate_first_view_blue);
                TextViews[updateViewPosition].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
            } else if (updateViewPosition == ITEM_VIEW_COUNT - 1) {
                TextViews[updateViewPosition - 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                TextViews[updateViewPosition].setBackgroundResource(R.drawable.license_plate_last_view_blue);
            } else {
                TextViews[updateViewPosition - 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                TextViews[updateViewPosition].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
            }
            //切换数字输入
            mProvinceView.setVisibility(GONE);
            mNumView.setVisibility(VISIBLE);
            if (stringBuffer.length() == ITEM_VIEW_COUNT) {
                //文字长度为sblength  则调用完成输入的监听
                if (inputListener != null) {
                    inputListener.inputComplete(inputContent);
                    mNumView.setVisibility(GONE);
                }
            }
        }
    }

    /**
     * 显示输入框的TouchListener
     */
    private class OnFrameTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setFocusable(true);
                String tvString = (String) tv.getText();
                if (TextUtils.isEmpty(tvString)) {
                    isUpdateView = false;
                    return false;
                }
                int viewId = tv.getId();
                for (int i = 0; i < stringBuffer.length(); i++) {
                    if (viewId == VIEW_IDS[i]) {
                        updateViewPosition = i;
                        if (isUpdateView) {
                            if (lastPosition == 0) {
                                TextViews[lastPosition].setBackgroundResource(R.drawable.license_plate_first_view_blue);
                            } else if (lastPosition == 1) {
                                TextViews[lastPosition - 1].setBackgroundResource(R.drawable.license_plate_first_view_blue);
                                TextViews[lastPosition].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                            } else if (lastPosition < ITEM_VIEW_COUNT - 1) {
                                TextViews[lastPosition - 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                                TextViews[lastPosition].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                            } else {
                                TextViews[lastPosition - 1].setBackgroundResource(R.drawable.license_plate_mid_view_blue);
                                TextViews[lastPosition].setBackgroundResource(R.drawable.license_plate_last_view_blue);
                            }
                        }
                        if (i == 0) {
                            TextViews[i].setBackgroundResource(R.drawable.license_plate_choice_first_bg);
                            isUpdateView = true;
                            mProvinceView.setVisibility(VISIBLE);
                            mNumView.setVisibility(GONE);
                        } else if (i == 1) {
                            TextViews[i - 1].setBackgroundResource(R.drawable.license_plate_second_choice_first_blue);
                            TextViews[i].setBackgroundResource(R.drawable.license_plate_choice_view_bg);
                            isUpdateView = true;
                            mProvinceView.setVisibility(GONE);
                            mNumView.setVisibility(VISIBLE);
                        } else if (i < ITEM_VIEW_COUNT - 1 && i > 1) {
                            TextViews[i - 1].setBackgroundResource(R.drawable.license_plate_before_view_bg);
                            TextViews[i].setBackgroundResource(R.drawable.license_plate_choice_view_bg);
                            isUpdateView = true;
                            mProvinceView.setVisibility(GONE);
                            mNumView.setVisibility(VISIBLE);
                        } else {
                            TextViews[i - 1].setBackgroundResource(R.drawable.license_plate_before_view_bg);
                            TextViews[i].setBackgroundResource(R.drawable.license_plate_choice_last_bg);
                            isUpdateView = true;
                            mProvinceView.setVisibility(GONE);
                            mNumView.setVisibility(VISIBLE);
                        }
                        lastPosition = i;
                    }
                }
            }
            return true;
        }

    }

    /**
     * 禁用系统软键盘
     */
    public void hideSoftInputMethod() {
        mActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                editText.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
