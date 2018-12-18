package com.services.testmeli;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.services.testmeli.Api.ApiServices;
import com.services.testmeli.Api.ApiInterface;
import com.services.testmeli.Model.Attributes;
import com.services.testmeli.Model.Item;
import com.services.testmeli.Model.Pictures;
import com.services.testmeli.Adapter.AttributesItemAdapter;
import com.services.testmeli.Utils.Utils;
import com.services.testmeli.Adapter.ImageItemViewPagerAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    //Variables
    private TextView textViewTitleItem, textViewCounterImageItem, textViewOriginalPriceItem,
            textViewActualPriceItem, textViewPriceOffItem, textViewConditionsItem,
            textViewAvailableQuantity, textViewSoldQuantity, textViewDescriptionItem,
            textViewErrorTitleMessage, textViewErrorAdviceMessage, textViewActionReload ;
    private ImageView imageViewErrorMessage;
    private RelativeLayout layoutMessageErrorNotification;
    private ViewPager viewPagerSlideImageItem;
    private ProgressBar progresbarLoadDetailItem;
    private ListView listViewAtributesItem;
    private Item itemDetailFound;
    private String TAG, stringIdSearchItems, stringNameSearchItems, errorCodeMessage;
    private Integer intOffsetPagination;
    private AttributesItemAdapter attributesItemAdapter;
    private ImageItemViewPagerAdapter viewPagerAdapterImgItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        initVariables();

        Intent intentRetrieveValueSearch = getIntent();
        Bundle bundleValueSearch = intentRetrieveValueSearch .getExtras();

        if(bundleValueSearch != null) {
            stringIdSearchItems = bundleValueSearch.getString("id_searchItem");
            stringNameSearchItems = bundleValueSearch.getString("name_searchItem");
            if(stringIdSearchItems.length() >0){
                if(Utils.isOnline(this)==true){
                    consumeApiServices(stringIdSearchItems);
                    this.setTitle(stringNameSearchItems);
                }else{
                    errorMessageScreen("network");
                }
            }else{
                errorMessageScreen("unknownError");
            }
        } else{
            if(Utils.isOnline(this)==true){
                consumeApiServices("");
            }else{
                errorMessageScreen("network");
            }

        }


    }


    //Init Variables
    private void initVariables(){
        TAG = ItemDetailsActivity.class.getSimpleName();
        textViewCounterImageItem = (TextView) findViewById(R.id.textViewCountImageItem);
        textViewTitleItem = (TextView) findViewById(R.id.textViewTitleItem);
        textViewOriginalPriceItem = (TextView) findViewById(R.id.textViewOriginalPriceItem);
        textViewConditionsItem = (TextView) findViewById(R.id.textViewConditionsItem);
        textViewActualPriceItem = (TextView) findViewById(R.id.textViewActualPriceItem);
        textViewPriceOffItem = (TextView) findViewById(R.id.textViewPriceOffItem);
        textViewAvailableQuantity = (TextView) findViewById(R.id.textViewAvailableQuantity);
        textViewSoldQuantity = (TextView) findViewById(R.id.textViewSoldQuantity);
        textViewDescriptionItem = (TextView) findViewById(R.id.textViewDescriptionItem);
        viewPagerSlideImageItem = (ViewPager) findViewById(R.id.viewPagerSlideImageItem);
        progresbarLoadDetailItem = (ProgressBar) findViewById(R.id.progresbarLoadItemsFound);
        listViewAtributesItem = (ListView) findViewById(R.id.listViewAtributesItem);
        layoutMessageErrorNotification = (RelativeLayout) findViewById(R.id.layoutMessageErrorNotification);
        textViewErrorTitleMessage = (TextView) findViewById(R.id.textViewErrorTitleMessage);
        textViewErrorAdviceMessage = (TextView) findViewById(R.id.textViewErrorAdviceMessage);
        textViewActionReload = (TextView) findViewById(R.id.textViewActionReload);
        imageViewErrorMessage = (ImageView) findViewById(R.id.imageViewErrorMessage);
        itemDetailFound = new Item();
        intOffsetPagination=0;
    }


    //Load Search Box
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Buscar en Mercado Libre");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String querySearch) {
                if(querySearch.length() > 0) {
                    IntentActivitySearch(querySearch);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }


    //Load Intent for Activity Search
    private void IntentActivitySearch(final String searchItems){
        Intent intentSearchItemsAcitivity = new Intent(this, SearchItemActivity.class);
        intentSearchItemsAcitivity .putExtra("searchItems", searchItems);
        intentSearchItemsAcitivity.putExtra("offset", intOffsetPagination);
        this.startActivity(intentSearchItemsAcitivity);
    }


    //Consume resource with Retrofit 2
    private void consumeApiServices(final String stringIdSearchItems){
        final ApiInterface apiInterface = ApiServices.getApliClient().create(ApiInterface.class);

        if(stringIdSearchItems.length() > 0){
            Call<Item> getItemDetail = apiInterface.getItemDetail(stringIdSearchItems);
            progresbarLoadDetailItem.setVisibility(View.VISIBLE);

            try{
                getItemDetail.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {

                        if(response.isSuccessful() && response.code()== 200){
                            itemDetailFound = response.body();
                            setValuesViewItem(itemDetailFound);
                        }else{
                            switch (response.code()){
                                case 404:
                                    errorCodeMessage = "notfound";
                                case 500:
                                    errorCodeMessage = "unknownError";
                                default:
                                    errorCodeMessage = "unknownError";
                            }
                            errorMessageScreen(errorCodeMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        progresbarLoadDetailItem.setVisibility(View.GONE);
                        errorMessageScreen("unknownError");
                    }
                });
            }catch (Exception ex){
                Log.e(TAG, "Excepción en consumo de api: ", ex);
            }
        }
    }


    //Set values into the view in xml activity_items_detail
    private void setValuesViewItem(Item itemDetailFound){
        progresbarLoadDetailItem.setVisibility(View.GONE);
        textViewCounterImageItem.setText(itemDetailFound.getPictures().size() + " Fotos");
        textViewCounterImageItem.setVisibility(View.VISIBLE);
        textViewTitleItem.setText(itemDetailFound.getTitle());
        textViewActualPriceItem.setText("$ " + itemDetailFound.getPrice());

        if(itemDetailFound.getOriginal_price()==null){
            textViewOriginalPriceItem.setVisibility(View.GONE);
            textViewPriceOffItem.setVisibility(View.GONE);
            textViewConditionsItem.setText(itemDetailFound.getCondition());
        }else{
            textViewConditionsItem.setText(itemDetailFound.getCondition() + "  -  " );

            //Calcule price discounts
            double percentageValueOffPrice = Utils.calculeForDiscountPrice(itemDetailFound.getPrice(), itemDetailFound.getOriginal_price());

            textViewOriginalPriceItem.setText("$ " + itemDetailFound.getOriginal_price() +" Precio original.");
            textViewPriceOffItem.setText(String.valueOf(percentageValueOffPrice) + "% OFF");
            textViewPriceOffItem.setPaintFlags(textViewPriceOffItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textViewPriceOffItem.setVisibility(View.VISIBLE);
        }

        textViewAvailableQuantity.setText("Cantidad disponible: " + itemDetailFound.getAvailable_quantity());
        textViewSoldQuantity.setText("Cantidad vendida: " + itemDetailFound.getSold_quantity());
        textViewDescriptionItem.setText(itemDetailFound.getPlain_text());

        notifyChangeAdapter(itemDetailFound.getAttributes());
        loadImageViewPager(itemDetailFound.getPictures());
        disabledAdapterScrollFunction();
    }


    //Save resource in ListRecycler
    private void notifyChangeAdapter(List<Attributes> attributes){
        attributesItemAdapter = new AttributesItemAdapter(this, attributes);
        listViewAtributesItem.setAdapter(attributesItemAdapter);
        attributesItemAdapter.notifyDataSetChanged();
    }


    //Load image in ViewPager
    private void loadImageViewPager(List<Pictures> pictures){
        viewPagerAdapterImgItem = new ImageItemViewPagerAdapter(ItemDetailsActivity.this, pictures);
        viewPagerSlideImageItem.setAdapter(viewPagerAdapterImgItem);
    }


    //Load Message Error Screen
    private void errorMessageScreen(String typeErrorMessage){
        layoutMessageErrorNotification.setVisibility(View.VISIBLE);

        if(typeErrorMessage.equals("notfound")){
            textViewActionReload.setVisibility(View.GONE);
            textViewErrorTitleMessage.setText("No encontramos publicaciones");
            textViewErrorAdviceMessage.setText("Revisa que la publicacion esté bien escrita.");
            imageViewErrorMessage.setImageResource(R.drawable.ic_errors_not_found);
        }
        if (typeErrorMessage.equals("network")){
            textViewErrorTitleMessage.setText("¡Parece que no hay internet!");
            textViewErrorAdviceMessage.setText("Revisa tu conexión para seguir navegando.");
            imageViewErrorMessage.setImageResource(R.drawable.ic_errors_conection);
            textViewActionReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.isOnline(ItemDetailsActivity.this)==true){
                        layoutMessageErrorNotification.setVisibility(View.GONE);
                        consumeApiServices(stringIdSearchItems);
                        ItemDetailsActivity.this.setTitle(stringNameSearchItems);
                    }

                }
            });
        }
        if (typeErrorMessage.equals("unknownError")){
            textViewErrorTitleMessage.setText("Algo salió mal");
            textViewErrorAdviceMessage.setText("Estamos trabajando para solucionarlo.");
            imageViewErrorMessage.setImageResource(R.drawable.ic_errors_fixing_problems);
            textViewActionReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutMessageErrorNotification.setVisibility(View.GONE);
                    onBackPressed();
                }
            });
        }
    }


    //Save values for the search items
    public void onSaveInstanceState(Bundle bundleValueSearch) {
        String resultsSearchItems = Utils.getGsonParser().toJson(itemDetailFound);
        bundleValueSearch.putString("itemDetailFound", resultsSearchItems);
        super.onSaveInstanceState(bundleValueSearch);
    }


    //Retrive values for search items
    public void onRestoreInstanceState(Bundle bundleValueSearch){
        super.onRestoreInstanceState(bundleValueSearch);
        String str = bundleValueSearch.getString("itemDetailFound");
        itemDetailFound= Utils.getGsonParser().fromJson(str, Item.class);
        try{
            if(itemDetailFound != null){
                setValuesViewItem(itemDetailFound);
            }
        } catch(Exception ex){
            Log.e(TAG, "Error al recuperar instancia: " + ex);
        }
    }


    private void disabledAdapterScrollFunction(){
        //Desabilitamos la funcion del listView mediante la funcion del adapter
        listViewAtributesItem.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int totalHeight = 0;
                        for (int i = 0; i < attributesItemAdapter.getCount(); i++) {
                            View listItem = attributesItemAdapter.getView(i, null, listViewAtributesItem);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }

                        ViewGroup.LayoutParams par = listViewAtributesItem.getLayoutParams();
                        par.height = totalHeight + (listViewAtributesItem.getDividerHeight() * (attributesItemAdapter.getCount() - 1));
                        listViewAtributesItem.setLayoutParams(par);
                        listViewAtributesItem.requestLayout();
                    }
                }
        );
    }

}
