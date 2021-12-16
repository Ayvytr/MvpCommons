package com.common.base.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.R;
import com.common.R2;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * @author Administrator
 */
public class UpdateAppDialog extends BaseDialog{
    @BindView(R2.id.tv_skip)
    TextView tvSkip;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.btn_update_now)
    Button btnUpdateNow;
    private String version;
    private String content;

    public UpdateAppDialog(@NonNull Context context, String version, String content) {
        super(context);
        this.version = version;
        this.content = content;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_update;
    }

    @Override
    protected void initView() {
        setCanceledOnTouchOutside(false);

        tvContent.setText(Html.fromHtml(content));
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    String packageName = getContext().getPackageName();
//                    Uri uri = Uri.parse("market://details?id=" + packageName);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.setPackage("com.android.vending");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getContext().startActivity(intent);
//                } catch (Exception e) {
//                    L.e(e);
//                    ToastUtil.show(getContext(), R.string.open_google_play_failed);
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

}
