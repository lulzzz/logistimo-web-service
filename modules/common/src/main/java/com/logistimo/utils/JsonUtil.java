/*
 * Copyright © 2017 Logistimo.
 *
 * This file is part of Logistimo.
 *
 * Logistimo software is a mobile & web platform for supply chain management and remote temperature monitoring in
 * low-resource settings, made available under the terms of the GNU Affero General Public License (AGPL).
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. To know more about
 * the commercial license, please contact us at opensource@logistimo.com
 */

/**
 *
 */
package com.logistimo.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Arun
 */
public class JsonUtil {

  // Get map from a JSON object; each Object value in the Map can be either String, List<String> or (recursively) Map<String,Object>
  @SuppressWarnings("unchecked")
  public static Map<String, Object> toMap(JSONObject json) throws JSONException {
    if (json == null) {
      return null;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    Iterator<String> keys = json.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      Object o = json.get(key);
      if (o instanceof JSONArray) {
        map.put(key, toList((JSONArray) o));
      } else if (o instanceof JSONObject) {
        map.put(key, toMap((JSONObject) o));
      } else {
        map.put(key, o);
      }
    }
    return map;
  }

  // Get a List from a JSON Array
  public static List<String> toList(JSONArray array) throws JSONException {
    if (array == null || array.length() == 0) {
      return null;
    }
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < array.length(); i++) {
      list.add(array.getString(i));
    }
    return list;
  }

  // Convert a map to a JSON object; each value in Map can be a String, List<String> or Map<String,Object>
  @SuppressWarnings("unchecked")
  public static JSONObject toJSON(Map<String, Object> map) throws JSONException {
    if (map == null || map.isEmpty()) {
      return null;
    }
    JSONObject json = new JSONObject();
    Iterator<String> it = map.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      Object o = map.get(key);
      if (o instanceof List) {
        json.put(key, toJSON((List<String>) o));
      } else if (o instanceof Map) {
        json.put(key, toJSON((Map<String, Object>) o));
      } else {
        json.put(key, o);
      }
    }
    return json;
  }

  // Convert a List to a JSONAray
  public static JSONArray toJSON(List<String> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }
    JSONArray array = new JSONArray();
    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
      array.put(it.next());
    }
    return array;
  }

  public static Map<String, Object> toMap(String json) {
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    return new GsonBuilder().create().fromJson(json, type);
  }
}
