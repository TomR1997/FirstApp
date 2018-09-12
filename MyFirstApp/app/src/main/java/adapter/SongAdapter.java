package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.myfirstapp.R;

import java.util.List;

import dao.SongDAO;
import domain.Song;
import provider.MusicController;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private List<Song> mDataset;
    private Context context;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songTextView;
        TextView artistTextView;
        ImageButton textViewOptions;
        private MusicController musicController = new MusicController();
        private SongDAO songDao;

        MyViewHolder(View v) {
            super(v);
            songDao = new SongDAO(v.getContext());
            musicController.setSongs(songDao.getAll());
            songTextView = itemView.findViewById(R.id.songTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);

            /*songTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicController.play(view.getContext());
                }
            });*/
        }
    }

    public SongAdapter(List<Song> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_song, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.songTextView.setText(mDataset.get(position).getTitle());
        holder.artistTextView.setText(mDataset.get(position).getArtist().getName());
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.textViewOptions);
                popupMenu.inflate(R.menu.menu_song);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.action_add_queue:
                                Toast.makeText(context, "Add to queue", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_add_playlist:
                                Toast.makeText(context, "Add to playlist", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_delete_song:
                                Toast.makeText(context, "Delete song", Toast.LENGTH_SHORT).show();
                                //notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}