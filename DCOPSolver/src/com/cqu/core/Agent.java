package com.cqu.core;

import java.util.List;
import java.util.Map;

import com.cqu.util.CollectionUtil;

/**
 * 计算单位
 * @author CQU
 *
 */
public abstract class Agent extends QueueMessager{
	
	public final static int QUEUE_CAPACITY=50;
	
	protected int id;
	protected String name;
	protected int[] domain;
	
	protected int[] neighbours;
	protected int parent;
	protected int[] allParents;
	protected int[] pseudoParents;
	protected int[] allChildren;
	protected int[] children;
	protected int[] pseudoChildren;
	
	protected Map<Integer, int[]> neighbourDomains;
	protected Map<Integer, int[][]> constraintCosts;
	
	protected MessageMailer msgMailer;
	
	protected int valueIndex;
	
	public Agent(int id, String name, int[] domain) {
		super("Agent "+name, QUEUE_CAPACITY);
		this.id = id;
		this.name = name;
		this.domain=domain;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setNeibours(int[] neighbours, int parent, int[] children, int[] allParents, int[] allChildren, Map<Integer, int[]> neighbourDomains, Map<Integer, int[][]> constraintCosts)
	{
		this.neighbours=neighbours;
		this.parent=parent;
		this.children=children;
		this.allParents=allParents;
		this.allChildren=allChildren;
		if(this.allChildren!=null&&this.children!=null)
		{
			this.pseudoChildren=CollectionUtil.except(this.allChildren, this.children);
		}
		if(this.allParents!=null&&this.parent!=-1)
		{
			this.pseudoParents=CollectionUtil.except(this.allParents, new int[]{this.parent});
		}
		
		this.neighbourDomains=neighbourDomains;
		this.constraintCosts=constraintCosts;
	}
	
	public void setMessageMailer(MessageMailer msgMailer)
	{
		this.msgMailer=msgMailer;
	}
	
	public void sendMessage(Message msg)
	{
		msgMailer.addMessage(msg);
	}
	
	@Override
	protected void initRun() {
		// TODO Auto-generated method stub
		super.initRun();
		
		try {
			Thread.sleep(100);//延迟启动，让所有的Agent thread创建完成后再运行
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
	}
	
    public abstract void printResults(List<Map<String, Object>> results);
    
	public abstract String easyMessageContent(Message msg, Agent sender, Agent receiver);
	
	protected boolean isLeafAgent()
	{
		return this.children==null;
	}
	
	protected boolean isRootAgent()
	{
		return this.parent==-1;
	}
}
