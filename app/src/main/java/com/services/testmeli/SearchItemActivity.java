package com.services.testmeli;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.services.testmeli.Api.ApiServices;
import com.services.testmeli.Api.ApiInterface;
import com.services.testmeli.Model.ListItems;
import com.services.testmeli.Adapter.ItemsRecyclerAdapter;
import com.services.testmeli.Utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity {

    //Variables
    private RecyclerView recyclerViewItem;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progresbarLoadItemFound;
    private ItemsRecyclerAdapter recyclerAdapterListenItems;
    private ListItems results;
    private String TAG;
    private Integer intOffsetPagination;
    private String stringSearchItems, site_id;
    private RelativeLayout layoutMessageErrorNotification, relativeChoiseCountry;
    private TextView textViewErrorTitleMessage, textViewErrorAdviceMessage, textViewActionReload ;
    private ImageView imageViewErrorMessage, btnArgentina, btnBolivia,
            btnBrasil, btnChile, btnColombia, btnCostaRica, btnDominicana,
            btnEcuador, btnGuatemala, btnHonduras, btnMexico, btnNicaragua,
            btnPanama, btnParaguay, btnPeru, btnSalvador, btnUruguay, btnVenezuela;
    private TextView textViewPaginationContinue, textViewPaginationBack;
    private LinearLayout bottomOptionsPagination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        initVariables();

        setCountryForSearch();

        Intent intentRetrieveValueSearch = getIntent();
        Bundle bundleValueSearch = intentRetrieveValueSearch .getExtras();

        if(bundleValueSearch != null) {
            stringSearchItems = bundleValueSearch.getString("searchItems", "");
            intOffsetPagination = bundleValueSearch.getInt("offset",0);
            site_id = bundleValueSearch.getString("site_id","");
            if(Utils.isOnline(this)==true){
                if(site_id.equals("")){
                    relativeChoiseCountry.setVisibility(View.VISIBLE);
                }else {
                    relativeChoiseCountry.setVisibility(View.GONE);
                }
                consumeApiServices(site_id, stringSearchItems, intOffsetPagination);
            }else{
                relativeChoiseCountry.setVisibility(View.GONE);
                errorMessageScreen("network");
            }
        } else{
            stringSearchItems = "";
            intOffsetPagination = 0;
            if(Utils.isOnline(this)==true){
                if(site_id.equals("")){
                    relativeChoiseCountry.setVisibility(View.VISIBLE);
                }else {
                    relativeChoiseCountry.setVisibility(View.GONE);
                }
                consumeApiServices(site_id, stringSearchItems, intOffsetPagination);
            }else{
                relativeChoiseCountry.setVisibility(View.GONE);
                errorMessageScreen("network");
            }

        }
    }

    //Init Variables
    private void initVariables(){
        TAG = SearchItemActivity.class.getSimpleName();
        progresbarLoadItemFound = (ProgressBar) findViewById(R.id.progresbarLoadDetailItem);
        recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerViewItem);
        layoutMessageErrorNotification = (RelativeLayout) findViewById(R.id.layoutMessageErrorNotification);
        textViewErrorTitleMessage = (TextView) findViewById(R.id.textViewErrorTitleMessage);
        textViewErrorAdviceMessage = (TextView) findViewById(R.id.textViewErrorAdviceMessage);
        textViewActionReload = (TextView) findViewById(R.id.textViewActionReload);
        imageViewErrorMessage = (ImageView) findViewById(R.id.imageViewErrorMessage);
        layoutManager = new LinearLayoutManager(SearchItemActivity.this);
        recyclerViewItem.setLayoutManager(layoutManager);
        recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItem.setNestedScrollingEnabled(false);
        results = new ListItems();
        stringSearchItems="";
        site_id="";
        intOffsetPagination =0;
        textViewPaginationContinue = (TextView) findViewById(R.id.textViewPaginationContinue);
        textViewPaginationBack = (TextView) findViewById(R.id.textViewPaginationBack);
        bottomOptionsPagination = (LinearLayout) findViewById(R.id.bottomOptionsPagination);
        btnArgentina = (ImageView) findViewById(R.id.btnArgentina);
        btnBolivia = (ImageView) findViewById(R.id.btnBolivia);
        btnBrasil = (ImageView) findViewById(R.id.btnBrasil);
        btnChile = (ImageView) findViewById(R.id.btnChile);
        btnColombia = (ImageView) findViewById(R.id.btnColombia);
        btnCostaRica = (ImageView) findViewById(R.id.btnCostaRica);
        btnDominicana = (ImageView) findViewById(R.id.btnDominicana);
        btnEcuador = (ImageView) findViewById(R.id.btnEcuador);
        btnGuatemala = (ImageView) findViewById(R.id.btnGuatemala);
        btnHonduras = (ImageView) findViewById(R.id.btnHonduras);
        btnMexico = (ImageView) findViewById(R.id.btnMexico);
        btnNicaragua = (ImageView) findViewById(R.id.btnNicaragua);
        btnPanama = (ImageView) findViewById(R.id.btnPanama);
        btnParaguay = (ImageView) findViewById(R.id.btnParaguay);
        btnPeru = (ImageView) findViewById(R.id.btnPeru);
        btnSalvador = (ImageView) findViewById(R.id.btnSalvador);
        btnUruguay = (ImageView) findViewById(R.id.btnUruguay);
        btnVenezuela = (ImageView) findViewById(R.id.btnVenezuela);
        relativeChoiseCountry = (RelativeLayout) findViewById(R.id.relativeChoiseCountry);
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
        if(!site_id.equals("")){
            Intent intentSearchItemsAcitivity = new Intent(this, SearchItemActivity.class);
            intentSearchItemsAcitivity .putExtra("searchItems", searchItems);
            intentSearchItemsAcitivity.putExtra("offset", intOffsetPagination);
            intentSearchItemsAcitivity.putExtra("site_id", site_id);
            this.startActivity(intentSearchItemsAcitivity);
        }else{
            Toast.makeText(this, "Seleccione un pais...", Toast.LENGTH_SHORT).show();
        }
    }


    //Load Intent for Activity Item Detail
    private void IntentActivityItemDetail(){
        recyclerAdapterListenItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentActivityItemDetail = new Intent(SearchItemActivity.this, ItemDetailsActivity.class);
                intentActivityItemDetail.putExtra("id_searchItem", results.getResults().get(recyclerViewItem.getChildAdapterPosition(view)).getId());
                intentActivityItemDetail.putExtra("name_searchItem", results.getResults().get(recyclerViewItem.getChildAdapterPosition(view)).getTitle());
                startActivity(intentActivityItemDetail);
            }
        });
    }


    //Consume resource with Retrofit 2
    private void consumeApiServices(final String site_id, final String searchItems, final Integer intOffsetPagination){
        ApiInterface apiInterface = ApiServices.getApliClient().create(ApiInterface.class);
        Call<ListItems> getListItems;

        if(searchItems.length() > 0){
            this.setTitle(searchItems);
            getListItems = apiInterface.getListItems(site_id,searchItems, intOffsetPagination);
            relativeChoiseCountry.setVisibility(View.GONE);
            progresbarLoadItemFound.setVisibility(View.VISIBLE);

            try{
                getListItems.enqueue(new Callback<ListItems>() {
                    @Override
                    public void onResponse(Call<ListItems> call, Response<ListItems> response) {
                        if(response.isSuccessful() && response.body().getResults().isEmpty()){
                            progresbarLoadItemFound.setVisibility(View.GONE);
                            errorMessageScreen("notfound");
                        } else{
                            results = response.body();
                            notifyChangeAdapter(results);
                            progresbarLoadItemFound.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ListItems> call, Throwable t) {
                        progresbarLoadItemFound.setVisibility(View.GONE);
                        errorMessageScreen("unknownError");
                    }
                });
                bottomOptionsPagination.setVisibility(View.VISIBLE);
            }catch (Exception ex){
                Log.e(TAG, "Excepción en consumo de api: ", ex);
            }
        }
    }


    //Save resource in ListRecycler
    private void notifyChangeAdapter(final ListItems results){
        layoutManager = new LinearLayoutManager (this);
        recyclerViewItem.setLayoutManager (layoutManager);

        recyclerAdapterListenItems = new ItemsRecyclerAdapter(results, SearchItemActivity.this);
        recyclerViewItem.setAdapter(recyclerAdapterListenItems);
        progresbarLoadItemFound.setVisibility(View.GONE);


        recyclerAdapterListenItems.notifyDataSetChanged();
        //Pagination
        paginationMode();


        IntentActivityItemDetail();
    }


    private void paginationMode(){
        if(intOffsetPagination <= 1){
            textViewPaginationBack.setEnabled(false);
        }else {
            textViewPaginationBack.setEnabled(true);
        }

        if(intOffsetPagination > 1000 ){
            textViewPaginationContinue.setEnabled(false);
        }else {
            textViewPaginationContinue.setEnabled(true);
        }
        textViewPaginationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intOffsetPagination=intOffsetPagination-50;
                if(intOffsetPagination < 0){
                    intOffsetPagination = 0;
                }
                IntentActivitySearch(stringSearchItems);
            }
        });

        textViewPaginationContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intOffsetPagination=intOffsetPagination+50;
                if(intOffsetPagination > 1000){
                    intOffsetPagination = 1000;
                }
                IntentActivitySearch(stringSearchItems);
            }
        });
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
                    if(Utils.isOnline(SearchItemActivity.this)==true){
                        layoutMessageErrorNotification.setVisibility(View.GONE);
                        consumeApiServices(site_id, stringSearchItems, intOffsetPagination);
                        SearchItemActivity.this.setTitle(stringSearchItems);
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


    //Load ubication search
    private void setCountryForSearch(){
        btnArgentina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLA";
            }
        });
        btnBolivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MBO";
            }
        });
        btnBrasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLB";
            }
        });
        btnChile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLC";
            }
        });
        btnColombia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MCO";
            }
        });
        btnCostaRica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MCR";
            }
        });
        btnDominicana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MRD";
            }
        });
        btnEcuador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MEC";
            }
        });
        btnGuatemala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MGT";
            }
        });
        btnHonduras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MHN";
            }
        });
        btnMexico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLM";
            }
        });
        btnNicaragua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MNI";
            }
        });
        btnPanama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MPA";
            }
        });
        btnParaguay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MPY";
            }
        });
        btnPeru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MPE";
            }
        });
        btnSalvador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MSV";
            }
        });
        btnUruguay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLU";
            }
        });
        btnVenezuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_id = "MLV";
            }
        });




    }


    //Save values for the search items
    public void onSaveInstanceState(Bundle bundleValueSearch) {
        String resultsSearchItems = Utils.getGsonParser().toJson(results);
        bundleValueSearch.putString("results", resultsSearchItems);
        bundleValueSearch.putString("site_id", site_id);
        super.onSaveInstanceState(bundleValueSearch);
    }


    //Retrive values for search items
    public void onRestoreInstanceState(Bundle bundleValueSearch){
        super.onRestoreInstanceState(bundleValueSearch);
        String str = bundleValueSearch.getString("results");
        results= Utils.getGsonParser().fromJson(str, ListItems.class);
        site_id = bundleValueSearch.getString("site_id", "");
        try{
            if(results.getResults() != null){
                notifyChangeAdapter(results);
            }
        } catch(Exception ex){
            Log.e(TAG, "Error al recuperar instancia: " + ex);
        }

    }

}
