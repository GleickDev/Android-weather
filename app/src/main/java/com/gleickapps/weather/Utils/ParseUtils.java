package com.gleickapps.weather.Utils;

import com.gleickapps.weather.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ParseUtils {

    public Weather getInfo(String end){
        String json = null;
        Weather result;
        try {
            json = NetworkUtils.getJSONFromAPI(end);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = parseJson(json);

        return result;
    }

    private Weather parseJson(String json){
        try {
            Weather weather = new Weather();

            JSONObject jsonObj = new JSONObject(json);

            weather.setCity(jsonObj.getString("name"));
            weather.setTemperature(jsonObj.getJSONObject("main").getString("temp"));
            weather.setIcon(jsonObj.getJSONArray("weather")
                    .getJSONObject(0).getString("icon"));
            weather.setDescription(jsonObj.getJSONArray("weather")
                    .getJSONObject(0).getString("description"));
            weather.setHumidity(jsonObj.getJSONObject("main").getString("humidity"));
            weather.setWindSpeed(jsonObj.getJSONObject("wind").getString("speed"));

            return weather;

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
