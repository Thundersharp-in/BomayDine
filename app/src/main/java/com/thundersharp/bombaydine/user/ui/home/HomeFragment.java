package com.thundersharp.bombaydine.user.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.CategoryAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.TopsellingAdapter;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.bombaydine.user.ui.orders.RecentOrders;
import com.thundersharp.bombaydine.user.ui.scanner.QrScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    List<Object> data = new ArrayList<>();

    private ImageView qrcode;
    private TextView recentorders,allitemsview;
    private AllItemAdapter allItemAdapter;
    private RecyclerView horizontalScrollView, categoryRecycler,topsellingholder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDemoSlider = view.findViewById(R.id.slider);
        horizontalScrollView = view.findViewById(R.id.allitems);
        topsellingholder = view.findViewById(R.id.topsellingholder);
        qrcode = view.findViewById(R.id.qrcode);
        allitemsview = view.findViewById(R.id.allitemsview);
        horizontalScrollView.setHasFixedSize(true);
        recentorders = view.findViewById(R.id.recentorders);

        allitemsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllItemsActivity.class));
            }
        });

        recentorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(getActivity(), RecentOrders.class));
                }else{
                    Toast.makeText(getContext(), "Kindly login to see your recent orders.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QrScanner.class));
            }
        });


        categoryRecycler = view.findViewById(R.id.recentordcategoryholderer);
        categoryRecycler.setHasFixedSize(true);

        HashMap<String, String> dataq = new HashMap<>();

        for (int i = 0; i <= 5; i++) {
            dataq.clear();
            dataq.put("imageuri", "https://media.istockphoto.com/photos/butter-chicken-spicy-curry-meat-food-in-kadai-dish-on-dark-background-picture-id1127522313?k=6&m=1127522313&s=612x612&w=0&h=gJ71z63zFrd_aGsNH_9WeW2blSyGAt4Ja3Ya5ay_kdg=");
            dataq.put("name", "Butter Chicken");
            data.add(dataq);
        }

        allItemAdapter = new AllItemAdapter(data, getContext());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        //horizontalScrollView.setLayoutManager(gridLayoutManager);
        horizontalScrollView.setAdapter(allItemAdapter);



        List<Object> datac = new ArrayList<>();
        int fgi;

        for (int i = 0; i <= 5; i++) {
            fgi = i;

            if (String.valueOf(fgi).equals("0")) {
                //datac.clear();
                HashMap<String, String> datacat = new HashMap<>();

                datacat.put("imageuri", "https://www.thespruceeats.com/thmb/xveETWq0ADQy1vHvVsfEd8VJx6g=/2996x2000/filters:fill(auto,1)/butter-chicken-479366-hero-2-75d134ff86ee42bc85e34232dbb319bf.jpg");
                datacat.put("name", "Main course");
                datac.add(datacat);
            }else if (i == 1) {
                HashMap<String, String> datacat = new HashMap<>();

                datacat.put("imageuri", "https://i2.wp.com/runningonrealfood.com/wp-content/uploads/2018/01/gluten-free-vegan-everyday-healthy-rainbow-salad-Running-on-Real-Food-6-of-10.jpg");
                datacat.put("name", "Starters");
                datac.add(datacat);
            }

            else if (i == 2) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTopOh0jXO5zHnsfQ1nA3RhDEXLkdxNWKRJZg&usqp=CAU");
                datacat.put("name", "Biryani");
                datac.add(datacat);
            }
            else if (i == 3) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://bsmedia.business-standard.com/_media/bs/img/article/2019-09/12/full/1568274937-1296.jpg");
                datacat.put("name", "Accomplishments");
                datac.add(datacat);
            }

            else if (i == 4) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://www.englishclub.com/images/vocabulary/food/chinese/chinese-food.jpg");
                datacat.put("name", "Chinese");
                datac.add(datacat);
            }else if (i == 5) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://i0.wp.com/www.binginretreat.com/wp-content/uploads/2019/07/what-to-do-09.jpg?fit=683%2C683");
                datacat.put("name", "Extras");
                datac.add(datacat);
            }





        }

        CategoryAdapter categoryAdapter = new CategoryAdapter(datac, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        categoryRecycler.setLayoutManager(gridLayoutManager);
        categoryRecycler.setAdapter(categoryAdapter);

        ArrayList<String> listUrl = new ArrayList<>();

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/bombay-dine.appspot.com/o/4349754.jpg?alt=media&token=1f4c313b-d8dc-4316-a04a-8613710a1567");

        listUrl.add("https://thumbs.dreamstime.com/z/pizzeria-discount-banner-pepperoni-margherita-slices-italian-food-advertisement-pizza-tomatoes-mozzarella-ketchup-186048578.jpg");

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/bombay-dine.appspot.com/o/4349754.jpg?alt=media&token=1f4c313b-d8dc-4316-a04a-8613710a1567");

        listUrl.add("https://thumbs.dreamstime.com/z/pizzeria-discount-banner-pepperoni-margherita-slices-italian-food-advertisement-pizza-tomatoes-mozzarella-ketchup-186048578.jpg");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < listUrl.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(null)
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());

            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);
        dunny();

        return view;
    }

    public void dunny(){
        List<Object> datac = new ArrayList<>();
        int fgi;

        for (int i = 0; i <= 5; i++) {
            fgi = i;

            if (String.valueOf(fgi).equals("0")) {
                //datac.clear();
                HashMap<String, String> datacat = new HashMap<>();

                datacat.put("imageuri", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTopOh0jXO5zHnsfQ1nA3RhDEXLkdxNWKRJZg&usqp=CAU");
                datacat.put("name", "Dum Biriyani");
                datac.add(datacat);
            }else if (i == 1) {
                HashMap<String, String> datacat = new HashMap<>();

                datacat.put("imageuri", "https://previews.123rf.com/images/lblinova/lblinova1809/lblinova180900117/108167439-indian-food-rogan-josh-curry-sauce-pork-rogan-josh-with-rice-.jpg");
                datacat.put("name", "Rogan josh");
                datac.add(datacat);
            }

            else if (i == 2) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbbyCCITkO0CtqqwmykxgkYUiQXoc8gk_arQ&usqp=CAU");
                datacat.put("name", "Chole Bhature");
                datac.add(datacat);
            }
            else if (i == 3) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://i.pinimg.com/originals/0d/41/c0/0d41c048fc3b1d3fd83330926feda7db.jpg");
                datacat.put("name", "Chicken Roll");
                datac.add(datacat);
            }

            else if (i == 4) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://www.cookwithmanali.com/wp-content/uploads/2016/01/Chilli-Paneer-Restaurant-Style.jpg");
                datacat.put("name", "Paneer Chillli");
                datac.add(datacat);
            }else if (i == 5) {
                HashMap<String, String> datacat = new HashMap<>();
                datacat.put("imageuri", "https://thesimplemenu.com/wp-content/uploads/2020/06/IMG_6924-855x1024.jpg");
                datacat.put("name", "Set Dosa");
                datac.add(datacat);
            }





        }

        TopsellingAdapter categoryAdapter = new TopsellingAdapter(getContext(),datac);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        //topsellingholder.setLayoutManager(gridLayoutManager);
        topsellingholder.setAdapter(categoryAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}