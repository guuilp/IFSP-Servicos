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
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;
import br.com.lp.guilherme.ifspservicos.domain.Nota;

/**
 * Created by Guilherme on 29/11/2015.
 */
public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {
    private final List<Nota> notas;
    private final Context context;
    private NotaOnClickListener notaOnClickListener;

    public NotaAdapter(List<Nota> notas, Context context, NotaOnClickListener notaOnClickListener) {
        this.notas = notas;
        this.context = context;
        this.notaOnClickListener = notaOnClickListener;
    }

    @Override
    public NotaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_nota, viewGroup, false);
        NotaViewHolder holder = new NotaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NotaViewHolder holder, final int position) {
        Nota n = notas.get(position);
        holder.tDescAvaliacao.setText(n.descricao_avaliacao);
        holder.tDataAvaliacao.setText(n.data_avaliacao);
        holder.tNotaAvaliacao.setText(n.nota_avaliacao);
        holder.tPesoAvaliacao.setText(n.peso_avaliacao);
        //Click
        if (notaOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A variável position e final
                    notaOnClickListener.onClickNota(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.notas != null ? this.notas.size() : 0;
    }

    public interface NotaOnClickListener{
        public void onClickNota(View view, int idx);
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder{
        public TextView tDescAvaliacao;
        public TextView tDataAvaliacao;
        public TextView tNotaAvaliacao;
        public TextView tPesoAvaliacao;

        public NotaViewHolder(View view) {
            super(view);
            tDescAvaliacao = (TextView) view.findViewById(R.id.descricao_avaliacao);
            tDataAvaliacao = (TextView) view.findViewById(R.id.data_avaliacao);
            tNotaAvaliacao = (TextView) view.findViewById(R.id.nota_avaliacao);
            tPesoAvaliacao = (TextView) view.findViewById(R.id.peso_avaliacao);
        }
    }
}
