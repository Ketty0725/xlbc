package com.ketty.provider_sms.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 获取状态报告的线程
 */
public class RecvRptThread extends Thread
{

	// 用户账号
	private String	userid		= null;

	// 用户密码
	private String	pwd	= null;
	
	//密码是否加密   true：密码加密;false：密码不加密
	private boolean isEncryptPwd=true;
	
	// 每次请求想要获取状态报告的最大条数
	private int retsize = 0;

	// 日期格式定义
	private static SimpleDateFormat	sdf						= new SimpleDateFormat("MMddHHmmss");

	/**
	 * 
	 * @description    构造函数
	 * @param userid   用户账号
	 * @param password      密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 * @param retsize  每次请求想要获取上行的最大条数      			 
	 */
	public RecvRptThread(String userid, String password,boolean isEncryptPwd, int retsize)
	{
		// 用户账号
		this.userid = userid;
		// 用户密码
		this.pwd = password;
		//密码是否加密
		this.isEncryptPwd=isEncryptPwd;
		// 每次请求想要获取状态报告的最大条数
		this.retsize = retsize;
	}

	// 获取状态报告业务类
	private CHttpPost	cHttpPost	= new CHttpPost();

	/**
	 * 获取状态报告线程的方法
	 */
	@Override
	public void run()
	{
		//状态报告集合  本集合临时存储状态报告，需要将收到的状态报告保存在一个队列中，由另外一个线程去处理
		List<RPT> rpts=new ArrayList<RPT>();
		//处理后的密码
		String handlePwd=null;
		// 返回值
		int result = -310099;
		//状态报告对象声明
		RPT rpt = null;
		//时间戳声明
		String timestamp=null;
		// 循环调用获取状态报告的方法
		while(true)
		{
			try
			{
				// 初始化返回值
				result = -310099;
				// 清空状态报告集合中的对象
				rpts.clear();
				// 将 userid转成大写,以防大小写不一致
				userid = userid.toUpperCase();
				//判断密码是否加密。
				//密码加密，则对密码进行加密
				if(isEncryptPwd)
				{
					// 设置时间戳
				    timestamp = sdf.format(Calendar.getInstance().getTime());
					
					// 对密码进行加密
				    handlePwd = cHttpPost.encryptPwd(userid,pwd, timestamp);
				}else
				{
					//不加密，不需要设置时间戳
					timestamp=null;
					//密码不加密，使用不加密的密码
					handlePwd=pwd;
				}
				
				// 调用获取状态报告接口
				result = cHttpPost.getRpt(userid, handlePwd,timestamp,retsize,rpts);
				// 如果获取状态报告成功，并且有状态报告
				if(result == 0&&rpts != null && rpts.size() > 0)
				{
					// 有状态报告
					// 添加到状态报告队列
					// add rpt queue
					// 这里不要做复杂的耗时的处理，将收到的状态报告保存在一个队列中，由另外一个线程去处理。

					//代码示例是将状态报告信息打印出来
					System.out.println("获取状态报告成功！获取到的状态报告有" + rpts.size() + "条记录。");
					for (int i = 0; i < rpts.size(); i++)
					{
						rpt = rpts.get(i);
						System.out.println("状态报告记录:"+"msgid:"+rpt.getMsgid() + ",custid:" + rpt.getCustid() + ",pknum:" + rpt.getPknum()
								+ ",pktotal:" + rpt.getPktotal() + ",mobile:" + rpt.getMobile() + ",spno:" + rpt.getSpno() 
								+ ",exno:" + rpt.getExno() + ",stime:" + rpt.getStime() + ",rtime:" + rpt.getRtime() 
								+ ",status:" + rpt.getStatus() + ",errcode:" + rpt.getErrcode() + ",exdata:" + rpt.getExdata()
								+ ",errdesc:"+rpt.getErrdesc());
					}
					
					// 继续循环
					continue;
				}
				else
				{
					//如果获取状态报告失败，则将错误码打印
					if(result!=0)
					{
						System.out.println("获取状态报告失败，错误码为:" + result);
					}
					
					// 没有状态报告，延时5秒以上
					try
					{
						Thread.sleep(5000L);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// 循环出现异常，暂停5秒
				try
				{
					Thread.sleep(5000L);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

}
