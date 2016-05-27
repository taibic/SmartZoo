package com.br.smartzoo.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.smartzoo.R;
import com.br.smartzoo.model.entity.Veterinary;
import com.br.smartzoo.model.interfaces.OnManageVeterinary;
import com.br.smartzoo.util.AlertDialogUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by adenilson on 26/05/16.
 */
public class VeterinaryListAdapter extends RecyclerView.Adapter<VeterinaryListAdapter.ViewHolder>
         {

    private OnManageVeterinary mOnManageVeterinary;
    private List<Veterinary> mVeterinaryList;
    private Activity mContext;
    private AlertDialog.Builder mAlertDialog;

    public VeterinaryListAdapter(Activity context, List<Veterinary> veterinaries) {
        this.mVeterinaryList = veterinaries;
        this.mContext = context;
    }

    public void addOnManageVeterinary(OnManageVeterinary onManageVeterinary) {
        this.mOnManageVeterinary = onManageVeterinary;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_veterinary_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Veterinary veterinary = mVeterinaryList.get(position);
        Glide.with(mContext).load(veterinary.getImage()).into(holder.mImageViewVeterinary);
        Glide.with(mContext).load(R.drawable.ic_demit).into(holder.mImageViewDemission);
        Glide.with(mContext).load(R.drawable.ic_salary).into(holder.mImageViewSalary);
        Glide.with(mContext).load(R.drawable.ic_treatments).into(holder.mImageViewAnimalsTreatments);
        holder.mTextViewName.setText(veterinary.getName());
        holder.mTextViewAge.setText(String.valueOf(veterinary.getAge()));
        holder.mTextViewSalary.setText(String.valueOf(veterinary.getSalary()));
        holder.mTextViewStatus.setText(veterinary.getStatus());
        holder.mTextViewNumberTreatments.setText(veterinary.getNumberAnimalTreated());

        addListenerToDemission(holder);
        addListenerToSalary(holder);

    }

    private void addListenerToSalary(ViewHolder holder) {


        holder.mImageViewSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addListenerToDemission(ViewHolder holder) {

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOnManageVeterinary.onDemit();
            }
        };


        holder.mImageViewDemission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog = AlertDialogUtil.makeAlertDialogExit(mContext,
                        mContext.getString(R.string.title_demission),
                        mContext.getString(R.string.message_sure), listener);

                mAlertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mVeterinaryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewVeterinary;
        private TextView mTextViewName;
        private TextView mTextViewAge;
        private TextView mTextViewSalary;
        private TextView mTextViewStatus;
        private ImageView mImageViewDemission;
        private ImageView mImageViewSalary;
        private TextView mTextViewNumberTreatments;
        private ImageView mImageViewAnimalsTreatments;


        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewVeterinary = (ImageView) itemView.findViewById(R.id.image_view_veterinary);
            mTextViewName = (TextView) itemView.findViewById(R.id.text_view_name_veterinary);
            mTextViewAge = (TextView) itemView.findViewById(R.id.text_view_age_veterinary);
            mTextViewSalary = (TextView) itemView.findViewById(R.id.text_view_salary_veterinary);
            mTextViewStatus = (TextView) itemView.findViewById(R.id.text_view_status_veterinary);
            mImageViewDemission = (ImageView) itemView.findViewById(R.id.image_view_demission);
            mImageViewSalary = (ImageView) itemView.findViewById(R.id.image_view_salary);
            mTextViewNumberTreatments = (TextView) itemView.findViewById(R.id.text_view_number_treatment);
            mImageViewAnimalsTreatments = (ImageView) itemView.findViewById(R.id.image_view_animals_treatments);
        }
    }
}