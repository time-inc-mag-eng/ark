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

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.ErrorMessage.Code;


/**
 * Serialize an object to json
 * Date types are formatted to yyyy-MM-dd'T'HH:mm:ss.SSS'Z using UTC as the timezone.
 * Enum types are serialized using the toString representation.
 */
public class ExtJsJsonSerializer {
	private static volatile Gson gson;
	
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapterFactory(new StringEnumTypeAdapterFactory());
		builder.registerTypeHierarchyAdapter(Date.class, new ISODateAdapter());
		gson = builder.create();
	}
	
	private static final String EXTJS_SUCCESS_PROPERTY = "success";
	private static final String EXTJS_AJAX_DATA_PROPERTY = "result";
	private static final String EXTJS_ERROR_PROPERTY = "error";
	
	private ExtJsJsonSerializer() {}
	
	/**
	 * Serialize an object to a json with a "success: true" property
	 * at the root.
	 * @param obj the obj to serialize
	 * @return the json
	 */
	public static String successWithNoBody(Object obj) {
		String jsonResult = gson.toJson(obj);
		
		JsonObject result = new JsonParser().parse(jsonResult).getAsJsonObject();
		result.addProperty(EXTJS_SUCCESS_PROPERTY, true);
		return result.toString();
	}
	
	
	/**
	 * Serialize an object to a json of the form
	 * {@literal {"success": true, "result": jsonBodyValue} }
	 * @param obj the obt to serialize
	 * @return a json
	 */
	public static String success(Object obj) {
		return generateJsonString(EXTJS_AJAX_DATA_PROPERTY, gson.toJson(obj), true);
	}
	
	/**
	 * A failure json of the form 
	 * {@literal {"success": false, "error": { "reason": "errorMsg", "errorCode": "errorCode" }} }
	 *
	 * @param errorMsg the error msg
	 * @param errorCode the error code
	 * @return the json
	 */
	public static String fail(String errorMsg, Code errorCode) {
		return fail(gson.toJson(new ErrorMessage(errorMsg, errorCode)));
	}
	
	/**
	 * A failure json of the form 
	 * {@literal {"success": false, "error": { "reason": "errorMsg", "errorCode": "errorCode", "customData": customError }} }
	 *
	 * @param errorMsg the error msg
	 * @param errorCode the error code
	 * @param customError the custom error
	 * @return the json
	 */
	public static String fail(String errorMsg, Code errorCode, Collection<?> customError) {
		return fail(gson.toJson(new ErrorMessage(errorMsg, errorCode, customError)));
	}
	
	private static String fail(String jsonBodyValue) {
		return generateJsonString(EXTJS_ERROR_PROPERTY, jsonBodyValue, false);
	}
	

	private static String generateJsonString(String jsonBodyName, String jsonBodyValue, boolean isSuccess) {	
		try {
			JsonElement bodyElement = new JsonParser().parse(jsonBodyValue);
			
			JsonObject response = new JsonObject();
			
			response.addProperty(EXTJS_SUCCESS_PROPERTY, isSuccess);
			response.add(jsonBodyName, bodyElement);
			
			return response.toString();	
		} catch(JsonParseException jpe) {
			throw new IllegalArgumentException("The json " + jsonBodyValue + " is invalid json string!");
		}
	}

	
	private static class StringEnumTypeAdapterFactory implements TypeAdapterFactory {
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			Class<T> rawType = (Class<T>) type.getRawType();
			
			if (!rawType.isEnum()) {
				return null;
			}

			final Map<String, T> stringToEnum = new HashMap<String, T>();
			for (T constant : rawType.getEnumConstants()) {
				stringToEnum.put(constant.toString(), constant);
			}

			return new TypeAdapter<T>() {
				public void write(JsonWriter out, T value) throws IOException {
					if (value == null) {
						out.nullValue();
					} else {
						out.value(value.toString());
					}
				}

				public T read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					} else {
						return stringToEnum.get(reader.nextString());
					}
				}
			};
		}
	}	
	
	private static class ISODateAdapter implements JsonSerializer<Date> {
	    @Override
	    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
	    	DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
	    	iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
	        return new JsonPrimitive(iso8601Format.format(src));
	    }
	}
}
