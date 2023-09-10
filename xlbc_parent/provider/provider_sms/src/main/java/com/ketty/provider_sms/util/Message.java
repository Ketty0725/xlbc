package com.ketty.provider_sms.util;

/**
 * @功能概要： 短信请求实体类
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 */
public class Message
{
	// 用户账号
	private String	userid;

	// 用户密码
	private String	pwd;

	//短信接收的手机号，用英文逗号(,)分隔，最大1000个号码。一次提交的号码类型不受限制，但手机会做验证，若有不合法的手机号将会被退回。号码段类型分为：移动、联通、电信手机
	// 注意：请不要使用中文的逗号
	private String	mobile;

	//最大支持350个字，一个字母或一个汉字都视为一个字
	private String	content;

	// 时间戳,格式为:MMDDHHMMSS,即月日时分秒,定长10位,月日时分秒不足2位时左补0.时间戳请获取您真实的服务器时间,不要填写固定的时间,否则pwd参数起不到加密作用
	private String	timestamp;

	// 扩展号
	// 长度由账号类型定4-6位，通道号总长度不能超过20位。如：10657****主通道号，3321绑定的扩展端口，主+扩展+子端口总长度不能超过20位。
	private String	exno;

	// 该条短信在您业务系统内的用户自定义流水编号，比如订单号或者短信发送记录的流水号。填写后发送状态返回值内将包含这个ID.最大可支持64位的字符串
	private String	custid;

	// 额外提供的最大64个长度的自定义扩展数据.填写后发送状态返回值内将会包含这部分数据
	private String	exdata;

	// 批量短信请求包。该字段中包含N个短信包结构体。N的范围为1~100。
	private String	multimt;	

	// 获取上行或者状态报告的最大条数
	private Integer	retsize;
	
	//业务类型
	private String svrtype;
	
	//apikey
	private String apikey;

	public String getSvrtype()
	{
		return svrtype;
	}

	public void setSvrtype(String svrtype)
	{
		this.svrtype = svrtype;
	}

	public String getApikey()
	{
		return apikey;
	}

	public void setApikey(String apikey)
	{
		this.apikey = apikey;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getExno()
	{
		return exno;
	}

	public void setExno(String exno)
	{
		this.exno = exno;
	}

	public String getCustid()
	{
		return custid;
	}

	public void setCustid(String custid)
	{
		this.custid = custid;
	}

	public String getExdata()
	{
		return exdata;
	}

	public void setExdata(String exdata)
	{
		this.exdata = exdata;
	}

	public String getMultimt()
	{
		return multimt;
	}

	public void setMultimt(String multimt)
	{
		this.multimt = multimt;
	}

	public Integer getRetsize()
	{
		return retsize;
	}

	public void setRetsize(Integer retsize)
	{
		this.retsize = retsize;
	}

}
