/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.utils.OkHttpUtils2;
import cn.ucai.superwechat.utils.OnSetAvatarListener;
import cn.ucai.superwechat.utils.Utils;
import cn.ucai.superwechat.widget.I;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {
	final static String TAG = RegisterActivity.class.getName().toString();
	private EditText userNameEditText;
	private EditText userNickEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	RelativeLayout layout_useravatar;
	String AvatarName;
	ImageView iv_avatar;

	String username;
	String Nick;
	String pwd;
	 ProgressDialog pd;

	public String getAvatarName() {
		return AvatarName=String.valueOf(SystemClock.currentThreadTimeMillis());
	}

	OnSetAvatarListener mOnSetAvatarListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cn.ucai.superwechat.R.layout.activity_register);
		init();
		setListener();
	}

	private void setListener() {
		findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				back(view);
			}
		});
		findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				register(view);
			}
		});
		layout_useravatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mOnSetAvatarListener = new OnSetAvatarListener(RegisterActivity.this,
						R.id.layout_register,getAvatarName(), I.AVATAR_TYPE_USER_PATH);
				//iv_avatar.setImageDrawable(mOnSetAvatarListener);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode!=RESULT_OK){
			return;
		}
		mOnSetAvatarListener.setAvatar(requestCode,data,iv_avatar);
	}

	private void init() {
		userNameEditText = (EditText) findViewById(R.id.username);

		userNickEditText = (EditText) findViewById(R.id.etNick);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);

		layout_useravatar = (RelativeLayout) findViewById(R.id.layout_user_avatar);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);

	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		 username = userNameEditText.getText().toString().trim();
		pwd = passwordEditText.getText().toString().trim();
		        Nick = userNickEditText.getText().toString().trim();
		       String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		}else if (TextUtils.isEmpty(Nick)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
			userNickEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			 pd = new ProgressDialog(this);
			pd.setMessage(getResources().getString(cn.ucai.superwechat.R.string.Is_the_registered));
			pd.show();
                RegisterAppserver();
		//	RegisterEMserver(username, pwd, pd);

		}
	}

	private void RegisterAppserver() {
		File file = new File(OnSetAvatarListener.getAvatarPath(RegisterActivity.this, I.AVATAR_TYPE_USER_PATH)
				,AvatarName+I.AVATAR_SUFFIX_JPG);
		OkHttpUtils2<Result> utils = new OkHttpUtils2<Result>();
		utils.setRequestUrl(I.REQUEST_REGISTER)
				.addParam(I.User.USER_NAME,username)
				.addParam(I.User.NICK,Nick)
				.addParam(I.User.PASSWORD,pwd)
				.targetClass(Result.class)
				.addFile(file)
				.execute(new OkHttpUtils2.OnCompleteListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						if (result.isRetMsg()){
							RegisterEMserver();
						}else {
							Log.e(TAG, "Register:fail "+result.getRetCode() );
							pd.dismiss();

							Toast.makeText(getApplicationContext(),
									R.string.Login_failed+ Utils.getResourceString(RegisterActivity.this,result.getRetCode()),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onError(String error) {
						pd.dismiss();
						Log.e(TAG, "Register.error: "+error );
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
					}
				});

	}

	/**
	 * 调用sdk注册方法，注册环信帐号
     */
	private void RegisterEMserver() {
		new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, pwd);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // 保存用户名
                            SuperWeChatApplication.getInstance().setUserName(username);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {

					runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NONETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ALREADY_EXISTS){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.UNAUTHORIZED){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.ILLEGAL_USER_NAME){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
					//远端服务器注册成功。环信服务器注册失败。则用此方法删除远端服务器注册的帐号
					unRegisterAppServer();
                }
            }
        }).start();
	}
	/**
	 *  删除远端服务期注册的用户
	 */

private void unRegisterAppServer(){
	OkHttpUtils2<Result> utils = new OkHttpUtils2<Result>();
	utils.setRequestUrl(I.REQUEST_UNREGISTER)
			.addParam(I.User.USER_NAME,username)
			.targetClass(Result.class)
			.execute(new OkHttpUtils2.OnCompleteListener<Result>() {
				@Override
				public void onSuccess(Result result) {
					Log.e(TAG, "Register:fail. 删除本地服务端注册的用户-成功");
				}

				@Override
				public void onError(String error) {
					Log.e(TAG, "Register:fail. 删除本地服务端注册的用户-失败");
				}
			});

}
	public void back(View view) {
		finish();
	}

}
