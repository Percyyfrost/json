package com.example.vuyaninxele.sight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vuyani.nxele on 2018/02/23.
 */

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.DataObjectHolder> {

    private Context context;
    private List<Sites> mDataset;
    private List<Sites> Sitesfiltered;
    private ClickListener clickListener = null;

    public SitesAdapter(ArrayList<Sites> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        context = parent.getContext();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.SiteName.setText(mDataset.get(position).getSiteName());
        holder.SiteNumber.setText(String.valueOf(mDataset.get(position).getSiteNumber()));

    }

    @Override
    public int getItemCount() {
        return  mDataset.size();
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView SiteName;
        TextView SiteNumber;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            SiteName = (TextView) itemView.findViewById(R.id.name);
            SiteNumber = (TextView) itemView.findViewById(R.id.sitenr);
            //image = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.itemClicked( mDataset, view, getAdapterPosition());
        }

    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Sitesfiltered = mDataset;
                } else {
                    List<Sites> filteredList = new ArrayList<>();

                    for (Sites row : mDataset) {

                            Log.d("INSIDE FILTER", row.getSiteName());
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSiteName().contains(charString.toLowerCase()) || row.getSiteNumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    Sitesfiltered = (ArrayList<Sites>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Sitesfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Sitesfiltered = (ArrayList<Sites>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(ClickListener clicklistener) {
        this.clickListener = clicklistener;
    }
}
