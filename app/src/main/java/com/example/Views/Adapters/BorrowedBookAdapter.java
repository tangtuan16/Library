package com.example.Views.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Contracts.NotificationContract;
import com.example.Models.BorrowedBook;
import com.example.Presenters.BorrowPresenter;
import com.example.Presenters.NotificationPresenter;
import com.example.btl_libary.R;

import java.util.List;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.BorrowedBookHolder>  {
    List<BorrowedBook> list;
    private Context context;
    private BorrowPresenter BorrowPresenter;

    public BorrowedBookAdapter(List<BorrowedBook> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public BorrowedBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowed_book_item, parent, false);
        return new BorrowedBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedBookHolder holder, int position) {
        BorrowedBook borrowedBook = list.get(position);
        holder.imgbbavt.setImageResource(borrowedBook.getAvt());
        holder.txtbbtitle.setText(borrowedBook.getTitle());
        holder.txtbbauthor.setText(borrowedBook.getAuthor());
        holder.txtbbcategory.setText(borrowedBook.getCategory());
        holder.txtbbtotal.setText(String.valueOf("Số lượng: " + borrowedBook.getTotal()));
        holder.txtbbborrowdate.setText("Ngày mượn: " + borrowedBook.getBorrowedDate());
        holder.txtbbreturndate.setText("Ngày trả: " + borrowedBook.getReturnDate());
        holder.txtbblocation.setText("Vị trí: " + borrowedBook.getPosition());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BorrowPresenter = new BorrowPresenter(context, (BookContract.View.BorrowBookView) context);
                                BorrowPresenter.deleteBorrowedBook(borrowedBook.getId());
                                BorrowPresenter.LoadBorrowedBooks();

                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class BorrowedBookHolder extends RecyclerView.ViewHolder {
        private TextView txtbbtitle, txtbbauthor, txtbbcategory,txtbbtotal, txtbbborrowdate, txtbbreturndate,txtbblocation;
        private ImageView imgbbavt;

        public BorrowedBookHolder(@NonNull View itemView) {
            super(itemView);
            imgbbavt = itemView.findViewById(R.id.imgbbavt);
            txtbbtitle = itemView.findViewById(R.id.txtbbtitle);
            txtbbauthor = itemView.findViewById(R.id.txtbbauthor);
            txtbbcategory = itemView.findViewById(R.id.txtbbcategory);
            txtbbtotal = itemView.findViewById(R.id.txtbbtotal);
            txtbbborrowdate = itemView.findViewById(R.id.txtbbborrowdate);
            txtbbreturndate = itemView.findViewById(R.id.txtbbreturndate);
            txtbblocation = itemView.findViewById(R.id.txtbblocation);

        }
    }
}
