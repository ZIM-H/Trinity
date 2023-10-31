using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class TouchToStartGame : MonoBehaviour
{
    public TextMeshProUGUI textObject; // 화면에 보이는 텍스트 Text 컴포넌트
    public Button button1; // 버튼 1
    public Button button2; // 버튼 2

    void Start()
    {
        // 게임 시작 시 버튼2를 비활성화
        button2.gameObject.SetActive(false);
    }

    void Update()
    {
        // 터치 입력을 확인 (모바일 터치 또는 데스크톱 클릭)
        if (Input.GetMouseButtonDown(0) || (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began))
        {
            // 텍스트를 비활성화
            textObject.gameObject.SetActive(false);

            // 버튼 1을 비활성화
            button1.gameObject.SetActive(true);

            // 버튼 2를 활성화
            button2.gameObject.SetActive(true);
        }
    }
}
