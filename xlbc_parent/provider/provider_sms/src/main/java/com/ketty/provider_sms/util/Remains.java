package com.ketty.provider_sms.util;

/**
 * 
 * @功能概要：余额对象
 * @项目名称： JavaSmsSdk5.2
 * @初创作者： tanglili <jack860127@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2017-2-17 上午10:00:07
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class Remains
{
	//是否成功的标识   0:成功;其他则为错误码
	private int result=-310099;
	
	//计费类型  0:按条计费;1:按金额计费
	private int chargetype=0;
	
    //剩余条数  chargetype为1时,balance为0.
	private int balance=0;
	
	//剩余金额   chargetype为0时,money为0.
	private  String money="0";

	public Remains()
	{
		
	}
	
	public Remains(int result)
	{
		this.result=result;
	}
	
	public int getResult()
	{
		return result;
	}

	public void setResult(int result)
	{
		this.result = result;
	}

	public int getChargetype()
	{
		return chargetype;
	}

	public void setChargetype(int chargetype)
	{
		this.chargetype = chargetype;
	}

	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

	public String getMoney()
	{
		return money;
	}

	public void setMoney(String money)
	{
		this.money = money;
	}

}
