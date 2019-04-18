package com.litvinov.globustest.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.litvinov.globustest.R;
import com.litvinov.globustest.data.Valute;
import com.litvinov.globustest.data.source.ValuteRepository;
import com.litvinov.globustest.util.Injection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment implements MainContract.View {

    private static final String TAG = MainFragment.class.getSimpleName();
    private TextInputLayout tilCurrencyFromAmount;
    private TextInputLayout tilCurrencyToAmount;
    private Spinner spCurrencyFromCharCode;
    private Spinner spCurrencyToCharCode;
    private MainContract.Presenter presenter;

    public MainFragment() { }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        ValuteRepository valuteRepository = Injection.provideValuteRepository(getActivity().getApplicationContext());
        presenter = new MainPresenter(valuteRepository);
        presenter.attach(this);
    }

    private void setUpViews(View view){
        tilCurrencyFromAmount = view.findViewById(R.id.til_currency_from_amount);
        tilCurrencyToAmount = view.findViewById(R.id.til_currency_to_amount);
        spCurrencyFromCharCode = view.findViewById(R.id.sp_currency_from_charcode);
        spCurrencyToCharCode = view.findViewById(R.id.sp_currency_to_charcode);
        Button btnExchange = view.findViewById(R.id.btn_exchange);
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.doExchange(tilCurrencyFromAmount.getEditText().getText().toString(), (Valute) spCurrencyFromCharCode.getSelectedItem(), (Valute) spCurrencyToCharCode.getSelectedItem());
                Log.d(TAG, "onClick: ");
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        presenter.loadExchangeData();
    }

    @Override
    public void setUpCurrencyCharCodes(List<Valute> valutes) {
        Log.d(TAG, "setUpCurrencyCharCodes: " + valutes.size());
        spCurrencyFromCharCode.setAdapter(new ArrayAdapter<Valute>(getContext(), android.R.layout.simple_dropdown_item_1line, valutes));
        spCurrencyToCharCode.setAdapter(new ArrayAdapter<Valute>(getContext(), android.R.layout.simple_dropdown_item_1line, valutes));
        if (valutes.size() > 0){
            spCurrencyFromCharCode.setSelection(0);
            spCurrencyToCharCode.setSelection(0);
        }
    }

    @Override
    public void setExchangedAmount(String amount) {
        tilCurrencyToAmount.getEditText().setText(amount);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
