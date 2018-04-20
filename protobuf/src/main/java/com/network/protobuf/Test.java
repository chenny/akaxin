package com.network.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class Test {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		// TODO Auto-generated method stub
		Akaxin.Request.Builder  arb  = Akaxin.Request.newBuilder();
		arb.addNumbers(12);
		arb.addNumbers(13);
		arb.addNumbers(14);
		arb.addNumbers(15);
		Akaxin.Request ar = arb.build();
		
		Akaxin.Request artemp = Akaxin.Request.parseFrom(ar.toByteArray());
		for(Integer i:artemp.getNumbersList()) {
			System.out.println(i);
			
		}
	}

}
