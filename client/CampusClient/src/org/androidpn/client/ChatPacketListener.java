package org.androidpn.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.androidpn.data.ChatInfo;
import org.androidpn.data.ChatManager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class ChatPacketListener implements PacketListener{
	private static String LOGTAG = "ChatPacketListener";
	private XmppManager manager;
//	private SessionServiceBinder myBinder = new SessionServiceBinder();
	public ChatPacketListener(XmppManager manager){
		this.manager=manager;
	}
	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		if (packet instanceof Message) {
			// 显示接收到的消息
			String jid = packet.getFrom();
			String username = jid.substring(0, jid.indexOf('@'));
			Log.i(LOGTAG, "recv packet from:" + username+" content:"+((Message)packet).getBody());
			
			ChatManager.addMsg(
				username,new ChatInfo(username, 
					((Message) packet).getBody(), new Date(System
					.currentTimeMillis()), packet
					.getPacketID(), false));
			
		} else if (packet instanceof IQ) {
			if (((IQ) packet).getType() == IQ.Type.RESULT) {
				Log.i(LOGTAG, "recv a result IQ whose id is :"
						+ packet.getPacketID());
				if (((IQ) packet).getError() == null) {
					// 发送的消息成功被服务器接收
					ChatManager.msgSent(packet.getPacketID());
//					Intent intent = new Intent(
//							Constants.ACTION_CHAT_SENT);
//					intent.putExtra("packetid", packet.getPacketID());
				}else{
					Log.i(LOGTAG,"iq error is:"+((IQ)packet).getError());
				}
			}
		}
	}
//	private static ChatPacketListener instance=null;
//	private static Object lock=new Object();
//	public static void listen(){
//		getInstance();
//	}
//	private static ChatPacketListener getInstance(){
//		synchronized(lock){
//			if(instance==null){
//				instance=new ChatPacketListener();
//			}
//		}
//		return instance;
//	}
//	public MessagePacketListener()  {
//		Log.i(LOGTAG,"MessagePacketListener: add packet listener to xmppmanager");
//		manager=Constants.xmppManager;
//		manager.addPacketListener(new PacketListener() {
//			
//			}
//		}, 
//		});
	}

//	@Override
//	public IBinder onBind(Intent arg0) {
//		// TODO Auto-generated method stub
//		Log.i(LOGTAG, "onbind");
//		return myBinder;
//	}
//
//	public class SessionServiceBinder extends Binder {
//		// this getService function should be changed to public
//		public PacketListenerManager getService() {
//			// Return this instance of LocalService so clients can call public
//			// methods
//			return PacketListenerManager.this;
//		}
//	}

