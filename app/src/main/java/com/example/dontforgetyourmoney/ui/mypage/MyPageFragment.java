package com.example.dontforgetyourmoney.ui.mypage;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Condition;
import com.example.dontforgetyourmoney.data.repository.ConditionRepository.ConditionRepository;
import com.example.dontforgetyourmoney.databinding.DialogSetConditionBinding;
import com.example.dontforgetyourmoney.databinding.FragmentMypageBinding;
import com.example.dontforgetyourmoney.ui.dashboard.DashboardFragment;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.hilt.android.AndroidEntryPoint;
import jakarta.inject.Inject;

@AndroidEntryPoint
public class MyPageFragment extends Fragment {

    private FragmentMypageBinding binding;

    @Inject
    ConditionRepository conditionRepository;

    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSetMyCondition.setOnClickListener(v -> showSetConditionDialog());

        refreshMyConditionText();
        return root;
    }

    private void refreshMyConditionText() {
        if (binding.tvMyCondition.getText().equals(": 선택된 검색조건 없음")) {

        }else{
            binding.tvMyCondition.setText("");
        }
    }

    private void showSetConditionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogSetConditionBinding dialogBinding = DialogSetConditionBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());

        EditText etKeyword = dialogBinding.etKeyword;
        RadioGroup rgGrade = dialogBinding.rgGrade;
        Spinner spIncomeBracket = dialogBinding.spIncomeBracket;
        EditText etGPA = dialogBinding.etGPA;
        Button btnCancel = dialogBinding.btnCancel;
        Button btnConfirm = dialogBinding.btnConfirm;

        //! 나의 조건 설정 화면에 맞게 text 바꿔주기
        dialogBinding.tvKeyword.setText("나의 키워드 설정");
        dialogBinding.btnConfirm.setText("저장");

        // SharedPreferences 에서 컨디션 객체 가져오기
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my-condition", Context.MODE_PRIVATE);
        String conditionJson = sharedPreferences.getString("my-condition", null);
        Condition myCondition = null;
        if (conditionJson != null) { // SharedPreferences 에 이미 저장된 my-condition key-value가 존재하면, 객체로 변환
            myCondition = new Gson().fromJson(conditionJson, Condition.class);
        } else { // SharedPreferences 에 이미 저장된 my-condition key-value가 없으면, 새로운 Condition 객체 생성
            myCondition = new Condition(null, null, null, null);
        }   

        // 소득구간 스피너 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.income_brackets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spIncomeBracket.setAdapter(adapter);

        // 기존 myCondition 객체의 값이 존재하면 UI에 set
        if (myCondition != null) {
            if (myCondition.getKeyword() != null) {
                etKeyword.setText(myCondition.getKeyword());
            }
            if (myCondition.getGrade() != null) {
                int gradeId = DashboardFragment.getRadioButtonIdForGrade(myCondition.getGrade());
                if (gradeId != -1) {
                    rgGrade.check(gradeId);
                }
            }
            if (myCondition.getIncomeBracket() != null) {
                String incomeBracketString = myCondition.getIncomeBracket().toString() + "구간";
                int spinnerPosition = adapter.getPosition(incomeBracketString);
                spIncomeBracket.setSelection(spinnerPosition);
            }
            if (myCondition.getRating() != null) {
                etGPA.setText(String.valueOf(myCondition.getRating()));
            }
        }

        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            // 입력된 값들을 myCondition 객체에 저장
            String keyword = etKeyword.getText().toString().trim();
            int selectedGradeId = rgGrade.getCheckedRadioButtonId();
            String incomeBracketString = spIncomeBracket.getSelectedItem().toString();
            String gpaText = etGPA.getText().toString().trim();

            // GPA 값이 입력되지 않은 경우 null로 설정
            Double gpa = null;
            if (!gpaText.isEmpty()) {
                // GPA 입력 형식 검증
                if (!gpaText.matches("^([0-4](\\.\\d{1,2})?|4\\.5)$")) {
                    etGPA.setError("올바른 형식의 학점 평점을 입력하세요.");
                    return;
                }

                // GPA 값을 double로 변환
                gpa = Double.parseDouble(gpaText);

                // GPA 값 범위 검증
                if (gpa < 0.0 || gpa > 4.5) {
                    etGPA.setError("학점 평점은 0.0과 4.5 사이여야 합니다.");
                    return;
                }
            }

            // "모두보기"가 아닌 경우에만 Integer로 변환
            Integer incomeBracket = null;
            if (!incomeBracketString.equals("모두보기")) {
                incomeBracket = Integer.parseInt(incomeBracketString.replace("구간", ""));
            }

            // 라디오 버튼 ID를 학년 값으로 변환
            Integer grade = DashboardFragment.getGradeFromRadioButtonId(selectedGradeId);

            //! DashBoard 의 singleton 객체에는 이렇게 하면안됨.
            //! condition = new Condition(keyword, grade, incomeBracket, gpa);

            Condition finalCondition = new Condition(keyword, grade, incomeBracket, gpa);

            Log.d("DashboardFragment", "나의 컨디션 세팅됨 Condition: " + finalCondition.toString());
            //binding.tvCondition.setText(finalCondition.getConditions());

            //* finalCondition 을 SharedPreferences 에 저장
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("my-condition", new Gson().toJson(finalCondition));
            editor.apply();

            refreshMyConditionText();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
