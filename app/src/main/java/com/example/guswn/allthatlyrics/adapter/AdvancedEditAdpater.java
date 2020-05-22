package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.extension.MyPhotoFilter;
import com.example.guswn.allthatlyrics.ui.social.activity.AdvancedEditPhotoActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.AdvancedImgModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvancedEditAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.show_edit_txt)
        TextView show_edit_txt;
        @BindView(R.id.show_edit_img)
        ImageView show_edit_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        AdvancedImgModel clikedEffect = editEffects.get(pos);
                        AdvancedEditPhotoActivity.myAdapter.isEdit =true;
                        AdvancedEditPhotoActivity.myAdapter.editedType = clikedEffect.getType();
                        AdvancedEditPhotoActivity.myAdapter.notifyDataSetChanged();

                      if (AdvancedEditPhotoActivity.myAdapter.isSingleEditNow) {
                          AdvancedEditPhotoActivity.EditedPreUploadFiles.get(AdvancedEditPhotoActivity.myAdapter.clickedpos).setType(clikedEffect.getType());
                      }else {
                          for ( AdvancedImgModel model : AdvancedEditPhotoActivity.EditedPreUploadFiles){
                              model.setType(clikedEffect.getType());
                          }
                      }

                    }
                }
            });
        }
    }

    private ArrayList<AdvancedImgModel> editEffects;
    private Context context;
    private String FirstFilePath;
    MyPhotoFilter myPhotoFilter;
    public AdvancedEditAdpater(ArrayList<AdvancedImgModel> editEffects, Context context, String FirstFilePath){
        this.editEffects = editEffects;
        this.context = context;
        this.FirstFilePath = FirstFilePath;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_edit_recycler_item_2,viewGroup,false);
        return new AdvancedEditAdpater.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final AdvancedImgModel object = editEffects.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        if(object!=null){
            myViewHolder.show_edit_txt.setText(context.getResources().getString(object.getType()));
            myPhotoFilter = new MyPhotoFilter(false,object.getType(),FirstFilePath,null,context,myViewHolder.show_edit_img);
            myPhotoFilter.photoFilterByType();
//            Glide.with(context)
//                    .load(FirstFilePath)
//                    .apply(bitmapTransform(new BlurTransformation(25, 3)))
//                    .into(myViewHolder.show_edit_img);
        }

        }

    @Override
    public int getItemCount() {
        return editEffects.size();
    }
}
