package com.dysen.commom_library.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.dysen.commom_library.R;
import com.dysen.commom_library.base.ParentActivity;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketUtil {
	private String HOST = "";// 网络请求IP地址
	private int PORT = 0;// 网络请求端口号
	private Socket socket = null;// Socket
	private Context context;//
	private Dialog dialog;// 加载框
	private boolean showDialog = false;// 默认不显示Dialog
	private String data = null;// 发送的XML请求
	private InputStream is;// 服务器传过来的输出流
	private String responseString;// 从服务器返回过来的数据
	private OutputStream os;// 客户端发送的数据流
	private GetSocketData getData;// 成功接收服务器上的

	// 用来接收数据传输给主线程
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (msg.what == 2) {
				responseString = (String) msg.obj;
				closeSocket();
				getData.getSocketData(responseString + 1);
			} else if (msg.what == 404) {
				new ParentActivity().ShowDialog(context, "网络连接失败");
			} else if (msg.what == 303) {
				new ParentActivity().ShowDialog(context, "网路中断");
			}
		};
	};

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public SocketUtil(Context context) {
		this.context = context;
	}

	public SocketUtil setDialog(boolean showDialog) {
		this.showDialog = showDialog;
		return this;
	}

	/**
	 * 设置Ip地址
	 * 
	 * @param Host
	 * @return
	 */
	public SocketUtil setHost(String Host) {
		this.HOST = Host;
		return this;
	}

	/**
	 * 启动线程，连接服务端
	 */
	public void execute() {
		if (showDialog == true) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.dialog_progress, null);
			dialog = new AlertDialog.Builder(context).create();
			dialog.show();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.x = 250;
			lp.y = 0;
			dialog.getWindow().setAttributes(lp);
			dialog.getWindow().setContentView(view);
			dialog.setCancelable(false);
		}
		new Thread(sendRunnable).start();
	}

	/**
	 * 设置端口号
	 * 
	 * @param port
	 * @return
	 */
	public SocketUtil setPort(int port) {
		this.PORT = port;
		return this;
	}

	/**
	 * 输入拼装的XML
	 * 
	 * @param data
	 * @return
	 */
	public SocketUtil setData(String data) {
		this.data = data;
		return this;
	}

	/**
	 * 关闭Socket流
	 */
	private void closeSocket() {
		try {
			os.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立socket连接
	 */
	private void connect() {
		try {
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(HOST, PORT);
			socket.connect(address, 4000);
			socket.setSoTimeout(3000);
			os = socket.getOutputStream();
			is = socket.getInputStream();
		} catch (Exception e) {
			handler.sendEmptyMessage(404);
		}
	}

	/**
	 * 接收数据
	 */
	private synchronized void rdata() {
		try {
			if (socket != null) {
				if (socket.isConnected()) {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					byte[] data = new byte[4096];
					int count = -1;
					while ((count = is.read(data, 0, 4096)) != -1) {
						outputStream.write(data, 0, count);
					}
					data = null;
					String s = new String(outputStream.toByteArray(), "gb2312");
					Message msg = new Message();
					msg.obj = s;
					msg.what = 2;
					handler.sendMessage(msg);
				} else {
				}
			} else {
				connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(303);
		}
	}

	/**
	 * Socket 连接成功后的回调接口
	 * 
	 * @author 金志君
	 * 
	 */
	public interface GetSocketData {
		public void getSocketData(String responseString);
	}

	/**
	 * 设置接口
	 * 
	 * @param getSocketData
	 */
	public void setGetSocketData(GetSocketData getSocketData) {
		this.getData = getSocketData;
	}

	/**
	 * 进行连接 如果连接成功则发送数据
	 */
	Runnable sendRunnable = new Runnable() {
		@Override
		public void run() {
			connect();
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(os, "gb2312")), true);
						// 发送完毕
						out.write(data);
						out.flush();
						rdata();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
				}
			} else {
			}
		}
	};
}
