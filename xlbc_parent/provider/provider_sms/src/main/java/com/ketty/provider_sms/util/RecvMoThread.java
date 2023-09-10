package com.ketty.provider_sms.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 获取上行的线程
 */
public class RecvMoThread extends Thread
{
	// 用户账号
	private String	userid		= null;

	// 用户密码
	private String	pwd	= null;
	
	//密码是否加密   true：密码加密;false：密码不加密
	private boolean isEncryptPwd=true;
	
	// 每次请求想要获取上行的最大条数
	private int retsize = 0;

	// 日期格式定义
	private static SimpleDateFormat	sdf						= new SimpleDateFormat("MMddHHmmss");
	
	/**
	 * 
	 * @description    构造函数
	 * @param userid   用户账号
	 * @param pwd      密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 * @param retsize  每次请求想要获取上行的最大条数      			 
	 */
	public RecvMoThread(String userid, String pwd,boolean isEncryptPwd, int retsize)
	{
		// 用户账号
		this.userid = userid;
		// 用户密码
		this.pwd = pwd;
		//密码是否加密
		this.isEncryptPwd=isEncryptPwd;
		// 每次请求想要获取上行的最大条数
		this.retsize = retsize;
	}

	// 获取上行业务类
	private CHttpPost	cHttpPost	= new CHttpPost();

	/**
	 * 获取上行线程的方法
	 */
	@Override
	public void run()
	{
		//上行集合   本集合临时存储上行，需要将收到的上行保存在一个队列中，由另外一个线程去处理。
		List<MO> mos =new ArrayList<MO>();
		//处理后的密码
		String handlePwd=null;
		// 返回值
		int result = -310099;
		//上行对象声明
		MO mo = null;
		//时间戳声明
		String timestamp=null;
		// 循环调用获取上行的方法
		while(true)
		{
			try
			{
				// 初始化返回值
				result = -310099;
				// 清空上行集合中的对象
				mos.clear();
				
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
				
				// 调用获取上行接口
				result = cHttpPost.getMo(userid, handlePwd,timestamp,retsize, mos);
				// 如果获取上行成功，并且有上行
				if(result == 0&&mos != null && mos.size() > 0)
				{
					// 有上行
					// 添加到上行队列
					// add mo queue
					// 这里不要做复杂的耗时的处理，将收到的上行保存在一个队列中，由另外一个线程去处理。

					//代码示例是将上行信息打印出来
					System.out.println("获取上行成功！获取到的上行有" + mos.size() + "条记录。");
					for (int i = 0; i < mos.size(); i++)
					{
						mo = mos.get(i);
						System.out.println("上行记录:"+"msgid:"+mo.getMsgid() + ",mobile:" + mo.getMobile() + ",spno:" + mo.getSpno() 
								+ ",exno:" + mo.getExno() + ",rtime:" + mo.getRtime() + ",content:" + mo.getContent());
					}
					
					// 继续循环
					continue;
				}
				else
				{
					//如果获取上行失败，则将错误码打印
					if(result!=0)
					{
						System.out.println("获取上行失败，错误码为:" + result);
					}
					
					// 没有上行，延时5秒以上
					try
					{
						Thread.sleep(5000L);
					}
					catch (Exception e)
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
