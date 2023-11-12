using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Timer : MonoBehaviour
{

    GameObject fade;
    string[] roomNames = new string[] { "", "BiRyoRoom", "medicineroom", "controlRoom" };

    // Start is called before the first frame update
    
    void Start()
    {
        if(VariableManager.Instance.monsterDate == 0){
            VariableManager.Instance.monsterDate = Random.Range(1,13);
        }
        fade = GameObject.Find("Fade");
        StartCoroutine(ExecuteAfterTime(15.0f));
    }

    // Update is called once per frame
    void Update()
    {
    }
    
    IEnumerator ExecuteAfterTime(float time)
    {
        yield return new WaitForSeconds(time);
        if (VariableManager.Instance.gameOver) {
            // 다른 스크립트에서 WebSocketClientManager의 인스턴스를 얻어옴
            WebSocketClientManager webSocketManager = FindObjectOfType<WebSocketClientManager>();

            // WebSocketClientManager의 인스턴스가 존재하는지 확인
            if (webSocketManager != null)
            {
                // toggleWebSocketConnection 함수 호출
                webSocketManager.ToggleWebSocketConnection();
            }
            else
            {
                Debug.LogError("WebSocketClientManager not found in the scene.");
            }
        }
        else {
            int roomNo = VariableManager.Instance.roomNo;
            string room = roomNames[roomNo];
            Debug.Log("여기부터 : "+roomNo);
            Debug.Log("여기부터 : "+room);
            fade.GetComponent<Fade>().FadeOut();
            yield return new WaitForSeconds(1.0f);
            SceneManager.LoadScene(room);
        }
    }
}
