package com.ketty.provider_sms.util;

/**
 * @功能概要：个性化信息详情类
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 */
public class MultiMt
{
	// 短信接收的手机号
	private String	mobile;

	// 短信内容
	private String	content;

	// 扩展号
	private String	exno;

	// 用户自定义流水编号
	private String	custid;

	// 自定义扩展数据
	private String	exdata;
	
	//业务类型
	private String svrtype;

	public String getSvrtype()
	{
		return svrtype;
	}

	public void setSvrtype(String svrtype)
	{
		this.svrtype = svrtype;
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

}
