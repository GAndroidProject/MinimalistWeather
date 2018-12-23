package com.baronzhang.android.weather;

import android.content.Intent;
import android.os.Bundle;

import com.baronzhang.android.weather.base.BaseActivity;
import com.baronzhang.android.library.util.system.StatusBarHelper;
import com.baronzhang.android.weather.feature.home.MainActivity;
import com.baronzhang.android.weather.data.db.CityDatabaseHelper;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;

import java.io.InvalidClassException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 */
public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.statusBarLightMode(this);
//  seachal annotation 把初始化这个耗时操作放在 io 进程中操作。
        Observable.just(initAppData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> gotoMainPage());
    }

    private void gotoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化应用数据
     */
    private String initAppData() {
        PreferenceHelper.loadDefaults();
        //TODO 测试，待删除
        if (PreferenceHelper.getSharedPreferences().getBoolean(WeatherSettings.SETTINGS_FIRST_USE.getId(), false)) {
            try {
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, "101020100");
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_FIRST_USE, false);
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
        CityDatabaseHelper.importCityDB();
//        seachal annotation 因为 just 中参数类型是 T， 也就是泛型。
        return null;
    }
}
