package com.thundersharp.bombaydine.user.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Adapters.AllItemAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.CategoryAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.PlacesAutoCompleteAdapter;
import com.thundersharp.bombaydine.user.core.Adapters.TopsellingAdapter;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.AddressHelper;
import com.thundersharp.bombaydine.user.core.address.AddressLoader;
import com.thundersharp.bombaydine.user.core.location.PinCodeContract;
import com.thundersharp.bombaydine.user.core.location.PinCodeInteractor;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.bombaydine.user.ui.orders.RecentOrders;
import com.thundersharp.bombaydine.user.ui.scanner.QrScanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.thundersharp.bombaydine.user.ui.home.MainPage.navController;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener,
        PlacesAutoCompleteAdapter.ClickListener,
        PinCodeContract.onPinDatafetchListner,
        AddressLoader.onAddresLoadListner {

    /**
     * Slider layout and other ui components
     */
    private SliderLayout mDemoSlider;
    List<Object> data = new ArrayList<>();
    private CircleImageView profile;
    private ImageView qrcode;
    private TextView recentorders,allitemsview;
    private AllItemAdapter allItemAdapter;
    private RecyclerView horizontalScrollView, categoryRecycler,topsellingholder;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;

    /**
     * Pin code details from API
     */
    String pinCode;
    private RequestQueue mRequestQueue;
    private LinearLayout current_loc;
    private PinCodeInteractor pinCodeInteractor;

    /**
     *Address Listeners and helpers
     */
    private AddressHelper addressHelper;



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
        profile = view.findViewById(R.id.profile);
        current_loc = view.findViewById(R.id.current_loc);

        mRequestQueue = Volley.newRequestQueue(getContext());

        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));
        pinCodeInteractor = new PinCodeInteractor(getContext(),this);
        addressHelper = new AddressHelper(getActivity(),this);

        //recyclerView = (RecyclerView) view.findViewById(R.id.places_recycler_view);

        addressHelper.loaduseraddress();
        allitemsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllItemsActivity.class));
            }
        });

        current_loc.setOnClickListener(viewlocation ->{
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
            View bottomview = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,view.findViewById(R.id.botomcontainer));

            //recyclerView = bottomview.findViewById(R.id.places_recycler_view);
            EditText editText = (EditText) bottomview.findViewById(R.id.searchedit);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() == 6){
                        pinCodeInteractor.getdetailsfromPincode(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

/*
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Set the fields to specify which types of place data to
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(getActivity());
                    startActivityForResult(intent, 1);

                }
            });
*/

            /*mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAutoCompleteAdapter.setClickListener(this);
            recyclerView.setAdapter(mAutoCompleteAdapter);
            mAutoCompleteAdapter.notifyDataSetChanged();*/

            bottomSheetDialog.setContentView(bottomview);
            bottomSheetDialog.show();
        });

        profile.setOnClickListener(viewclick -> {
            navController.navigate(R.id.profile);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(getContext(),place.getLatLng().toString(),Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("ERROR",status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(getContext(), place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDataFetch(JSONObject obj) {
        String district = null,state = null,country = null,name = null;
        try {
            district = obj.getString("District");
             state = obj.getString("State");
             country = obj.getString("Country");
             name = obj.getString("Name");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(getContext(),"Details of pin code is : \n" + "District is : " + district + "\n" + "State : " +
                state + "\n" + "Country : " + country+"\nName : "+name,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDataFetchFailureListner(Exception e) {
        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddressLoaded(List<AddressData> addressData) {
        Toast.makeText(getActivity(), ""+addressData.get(0).getADDRESS_LINE1(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddressLoadFailure(Exception e) {

    }
}