package com.ketty.provider_sms.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @功能概要：调用DEMO
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 */
public class Test
{
	// 日期格式定义
	private static SimpleDateFormat	sdf	= new SimpleDateFormat("MMddHHmmss");
	
	public static void main(String[] args)
	{
		// 用户账号
		String userid = "KUKU02";
		// 用户密码
		String pwd = "123456";
		
		//主IP信息  必填
		String masterIpAddress="192.169.3.223:8089";
		//备IP1  选填
		String ipAddress1="192.169.3.225:8088";
		//备IP2  选填
		String ipAddress2=null;
		//备IP3  选填
		String ipAddress3=null;
		//设置IP
		ConfigManager.setIpInfo(masterIpAddress, ipAddress1, ipAddress2, ipAddress3);
		
		//密码是否加密   true：密码加密;false：密码不加密
		ConfigManager.IS_ENCRYPT_PWD=true;
		boolean isEncryptPwd= ConfigManager.IS_ENCRYPT_PWD;

		// 单条发送
		singleSend(userid, pwd,isEncryptPwd);

		// 相同内容群发
		batchSend(userid, pwd,isEncryptPwd);

		// 个性化群发
		multiSend(userid, pwd,isEncryptPwd);

		// 查询余额
		getBalance(userid, pwd,isEncryptPwd);
		
		// 查询剩余金额或条数
		getRemains(userid, pwd,isEncryptPwd);
		
		// 每次请求想要获取的上行的最大条数。
		int retsizeMo = 100;
		//实例化获取上行线程
		RecvMoThread recvMoThread=new RecvMoThread(userid, pwd,isEncryptPwd,retsizeMo);
		//线程获取 启动获取上行的线程 
		recvMoThread.start();
		System.out.println("获取上行的线程启动成功！");
		
		// 每次请求想要获取的状态报告的最大条数。
		int retsizeRpt = 100;
		//实例化状态报告线程
		RecvRptThread recvRptThread=new RecvRptThread(userid, pwd,isEncryptPwd,retsizeRpt);
		//线程获取 启动获取状态报告的线程
		recvRptThread.start();
		System.out.println("获取状态报告的线程启动成功！");
		
	    //清除所有IP (此处为清除IP示例代码，如果需要修改IP，请先清除IP，再设置IP)
		ConfigManager.removeAllIpInfo();

	}
	
	/**
	 * 
	 * @description  单条发送  
	 * @param userid  用户账号
	 * @param pwd 用户密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 */
	public static void singleSend(String userid, String pwd,boolean isEncryptPwd)
	{
		try
		{
			// 参数类
			Message message = new Message();
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			
			// 设置账号   将 userid转成大写,以防大小写不一致
			message.setUserid(userid.toUpperCase());
			
			//判断密码是否加密。
			//密码加密，则对密码进行加密
			if(isEncryptPwd)
			{
				// 设置时间戳
				String timestamp = sdf.format(Calendar.getInstance().getTime());
				message.setTimestamp(timestamp);
				
				// 对密码进行加密
				String encryptPwd = cHttpPost.encryptPwd(message.getUserid(),pwd, message.getTimestamp());
				// 设置加密后的密码
				message.setPwd(encryptPwd);
				
			}else
			{
				// 设置密码
				message.setPwd(pwd);
			}
			
			// 设置手机号码 此处只能设置一个手机号码
			message.setMobile("159XXXXXXXX");
			// 设置内容
			message.setContent("测试短信");
			// 设置扩展号
			message.setExno("11");
			// 用户自定义流水编号
			message.setCustid("20160929194950100001");
			// 自定义扩展数据
			message.setExdata("abcdef");
			//业务类型
			message.setSvrtype("SMS001");

			// 返回的平台流水编号等信息
			StringBuffer msgId = new StringBuffer();
			// 返回值
			int result = -310099;
			// 发送短信
			result = cHttpPost.singleSend(message, msgId);
			// result为0:成功;非0:失败
			if(result == 0)
			{
				System.out.println("单条发送提交成功！");

				System.out.println(msgId.toString());

			}
			else
			{
				System.out.println("单条发送提交失败,错误码：" + result);
			}
		}
		catch (Exception e)
		{
			//异常处理
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description  相同内容群发
	 * @param userid  用户账号
	 * @param pwd 用户密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 */
	public static void batchSend(String userid, String pwd,boolean isEncryptPwd)
	{
		try
		{
			// 参数类
			Message message = new Message();
			
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			
			// 设置账号   将 userid转成大写,以防大小写不一致
			message.setUserid(userid.toUpperCase());
			
			//判断密码是否加密。
			//密码加密，则对密码进行加密
			if(isEncryptPwd)
			{
				// 设置时间戳
				String timestamp = sdf.format(Calendar.getInstance().getTime());
				message.setTimestamp(timestamp);
				
				// 对密码进行加密
				String encryptPwd = cHttpPost.encryptPwd(message.getUserid(),pwd, message.getTimestamp());
				// 设置加密后的密码
				message.setPwd(encryptPwd);
				
			}else
			{
				// 设置密码
				message.setPwd(pwd);
			}
			
			// 设置手机号码
			message.setMobile("159XXXXXXXX,139XXXXXXXX");
			// 设置内容
			message.setContent("测试短信");
			// 设置扩展号
			message.setExno("11");
			// 用户自定义流水编号
			message.setCustid("20160929194950100001");
			// 自定义扩展数据
			message.setExdata("abcdef");
			//业务类型
			message.setSvrtype("SMS001");

			// 返回的平台流水编号等信息
			StringBuffer msgId = new StringBuffer();
			// 返回值
			int result = -310099;
			// 发送短信
			result = cHttpPost.batchSend(message, msgId);
			// result为0:成功;非0:失败
			if(result == 0)
			{
				System.out.println("相同内容发送提交成功！");

				System.out.println(msgId.toString());
			}
			else
			{
				System.out.println("相同内容发送提交失败,错误码：" + result);
			}
		}
		catch (Exception e)
		{
			//异常处理
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description  个性化群发
	 * @param userid  用户账号
	 * @param pwd 用户密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 */
	public static void multiSend(String userid, String pwd,boolean isEncryptPwd)
	{
		try
		{
			// List集合
			List<MultiMt> multixMts = new ArrayList<MultiMt>();
			// 第一条 短信对象
			MultiMt multixMt1 = new MultiMt();
			// 手机号
			multixMt1.setMobile("159XXXXXXXX");
			// 短信内容
			multixMt1.setContent("测试短信1");
			// 设置扩展号
			multixMt1.setExno("11");
			// 用户自定义流水编号
			multixMt1.setCustid("20160929194950100001");
			// 自定义扩展数据
			multixMt1.setExdata("abcdef");
			//业务类型
			multixMt1.setSvrtype("SMS001");
			// 添加短信对象
			multixMts.add(multixMt1);

			// 第二条 短信对象
			MultiMt multixMt2 = new MultiMt();
			// 手机号
			multixMt2.setMobile("139XXXXXXXX");
			// 短信内容
			multixMt2.setContent("测试短信2");
			// 设置扩展号
			multixMt2.setExno("11");
			// 用户自定义流水编号
			multixMt2.setCustid("20160929194950100001");
			// 自定义扩展数据
			multixMt2.setExdata("abcdef");
			//业务类型
			multixMt2.setSvrtype("SMS001");
			// 添加短信对象
			multixMts.add(multixMt2);

			// 返回的流水号
			StringBuffer msgId = new StringBuffer();
			// 返回值
			int result = -310099;
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			
			// 将 userid转成大写,以防大小写不一致
			userid = userid.toUpperCase();
			//判断密码是否加密。
			//密码加密，则对密码进行加密
			String timestamp=null;
			if(isEncryptPwd)
			{
				// 设置时间戳
			    timestamp = sdf.format(Calendar.getInstance().getTime());
				
				// 对密码进行加密
				pwd = cHttpPost.encryptPwd(userid,pwd, timestamp);
			}else
			{
				//不加密，不需要设置时间戳
				timestamp=null;
			}
			
			// 发送短信
			result = cHttpPost.multiSend(userid, pwd,timestamp,multixMts, msgId);
			// result为0:成功;非0:失败
			if(result == 0)
			{
				System.out.println("个性化群发提交成功！");
				System.out.println(msgId.toString());
			}
			else
			{
				System.out.println("个性化群发提交失败,错误码：" + result);
			}
		}
		catch (Exception e)
		{
			//异常处理
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @description  查询余额
	 * @param userid  用户账号
	 * @param pwd 用户密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 */
	public static void getBalance(String userid, String pwd,boolean isEncryptPwd)
	{
		// 返回值
		int result =-310099;
		try
		{
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			
			// 将 userid转成大写,以防大小写不一致
			userid = userid.toUpperCase();
			//判断密码是否加密。
			//密码加密，则对密码进行加密
			String timestamp=null;
			if(isEncryptPwd)
			{
				// 设置时间戳
			    timestamp = sdf.format(Calendar.getInstance().getTime());
				
				// 对密码进行加密
				pwd = cHttpPost.encryptPwd(userid,pwd, timestamp);
			}else
			{
				//不加密，不需要设置时间戳
				timestamp=null;
			}
			
			// 调用查询余额的方法查询余额
			result = cHttpPost.getBalance(userid, pwd,timestamp);
			// 返回值大于等于0:查询成功;小于0:查询失败
			if(result >= 0)
			{
				System.out.println("查询余额成功，余额为：" + result);
			}
			else
			{
				System.out.println("查询余额失败，错误码为：" + result);
			}
		}
		catch (Exception e)
		{
			//异常处理
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @description  查询剩余金额或条数接口
	 * @param userid  用户账号
	 * @param pwd 用户密码
	 * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
	 */
	public static void getRemains(String userid, String pwd,boolean isEncryptPwd)
	{
		// 返回值
		Remains remains=new Remains(-310099);
		try
		{
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			
			// 将 userid转成大写,以防大小写不一致
			userid = userid.toUpperCase();
			//判断密码是否加密。
			//密码加密，则对密码进行加密
			String timestamp=null;
			if(isEncryptPwd)
			{
				// 设置时间戳
			    timestamp = sdf.format(Calendar.getInstance().getTime());
				
				// 对密码进行加密
				pwd = cHttpPost.encryptPwd(userid,pwd, timestamp);
			}else
			{
				//不加密，不需要设置时间戳
				timestamp=null;
			}
			
			// 调用查询余额的方法查询余额
			remains = cHttpPost.getRemains(userid, pwd,timestamp);
			
			//remains不为空
			if(remains!=null)
			{
				//查询成功
				if(remains.getResult()==0)
				{
					//计费类型为0，条数计费
					if(remains.getChargetype()==0)
					{
						System.out.println("查询成功,剩余条数为：" + remains.getBalance());
					}else if(remains.getChargetype()==1)
					{
						//计费类型为1，金额计费
						System.out.println("查询成功,剩余金额为：" + remains.getMoney());
					}else
					{
						System.out.println("未知的计费类型,计费类型:"+remains.getChargetype());
					}
				}
				else
				{
					//查询失败
					System.out.println("查询失败,错误码为：" + remains.getResult());
				}
			}else
			{
				System.out.println("查询失败。");
			}
		}
		catch (Exception e)
		{
			//异常处理
			e.printStackTrace();
		}

	}
}
