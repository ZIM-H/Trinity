using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.SceneManagement;
using TMPro;
public class DayTimer : MonoBehaviour
{
    private TextMeshProUGUI counter;
    private float time;
    // Start is called before the first frame update
    
    void Start()
    {
        time = 0.0f;
        counter = GetComponent<TextMeshProUGUI>();
        StartCoroutine(ExecuteAfterTime(40f));
    }
    
    IEnumerator ExecuteAfterTime(float time)
    {
        yield return new WaitForSeconds(time);
        VariableManager.Instance.nightTitleText = VariableManager.Instance.date.ToString() + "일차 종료\n";
        string nightText = "오늘 한 일\n";
        bool didSomething = false;
        if (VariableManager.Instance.inputFertilizerTry) {
            nightText += "중앙 정원에 비료를 투입했습니다.\n";
            didSomething = true;
        }
        if (VariableManager.Instance.makeFertilizerTry) {
            nightText += "비료를 생산했습니다.\n";
            didSomething = true;
        }
        if (VariableManager.Instance.roomNo == 1) {
            if (VariableManager.Instance.fertilizerUpgradeTry) {
                nightText += "비료 생산 개선 연구를 진행했습니다.\n";
                didSomething = true;
            }
            if (VariableManager.Instance.purifierTry) {
                nightText += "정수 시스템을 수리했습니다.\n";
                didSomething = true;
            }
        } else if (VariableManager.Instance.roomNo == 2) {
            if (VariableManager.Instance.carbonCaptureTry) {
                nightText += "이산화탄소 포집기 수리 작업을 했습니다.\n";
                didSomething = true;
            }
            if (VariableManager.Instance.taurineFilterTry) {
                nightText += "타우린을 정제했습니다.\n";
                didSomething = true;
            }
            if (VariableManager.Instance.farmTry) {
                nightText += "중앙 정원을 수리했습니다.\n";
                didSomething = true;
            }
        } else {
            if (VariableManager.Instance.observerTry) {
                nightText += "우주 항로를 관측했습니다.\n";
                didSomething = true;
            }
            if (VariableManager.Instance.asteroidDestroyTry) {
                nightText += "우주선에 다가오던 소행성을 파괴했습니다.\n";
                didSomething = true;
            }
            if (VariableManager.Instance.barrierDevTry) {
                nightText += "방어막 시스템 개발을 연구했습니다.\n";
                didSomething = true;
            }
        }
        if ( !didSomething ) {
            if ( VariableManager.Instance.power == 0 ) {
                nightText += "전날 과로로 인해 아무 일도 하지 못했습니다.\n";
            } else {
                nightText += "생존을 위한 아무 활동도 하지 않았습니다.\n";
            }
        }
        VariableManager.Instance.nightContextText = nightText.Remove(nightText.Length - 1);
        // 다른 스크립트에서 WebSocketClientManager의 인스턴스에 접근
        WebSocketClientManager webSocketManager = FindObjectOfType<WebSocketClientManager>();

        // WebSocketClientManager의 인스턴스가 존재하는지 확인
        if (webSocketManager != null) {
            // SendRoundEnd() 함수 호출
            webSocketManager.SendRoundEnd();
        } else {
            Debug.LogError("WebSocketClientManager not found in the scene.");
        }

        SceneManager.LoadScene("Night");
    }


    // Update is called once per frame
    void Update()
    {
        time += Time.deltaTime;
        int intTime = 60 - (int)time;
        counter.text = intTime.ToString();
    }
}
