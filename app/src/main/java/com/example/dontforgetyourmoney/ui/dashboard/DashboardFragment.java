package com.example.dontforgetyourmoney.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Post;
import com.example.dontforgetyourmoney.data.repository.PostRepository.PostRepository;
import com.example.dontforgetyourmoney.databinding.FragmentDashboardBinding;

import com.example.dontforgetyourmoney.data.model.Condition;
import com.example.dontforgetyourmoney.ui.home.PostAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    //* App-level 에서 Condition 객체 하나를 관리 해 주기 위해서 Hilt 를 이용한 싱글톤 패턴 사용.
    @Inject
    @Named("SearchCondition")
    Condition condition;

    @Inject
    PostRepository postRepository;

    private FragmentDashboardBinding binding;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private Handler mainHandler = new Handler(Looper.getMainLooper()); //! 메인 스레드 핸들러

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSearchTool.setOnClickListener(v -> showSetConditionDialog());

        //Log.d("DashboardFragment", "컨디션 텍스트: " + condition.toString());
        binding.tvCondition.setText(condition.getConditions());
        binding.btnClearCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition.clear();
                binding.tvCondition.setText(condition.getConditions());
                Toast.makeText(getContext(), "검색조건이 초기화 되었습니다", Toast.LENGTH_SHORT).show();
                refreshPostsByCondition();
            }
        });

        //* RecyclerView 세팅
        //recyclerView = binding.recyclerViewPostsByConditions;
        recyclerView = root.findViewById(R.id.recyclerViewPostsByConditions);
        postAdapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);

        refreshPostsByCondition();
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

        // 기존 Condition 객체의 값이 존재하면 UI에 set
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
            //! condition = new Condition(keyword, grade, incomeBracket, gpa);

            condition.setKeyword(keyword);
            condition.setGrade(grade);
            condition.setIncomeBracket(incomeBracket);
            condition.setRating(gpa);

            Log.d("DashboardFragment", "컨디션 세팅됨 Condition: " + condition.toString());

            binding.tvCondition.setText(condition.getConditions());
            refreshPostsByCondition();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void refreshPostsByCondition() {

        new Thread(() -> {
            // keyword 가 존재하면 변수에 저장, 없으면 "" 저장
            String keyword = condition.getKeyword() != null ?
                    condition.getKeyword().replaceAll("\\s+", "") : "";

            Integer grade = condition.getGrade() != null ? condition.getGrade() : 0;
            Integer incomeBracket = condition.getIncomeBracket() != null ? condition.getIncomeBracket() : 0;
            Double rating = condition.getRating() != null ? condition.getRating() : null;

            //List<Post> posts = postRepository.getAllPosts();
            List<Post> posts = Collections.synchronizedList(postRepository.getAllPosts());

            // 조건에 맞는 게시물 필터링
//            List<Post> filteredPosts = posts.stream()
//                    .filter(post -> keyword.isBlank() ||
//                            post.getTitle().contains(keyword) ||
//                            post.getContent().replaceAll("\\s+", "").contains(keyword))
//
//                    .filter(post -> grade == 0 || post.getGrade().contains(grade.toString()))
//                    .filter(post -> Double.parseDouble(post.getIncomeBracket()) <= incomeBracket)
//                    //.filter(post -> post.getRating() == rating)
//                    .collect(Collectors.toList());

            //* 1. 키워드 검색

            if (keyword.isBlank()) {
                // 조건 적용 x
            } else {
                // posts = posts.stream()
                //         .filter(post ->
                //                 (post.getTitle().contains(keyword) ||
                //                         post.getContent().replaceAll("\\s+", "").contains(keyword)))
                //         .collect(Collectors.toList());

                List<Post> filteredPosts = new ArrayList<>();
                for (Post post : posts) {
                    if (post.getTitle().contains(keyword) || post.getContent().replaceAll("\\s+", "").contains(keyword)) {
                        filteredPosts.add(post);
                    }
                }
                posts = null;
                posts = filteredPosts;
            }

            //* 2. 학년 검색

            if (grade == 0) {
                // 조건 적용 x
            } else {
                // posts = posts.stream()
                //         .filter(post -> post.getGrade().equals("판별불가") || post.getGrade().contains(grade.toString()))
                //         .collect(Collectors.toList());

                List<Post> filteredPosts = new ArrayList<>();
                for (Post post : posts) {
                    if (post.getGrade().equals("판별불가") || post.getGrade().contains(grade.toString())) {
                        filteredPosts.add(post);
                    }
                }
                posts = null;
                posts = filteredPosts;
            }

            //* 3. 소득구간 검색

            // posts = posts.stream()
            //         .filter(post -> {
            //             String str = post.getIncomeBracket();
            //             if (str.equals("판별불가")) return true;
            //             int low = Integer.parseInt(str.trim().split("-")[0]);
            //             int high = Integer.parseInt(str.trim().split("-")[1]);
            //             return low <= incomeBracket && incomeBracket <= high;
            //         })
            //         .collect(Collectors.toList());


            if(incomeBracket != 0){
                List<Post> filteredPosts = new ArrayList<>();
                for (Post post : posts) {
                    String str = post.getIncomeBracket();
                    if (str.equals("판별불가")) {
                        filteredPosts.add(post);
                    } else {
                        int low = Integer.parseInt(str.trim().split("-")[0]);
                        int high = Integer.parseInt(str.trim().split("-")[1]);
                        if (low <= incomeBracket && incomeBracket <= high) {
                            filteredPosts.add(post);
                        }
                    }
                }
                posts = null;
                posts = filteredPosts;
            }

            //* 4. 평점 검색

            if (rating != null) {
                posts = posts.stream()
                        .filter(post -> {
                            String str = post.getRating()
                                    .replaceAll("\\s+", "")
                                    .replaceAll("이상", "");

                            if (str.equals("판별불가") || !str.contains(".")) return true;
                            try {
                                return Double.parseDouble(str) <= rating;
                            } catch (NumberFormatException e) {
                                Log.d("DashboardFragment", "평점 판별 오류: " + e.getMessage());
                                return true;
                            }
                        })
                        .collect(Collectors.toList());
            }


            // UI 업데이트는 메인 스레드에서 수행해야 하므로 핸들러 사용
            List<Post> finalPosts = posts;

            // 모든 포스트 toString()에 대한 로그 출력
//            for (Post post : finalPosts) {
//                Log.d("DashboardFragment",
//                        post.getTitle() +
//                        post.getDate() +
//                        post.getGrade() +
//                        post.getIncomeBracket() +
//                        post.getRating());
//            }

            mainHandler.post(() -> postAdapter.setPosts(finalPosts));
        }).start();
    }

    public static int getRadioButtonIdForGrade(Integer grade) {
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

    public static Integer getGradeFromRadioButtonId(int radioButtonId) {
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