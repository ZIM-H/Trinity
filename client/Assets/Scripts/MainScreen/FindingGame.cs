using UnityEngine;
using TMPro;

public class FindingGame : MonoBehaviour
{
    public GameObject button1; // 버튼 1 게임 시작 (매칭)
    public GameObject button2; // 버튼 2 튜토리얼
    public GameObject button3; // 버튼 3 매칭 취소
    public TextMeshProUGUI findingText; // "Finding a Game..." TextMeshProUGUI

    public void StartFindingGame()
    {
        // 버튼 1과 버튼 2를 비활성화
        button1.SetActive(false);
        button2.SetActive(false);
        button3.SetActive(true);

        // "Finding a Game..." TextMeshProUGUI를 활성화
        findingText.gameObject.SetActive(true);
    }
}
