/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Deserializes json strings into objects. Date format are expected to be in
 * yyyy-MM-dd'T'HH:mm:s
 */
public class ExtJsJsonDeserializer {
	private static volatile Gson gson;
	
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new ExtJSDateDeserializer());
		gson = builder.create();
	}
	
	/**
	 * Convenience class to deserialize a json object that just contains an integer id
	 */
	public static class IdWrapper {
		/** The id. */
		public int id;
	}
	
	
	/**
	 * Deserialize a parameterized type.
	 *
	 * @param type the type
	 * @param json the json
	 * @return null of json is empty otherwise the parameterized type
	 */
	public static <T> T asType(Type type, String json) {
		if(StringUtil.isEmpty(json))
			return null;
		
		T result = gson.fromJson(json, type);
		
		return result;
	}
	
	private static class ExtJSDateDeserializer implements  JsonDeserializer<Date> {
		private static final String format = "yyyy-MM-dd'T'HH:mm:ss"; // UTC time
		
		@Override
		public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			try {
				DateFormat extJSDateFormat = new SimpleDateFormat(format);
				extJSDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				return extJSDateFormat.parse(arg0.getAsString());
			} catch (ParseException pe) {
				throw new JsonParseException(pe);
			}
		}
	}
}
