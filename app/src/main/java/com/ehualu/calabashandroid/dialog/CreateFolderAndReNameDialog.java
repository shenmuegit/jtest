package com.ehualu.calabashandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.utils.DensityUtil;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFolderAndReNameDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.etNewName)
    EditText etNewName;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvEnsure)
    TextView tvEnsure;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private NormalDialogInterface normalDialogInterface;
    private Context mContext;
    private String title;
    private String name;

    public CreateFolderAndReNameDialog(Context context, String title, String name,
                                       NormalDialogInterface normalDialogInterface) {
        super(context, R.style.NormalDialog);
        this.normalDialogInterface = normalDialogInterface;
        this.mContext = context;
        this.title = title;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_create_folder, null);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        this.getWindow().setContentView(view);
        this.getWindow().setLayout(width - DensityUtil.dip2px(10, mContext), LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        etNewName.setText(name);

        //如果是文件夹，选中名称；如果是文件,则选中不带后缀的文件名
        int selectionStart = 0;
        int extensionStart = !name.contains(".") ? -1 : name.lastIndexOf('.');
        int selectionEnd = extensionStart >= 0 ? extensionStart : name.length();
        if (selectionStart >= 0 && selectionEnd >= 0) {
            etNewName.setSelection(Math.min(selectionStart, selectionEnd), Math.max(selectionStart, selectionEnd));
        }

        etNewName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        /**
         * 通过反射修改光标颜色
         */
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(etNewName, R.drawable.edittext_cursor_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCancel.setOnClickListener(this);
        tvEnsure.setOnClickListener(this);
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                normalDialogInterface.onCancel();
                break;
            case R.id.tvEnsure:
                normalDialogInterface.onConfirm(etNewName.getText().toString());
                break;
        }
        dismiss();
    }
}
