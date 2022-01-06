package io.agora.board.fast.sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.agora.board.fast.sample.misc.TestCase;

public class TestCaseAdapter extends RecyclerView.Adapter<TestCaseAdapter.ViewHolder> {
    private List<TestCase> testcases;
    private OnItemClickListener onItemClickListener;

    public TestCaseAdapter(List<TestCase> testcases) {
        this.testcases = testcases;
    }

    @NonNull
    @Override
    public TestCaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testcase_card, parent, false);
        return new TestCaseAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TestCase testCase = testcases.get(position);

        viewHolder.desc.setText(testCase.describe);
        viewHolder.theme.setText(testCase.theme);
        viewHolder.live.setVisibility(testCase.isLive() ? View.VISIBLE : View.GONE);

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(testCase);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testcases.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView theme;
        public TextView desc;
        public View live;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            theme = itemView.findViewById(R.id.theme);
            desc = itemView.findViewById(R.id.describe);
            live = itemView.findViewById(R.id.live);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TestCase testCase);
    }
}
