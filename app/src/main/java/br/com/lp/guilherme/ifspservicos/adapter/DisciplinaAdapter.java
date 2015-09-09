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
    public void onBindViewHolder(DisciplinaViewHolder holder, int position) {
        Disciplina d = disciplinas.get(position);
        holder.tNome.setText(d.nome);
        holder.tNota.setText(d.nota);
        holder.tFrequencia.setText(d.frequencia);
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
        CardView cardView;

        public DisciplinaViewHolder(View view) {
            super(view);
            tNome = (TextView) view.findViewById(R.id.nome);
            tNota = (TextView) view.findViewById(R.id.nota);
            tFrequencia = (TextView) view.findViewById(R.id.frequencia);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
