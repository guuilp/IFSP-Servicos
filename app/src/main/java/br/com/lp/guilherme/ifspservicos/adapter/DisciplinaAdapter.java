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

/**
 * Created by Guilherme on 08-Sep-15.
 */
public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.DisciplinaViewHolder> {
    private final List<Disciplina> disciplinas;
    private final Context context;
    private DisciplinaOnClickListener disciplinaOnClickListener;

    public DisciplinaAdapter(List<Disciplina> disciplinas, Context context, DisciplinaOnClickListener disciplinaOnClickListener) {
        this.disciplinas = disciplinas;
        this.context = context;
        this.disciplinaOnClickListener = disciplinaOnClickListener;
    }

    @Override
    public DisciplinaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_disciplina, viewGroup, false);
        DisciplinaViewHolder holder = new DisciplinaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DisciplinaViewHolder holder, final int position) {
        Disciplina d = disciplinas.get(position);
        holder.tNome.setText(d.descricao);
        holder.tNota.setText(d.nota);
        holder.tFrequencia.setText(d.frequencia);
        holder.tDescricaoSituacao.setText(d.descricao_situacao);
        //Click
        if (disciplinaOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A variável position e final
                    disciplinaOnClickListener.onClickDisciplina(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.disciplinas != null ? this.disciplinas.size() : 0;
    }

    public interface DisciplinaOnClickListener{
        public void onClickDisciplina(View view, int idx);
    }

    public static class DisciplinaViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        public TextView tNota;
        public TextView tFrequencia;
        public TextView tDescricaoSituacao;

        CardView cardView;

        public DisciplinaViewHolder(View view) {
            super(view);
            tNome = (TextView) view.findViewById(R.id.nome);
            tNota = (TextView) view.findViewById(R.id.nota);
            tFrequencia = (TextView) view.findViewById(R.id.frequencia);
            tDescricaoSituacao = (TextView) view.findViewById(R.id.descricao_situacao);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}