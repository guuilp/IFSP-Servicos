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
import br.com.lp.guilherme.ifspservicos.domain.Telefone;

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class TelefoneAdapter extends RecyclerView.Adapter<TelefoneAdapter.TelefoneViewHolder> {
    private final List<Telefone> telefones;
    private final Context context;
    private TelefoneOnClickListener telefoneOnClickListener;

    public TelefoneAdapter(List<Telefone> telefones, Context context, TelefoneOnClickListener telefoneOnClickListener) {
        this.telefones = telefones;
        this.context = context;
        this.telefoneOnClickListener = telefoneOnClickListener;
    }

    @Override
    public TelefoneViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_telefone, viewGroup, false);
        TelefoneViewHolder holder = new TelefoneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TelefoneViewHolder holder, final int position) {
        Telefone d = telefones.get(position);
        holder.tLocal.setText(d.local);
        holder.tNumero.setText(d.numero_formatado);
        //Click
        if (telefoneOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A variável position e final
                    telefoneOnClickListener.onClickTelefone(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.telefones != null ? this.telefones.size() : 0;
    }

    public interface TelefoneOnClickListener{
        public void onClickTelefone(View view, int idx);
    }

    public static class TelefoneViewHolder extends RecyclerView.ViewHolder{
        public TextView tLocal;
        public TextView tNumero;
        CardView cardView;

        public TelefoneViewHolder(View view) {
            super(view);
            tLocal = (TextView) view.findViewById(R.id.local);
            tNumero = (TextView) view.findViewById(R.id.numero);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
