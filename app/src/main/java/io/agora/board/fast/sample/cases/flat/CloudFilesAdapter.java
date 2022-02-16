package io.agora.board.fast.sample.cases.flat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.misc.CloudFile;

public class CloudFilesAdapter extends RecyclerView.Adapter<CloudFilesAdapter.ViewHolder> {
    private List<CloudFile> cloudFiles;
    private OnItemClickListener onItemClickListener;

    public CloudFilesAdapter(List<CloudFile> cloudFiles) {
        this.cloudFiles = cloudFiles;
    }

    @NonNull
    @Override
    public CloudFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cloud_file, parent, false);
        return new CloudFilesAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CloudFile cloudFile = cloudFiles.get(position);

        viewHolder.icon.setImageResource(getImageResourceByType(cloudFile.type));
        viewHolder.name.setText(cloudFile.name);

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(cloudFile);
            }
        });
    }

    private int getImageResourceByType(String type) {
        switch (type) {
            case "png":
            case "jpg":
            case "jpeg":
                return R.drawable.ic_cloud_storage_image;
            case "pptx":
                return R.drawable.ic_cloud_storage_ppt;
            case "pdf":
                return R.drawable.ic_cloud_storage_pdf;
            case "mp4":
                return R.drawable.ic_cloud_storage_video;
        }
        return R.drawable.ic_cloud_storage_doc;
    }

    @Override
    public int getItemCount() {
        return cloudFiles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CloudFile cloudFile);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }
    }
}
