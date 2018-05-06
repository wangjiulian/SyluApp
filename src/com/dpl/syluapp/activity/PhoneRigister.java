//package com.dpl.syluapp.activity;
//
//import java.util.HashMap;
//import java.util.Random;
//
//import com.dpl.syluapp.AppActivity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Handler.Callback;
//import android.os.Message;
//import android.util.Log;
//import android.widget.Toast;
//import cn.smssdk.EventHandler;
//import cn.smssdk.R;
//import cn.smssdk.SMSSDK;
//import cn.smssdk.gui.RegisterPage;
//
//public class PhoneRigister extends AppActivity implements Callback {
//	// 鐭俊娉ㄥ唽锛岄殢鏈轰骇鐢熷ご鍍�
//	private static final String[] AVATARS = {
//			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
//			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
//			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
//			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
//			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
//			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
//			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
//			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
//			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
//			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
//			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
//			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//setContentView(R.layout.activity_main);
//		initSDK();
//		// SMSSDK.initSDK(this, "4cd3fce06aac",
//		// "87904577fbb8f5e19b360a0c725d19b5");
//		init();
//		Toast.makeText(this, "ok!", 1).show();
//	}
//
//	void init() {
//
//		// 鎵撳紑娉ㄥ唽椤甸潰
//		RegisterPage registerPage = new RegisterPage();
//		
//		registerPage.setRegisterCallback(new EventHandler() {
//			public void afterEvent(int event, int result, Object data) {
//				// 瑙ｆ瀽娉ㄥ唽缁撴灉
//				if (result == SMSSDK.RESULT_COMPLETE) {
//					@SuppressWarnings("unchecked")
//					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//					String country = (String) phoneMap.get("country");
//					String phone = (String) phoneMap.get("phone");
//
//					// 鎻愪氦鐢ㄦ埛淇℃伅
//					registerUser(country, phone);
//				}
//			}
//		});
//		registerPage.show(this);
//	}
//
//	private void registerUser(String country, String phone) {
//		Random rnd = new Random();
//		int id = Math.abs(rnd.nextInt());
//		String uid = String.valueOf(id);
//		String nickName = "SmsSDK_User_" + uid;
//		String avatar = AVATARS[id % 12];
//		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
//	}
//
//	private void initSDK() {
//		// 鍒濆鍖栫煭淇DK
//		SMSSDK.initSDK(this, "5456d2f7face", "5afa9ae259fee1fa1cd95accdc0b3044");
//		final Handler handler = new Handler(this);
//		EventHandler eventHandler = new EventHandler() {
//			public void afterEvent(int event, int result, Object data) {
//				Message msg = new Message();
//				msg.arg1 = event;
//				msg.arg2 = result;
//				msg.obj = data;
//				handler.sendMessage(msg);
//			}
//		};
//		SMSSDK.registerEventHandler(eventHandler);
//
//	}
//
//	@Override
//	public boolean handleMessage(Message msg) {
//
//		int event = msg.arg1;
//		int result = msg.arg2;
//		Object data = msg.obj;
//
//		Log.i("TAG", "result-->" + result);
//		Log.i("TAG", "event-->" + event);
//		Log.i("TAG", "data-->" + data);
//		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//			// 鐭俊娉ㄥ唽鎴愬姛鍚庯紝杩斿洖MainActivity,鐒跺悗鎻愮ず鏂板ソ鍙�
//			if (result == SMSSDK.RESULT_COMPLETE) {
//				Toast.makeText(this, R.string.smssdk_user_info_submited,
//						Toast.LENGTH_SHORT).show();
//
//			} else {
//				((Throwable) data).printStackTrace();
//			}
//		} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
//			if (result == SMSSDK.RESULT_COMPLETE) {
//
//			} else {
//				((Throwable) data).printStackTrace();
//			}
//		}
//
//		else if (result == -1) {
//			if (event == 3) {
//				//楠岃瘉鎴愬姛锛岃幏鍙栨墜鏈哄彿
//				Log.i("TAG", "event11-->" + event);
//				Log.i("TAG", "data--ss>" + data);
//			}
//		}
//		return false;
//
//	}
//
//}
//
