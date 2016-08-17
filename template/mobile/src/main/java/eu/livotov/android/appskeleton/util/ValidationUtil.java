package eu.livotov.android.appskeleton.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import eu.livotov.android.appskeleton.R;
import eu.livotov.android.appskeleton.core.App;

/**
 * Created by dlivotov on 10/07/2016.
 */

public class ValidationUtil
{
    public static boolean validateLogin(ImageView fieldIcon, EditText field, TextInputLayout hintLayout)
    {
        if (TextUtils.isEmpty(field.getText()))
        {
            ValidationUtil.showError(fieldIcon, field, hintLayout, App.getContext().getString(R.string.form_error_misssed_phone));
            return false;
        }

        if (!TextUtils.isDigitsOnly(field.getText()))
        {
            ValidationUtil.showError(fieldIcon, field, hintLayout, App.getContext().getString(R.string.form_error_phone_wrong));
            return false;
        }

        ValidationUtil.hideError(fieldIcon, field, hintLayout);
        return true;
    }

    public static void showError(ImageView fieldIcon, EditText field, TextInputLayout fieldHintLayout, String error)
    {
        fieldIcon.setImageTintList(createColorStateList(fieldIcon.getContext(), R.color.color_validation_error));
        field.setTextColor(createColorStateList(fieldIcon.getContext(), R.color.color_validation_error));
        field.setForegroundTintList(createColorStateList(fieldIcon.getContext(), R.color.color_validation_error));
        fieldHintLayout.setErrorEnabled(true);
        fieldHintLayout.setError(error);
    }

    public static void hideError(ImageView fieldIcon, EditText field, TextInputLayout fieldHintLayout)
    {
        fieldIcon.setImageTintList(createColorStateList(fieldIcon.getContext(), R.color.color_validation_ok));
        field.setTextColor(createColorStateList(fieldIcon.getContext(), R.color.color_validation_ok));
        field.setForegroundTintList(createColorStateList(fieldIcon.getContext(), R.color.color_validation_ok));
        fieldHintLayout.setErrorEnabled(false);
        fieldHintLayout.setError("");
    }

    public static ColorStateList createColorStateList(Context ctx, @ColorRes int color)
    {
        int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{ctx.getResources().getColor(color), ctx.getResources().getColor(color), ctx.getResources().getColor(color), ctx.getResources().getColor(color)};

        return new ColorStateList(states, colors);
    }

    public static boolean validatePassword(ImageView fieldIcon, EditText field, TextInputLayout hintLayout)
    {
        if (TextUtils.isEmpty(field.getText()))
        {
            ValidationUtil.showError(fieldIcon, field, hintLayout, App.getContext().getString(R.string.form_error_missed_password));
            return false;
        }

        ValidationUtil.hideError(fieldIcon, field, hintLayout);
        return true;
    }
}
