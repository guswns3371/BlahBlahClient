package com.example.guswn.allthatlyrics.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.home.Frag1_friends.InnerFriendActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.FriendInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter_Friend extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int LAYOUT_ONE= 0;
    private static final int LAYOUT_TWO= 1;
    public static ArrayList<String> AddedChatPeopleList = new ArrayList<>();

    public class MyViewHolderA extends RecyclerView.ViewHolder{
        @BindView(R.id.f1_a_img_A)
        ImageView a_img;
        @BindView(R.id.f1_a_name_txt_A)
        TextView a_name_txt;
        @BindView(R.id.f1_a_des_txt_A)
        TextView a_des_txt;

        public MyViewHolderA(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            if(isChatAdd){
                itemView.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        FriendInfo clickedFriend = friendInfos.get(pos);
                        Intent intent = new Intent(context, InnerFriendActivity.class);
                        intent.putExtra("idx",friendInfos.get(pos).getIdx());
                        intent.putExtra("image",friendInfos.get(pos).getImg());
                        intent.putExtra("name",friendInfos.get(pos).getName());
                        intent.putExtra("email",friendInfos.get(pos).getEmail());
                        intent.putExtra("birthday",friendInfos.get(pos).getBirthday());
                        intent.putExtra("introduce",friendInfos.get(pos).getDescription());
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //위코드처럼 플래그를 달면  밑에 startActivityForResult가 제대로 실행되지 않는다
                        //context.startActivity(intent);
                        ((Activity) context).startActivityForResult(intent,1);
                       // Toast.makeText(v.getContext(),"You clicked : "+clickedFriend.getEmail(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    public class MyViewHolderB extends RecyclerView.ViewHolder{
        @BindView(R.id.f1_a_img)
        ImageView b_img;
        @BindView(R.id.f1_a_name_txt)
        TextView b_name_txt;
        @BindView(R.id.f1_a_des_txt)
        TextView b_des_txt;
        @BindView(R.id.f1_a_checkbox)
        TextView b_checkbox;
        public MyViewHolderB(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        FriendInfo clickedFriend = friendInfos.get(pos);
                        Intent intent = new Intent(context,InnerFriendActivity.class);
                        intent.putExtra("idx",friendInfos.get(pos).getIdx());
                        intent.putExtra("image",friendInfos.get(pos).getImg());
                        intent.putExtra("name",friendInfos.get(pos).getName());
                        intent.putExtra("email",friendInfos.get(pos).getEmail());
                        intent.putExtra("birthday",friendInfos.get(pos).getBirthday());
                        intent.putExtra("introduce",friendInfos.get(pos).getDescription());
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //위코드처럼 플래그를 달면  밑에 startActivityForResult가 제대로 실행되지 않는다
                        //context.startActivity(intent);
                        ((Activity) context).startActivityForResult(intent,1);
                       // Toast.makeText(v.getContext(),"You clicked : "+clickedFriend.getEmail(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //
    public interface FriendRycyclerClickListner{
        void onItemClick(int position,View v);
        void onCheckBoxClick(int position,View v);
    }
    private FriendRycyclerClickListner FRlistner;
    public void setOnClickListener_Friend(FriendRycyclerClickListner f){
        this.FRlistner = f;
    }

    private ArrayList<FriendInfo> friendInfos;
    private Context context;
    private Boolean isChatAdd =false;
    public MyAdapter_Friend(ArrayList<FriendInfo> friendInfos1, Context context, Boolean isChatAdd){
        this.friendInfos = friendInfos1;
        this.context = context;
        this.isChatAdd = isChatAdd;
    }
    //

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(i==LAYOUT_ONE)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_f1_a,viewGroup,false);
            viewHolder = new MyViewHolderA(view);
        }
        else
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_f1_b,viewGroup,false);
            viewHolder= new MyViewHolderB(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
       final FriendInfo object = friendInfos.get(i);

       final int position = i;
        if(object!=null){
            switch (object.type){
                case LAYOUT_ONE:
                    Log.e("URL+object.getImg()",context.getString(R.string.URL)+object.getImg());
                    if(object.getImg()==null){
                        ((MyViewHolderA)viewHolder).a_img.setImageResource(R.drawable.account);
                    }else {

                        Picasso.with(context)
                                .load(context.getString(R.string.URL)+object.getImg())
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.load)
                                .into(((MyViewHolderA)viewHolder).a_img);
                    }

                   // ((MyViewHolderA)viewHolder).a_img.setImageBitmap(StringToBitMap(object.getImg()));
                    ((MyViewHolderA)viewHolder).a_des_txt.setText(object.getDescription());
                    if(object.getDescription()!=null){
                        if(object.getDescription().equals("")){
                            ((MyViewHolderA)viewHolder).a_des_txt.setVisibility(View.GONE);
                            ((MyViewHolderA)viewHolder).a_name_txt.setVisibility(View.VISIBLE);
                        }else {//이렇게 명시를 해줘야 리사이클러뷰에서 갑자기 안보이는걸 막을수 있다
                            ((MyViewHolderA)viewHolder).a_des_txt.setVisibility(View.VISIBLE);
                            ((MyViewHolderA)viewHolder).a_name_txt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ((MyViewHolderA)viewHolder).a_des_txt.setVisibility(View.GONE);
                        ((MyViewHolderA)viewHolder).a_name_txt.setVisibility(View.VISIBLE);
                    }

                    ((MyViewHolderA)viewHolder).a_name_txt.setText(object.getName());
                    if(FRlistner!=null){
                        final int pos =i;

                        ((MyViewHolderA)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FRlistner.onItemClick(pos,v);
                            }
                        });
                    }
                    break;
                case LAYOUT_TWO:
                    if(object.getImg()==null){
                        ((MyViewHolderB)viewHolder).b_img.setImageResource(R.drawable.person);
                    }else {
                        Picasso.with(context)
                                .load(context.getString(R.string.URL)+object.getImg())
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.load)
                                .into(((MyViewHolderB)viewHolder).b_img);
                    }
                   // ((MyViewHolderB)viewHolder).b_img.setImageBitmap(StringToBitMap(object.getImg()));
                    ((MyViewHolderB)viewHolder).b_des_txt.setText(object.getDescription());
                    ((MyViewHolderB)viewHolder).b_name_txt.setText(object.getName());

                    if(object.getDescription()!=null){
                        if(object.getDescription().equals("")){
                            ((MyViewHolderB)viewHolder).b_des_txt.setVisibility(View.GONE);
                            ((MyViewHolderB)viewHolder).b_name_txt.setVisibility(View.VISIBLE);
                        }else {
                            ((MyViewHolderB)viewHolder).b_des_txt.setVisibility(View.VISIBLE);
                            ((MyViewHolderB)viewHolder).b_name_txt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ((MyViewHolderB)viewHolder).b_des_txt.setVisibility(View.GONE);
                        ((MyViewHolderB)viewHolder).b_name_txt.setVisibility(View.VISIBLE);
                    }

                    //체크박스 visibility
                    if(isChatAdd){
                        CheckBox c = (CheckBox) ((MyViewHolderB)viewHolder).b_checkbox;
                        c.setVisibility(View.VISIBLE);

                        //체크박스 상태 유지
                        if(object.getAddChecked()!=null){
                            c.setChecked(object.getAddChecked());
                        }else {
                            c.setChecked(false);
                        }
                        //체크박스 상태 유지

                        Log.e("CHECKBOXES",c.isChecked()+Integer.toString(position)+""+object.getAddChecked());
                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox checkBox = (CheckBox) v;
                                if(checkBox.isChecked()){
                                    object.setAddChecked(true);
                                    //check box 를 체크 상태를 저장 하기위해서는 그냥
                                    //모델 클래스에 boolean 변수를 넣고 바인드 뷰 홀더에서 boolean 변수 세팅을 해준다
                                    //여기서 관건은 final FriendInfo object = friendInfos.get(i); 을 선언해준다는것
                                    //그래야 boolean 변수를 세팅 할수 있다.
                                    AddedChatPeopleList.add(object.getIdx());
                                    Log.e("checkBox.isChecked()",checkBox.isChecked()+object.getIdx()+object.getAddChecked()+"");
                                }else {
                                    object.setAddChecked(false);
                                    AddedChatPeopleList.remove(object.getIdx());
                                    Log.e("checkBox.isChecked()",checkBox.isChecked()+object.getIdx()+object.getAddChecked()+"");
                                }
                            }
                        });


                    }
                    //체크박스 visibility
                    if(FRlistner!=null){
                        final int pos =i;
                        ((MyViewHolderB)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FRlistner.onItemClick(pos,v);
                            }
                        });
                        ((MyViewHolderB)viewHolder).b_checkbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FRlistner.onCheckBoxClick(pos,v);
                            }
                        });
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return friendInfos.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position==0)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }
}
