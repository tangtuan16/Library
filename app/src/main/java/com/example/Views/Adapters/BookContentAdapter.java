    package com.example.Views.Adapters;

    import android.graphics.Color;
    import android.text.Spannable;
    import android.text.SpannableString;
    import android.text.style.BackgroundColorSpan;
    import android.util.TypedValue;
    import android.view.ActionMode;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;

    import androidx.recyclerview.widget.RecyclerView;

    import com.example.btl_libary.R;

    import java.util.List;

    public class BookContentAdapter extends RecyclerView.Adapter<BookContentAdapter.BookViewHolder> {
        private List<String> textChunks;
        private float currentTextSize;


        public BookContentAdapter(List<String> textChunks) {
            this.textChunks = textChunks;
            this.currentTextSize = -1; // Chưa được thiết lập
        }

        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_content, parent, false);
            return new BookViewHolder(view);
        }
        public void updateTextSize(float newSize) {
            this.currentTextSize = newSize;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            String modifiedText = textChunks.get(position).replace(" ", "  ");
            holder.textViewContent.setText(modifiedText);

            if (currentTextSize > 0) {
                holder.textViewContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);
            }

            holder.textViewContent.setCustomSelectionActionModeCallback(new ActionMode.Callback() {


                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    menu.add(0, 1, 0, "Highlight");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int start = holder.textViewContent.getSelectionStart();
                    int end = holder.textViewContent.getSelectionEnd();
                    Spannable spannableText = new SpannableString(holder.textViewContent.getText());
                    if (item.getItemId() == 1) {
                        spannableText.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.textViewContent.setText(spannableText);
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return textChunks.size();
        }

        public static class BookViewHolder extends RecyclerView.ViewHolder {
            TextView textViewContent;

            public BookViewHolder(View itemView) {
                super(itemView);


                textViewContent = itemView.findViewById(R.id.textViewContent);

            }
        }
    }
