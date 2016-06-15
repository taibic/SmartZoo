package com.br.smartzoo.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.smartzoo.R;
import com.br.smartzoo.model.helper.AnimalHelper;
import com.br.smartzoo.model.entity.Cage;
import com.br.smartzoo.model.interfaces.OnManageCage;
import com.br.smartzoo.util.AlertDialogUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by adenilson on 07/06/16.
 */

public class CageListAdapter extends RecyclerView.Adapter<CageListAdapter.ViewHolder> {
    private List<Cage> mCageList;
    private Activity mContext;
    private OnManageCage mOnManageCage;

    public CageListAdapter(Activity context, List<Cage> cages) {
        this.mCageList = cages;
        this.mContext = context;
    }

    public void addOnManageCage(OnManageCage onManageCage) {
        this.mOnManageCage = onManageCage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater.from(mContext).inflate(R.layout.item_cage_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Cage cage = mCageList.get(position);

        Glide.with(mContext).load(R.drawable.ic_cage).into(holder.mImageViewCage);
        Glide.with(mContext).load(R.drawable.ic_destroy).into(holder.mImageViewDestroy);
        Glide.with(mContext).load(R.drawable.ic_clean).into(holder.mImageViewClean);
        Glide.with(mContext).load(AnimalHelper.getImageByType(mContext, cage.getAnimalType()))
                .into(holder.mImageViewTypeAnimal);
        holder.mTextViewName.setText(cage.getName());

        if(cage.getAvaibleSpace()>0)
            holder.mTextViewCapacity.setTextColor(ContextCompat.getColor(mContext,R.color.green_500));
        else
            holder.mTextViewCapacity.setTextColor(ContextCompat.getColor(mContext,R.color.red_500));

        holder.mTextViewCapacity.setText(mContext.getString(R.string.label_avaible_space) + cage.getAvaibleSpace() + mContext.getString(R.string.label_avaible_space_animals));

        holder.mImageViewClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnManageCage.onCleanCage(cage);
            }
        });


        final DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOnManageCage.onDestroyCage(cage);
            }
        };

        holder.mImageViewDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.makeConfirmationDialog(mContext
                        , mContext.getString(R.string.title_cage), mContext.getString(R.string.message_sure)
                        , okListener).show();

            }
        });

        if(cage.isClean()) {
            holder.mTextViewStatus.setText(R.string.text_clean);
            holder.mTextViewStatus.setTextColor(Color.parseColor("#43A047"));
        }else{
            holder.mTextViewStatus.setText(R.string.text_dirt);
            holder.mTextViewStatus.setTextColor(Color.parseColor("#E53935"));
        }

    }

    @Override
    public int getItemCount() {
        return mCageList.size();
    }

    public void removeCage(Cage cage) {
        if(mCageList.contains(cage)){
            mCageList.remove(cage);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewCage;
        private ImageView mImageViewDestroy;
        private ImageView mImageViewClean;
        private TextView mTextViewName;
        private TextView mTextViewCapacity;
        private ImageView mImageViewTypeAnimal;
        private TextView mTextViewStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewStatus = (TextView) itemView.findViewById(R.id.text_view_status_cage);
            mImageViewCage = (ImageView) itemView.findViewById(R.id.image_view_list_cage);
            mImageViewDestroy = (ImageView) itemView.findViewById(R.id.image_view_destroy);
            mImageViewClean = (ImageView) itemView.findViewById(R.id.image_view_clean);
            mTextViewName = (TextView) itemView.findViewById(R.id.text_view_name_list_cage);
            mTextViewCapacity = (TextView) itemView.findViewById(R.id.text_view_capacity_list_cage);
            mImageViewTypeAnimal = (ImageView) itemView.findViewById(R.id.image_view_type_animal);
        }
    }

    public void setCageList(List<Cage> cages){
        this.mCageList = cages;
        notifyDataSetChanged();
    }

    public void cleanCage(Cage cage){
        for(Cage c : mCageList){
            if(c.equals(cage)){
                c.setIsClean(true);
                notifyDataSetChanged();
                break;
            }
        }
    }
}