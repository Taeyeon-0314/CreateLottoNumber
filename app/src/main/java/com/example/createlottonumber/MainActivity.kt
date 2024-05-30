package com.example.createlottonumber

import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    // 초기화
    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    // 번호를 표시할 TextView 목록 초기화
    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    // 로또 번호를 생성했는지 여부 저장
    private var didRun = false

    //사용자가 선택한 번호를 저장할 Set
    private val pickNumberSet = hashSetOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // NuberPicker의 최소값과 최대값을 설정
        numPick.minValue = 1
        numPick.maxValue = 45

        // 버튼 초기화 함수 호출
        initAddButton()
        initRunButton()
        initCLearButton()

    }

    // "추가" 버튼 초기화
    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                // 로또 번호를 이미 생성한 경우
                didRun -> showToast("초기화 후에 시도해주세요.")
                // 최대 5개까지 번호를 선택할 수 있음
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                // 이미 선택된 번호를 선택하려고 하는 경우
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택된 숫자입니다.")
                else -> {
                    // 선택된 번호를 표시할 TextView 설정
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()
                    // 번ㅎ에 따라 배경을 설정
                    setNumBack(numPick.value, textView)
                    //선택된 번호를 Set에 추가
                    pickNumberSet.add(numPick.value)
                }
            }
        }
    }

    // "초기화" 버튼을 초기화
    private fun initCLearButton() {
        clearButton.setOnClickListener {
            // 선택된 번호를 모두 지움
            pickNumberSet.clear()
            // 모든 TextView를 숨김
            numTextViewList.forEach { it.isVisible = false }
            didRun = false
            // NumberPicker의 값을 초기값으로 설정
            numPick.value = 1
        }
    }

    // "실행" 버튼을 초기화
    private fun initRunButton() {
        runButton.setOnClickListener {
            // 무작위 번호를 생성
            val list = getRandom()
            didRun = true
            // 생성된 번호를 TextView에 표시
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    // 무작위로 번호를 생성하여 반환
    private fun getRandom(): List<Int> {
        // 1부터 45까지의 번호 중 선택되지 않은 번호를 필터링
        val numbers = (1..45).filter { it !in pickNumberSet }
        // 선택된 번호와 무작위로 선택된 번호를 합하여 정렬
        return (pickNumberSet + numbers.shuffled().take(6 - pickNumberSet.size)).sorted()
    }

    // 번호에 따라 TextView의 배경을 설정
    private fun setNumBack(number: Int, textView: TextView) {
        // 번호 범위에 따라 배경 리소스를 설정
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        // 배경 설정
        textView.background = ContextCompat.getDrawable(this, background)
    }

    // Toast 메시지를 표시
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
