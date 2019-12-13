package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.guswn.allthatlyrics.Extension.MyRetrofit;
import com.example.guswn.allthatlyrics.Home.Frag2_social.InnerSocialActivity;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialAPI;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialImageModel;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialInfoModel;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialLikedMarkedResponse;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialUploadResponse;
import com.example.guswn.allthatlyrics.Extension.PhotoFilter;
import com.example.guswn.allthatlyrics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;
import static com.example.guswn.allthatlyrics.Extension.PhotoFilter.getTypeFromString;

public class Home_frag3_history extends Fragment {

    /**프래그먼트 생성자 역할*/
    String UserIdx;
    public static Home_frag3_history newInstance(String UserIdx) {
        Bundle bundle = new Bundle();
        bundle.putString("UserIdx", UserIdx);


        Home_frag3_history fragment = new Home_frag3_history();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            UserIdx = bundle.getString("UserIdx");

        }
    }
    /**프래그먼트 생성자 역할*/
     SocialAPI api;
     ArrayList<SocialInfoModel> historyInfos = new ArrayList<>();
    HistoryAdapter historyAdapter;
    @BindView(R.id.frag3_history_gridview)
    GridView gridView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home_frag3_history, container, false);
        ButterKnife.bind(this, rootView);
        /**프래그먼트 생성자 역할*/
        readBundle(getArguments());
        /**프래그먼트 생성자 역할*/
        //레트로핏
        api = new MyRetrofit().create(SocialAPI.class);
        //레트로핏

        loadSocialHistory_oneidx(UserIdx);
        return rootView;
    }

    public void loadSocialHistory_oneidx(final String USER_IDX){
        Call<SocialUploadResponse> call = api.getSocialHistoryList_oneidx(USER_IDX);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Social Information Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("loadSocialHistory_oneidx_code",""+response.code());
                    return;
                }
                progressDoalog.dismiss();

                SocialUploadResponse res2 = response.body();
                List<SocialUploadResponse> list = res2.getSocialHistoryList();

                for (SocialUploadResponse res : list){
                    ArrayList<SocialImageModel> socialImageModels = new ArrayList<>() ;

                    String content = res.getSocial_content();
                    String idx = res.getIdx();
                    String useridx = res.getSocial_useridx();
                    String username = res.getSocial_username();
                    String time = res.getSocial_time();
                    String location = res.getSocial_location();
                    String imgpath = res.getSocial_imagepath_list();
                    Log.e("loadSocialHistory ",content+"/"+useridx+"/"+
                            username+"/"+time+"/"+location+"/"+imgpath);

                    /**imgpath는 string이다*/
                    try {
                        JSONArray jsonArray = new JSONArray(imgpath);
                        /**스트링을 제이슨어레이로 [ {" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"}...]*/
                        JSONObject explrObject;
                        for (int a=0;a<jsonArray.length();a++){
                            explrObject = jsonArray.getJSONObject(a);
                            //Log.e("1_explrObject key "+a, String.valueOf(explrObject));
                            for (Iterator<String> it = explrObject.keys(); it.hasNext(); ) {
                                String key = it.next(); /**제이슨 오브젝트의 키*/
                                String value2 = explrObject.getString(key); /** 제이슨오브젝트의 밸류*/
                                String mimetype = null;
                                String filter = null;
                                if (value2.contains(",")){//array ["filter_type","mime_type"]
                                    JSONArray innerArray  = explrObject.getJSONArray(key);
                                    filter = innerArray.getString(0);
                                    mimetype = innerArray.getString(1);
                                }else {//string "Normal" etc...
                                    filter = value2;
                                    mimetype = "image";
                                }
                                Log.e("2_loadSocialHistory_oneidx_explrObject key/value"+a,URL_withoutslash+key+" ____ "+filter+"___"+mimetype); /** 성공*/
                                socialImageModels.add(new SocialImageModel(key,filter,mimetype));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /**userinfo*/
                    List<Value_3> list2 = res.getSocialUserInfoList();
                    String photo = list2.get(0).getPhoto();
                    String name = list2.get(0).getUsername();

                    /**liked_list*/
                    List<SocialLikedMarkedResponse> likedlist = res.getSocial_Liked_List();
                    int likedcnt = 0;
                    boolean isLiked = false;
                    boolean isMarked = false;
                    for (SocialLikedMarkedResponse one : likedlist){

                        String myidx = one.getSocial_Liked_myidx();
                        if (!myidx.equals("null")){
                            likedcnt++;
                        }
                        if (myidx.equals(USER_IDX)){
                            isLiked=true;
                            // model.setLiked(true);
                        }
                    }

                    /**marked_list*/
                    List<SocialLikedMarkedResponse> markedlist = res.getSocial_Marked_List();
                    for (SocialLikedMarkedResponse one : markedlist){
                        String myidx = one.getSocial_Marked_myidx();
                        if (myidx.equals(USER_IDX)){
                            isMarked = true;
                            // model.setBookMarked(true);
                        }
                    }
                    SocialInfoModel model = new SocialInfoModel(idx,useridx,photo,name,location,likedcnt+"",content,time,socialImageModels);
                    if (isLiked){
                        model.setLiked(true);
                    }
                    if (isMarked){
                        model.setBookMarked(true);
                    }
                    historyInfos.add(model);
                }

                /**그리드 뷰*/
                historyAdapter = new HistoryAdapter(getActivity());
                gridView.setAdapter(historyAdapter);
                /**그리드 뷰*/

            }


            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("loadSocialHistory_oneidx_fail","Error : "+t.getMessage());
                progressDoalog.dismiss();
            }
        });
    }

    private class HistoryAdapter extends BaseAdapter {
        private Activity context;

        public HistoryAdapter(Activity localContext){
            context = localContext;
        }
        @Override
        public int getCount() {
            return historyInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {

            final SocialInfoModel Clikedmodel = historyInfos.get(position);

            View picturesView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView imageViewAndroid;
            final ImageView imageViewAndroid2;

            if (convertView == null) {
                picturesView = new View(context);
                picturesView = inflater.inflate(R.layout.custom_grid_view_frag3_history, null);
                imageViewAndroid2 = (ImageView) picturesView.findViewById(R.id.custom_img_2);
                imageViewAndroid = (ImageView) picturesView.findViewById(R.id.custom_img);
            } else {
                picturesView = (View) convertView;
                picturesView = inflater.inflate(R.layout.custom_grid_view_frag3_history, null);
                imageViewAndroid2 = (ImageView) picturesView.findViewById(R.id.custom_img_2);
                imageViewAndroid = (ImageView) picturesView.findViewById(R.id.custom_img);
            }

            int imagecnt = historyInfos.get(position).getSocialImageModelList().size();
            if (imagecnt>1){
                imageViewAndroid2.setVisibility(View.VISIBLE);
            }else {
                imageViewAndroid2.setVisibility(View.GONE);
            }

            /**picturesView clicked*/
            picturesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), InnerSocialActivity.class);
                    intent.putExtra("Clikedmodel", Clikedmodel);
                    intent.putParcelableArrayListExtra("Clikedmodel_SocialImageModelList", Clikedmodel.getSocialImageModelList());
                    // Clikedmodel 속 어레이 리스트는 단독으로 넘겨야 한다
                    context.startActivity(intent);
                }
            });

            /**그리드 이미지*/
            String imgurl = Clikedmodel.getSocialImageModelList().get(0).getUrl();
            Integer imgFilter =getTypeFromString(Clikedmodel.getSocialImageModelList().get(0).getFilter()) ;
            PhotoFilter photoFilter = new PhotoFilter(true,imgFilter,URL_withoutslash+imgurl,null,context,imageViewAndroid);
            photoFilter.photoFilterByType();
            /**그리드 이미지*/
            return picturesView;
        }
    }
}
