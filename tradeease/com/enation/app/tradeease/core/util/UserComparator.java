package com.enation.app.tradeease.core.util;

import java.util.Comparator;

import com.enation.app.shop.core.model.CustomerFreight;

public class UserComparator implements Comparator<CustomerFreight> {

	@Override
	public int compare(CustomerFreight o1, CustomerFreight o2) {
		if(o1.getFreightprice()>o2.getFreightprice())
			   return 1;
			  else if(o1.getFreightprice()<o2.getFreightprice()){
			  return -1;
			  }else if(o1.getFreightprice()==o2.getFreightprice()){
			    return 0;
	       }
	        return -1;
	}

}
