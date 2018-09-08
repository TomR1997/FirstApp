package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tom.myfirstapp.R;

import java.util.List;

import domain.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private List<Song> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView songTextView;
        TextView artistTextView;

        MyViewHolder(View v) {
            super(v);
            songTextView = itemView.findViewById(R.id.songTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SongAdapter(List<Song> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_song, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.songTextView.setText(mDataset.get(position).getTitle());
        holder.artistTextView.setText(mDataset.get(position).getArtist().getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}