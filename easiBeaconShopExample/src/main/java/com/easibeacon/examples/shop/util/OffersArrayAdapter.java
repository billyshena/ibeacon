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

package com.easibeacon.examples.shop.util;

import java.util.ArrayList;

import com.easibeacon.examples.shop.Offer;
import com.easibeacon.examples.shop.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OffersArrayAdapter extends ArrayAdapter<Offer> {

	private Context context;
	private ArrayList<Offer> values;
	private LayoutInflater inflater;
	private static LruCache<String, Bitmap> _bitmapCache;

	
	public class CustomListItem {
		TextView txtProductDescription;
		TextView txtDiscountTextBig;
		TextView txtDiscountTextSmall;
		TextView txtOfferInfo;
		ImageView imgProduct;
		ProgressBar barLoadProductImage;
	}
	
	public OffersArrayAdapter(Context context, ArrayList<Offer> commandsList) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.offer_list_item, commandsList);
		this.context = context;
		values = new ArrayList<Offer>();
		values.addAll(commandsList);
		inflater = LayoutInflater.from(this.context);
		if(_bitmapCache == null)
			_bitmapCache = new LruCache<String, Bitmap>(5);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		CustomListItem myListItem;
		
		Offer offer = getItem(position);
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.offer_list_item, parent, false);
			myListItem = new CustomListItem();
			
			myListItem.txtProductDescription = (TextView) convertView.findViewById(R.id.txtProductDescription);
			myListItem.txtDiscountTextBig = (TextView) convertView.findViewById(R.id.txtOfferBig);
			myListItem.txtDiscountTextSmall = (TextView) convertView.findViewById(R.id.txtOfferMedium);
			myListItem.txtOfferInfo = (TextView) convertView.findViewById(R.id.txtOfferInfo);
			myListItem.imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
			myListItem.barLoadProductImage = (ProgressBar) convertView.findViewById(R.id.barLoadProductImage);
			
			convertView.setTag(myListItem);
		} else {
			myListItem = (CustomListItem) convertView.getTag();
		}
		
		myListItem.txtProductDescription.setText(offer.getDescription());
		myListItem.txtDiscountTextBig.setText(offer.getDiscountTextBig());
		myListItem.txtDiscountTextSmall.setText(offer.getDiscountTextSmall());
		myListItem.txtOfferInfo.setText(offer.getDiscountInfo());
		myListItem.barLoadProductImage.setVisibility(View.GONE);
		myListItem.imgProduct.setVisibility(View.VISIBLE);
		myListItem.imgProduct.setImageResource(offer.getImageId());
		
		return convertView;
	}


}
