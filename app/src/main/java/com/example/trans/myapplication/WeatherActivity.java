package com.example.trans.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trans.myapplication.utils.GPSUtils;
import com.example.trans.myapplication.utils.NetUtils;
import com.example.trans.myapplication.utils.WeatherTestJson;
import com.example.trans.myapplication.utils.WeatherUtils;
import com.example.trans.myapplication.weatherEntity.Daily;
import com.example.trans.myapplication.weatherEntity.Weather;
import com.example.trans.myapplication.weatherEntity.WeatherCity;
import com.example.trans.myapplication.weatherEntity.Yin;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    private TextView tv_temperature;
    private TextView tv_locationCity, tv_weather_state, tv_quality_pm25, tv_winddirect, tv_humidity;
    private TextView tv_calendar, tv_lunar_calendar, tv_branch, tv_zodiac;
    private TextView tv_suitable, tv_avoid;
    private ImageView img_exit;
    private ListView lv_weathers;
    private final int ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE = 0x99;
    private final int MAX_ITEMS = 4;

    private final String Address = "home/v1/weather";
    private static final String TAG = "WeatherActivity";
    private WeatherCity weatherCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weather);
        initView();
        getIntentBundler();
        // Log.d(TAG, "onCreate: "+ WeatherTestJson.json);
        jsonXmlSuccess(WeatherTestJson.json);  //test
        dataBindView();
        bindClickListener();
    }

    //初始化标签
    private void initView() {
        tv_temperature = this.findViewById(R.id.tv_weather_tempera);
        img_exit = this.findViewById(R.id.img_weather_exit);

        tv_locationCity = this.findViewById(R.id.tv_weather_location_city);
        tv_winddirect = this.findViewById(R.id.tv_weather_winddirect);
        tv_weather_state = this.findViewById(R.id.tv_weather_state);
        tv_quality_pm25 = this.findViewById(R.id.tv_weather_quality_pm25);
        tv_humidity = this.findViewById(R.id.tv_weather_humidity);

        tv_calendar = this.findViewById(R.id.tv_weather_calendar);
        tv_lunar_calendar = this.findViewById(R.id.tv_weather_lunar_calendar);
        tv_branch = this.findViewById(R.id.tv_weather_branch);
        tv_zodiac = this.findViewById(R.id.tv_weather_zodiac);

        tv_suitable = this.findViewById(R.id.tv_weather_suitable);
        tv_avoid = this.findViewById(R.id.tv_weather_avoid);

        lv_weathers = this.findViewById(R.id.lv_weather_items);
    }

    //获取Intent中的值
    private void getIntentBundler() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            WeatherCity bundleWeatherCity = (WeatherCity) bundle.getSerializable(WeatherUtils.BUNDLE_WERTHERCITY);
            if (bundleWeatherCity != null) {
                Log.d(TAG, "getIntentBundler: bundleWeatherCity：" + bundleWeatherCity.toString());
                this.weatherCity = bundleWeatherCity;
            }
        }
    }


    //绑定点击事件
    private void bindClickListener() {
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean netResult = NetUtils.isNetworkAvailable(this);
        if (!netResult) {
            Toast.makeText(this, "请连接网络", Toast.LENGTH_SHORT).show();
        }

        checkPermission();
        //testlocation
       /* boolean result = GPSUtils.getInstance(this).checkPermission(WeatherActivity.this);
        Log.d(TAG, "onResume: result------"+result);
        if (result) {
            getLocation();
        }*/
    }

   //权限判断
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 用户拒绝过这个权限，应该提示用户。
                Log.d(TAG, "checkPermission: 拒绝过这个权限，提示，或者提示用户重新开启 ");
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE);
            }
        }else{
            Log.d(TAG, "checkPermission: ");
            getLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限同意;
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    getLocation();
                } else {
                    // 权限拒绝;
                    Toast.makeText(this, "定位请求失败", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    //获取定位
    private void getLocation() {
        //gps
        GPSUtils.getInstance(WeatherActivity.this).getLngAndLat(new GPSUtils.OnLocationResultListener() {
            double latitude;
            double longitude;

            @Override
            public void onLocationResult(Location location) {
                //gps
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(TAG, "onLocationResult: latitude:" + latitude);
            }

            @Override
            public void OnLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(TAG, "onLocationResult: latitude:" + latitude);
            }
        });
    }


    //绑定数据
    private void dataBindView() {

        if (weatherCity != null) {
            tv_temperature.setText(weatherCity.getTemp() + "°");

            tv_locationCity.setText(weatherCity.getCity());
            tv_winddirect.setText(weatherCity.getWinddirect() + this.getString(R.string.spance) + weatherCity.getWindpower());
            tv_weather_state.setText(weatherCity.getWeather());
            tv_quality_pm25.setText(weatherCity.getIpm2_5() + this.getString(R.string.spance) + weatherCity.getQuality());
            tv_humidity.setText(weatherCity.getHumidity() + "%");

            tv_calendar.setText(weatherCity.getDate() + this.getString(R.string.spance) + weatherCity.getWeek());

            Yin yin = weatherCity.getYin();
            String lunar_calendar = yin.getLunar_calendar();
            tv_lunar_calendar.setText(lunar_calendar);
            String zodiac = yin.getZodiac(); //生肖

            String branch = yin.getBranch();
            String[] branchs = branch.split(" ");
            Log.d(TAG, "dataBindView: Branchs" + branchs.toString());

            String yearStr = branchs[0];
            int index = branch.indexOf(this.getString(R.string.year));
            String tempYear = yearStr.substring(0, index);
            tv_branch.setText(tempYear + "【" + zodiac + "】" + yearStr.substring(index, index + 1));
            tv_zodiac.setText(branchs[1] + this.getString(R.string.spance) + branchs[2]);

            String suitable = yin.getSuitable();
            index = suitable.indexOf("】") + 1;
            suitable = suitable.substring(index);
            String[] suitables = suitable.split("　");
            Log.d(TAG, "dataBindView: suitables--" + suitable);
            tv_suitable.setText(suitables[0] + "  " + suitables[1] + "  " + suitables[2]); //宜：

            String avoid = yin.getAvoid();
            index = avoid.indexOf("】") + 1;
            avoid = avoid.substring(index);
            String[] avoids = avoid.split("　");
            Log.d(TAG, "dataBindView: avoids--" + avoid);
            tv_avoid.setText(avoids[0] + "  " + avoids[1] + "  " + avoids[2]); //忌

            ArrayList<Daily> weathers = weatherCity.getDaily();
            weathers.remove(0);
            lv_weathers.setAdapter(new weatherAdapter(weathers));
        } else {
            Log.d(TAG, "dataBindView: weatherCity is null");

        }
    }

    class weatherAdapter extends BaseAdapter {

        private ArrayList<Daily> weathers;

        public weatherAdapter(ArrayList<Daily> weathers) {
            this.weathers = weathers;
        }

        //arrayList是设置给ListView数据的集合
        @Override
        public int getCount() {
            return MAX_ITEMS;
        }

        @Override
        public Object getItem(int i) {
            return weathers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = View.inflate(WeatherActivity.this, R.layout.item_weather_day, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_week = view.findViewById(R.id.tv_weather_item_week);
                viewHolder.img_weather_icon = view.findViewById(R.id.img_weather_item_icon);
                viewHolder.tv_temperature_range = view.findViewById(R.id.tv_weather_item_temperature_range);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Daily daily = weathers.get(i);

            //给ViewHolder设置值，即给ListView的各个组件设置值
            viewHolder.tv_week.setText(daily.getWeek());

            InputStream is = getResources().openRawResource(WeatherUtils.getDrawbleByImgPath(daily.getDay().getImg()));
            Bitmap mBitmap = BitmapFactory.decodeStream(is);
            viewHolder.img_weather_icon.setImageBitmap(mBitmap);
            String templow = daily.getNight().getTemplow();
            String tempHigh = daily.getDay().getTemphigh();
            viewHolder.tv_temperature_range.setText(templow + "°~" + tempHigh + "°");
            return view;
        }

        //为了优化ListView不可避免的使用了ViewHolder来复用
        class ViewHolder {
            TextView tv_week;
            ImageView img_weather_icon;
            TextView tv_temperature_range;
        }
    }

    //数据返回成功
    private void jsonXmlSuccess(String responseJson) {
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            int code = jsonObject.optInt("code");
            String message = jsonObject.optString("message");
            String jsonData = jsonObject.optString("data");
            Log.d(TAG, "jsonXmlSuccess: " + code + "---" + message);
            Log.d(TAG, "jsonXmlSuccess: jsonData:" + jsonData);
            if (code == 1000) {
                getWeatherCityByJson(jsonData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //数据解析
    private void getWeatherCityByJson(String jsonData) {
        weatherCity = WeatherUtils.getWeatherCityByScuessJson(jsonData);
    }


    @Override
    protected void onDestroy() {
      //  GPSUtils.getInstance(this).removeListener();
        super.onDestroy();
    }
}
