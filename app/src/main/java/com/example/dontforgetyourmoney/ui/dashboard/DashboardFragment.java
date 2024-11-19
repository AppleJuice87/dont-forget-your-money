package com.example.dontforgetyourmoney.ui.dashboard;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.databinding.FragmentDashboardBinding;

import com.example.dontforgetyourmoney.data.model.Condition;

public class DashboardFragment extends Fragment {

    private Condition condition;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSearchTool.setOnClickListener(v -> showSetConditionDialog());

        return root;
    }

    private void showSetConditionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_condition, null);
        builder.setView(dialogView);

        EditText etKeyword = dialogView.findViewById(R.id.etKeyword);
        RadioGroup rgGrade = dialogView.findViewById(R.id.rgGrade);
        Spinner spIncomeBracket = dialogView.findViewById(R.id.spIncomeBracket);
        EditText etGPA = dialogView.findViewById(R.id.etGPA);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // 소득구간 스피너 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.income_brackets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIncomeBracket.setAdapter(adapter);

        // 기존 Condition 객체의 값이 존재하면 UI에 설정
        if (condition != null) {
            if (condition.getKeyword() != null) {
                etKeyword.setText(condition.getKeyword());
            }
            if (condition.getGrade() != null) {
                int gradeId = getRadioButtonIdForGrade(condition.getGrade());
                if (gradeId != -1) {
                    rgGrade.check(gradeId);
                }
            }
            if (condition.getIncomeBracket() != null) {
                String incomeBracketString = condition.getIncomeBracket().toString() + "구간";
                int spinnerPosition = adapter.getPosition(incomeBracketString);
                spIncomeBracket.setSelection(spinnerPosition);
            }
            if (condition.getRating() != null) {
                etGPA.setText(String.valueOf(condition.getRating()));
            }
        }

        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            // 입력된 값들을 Condition 객체에 저장
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
            Integer grade = getGradeFromRadioButtonId(selectedGradeId);

            // Condition 객체에 값 설정
            condition = new Condition(keyword, grade, incomeBracket, gpa);

            Log.d("DashboardFragment", "Condition: " + condition.toString());

            dialog.dismiss();
        });

        dialog.show();
    }

    private int getRadioButtonIdForGrade(Integer grade) {
        switch (grade) {
            case 1:
                return R.id.rbGrade1;
            case 2:
                return R.id.rbGrade2;
            case 3:
                return R.id.rbGrade3;
            case 4:
                return R.id.rbGrade4;
            default:
                return -1;
        }
    }

    private Integer getGradeFromRadioButtonId(int radioButtonId) {
        if (radioButtonId == R.id.rbGrade1) {
            return 1;
        } else if (radioButtonId == R.id.rbGrade2) {
            return 2;
        } else if (radioButtonId == R.id.rbGrade3) {
            return 3;
        } else if (radioButtonId == R.id.rbGrade4) {
            return 4;
        } else {
            return null; // 선택되지 않은 경우
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}