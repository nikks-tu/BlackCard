package com.techuva.blackcard.new_changes.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.blackcard.R;
import com.techuva.blackcard.contus.MApplication;
import com.techuva.blackcard.contus.activity.CustomRequest;
import com.techuva.blackcard.contus.activity.VolleyResponseListener;
import com.techuva.blackcard.contusfly.api_interface.AppVersionDataInterface;
import com.techuva.blackcard.contusfly.model.AnnouncementReadObject;
import com.techuva.blackcard.contusfly.smoothprogressbar.SmoothProgressBar;
import com.techuva.blackcard.contusfly.utils.Constants;
import com.techuva.blackcard.new_changes.adapters.AnnouncementRecyclerAdapter;
import com.techuva.blackcard.new_changes.models.AnnouncementModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.techuva.blackcard.contus.app.Constants.PARTICIPATE_ANNOUNCEMENT;
import static com.techuva.blackcard.contus.app.Constants.SearchKey;


public class SearchAnnouncements extends Fragment implements AnnouncementRecyclerAdapter.EventListener{
    //View
    private View parentView;
    //Iamge view chat
    private ImageView imgChat;
    boolean isMore = false;

    private SmoothProgressBar googleNow;

    //Google ad
    private AdView mAdView;
    private Activity searchFragment;
    RecyclerView announcement_list;
    private SwipeRefreshLayout pull_to_refresh;
    LinearLayout ll_no_announcement;
    String userId;
    ArrayList<AnnouncementModel> annonceList;
    ArrayList<AnnouncementReadObject> list;

    int NextPageNumber,PageNumber,RowsPerPage,TotalCount;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    AnnouncementRecyclerAdapter linearListAdapter;
    int pageno=1;
    private boolean loading = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.announcement_layout, container, false);
        mAdView =  parentView.findViewById(R.id.adView);
        googleNow =  parentView.findViewById(R.id.google_now);
        pull_to_refresh = parentView.findViewById(R.id.pull_to_refresh);
        ll_no_announcement = parentView.findViewById(R.id.ll_no_announcement);
        announcement_list   = parentView.findViewById(R.id.announcement_list);
        userId = MApplication.getString(getActivity(), Constants.USER_ID);

        LinearLayout userpoll =  getActivity().findViewById(R.id.publicpoll);
        userpoll.setVisibility(View.VISIBLE);

        ImageView pollsearch_iv=getActivity().findViewById(R.id.imgSearch);
        ImageView imgEdit=getActivity().findViewById(R.id.imgEdit);

        pollsearch_iv.setVisibility(View.INVISIBLE);
        imgEdit.setVisibility(View.INVISIBLE);

        //getAnnouncement_list(false);

        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageno=1;
                loading = true;
                annonceList.clear();
                list.clear();
                getAnnouncement_list(false);
            }
        });



        announcement_list.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            pageno= pageno+1;

                            getAnnouncement_list(true);

                            //int lastitemposition=totalItemCount;
                            /*if(annonceList.get(lastitemposition).getUser_participate_announcements()==null
                                    ||annonceList.get(lastitemposition).getUser_participate_announcements().get(0).getIsRead()==0)
                            {*/

                           // }
                        }


                    }

                }
            }
        });

        //Returning the view
        return parentView;
    }


   public void getAnnouncement_list(final boolean more)
   {
       String searchKey = MApplication.getString(searchFragment, SearchKey);
       EditText search_et= getActivity().findViewById(R.id.search_et);
       JsonObject obj = new JsonObject();
       try {
           obj.addProperty("action","searchUserAnnouncement");
           obj.addProperty("user_id",userId);
           obj.addProperty("search_key", searchKey);
           obj.addProperty("page", pageno);
           Log.v("...", obj.toString());
       }
       catch (Exception ae)
       {

       }
       googleNow.setVisibility(View.VISIBLE);
       googleNow.progressiveStart();
       isMore = more;
       serviceCall(obj);
  /*     CustomRequest.makeJsonObjectRequest(getActivity(), SEARCH_ANNOUNCEMENT,obj, new VolleyResponseListener() {
           @Override
           public void onError(String message) {
               Log.i("onErrormessage", "message= " +message);
           }

           @Override
           public void onResponse(JSONObject response) {
               JSONObject result = response.optJSONObject("results");
               googleNow.progressiveStop();
               googleNow.setVisibility(View.GONE);
               //stopping swipe refresh
               pull_to_refresh.setRefreshing(false);
               try {
                   int success=response.optInt("success");
                   if(success==1)
                   {
                       announcement_list.setVisibility(View.VISIBLE);
                       ll_no_announcement.setVisibility(View.GONE);
                       TotalCount =result.optInt("total");
                       int per_page =result.optInt("per_page");
                       int current_page =result.optInt("current_page");
                       int last_page =result.optInt("last_page");
                       int from =result.optInt("from");
                       int to =result.optInt("to");

                       JSONArray data =result.optJSONArray("data");

                       if(!more) {

                           annonceList = new ArrayList<>();
                       }
                       for(int i=0;i<data.length();i++)
                       {
                          JSONObject obj =data.getJSONObject(i);
                          String adminCount= obj.optString("adminCount");
                           String announcement= obj.optString("announcement");
                           String announcementCommentsCounts= obj.optString("announcementCommentsCounts");
                           String announcementLikesCounts= obj.optString("announcementLikesCounts");
                           String announcementParticipateCount= obj.optString("announcementParticipateCount");
                           String created_at= obj.optString("created_at");
                           String id= obj.optString("id");
                           String image= obj.optString("image");
                           String is_delete= obj.optString("is_delete");
                           String status= obj.optString("status");
                           String title= obj.optString("title");
                           String updated_at= obj.optString("updated_at");
                           String userAnnouncementLikes= obj.optString("userAnnouncementLikes");
                           String userCount= obj.optString("userCount");
                           JSONArray jsonArray = new JSONArray();
                           list = new ArrayList<>();
                           if(obj.opt("user_participate_announcements")!=null)
                           {
                               jsonArray = obj.optJSONArray("user_participate_announcements");
                               for (int a=0; a<jsonArray.length(); a++)
                               {
                                   AnnouncementReadObject readObject = new AnnouncementReadObject();
                                   JSONObject jsonObject = jsonArray.optJSONObject(a);
                                   readObject.setId(jsonObject.optInt("id"));
                                   readObject.setAnnouncementId(jsonObject.optInt("announcement_id"));
                                   readObject.setIsRead(jsonObject.getInt("is_read"));
                                   readObject.setUserId(jsonObject.getInt("user_id"));
                                   readObject.setStatus(jsonObject.getInt("status"));
                                   readObject.setIsDelete(jsonObject.getInt("is_delete"));
                                   list.add(readObject);
                               }
                           }
                           JSONObject announcementcategory =obj.optJSONObject("announcementcategory");
                           String category_name="";
                           if(announcementcategory!=null)
                           {
                               JSONObject category =announcementcategory.optJSONObject("category");
                                category_name =category.optString("category_name");
                           }
                           JSONObject user_infos =obj.optJSONObject("user_infos");
                           String adminProfilePic="";
                           if(user_infos!=null)
                           {
                               adminProfilePic =user_infos.optString("image");
                           }
                           AnnouncementModel obi=new AnnouncementModel();
                           obi.setAdminCount(adminCount);
                           obi.setAnnouncement(announcement);
                           obi.setAnnouncementCommentsCounts(announcementCommentsCounts);
                           obi.setAnnouncementLikesCounts(announcementLikesCounts);
                           obi.setAnnouncementParticipateCount(announcementLikesCounts);
                           obi.setAnnouncementParticipateCount(announcementParticipateCount);
                           obi.setCreated_at(created_at);
                           obi.setId(id);
                           obi.setImage(image);
                           obi.setIs_delete(is_delete);
                           obi.setStatus(status);
                           obi.setTitle(title);
                           obi.setUpdated_at(updated_at);
                           obi.setUserCount(userCount);
                           obi.setCategory_name(category_name);
                           obi.setUserAnnouncementLikes(userAnnouncementLikes);
                           obi.setAdminProfilePic(adminProfilePic);
                           if(list.size()>0)
                           {
                               obi.setUser_participate_announcements(list);
                           }
                           annonceList.add(obi);
                       }
                       if(!more) {
                           googleNow.progressiveStop();
                           googleNow.setVisibility(View.GONE);
                           announcement_list.setVisibility(View.VISIBLE);
                           ll_no_announcement.setVisibility(View.GONE);
                           mLayoutManager = new LinearLayoutManager(getContext());
                           announcement_list.setLayoutManager(mLayoutManager);
                           linearListAdapter=new AnnouncementRecyclerAdapter(annonceList, SearchAnnouncements.this);
                           announcement_list.setAdapter(linearListAdapter);
                       }
                       else
                       {
                           googleNow.progressiveStop();
                           googleNow.setVisibility(View.GONE);
                           announcement_list.setVisibility(View.VISIBLE);
                           ll_no_announcement.setVisibility(View.GONE);
                           linearListAdapter.notifyDataSetChanged();
                       }

                       if (more) {
                           if (announcement_list.getAdapter() != null && announcement_list.getAdapter().getItemCount() < TotalCount) {
                               loading = true;
                           } else {
                               loading = false;
                           }
                       }
                   }
                   else
                   {
                       if(pageno==1)
                       {
                           announcement_list.setVisibility(View.GONE);
                           ll_no_announcement.setVisibility(View.VISIBLE);
                       }
                       Toast.makeText(getContext(),result.getString("msg"), Toast.LENGTH_SHORT).show();
                   }


                   //updateAnnouncement(annonceList.size()-1);


               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }
       });
  */ }


    public void updateAnnouncement(final int position )
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("action","announcement_read");
            obj.put("user_id",userId);
            obj.put("announcement_id",annonceList.get(position).getId());

            Log.v("read", obj.toString());
        }
        catch (Exception ae)
        {

        }


        CustomRequest.makeJsonObjectRequest(getContext(), PARTICIPATE_ANNOUNCEMENT,obj, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
//            dialog.dismiss();
                Log.i("onErrormessage", "message= " +message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.i("PCCmessage", "message " + result.getString("msg"));
                    int seccesss=response.getInt("success");
                    int count=response.optInt("count");
                    if(seccesss==1)
                    {

                        for(int i = 0;i<position+1 ;i++)
                        {
                            ArrayList<AnnouncementReadObject> list = new ArrayList<>();
                            AnnouncementReadObject readObject = new AnnouncementReadObject();
                            readObject.setId(0);
                            readObject.setAnnouncementId(0);
                            readObject.setIsRead(1);
                            readObject.setUserId(Integer.parseInt(userId));
                            readObject.setStatus(0);
                            readObject.setIsDelete(0);
                            list.add(readObject);
                            annonceList.get(i).setUser_participate_announcements(list);
                        }



                        linearListAdapter.notifyDataSetChanged();


                    }
                    else
                    {

                        // eventListener.onEvent(false);
                       // Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                        //annonceList.get(position).getUser_participate_announcements().get(0).setIsRead(0);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




    }



    public void serviceCall(JsonObject obj){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(com.techuva.blackcard.contus.app.Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppVersionDataInterface service = retrofit.create(AppVersionDataInterface.class);
        Call<JsonElement> call = service.searchAnnouncements(obj);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                int version_no;
                if (response.body() != null) {
                    JsonObject jobj = response.body().getAsJsonObject();

                    JsonObject result = jobj.get("results").getAsJsonObject();
                    googleNow.progressiveStop();

                    googleNow.setVisibility(View.GONE);
                    //stopping swipe refresh
                    pull_to_refresh.setRefreshing(false);
                    try {
                        int success = jobj.get("success").getAsInt();

                        //Toast.makeText(getContext(), "Error"+ success , Toast.LENGTH_SHORT).show();
                        if (success == 1) {
                            announcement_list.setVisibility(View.VISIBLE);
                            ll_no_announcement.setVisibility(View.GONE);
                            TotalCount = result.get("total").getAsInt();
                            int per_page = result.get("per_page").getAsInt();
                            int current_page = result.get("current_page").getAsInt();
                            int last_page = result.get("last_page").getAsInt();
                            int from = result.get("from").getAsInt();
                            int to = result.get("to").getAsInt();

                            JsonArray data = result.get("data").getAsJsonArray();

                            if (!isMore) {
                                annonceList = new ArrayList<>();
                            }
                            for (int i = 0; i < data.size(); i++) {
                                JsonObject obj = data.get(i).getAsJsonObject();
                                String adminCount="";
                                if(obj.has("adminCount"))
                                {
                                    adminCount = obj.get("adminCount").getAsString();
                                }
                                String announcement = obj.get("announcement").getAsString();
                                String announcementCommentsCounts = obj.get("announcementCommentsCounts").getAsString();
                                String announcementLikesCounts = obj.get("announcementLikesCounts").getAsString();
                                String announcementParticipateCount = obj.get("announcementParticipateCount").getAsString();
                                String created_at = obj.get("created_at").getAsString();
                                String id = obj.get("id").getAsString();
                                String image = obj.get("image").getAsString();
                                String is_delete = obj.get("is_delete").getAsString();
                                String status = obj.get("status").getAsString();
                                String title = obj.get("title").getAsString();
                                String updated_at = obj.get("updated_at").getAsString();
                                String userAnnouncementLikes = obj.get("userAnnouncementLikes").getAsString();
                                String userCount = "";
                                if(obj.has("userCount"))
                                {
                                    userCount = obj.get("userCount").getAsString();
                                }
                                JsonArray jsonArray = new JsonArray();
                                list = new ArrayList<>();
                                if (obj.get("user_participate_announcements") != null) {
                                    jsonArray = obj.get("user_participate_announcements").getAsJsonArray();
                                    for (int a = 0; a < jsonArray.size(); a++) {
                                        AnnouncementReadObject readObject = new AnnouncementReadObject();
                                        JsonObject jsonObject = jsonArray.get(a).getAsJsonObject();
                                        readObject.setId(jsonObject.get("id").getAsInt());
                                        readObject.setAnnouncementId(jsonObject.get("announcement_id").getAsInt());
                                        readObject.setIsRead(jsonObject.get("is_read").getAsInt());
                                        readObject.setUserId(jsonObject.get("user_id").getAsInt());
                                        readObject.setStatus(jsonObject.get("status").getAsInt());
                                        readObject.setIsDelete(jsonObject.get("is_delete").getAsInt());
                                        list.add(readObject);
                                    }
                                }
                                JsonObject announcementcategory = obj.get("announcementcategory").getAsJsonObject();
                                String category_name = "";
                                if (announcementcategory != null) {
                                    JsonObject category = announcementcategory.get("category").getAsJsonObject();
                                    category_name = category.get("category_name").getAsString();
                                }
                                JsonObject user_infos = obj.get("user_infos").getAsJsonObject();
                                String adminProfilePic = "";
                                if (user_infos != null) {
                                    adminProfilePic = user_infos.get("image").getAsString();
                                }
                                AnnouncementModel obi = new AnnouncementModel();
                                obi.setAdminCount(adminCount);
                                obi.setAnnouncement(announcement);
                                obi.setAnnouncementCommentsCounts(announcementCommentsCounts);
                                obi.setAnnouncementLikesCounts(announcementLikesCounts);
                                obi.setAnnouncementParticipateCount(announcementLikesCounts);
                                obi.setAnnouncementParticipateCount(announcementParticipateCount);
                                obi.setCreated_at(created_at);
                                obi.setId(id);
                                obi.setImage(image);
                                obi.setIs_delete(is_delete);
                                obi.setStatus(status);
                                obi.setTitle(title);
                                obi.setUpdated_at(updated_at);
                                obi.setUserCount(userCount);
                                obi.setCategory_name(category_name);
                                obi.setUserAnnouncementLikes(userAnnouncementLikes);
                                obi.setAdminProfilePic(adminProfilePic);
                                if (list.size() > 0) {
                                    obi.setUser_participate_announcements(list);
                                }
                                annonceList.add(obi);
                            }
                            if (!isMore) {
                                googleNow.progressiveStop();
                                googleNow.setVisibility(View.GONE);
                                announcement_list.setVisibility(View.VISIBLE);
                                ll_no_announcement.setVisibility(View.GONE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                announcement_list.setLayoutManager(mLayoutManager);
                                linearListAdapter = new AnnouncementRecyclerAdapter(annonceList, SearchAnnouncements.this);
                                announcement_list.setAdapter(linearListAdapter);
                            } else {
                                googleNow.progressiveStop();
                                googleNow.setVisibility(View.GONE);
                                announcement_list.setVisibility(View.VISIBLE);
                                ll_no_announcement.setVisibility(View.GONE);
                                linearListAdapter.notifyDataSetChanged();
                            }

                            if (isMore) {
                                if (announcement_list.getAdapter() != null && announcement_list.getAdapter().getItemCount() < TotalCount) {
                                    loading = true;
                                } else {
                                    loading = false;
                                }
                            }
                        } else {

                            //Toast.makeText(getContext(), "Error"+ success , Toast.LENGTH_SHORT).show();
                            if (pageno == 1) {
                                announcement_list.setVisibility(View.GONE);
                                ll_no_announcement.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(getContext(), result.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                googleNow.progressiveStop();
                googleNow.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error" +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        annonceList = new ArrayList<>();
        list= new ArrayList<>();
        pageno = 1;
        getAnnouncement_list(false);
        MApplication.googleAd(mAdView);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            searchFragment = (Activity) context;
        }

    }

    @Override
    public void onEvent(Boolean data) {
        annonceList = new ArrayList<>();
        pageno = 1;
        annonceList = new ArrayList<>();
        list = new ArrayList<>();
        getAnnouncement_list(false);
    }




}

