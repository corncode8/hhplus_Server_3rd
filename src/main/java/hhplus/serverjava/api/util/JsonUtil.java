package hhplus.serverjava.api.util;

import java.time.ZonedDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hhplus.serverjava.api.util.adapter.ZonedDateTimeAdapter;

public class JsonUtil {

	public static String toJson(Object obj) {
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
			.create();
		return gson.toJson(obj);
	}
}
