package com.example.christiansoeappproject.ui.admins;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christiansoeappproject.databinding.FragmentAdminBinding;
import com.example.christiansoeappproject.ui.admin.AdminActivity;

public class AdminFragment extends Fragment implements View.OnClickListener {

    private AdminViewModel adminViewModel;
    private FragmentAdminBinding binding;
    private Button loginButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loginButton = binding.loginButton;
        loginButton.setOnClickListener(this);

        final TextView textView = binding.passwordEditText;
        adminViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AdminActivity.class);
        EditText pas = binding.passwordEditText;
        String password = pas.getText().toString();
        String expected = "1";
        if(password.equals(expected)){
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
        }
    }
}