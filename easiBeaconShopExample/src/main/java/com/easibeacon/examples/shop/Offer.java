/*
 * Copyright 2014 Easi Technologies and Consulting Services, S.L.
 *  
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.easibeacon.examples.shop;

public class Offer {
	
	private int _offerId;
	private String _description;
	private int _imageId;
	private String _discountTextBig;
	private String _discountTextSmall;
	private String _discountInfo;
	
	public Offer(){
	}

	public int getOfferId() {
		return _offerId;
	}

	public void setOfferId(int _offerid) {
		this._offerId = _offerid;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String _description) {
		this._description = _description;
	}

	public int getImageId() {
		return _imageId;
	}

	public void setImageId(int _imageId) {
		this._imageId = _imageId;
	}

	public String getDiscountTextBig() {
		return _discountTextBig;
	}

	public void setDiscountTextBig(String _discountTextBig) {
		this._discountTextBig = _discountTextBig;
	}

	public String getDiscountTextSmall() {
		return _discountTextSmall;
	}

	public void setDiscountTextSmall(String _discountTextSmall) {
		this._discountTextSmall = _discountTextSmall;
	}
	
	public String getDiscountInfo() {
		return _discountInfo;
	}

	public void setDiscountInfo(String _discountInfo) {
		this._discountInfo = _discountInfo;
	}

	public static Offer getSampleOffer1(){
		Offer o = new Offer();
		o._offerId = 1;
		o._description = "Brown heels for woman";
		o._discountTextBig = "10";
		o._discountTextSmall = "% off buying two pairs";
		o._discountInfo = "offer ends in 2 hours";
		o._imageId = R.drawable.offer1;
		return o;
	}

	public static Offer getSampleOffer2(){
		Offer o = new Offer();
		o._offerId = 1;
		o._description = "Red T-Shirt";
		o._discountTextBig = "20";
		o._discountTextSmall = "% off only today";
		o._discountInfo = "offer ends in 6 hours";
		o._imageId = R.drawable.offer2;
		return o;
	}

	public static Offer getSampleOffer3(){
		Offer o = new Offer();
		o._offerId = 1;
		o._description = "Leather purse";
		o._discountTextBig = "15";
		o._discountTextSmall = "\u20ac off only today";
		o._discountInfo = "get a scarf for free!";
		o._imageId = R.drawable.offer3;
		return o;
	}

}
