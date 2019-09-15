package com.my.baselib.lib.view.addressselector;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.my.baselib.R;
import com.my.baselib.lib.view.addressselector.listener.OnAddressSelectedListener;
import com.my.baselib.lib.view.addressselector.model.City;
import com.my.baselib.lib.view.addressselector.model.County;
import com.my.baselib.lib.view.addressselector.model.Province;
import com.my.baselib.lib.view.addressselector.model.Street;
import com.my.baselib.lib.view.addressselector.util.ListUtils;

import java.util.ArrayList;
import java.util.List;


public class AddressSelector implements AdapterView.OnItemClickListener {
    private static final int INDEX_TAB_PROVINCE = 0;
    private static final int INDEX_TAB_CITY = 1;
    private static final int INDEX_TAB_COUNTY = 2;
    private static final int INDEX_TAB_STREET = 3;

    private static final int INDEX_INVALID = -1;

    private static final int WHAT_PROVINCES_PROVIDED = 0;
    private static final int WHAT_CITIES_PROVIDED = 1;
    private static final int WHAT_COUNTIES_PROVIDED = 2;
    private static final int WHAT_STREETS_PROVIDED = 3;

    @SuppressWarnings("unchecked")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PROVINCES_PROVIDED:
                    provinces = (List<Province>) msg.obj;
                    provinceAdapter.notifyDataSetChanged();
                    listView.setAdapter(provinceAdapter);

                    break;

                case WHAT_CITIES_PROVIDED:
                    cities = (List<City>) msg.obj;
                    cityAdapter.notifyDataSetChanged();
                    if (ListUtils.notEmpty(cities)) {
                        // 以次级内容更新列表
                        listView.setAdapter(cityAdapter);
                        // 更新索引为次级
                        tabIndex = INDEX_TAB_CITY;

                        // 缓存省-市数据
                        long provinceId = cities.get(0).province_id;
                        if(!province2city.containsKey(provinceId)){
                            List<City> cityList = new ArrayList<>();
                            ListUtils.copy(cities, cityList);
                            province2city.put(provinceId, cityList);

//                            System.out.println("***** save !!!!!");
//                            System.out.println("cities = " + cities.toString());
                        }

                    } else {
                        // 次级无内容，回调
                        callbackInternal();
                    }

                    break;

                case WHAT_COUNTIES_PROVIDED:
                    counties = (List<County>) msg.obj;
                    countyAdapter.notifyDataSetChanged();
                    if (ListUtils.notEmpty(counties)) {
                        listView.setAdapter(countyAdapter);
                        tabIndex = INDEX_TAB_COUNTY;

                        // 缓存市-区数据
                        long cityId = counties.get(0).city_id;
                        if(!city2county.containsKey(cityId)){
                            List<County> countyList = new ArrayList<>();
                            ListUtils.copy(counties, countyList);
                            city2county.put(cityId, countyList);
                        }

                    } else {
                        callbackInternal();
                    }

                    break;

                case WHAT_STREETS_PROVIDED:
                    streets = (List<Street>) msg.obj;
                    streetAdapter.notifyDataSetChanged();
                    if (ListUtils.notEmpty(streets)) {
                        listView.setAdapter(streetAdapter);
                        tabIndex = INDEX_TAB_STREET;
                        // 缓存市-区数据
                        long countryId = streets.get(0).county_id;
                        if(!county2street.containsKey(countryId)){
                            List<Street> streetList = new ArrayList<>();
                            ListUtils.copy(streets, streetList);
                            county2street.put(countryId, streetList);
                        }
                    } else {
                        callbackInternal();
                    }

                    break;
            }

            updateTabsVisibility();
            updateProgressVisibility();
            updateIndicator();

            return true;
        }
    });


    private final Context context;
    private OnAddressSelectedListener listener;

    private View view;

    private View indicator;

    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private TextView textViewStreet;

    private ImageView cancelImg;
    private ProgressBar progressBar;

    private ListView listView;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountyAdapter countyAdapter;
    private StreetAdapter streetAdapter;

    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
    private List<Street> streets;

    /** 缓存数据:省-市 */
    private ArrayMap<Long, List<City>> province2city = new ArrayMap<>();
    /** 缓存数据:市-区 */
    private ArrayMap<Long, List<County>> city2county = new ArrayMap<>();
    /** 缓存数据:区-街道 */
    private ArrayMap<Long, List<Street>> county2street = new ArrayMap<>();

    private int provinceIndex = INDEX_INVALID;
    private int cityIndex = INDEX_INVALID;
    private int countyIndex = INDEX_INVALID;
    private int streetIndex = INDEX_INVALID;

    private int tabIndex = INDEX_TAB_PROVINCE;

    public AddressSelector(Context context) {
        this.context = context;

        initViews();
        initAdapters();
    }

    private void initAdapters() {
        provinceAdapter = new ProvinceAdapter();
        cityAdapter = new CityAdapter();
        countyAdapter = new CountyAdapter();
        streetAdapter = new StreetAdapter();
    }

    public void clearCacheData(){
        province2city.clear();
        city2county.clear();
        county2street.clear();

        // 清空子级数据
        provinces = null;
        cities = null;
        counties = null;
        streets = null;
        provinceAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();
        countyAdapter.notifyDataSetChanged();
        streetAdapter.notifyDataSetChanged();

        provinceIndex = INDEX_INVALID;
        cityIndex = INDEX_INVALID;
        countyIndex = INDEX_INVALID;
        streetIndex = INDEX_INVALID;

        tabIndex = INDEX_TAB_PROVINCE;
        textViewProvince.setText("请选择");
        updateTabsVisibility();
        updateProgressVisibility();
        updateIndicator();
    }

    private void initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.address_selector, null);

        this.cancelImg = (ImageView) view.findViewById(R.id.img_cancel);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        this.listView = (ListView) view.findViewById(R.id.listView);
        this.indicator = view.findViewById(R.id.indicator);

        this.textViewProvince = (TextView) view.findViewById(R.id.textViewProvince);
        this.textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        this.textViewCounty = (TextView) view.findViewById(R.id.textViewCounty);
        this.textViewStreet = (TextView) view.findViewById(R.id.textViewStreet);

        this.textViewProvince.setOnClickListener(new OnProvinceTabClickListener());
        this.textViewCity.setOnClickListener(new OnCityTabClickListener());
        this.textViewCounty.setOnClickListener(new OnCountyTabClickListener());
        this.textViewStreet.setOnClickListener(new OnStreetTabClickListener());
        this.cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelSelected();
            }
        });

        this.listView.setOnItemClickListener(this);

        updateIndicator();
        progressBar.setVisibility(View.VISIBLE);
    }

    public View getView() {
        return view;
    }

    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (tabIndex) {
                    case INDEX_TAB_PROVINCE:
                        buildIndicatorAnimatorTowards(textViewProvince).start();
                        break;
                    case INDEX_TAB_CITY:
                        buildIndicatorAnimatorTowards(textViewCity).start();
                        break;
                    case INDEX_TAB_COUNTY:
                        buildIndicatorAnimatorTowards(textViewCounty).start();
                        break;
                    case INDEX_TAB_STREET:
                        buildIndicatorAnimatorTowards(textViewStreet).start();
                        break;
                }
            }
        });
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX());

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }

    private class OnProvinceTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_PROVINCE;
            listView.setAdapter(provinceAdapter);

            if (provinceIndex != INDEX_INVALID) {
                listView.setSelection(provinceIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCityTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_CITY;
            listView.setAdapter(cityAdapter);

            if (cityIndex != INDEX_INVALID) {
                listView.setSelection(cityIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCountyTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_COUNTY;
            listView.setAdapter(countyAdapter);

            if (countyIndex != INDEX_INVALID) {
                listView.setSelection(countyIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnStreetTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_STREET;
            listView.setAdapter(streetAdapter);

            if (streetIndex != INDEX_INVALID) {
                listView.setSelection(streetIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private void updateTabsVisibility() {
        textViewProvince.setVisibility(ListUtils.notEmpty(provinces) ? View.VISIBLE : View.GONE);
        textViewCity.setVisibility(ListUtils.notEmpty(cities) ? View.VISIBLE : View.GONE);
        textViewCounty.setVisibility(ListUtils.notEmpty(counties) ? View.VISIBLE : View.GONE);
        textViewStreet.setVisibility(ListUtils.notEmpty(streets) ? View.VISIBLE : View.GONE);

        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
        textViewStreet.setEnabled(tabIndex != INDEX_TAB_STREET);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                Province province = provinceAdapter.getItem(position);

                // 更新当前级别及子级标签文本
                textViewProvince.setText(province.name);
                textViewCity.setText("请选择");
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");

                // 清空子级数据
                cities = null;
                counties = null;
                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();

                // 更新已选中项
                this.provinceIndex = position;
                this.cityIndex = INDEX_INVALID;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                // 更新选中效果
                provinceAdapter.notifyDataSetChanged();

                // 有缓存则直接使用缓存,否则去重新请求
                if(province2city.containsKey(province.id)){
                    setCities(province2city.get(province.id));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    listener.onProvinceSelected(province);
                }


                break;

            case INDEX_TAB_CITY:
                City city = cityAdapter.getItem(position);

                textViewCity.setText(city.name);
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");

                counties = null;
                streets = null;
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();

                this.cityIndex = position;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                cityAdapter.notifyDataSetChanged();
                System.out.println(city2county.toString());

                // 有缓存则直接使用缓存,否则去重新请求
                if(city2county.containsKey(city.id)){
                    System.out.println("cityId = " + city.id);
                    setCountries(city2county.get(city.id));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    listener.onCitySelected(city);
                }

                break;

            case INDEX_TAB_COUNTY:
                County county = countyAdapter.getItem(position);

                textViewCounty.setText(county.name);
                textViewStreet.setText("请选择");

                streets = null;
                streetAdapter.notifyDataSetChanged();

                this.countyIndex = position;
                this.streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();

                //有缓存则直接使用缓存,否则去重新请求
                if(county2street.containsKey(county.id)){
                    setStreets(county2street.get(county.id));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    listener.onCountySelected(county);
                }
                //callbackInternal();
                break;

            case INDEX_TAB_STREET:
                Street street = streetAdapter.getItem(position);

                textViewStreet.setText(street.name);

                this.streetIndex = position;

                streetAdapter.notifyDataSetChanged();

                callbackInternal();

                break;
        }

        updateTabsVisibility();
        updateIndicator();
    }

    /**
     * 地址选择完成时调用的方法
     */
    private void callbackInternal() {
        if (listener != null) {
            Province province = provinces == null || provinceIndex == INDEX_INVALID ? null : provinces.get(provinceIndex);
            City city = cities == null || cityIndex == INDEX_INVALID ? null : cities.get(cityIndex);
            County county = counties == null || countyIndex == INDEX_INVALID ? null : counties.get(countyIndex);
            Street street = streets == null || streetIndex == INDEX_INVALID ? null : streets.get(streetIndex);

            listener.onAddressSelected(province, city, county, street);
        }
    }

    private void updateProgressVisibility() {
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();
        progressBar.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);
    }

    private class ProvinceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return provinces == null ? 0 : provinces.size();
        }

        @Override
        public Province getItem(int position) {
            return provinces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Province item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = provinceIndex != INDEX_INVALID && provinces.get(provinceIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities == null ? 0 : cities.size();
        }

        @Override
        public City getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            City item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = cityIndex != INDEX_INVALID && cities.get(cityIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CountyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return counties == null ? 0 : counties.size();
        }

        @Override
        public County getItem(int position) {
            return counties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            County item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = countyIndex != INDEX_INVALID && counties.get(countyIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class StreetAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return streets == null ? 0 : streets.size();
        }

        @Override
        public Street getItem(int position) {
            return streets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Street item = getItem(position);
            holder.textView.setText(item.name);

            boolean checked = streetIndex != INDEX_INVALID && streets.get(streetIndex).id == item.id;
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    public OnAddressSelectedListener getOnAddressSelectedListener() {
        return listener;
    }

    /**
     * 设置回调接口
     * @param listener
     */
    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 设置省列表
     * @param provinces 省份列表
     */
    public void setProvinces(List<Province> provinces){
        handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, provinces));
    }

    /**
     * 设置市列表
     * @param cities 城市列表
     */
    public void setCities(List<City> cities){
        handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, cities));
    }

    /**
     * 设置区列表
     * @param countries 区/县列表
     */
    public void setCountries(List<County> countries){
        handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, countries));
    }

    /**
     * 设置街道列表
     * @param streets 街道列表
     */
    public void setStreets(List<Street> streets){
        handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, streets));
    }


    /**
     * 有地址数据的时候,直接设置地址选择器
     * @param provinces 省份列表
     * @param provinceIndex 当前省在列表中的位置
     * @param cities 当前省份的城市列表
     * @param cityIndex 当前城市在列表中的位置
     * @param countries 当前城市的区县列表
     * @param countyIndex 当前区县在列表中的位置
     */
    public void setAddressSelector(@NonNull List<Province> provinces, int provinceIndex, @NonNull List<City> cities, int cityIndex, @Nullable List<County> countries, int countyIndex){
        if(provinces == null || provinces.size() == 0){
            return;
        } else if(cities == null || cities.size() == 0){
            setProvinces(provinces, provinceIndex);
        } else {
            setProvinces(provinces, provinceIndex);
            setCities(cities, cityIndex);
            setCountries(countries, countyIndex);
        }
        refreshSelector();
    }

    /**
     * 有地址数据的时候,直接设置地址选择器
     * @param provinces 省份列表
     * @param provinceIndex 当前省在列表中的位置
     * @param cities 当前省份的城市列表
     * @param cityIndex 当前城市在列表中的位置
     * @param countries 当前城市的区县列表
     * @param countyIndex 当前区县在列表中的位置
     * @param streets 当前街道在列表中的位置
     * @param streetIndex 当前街道在列表中的位置
     */
    public void setAddressSelector(@NonNull List<Province> provinces, int provinceIndex, @NonNull List<City> cities, int cityIndex, @Nullable List<County> countries, int countyIndex, @Nullable List<Street> streets, int streetIndex){
        if(provinces == null || provinces.size() == 0){
            return;
        } else if(cities == null || cities.size() == 0){
            setProvinces(provinces, provinceIndex);
        } else {
            setProvinces(provinces, provinceIndex);
            setCities(cities, cityIndex);
            setCountries(countries, countyIndex);
            setStreets(streets, streetIndex);
        }
        refreshSelector();
    }

    /**
     * 隐藏loading
     */
    public void hideLoading(){
        progressBar.setVisibility(View.GONE);
    }




    private void setProvinces(List<Province> provinces, int position){
        if(provinces == null || provinces.size() == 0){
            return;
        }
        this.provinces = provinces;
        tabIndex = INDEX_TAB_PROVINCE;
        this.provinceIndex = position;
        Province province = this.provinces.get(position);
        textViewProvince.setText(province.name);

        listView.setAdapter(provinceAdapter);
        if (provinceIndex != INDEX_INVALID) {
            listView.setSelection(provinceIndex);
        }
    }

    private void setCities(List<City> cities, int position){
        if(cities == null || cities.size() == 0){
            return;
        }
        this.cities = cities;
        tabIndex = INDEX_TAB_CITY;
        this.cityIndex = position;
        City city = this.cities.get(position);
        textViewCity.setText(city.name);
        // 缓存省-市数据
        long provinceId = cities.get(0).province_id;
        if(!province2city.containsKey(provinceId)){
            List<City> cityList = new ArrayList<>();
            ListUtils.copy(cities, cityList);
            province2city.put(provinceId, cityList);
        }

        listView.setAdapter(cityAdapter);
        if (cityIndex != INDEX_INVALID) {
            listView.setSelection(cityIndex);
        }
    }

    private void setCountries(List<County> countries, int position){
        if(countries == null || countries.size() == 0){
            return;
        }
        this.counties = countries;
        tabIndex = INDEX_TAB_COUNTY;
        this.countyIndex = position;
        County county = this.counties.get(position);
        textViewCounty.setText(county.name);
        // 缓存市-区数据
        long cityId = counties.get(0).city_id;
        if(!city2county.containsKey(cityId)){
            List<County> countyList = new ArrayList<>();
            ListUtils.copy(counties, countyList);
            city2county.put(cityId, countyList);
        }

        listView.setAdapter(countyAdapter);
        if (countyIndex != INDEX_INVALID) {
            listView.setSelection(countyIndex);
        }

    }

    private void setStreets(List<Street> streets, int position){
        if(streets == null || streets.size() == 0){
            return;
        }
        this.streets = streets;
        tabIndex = INDEX_TAB_STREET;
        this.streetIndex = position;
        Street street = this.streets.get(position);
        textViewStreet.setText(street.name);
        // 缓存区-街道数据
        long countyId = streets.get(0).county_id;
        if(!county2street.containsKey(countyId)){
            List<Street> streetList = new ArrayList<>();
            ListUtils.copy(streets, streetList);
            county2street.put(countyId, streetList);
        }

        listView.setAdapter(streetAdapter);
        if (countyIndex != INDEX_INVALID) {
            listView.setSelection(streetIndex);
        }

    }

    /**
     * 刷新地址选择器
     */
    private void refreshSelector(){
        progressBar.setVisibility(View.GONE);
        updateTabsVisibility();
        updateIndicator();
    }

}
