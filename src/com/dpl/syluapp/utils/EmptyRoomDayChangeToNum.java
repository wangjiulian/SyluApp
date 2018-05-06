package com.dpl.syluapp.utils;
/**
 * 
 * @author JUST玖
 *
 * 2015-3-17
 */
public class EmptyRoomDayChangeToNum {
	String num;
	String day;
	@Override
	public String toString() {
		return "dayChangeToNum [num=" + num + ", day=" + day + "]";
	}
	
	public void setNum(String num) {
		this.num = num;
	}
	
	
	public void setDay(String day) {
		this.day = day;
	}
	
	  public String dayChange(String swichday)
	  {
		  if(swichday.equals(day))
			  return num;
		  else  return null;
		  
		  
	  }

}
