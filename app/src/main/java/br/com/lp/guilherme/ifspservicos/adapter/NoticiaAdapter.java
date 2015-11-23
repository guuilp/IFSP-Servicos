package br.com.lp.guilherme.ifspservicos.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.lp.guilherme.ifspservicos.R;
import br.com.lp.guilherme.ifspservicos.domain.Noticia;

/**
 * Created by Guilherme on 22-Nov-15.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {
    private final List<Noticia> noticias;
    private final Context context;
    private NoticiaOnClickListener noticiaOnClickListener;

    public NoticiaAdapter(List<Noticia> noticias, Context context, NoticiaOnClickListener noticiaOnClickListener) {
        this.noticias = noticias;
        this.context = context;
        this.noticiaOnClickListener = noticiaOnClickListener;
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_noticia, viewGroup, false);
        NoticiaViewHolder holder = new NoticiaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NoticiaViewHolder holder, final int position) {
        Noticia n = noticias.get(position);
        holder.tTitulo.setText(n.titulo);
        //Click
        if (noticiaOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A variável position e final
                    noticiaOnClickListener.onClickNoticia(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.noticias != null ? this.noticias.size() : 0;
    }

    public interface NoticiaOnClickListener{
        public void onClickNoticia(View view, int idx);
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder{
        public TextView tTitulo;
        CardView cardView;

        public NoticiaViewHolder(View view) {
            super(view);
            tTitulo = (TextView) view.findViewById(R.id.titulo);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

}
