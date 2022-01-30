package com.gleickapps.weather.ui;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gleickapps.weather.R;
import com.gleickapps.weather.Utils.ParseUtils;
import com.gleickapps.weather.model.Weather;

import java.io.IOException;
import java.util.Locale;

public class WeatherActivity  extends AppCompatActivity {

    private TextView tvCity, tvTemperature, tvDescription, tvHumidity, tvWindSpeed;
    private ImageView ivWeatherIcon;

    private int citiesListPosition;

    private String[] units = {"metric", "º C", " %", " m/s"};
    private String lang = "en";

    // Atributos de áudio
    SoundPool soundPool;
    AudioAttributes audioAttributes;
    AssetManager am;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // A key é gerada pela MainActivity com a posição da RecyclerView
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            citiesListPosition = extras.getInt("key");
        }

        // Se estiver no sistema imperial
        if (!MainActivity.isMetric) {
            units[0] = "imperial";
            units[1] = "º F";
            units[2] = " %";
            units[3] = " mph";
        }

        // Se o idioma do sistema for português, os dados do clima serão em português também
        String systemLang = Locale.getDefault().getLanguage();
        if (systemLang.equals("pt")) lang = "pt";

        initViews();
        prepareAudio();
        prepareConnection();
    }

    private void initViews() {
        tvCity = findViewById(R.id.tv_city);
        tvTemperature = findViewById(R.id.tv_temperature);
        ivWeatherIcon = findViewById(R.id.iv_weather_icon);
        tvDescription = findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
    }

    private void prepareAudio() {
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(9)
                .setAudioAttributes(audioAttributes)
                .build();
        am = this.getAssets();
    }

    /**
     * Pega o código id da cidade e chama a API
     */
    private void prepareConnection() {
        Resources res = this.getResources();
        String[] codes = res.getStringArray(R.array.city_id);

        try {
            String code = codes[citiesListPosition];

            GetJson download = new GetJson(code);

            //Chama Async Task
            download.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String name) {
        try {
            int sound = soundPool.load(am.openFd("sounds/"+name+".ogg"), 1);

            // O áudio demora um pouco para ficar pronto e poder tocar
            soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) ->
                    soundPool.play(
                            sound, MainActivity.volume, MainActivity.volume,
                            0, 0, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(String name) {
        int id = this.getResources()
                .getIdentifier(name, "drawable", this.getPackageName());

        ivWeatherIcon.setImageResource(id);
    }

    /**
     * Utiliza o código de ícone retornado pela API para definir a imagem do clima e o som
     * @param icon String código do ícone do clima
     */
    private void condition(String icon) {
        switch (icon) {
            case "01d":
            case "01n":
                setIconAndPlay("clear_sky");
                break;
            case "02d":
            case "02n":
                setIconAndPlay("few_clouds");
                break;
            case "03d":
            case "03n":
                setIconAndPlay("scattered_clouds");
                break;
            case "04d":
            case "04n":
                setIconAndPlay("broken_clouds");
                break;
            case "09d":
            case "09n":
                setIconAndPlay("shower_rain");
                break;
            case "10d":
            case "10n":
                setIconAndPlay("rain");
                break;
            case "11d":
            case "11n":
                setIconAndPlay("thunderstorm");
                break;
            case "13d":
            case "13n":
                setIconAndPlay("snow");
                break;
            case "50d":
            case "50n":
                setIconAndPlay("mist");
                break;
        }
    }

    private void setIconAndPlay(String name) {
        playSound(name);
        setWeatherIcon(name);
    }

    /**
     * Um html com variáveis é usado para chamar a API e depois exibir os dados
     */
    private class GetJson extends AsyncTask<Void, Void, Weather> {

        String code;

        public GetJson(String code) {
            this.code = code;
        }

        @Override
        protected Weather doInBackground(Void... params) {
            ParseUtils util = new ParseUtils();

            return util.getInfo("https://api.openweathermap.org/data/2.5/weather?" +
                    "id=" + code + "&units=" + units[0] + "&lang=" + lang
                    + "&appid=4b0d06b5527951918c2d3f6708fff2da");
        }

        @Override
        protected void onPostExecute(Weather weather){
            tvCity.setText(weather.getCity());
            tvDescription.setText(weather.getDescription());
            tvHumidity.setText(weather.getHumidity().concat(units[2]));
            tvWindSpeed.setText(weather.getWindSpeed().concat(units[3]));

            // Se a temperatura possuir casas decimais, será retornado somente a parte inteira
            if (weather.getTemperature().contains(".")) {
                int end = weather.getTemperature().indexOf(".");
                tvTemperature.setText(weather.getTemperature().substring(0, end).concat(units[1]));
            } else {
                tvTemperature.setText(weather.getTemperature().concat(units[1]));
            }

            condition(weather.getIcon());
        }
    }
}
