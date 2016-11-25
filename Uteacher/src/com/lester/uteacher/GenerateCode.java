package com.lester.uteacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.zxing.WriterException;
import com.lester.uteacher.tool.Usercode;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;


public class GenerateCode extends Activity {
    /** Called when the activity is first created. */
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	
	

	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
	}
	@Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.generatecode);
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
       findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), CodeDetail.class);
			startActivity(intent);
			}
       });
        
        
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(GenerateCode.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {
						//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
//						Usercode usercode=new Usercode();//保存图片
//						usercode.saveQrCodePicture(me_generatecode.this,qrCodeBitmap);
					}else {
						Toast.makeText(GenerateCode.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
					}
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode==0&&resultCode==RESULT_OK) {
			String str=data.getStringExtra("result");
			if (!str.equals("")) {
				resultTextView.setText(str);
			}
		}
    
    }
    
}