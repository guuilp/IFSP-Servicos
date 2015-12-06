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
import br.com.lp.guilherme.ifspservicos.domain.DataAvaliacoes;
import br.com.lp.guilherme.ifspservicos.domain.Disciplina;

/**
 * Created by Guilherme on 05/12/2015.
 */
public class DataAvaliacoesAdapter extends RecyclerView.Adapter<DataAvaliacoesAdapter.DataAvaliacoesViewHolder>{
    private final List<DataAvaliacoes> datasAvaliacoes;
    private final Context context;
    private DataAvaliacoesOnClickListener dataAvaliacoesOnClickListener;

    public DataAvaliacoesAdapter(List<DataAvaliacoes> datasAvaliacoes, Context context, DataAvaliacoesOnClickListener dataAvaliacoesOnClickListener) {
        this.datasAvaliacoes = datasAvaliacoes;
        this.context = context;
        this.dataAvaliacoesOnClickListener = dataAvaliacoesOnClickListener;
    }

    @Override
    public DataAvaliacoesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_data_avaliacoes, viewGroup, false);
        DataAvaliacoesViewHolder holder = new DataAvaliacoesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DataAvaliacoesViewHolder holder, final int position) {
        DataAvaliacoes da = datasAvaliacoes.get(position);
        holder.tDescricaoDisciplina.setText(da.descricao_disciplina);
        holder.tDescricaoAvaliacao.setText(da.descricao_avaliacao);
        holder.tDataAvaliacao.setText(da.data_avaliacao);
        holder.tDiasRestantes.setText(da.dias_restantes);

        //Click
        if (dataAvaliacoesOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A variável position e final
                    dataAvaliacoesOnClickListener.onClickDataAvaliacoes(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.datasAvaliacoes != null ? this.datasAvaliacoes.size() : 0;
    }

    public interface DataAvaliacoesOnClickListener {
        public void onClickDataAvaliacoes(View view, int idx);
    }

    public static class DataAvaliacoesViewHolder extends RecyclerView.ViewHolder{
        public TextView tDescricaoDisciplina;
        public TextView tDescricaoAvaliacao;
        public TextView tDataAvaliacao;
        public TextView tDiasRestantes;
        CardView cardView;

        public DataAvaliacoesViewHolder(View view) {
            super(view);
            tDescricaoDisciplina = (TextView) view.findViewById(R.id.descricao_disciplina);
            tDescricaoAvaliacao = (TextView) view.findViewById(R.id.descricao_avaliacao);
            tDataAvaliacao = (TextView) view.findViewById(R.id.data_avaliacao);
            tDiasRestantes = (TextView) view.findViewById(R.id.dias_restantes);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
