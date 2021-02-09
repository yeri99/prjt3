package com.example.sdsd;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sdsd.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private List<com.example.sdsd.SearchItem> searchList;
    private List<com.example.sdsd.SearchItem> searchListFull;

    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        SearchViewHolder(View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.text_view1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), com.example.sdsd.HowToRecycle.class);
                    intent.putExtra("name", searchList.get(getAdapterPosition()).getText1());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    SearchAdapter(List<com.example.sdsd.SearchItem> searchList){
        this.searchList = searchList;
        searchListFull = new ArrayList<>(searchList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        com.example.sdsd.SearchItem currentItem = searchList.get(position);
        holder.textView1.setText(currentItem.getText1());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<com.example.sdsd.SearchItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(searchListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(com.example.sdsd.SearchItem item : searchListFull){
                    if(item.getText1().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchList.clear();
            searchList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
